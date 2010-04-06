package nci.gps.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import nci.gps.client.GpsClient;
import nci.gps.db.DBExcute;
import nci.gps.log.MsgLogger;

/**
 * @���ܣ����ݲ�����
 * @ʱ�䣺2008-07-23
 * @author ZHOUHM
 * 
 */
public class DataUtil {
	private String messageKey;	// ����֡Ψһ��ʶ
	public DataUtil(String messageKey){
		this.messageKey = messageKey;
	}

	/**
	 * ��ȡָ��sql��������� ��ʹ��XML��ʽ��ʽ��
	 * 
	 * @param sql
	 *            String SQL�������
	 * @return
	 */
	public String getResultSet(String sql) {
		return getResultSet(sql, "");
	}

	/**
	 * ��ȡָ��sql��������� ��ʹ��XML��ʽ��ʽ��
	 * 
	 * @param sql
	 *            String SQL�������
	 * @return
	 */
	public String getResultSet(String sql, String name) {
		String result = "";
//		System.out.println("SQL:" + sql);
		MsgLogger.log(MsgLogger.INFO, "SQL:" + sql);
		if (sql == null || sql.equals(""))
			return result;
		DBExcute dbe = new DBExcute();
		ResultSet rs = null;

		try {
			rs = dbe.ExecuteQuery(sql);
			result = generateXML(rs, name);
		} catch (Exception e) {
			result = e.toString();
			MsgLogger.logExceptionTrace("���ݲ����࣬ת��XMLʱ�������ݿ����", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				MsgLogger.logExceptionTrace("���ݲ����࣬ת��XMLʱ�����ر����ݼ�����", e);
				e.printStackTrace();
			}
			dbe.close();
		}
		return result;
	}

