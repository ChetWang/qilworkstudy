/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ�����������־ʵ����
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
	 * ���ݿ��¼�����ڸ�ʽ
	 */
	private SimpleDateFormat dbDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * ���캯��
	 * 
	 * @param obj
	 *            Object ��ʼ���������
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
		// �ṩĬ�ϸ�ʽ
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
				logToFile(ERROR, "��¼���ݿ���־�쳣��SQL��������");
				logToFile(ERROR, e);
			} finally {
				dbAppender.closeConnection(conn);
			}
		} catch (ClassCastException e) {
			logToFile(ERROR, "��¼���ݿ���־�쳣������ת������");
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
		// ������־Ŀ¼
		loggerFolderPath = Global.appRoot + "\\log\\";
		commonLogger = Logger.getLogger(LoggerAdapter.class.getName());
		if (loggerFolderPath != null) {
			File loggerFolder = new File(loggerFolderPath);
			if (!loggerFolder.exists()) {
				loggerFolder.mkdirs();
			}
		}

		// ������־��ʽ
		PatternLayout layout = new PatternLayout(getLogLayoutPattern());

		// ������־��¼�ҽ���
		{
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			commonLogger.addAppender(consoleAppender);
			if (loggerFolderPath != null) {
				DailyRollingFileAppender fileAppender = null;
				try {
					fileAppender = new DailyRollingFileAppender(layout,
							loggerFolderPath + "log.txt", "'.'yyyy-MM-dd");
				} catch (IOException e) {
					System.out.println("�����ļ�·������·����" + loggerFolderPath);
				}
				fileAppender.setEncoding("GBK");
				commonLogger.addAppender(fileAppender);
			}
		}
		// ���ü�¼����
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
	 * ��ȡ��־��¼ʱ��
	 * 
	 * @return ��¼ʱ��
	 */
	protected synchronized String getTime() {
		String time = dbDateFormat.format(new Date());
		time = time.replaceAll(":|-| ", "");
		return time;
	}

	public int start() {
		// TODO Auto-generated method stub
		// �������ģ���б�ת�����ڲ���־�����б�
		mapPolicy = (HashMap) this.getParameter("LOGPOLICY");
		return super.start();
	}

}
