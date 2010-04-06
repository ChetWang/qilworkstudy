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
 * �ͻ�����־�������
 * 
 * @author Qil.Wong
 * 
 */
public class ClientLoggerImpl extends LoggerAdapter {

	public static final String MODULE_ID = "eee45e55-d71a-4b03-a15b-6b889c26efcd";

	/**
	 * ��־������캯��
	 * 
	 * @param mainModuleController
	 *            ���������
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
			// ������־Ŀ¼
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
		new Exception("Client���ṩֱ�Ӷ����ݿ�ķ��ʣ��벻Ҫʹ�ô���ֱ�Ӻ����ݿ⽻���ķ�����")
				.printStackTrace();
	}

	@Override
	protected String getLogLayoutPattern() {
		// TODO ʵ�ֶ�ȡԶ�̸�ʽ����

		// �ṩĬ�ϸ�ʽ
		return "[%d{ISO8601}: %p %m] %l  %n";
	}

	@Override
	protected Level getLogLevel() {
		if (defaultLevel == null)
			// FIXME ʵ�ֻ�ȡ��־�����ȡ
			defaultLevel = Level.DEBUG;
		return defaultLevel;
	}

}
