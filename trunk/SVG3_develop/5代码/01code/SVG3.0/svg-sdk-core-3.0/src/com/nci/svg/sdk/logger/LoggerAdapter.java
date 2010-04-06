package com.nci.svg.sdk.logger;

import java.util.HashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.nci.svg.sdk.module.DefaultModuleAdapter;
import com.nci.svg.sdk.module.GeneralModuleIF;
import com.nci.svg.sdk.module.ModuleControllerAdapter;
import com.nci.svg.sdk.module.ModuleInitializeFailedException;
import com.nci.svg.sdk.module.ModuleStartFailedException;
import com.nci.svg.sdk.module.ModuleStopedException;

/**
 * SVG平台日志记录对象，可以将日志输出到控制台、指定文件、数据库。
 * 
 * @author Qil.Wong
 * 
 */
public abstract class LoggerAdapter extends DefaultModuleAdapter {

	// 普通的日志记录器，包括控制台和文本记录器
	protected Logger commonLogger = null;

	/**
	 * 调试级别日志标签
	 */
	public final static int DEBUG = 10000;
	/**
	 * 提示级别日志标签
	 */
	public final static int INFO = 20000;
	/**
	 * 警告级别日志标签
	 */
	public final static int WARN = 30000;
	/**
	 * 一般性错误级别日志标签
	 */
	public final static int ERROR = 40000;
	/**
	 * 致命错误级别日志标签
	 */
	public final static int FATAL = 50000;

	protected String fqcn = LoggerAdapter.class.getName();

	/**
	 * 默认的日志级别
	 */
	protected Level defaultLevel = null;

	/**
	 * 主管理组件
	 */
	protected ModuleControllerAdapter mainModuleController;
	
	/**
	 * 日志策略
	 */
	protected  HashMap mapPolicy = null;

	/**
	 * 平台日志组件构建函数
	 */
	public LoggerAdapter(ModuleControllerAdapter mainModuleController) {
		super();		
		int result = init();
		if (result != MODULE_INITIALIZE_COMPLETE) {
			new ModuleInitializeFailedException(this).printStackTrace();
		}
		result = start();
		if (result != MODULE_START_COMPLETE) {
			new ModuleStartFailedException(this).printStackTrace();
		}
		this.mainModuleController = mainModuleController;
	}
	
	/**
	 * 获取日志格式
	 * 
	 * @return 日志格式
	 */
	protected abstract String getLogLayoutPattern();

	/**
	 * 获取日志记录器记录信息的最低级别
	 * 
	 * @return 记录级别
	 */
	protected abstract Level getLogLevel();

	/**
	 * 将信息记录到文件
	 * 
	 * @param level
	 *            记录日志等级
	 * @param message
	 *            记录的日志对象
	 */
	private void logToFile(Level level, Object message) {
		log(commonLogger, level, message);
	}

	/**
	 * 输出到指定日志记录器
	 * 
	 * @param logger
	 *            指定的记录器
	 * @param level
	 *            日志输出级别
	 * @param message
	 *            日志消息
	 */
	private void log(Logger logger, Level level, Object message) {
		if (message instanceof Exception) {
			logToFile(level, ((Exception) message).getClass()+":"+((Exception) message).getMessage());
			StackTraceElement[] ele = ((Exception) message).getStackTrace();
			for (int i = 0; i < ele.length; i++) {
				log(logger, level, ele[i].toString());
			}
		} else {
			logger.log(fqcn, level, message, null);
			// logger.info(message);
		}
	}

	/**
	 * 记录日志信息，只记录到文本但不记录到数据库
	 * 
	 * @param level
	 *            日志级别
	 * @param message
	 *            日志信息对象
	 */
	public final void log(GeneralModuleIF module, int level, Object message) {

		log(module, level, message, false);
	}

	/**
	 * 记录日志信息
	 * 
	 * @param level
	 *            日志级别
	 * @param message
	 *            日志消息对象
	 * @param alsoLogToDB
	 *            是否同时记录到数据库,在数据库初始化或连接池初始化中，该参数不要设置成true!
	 */
	public final void log(GeneralModuleIF module, int level, Object message,
			boolean alsoLogToDB) {
		// TODO 模块判断，是否需要对该模块进行记录
		if (alsoLogToDB) {
			logToDB(message);
		}

		logToFile(level, message);
	}

	/**
	 * 日志记录
	 * 
	 * @param logger
	 *            记录日志的记录器
	 * @param level
	 *            日志记录等级
	 * @param message
	 *            日志消息对象
	 */
	protected void log(Logger logger, int level, Object message) {
		if (!startFlag) {
			new ModuleStopedException(this).printStackTrace();
			return;
		}
		switch (level) {
		case DEBUG: {
			log(logger, Level.DEBUG, message);
			break;
		}
		case INFO: {
			log(logger, Level.INFO, message);
			break;
		}
		case WARN: {
			log(logger, Level.WARN, message);
			break;
		}
		case ERROR: {
			log(logger, Level.ERROR, message);
			break;
		}
		case FATAL: {
			log(logger, Level.FATAL, message);
			break;
		}
		default: {
			new Exception("NO SUCH LEVEL DEFINED IN SVGLOGGER! Level" + level)
					.printStackTrace();
			break;
		}
		}
	}

	/**
	 * 记录日志信息到文件
	 * 
	 * @param level
	 *            日志级别
	 * @see LoggerAdapter#DEBUG
	 * @see LoggerAdapter#INFO
	 * @see LoggerAdapter#WARN
	 * @see LoggerAdapter#ERROR
	 * @see LoggerAdapter#FATAL
	 * @param message
	 *            日志信息对象
	 */
	protected void logToFile(int level, Object message) {
		log(commonLogger, level, message);
	}

	/**
	 * 将信息记录到数据库，数据库信息一般以INFO级别记录
	 * 
	 * @param message
	 *            日志信息
	 */
	protected abstract void logToDB(Object message);

	public String getModuleType() {
		return "Logger";
	}

}
