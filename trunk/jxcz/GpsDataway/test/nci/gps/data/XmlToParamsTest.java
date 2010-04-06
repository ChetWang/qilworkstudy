package nci.gps.data;

import java.util.ArrayList;

import junit.framework.TestCase;

public class XmlToParamsTest extends TestCase {

	public final void testGetParamList() {
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\"?><ResultSet><row id=\"0\"><col name=\"ID\">175</col><col name=\"GDID\">12</col><col name=\"LCZT\">05</col><col name=\"KSSJ\">2007-12-18 18:09:53</col> <col name=\"JSSJ\">2007-12-18 18:09:53</col><col name=\"CLBMID\">0330000000000</col>  <col name=\"CLBMMC\">浙江省电力公司</col><col name=\"CLRID\">00181585</col><col name=\"CLRXM\">陈理</col><col name=\"CLYJ\"></col><col name=\"XYCLBMID\">0330000000000</col><col name=\"XYCLBMMC\">浙江省电力公司</col> </row></ResultSet>";
		XmlToParams xtp = new XmlToParams(xml);
		ArrayList paramList = xtp.getParamList();
		for (int i = 0, size = paramList.size(); i < size; i++) {
			ArrayList[] rowList = (ArrayList[])paramList.get(i);
			System.out.println(rowList[0]);
			System.out.println(rowList[1]);
			String[] aNames = new String[rowList[0].size()];
			rowList[0].toArray(aNames);
			String[] aValues = new String[rowList[1].size()];
			rowList[1].toArray(aValues);
			System.out.println("");
		}
	}

}
