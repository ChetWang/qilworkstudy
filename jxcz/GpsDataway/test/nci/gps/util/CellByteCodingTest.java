package nci.gps.util;

import junit.framework.TestCase;

public class CellByteCodingTest extends TestCase {

	public final void testBin2Byte() {
		
		CellByteCoding cellByteCoding = new CellByteCoding();
		cellByteCoding.setBitValue(0, true);
		cellByteCoding.setBitValue(1, true);
		cellByteCoding.setBitValue(4, true);
		cellByteCoding.setBitValue(5, true);
		byte b = cellByteCoding.getByte();
		byte[] bs = new byte[]{b};
		System.out.println(CharCoding.byte2hex(bs));
	}

	public final void testFindIndex() {
		fail("Not yet implemented"); // TODO
	}

	public final void testUniteBytes() {
		fail("Not yet implemented"); // TODO
	}

}
