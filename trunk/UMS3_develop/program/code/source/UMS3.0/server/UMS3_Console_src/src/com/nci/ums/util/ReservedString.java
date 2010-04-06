/*
 * ReservedString.java
 *
 * Created on 2007-10-3, 10:15:43
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.nci.ums.v3.message.basic.BasicMsg;

/**
 * 
 * @author Qil.Wong
 */
public class ReservedString {

	// private static String reserve = "";
	private static Properties bundle;
	public static final int CHINA_MOBILE = 1;
	public static final int CHINA_UNICOM = 2;
	private static byte[] lock = new byte[0];

	static {
		synchronized (lock) {
			if (bundle == null) {
				try {
					bundle = new Properties();
					bundle
							.load(new ServletTemp()
									.getInputStreamFromFile("/resources/reserve.properties"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public static String getSMSReservedString_cm(int contentMode) {
		switch (contentMode) {
		case BasicMsg.BASICMSG_CONTENTE_MODE_GBK:
			return bundle.getProperty("SMSReserved_GBK_cm");
		case BasicMsg.BASICMSG_CONTENTE_MODE_8859_1:
			return " "+bundle.getProperty("SMSReserved_ISO_cm");
		default:
			return bundle.getProperty("SMSReserved_GBK_cm");
		}
	}
}