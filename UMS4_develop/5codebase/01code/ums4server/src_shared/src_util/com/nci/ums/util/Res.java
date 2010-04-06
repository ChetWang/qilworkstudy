package com.nci.ums.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JTextArea;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.nci.ums.jmx.LogInfo;
import com.nci.ums.jmx.server.ServerJMXHandler;

/**
 * This class can get error code string and other string from config file.
 */
public class Res {

	private static String props = "/resources/code.props";
	private static Properties res = null;
	private static Logger logger = null;
	private static String FQCN = Res.class.getName();
	/* Log levels */
	public static final int DEBUG = 10000;
	public static final int INFO = 20000;
	public static final int WARN = 30000;
	public static final int ERROR = 40000;
	public static final int FATAL = 50000;
	/* File catalog */
	public static final String ERROR_PATH = "error";
	public static final String QUERY_PATH = "query";
	public static final String WHITESPACEREG = "[\\s]*";
	public static final int INTERVALDATE = 31;
	private static Level currentLevel;
	private static Vector allowIP;
	private static String defaultLogFileName = "UMSLOG.txt";
	private static String webServerUMSLogDir = "logs/";

	public static void load() {
		logger = Logger.getLogger(Res.class);
		/* Load code properties */
		res = new Properties(); // Empty properties
		try {
			InputStream in = new DynamicUMSStreamReader()
					.getInputStreamFromFile(props);
			res.load(in); // Try to load properties
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "load code error" + e.getMessage());
			System.exit(1);
		}

