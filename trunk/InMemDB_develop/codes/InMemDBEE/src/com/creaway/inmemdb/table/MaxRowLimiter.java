/*
 * @(#)MaxRowLimiter.java	1.0  09/26/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.creaway.inmemdb.util.ConnectionManager;
import com.creaway.inmemdb.util.DBLogger;

/**
 * 数据库表行数限制器
 * 
 * @author Qil.Wong
 * 
 */
public abstract class MaxRowLimiter extends Thread {

	/**
	 * 将要提交行数最大校验处理的缓存对象，key为表名，value为允许行数最大值，这里用Map的好处是如果前一阶段的校验还未结束，
	 * 后续命令会覆盖前一命令，不用重复执行
	 */
	protected Map<String, Integer> tableActions = new ConcurrentHashMap<String, Integer>();

	/**
	 * 线程存活标记
	 */
	protected boolean flag = true;

	/**
	 * 线程运行锁
	 */
	protected Object lock = new Object();

	/**
	 * 表最大行数在sql语句中的正则代码
	 */
	public static final String regExp_count = "xxyyzz";

	/**
	 * 表名称在sql语句中的正则代码
	 */
	public static final String regExp_tableName = "aabbccdd";

	public MaxRowLimiter() {

	}

	/**
	 * 获取行数限制器对应的数据库的连接
	 * 
	 * @return
	 */
	public abstract Connection getConnection();

	/**
	 * 释放连接，不同的行数限制器实现类可能对释放连接有不同的定义，需要特别实现
	 * 
	 * @param conn
	 */
	protected void releaseConnection(Connection conn) {
		ConnectionManager.releaseConnection(conn);
	}

	/**
	 * 唤醒行数限制器，执行命令
	 * 
	 * @param tableName
	 * @param maxRowCount
	 */
	public void fire(String tableName, int maxRowCount) {
		tableActions.put(tableName, maxRowCount);
		notifyLock();
	}

	public void run() {

		while (flag) {
			Iterator<String> tables = tableActions.keySet().iterator();
			Connection conn = getConnection();
			boolean hasData = false;
			try {
				while (tables.hasNext() && flag) {
					hasData = true;
					String tableName = tables.next();
					int maxRowCount = tableActions.get(tableName);
					tableActions.remove(tableName);
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM "
							+ tableName);
					int rows = 0;
					while (rs.next()) {
						rows = rs.getInt(1);
					}
					rs.close();
					if (rows > maxRowCount) {
						int topCount = rows - maxRowCount;
						String sql = getTemplateDeleteSQL(tableName, topCount);
						st.execute(sql);
						DBLogger.log(DBLogger.DEBUG, getName() + "   "
								+ topCount + "  " + sql);
					}
					st.close();
				}
				if (hasData && flag)
					conn.commit();
			} catch (SQLException e) {
				DBLogger.log(DBLogger.ERROR, getName()+" "+e.getMessage(), e);
			} finally {
				releaseConnection(conn);
				if (!hasData) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						DBLogger.log(DBLogger.ERROR, getName() + "线程休眠冲突", e);
					}
				}

				// synchronized (lock) {
				// try {
				// lock.wait();
				// } catch (InterruptedException e1) {
				// e1.printStackTrace();
				// }
				// }
			}
		}

	}

	/**
	 * 唤醒行数检查线程
	 */
	public void notifyLock() {
		// synchronized (lock) {
		// lock.notify();
		// }
	}

	public void stopLimiter() {
		flag = false;
		notifyLock();
	}

	/**
	 * 删除语句的模板，行数用xxyyzz代替,tableName用aabbccdd代替
	 * 
	 * @return
	 */
	public abstract String getTemplateDeleteSQL(String tableName, int rowCount);

}
