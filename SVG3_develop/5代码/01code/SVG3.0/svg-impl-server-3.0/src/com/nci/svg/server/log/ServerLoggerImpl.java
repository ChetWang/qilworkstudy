/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：服务器端日志实现类
 *
 */
package com.nci.svg.server.log;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.nci.svg.sdk.logger.LogInfoBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.logger.SVGDBAppender;
import com.nci.svg.sdk.module.GeneralModuleIF;
import com.nci.svg.sdk.module.ModuleStopedException;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.sdk.server.database.DBConnectionManagerAdapter;
import com.nci.svg.sdk.server.util.ServerUtilities;
import com.nci.svg.server.database.DBConnectionManagerImpl;
import com.nci.svg.server.util.Global;

public class ServerLoggerImpl extends LoggerAdapter {

	// private Logger dbLogger = Logger.getLogger(LoggerAdapter.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1093182747039286025L;

	private String loggerFolderPath = null;

	public static final String moduleID = "4002a653-33a4-4327-bc64-88bab0362c54";

	private SVGDBAppender dbAppender = null;

	private static ServerLoggerImpl serverLogger = null;

	/**
	 * 数据库记录的日期格式
	 */
	private SimpleDateFormat dbDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 构造函数
	 * 
	 * @param obj
	 *            Object 初始化所需参数
	 */
	private ServerLoggerImpl(ServerModuleControllerAdapter mainModuleController) {
		super(mainModuleController);
		System.out.println("create ServerLoggerImpl");
	}

	public static synchronized ServerLoggerImpl getInstance(
			ServerModuleControllerAdapter mainModuleController) {
		if (serverLogger == null) {
			serverLogger = new ServerLoggerImpl(mainModuleController);
		}
		return serverLogger;
	}

	protected Level getLogLevel() {
		if (defaultLevel == null)
			defaultLevel = Level.DEBUG;

		return defaultLevel;
	}

	protected String getLogLayoutPattern() {
		// 提供默认格式
		return "[%d{ISO8601}: %p %m] %l  %n";
	}

	protected synchronized void logToDB(Object message) {
		LogInfoBean loginfo;
		try {
			if (message instanceof String) {
				loginfo = new LogInfoBean();
				loginfo.setOperDesc((String) message);
			} else {
				loginfo = (LogInfoBean) message;
			}

			dbAppender = new SVGDBAppender() {
				public void closeConnection(Connection conn) {
					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}

				public Connection getConnection() {
					DBConnectionManagerAdapter dbm = DBConnectionManagerImpl
							.getInstance(this);
					return dbm.getConnection("svg");
				}

				public String getInsertSQL() {
					// return "INSERT INTO t_svg_log_operation
					// (lo_oper_time, lo_oper_type,
					// lo_oper_desc) VALUES ('%d{ISO8601}', '%-5p', '%m')";
					return "INSERT INTO t_svg_log_operation (lo_oper_time, lo_oper_type, lo_oper_desc, lo_id) VALUES (?, ?, ?, ?)";
				}
			};
			// dbLogger.addAppender(dbAppender);
			// dbLogger.setLevel(Level.INFO);
			// dbLogger.log(Level.INFO, message);
			Connection conn = null;
			try {
				conn = dbAppender.getConnection();
				PreparedStatement prep = conn.prepareStatement(dbAppender
						.getInsertSQL());
				prep.setString(1, getTime());
				prep.setString(2, loginfo.getOperType());
				// prep.setString(2, "");
				prep.setString(3, loginfo.getOperDesc());
				prep.setString(4, ServerUtilities.getUUIDString());
				prep.executeUpdate();
			} catch (SQLException e) {
//				e.printStackTrace();
				logToFile(ERROR, "记录数据库日志异常，SQL处理有误！");
				logToFile(ERROR, e);
			} finally {
				dbAppender.closeConnection(conn);
			}
		} catch (ClassCastException e) {
			logToFile(ERROR, "记录数据库日志异常，对象转换错误！");
			logToFile(ERROR, e);
		}
	}

	public String getModuleID() {
		return moduleID;
	}

	public Object handleOper(int index, Object obj)
			throws ModuleStopedException {
		if (!startFlag) {
			throw new ModuleStopedException(this);
		}
		return null;
	}

	public int init() {
		// 创建日志目录
		loggerFolderPath = Global.appRoot + "\\log\\";
		commonLogger = Logger.getLogger(LoggerAdapter.class.getName());
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
		return 0;
	}

	public int reInit() {
		try {
			commonLogger = null;
			commonLogger = Logger.getLogger(LoggerAdapter.class);
			// dbLogger = null;
			// dbLogger = Logger.getLogger(ServerLoggerImpl.class);
			init();
		} catch (Exception e) {
			e.printStackTrace();
			return GeneralModuleIF.MODULE_INITIALIZE_FAILED;
		}
		return GeneralModuleIF.MODULE_INITIALIZE_COMPLETE;
	}

	/**
	 * 获取日志记录时间
	 * 
	 * @return 记录时间
	 */
	protected synchronized String getTime() {
		String time = dbDateFormat.format(new Date());
		time = time.replaceAll(":|-| ", "");
		return time;
	}

	public int start() {
		// TODO Auto-generated method stub
		// 将传入的模块列表转换成内部日志策略列表
		mapPolicy = (HashMap) this.getParameter("LOGPOLICY");
		return super.start();
	}

}
