package nci.gps.data;

import nci.gps.util.ReadConfig;
import junit.framework.TestCase;

public class DataUtilTest extends TestCase {

	public final void testGetResultSetString() {
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		// ������������
		String sql = "select SYMBOL,StandardName,Code,Name from TB_GPS_CZ_LS_DMB";
		DataUtil du = new DataUtil("");
		System.out.println(du.getResultSet(sql));
	}

	public final void testGetResultSetStringString() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGenerateXML() {
		fail("Not yet implemented"); // TODO
	}

}
