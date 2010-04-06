package nci.gps.message;

import nci.gps.util.CharCoding;
import junit.framework.TestCase;

public class PackageMessageTest extends TestCase {

	public final void testPackageMessage1(){
		PackageMessage pm = new PackageMessage();
		
		byte[] logicalAddress = new byte[] { (byte) 0xA1, (byte) 0xB1,
				(byte) 0xC1, (byte) 0xD1 };
		String strData = "I am zhouhm";
		byte[] data = strData.getBytes();
		
		pm.setLogicalAddress(logicalAddress);
		pm.setMstaSeq(3, 122);	// 11, 01111010
		pm.setControlCode(true, false, 0x16);
		pm.setData(data);
//		pm.setDataLength(41029);
		
		byte[] b = pm.packageMessage();
		/**
		 * b的十六进制表示为
		 * 68 A1B1C1D1 A01E 68 21 0B00 4920616D207A686F75686D 90 16
		 */
		System.out.println(CharCoding.byte2hex(b));
	}
	
	public final void testGetIseq(){
		byte iseq = 6;
		iseq <<= 5;
		System.out.println(iseq);
	}
	
//	public final void testPackageMessage() {
//		byte[] logicalAddress = new byte[] { (byte) 0xA1, (byte) 0xB1,
//				(byte) 0xC1, (byte) 0xD1 };
//		byte[] msta_seq = new byte[] { (byte) 0x01, (byte) 0x02 };
//		byte controlCode = 0x22;
//		byte[] dataLength = new byte[] { (byte) 0x00, (byte) 0x02 };
//		byte[] data = new byte[] { (byte) 0x11, (byte) 0x12 };
//		PackageMessage pm = new PackageMessage();
//		pm.packageMessage(logicalAddress, msta_seq, controlCode, dataLength,
//				data);
//	}
//	
//	
//	public final void testGetMstaSeq(){
//		PackageMessage pm = new PackageMessage();
//		int iseq = 0;
//		int fseq = 122;	// 0x7A
//		// b[0]=10100000:A0,b[1]=00011110:1E
//		byte[] b = pm.getMstaSeq(iseq, fseq);
//		System.out.println(CharCoding.byte2hex(b));
//	}
//	
//	public final void testGetMstaSeq1(){
//		PackageMessage pm = new PackageMessage();
//		int iseq = 7;
//		int fseq = 108;	// 0x6C
//		// b[0]=00100000:20,b[1]=11111011:FB
//		byte[] b = pm.getMstaSeq(iseq, fseq);
//		System.out.println(CharCoding.byte2hex(b));
//	}
//	
//	public final void testGetCongrolCode(){
//		PackageMessage pm = new PackageMessage();
//		boolean direction = false;
//		boolean errFlag = false;
//		int functionCode = 33;
//		byte b = pm.getCongrolCode(direction, errFlag, functionCode);
//		byte[] bs = {b};
//		// b=00100001:21
//		System.out.println(CharCoding.byte2hex(bs));
//	}
	
	/**
	 * 测试获取终端命令1请求的字节组
	 */
	public final void testGetMethod1(){
		byte[] logicalAddress = new byte[] { (byte) 0x96, (byte) 0x01,
				(byte) 0x00, (byte) 0x01 };
		byte[] data = new byte[]{0x01};
		
		PackageMessage pm = new PackageMessage();
		pm.setLogicalAddress(logicalAddress);
		pm.setMstaSeq(0, 122);	// 11, 01111010
		pm.setControlCode(true, false, 0x0F);
		pm.setData(data);
		
		byte[] b = pm.packageMessage();
		/**
		 * b的十六进制表示为
		 * 68 96010001 A01E 68 8F 0300 010001 BA 16
		 */
		System.out.println(CharCoding.byte2hex(b));
	}
	
