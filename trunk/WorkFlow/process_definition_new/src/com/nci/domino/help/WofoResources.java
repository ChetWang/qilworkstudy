package com.nci.domino.help;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 中文配置资源类
 * 
 * @author Qil.Wong
 * 
 */
public class WofoResources {

	/**
	 * 键值对应
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
	 * 根据键值获取国际化文件对应的实际值
	 * 
	 * @param key
	 * @return
	 */
	public static String getValueByKey(String key) {
		return commandProp.getProperty(key);

	}

}
