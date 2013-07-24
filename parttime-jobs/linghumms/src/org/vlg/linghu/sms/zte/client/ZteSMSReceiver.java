package org.vlg.linghu.sms.zte.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zte.smgw.api.sgip.message.SGIPDeliver;
import com.zte.smgw.api.sgip.message.SGIPReport;
import com.zte.smgw.api.sgip.server.SGIPRecieveMsg;
import com.zte.smgw.api.sgip.server.SGIPServer;
import com.zte.smgw.api.sgip.server.SGIPServerInitInfo;

public class ZteSMSReceiver {
	
	private static final Logger logger =LoggerFactory.getLogger(ZteSMSReceiver.class);

	public void start() {
		SGIPServerInitInfo info = new SGIPServerInitInfo();
		info.IP = "123.159.200.55";
		info.port = 6888;
		info.userName = "xiangyu";
		info.passWord = "xy28187688";
		SGIPServer server = SGIPServer.getInstance();
		server.init(info);
		try {
			server.start();
		} catch (Exception e) {
			logger.error("",e);
		} 
		while (true) {
			// 判断是否有消息
			if (server.MessageLength() > 0) {
				SGIPRecieveMsg msg = server.ReceiveMessage();

				// 判断当前消息是deliver 或者是report消息
				// deliver：1 report：2
				if (msg.messageType == 1) {
					// deliver
					SGIPDeliver deliver = (SGIPDeliver) msg.obj;
					logger.info("收到用户上行消息deliver message: {}",deliver.toString());
				} else if (msg.messageType == 2) {
					// report
					SGIPReport report = (SGIPReport) msg.obj;
					logger.info("收到网关状态报告消息report message: {}",report.toString());
				}
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
