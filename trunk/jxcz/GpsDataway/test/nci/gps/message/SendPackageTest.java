package nci.gps.message;

import javax.jms.JMSException;

import nci.gps.message.jms.Producer001;
import nci.gps.util.CharCoding;
import nci.gps.util.ReadConfig;
import nci.gps.util.SerialFSEQ;

public class SendPackageTest {

	public final void testSendStrMessage() {
		
	}

	public static void main(String[] sss) {
		ReadConfig rc = ReadConfig.getInstance();
		// 获取登录用字节数组
//		byte[] password = { 0x01, 0x02, 0x03 };
//		byte[] b = CommonPackage.getLoginPackage(password);
		// 获取心跳字节数组
//		byte[] b= CommonPackage.getHeartBeatPackage();
		// 请求发送短信消息
		PackageMessage pm = new PackageMessage();
		byte[] logicalAddress = {(byte) 0x96,0x00,0x00,0x02};
		pm.setLogicalAddress(logicalAddress);
		pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setControlCode(false, false, 40);
		String strData = "+8615906622497I am zhm";
		byte[] data = strData.getBytes();
		pm.setData(data);
		
		byte[]b = pm.packageMessage();
		
		
		String strB = CharCoding.byte2hex(b);
		System.out.println(strB);

		
		// 发送消息给分包服务器
		// SendPackage sendPackage = new SendPackage();
		try {
			// Producer001 p = Producer001.getInstance();
			// p.start();
			Producer001.getInstance().addBytesIntoSendQueue(b);
			// sendPackage.sendBytesMessage(b);
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
