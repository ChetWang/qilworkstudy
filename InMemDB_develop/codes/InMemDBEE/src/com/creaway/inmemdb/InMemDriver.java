/*
 * @(#)InMemDriver.java	1.0  08/24/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.creaway.inmemdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.h2.Driver;
import org.h2.engine.Constants;
import org.h2.jdbc.JdbcConnection;
import org.h2.message.DbException;
import org.h2.message.TraceSystem;
import org.h2.upgrade.DbUpgrade;
import org.h2.util.StringUtils;

/**
 * CREAWAY内存数据库驱动类
 * 
 * @author Qil.Wong
 * 
 */
public class InMemDriver extends Driver {

	/**
	 * 是否已经注册当前驱动
	 */
	private static boolean registered = false;

	/**
	 * 驱动的实体对象
	 */
	private static final Driver INSTANCE = new InMemDriver();

	static {
		// 要在h2的Driver加载后重新把它unload掉，否则会引起url重复导致得不到自定义的jdbc connection
		Driver.unload();
		// 重新加载驱动
		load();
	}

	public Connection connect(String url, Properties info) throws SQLException {
		url = parseURL(url);
		try {
			if (info == null) {
				info = new Properties();
			}
			boolean noUpgrade = StringUtils.toUpperEnglish(url).indexOf(
					";NO_UPGRADE=TRUE") >= 0;
			url = StringUtils.replaceAllIgnoreCase(url, ";NO_UPGRADE=TRUE", "");
			if (DbUpgrade.areV1dot1ClassesPresent()) {
				if (noUpgrade) {
					Connection connection = DbUpgrade.connectWithOldVersion(
							url, info);
					if (connection != null) {
						return connection;
					}
				} else {
					DbUpgrade.upgrade(url, info);
				}
			}

			return new JdbcConnection(url, info);
		} catch (Exception e) {
			throw DbException.toSQLException(e);
		}
	}

	/**
	 * 加载驱动。 INTERNAL，内部使用，外部建议使用
	 */
	public static synchronized Driver load() {
		try {
			if (!registered) {
				registered = true;
				DriverManager.registerDriver(INSTANCE);
			}
		} catch (SQLException e) {
			TraceSystem.traceThrowable(e);
		}
		return INSTANCE;
	}

	/**
	 * 卸载驱动。 INTERNAL，内部使用，外部建议使用
	 */
	public static synchronized void unload() {
		try {
			if (registered) {
				registered = false;
				DriverManager.deregisterDriver(INSTANCE);
			}
		} catch (SQLException e) {
			TraceSystem.traceThrowable(e);
		}
	}

	public boolean acceptsURL(String url) {
		return url != null && url.startsWith("jdbc:creaway");
	}

	/**
	 * 将 jdbc.creaway.inmemdb开头的驱动url转化为符合内核需要的url
	 * @param originalDBUrl
	 * @return
	 */
	public static String parseURL(String originalDBUrl) {
		return originalDBUrl.replaceAll("jdbc.creaway.inmemdb", "jdbc:h2")
				+ ";MVCC=TRUE;DB_CLOSE_ON_EXIT=FALSE";
	}

}
