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
 * SVGƽ̨��־��¼���󣬿��Խ���־���������̨��ָ���ļ������ݿ⡣
 * 
 * @author Qil.Wong
 * 
 */
public abstract class LoggerAdapter extends DefaultModuleAdapter {

	// ��ͨ����־��¼������������̨���ı���¼��
	protected Logger commonLogger = null;

	/**
	 * ���Լ�����־��ǩ
	 */
	public final static int DEBUG = 10000;
	/**
	 * ��ʾ������־��ǩ
	 */
	public final static int INFO = 20000;
	/**
	 * ���漶����־��ǩ
	 */
	public final static int WARN = 30000;
	/**
	 * һ���Դ��󼶱���־��ǩ
	 */
	public final static int ERROR = 40000;
	/**
	 * �������󼶱���־��ǩ
	 */
	public final static int FATAL = 50000;

	protected String fqcn = LoggerAdapter.class.getName();

	/**
	 * Ĭ�ϵ���־����
	 */
	protected Level defaultLevel = null;

	/**
	 * ���������
	 */
	protected ModuleControllerAdapter mainModuleController;
	
	/**
	 * ��־����
	 */
	protected  HashMap mapPolicy = null;

	/**
	 * ƽ̨��־�����������
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
	 * ��ȡ��־��ʽ
	 * 
	 * @return ��־��ʽ
	 */
	protected abstract String getLogLayoutPattern();

	/**
	 * ��ȡ��־��¼����¼��Ϣ����ͼ���
	 * 
	 * @return ��¼����
	 */
	protected abstract Level getLogLevel();

	/**
	 * ����Ϣ��¼���ļ�
	 * 
	 * @param level
	 *            ��¼��־�ȼ�
	 * @param message
	 *            ��¼����־����
	 */
	private void logToFile(Level level, Object message) {
		log(commonLogger, level, message);
	}

	/**
	 * �����ָ����־��¼��
	 * 
	 * @param logger
	 *            ָ���ļ�¼��
	 * @param level
	 *            ��־�������
	 * @param message
	 *            ��־��Ϣ
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
	 * ��¼��־��Ϣ��ֻ��¼���ı�������¼�����ݿ�
	 * 
	 * @param level
	 *            ��־����
	 * @param message
	 *            ��־��Ϣ����
	 */
	public final void log(GeneralModuleIF module, int level, Object message) {

		log(module, level, message, false);
	}

	/**
	 * ��¼��־��Ϣ
	 * 
	 * @param level
	 *            ��־����
	 * @param message
	 *            ��־��Ϣ����
	 * @param alsoLogToDB
	 *            �Ƿ�ͬʱ��¼�����ݿ�,�����ݿ��ʼ�������ӳس�ʼ���У��ò�����Ҫ���ó�true!
	 */
	public final void log(GeneralModuleIF module, int level, Object message,
			boolean alsoLogToDB) {
		// TODO ģ���жϣ��Ƿ���Ҫ�Ը�ģ����м�¼
		if (alsoLogToDB) {
			logToDB(message);
		}

		logToFile(level, message);
	}

	/**
	 * ��־��¼
	 * 
	 * @param logger
	 *            ��¼��־�ļ�¼��
	 * @param level
	 *            ��־��¼�ȼ�
	 * @param message
	 *            ��־��Ϣ����
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
	 * ��¼��־��Ϣ���ļ�
	 * 
	 * @param level
	 *            ��־����
	 * @see LoggerAdapter#DEBUG
	 * @see LoggerAdapter#INFO
	 * @see LoggerAdapter#WARN
	 * @see LoggerAdapter#ERROR
	 * @see LoggerAdapter#FATAL
	 * @param message
	 *            ��־��Ϣ����
	 */
	protected void logToFile(int level, Object message) {
		log(commonLogger, level, message);
	}

	/**
	 * ����Ϣ��¼�����ݿ⣬���ݿ���Ϣһ����INFO�����¼
	 * 
	 * @param message
	 *            ��־��Ϣ
	 */
	protected abstract void logToDB(Object message);

	public String getModuleType() {
		return "Logger";
	}

}
