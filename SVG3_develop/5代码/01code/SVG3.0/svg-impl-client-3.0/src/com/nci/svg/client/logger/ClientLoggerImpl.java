package com.nci.svg.client.logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Inherited;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.module.GeneralModuleIF;
import com.nci.svg.sdk.module.ModuleControllerAdapter;
import com.nci.svg.sdk.module.ModuleInitializeFailedException;
import com.nci.svg.sdk.module.ModuleStartFailedException;
import com.nci.svg.sdk.module.ModuleStopedException;

/**
 * 客户端日志组件对象
 * 
 * @author Qil.Wong
 * 
 */
public class ClientLoggerImpl extends LoggerAdapter {

	public static final String MODULE_ID = "eee45e55-d71a-4b03-a15b-6b889c26efcd";

	/**
	 * 日志组件构造函数
	 * 
	 * @param mainModuleController
	 *            主管理组件
	 */
	public ClientLoggerImpl(ModuleControllerAdapter mainModuleController) {
		super(mainModuleController);
		
		moduleUUID = MODULE_ID;
	}


	@Override
	public int init() {
		try {
			commonLogger = Logger.getLogger(LoggerAdapter.class.getName());
			String loggerFolderPath = Constants.NCI_SVG_LOGGER_DIR;
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
								loggerFolderPath + "log.txt", "'.'yyyy-MM-dd");
					} catch (IOException e) {
						System.out.println("创建文件路径出错！路径：" + loggerFolderPath);
					}
					fileAppender.setEncoding("GBK");
					commonLogger.addAppender(fileAppender);
				}
			}
			// 设置记录级别
			commonLogger.setLevel(getLogLevel());
		} catch (Exception e) {
			e.printStackTrace();
			return GeneralModuleIF.MODULE_INITIALIZE_FAILED;
		}
		return GeneralModuleIF.MODULE_INITIALIZE_COMPLETE;
	}

	@Override
	public int reInit() {
		try {
			commonLogger = null;
			commonLogger = Logger.getLogger(LoggerAdapter.class);
			init();
		} catch (Exception e) {
			e.printStackTrace();
			return GeneralModuleIF.MODULE_INITIALIZE_FAILED;
		}
		return GeneralModuleIF.MODULE_INITIALIZE_COMPLETE;
	}

	@Override
	@Deprecated
	protected void logToDB(Object message) {
		new Exception("Client不提供直接对数据库的访问，请不要使用此类直接和数据库交互的方法！")
				.printStackTrace();
	}

	@Override
	protected String getLogLayoutPattern() {
		// TODO 实现读取远程格式配置

		// 提供默认格式
		return "[%d{ISO8601}: %p %m] %l  %n";
	}

	@Override
	protected Level getLogLevel() {
		if (defaultLevel == null)
			// FIXME 实现获取日志级别读取
			defaultLevel = Level.DEBUG;
		return defaultLevel;
	}

}
