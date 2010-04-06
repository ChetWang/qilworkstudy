package com.nci.ums.test.transmit;

import com.nci.ums.transmit.client.RequestHandler;
import com.nci.ums.transmit.client.TransmitClient;
import com.nci.ums.transmit.client.sender.TransmitByteSender;
import com.nci.ums.transmit.common.TransmitData;
import com.nci.ums.transmit.common.message.ControlCode;

public class ServerSideTransmitClient {

	public static void main(String[] xxx) throws Exception {
		//接入部署在本机（localhost）的UMS转发平台，应用服务对应端口为10234，应用号1001
		TransmitClient client = new TransmitClient("localhost", 10234, 1001,
				ControlCode.DIRECTION_FROM_APPLICATION);
		//连接转发平台
		client.connect();
		//登陆转发平台
		client.login();
		String content = "hello+好哦分";
		//创建字节发送器，将指定的内容转化为字节后进行发送
		TransmitByteSender sender = new TransmitByteSender(client, 1009,
				content.getBytes(),false);
		// TransmitFileSender sender = new TransmitFileSender(client, 1009, new
		// File("D:/Instantiations.license"));
		
		//命令响应器，通过指定的用户命令（如99），将数据转发给另一个终端，终端根据用户命令（如99）及发送过来的数据
		//进行一系列的操作并将结果返回，这里RequestHandler的requestFinished处理返回的结果。
		//用户命令是服务和终端相互协商下的自定义命令（长度为2个字节，值范围0-65535），和UMS转发平台无关，用户两个系统间的操作映射
		RequestHandler requestH = new RequestHandler(client, 99, 60 * 1000) {

			public void requestFinished(TransmitData[] atransmitdata, int i) {
				System.out.println("finish");
				for (int x = 0; x < atransmitdata.length; x++) {
					byte[] b = atransmitdata[x]
							.getOneFrameUserDataWithoutUserCommand();
					System.out.println(new String(b));
					System.out.println(atransmitdata[x].getUserCommand());
				}
			}

			public void requestFailedTimeout() {
				System.err.println("err");

			}
		};
		sender.send(99, requestH);
	}

}
