package nci.gps.message;

import nci.gps.util.CharCoding;
import junit.framework.TestCase;

public class StaticUnPackageMessageTest extends TestCase {

	public final void testGetLogicalAddress() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetDataLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetData() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetControlCode() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetFunctionCode() {
		byte[] password = {0x01,0x02,0x03};
		byte[] b = CommonPackage.getLoginPackage(password);
		System.out.println(CharCoding.byte2hex(b));
		
		System.out.println(CharCoding
				.byte2hex(new byte[] { StaticUnPackageMessage
						.getFunctionCode(b) }));
	}
	
	public final void testSubMessage(){
		byte[] b = {104, -106, 0, 0, 2, 96, 0, 104, 40, 22, 0, 43, 56, 54, 49, 53, 57, 48, 54, 54, 50, 50, 52, 57, 55, 73, 32, 97, 109, 32, 122, 104, 109, -120, 22};
		byte bByte = 0x68;
		b = StaticUnPackageMessage.subMessage(b, bByte);
		System.out.println(CharCoding.byte2hex(b));
	}
	
	public final void testGetFSEQ(){
		// **********************
		// 生成一个数据帧
		// **********************
		for(int i=0; i<128; i++){
		byte[] logicalAddress = new byte[] { (byte) 0x96, (byte) 0x01,
					(byte) 0x00, (byte) 0x01 };
			byte[] data = new byte[] { 0x0C, 0x00, 0x01 };
			PackageMessage pm = new PackageMessage();
			pm.setLogicalAddress(logicalAddress);
			pm.setMstaSeq(0, i); // 11, 01111010
			pm.setControlCode(true, false, 0x0F);
			pm.setData(data);
			byte[] b = pm.packageMessage();
			// 测试获取流水号功能
			int no = StaticUnPackageMessage.getFSEQ(b);
			System.out.println("流水号：" + no);
		}
	}

}
