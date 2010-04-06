package nci.gps.message;

import nci.gps.util.CharCoding;
import junit.framework.TestCase;

public class CommonPackageTest extends TestCase {

	public final void testLoginPackage() {
		byte[] password = {0x01,0x02,0x03};
		byte[] b = CommonPackage.getLoginPackage(password);
		/**
		 * b��ʮ�����Ʊ�ʾΪ
		 * 68 00000000 2000 68 21 0300 010203 ** 16
		 */
		System.out.println(CharCoding.byte2hex(b));
	}
	
	public final void testLogoutPackage(){
		byte[] b = CommonPackage.getLogoutPackage();
		/**
		 * b��ʮ�����Ʊ�ʾΪ
		 * 68 00000000 2000 68 22 0000 12 16
		 */
		System.out.println(CharCoding.byte2hex(b));
	}

	public final void testHeartBeatPackage(){
		byte[] b = CommonPackage.getHeartBeatPackage();
		/**
		 * b��ʮ�����Ʊ�ʾΪ
		 * 68 00000000 2000 68 24 0000 14 16
		 */
		System.out.println(CharCoding.byte2hex(b));
	}
}
