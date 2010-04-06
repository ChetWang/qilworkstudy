package com.nci.ums.transmit.common;

import java.text.SimpleDateFormat;

public class TransmitUID {

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

	static int maxSerial = 10000;

	static SimpleDateFormat defaultCurrentFmt = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	private synchronized static String getCurrentTimeStr() {
		return defaultCurrentFmt.format(new java.util.Date());
	}

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
		return new StringBuffer().append(getCurrentTimeStr()).append(uidSide)
				.append(serial).toString();

	}
}
