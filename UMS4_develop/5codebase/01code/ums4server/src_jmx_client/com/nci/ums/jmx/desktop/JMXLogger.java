package com.nci.ums.jmx.desktop;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.swing.JTextArea;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.nci.ums.jmx.ComponentAppender;

public class JMXLogger {


	private static Properties res = null;
	private static Logger logger = Logger.getLogger(JMXLogger.class);
	private static String FQCN = JMXLogger.class.getName();
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

	private static String defaultLogFileName = "UMSJMXLOG.txt";
	private static String webServerUMSLogDir = "logs/";
//	private static DynamicUMSStreamReader servletTemp = new DynamicUMSStreamReader();
	public static javax.swing.JTextArea consoleloggerArea;
	// private static ArrayList umsThreads;
	static {
		

		PatternLayout layout = new PatternLayout("%d{ISO8601}:[%p] --> %m  %l%n ");
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
		consoleloggerArea = new JTextArea();
		ComponentAppender componentAppender = new ComponentAppender(
				consoleloggerArea, 150);
		componentAppender.setLayout(layout);
		logger.addAppender(componentAppender);
		logger.addAppender(fileAppender);
		logger.addAppender(consoleAppender);
		logger.setLevel(Level.INFO);
	}

	/**
	 * Log information
	 */
	public static void log(int level, String msg) {
		// synchronized (syncLock) {
		switch (level) {
		case INFO:
			logger.log(FQCN, Level.INFO, msg, null);
			break;
		case WARN:
			logger.log(FQCN, Level.WARN, msg, null);
			break;
		case ERROR:
			logger.log(FQCN, Level.ERROR, msg, null);
			break;
		case FATAL:
			logger.log(FQCN, Level.FATAL, msg, null);
			break;
		// default : for debug
		default:
			logger.log(FQCN, Level.DEBUG, msg, null);
			// }
		}

	}

	static byte[] syncLock = new byte[0];

	public static void logExceptionTrace(Exception e) {
		// synchronized (syncLock) {
		log(ERROR, e.getMessage());
		StackTraceElement[] ele = e.getStackTrace();
		for (int i = 0; i < ele.length; i++) {
			log(ERROR, ele[i].toString());
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
			log(ERROR, "Encode " + msg + "error: " + ex.getMessage());
			length = 4 + msg.getBytes().length;
		}
		char[] ch = { ' ', ' ', ' ', ' ', ' ' };
		String len = new Integer(length).toString();
		for (int i = 0; i < len.length(); i++) {
			ch[i] = len.charAt(i);
		}
		return new String(ch) + retcode + msg;
	}
}
