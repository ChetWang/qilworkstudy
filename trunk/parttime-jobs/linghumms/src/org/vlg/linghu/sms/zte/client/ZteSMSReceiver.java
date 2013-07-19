package org.vlg.linghu.sms.zte.client;

import java.io.IOException;
import java.net.UnknownHostException;

import com.zte.smgw.api.sgip.message.SGIPDeliver;
import com.zte.smgw.api.sgip.message.SGIPReport;
import com.zte.smgw.api.sgip.server.SGIPRecieveMsg;
import com.zte.smgw.api.sgip.server.SGIPServer;
import com.zte.smgw.api.sgip.server.SGIPServerInitInfo;

public class ZteSMSReceiver {

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
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			// �ж��Ƿ�����Ϣ
			if (server.MessageLength() > 0) {
				SGIPRecieveMsg msg = server.ReceiveMessage();

				// �жϵ�ǰ��Ϣ��deliver ������report��Ϣ
				// deliver��1 report��2
				if (msg.messageType == 1) {
					// deliver
					SGIPDeliver deliver = (SGIPDeliver) msg.obj;
					System.out.println("deliver: "
							+ deliver);
				} else if (msg.messageType == 2) {
					// report
					SGIPReport report = (SGIPReport) msg.obj;
					System.out.println("report: "
							+ report);
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
