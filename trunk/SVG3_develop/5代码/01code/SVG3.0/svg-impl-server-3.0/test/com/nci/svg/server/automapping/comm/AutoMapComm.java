package com.nci.svg.server.automapping.comm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class AutoMapComm {
	/**
	 * 读取数据源
	 * 
	 * @return
	 */
	public static String readDataString(String filepath) {
		StringBuffer data = new StringBuffer();
		String filename = System.getProperty("user.dir") + filepath;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename), "UTF-8"));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				data.append(tempString);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return data.toString();
	}

	public static String readDataString(String filepath, boolean isWeb) {
		StringBuffer data = new StringBuffer();
		String filename = null;
		if (isWeb) {
			filename = filepath;
		} else {
			filename = System.getProperty("user.dir") + filepath;
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename), "UTF-8"));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				data.append(tempString);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return data.toString();
	}

}
