package com.nci.ums.util.uid;

import com.nci.ums.util.Util;

public class UMSUID {

	/**
	 * UMS服务器类型
	 */
	public static final int UMSUID_SERVER = 0;
	/**
	 * UMS WEB管理模块类型
	 */
	public static final int UMSUID_WEB_MANAGE = 1;
	/**
	 * UMS通用模块类型
	 */
	public static final int UMSUID_GENERAL_MODULE = 2;

	static int serial = 0;

	static int maxSerial = 100000;

	/**
	 * 获取UMS系统下全局唯一UID
	 * 
	 * @param side
	 *            服务器端为0，web管理端为1，通用模块为2；
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