	/**
	 * 测试获取终端命令1请求的字节组
	 */
	public final void testGetMethod2(){
		byte[] logicalAddress = new byte[] { (byte) 0x96, (byte) 0x01,
				(byte) 0x00, (byte) 0x01 };
		byte[] data = new byte[]{0x02};
		
		PackageMessage pm = new PackageMessage();
		pm.setLogicalAddress(logicalAddress);
		pm.setMstaSeq(0, 122);	// 11, 01111010
		pm.setControlCode(true, false, 0x0F);
		pm.setData(data);
		
		byte[] b = pm.packageMessage();
		System.out.println(CharCoding.byte2hex(b));
	}
	
	/**
	 * 测试获取郎新服务
	 * B请求命令
	 */
	public final void tetGetMethodB(){
		byte[] logicalAddress = new byte[] { (byte) 0x96, (byte) 0x01,
				(byte) 0x00, (byte) 0x01 };
		PackageMessage pm = new PackageMessage();
		pm.setLogicalAddress(logicalAddress);
		pm.setMstaSeq(0, 122);	// 11, 01111010
		pm.setControlCode(true, false, 0x0F);
		
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\"?><ResultSet><row id=\"0\"><col name=\"CS\">2</col><col name=\"DDXCSJ\">2008-04-14 09:19:08</col><col name=\"CLRY\">00181585</col><col name=\"CLBM\">0330000000000</col><col name=\"XGCLBM\">0330000000000</col><col name=\"JDSJ\">2008-11-11 12:00:00</col><col name=\"CLSJ\">2008-08-12 09:48:13</col><col name=\"CLYJ\"></col><col name=\"GDH\">66</col></row></ResultSet>";
		byte[] params = xml.getBytes();
		byte methodNo = 0x0B;
		byte[] data = new byte[params.length+1];
		data[0] = methodNo;
		for(int i=0,size=params.length; i<size; i++){
			data[i+1] = params[i];
		}
		pm.setData(data);
		
		byte[] b = pm.packageMessage();
		System.out.println(CharCoding.byte2hex(b));
	}

	
	/**
	 * 测试获取终端命令c请求的字节组
	 */
	public final void testGetMethodC(){
		byte[] logicalAddress = new byte[] { (byte) 0x96, (byte) 0x01,
				(byte) 0x00, (byte) 0x01 };
		byte[] data = new byte[]{0x0C};
		
		PackageMessage pm = new PackageMessage();
		pm.setLogicalAddress(logicalAddress);
		pm.setMstaSeq(0, 122);	// 11, 01111010
		pm.setControlCode(true, false, 0x0F);
		pm.setData(data);
		
		byte[] b = pm.packageMessage();
		/**
		 * b的十六进制表示为
		 * 68 96010001 A01E 68 8F 0300 0C0001 C5 16
		 */
		System.out.println(CharCoding.byte2hex(b));
	}

	/**
	 * 测试获取终端命令d请求的字节组
	 */
	public final void testGetMethodD(){
		byte[] logicalAddress = new byte[] { (byte) 0x96, (byte) 0x01,
				(byte) 0x00, (byte) 0x01 };
		PackageMessage pm = new PackageMessage();
		pm.setLogicalAddress(logicalAddress);
		pm.setMstaSeq(0, 122);	// 11, 01111010
		pm.setControlCode(true, false, 0x0F);
		
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\"?><ResultSet><row id=\"0\"><col name=\"ZDID\">1</col><col name=\"CLID\">1846</col><col name=\"CZRYID\">00181585</col><col name=\"JSYID\">1</col><col name=\"KSSJ\">2008-08-12 09:43:41</col><col name=\"FLAG\">1</col></row></ResultSet>";
		byte[] params = xml.getBytes();
		byte methodNo = 0x0D;
		byte[] data = new byte[params.length+1];
		data[0] = methodNo;
		for(int i=0,size=params.length; i<size; i++){
			data[i+1] = params[i];
		}
		pm.setData(data);
		
		byte[] b = pm.packageMessage();
		System.out.println(CharCoding.byte2hex(b));
	}

}
