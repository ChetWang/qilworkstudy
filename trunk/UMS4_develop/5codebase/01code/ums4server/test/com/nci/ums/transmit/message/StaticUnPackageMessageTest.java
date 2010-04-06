package com.nci.ums.transmit.message;

import junit.framework.TestCase;

import com.nci.ums.transmit.common.message.UnPackageMessage;
import com.nci.ums.transmit.util.StringCoding;

/**
 * <p>
 * 标题：StaticUnPackageMessageTest.java
 * </p>
 * <p>
 * 描述： 静态报文分析类单元测试类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-8-24
 * @version 1.0
 */
public class StaticUnPackageMessageTest extends TestCase {

	public StaticUnPackageMessageTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * @功能 测试报文长度是否合法
	 *
	 * @Add by ZHM 2009-8-24
	 */
	public void testIsRightLength(){
		boolean expected = true;
		boolean actual = UnPackageMessage.isRightLength(getMessage());
		assertEquals(expected, actual);
	}
	
	/**
	 * @功能 测试检校报文检校码是否合法
	 *
	 * @Add by ZHM 2009-8-24
	 */
	public void testIsRightCs(){
		boolean expected = true;
		boolean actual = UnPackageMessage.isRightCs(getMessage());
		assertEquals(expected, actual);
	}

	/**
	 * @功能 测试获取应用逻辑地址
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public void testGetManufacturerAddress() {
		byte[] expected = new byte[] { (byte) 0xA1, (byte) 0xB1 };
		byte[] actual = UnPackageMessage
				.getManufacturerAddressBytes(getMessage());
		assertEquals(StringCoding.byte2hex(expected), StringCoding
				.byte2hex(actual));
	}

	/**
	 * @功能 测试获取应用下终端逻辑地址
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public void testGetConsumerAddress() {
		byte[] expected = new byte[] { (byte) 0xC1, (byte) 0xD1 };
		byte[] actual = UnPackageMessage.getConsumerAddressBytes(getMessage());
		assertEquals(StringCoding.byte2hex(expected), StringCoding
				.byte2hex(actual));
	}

	/**
	 * @功能 测试获取数据域长度
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public void testGetDataLength() {
		int expected = 11;
		int actual = UnPackageMessage.getDataLength(getMessage());
//		Res.log(Res.DEBUG, "xx");
//		Res.logExceptionTrace(new Exception());
//		Res.getConnection();
		assertEquals(expected, actual);
	}

	/**
	 * @功能 测试获取数据域
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public void testGetData() {
		String expected = "I am zhouhm";
		byte[] actual = UnPackageMessage.getData(getMessage());
		assertEquals(expected, new String(actual));
	}

	/**
	 * @功能 测试获取控制码
	 *
	 * @Add by ZHM 2009-8-24
	 */
	public void testGetControlCode() {
		byte expected = (byte) 0x96;
		byte actual = UnPackageMessage.getControlCode(getMessage());
		assertEquals(expected, actual);
	}

	/**
	 * @功能 测试获取报文发送方向
	 *
	 * @Add by ZHM 2009-8-24
	 */
	public void testIsToTerminal() {
		boolean expected = false;
		boolean actual = UnPackageMessage.isToTerminal(getMessage());
		assertEquals(expected, actual);
	}

	/**
	 * @功能 测试获取控制码下的功能码
	 *
	 * @Add by ZHM 2009-8-24
	 */
	public void testGetFunctionCode() {
		byte expected = (byte) 0x16;
		byte actual = UnPackageMessage.getFunctionCode(getMessage());
		assertEquals(expected, actual);
	}

	/**
	 * @功能 测试获取报文是否为异常报文
	 *
	 * @Add by ZHM 2009-8-24
	 */
	public void testGetIsErrorCode() {
		boolean expected = false;
		boolean actual = UnPackageMessage.isErrorMessage(getMessage());
		assertEquals(expected, actual);
	}

	/**
	 * @功能 测试获取报文流水号
	 *
	 * @Add by ZHM 2009-8-24
	 */
	public void testGetFSEQ() {
		// byte[] expected = new byte[]{(byte)0xA0, (byte)0XDE};
		// byte[] actual = StaticUnPackageMessage.getFSEQ(getMessage());
		int expected = 122;
		int actual = UnPackageMessage.getFSEQ(getMessage());
		assertEquals(expected, actual);
	}

	/**
	 * @功能 测试将字节数组转换成16进制字符串
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public void testStringCoding() {
		byte[] b = getMessage();
		String str = StringCoding.byte2hex(b);
		assertEquals("68A1B1C1D1A07E68960B004920616D207A686F75686D6516", str);
	}

	/**
	 * @功能 生成简单报文
	 * @return
	 *
	 * @Add by ZHM 2009-8-24
	 */
	private byte[] getMessage() {
		byte[] b = new byte[] { 104, -95, -79, -63, -47, -96, 126, 104, -106,
				11, 0, 73, 32, 97, 109, 32, 122, 104, 111, 117, 104, 109, 101,
				22 };
		return b;
	}

}
