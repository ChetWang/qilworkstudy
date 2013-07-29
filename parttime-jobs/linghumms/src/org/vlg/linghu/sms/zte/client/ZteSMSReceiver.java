package org.vlg.linghu.sms.zte.client;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vlg.linghu.SPConfig;
import org.vlg.linghu.mybatis.bean.SmsReceiveMessage;
import org.vlg.linghu.mybatis.bean.SmsSendMessage;
import org.vlg.linghu.mybatis.bean.SmsSendMessageExample;
import org.vlg.linghu.mybatis.mapper.SmsReceiveMessageMapper;
import org.vlg.linghu.mybatis.mapper.SmsSendMessageMapper;
import org.vlg.linghu.vac.VACNotifyHandler;

import com.zte.smgw.api.sgip.message.SGIPDeliver;
import com.zte.smgw.api.sgip.message.SGIPReport;
import com.zte.smgw.api.sgip.message.body.SGIPDeliverBody;
import com.zte.smgw.api.sgip.message.body.SGIPReportBody;
import com.zte.smgw.api.sgip.message.body.SGIPSequenceNo;
import com.zte.smgw.api.sgip.server.SGIPRecieveMsg;
import com.zte.smgw.api.sgip.server.SGIPServer;
import com.zte.smgw.api.sgip.server.SGIPServerInitInfo;

public class ZteSMSReceiver {

	private static final Logger logger = LoggerFactory
			.getLogger(ZteSMSReceiver.class);

	@Autowired
	SmsReceiveMessageMapper smsReceiveMessageMapper;
	
	@Autowired
	SmsSendMessageMapper smsSendMessageMapper;

	public void start() {

		SGIPServerInitInfo info = new SGIPServerInitInfo();
		info.IP = "0.0.0.0";
		info.port = 6888;
		info.userName = SPConfig.getUserName();
		info.passWord = SPConfig.getPassword();
		SGIPServer server = SGIPServer.getInstance();
		server.init(info);
		try {
			server.start();
		} catch (Exception e) {
			logger.error("", e);
		}
		while (true) {
			try {
				// 判断是否有消息
				if (server.MessageLength() > 0) {
					SGIPRecieveMsg msg = server.ReceiveMessage();

					// 判断当前消息是deliver 或者是report消息
					// deliver：1 report：2
					if (msg.messageType == 1) {
						// deliver
						SGIPDeliver deliver = (SGIPDeliver) msg.obj;
						SGIPDeliverBody body = deliver.getBody();
						int msgCoding = body.getMessageCoding();
						String encoding = "GBK";
						// if(msgCoding=)
						byte[] msgBytes = body.getMessageContent();
						String user = body.getUserNumber();
						String spnumber = body.getSPNumber();
						SmsReceiveMessage srm = new SmsReceiveMessage();
						srm.setIsok(true);
						srm.setReceiveAddtime(new Date());
						srm.setReceiveText(new String(msgBytes, encoding));
						srm.setServiceid(spnumber);
						srm.setUserId(user);
						smsReceiveMessageMapper.insertSelective(srm);
						logger.info("收到用户上行短信deliver message: {}",
								deliver.toString()+VACNotifyHandler.getBeanInfo(body));
					} else if (msg.messageType == 2) {
						//FIXME 这个处理应该和发送处理放到单线程池下保证顺序，否则这里先执行的话，数据库里查不到正确的msgid
						SGIPReport report = (SGIPReport) msg.obj;
						SGIPReportBody body = report.getBody();
						String user = body.getUserNumber();
						SGIPSequenceNo seq = body.getSubmitSequenceNumber();
						String msgId=seq.getNode()+""+seq.getTime()+""+seq.getNumber();
						SmsSendMessageExample ex = new SmsSendMessageExample();
						ex.createCriteria().andMsgidEqualTo(msgId).andUserIdEqualTo(user);
						SmsSendMessage ssm = new SmsSendMessage();
						ssm.setMsgid(msgId);
						ssm.setSendStatus(body.getErrorCode());
						smsSendMessageMapper.updateByExampleSelective(ssm, ex);
						logger.info("收到短信网关状态报告消息report message: {}",
								report.toString());
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ZteSMSReceiver().start();
	}

}
