package nci.gps.util;

import junit.framework.TestCase;

public class CodingTest extends TestCase {

	public final void testBin2hex() {
		String content = "����������EDF%&^%#_|~";
		System.out.println(CharCoding.bin2hex(content));
	}

	public final void testHex2bin() {
		String content = "����������EDF%&^%#_|~";
		content = CharCoding.bin2hex(content);
		System.out.println(content);
		System.out.println(CharCoding.hex2bin(content));
	}

	public final void testByte2hex() {
		String content = "����������EDF%&^%#_|~";
		byte[] b = content.getBytes();
		System.out.println(CharCoding.byte2hex(b));
	}

	public final void testHex2byte() {
		String content = "����������EDF%&^%#_|~";
		byte[] b = content.getBytes();
		content = CharCoding.byte2hex(b);
		System.out.println(content);
		b = content.getBytes();
		b = CharCoding.hex2byte(b);
		System.out.println(new String(b));
	}

}