	/**
	 * ��ResultSetת���XML
	 * 
	 * @param rs
	 *            ResultSet ���ݼ�
	 * @param name
	 *            String ��������
	 * @return
	 * @throws SQLException
	 */
	public static String generateXML(final ResultSet rs, String name)
			throws SQLException {
		final StringBuffer buffer = new StringBuffer(1024 * 4);
		if (rs == null)
			return "";
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); // XML�ļ�ͷ
		if (name.equals("") || name == null)
			buffer.append("<ResultSet>");
		else
			buffer.append("<ResultSet id=\"").append(name).append("\">");
		ResultSetMetaData rsmd = rs.getMetaData(); // ���ݼ�������ͷ
		int colCount = rsmd.getColumnCount(); // ��ȡ������
		for (int id = 0; rs.next(); id++) { // ��һ��������
			// ��ʽΪrow id , col name, col context
			buffer.append("<row id=\"").append(id).append("\">");
			for (int i = 1; i <= colCount; i++) {
				int type = rsmd.getColumnType(i); // ��ȡ�ֶ�����
				buffer.append("<col name=\"" + rsmd.getColumnName(i) + "\">");
				buffer.append(getValue(rs, i, type));
				buffer.append("</col>");
			}
			buffer.append("</row>");
		}
		buffer.append("</ResultSet>");
		rs.close();
		return buffer.toString();
	}

	/**
	 * ��ȡ���ݼ�������ֵ
	 * 
	 * @param rs
	 *            ResultSet ���ݼ�
	 * @param colNum
	 *            int �к�
	 * @param type
	 *            int ��������
	 * @return
	 * @throws SQLException
	 */
	private static String getValue(final ResultSet rs, int colNum, int type)
			throws SQLException {
		switch (type) {
		case Types.ARRAY:
		case Types.BLOB:
		case Types.CLOB:
		case Types.DISTINCT:
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
		case Types.BINARY:
		case Types.REF:
		case Types.STRUCT:
			return "undefined";
		default: {
			Object value = rs.getObject(colNum);
			if (rs.wasNull() || (value == null))
				return ("");
			else
				return (value.toString());
		}
		}
	}

	/**
	 * �޸Ĺ�����Ϣ ���������xml�ַ����������ɶ�Ӧ��SQL���
	 * 
	 * @param xml
	 *            String xml�ַ���
	 * @return String[] SQL�������
	 */
	public String[] setGdInfo(String xml) {
		XmlToParams xtp = new XmlToParams(xml);
		ArrayList paramList = xtp.getParamList();

		int size = paramList.size();
		String[] sql = new String[size];
		for (int i = 0; i < size; i++) {
			ArrayList[] rowList = (ArrayList[]) paramList.get(i);
			String[] aNames = new String[rowList[0].size()];
			rowList[0].toArray(aNames);
			String[] aValues = new String[rowList[1].size()];
			rowList[1].toArray(aValues);
			String whereStr = "";
			for (int j = 0; j < aNames.length; j++) {
				if ("ID".equals(aNames[j])) {
					whereStr = " where ID='" + aValues[j] + "'";
				}
			}
			try {
				sql[i] = getSql("TB_GPS_CZ_GD", "update", aValues, aNames,
						whereStr, "", "");
			} catch (Exception e) {
				MsgLogger.logExceptionTrace("�޸Ĺ�����Ϣ����������SQL������", e);
				e.printStackTrace();
			}
		}
		return sql;
	}

	/**
	 * ���XML��������ֵ
	 * 
	 * @param xml
	 *            String xml�ַ���
	 * @return String[] ����ֵ����
	 */
	public String[] getKeyValue(String xml) {
		XmlToParams xtp = new XmlToParams(xml);
		ArrayList paramList = xtp.getParamList();

		int size = paramList.size();
		String[] returnValue = new String[size];
		int flag = 0;
		for (int i = 0; i < size; i++) {
			ArrayList[] rowList = (ArrayList[]) paramList.get(i);
			String[] aNames = new String[rowList[0].size()];
			rowList[0].toArray(aNames);
			String[] aValues = new String[rowList[1].size()];
			rowList[1].toArray(aValues);
			for (int j = 0; j < aNames.length; j++) {
				if ("ID".equals(aNames[j])) {
					returnValue[flag] = aValues[j];
					flag++;
				}
			}
		}
		return returnValue;
	}

	/**
	 * �������XMLƴװ��SQL���
	 * 
	 * @param tableName
	 *            String Ҫ���������ݱ����
	 * @param flag
	 *            String SQL������ͣ��ֱ�Ϊupdate��insert
	 * @param columnParam
	 *            String ����ֵ
	 * @param columnName
	 *            String �ֶ���
	 * @param whereStr
	 *            String �������
	 * @param keyName
	 *            String ��������
	 * @param value
	 *            String ����ֵ
	 * @return
	 * @throws Exception
	 */
	private String getSql(String tableName, String flag, String[] columnParam,
			String[] columnName, String whereStr, String keyName, String value)
			throws Exception {
		String returnValue = null;
		StringBuffer sb = new StringBuffer();
		int length = columnParam.length;
		// int style = 0;
		if (length > 0) {
			if ("update".equals(flag)) {
				sb.append("update ").append(tableName).append(" set ");
				for (int i = 0; i < length; i++) {
					// System.out.println(columnName[i]);
					if (i == length - 1) {
						if ("SLSJ".equals(columnName[i])
								|| "JDSJ".equals(columnName[i])
								|| "PCSJ".equals(columnName[i])
								|| "DDXCSJ".equals(columnName[i])
								|| "WCSJ".equals(columnName[i])
								|| "KSSJ".equals(columnName[i])
								|| "JSSJ".equals(columnName[i])
								|| "SJ".equals(columnName[i])) {

							sb.append(columnName[i]).append("=").append(
									"to_date('").append(columnParam[i]).append(
									"','YYYY-MM-DD hh24:mi:ss')");
						} else {
							sb.append(columnName[i]).append("='").append(
									columnParam[i]).append("'");
						}
					} else {
						if ("SLSJ".equals(columnName[i])
								|| "JDSJ".equals(columnName[i])
								|| "PCSJ".equals(columnName[i])
								|| "DDXCSJ".equals(columnName[i])
								|| "WCSJ".equals(columnName[i])
								|| "KSSJ".equals(columnName[i])
								|| "JSSJ".equals(columnName[i])
								|| "SJ".equals(columnName[i])) {

							sb.append(columnName[i]).append("=").append(
									"to_date('").append(columnParam[i]).append(
									"','YYYY-MM-DD hh24:mi:ss'),");
						} else if ("GDLX".equals(columnName[i])) {
							sb.append(columnName[i]).append("=").append(
									Integer.valueOf(columnParam[i]))
									.append(",");
						} else if ("ID".equals(columnName[i])) {

						} else {
							sb.append(columnName[i]).append("='").append(
									columnParam[i]).append("',");
						}
					}
				}
				if (whereStr != null && !whereStr.equals(""))
					sb.append(" ").append(whereStr);
				returnValue = sb.toString();
			} else if ("insert".equals(flag)) {
				sb.append("insert into ").append(tableName).append(" (");
				if (keyName != null && !keyName.equals(""))
					sb.append(keyName).append(",");
				for (int i = 0; i < length; i++) {
					if (i == length - 1) {
						// �������ڸ�ʽ���д���
						sb.append(columnName[i]);
					} else {
						sb.append(columnName[i]).append(",");
					}
				}
				sb.append(") values(");
				if (keyName != null && !keyName.equals("")
						&& !tableName.equals("TB_GPS_CZ_ZDDLXX"))
					sb.append(value).append(",");
				else
					sb.append("'").append(value).append("',");
				// sb.append("'").append(value).append("',");
				for (int j = 0; j < length; j++) {
					if (j == length - 1) {
						if ("SLSJ".equals(columnName[j])
								|| "JDSJ".equals(columnName[j])
								|| "PCSJ".equals(columnName[j])
								|| "DDXCSJ".equals(columnName[j])
								|| "WCSJ".equals(columnName[j])
								|| "JSSJ".equals(columnName[j])
								|| "KSSJ".equals(columnName[j])
								|| "SJ".equals(columnName[j])) {
							sb.append("to_date('").append(columnParam[j])
									.append("','YYYY-MM-DD hh24:mi:ss'")
									.append(")");
						} else {
							sb.append("'").append(columnParam[j]).append("'");
						}
					} else {
						if ("SLSJ".equals(columnName[j])
								|| "JDSJ".equals(columnName[j])
								|| "PCSJ".equals(columnName[j])
								|| "DDXCSJ".equals(columnName[j])
								|| "WCSJ".equals(columnName[j])
								|| "JSSJ".equals(columnName[j])
								|| "KSSJ".equals(columnName[j])
								|| "SJ".equals(columnName[j])) {
							sb.append("to_date('").append(columnParam[j])
									.append("','YYYY-MM-DD hh24:mi:ss'")
									.append("),");
						} else if ("GDLX".equals(columnName[j])) {
							sb.append(Integer.valueOf(columnParam[j])).append(
									",");
						} else {
							sb.append("'").append(columnParam[j]).append("',");
						}
					}
				}
				sb.append(")");

				returnValue = sb.toString();
			}
		}
		if (returnValue.equals("") || returnValue == null)
			throw new Exception("SQL���Ϊ��");
		System.out.println(returnValue);
		return returnValue;
	}

	/**
	 * ִ��sql���ݿ����
	 * 
	 * @param sql
	 *            String SQL���
	 * @return �Ƿ�����ɹ����ɹ������ַ�����1�������ɹ������ַ�����0��
	 */
	public String putSql(String sql) {
		int flag = -1;
		// ***********************
		// Ҫ���ն˽��յ���Ϣ��ȷ��֡
		// ***********************
//		try {
//			DBExcute dbe = new DBExcute();
//			dbe.beginTransaction();
//			TransactionPool.getInstance().addTransaction(messageKey, dbe, true);
//			try {
//				flag = dbe.ExecuteUpdate(sql);
//			} catch (SQLException e) {
//				MsgLogger.logExceptionTrace("ִ��SQL��䱨��SQL:" + sql, e);
//				e.printStackTrace();
//			}finally{
//				dbe.close();
//			}
//		} catch (SQLException e) {
//			MsgLogger.logExceptionTrace("������������+SQL:" + sql, e);
//		}
			
		// ***********************
		// ���õ��ն˽��յ���Ϣ��ȷ��֡
		// ***********************
		DBExcute dbe = new DBExcute();
		try {
			flag = dbe.ExecuteUpdate(sql);
			MsgLogger.log(MsgLogger.INFO, "SQL:" + sql);
		} catch (SQLException e) {
			MsgLogger.logExceptionTrace("ִ��SQL��䱨��SQL:" + sql, e);
			e.printStackTrace();
		} finally {
			dbe.close();
		}
		if (flag == -1)
			return "0";
		else
			return "1";
	}

	/**
	 * ����XML�����ļ�
	 * 
	 * @param name
	 *            String[] ��������
	 * @param value
	 *            String[] ����ֵ
	 * @return String xml�ַ���
	 */
	public String generateXML(String[] name, String[] value) {
		StringBuffer returnValue = new StringBuffer();
		returnValue.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); // XML�ļ�ͷ
		returnValue.append("<ResultSet>");
		for (int id = 0; id < name.length; id++) { // �ԷŻص�ȫ��������һ����
			returnValue.append("<row>");
			returnValue.append("<key>").append(name[id]).append("</key>");
			returnValue.append("<sign>").append(value[id]).append("</sign>");
			returnValue.append("</row>");
		}
		returnValue.append("</ResultSet>");
		return returnValue.toString();
	}

	/**
	 * ����XML�����ļ�
	 * 
	 * @param name
	 *            String ��������
	 * @param value
	 *            String ����ֵ
	 * @return String xml�ַ���
	 */
	public String generateXML(String name, String value) {
		StringBuffer returnValue = new StringBuffer();
		returnValue.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); // XML�ļ�ͷ
		returnValue.append("<ResultSet>");
		returnValue.append("<row>");
		returnValue.append("<key>").append(name).append("</key>");
		returnValue.append("<sign>").append(value).append("</sign>");
		returnValue.append("</row>");
		returnValue.append("</ResultSet>");
		return returnValue.toString();
	}

	/**
	 * insert������Ϣ
	 * 
	 * @param xml
	 *            String xml�ַ���
	 * @return String[] SQL�������
	 */
	public String[] setNewGdInfo(String xml) {
		XmlToParams xtp = new XmlToParams(xml);
		ArrayList paramList = xtp.getParamList();

		int size = paramList.size();
		String[] sql = new String[size];
		for (int i = 0; i < size; i++) {
			ArrayList[] rowList = (ArrayList[]) paramList.get(i);
			String[] aNames = new String[rowList[0].size()];
			rowList[0].toArray(aNames);
			String[] aValues = new String[rowList[1].size()];
			rowList[1].toArray(aValues);
			try {
				sql[i] = getSql("TB_GPS_CZ_GD", "insert", aValues, aNames, "",
						"ID", "S_TB_GPS_CZ_GD.nextval");
			} catch (Exception e) {
				MsgLogger.logExceptionTrace("�½�������Ϣ����������SQL������", e);
				e.printStackTrace();
			}
		}
		return sql;
	}

	/**
	 * ��������������Ϣ
	 * 
	 * @param xml
	 *            String xml�ַ���
	 * @return String SQL���
	 */
	public String setGdclgcInfo(String xml) {
		XmlToParams xtp = new XmlToParams(xml);
		ArrayList paramList = xtp.getParamList();

		int size = paramList.size();
		String sql = "";
		for (int i = 0; i < size; i++) {
			ArrayList[] rowList = (ArrayList[]) paramList.get(i);
			String[] aNames = new String[rowList[0].size()];
			rowList[0].toArray(aNames);
			String[] aValues = new String[rowList[1].size()];
			rowList[1].toArray(aValues);
			try {
				sql = getSql("TB_GPS_CZ_GDCLGC", "insert", aValues, aNames, "",
						"ID", "S_TB_GPS_CZ_GD.nextval");
			} catch (Exception e) {
				MsgLogger.logExceptionTrace("�½�����������Ϣ����������SQL������", e);
				e.printStackTrace();
			}
		}
		return sql;
	}

	/**
	 * ����������������
	 * 
	 * @param xml
	 *            String xml�ַ���
	 * @return String SQL���
	 */
	public String setClzbInfo(String xml) {
		XmlToParams xtp = new XmlToParams(xml);
		ArrayList paramList = xtp.getParamList();

		int size = paramList.size();
		String sql = "";
		for (int i = 0; i < size; i++) {
			ArrayList[] rowList = (ArrayList[]) paramList.get(i);
			String[] aNames = new String[rowList[0].size()];
			rowList[0].toArray(aNames);
			String[] aValues = new String[rowList[1].size()];
			rowList[1].toArray(aValues);
			try {
				sql = getSql("TB_GPS_CZ_CLZB", "insert", aValues, aNames, "",
						"ID", "S_TB_GPS_CZ_CLZB.nextval");
			} catch (Exception e) {
				MsgLogger.logExceptionTrace("�½���������������Ϣ����������SQL������", e);
				e.printStackTrace();
			}
		}
		return sql;
	}

	/**
	 * �����ն˵�½��Ϣ
	 * 
	 * @param xml String xml�ַ���
	 * @param value String ʱ��
	 * @return String SQL���
	 */
	public String setZddlxxInfo(String xml, String value) {
		XmlToParams xtp = new XmlToParams(xml);
		ArrayList paramList = xtp.getParamList();

		int size = paramList.size();
		String sql = "";
		for (int i = 0; i < size; i++) {
			ArrayList[] rowList = (ArrayList[]) paramList.get(i);
			String[] aNames = new String[rowList[0].size()];
			rowList[0].toArray(aNames);
			String[] aValues = new String[rowList[1].size()];
			rowList[1].toArray(aValues);
			try {
				sql = getSql("TB_GPS_CZ_ZDDLXX", "insert", aValues, aNames, "",
						"ID", value);
			} catch (Exception e) {
				MsgLogger.logExceptionTrace("�����ն˵�¼��Ϣ����������SQL������", e);
				e.printStackTrace();
			}
		}
		return sql;
	}

	/**
	 * �޸��ն˵�½��Ϣ
	 * 
	 * @param xml String xml�ַ���
	 * @return String SQL�������
	 */
	public String[] updateZddlxxInfo(String xml) {
		XmlToParams xtp = new XmlToParams(xml);
		ArrayList paramList = xtp.getParamList();

		int size = paramList.size();
		String[] sql = new String[size];
		for (int i = 0; i < size; i++) {
			ArrayList[] rowList = (ArrayList[]) paramList.get(i);
			String[] aNames = new String[rowList[0].size()];
			rowList[0].toArray(aNames);
			String[] aValues = new String[rowList[1].size()];
			rowList[1].toArray(aValues);
			String whereStr = "";
			for (int j = 0; j < aNames.length; j++) {
				if ("ID".equals(aNames[j])) {
					whereStr = " where ID='" + aValues[j] + "'";
				}
			}
			try {
				sql[i] = getSql("TB_GPS_CZ_ZDDLXX", "update", aValues, aNames,
						whereStr, "", "");
			} catch (Exception e) {
				MsgLogger.logExceptionTrace("�޸ĵ�¼��Ϣ����������SQL������", e);
				e.printStackTrace();
			}
		}
		return sql;
	}
	
	/**
	 * �޸��ն�������Ϣ
	 * 
	 * @param xml String xml�ַ���
	 * @return String SQL�������
	 */
	public String[] updateZdsxInfo(String xml) {
		XmlToParams xtp = new XmlToParams(xml);
		ArrayList paramList = xtp.getParamList();

		int size = paramList.size();
		String[] sql = new String[size];
		for (int i = 0; i < size; i++) {
			ArrayList[] rowList = (ArrayList[]) paramList.get(i);
			String[] aNames = new String[rowList[0].size()];
			rowList[0].toArray(aNames);
			String[] aValues = new String[rowList[1].size()];
			rowList[1].toArray(aValues);
			String whereStr = "";
			for (int j = 0; j < aNames.length; j++) {
				if ("ID".equals(aNames[j])) {
					whereStr = " where ID='" + aValues[j] + "'";
				}
			}
			try {
				sql[i] = getSql("TB_GPS_CZ_ZDXX", "update", aValues, aNames,
						whereStr, "", "");
			} catch (Exception e) {
				MsgLogger.logExceptionTrace("�޸��ն���Ϣ����������SQL������", e);
				e.printStackTrace();
			}
		}
		return sql;
	}


	/**
	 * ����LS�ӿ�
	 * 
	 * @throws Exception
	 */
	public String[] setLSInfo(String xml) {
		GpsClient.init();
		String wsAddress = GpsClient.wsAddress;
		String funName = GpsClient.funName[0];
		boolean returnValue = true;
		String[] pram = null;
		String[] value = null;

		XmlToParams xtp = new XmlToParams(xml);
		ArrayList paramList = xtp.getParamList();
		int size = paramList.size();
		String[] flag = new String[size];
		for (int i = 0; i < size; i++) {
			ArrayList[] rowList = (ArrayList[]) paramList.get(i);
			String[] aNames = new String[rowList[0].size()];
			rowList[0].toArray(aNames);
			String[] aValues = new String[rowList[1].size()];
			rowList[1].toArray(aValues);
			GpsClient client = new GpsClient();
			pram = client.getParm();
			value = client.getValue(aValues, aNames);

			returnValue = client.callServices(wsAddress, funName, pram, value);
			// System.out.println(returnValue);
			if (returnValue)
				flag[i] = "1";
			else
				flag[i] = "0";
		}
		return flag;
	}

	/**
	 * ���XML��GDH��ֵ
	 */
	public String[] getGdhValue(String xml) {
		XmlToParams xtp = new XmlToParams(xml);
		ArrayList paramList = xtp.getParamList();

		int size = paramList.size();
		String[] returnValue = new String[size];
		int flag = 0;
		for (int i = 0; i < size; i++) {
			ArrayList[] rowList = (ArrayList[]) paramList.get(i);
			String[] aNames = new String[rowList[0].size()];
			rowList[0].toArray(aNames);
			String[] aValues = new String[rowList[1].size()];
			rowList[1].toArray(aValues);
			for (int j = 0; j < aNames.length; j++) {
				if (aNames[j].equals("GDH")) {
					returnValue[flag] = aValues[j];
					flag++;
				}
			}
		}
		return returnValue;

	}
}
