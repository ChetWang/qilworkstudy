/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

import com.nci.Main;

/**
 * 
 * @author Qil.Wong
 */
public class KnoLogger {

	// static Logger log = Logger.getLogger(KnoLogger.class.getName());
	static protected Logger commonLogger = null;
	private static String FQCN = KnoLogger.class.getName();
	static {
		// try {
		// boolean append = true;
		// FileHandler fh = new
		// FileHandler(System.getProperty("user.dir")+"/log/QilKno.log",append);
		// fh.setFormatter(new SimpleFormatter());
		// log.addHandler(fh);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		commonLogger = Logger.getLogger(KnoLogger.class);
		String loggerFolderPath = System.getProperty("user.dir") + "/log/";
		System.out.println("log path:"+loggerFolderPath);
		// 创建日志目录
		if (loggerFolderPath != null) {
			File loggerFolder = new File(loggerFolderPath);
			if (!loggerFolder.exists()) {
				loggerFolder.mkdirs();
			}
		}
		// 设置日志格式
		PatternLayout layout = new PatternLayout(getLogLayoutPattern());
		// 创建日志记录挂接器
		{
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			commonLogger.addAppender(consoleAppender);
			if (loggerFolderPath != null) {
				DailyRollingFileAppender fileAppender = null;
				try {
					fileAppender = new DailyRollingFileAppender(layout,
							loggerFolderPath + "sqlserver-oracle-log.txt", "'.'yyyy-MM-dd");
				} catch (IOException e) {
					System.out.println("创建文件路径出错！路径：" + loggerFolderPath);
				}
				fileAppender.setEncoding("GBK");
				commonLogger.addAppender(fileAppender);
			}
		}
		// 设置记录级别
		commonLogger.setLevel(getLogLevel());
	}

	public synchronized static void log(Priority priority, Object message,
			Throwable t) {
		commonLogger.log(FQCN,priority, message, t);
	}

	
	public static void main(String[] xx){
		KnoLogger.log(Level.DEBUG, "xxxxxx", null);
		KnoLogger.log(Level.ERROR, "yyyyyy", new Exception("www"));
	}
	
	public synchronized static Level getLogLevel(){
		String priorityName = getProperty("level", System.getProperty("user.dir")+"/conf/logConfig.props");
		if(priorityName.equalsIgnoreCase("INFO")){
			return Level.INFO;
		}else if(priorityName.equalsIgnoreCase("DEBUG")){
			return Level.DEBUG;
		}else if(priorityName.equalsIgnoreCase("WARN")){
			return Level.WARN;
		}else if(priorityName.equalsIgnoreCase("ERROR")){
			return Level.ERROR;
		}else if(priorityName.equalsIgnoreCase("FATAL")){
			return Level.FATAL;
		}
		return Level.INFO;
	}
	
	public synchronized static String getLogLayoutPattern(){
		String layout = getProperty("layout", System.getProperty("user.dir")+"/conf/logConfig.props");
		return layout;
	}
	
	public static synchronized String getProperty(String propName,String propFile){
		Properties pro = new Properties();
		try {
			pro.load(new FileInputStream(propFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pro.getProperty(propName);		
	}
}
