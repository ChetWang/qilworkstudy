/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nci.gps.log;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * 
 * @author Qil.Wong
 */
public class MsgLogger {

	private static Logger logger = Logger.getLogger(MsgLogger.class);

	static byte[] syncLock = new byte[0];

	/* Log levels */
	public static final int DEBUG = 1;
	public static final int INFO = 2;
	public static final int WARN = 3;
	public static final int ERROR = 4;
	public static final int FATAL = 5;

	private static String FQCN = MsgLogger.class.getName();

	static {
		String pattern = "";
		pattern += "[%d{ISO8601}] ";
		pattern += "%l %m %n";

		PatternLayout layout = new PatternLayout(pattern);
		ConsoleAppender consoleAppender = new ConsoleAppender(layout);
		DailyRollingFileAppender appender = null;
		try {
			appender = new DailyRollingFileAppender(layout, "logs/log.log",
					"'.'yyyy-MM-dd");
			appender.setEncoding("GBK");
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.addAppender(appender);
		logger.addAppender(consoleAppender);
		logger.setLevel(Level.DEBUG);
	}

	public static void log(int level, String msg) {
		synchronized (syncLock) {
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
			}
		}
	}

	public static void logExceptionTrace(String msg, Exception e) {
		synchronized (syncLock) {
			if (msg != null && !msg.equals("")) {
				logger.log(FQCN, Level.ERROR, msg, null);
			}
			logger.log(Level.ERROR, e);
			StackTraceElement[] ele = e.getStackTrace();
			for (int i = 0; i < ele.length; i++) {
				logger.log(FQCN, Level.ERROR, ele[i].toString(), null);
			}
		}
	}
	
}
