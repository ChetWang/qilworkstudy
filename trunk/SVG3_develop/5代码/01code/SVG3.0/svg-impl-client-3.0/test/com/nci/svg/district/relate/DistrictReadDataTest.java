package com.nci.svg.district.relate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.nci.svg.district.relate.DistrictReadData;
import com.nci.svg.district.relate.bean.DistrictAreaBean;

import junit.framework.TestCase;

public class DistrictReadDataTest extends TestCase {

	public DistrictReadDataTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testReadData() {
		String filepath = "D:\\WORK\\SVG30\\svg-impl-server-3.0\\test\\demoAreaData.xml";
		String data = readDataString(filepath);
//		System.out.println(data);
		DistrictReadData test = new DistrictReadData();
		DistrictAreaBean area = test.readData(data);
		System.out.println(area.getProperties());
	}

	/**
	 * 读取数据源
	 * 
	 * @return
	 */
	public static String readDataString(String filepath) {
		StringBuffer data = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filepath), "UTF-8"));
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
