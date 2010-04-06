package com.nci.ums.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

	private static Properties emailProp;

	private static byte[] lock = new byte[0];

	public static void load() {
		synchronized (lock) {
			if (emailProp == null) {
				emailProp = new Properties();
				try {
					emailProp.load(new DynamicUMSStreamReader()
							.getInputStreamFromFile("/resources/email.props"));
				} catch (FileNotFoundException e) {
					Res.log(Res.ERROR, "邮件配置文件不存在.");
					Res.logExceptionTrace(e);
				} catch (IOException e) {
					Res.log(Res.ERROR, "邮件配置文件读取失败.");
					Res.logExceptionTrace(e);
				}
			}
		}
	}

	public static Properties getEmailProperty() {
		return emailProp;
	}

	public static String getUMSAdminEmail() {
		if (emailProp == null)
			emailProp = getEmailProperty();
		return emailProp.getProperty("UMSAdminEmail");
	}
	
	public static synchronized String getProperty(String propName,String propFile){
		Properties pro = new Properties();
		try {
			pro.load(new DynamicUMSStreamReader()
								.getInputStreamFromFile(propFile));
		} catch(Exception e){
			e.printStackTrace();
		}
		return pro.getProperty(propName);
		
	}
}
