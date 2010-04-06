package com.nci.svg.sdk.logger;

import java.sql.Connection;

/**
 * ���ݿ���־��¼��
 * 
 * @author Qil.Wong
 * 
 */
public abstract class SVGDBAppender {

	public SVGDBAppender() {
//		super();
//		sqlStatement = getInsertSQL();
//		layout = new PatternLayout("%d{ISO8601}:%p --> %m  [%l]%n");
	}

	/**
	 * ʵ�ֻ�ȡ���ݿ�����
	 */
	public abstract Connection getConnection();

	/**
	 * ʵ�ֹر����ݿ�����
	 */
	public abstract void closeConnection(Connection conn);

	/**
	 * ���������� 
	 * ���磺 INSERT INTO LOGGING (log_date, log_level, location, message)
	 * VALUES ('%d{ISO8601}', '%-5p', '%C,%L', '%m')
	 */
	public abstract String getInsertSQL();

}
