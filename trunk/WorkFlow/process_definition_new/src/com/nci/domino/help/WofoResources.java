package com.nci.domino.help;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ����������Դ��
 * 
 * @author Qil.Wong
 * 
 */
public class WofoResources {

	/**
	 * ��ֵ��Ӧ
	 */
	protected static Properties commandProp = null;

	static {
		if (commandProp == null) {
			commandProp = new Properties();
			try {
				InputStream is = WofoResources.class
						.getResourceAsStream("/resources/resources.properties");
				commandProp.load(is);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * ���ݼ�ֵ��ȡ���ʻ��ļ���Ӧ��ʵ��ֵ
	 * 
	 * @param key
	 * @return
	 */
	public static String getValueByKey(String key) {
		return commandProp.getProperty(key);

	}

}
