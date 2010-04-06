package com.nci.ums.test.transmit;

import com.nci.ums.transmit.client.DataReceivedHandler;
import com.nci.ums.transmit.client.TransmitClient;
import com.nci.ums.transmit.client.sender.TransmitByteSender;
import com.nci.ums.transmit.common.TransmitData;
import com.nci.ums.transmit.common.message.ControlCode;
import com.nci.ums.transmit.common.message.UnPackageMessage;

/**
 * 终端接入测试
 * @author Qil.Wong
 *
 */
public class ClientSideTransmitClient {

	public static void main(String[] xxx) throws Exception {
		//接入部署在本机（localhost）的UMS转发平台，终端对应端口为10235，终端号1009
		final TransmitClient client = new TransmitClient("localhost", 10235,
				1009, ControlCode.DIRECTION_FROM_TREMINAL);
		//创建数据接收响应器
		DataReceivedHandler receiveH = new DataReceivedHandler() {

			public void onReceived(TransmitData[] atransmitdata) {
				for (int i = 0; i < atransmitdata.length; i++) {
					byte[] b = atransmitdata[i]
							.getOneFrameUserDataWithoutUserCommand();
					System.out.println(new String(b));
					System.out.println(atransmitdata[i].getUserCommand());
				}
				// 返回处理结果，终端处理完后，需要将结果反馈给服务器
				TransmitByteSender sender = new TransmitByteSender(client,
						1001, "服务端请求完成".getBytes(), true);
				try {
					sender.setFseq(UnPackageMessage.getFSEQ(atransmitdata[0]
							.getOneFrameData()));
					System.out.println("发送回执反馈");
					sender.send(99, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void failedReceivedMultiple(TransmitData[] atransmitdata) {
				System.err.println(atransmitdata);

			}
		};
		//注册数据接收响应器
		client.registerDataReceivedHandler(receiveH);
		//连接转发平台
		client.connect();
		//登陆转发平台
		client.login();
	}

}
