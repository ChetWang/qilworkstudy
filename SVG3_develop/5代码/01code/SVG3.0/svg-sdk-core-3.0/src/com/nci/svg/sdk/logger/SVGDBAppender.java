package com.nci.svg.sdk.logger;

import java.sql.Connection;

/**
 * 数据库日志记录器
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
	 * 实现获取数据库连接
	 */
	public abstract Connection getConnection();

	/**
	 * 实现关闭数据库连接
	 */
	public abstract void closeConnection(Connection conn);

	/**
	 * 定义插入语句 
	 * 例如： INSERT INTO LOGGING (log_date, log_level, location, message)
	 * VALUES ('%d{ISO8601}', '%-5p', '%C,%L', '%m')
	 */
	public abstract String getInsertSQL();

}
