package com.nci.ums.test.transmit;

import com.nci.ums.transmit.client.DataReceivedHandler;
import com.nci.ums.transmit.client.TransmitClient;
import com.nci.ums.transmit.client.sender.TransmitByteSender;
import com.nci.ums.transmit.common.TransmitData;
import com.nci.ums.transmit.common.message.ControlCode;
import com.nci.ums.transmit.common.message.UnPackageMessage;

/**
 * �ն˽������
 * @author Qil.Wong
 *
 */
public class ClientSideTransmitClient {

	public static void main(String[] xxx) throws Exception {
		//���벿���ڱ�����localhost����UMSת��ƽ̨���ն˶�Ӧ�˿�Ϊ10235���ն˺�1009
		final TransmitClient client = new TransmitClient("localhost", 10235,
				1009, ControlCode.DIRECTION_FROM_TREMINAL);
		//�������ݽ�����Ӧ��
		DataReceivedHandler receiveH = new DataReceivedHandler() {

			public void onReceived(TransmitData[] atransmitdata) {
				for (int i = 0; i < atransmitdata.length; i++) {
					byte[] b = atransmitdata[i]
							.getOneFrameUserDataWithoutUserCommand();
					System.out.println(new String(b));
					System.out.println(atransmitdata[i].getUserCommand());
				}
				// ���ش��������ն˴��������Ҫ�����������������
				TransmitByteSender sender = new TransmitByteSender(client,
						1001, "������������".getBytes(), true);
				try {
					sender.setFseq(UnPackageMessage.getFSEQ(atransmitdata[0]
							.getOneFrameData()));
					System.out.println("���ͻ�ִ����");
					sender.send(99, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void failedReceivedMultiple(TransmitData[] atransmitdata) {
				System.err.println(atransmitdata);

			}
		};
		//ע�����ݽ�����Ӧ��
		client.registerDataReceivedHandler(receiveH);
		//����ת��ƽ̨
		client.connect();
		//��½ת��ƽ̨
		client.login();
	}

}
