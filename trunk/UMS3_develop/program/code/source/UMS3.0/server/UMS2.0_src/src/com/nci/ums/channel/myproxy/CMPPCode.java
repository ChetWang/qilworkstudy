package com.nci.ums.channel.myproxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.nci.ums.util.Res;
import com.nci.ums.util.ServletTemp;

public class CMPPCode {

	static Properties codeMap;

	static {
		codeMap = new Properties();
		try {
			// InputStream is = new
			// ServletTemp().getInputStreamFromFile("/resources/CMPP_ackCode.properties");
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					new ServletTemp().getURL(
							("/resources/CMPP_ackCode.properties")).getFile())));
			String s = "";
			while ((s = reader.readLine()) != null) {
				String[] x = s.split("=");
				if (x.length > 1) {
					codeMap.put(x[0], x[1]);
				}
			}
		} catch (IOException e) {
			Res.logExceptionTrace(e);
		}
	}

	public static String getDesciption(String code) {
		String s = (String) codeMap.get(code);
		if (s == null) {
			String code2 = code.substring(0, 2);
			s = (String) codeMap.get(code2);
		}
		if (s == null)
			Res.log(Res.ERROR, "回执代码：" + code + " 未存在对应描述");
		return s;
	}

}
