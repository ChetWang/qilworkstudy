package nci.gps.message.jms;

import javax.jms.JMSException;

import nci.gps.message.PackageMessage;
import nci.gps.util.CharCoding;

public class Consumer001Test2 {

	// 发送消息
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

			// 延时500毫秒之后发送消息
			Thread.sleep(500);

			// 创建终端请求消息帧
			PackageMessage pm = new PackageMessage();
			// 用户自定义格式
			pm.setControlCode(true, false, 15);
			// 逻辑好为0x01,0x02,0x03,0x04
			pm.setLogicalAddress(new byte[] { 0x01, 0x02, 0x03, 0x04 });
			// 设置数据域
			byte[] data = new byte[] { 0x01, (byte) 0xAC, 0x01 };
			pm.setData(data);
			// 设置主站地址与命令序号
			pm.setMstaSeq(0, 122);

			byte[] message = pm.packageMessage();
			System.out.println("请求数据帧：" + CharCoding.byte2hex(message));
			producer.produceMessage(message);
			//	            
			// 延时500毫秒之后停止接受消息
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
