/**
 * <p>Title: Util.java</p>
 * <p>Description:
 *    提供公用的函数库
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * May 20 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.Level;

import com.nci.ums.dialect.MysqlDialect;
import com.nci.ums.dialect.OracleDialect;
import com.nci.ums.dialect.SQLServerDialect;
import com.nci.ums.dialect.UMSDialect;
import com.nci.ums.v3.message.euis.EnterpriseUserInfo;

public class Util {

	public static int DAYTIME = 24 * 60 * 60;
	public static final int UMS_DATABASE_SQLSERVER = 1;
	public static final int UMS_DATABASE_ORACLE = 2;
	public static final int UMS_DATABASE_DB2 = 3;
	public static final int UMS_DATABASE_SYBASE = 4;
	public static final int UMS_DATABASE_DERBY = 5;
	public static final int UMS_DATABASE_MYSQL = 6;
	private static int dbType = -1;
	private static UMSDialect dialect;

	static {
		dbType = getDataBaseType();
	}

	public synchronized static UMSDialect getDialect() {
		if (dialect == null) {
			switch (dbType) {
			case Util.UMS_DATABASE_DB2:
				break;
			case Util.UMS_DATABASE_DERBY:
				break;
			case Util.UMS_DATABASE_MYSQL:
				dialect = new MysqlDialect();
				break;
			case Util.UMS_DATABASE_ORACLE:
				dialect = new OracleDialect();
				break;
			case Util.UMS_DATABASE_SQLSERVER:
				dialect = new SQLServerDialect();
				break;
			case Util.UMS_DATABASE_SYBASE:
				break;
			default:
				return null;
			}
		}
		return dialect;
	}

	public static EnterpriseUserInfo getEuiObject() {
		String name = PropertyUtil.getProperty("euiClass",
				"/resources/eui.props");
		try {
			Class euiClass = Class.forName(name);
			return (EnterpriseUserInfo) euiClass.newInstance();
		} catch (ClassNotFoundException e) {
			Res.log(Res.ERROR, "企业用户的具体实现类加载错误：" + name + "." + e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 用来返回本地时间的函数，可以指定格式，返回为int类型。
	 */
	public static int getCurrentTime(String format) {
		SimpleDateFormat current = new SimpleDateFormat(format);
		int currentTime = Integer
				.parseInt(current.format(new java.util.Date()));
		return currentTime;
	}

	/*
	 * 获得当前日期下一日期间隔的起始日期
	 */
	/*
	 * 时间加法运算
	 */
	public static String addMinute(String format, int minute) {
		SimpleDateFormat current = new SimpleDateFormat(format);
		java.util.Date utilDate = new java.util.Date();
		utilDate.setTime(utilDate.getTime() + minute * 1000 * 60);
		String currentTime = current.format(utilDate);
		return currentTime;
	}

	/*
	 * 用来返回本地时间的函数，可以指定格式，返回为string类型。
	 */
	public static String getCurrentTimeStr(String format) {
		SimpleDateFormat current = new SimpleDateFormat(format);
		String currentTime = current.format(new java.util.Date());
		return currentTime;
	}

	/*
	 * 用来得到utc时间的函数
	 */
	/*
	 * public static int getUTCTime(){ java.text.SimpleDateFormat formatter =
	 * new java.text.SimpleDateFormat ("MMddhhmmss"); Date currentTime = new
	 * Date(); String dateString = formatter.format(currentTime); return
	 * Integer.parseInt(dateString);
	 */
	public static int getUTCTime1() {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		return (int) (calendar.getTimeInMillis() / 1000);
	}

	public static int getUTCTime() {
		long UTCTime = System.currentTimeMillis() / 1000;

		return (int) UTCTime;
	}

	/*
	 * 用来把string转换为byte的函数。
	 */
	public static synchronized byte[] getByteString(String msg) {
		byte[] b1 = new byte[21];
		System.arraycopy(msg.getBytes(), 0, b1, 0, msg.getBytes().length);
		b1[msg.getBytes().length] = 0x0;
		return b1;
	}

	public static synchronized byte[] getMaxByteString(String msg) {
		byte[] b1 = new byte[612];
		System.arraycopy(msg.getBytes(), 0, b1, 0, msg.getBytes().length);
		b1[msg.getBytes().length] = 0x0;
		return b1;
	}

	public static byte[] getByte(String msg, int data_code) {
		try {
			if (data_code == 15) {
				return msg.getBytes("GBK");
			} else if (data_code == 0) {
				return msg.getBytes("8859_1");
			} else if (data_code == 21 || data_code == 4) {
				return msg.getBytes("UnicodeBigUnmarked");// ucs2
			} else if (data_code == 8) {
				return msg.getBytes("utf-16");
			} else {
				return msg.getBytes("UnicodeBig");
			}
		} catch (Exception e) {
			return msg.getBytes();
		}
	}

	public static byte[] getByteFrom_tp_Ddhi(String msg, int data_code,
			byte[] tp_Ddhi) {
		byte[] msgByte = null;
		try {
			if (data_code == 15) {
				msgByte = msg.getBytes("GBK");
			} else if (data_code == 0) {
				msgByte = msg.getBytes("8859_1");
			} else if (data_code == 21 || data_code == 4) {
				msgByte = Util.getByte(msg);
			} else if (data_code == 8) {
				msgByte = msg.getBytes("UnicodeBigUnmarked");// ucs2
			} else {
				msgByte = msg.getBytes("UnicodeBig");
			}
		} catch (Exception e) {
			msgByte = msg.getBytes();
		}
		byte[] contentByte = new byte[msgByte.length + tp_Ddhi.length];
		for (int i = 0; i < tp_Ddhi.length; i++)
			contentByte[i] = tp_Ddhi[i];
		for (int i = 0; i < msgByte.length; i++)
			contentByte[tp_Ddhi.length + i] = msgByte[i];
		return contentByte;
	}

	public static String getStringFromBytes(byte[] bytes, int startIndex,
			int toIndex, int byteFormat) {
		try {
			if (byteFormat == 15) {
				return new String(bytes, startIndex, toIndex, "GBK");
			} else if (byteFormat == 0) {
				return new String(bytes, startIndex, toIndex, "ISO8859-1");
			} else if (byteFormat == 21 || byteFormat == 4) {// ucs
				return new String(bytes, startIndex, toIndex,
						"UnicodeBigUnmarked");
			} else {
				return new String(bytes, startIndex, toIndex, "UnicodeBig");
			}
		} catch (Exception e) {
			return new String(bytes, startIndex, toIndex);
		}
	}

	public static synchronized String getFixedString(String msg, int len) {
		if (msg == null) {
			msg = "";
		}

		StringBuffer sb = new StringBuffer(msg);
		for (int i = msg.getBytes().length + 1; i <= len; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public static synchronized int getTime(int time) {
		return (time / 10000) * 3600 + (time / 100 - time / 10000 * 100) * 60
				+ (time - time / 100 * 100);
	}

	/*
	 * 用来取代null的函数。
	 */
	public static String replaceNULL(String msg) {
		if (msg == null || msg.equals("null")) {
			return "";
		} else {
			return msg.trim();
		}
	}

	/*
	 * 字符串替换函数 @param sAll String 原来的字符串 @param older String 要替换掉的字符串 @param
	 * newer String 新的字符串 @return String 替换后的结果
	 */
	public static synchronized String strReplace(String sAll, String sOld,
			String sNew) {
		int iT = 0;
		String sF = null;
		String sH = null;
		// 如果新串中包括旧串,不让替多只让替少
		if (sNew.indexOf(sOld) != -1) {
			return sAll;
		}
		if (sAll == null || sOld == null || sNew == null) {
			return sAll;
		}
		iT = sAll.indexOf(sOld);
		while (iT != -1) {
			sF = sAll.substring(0, iT);
			sH = sAll.substring(iT + sOld.length());
			sAll = sF + sNew + sH;
			iT = sAll.indexOf(sOld);
		}
		return sAll;
	}

	/*
	 * 过滤接收字符{MO} @param sMo String 转换前字符 @return boolean 转换后字符 @说明
	 */
	/*
	 * 过滤接收字符{MT} @param sMt String 转换前字符 @return boolean 转换后字符 @说明
	 */
	public static synchronized String convertHalfToFull(String sMt) {
		String sReturn = sMt;
		if (sReturn == null) {
			return sReturn;
		}
		try {
			sReturn = strReplace(sReturn, "'", "’");
			sReturn = sReturn.replace(',', '，');
			sReturn = sReturn.replace('"', '“');
		} catch (Exception ex) {
			return sMt;
		}
		return sReturn;
	}

	/*
	 * 过滤接收字符{MT} @param sMo String 转换前字符 @return boolean 转换后字符 @说明
	 */
	public static synchronized String convertFullToHalf(String sMt) {
		String sReturn = sMt;
		if (sReturn == null) {
			sReturn = "";
		}
		try {
			sReturn = strReplace(sReturn, "‘", "'");
			sReturn = sReturn.replace('，', ',');
			sReturn = sReturn.replace('。', '.');
			sReturn = sReturn.replace('；', ';');
			sReturn = sReturn.replace('！', '!');
			sReturn = sReturn.replace('？', '?');
			sReturn = sReturn.replace('：', ':');
			sReturn = sReturn.replace('“', '＂');
			sReturn = sReturn.replace('“', '＂');
			sReturn = sReturn.replace('”', '＂');
		} catch (Exception ex) {
		}
		return sReturn;
	}

	public static synchronized String fromDatabase(String change) {
		String result = "";
		try {
			byte[] temp = change.getBytes("gb2312");
			return new String(temp, "iso-8859-1");
		} catch (Exception e) {
		}

		return result;
	}

	/*
	 * 主要把程序中可认的中文字符转换为存入数据库中的字符 @param change 需要转换的程序中的中文字符 @return 转换后的数据库字符
	 * @throws UnsupportedEncodingException 不能转换时抛出的异常
	 */
	public static synchronized String toDatabase(String change) {
		// change.get
		String result = "";
		try {
			result = new String(change.getBytes("gb2312"), "iso-8859-1");
		} catch (Exception e) {
		}

		return result;
	}

	public static boolean isNumeric(String msg) {
		try {
			Long.parseLong(msg);
			// Integer.parseInt(msg);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public static String getASCII(byte[] shortMsg, int smLen) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < smLen; i++) {
			byte b = shortMsg[i];
			String ascii = Integer.toHexString(b);
			if (b < 16 && b >= 0) {
				ascii = "0" + ascii;
			}
			if (ascii.length() > 2) {
				ascii = ascii.substring(6, 8);
			}
			result.append(ascii);
		}
		// Res.log(Res.ERROR,"ascII:"+result.toString());
		return result.toString().toUpperCase();
	}

	public static byte[] getByte(String msg) {

		// 取消息长度
		int length = msg.getBytes().length;
		byte[] temp = new byte[length];
		int i = 0;
		int j = 0;

		// 按字节压缩
		for (i = 0; i < length; i++) {
			// 取一个字节
			int b1 = Integer.parseInt(msg.substring(i, i + 1), 16);
			// 移位
			b1 <<= 4;
			i++;
			if (i < length) {
				// 取下一个字节
				int b2 = Integer.parseInt(msg.substring(i, i + 1), 16);
				b1 = b1 + b2;
			}
			temp[j] = (byte) b1;
			j++;
		}
		byte[] result = new byte[j];
		for (int f = 0; f < j; f++) {
			result[f] = temp[f];
		}
		// result[j]=0x0;
		return result;
	}

	public static Timestamp getTimeStamp() {
		Timestamp result = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date nowDate = new java.util.Date();

		result = new Timestamp(nowDate.getTime());

		return result;
	}

	/**
	 * get the database type which ums use. the database property file is
	 * restrict to be pattern of which the "driver" property must be at first
	 * line.
	 * 
	 * Since UMS3.0, created by Qil.Wong
	 * 
	 * @return database type. UMS_DATABASE_SQLSERVER = 1; UMS_DATABASE_ORACLE =
	 *         2; UMS_DATABASE_DB2 = 3; UMS_DATABASE_SYBASE = 4;
	 *         UMS_DATABASE_DERBY = 5; UMS_DATABASE_MYSQL = 6;
	 */
	public static int getDataBaseType() {
		BufferedReader reader = null;
		if (dbType == -1) {
			try {
				reader = new BufferedReader(
						new InputStreamReader(
								(new ServletTemp()
										.getInputStreamFromFile("/resources/umsdb.props"))));
				try {
					String s = reader.readLine();
					if (s.indexOf("OracleDriver") > 0) {
						return Util.UMS_DATABASE_ORACLE;
					}
					if (s.indexOf("SQLServerDriver") > 0) {
						return Util.UMS_DATABASE_SQLSERVER;
					}
					if (s.indexOf("DB2Driver") > 0) {
						return Util.UMS_DATABASE_DB2;
					}
					if (s.indexOf("mysql") > 0) {
						return Util.UMS_DATABASE_MYSQL;
					}
					if (s.indexOf("derby") > 0) {
						return Util.UMS_DATABASE_DERBY;
					}
					if (s.indexOf("sybase") > 0) {
						return Util.UMS_DATABASE_SYBASE;
					}
				} catch (IOException e) {
					Res
							.log(Res.ERROR,
									"Read the database property file error!");
					return -1;
				}
			} catch (Exception e) {
				Res.log(Res.ERROR, "Can not read the database property file!"
						+ e.getMessage());
				return -1;
			}finally{
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		
		return dbType;
	}

	public static void main(String[] args) {
		try {
			/*
			 * DBConnect db=new DBConnect(); //db.executeUpdate("create sequence
			 * s_a89"); //ResultSet rs=db.executeQuery("select s_a89.nextval
			 * from dual"); //ResultSet rs=db.executeQuery("select * from a01
			 * where substr(LEADER_CODE,3,2)='02'"); ResultSet
			 * rs=db.executeQuery("select * from a01 where
			 * a0107>to_date('1960-09-01','yyyy-mm-dd')");
			 * System.out.println(rs.getMetaData().getColumnName(6));
			 * if(rs.next()){ for(int i=1;i<rs.getMetaData().getColumnCount();i++){
			 * System.out.println(i+":"+rs.getString(i)); } }
			 */
			System.out.println(Util.getCurrentTimeStr("yyMMddHHmmss"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static Level getLogLevel() {
		String priorityName = PropertyUtil.getProperty("level",
				"/resources/logConfig.props");
		if (priorityName.equalsIgnoreCase("INFO")) {
			return Level.INFO;
		} else if (priorityName.equalsIgnoreCase("DEBUG")) {
			return Level.DEBUG;
		} else if (priorityName.equalsIgnoreCase("WARN")) {
			return Level.WARN;
		} else if (priorityName.equalsIgnoreCase("ERROR")) {
			return Level.ERROR;
		} else if (priorityName.equalsIgnoreCase("FATAL")) {
			return Level.FATAL;
		}
		return Level.INFO;
	}

	public synchronized static String getLogLayoutPattern() {
		String layout = PropertyUtil.getProperty("layout",
				"/resources/logConfig.props");
		return layout;
	}

	public static synchronized String getProperty(String propName,
			String propFile) {
		Properties pro = new Properties();
		try {
			pro.load(new ServletTemp().getInputStreamFromFile(propFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pro.getProperty(propName);

	}
}