package com.nci.ums.transmit.common;

/**
 * ����ת���ͻ���log�ӿ�
 * 
 * @author Qil.Wong
 * 
 */
public interface UMSTransmitLogger {

	/**
	 * debug level
	 */
	public static final int DEBUG = 0;
	/**
	 * info level
	 */
	public static final int INFO = 1;
	/**
	 * error level
	 */
	public static final int ERROR = 2;
	/**
	 * fatal level
	 */
	public static final int FATAL = 3;

	/**
	 * ��¼��Ϣ
	 * 
	 * @param level
	 *            ��¼����
	 * @see UMSTransmitLogger.DEBUG
	 * @see UMSTransmitLogger.INFO
	 * @see UMSTransmitLogger.ERROR
	 * @see UMSTransmitLogger.FATAL
	 * @param info
	 *            ��¼����
	 */
	public void log(int level, Object info);

}
