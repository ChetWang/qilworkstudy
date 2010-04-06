package com.nci.ums.transmit.common;

import java.text.SimpleDateFormat;

public class TransmitUID {

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

	static int maxSerial = 10000;

	static SimpleDateFormat defaultCurrentFmt = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	private synchronized static String getCurrentTimeStr() {
		return defaultCurrentFmt.format(new java.util.Date());
	}

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
		return new StringBuffer().append(getCurrentTimeStr()).append(uidSide)
				.append(serial).toString();

	}
}