		PatternLayout layout = new PatternLayout(Util.getLogLayoutPattern());
		ConsoleAppender consoleAppender = new ConsoleAppender(layout);
		DailyRollingFileAppender fileAppender = null;
		String appBinDir = System.getProperty("user.dir");
		try {
			fileAppender = new DailyRollingFileAppender(layout,
					webServerUMSLogDir + defaultLogFileName, "'.'yyyy-MM-dd");
			fileAppender.setEncoding("GBK");
		} catch (IOException e) {
			System.out
					.println("UMS Server cannot find the log directory specified: "
							+ webServerUMSLogDir
							+ ". Server will create log files by default...");
			System.out.println("UMS Server log files will be created at \""
					+ appBinDir + "\"");
			try {
				fileAppender = new DailyRollingFileAppender(layout,
						defaultLogFileName, "'.'yyyy-MM-dd");
			} catch (IOException e1) {
			}
		}
		logger.addAppender(fileAppender);
		logger.addAppender(consoleAppender);
		currentLevel = Util.getLogLevel();
		logger.setLevel(currentLevel);
	}

	/**
	 * Get coded string from properties file
	 */
	public static String getStringFromCode(String code) {
		return native2Unicode(res.getProperty(code));
	}

	public static String getStringFromCode(String code, String arg1) {
		String str = getStringFromCode(code);
		str = str.replaceFirst("%s", arg1);
		return str;
	}

	public static String getStringFromCode(String code, String arg1, String arg2) {
		String str = getStringFromCode(code);
		str = str.replaceFirst("%s", arg1);
		str = str.replaceFirst("%s", arg2);
		return str;
	}

	public static String getStringFromCode(String code, String arg1,
			String arg2, String arg3) {
		String str = getStringFromCode(code);
		str = str.replaceFirst("%s", arg1);
		str = str.replaceFirst("%s", arg2);
		str = str.replaceFirst("%s", arg3);
		return str;
	}

	/**
	 * Log information
	 */
	public static void log(int level, String msg) {
		// synchronized (syncLock) {
		switch (level) {
		case Res.INFO:
			logger.log(FQCN, Level.INFO, msg, null);
			break;
		case Res.WARN:
			logger.log(FQCN, Level.WARN, msg, null);
			break;
		case Res.ERROR:
			logger.log(FQCN, Level.ERROR, msg, null);
			break;
		case Res.FATAL:
			logger.log(FQCN, Level.FATAL, msg, null);
			break;
		// default : for debug
		default:
			logger.log(FQCN, Level.DEBUG, msg, null);
			// }
		}
		if (ServerJMXHandler.getInstance().getRegisteredMBean() != null
				&& currentLevel.toInt() <= level)
			ServerJMXHandler.getInstance().getRegisteredMBean().sendLog(
					new LogInfo(level, msg));
	}

	public static void log(int level, String code, String arg1) {
		String str = getStringFromCode(code, arg1);
		log(level, code + str);
	}

	public static void log(int level, String code, String arg1, String arg2) {
		String str = getStringFromCode(code, arg1, arg2);
		log(level, code + str);
	}

	public static void log(int level, String code, String arg1, String arg2,
			String arg3) {
		String str = getStringFromCode(code, arg1, arg2, arg3);
		log(level, code + str);
	}

	static byte[] syncLock = new byte[0];

	public static void logExceptionTrace(Exception e) {
		// synchronized (syncLock) {
		Res.log(Res.ERROR, e.getMessage());
		StackTraceElement[] ele = e.getStackTrace();
		for (int i = 0; i < ele.length; i++) {
			Res.log(Res.ERROR, ele[i].toString());
			// }
		}
	}

	/**
	 * -> length retcode msg
	 */
	public static String getMessage(String retcode, String msg) {
		int length = 0;
		try {
			length = 4 + msg.getBytes("GB2312").length;
		} catch (UnsupportedEncodingException ex) {
			Res.log(Res.ERROR, "Encode " + msg + "error: " + ex.getMessage());
			length = 4 + msg.getBytes().length;
		}
		char[] ch = { ' ', ' ', ' ', ' ', ' ' };
		String len = new Integer(length).toString();
		for (int i = 0; i < len.length(); i++) {
			ch[i] = len.charAt(i);
		}
		return new String(ch) + retcode + msg;
	}

	public static String getMessage(String msg) {
		int length = 0;
		length = msg.getBytes().length;
		char[] ch = { ' ', ' ', ' ', ' ', ' ' };
		String len = new Integer(length).toString();
		for (int i = 0; i < len.length(); i++) {
			ch[i] = len.charAt(i);
		}
		return new String(ch) + msg;
	}

	/**
	 * native(GBK) -> Unicode
	 */
	public static String native2Unicode(String str) {
		String ustr = "";
		if (str == null) {
			return "";
		}
		try {
			ustr = new String(str.getBytes("iso-8859-1"), "GBK");
		} catch (UnsupportedEncodingException ex) {
			System.err.println("Encoding not supported: " + ex.getMessage());
			return str;
		}
		return ustr;
	}

	public static Vector getAllowIP() {
		return allowIP;
	}

	/**
	 * Unicode -> native(GBK)
	 */
	public static String unicode2Native(String str) {
		String ustr = "";
		if (str == null) {
			return "";
		}
		try {
			ustr = new String(str.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException ex) {
			System.err.println("Encoding not supported: " + ex.getMessage());
			return str;
		}
		return ustr;
	}

	/**
	 * Formated output key-value of a map
	 */
	public static String printMap(Map map) {
		StringBuffer sb = new StringBuffer();
		Iterator keys = map.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			sb.append(key + " = " + map.get(key) + "("
					+ ((String) map.get(key)).length() + ")\n");
		}
		return sb.toString();
	}

	/**
	 * 得到string value，并进行相关的处理
	 */
	public static String getValue(Map map, Object key) {
		Object value = map.get(key);
		if (value == null) {
			return "";
		} else {
			String ret;
			ret = (String) value;
			ret = ret.trim();
			if (ret == null) {
				return "";
			} else {
				return ret;
			}
		}
	}

	/**
	 * 得到当前时间和日期
	 */
	public static String getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(new Date());
	}

	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("HHmmss");
		return df.format(new Date());
	}

	/**
	 * 得到随机数0-9
	 */
	public static char getRandom() {
		int ran = (int) (Math.random() * 10);
		return (char) (ran + '0');
	}

	/**
	 * 将数字转化为字符串
	 */
	public static String int2String(int num) {
		return Integer.toString(num);
	}

	/**
	 * 将数字字符串除以100
	 */
	public static String getCurrency(String mon, int div) {
		double dMon = Double.parseDouble(mon);
		double ret;
		ret = dMon / div;
		return new Double(ret).toString();
	}

	/**
	 * 将字符串右补空格
	 */
	public static String getFixed(String fix, int length) {
		int len = 0;
		try {
			len = fix.getBytes("GBK").length;
		} catch (UnsupportedEncodingException ex) {
			Res.log(Res.ERROR, "Encode " + fix + "error: " + ex.getMessage());
			len = fix.getBytes().length;
		}
		if (len >= length) {
			return fix;
		}
		String space = "";
		for (int i = 0; i < length - len; i++) {
			space = space + " ";
		}
		return fix + space;
	}

	/**
	 * 如果渠道号为010，则需要区分130-134为联通手机，渠道号为011 135－139为移动手机，渠道号为010
	 */
	public static String getRealMediaID(String oldMediaId, String recv) {
		String ret = oldMediaId;
		if (oldMediaId.equalsIgnoreCase("010")) {
			if (recv.startsWith("130") || recv.startsWith("131")
					|| recv.startsWith("132") || recv.startsWith("133")
					|| recv.startsWith("134")) {
				ret = "011";
			}
		}
		return ret;
	}

	/**
	 * 
	 */
	public static String addDate(String strDate) {
		// yyyyMMDD
		int[] standardDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int[] leapyearDays = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int y = Integer.parseInt(strDate.substring(0, 4));
		int m = Integer.parseInt(strDate.substring(4, 6));
		int d = Integer.parseInt(strDate.substring(6)) + INTERVALDATE;
		int maxDateCount = 0;
		while (true) {
			if ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0) {
				maxDateCount = leapyearDays[m - 1];
			} else {
				maxDateCount = standardDays[m - 1];
			}
			if (d > maxDateCount) {
				d = d - maxDateCount;
				m++;
			} else {
				break;
			}
			if (m > 12) {
				m = 1;
				y++;
			}
		}
		java.text.DecimalFormat yf = new java.text.DecimalFormat("0000");
		java.text.DecimalFormat mdf = new java.text.DecimalFormat("00");
		return yf.format(y) + mdf.format(m) + mdf.format(d);
	}

	public static String getTrimString(String s, int length) {
		String ss = "";
		try {
			ss = new String(s.getBytes("GBK"), 0, length, "GBK");
		} catch (Exception e) {
			ss = new String(s.getBytes(), 0, length);
			Res.log(Res.ERROR, "解码错误" + e);
		}
		return ss;
	}

	/*
	 * public static ArrayList getUMSThreads() { return umsThreads; }
	 */
	public static Properties getProps(String fileName) {
		res = new Properties(); // Empty properties
		try {
			InputStream in = ClassLoader.getSystemResourceAsStream(fileName);
			res.load(in); // Try to load properties
			in.close();
		} catch (Exception e) {
			System.err.println("Load " + fileName + " properties failed.");
			System.exit(1);
		} // Load code properties finished
		finally {
		}
		return res;
	}

	/*
	 * 用于得到要发送的信息内容的函数。
	 */
	public static String getMsgContent(String msgContent) {

		StringBuffer content = new StringBuffer();
		try {
			String contentString = msgContent.trim();
			if (contentString.getBytes().length >= 160) {
				content.append(getTrimString(msgContent.trim(), 160));
			} else {
				content.append(msgContent.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(ERROR, "1091", e.getMessage());
		}
		// Res.log(DEBUG, "content=" + content);
		return content.toString();
	}

	/**
	 * 获取UMS默认的数据库连接（DataBaseOp.getPoolName()）
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		return getConnection(DataBaseOp.getPoolName());
	}

	/**
	 * 获取指定连接池名称的数据库连接
	 * 
	 * @param poolName
	 * @return
	 */
	public static Connection getConnection(String poolName) {
		try {
			return DriverManager.getConnection(poolName);
		} catch (SQLException e) {
			Res.logExceptionTrace(e);
		}
		return null;
	}
	
	/**
	 * 销毁数据库连接池
	 */
	public static void destroyDBPool(){
		DataBaseOp.unRegisterDriver();
	}

	public static int bytesToInt(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		if (bytes.length >= 2)
			num |= ((bytes[1] << 8) & 0xFF00);
		return num;
	}

	public static void main(String[] x) {
		System.out.println(Level.DEBUG.toInt());
		System.out.println(Level.INFO.toInt());
		System.out.println(Level.WARN.toInt());
		System.out.println(Level.ERROR.toInt());
		System.out.println(Level.FATAL.toInt());
		System.out.println(Level.ALL.toInt());
		System.out.println(Level.OFF.toInt());
	}
}
