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
 * @功能：数据操作类
 * @时间：2008-07-23
 * @author ZHOUHM
 * 
 */
public class DataUtil {
	private String messageKey;	// 数据帧唯一标识
	public DataUtil(String messageKey){
		this.messageKey = messageKey;
	}

	/**
	 * 获取指定sql请求的数据 并使用XML格式格式化
	 * 
	 * @param sql
	 *            String SQL请求语句
	 * @return
	 */
	public String getResultSet(String sql) {
		return getResultSet(sql, "");
	}

	/**
	 * 获取指定sql请求的数据 并使用XML格式格式化
	 * 
	 * @param sql
	 *            String SQL请求语句
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
			MsgLogger.logExceptionTrace("数据操作类，转译XML时操作数据库错误！", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				MsgLogger.logExceptionTrace("数据操作类，转译XML时操作关闭数据集错误！", e);
				e.printStackTrace();
			}
			dbe.close();
		}
		return result;
	}

	/**
	 * 将ResultSet转译成XML
	 * 
	 * @param rs
	 *            ResultSet 数据集
	 * @param name
	 *            String 数据类型
	 * @return
	 * @throws SQLException
	 */
	public static String generateXML(final ResultSet rs, String name)
			throws SQLException {
		final StringBuffer buffer = new StringBuffer(1024 * 4);
		if (rs == null)
			return "";
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); // XML文件头
		if (name.equals("") || name == null)
			buffer.append("<ResultSet>");
		else
			buffer.append("<ResultSet id=\"").append(name).append("\">");
		ResultSetMetaData rsmd = rs.getMetaData(); // 数据集的数据头
		int colCount = rsmd.getColumnCount(); // 获取列总数
		for (int id = 0; rs.next(); id++) { // 逐一处理数据
			// 格式为row id , col name, col context
			buffer.append("<row id=\"").append(id).append("\">");
			for (int i = 1; i <= colCount; i++) {
				int type = rsmd.getColumnType(i); // 获取字段类型
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
	 * 获取数据集中数据值
	 * 
	 * @param rs
	 *            ResultSet 数据集
	 * @param colNum
	 *            int 列号
	 * @param type
	 *            int 数据类型
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
	 * 修改工单信息 根据输入的xml字符串参数生成对应的SQL语句
	 * 
	 * @param xml
	 *            String xml字符串
	 * @return String[] SQL语句数组
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
				MsgLogger.logExceptionTrace("修改工单信息函数，生成SQL语句出错！", e);
				e.printStackTrace();
			}
		}
		return sql;
	}

	/**
	 * 获得XML中主键的值
	 * 
	 * @param xml
	 *            String xml字符串
	 * @return String[] 主键值数组
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
	 * 将传入的XML拼装成SQL语句
	 * 
	 * @param tableName
	 *            String 要操作的数据表表名
	 * @param flag
	 *            String SQL语句类型，分别为update和insert
	 * @param columnParam
	 *            String 参数值
	 * @param columnName
	 *            String 字段名
	 * @param whereStr
	 *            String 过滤语句
	 * @param keyName
	 *            String 主键名称
	 * @param value
	 *            String 主键值
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
						// 对于日期格式进行处理
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
			throw new Exception("SQL语句为空");
		System.out.println(returnValue);
		return returnValue;
	}

	/**
	 * 执行sql数据库操作
	 * 
	 * @param sql
	 *            String SQL语句
	 * @return 是否操作成功，成功返回字符串“1”，不成功返回字符串“0”
	 */
	public String putSql(String sql) {
		int flag = -1;
		// ***********************
		// 要等终端接收到信息的确认帧
		// ***********************
//		try {
//			DBExcute dbe = new DBExcute();
//			dbe.beginTransaction();
//			TransactionPool.getInstance().addTransaction(messageKey, dbe, true);
//			try {
//				flag = dbe.ExecuteUpdate(sql);
//			} catch (SQLException e) {
//				MsgLogger.logExceptionTrace("执行SQL语句报错，SQL:" + sql, e);
//				e.printStackTrace();
//			}finally{
//				dbe.close();
//			}
//		} catch (SQLException e) {
//			MsgLogger.logExceptionTrace("事务启动错误+SQL:" + sql, e);
//		}
			
		// ***********************
		// 不用等终端接收到信息的确认帧
		// ***********************
		DBExcute dbe = new DBExcute();
		try {
			flag = dbe.ExecuteUpdate(sql);
			MsgLogger.log(MsgLogger.INFO, "SQL:" + sql);
		} catch (SQLException e) {
			MsgLogger.logExceptionTrace("执行SQL语句报错，SQL:" + sql, e);
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
	 * 生成XML返回文件
	 * 
	 * @param name
	 *            String[] 参数名称
	 * @param value
	 *            String[] 参数值
	 * @return String xml字符串
	 */
	public String generateXML(String[] name, String[] value) {
		StringBuffer returnValue = new StringBuffer();
		returnValue.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); // XML文件头
		returnValue.append("<ResultSet>");
		for (int id = 0; id < name.length; id++) { // 对放回的全部数据逐一处理
			returnValue.append("<row>");
			returnValue.append("<key>").append(name[id]).append("</key>");
			returnValue.append("<sign>").append(value[id]).append("</sign>");
			returnValue.append("</row>");
		}
		returnValue.append("</ResultSet>");
		return returnValue.toString();
	}

	/**
	 * 生成XML返回文件
	 * 
	 * @param name
	 *            String 参数名称
	 * @param value
	 *            String 参数值
	 * @return String xml字符串
	 */
	public String generateXML(String name, String value) {
		StringBuffer returnValue = new StringBuffer();
		returnValue.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); // XML文件头
		returnValue.append("<ResultSet>");
		returnValue.append("<row>");
		returnValue.append("<key>").append(name).append("</key>");
		returnValue.append("<sign>").append(value).append("</sign>");
		returnValue.append("</row>");
		returnValue.append("</ResultSet>");
		return returnValue.toString();
	}

	/**
	 * insert工单信息
	 * 
	 * @param xml
	 *            String xml字符串
	 * @return String[] SQL语句数组
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
				MsgLogger.logExceptionTrace("新建工单信息函数，生成SQL语句出错！", e);
				e.printStackTrace();
			}
		}
		return sql;
	}

	/**
	 * 新增工单处理信息
	 * 
	 * @param xml
	 *            String xml字符串
	 * @return String SQL语句
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
				MsgLogger.logExceptionTrace("新建工单处理信息函数，生成SQL语句出错！", e);
				e.printStackTrace();
			}
		}
		return sql;
	}

	/**
	 * 新增工单处理坐标
	 * 
	 * @param xml
	 *            String xml字符串
	 * @return String SQL语句
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
				MsgLogger.logExceptionTrace("新建工单处理坐标信息函数，生成SQL语句出错！", e);
				e.printStackTrace();
			}
		}
		return sql;
	}

	/**
	 * 新增终端登陆信息
	 * 
	 * @param xml String xml字符串
	 * @param value String 时间
	 * @return String SQL语句
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
				MsgLogger.logExceptionTrace("新增终端登录信息函数，生成SQL语句出错！", e);
				e.printStackTrace();
			}
		}
		return sql;
	}

	/**
	 * 修改终端登陆信息
	 * 
	 * @param xml String xml字符串
	 * @return String SQL语句数组
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
				MsgLogger.logExceptionTrace("修改登录信息函数，生成SQL语句出错！", e);
				e.printStackTrace();
			}
		}
		return sql;
	}
	
	/**
	 * 修改终端属性信息
	 * 
	 * @param xml String xml字符串
	 * @return String SQL语句数组
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
				MsgLogger.logExceptionTrace("修改终端信息函数，生成SQL语句出错！", e);
				e.printStackTrace();
			}
		}
		return sql;
	}


	/**
	 * 调用LS接口
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
	 * 获得XML中GDH的值
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
