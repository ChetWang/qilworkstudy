package com.nci.ums.transmit.message;


import junit.framework.TestCase;

import com.nci.ums.transmit.common.message.PackageMessage;
import com.nci.ums.transmit.common.message.UnPackageMessage;

import com.nci.ums.transmit.util.StringCoding;

public class PackageMessageTest extends TestCase {

	public PackageMessageTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPackageMessage() {
		int manufacturer = 1001;
		int consumer = 1002;
		String data = "I am zhouhm";

		PackageMessage pm = new PackageMessage();
		pm.setManufacturerAddress(manufacturer);
		pm.setConsumerAddress(consumer);
		pm.setMstaSeq(35, 1, 0, 0);
		pm.setControlCode(false, false, 0x24);
		pm.setData(data.getBytes());

		byte[] expected = pm.packageMessage();
		byte[] actual = { (byte) 0x68, (byte) 0xE9, 0x03, (byte) 0xEA, 0x03,
				0x23, 0x01, 0x00, 0x68, 0x24, 0x0B, 0x00, 73, 32, 97, 109, 32,
				122, 104, 111, 117, 104, 109, 0x00, 0x16 };
		for (int i = 0, size = actual.length; i < size - 2; i++) {
			actual[size - 2] += actual[i];
		}

		assertEquals(StringCoding.byte2hex(expected), StringCoding
				.byte2hex(actual));

		int uManufacturer = UnPackageMessage.getManufactureAddress(expected);
		assertEquals(manufacturer, uManufacturer);
		int uConsumer = UnPackageMessage.getConsumerAddress(expected);
		assertEquals(consumer, uConsumer);
		boolean direction = UnPackageMessage.isToTerminal(expected);
		assertEquals(true, direction);
		boolean isRightLength = UnPackageMessage.isRightLength(expected);
		assertEquals(true, isRightLength);
		boolean isCs = UnPackageMessage.isRightCs(expected);
		assertEquals(true, isCs);
		byte[] dataBytes = UnPackageMessage.getData(expected);
		String strData = new String(dataBytes);
		assertEquals(data, strData);
	}
	
	public void testMath(){
		int i = 3;
		double d = 2.33;
		assertEquals(3, (int)Math.ceil(d));
	}
}
