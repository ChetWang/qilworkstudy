package com.nci.ums.transmit.util;

/**
 * <p>
 * 标题：StringCoding.java
 * </p>
 * <p>
 * 描述： String与各种进制字符之间的转换
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-8-21
 * @version 1.0
 */
public class StringCoding {

	/**
	 * @功能 二进制字符串转换成十六进制值字符串
	 * @param bin
	 *            String 二进制字符串
	 * @return String 十六进制字符串
	 * 
	 * @Add by ZHM 2009-8-21
	 */
	public static String bin2hex(String bin) {
		char[] digital = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer("");
		byte[] bs = bin.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(digital[bit]);
			bit = bs[i] & 0x0f;
			sb.append(digital[bit]);
		}
		return sb.toString();
	}

	/**
	 * @功能 十六进制字符串转换成二进制字符串
	 * @param hex
	 *            String 十六进制字符串
	 * @return String 二进制字符串
	 * 
	 * @Add by ZHM 2009-8-21
	 */
	public static String hex2bin(String hex) {
		String digital = "0123456789ABCDEF";
		char[] hex2char = hex.toCharArray();
		byte[] bytes = new byte[hex.length() / 2];
		int temp;
		for (int i = 0; i < bytes.length; i++) {
			temp = digital.indexOf(hex2char[2 * i]) * 16;
			temp += digital.indexOf(hex2char[2 * i + 1]);
			bytes[i] = (byte) (temp & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * @功能 将字节流转换成16进制字符串
	 * @param bytes
	 *            byte[] 字节数组
	 * @return String 十六进制字符串
	 * 
	 * @Add by ZHM 2009-8-21
	 */
	public static String byte2hex(byte[] bytes) {
		StringBuffer hs = new StringBuffer();
		String tmp = "";
		for (int n = 0; n < bytes.length; n++) {
			// 整数转成十六进制表示
			tmp = (java.lang.Integer.toHexString(bytes[n] & 0XFF));
			if (tmp.length() == 1) {
				hs.append("0").append(tmp);
			} else {
				hs.append(tmp);
			}
		}
		tmp = null;
		return hs.toString().toUpperCase(); // 转成大写
	}

	/**
	 * @功能 将十六进制字节数组转换成正常字节数组
	 * @param hexBytes
	 *            byte[] 十六进制数组
	 * @return 正常字节数组
	 * 
	 * @Add by ZHM 2009-8-21
	 */
	public static byte[] hex2byte(byte[] hexBytes) {
		if ((hexBytes.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[hexBytes.length / 2];
		for (int n = 0; n < hexBytes.length; n += 2) {
			String item = new String(hexBytes, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		hexBytes = null;
		return b2;
	}
}