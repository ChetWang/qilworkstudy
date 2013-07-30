package org.vlg.linghu.sms;

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

import com.zte.smgw.api.sgip.client.SGIPClient;
import com.zte.smgw.api.sgip.client.SGIPClientInitInfo;
import com.zte.smgw.api.sgip.client.SGIPRsp;
import com.zte.smgw.api.sgip.message.SGIPSubmit;
import com.zte.smgw.api.sgip.message.SGIPSubmitResp;
import com.zte.smgw.api.sgip.message.body.SGIPSequenceNo;
import com.zte.smgw.api.sgip.message.body.SGIPSubmitBody;

public class SMSSender extends Thread {

	private static final Logger logger = LoggerFactory
			.getLogger(SMSSender.class);

	public static final int SEND_READY = 0;
	public static final int SEND_SENDING = 19999;

	@Autowired
	SmsSendMessageMapper smsSendMessageMapper;
	@Autowired
	VacReceiveMessageMapper vacReceiveMessageMapper;

	public SMSSender() {
		setDaemon(true);
		setName("SMS-Sender");
	}

	public void run() {
		logger.info("SMS Sender started");
		SmsSendMessageExample ex = new SmsSendMessageExample();
		ex.createCriteria().andSendStatusEqualTo(SEND_READY);
		while (true) {
			try {
				List<SmsSendMessage> msgs = smsSendMessageMapper
						.selectByExample(ex);
				if (msgs.size() > 0) {
					logger.info("获取到{}条待发彩信", msgs.size());
					SGIPClientInitInfo info = new SGIPClientInitInfo();
					info.IP = SPConfig.getSmsGateway();
					info.port = SPConfig.getSmsGatewayPort();
					info.userName = SPConfig.getUserName();
					info.passWord = SPConfig.getPassword();
					info.maxLink = 1;
					final SGIPClient client = SGIPClient.getInstance();
					client.init(info);

					for (final SmsSendMessage msg : msgs) {
						Runnable run = new Runnable() {
							public void run() {
								try {
									msg.setSendStatus(SEND_SENDING);
									logger.info("发送短信给{}" + msg.getUserId());
									smsSendMessageMapper
											.updateByPrimaryKeySelective(msg);
									SGIPSubmit submit = new SGIPSubmit();
									SGIPSubmitBody body = submit.getBody();
									setInitailBody(body, msg);
									SGIPRsp rsphandler6 = new SGIPRsp();
									SGIPSequenceNo seq = new SGIPSequenceNo();
									String msgId = seq.getNode() + ""
											+ seq.getTime() + ""
											+ seq.getNumber();
									submit.getHead().setSequenceNo(seq);
									client.sendSubmit(submit, rsphandler6);
									SGIPSubmitResp resp = rsphandler6
											.waitForSGIPSubmitResp();
									msg.setMsgid(msgId);
									msg.setSendStatus(resp.getBody()
											.getResult() + 50000);
									logger.info("发送短信给" + msg.getUserId()
											+ ", 已送至网关, msgid=" + msgId
											+ ", result="
											+ resp.getBody().getResult()+",sendstatus="+msg.getSendStatus());
									smsSendMessageMapper
											.updateByPrimaryKeySelective(msg);
								} catch (Exception e) {
									logger.error("", e);
								}
							}
						};
						SingleThreadPool.execute(run);
					}
				} else {
					sleep(SPConfig.getMsgDetectDuration());
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	private void setInitailBody(SGIPSubmitBody body, SmsSendMessage msg) {
		body.setCorpId(SPConfig.getVaspId());
		body.setSPNumber(SPConfig.getSpNumber());
		body.setUserCount(1);
		body.setFeeType(1);
		body.setFeeValue("0");
		body.setGivenValue("0");
		body.setAgentFlag(0);
		body.setMorelatetoMTFlag(0);
		body.setPriority(0);
		body.setReportFlag(1);
		// body.setTP_pid(0);
		// body.setTP_udhi(0);
		body.setMessageCoding(15);
		body.setMessageType(0);

		String serviceType = getServiceId(msg);
		body.setServiceType(serviceType);
		body.setUserNumber(msg.getUserId());
		body.setMessageContent(msg.getSendText().getBytes());
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
