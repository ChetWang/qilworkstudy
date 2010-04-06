package com.nci.ums.util.uid;

import com.nci.ums.util.Util;

public class UMSUID {

	/**
	 * UMS����������
	 */
	public static final int UMSUID_SERVER = 0;
	/**
	 * UMS WEB����ģ������
	 */
	public static final int UMSUID_WEB_MANAGE = 1;
	/**
	 * UMSͨ��ģ������
	 */
	public static final int UMSUID_GENERAL_MODULE = 2;

	static int serial = 0;

	static int maxSerial = 100000;

	/**
	 * ��ȡUMSϵͳ��ȫ��ΨһUID
	 * 
	 * @param side
	 *            ��������Ϊ0��web�����Ϊ1��ͨ��ģ��Ϊ2��
	 * @return
	 */
	public synchronized static String getUMSUID(int uidSide) {
		serial++;
		if (serial >= maxSerial)
			serial = 0;
		StringBuffer sb = new StringBuffer().append(Util.getCurrentTimeStr())
				.append("-").append(uidSide).append("-").append(autoAppend());
		return sb.toString();
	}

	private static String autoAppend() {
		String serialS = String.valueOf(serial);
		int len = serialS.length();
		int appendLen = 5 - len;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < appendLen; i++) {
			sb.append("0");
		}
		return sb.append(serialS).toString();
	}
}
