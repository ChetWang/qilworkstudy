package com.nci.ums.transmit.util;

/**
 * <p>
 * ���⣺StringCoding.java
 * </p>
 * <p>
 * ������ String����ֽ����ַ�֮���ת��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-8-21
 * @version 1.0
 */
public class StringCoding {

	/**
	 * @���� �������ַ���ת����ʮ������ֵ�ַ���
	 * @param bin
	 *            String �������ַ���
	 * @return String ʮ�������ַ���
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
	 * @���� ʮ�������ַ���ת���ɶ������ַ���
	 * @param hex
	 *            String ʮ�������ַ���
	 * @return String �������ַ���
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
	 * @���� ���ֽ���ת����16�����ַ���
	 * @param bytes
	 *            byte[] �ֽ�����
	 * @return String ʮ�������ַ���
	 * 
	 * @Add by ZHM 2009-8-21
	 */
	public static String byte2hex(byte[] bytes) {
		StringBuffer hs = new StringBuffer();
		String tmp = "";
		for (int n = 0; n < bytes.length; n++) {
			// ����ת��ʮ�����Ʊ�ʾ
			tmp = (java.lang.Integer.toHexString(bytes[n] & 0XFF));
			if (tmp.length() == 1) {
				hs.append("0").append(tmp);
			} else {
				hs.append(tmp);
			}
		}
		tmp = null;
		return hs.toString().toUpperCase(); // ת�ɴ�д
	}

	/**
	 * @���� ��ʮ�������ֽ�����ת���������ֽ�����
	 * @param hexBytes
	 *            byte[] ʮ����������
	 * @return �����ֽ�����
	 * 
	 * @Add by ZHM 2009-8-21
	 */
	public static byte[] hex2byte(byte[] hexBytes) {
		if ((hexBytes.length % 2) != 0) {
			throw new IllegalArgumentException("���Ȳ���ż��");
		}
		byte[] b2 = new byte[hexBytes.length / 2];
		for (int n = 0; n < hexBytes.length; n += 2) {
			String item = new String(hexBytes, n, 2);
			// ��λһ�飬��ʾһ���ֽ�,��������ʾ��16�����ַ�������ԭ��һ�������ֽ�
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		hexBytes = null;
		return b2;
	}
}