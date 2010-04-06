package nci.gps.message.jms;

import javax.jms.JMSException;

import nci.gps.message.PackageMessage;
import nci.gps.util.CharCoding;

public class Consumer001Test2 {

	// ������Ϣ
	public void produceMessage(byte[] message) throws JMSException, Exception {
		Producer001 pro = Producer001.getInstance();
		pro.start();
		pro.addBytesIntoSendQueue(message);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Consumer001 consumer;
		try {
			consumer = Consumer001.getInstance();
			consumer.start();
			Consumer001Test2 producer = new Consumer001Test2();

			// ��ʱ500����֮������Ϣ
			Thread.sleep(500);

			// �����ն�������Ϣ֡
			PackageMessage pm = new PackageMessage();
			// �û��Զ����ʽ
			pm.setControlCode(true, false, 15);
			// �߼���Ϊ0x01,0x02,0x03,0x04
			pm.setLogicalAddress(new byte[] { 0x01, 0x02, 0x03, 0x04 });
			// ����������
			byte[] data = new byte[] { 0x01, (byte) 0xAC, 0x01 };
			pm.setData(data);
			// ������վ��ַ���������
			pm.setMstaSeq(0, 122);

			byte[] message = pm.packageMessage();
			System.out.println("��������֡��" + CharCoding.byte2hex(message));
			producer.produceMessage(message);
			//	            
			// ��ʱ500����֮��ֹͣ������Ϣ
			Thread.sleep(5000);
			// consumer.destroy();
		} catch (NoSubjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
