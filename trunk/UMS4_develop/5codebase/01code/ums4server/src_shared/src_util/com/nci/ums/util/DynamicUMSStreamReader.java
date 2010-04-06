package com.nci.ums.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 动态配置文件流读取器
 * 
 * @author Qil.Wong
 */
public class DynamicUMSStreamReader {

	public InputStream getInputStreamFromFile(String relativePath) {
		return getClass().getResourceAsStream(relativePath);
	}
	
	 public URL getURL(String relativePath){
    	 return getClass().getResource(relativePath);
    }

	public Properties getProperties(String relativePath) {
		InputStream is = getClass().getResourceAsStream(relativePath);
		Properties p = new Properties();
		try {
			p.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			Res.logExceptionTrace(e);
		}
		return p;
	}
}
