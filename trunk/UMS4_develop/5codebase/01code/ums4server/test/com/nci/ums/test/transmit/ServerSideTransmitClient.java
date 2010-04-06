package com.nci.ums.test.transmit;

import com.nci.ums.transmit.client.RequestHandler;
import com.nci.ums.transmit.client.TransmitClient;
import com.nci.ums.transmit.client.sender.TransmitByteSender;
import com.nci.ums.transmit.common.TransmitData;
import com.nci.ums.transmit.common.message.ControlCode;

public class ServerSideTransmitClient {

	public static void main(String[] xxx) throws Exception {
		//���벿���ڱ�����localhost����UMSת��ƽ̨��Ӧ�÷����Ӧ�˿�Ϊ10234��Ӧ�ú�1001
		TransmitClient client = new TransmitClient("localhost", 10234, 1001,
				ControlCode.DIRECTION_FROM_APPLICATION);
		//����ת��ƽ̨
		client.connect();
		//��½ת��ƽ̨
		client.login();
		String content = "hello+��Ŷ��";
		//�����ֽڷ���������ָ��������ת��Ϊ�ֽں���з���
		TransmitByteSender sender = new TransmitByteSender(client, 1009,
				content.getBytes(),false);
		// TransmitFileSender sender = new TransmitFileSender(client, 1009, new
		// File("D:/Instantiations.license"));
		
		//������Ӧ����ͨ��ָ�����û������99����������ת������һ���նˣ��ն˸����û������99�������͹���������
		//����һϵ�еĲ�������������أ�����RequestHandler��requestFinished�����صĽ����
		//�û������Ƿ�����ն��໥Э���µ��Զ����������Ϊ2���ֽڣ�ֵ��Χ0-65535������UMSת��ƽ̨�޹أ��û�����ϵͳ��Ĳ���ӳ��
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
