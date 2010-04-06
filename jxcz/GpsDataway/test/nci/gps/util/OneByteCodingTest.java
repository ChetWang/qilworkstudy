package nci.gps.util;

import junit.framework.TestCase;

public class OneByteCodingTest extends TestCase {

	public final void testOneByteCoding() {
		byte b = 0x0f;
		ByteCoding obc = new ByteCoding(b);
		obc.setBitTrue(4);
		System.out.println(obc.getData());
		obc.setBitFalse(4);
		System.out.println(obc.getData());
		System.out.println(obc.getBit(3));
		System.out.println(obc.getBit(4));
	}

	public final void testGetData() {
		
	}

	public final void testSetBitTrue() {
		fail("Not yet implemented"); // TODO
	}

}
