package org.vlg.linghu.sms.huawei.client;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vlg.linghu.SPConfig;
import org.vlg.linghu.SingleThreadPool;
import org.vlg.linghu.mybatis.bean.SmsReceiveMessage;
import org.vlg.linghu.mybatis.bean.SmsSendMessage;
import org.vlg.linghu.mybatis.bean.SmsSendMessageExample;
import org.vlg.linghu.mybatis.mapper.SmsReceiveMessageMapper;
import org.vlg.linghu.mybatis.mapper.SmsSendMessageMapper;
import org.vlg.linghu.vac.VACNotifyHandler;

import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUserReportMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUserReportRepMessage;
import com.huawei.insa2.util.Cfg;
import com.huawei.smproxy.SGIPSMProxy;

import demo30.SendReqThread30;

public class MySGIPReceiveProxy extends SGIPSMProxy {

	@Autowired
	SmsReceiveMessageMapper smsReceiveMessageMapper;

	@Autowired
	SmsSendMessageMapper smsSendMessageMapper;

	private static final Logger logger = LoggerFactory
			.getLogger(MySGIPReceiveProxy.class);

	public MySGIPReceiveProxy() throws IOException {
		super(new Cfg(MySGIPReceiveProxy.class
				.getResource("/huawei-sgip.xml")
				.toString()).getArgs("SGIPConnect"));
	}
	
	public void start(){
		startService(SPConfig.getSmsReceiveIp(), SPConfig.getSmsReceivePort());
		logger.info("短信接收服务启动, "+SPConfig.getSmsReceiveIp()+":"+SPConfig.getSmsReceivePort());
	}

	public SGIPMessage onDeliver(SGIPDeliverMessage msg) {
		// this.demo.ProcessRecvDeliverMsg(msg);
		try {
			logger.info("收到用户上行短信: " + msg.getUserNumber()
					+ " ------ " + new String(msg.getMsgContent(), "GBK"));
			SmsReceiveMessage srm = new SmsReceiveMessage();
			srm.setIsok(true);
			srm.setReceiveAddtime(new Date());
			srm.setReceiveText(new String(msg.getMsgContent(), "GBK"));
//			srm.setServiceid();
			srm.setUserId(msg.getUserNumber());
			smsReceiveMessageMapper.insertSelective(srm);
			
		} catch (Exception e) {
			logger.error("", e);
		}
		return super.onDeliver(msg);
	}

	public SGIPMessage onReport(final SGIPReportMessage msg) {

		Runnable run = new Runnable() {
			public void run() {
				try {
					String s = VACNotifyHandler.getBeanInfo(msg);
					String msgId = "" + msg.getSrcNodeId() + msg.getTimeStamp()
							+ msg.getSequenceId();
					logger.info("收到短信状态报告：" + s);
					SmsSendMessageExample ex = new SmsSendMessageExample();
					ex.createCriteria().andMsgidEqualTo(msgId)
							.andUserIdEqualTo(msg.getUserNumber());
					SmsSendMessage ssm = new SmsSendMessage();
					ssm.setMsgid(msgId);
					ssm.setSendStatus(msg.getErrorCode() + 90000);
					smsSendMessageMapper.updateByExampleSelective(ssm, ex);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		};
		SingleThreadPool.execute(run);
		return super.onReport(msg);
	}

	public SGIPMessage onUserReport(SGIPUserReportMessage msg) {
		logger.info("收到用户报告userreport: "+VACNotifyHandler.getBeanInfo(msg));
		return new SGIPUserReportRepMessage(0);
	}

	public void OnTerminate() {
		// this.demo.Terminate();
	}
}
