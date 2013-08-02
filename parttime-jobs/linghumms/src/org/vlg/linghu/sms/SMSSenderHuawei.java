package org.vlg.linghu.sms;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vlg.linghu.SPConfig;
import org.vlg.linghu.SingleThreadPool;
import org.vlg.linghu.mybatis.bean.SmsSendMessage;
import org.vlg.linghu.mybatis.bean.SmsSendMessageExample;
import org.vlg.linghu.mybatis.bean.VacReceiveMessage;
import org.vlg.linghu.mybatis.bean.VacReceiveMessageExample;
import org.vlg.linghu.mybatis.mapper.SmsSendMessageMapper;
import org.vlg.linghu.mybatis.mapper.VacReceiveMessageMapper;
import org.vlg.linghu.sms.huawei.client.HuaweiSMSTest;
import org.vlg.linghu.sms.huawei.client.MySGIPSMProxy;

import com.huawei.insa2.comm.sgip.message.SGIPSubmitMessage;
import com.huawei.insa2.comm.sgip.message.SGIPSubmitRepMessage;
import com.huawei.insa2.util.Args;
import com.huawei.insa2.util.Cfg;

public class SMSSenderHuawei extends Thread {

	private static final Logger logger = LoggerFactory
			.getLogger(SMSSenderHuawei.class);

	public static final int SEND_READY = 0;
	public static final int SEND_SENDING = 19999;

	@Autowired
	SmsSendMessageMapper smsSendMessageMapper;
	@Autowired
	VacReceiveMessageMapper vacReceiveMessageMapper;

	public SMSSenderHuawei() {
		setDaemon(true);
		setName("SMS-Sender");
	}

	public void run() {
		logger.info("SMS Sender Huawei started");
		SmsSendMessageExample ex = new SmsSendMessageExample();
		ex.createCriteria().andSendStatusEqualTo(SEND_READY);
		while (true) {
			try {
				final List<SmsSendMessage> msgs = smsSendMessageMapper
						.selectByExample(ex);
				if (msgs.size() > 0) {
					logger.info("获取到{}条待发彩信", msgs.size());

					Runnable run = new Runnable() {
						public void run() {
							MySGIPSMProxy smProxy = null;
							try {
								Cfg config = new Cfg(HuaweiSMSTest.class
										.getResource("/huawei-sgip.xml")
										.toString());
								Args a = config.getArgs("SGIPConnect");
								smProxy = new MySGIPSMProxy(a);
								boolean result = smProxy.connect(
										SPConfig.getUserName(),
										SPConfig.getPassword());
								if (!result) {
									logger.warn("登陆短信网关失败！");
								}
							} catch (IOException e) {
								logger.error("初始化短信网关失败 " + e.getMessage());
							}
							for (final SmsSendMessage msg : msgs) {
								msg.setSendStatus(SEND_SENDING);
								logger.info("发送短信给{}", msg.getUserId());
								smsSendMessageMapper
										.updateByPrimaryKeySelective(msg);
								if (smProxy != null) {
									try {
										// 为防止msgid在没更新时却收到状态报告，这里要在单线程中执行，状态报告也一样要在单线程中更新
										SGIPSubmitMessage submit = createSubmit(msg);
										SGIPSubmitRepMessage resp = (SGIPSubmitRepMessage) smProxy
												.send(submit);
										msg.setMsgid("" + submit.getSrcNodeId()
												+ submit.getTimeStamp()
												+ submit.getSequenceId());
										msg.setSendStatus(resp.getResult() + 50000);
										logger.info("发送短信给" + msg.getUserId()
												+ ", 已送至网关, msgid="
												+ msg.getMsgid() + ", result="
												+ resp.getResult()
												+ ", sendstatus="
												+ msg.getSendStatus());
										smsSendMessageMapper
												.updateByPrimaryKeySelective(msg);
									} catch (Exception e) {
										logger.error("", e);
									}
								}
							}
						}
					};
					SingleThreadPool.execute(run);
					sleep(SPConfig.getMsgSendDuration());

				} else {
					sleep(SPConfig.getMsgDetectDuration());
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	private SGIPSubmitMessage createSubmit(SmsSendMessage msg) {
		String serviceType = getServiceId(msg);
		String corpId = SPConfig.getVaspId();
		String spNumber = SPConfig.getSpNumber();
		String chargeNumber = "";
		int feeType = 1;
		String feeValue = "0";
		String givenValue = "0";
		int agentFlag = 0;
		int morelatetoMTFlag = 0;
		int priority = 0;
		Date expireTime = null;
		Date scheduledTime = null;
		int reportFlag = 1;
		int TP_pid = 0;
		int TP_udhi = 0;
		int messageCoding = 15;
		int messageType = 0;
		String content = msg.getSendText();
		String reserve = "";
		return new SGIPSubmitMessage(spNumber, chargeNumber,
				new String[] { msg.getUserId() }, corpId, serviceType, feeType,
				feeValue, givenValue, agentFlag, morelatetoMTFlag, priority,
				expireTime, scheduledTime, reportFlag, TP_pid, TP_udhi,
				messageCoding, messageType, content.getBytes().length,
				content.getBytes(), reserve);
	}

	public String getServiceId(SmsSendMessage ms) {
		VacReceiveMessageExample ex = new VacReceiveMessageExample();
		ex.createCriteria().andUseridEqualTo(ms.getUserId());
		List<VacReceiveMessage> vacs = vacReceiveMessageMapper
				.selectByExample(ex);
		if (vacs.size() > 0) {
			return vacs.get(0).getProductid();
		}
		logger.warn("Cannot find serviceId for user " + ms.getUserId());
		return null;
	}

}
