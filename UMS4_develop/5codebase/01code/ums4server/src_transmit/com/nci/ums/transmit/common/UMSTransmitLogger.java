package com.nci.ums.transmit.common;

/**
 * 数据转发客户端log接口
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
	 * 记录信息
	 * 
	 * @param level
	 *            记录级别
	 * @see UMSTransmitLogger.DEBUG
	 * @see UMSTransmitLogger.INFO
	 * @see UMSTransmitLogger.ERROR
	 * @see UMSTransmitLogger.FATAL
	 * @param info
	 *            记录对象
	 */
	public void log(int level, Object info);

}
