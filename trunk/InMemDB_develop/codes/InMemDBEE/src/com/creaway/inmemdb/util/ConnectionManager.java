/*
 * @(#)ConnectionManager.java	1.0  08/27/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.util;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.persistent.PersistentBackupRestoreIfc;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据库管理器
 * 
 * @author Qil.Wong
 * 
 */
public class ConnectionManager {

	private static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
	private static String defaultSource;

	/**
	 * 加载，内部使用
	 */
	public static void load() {
		// 清空先前已有的数据源
		Iterator<DataSource> it = dataSources.values().iterator();
		while (it.hasNext()) {
			DataSource source = it.next();
			if (source instanceof ComboPooledDataSource) {
				((ComboPooledDataSource) source).close();
			}
		}
		defaultSource = null;
		Document config = Functions.getXMLDocument(ConnectionManager.class
				.getResource("/resources/datasource.xml"));
		NodeList sourceList = config.getElementsByTagName("pool");
		for (int i = 0; i < sourceList.getLength(); i++) {
			Element pool = (Element) sourceList.item(i);
			String poolType = pool.getAttribute("type");
			String name = pool.getAttribute("name");
			boolean defaultPool = "true".equals(pool.getAttribute("default")
					.toLowerCase());
			if (defaultPool)
				defaultSource = name;
			if (dataSources.containsKey("name")) {
				DBLogger.log(DBLogger.ERROR, "多个数据源具有同一个名称:" + name);
				break;
			}
			if (poolType.equals("jdbc")) {
				initJdbcDataSource(pool);
			} else if (poolType.equals("jndi")) {
				initJndiDataSource(pool);
			} else {
				DBLogger.log(DBLogger.ERROR, "没有此种匹配的数据库连接池类型：" + poolType);
				break;
			}
		}
	}

	/**
	 * 初始化外部jndi连接池，这个池不受内存数据库控制
	 * 
	 * @param pool
	 */
	private static void initJndiDataSource(Element pool) {
		String name = pool.getAttribute("name");
		DataSource ds = null;
		try {
			Context initCtx = new InitialContext();
			String javaEnv = pool.getAttribute("jndiprefix");
			if (javaEnv != null && !javaEnv.equals("")) {
				String jndiname = pool.getAttribute("jndiname");
				Context envCtx = (Context) initCtx.lookup(javaEnv);
				ds = (DataSource) envCtx.lookup(jndiname);

			} else {
				String jndiname = pool.getAttribute("jndiname");
				ds = (DataSource) initCtx.lookup(jndiname);
			}
			dataSources.put(name, ds);
		} catch (NamingException e) {
			DBLogger.log(DBLogger.ERROR, "", e);
		}
	}

	/**
	 * 初始化jdbc连接池，这个池是内存数据库应用程序自己维护的
	 * 
	 * @param pool
	 * @return
	 */
	private static void initJdbcDataSource(Element pool) {
		ComboPooledDataSource pooledDataSource = new ComboPooledDataSource(true);
		// PoolBackedDataSource p = new PoolBackedDataSource();
		String name = pool.getAttribute("name");
		dataSources.put(name, pooledDataSource);

		NodeList paramConfig = pool.getElementsByTagName("param");
		for (int n = 0; n < paramConfig.getLength(); n++) {
			Element param = (Element) paramConfig.item(n);
			String paramName = param.getAttribute("name").toLowerCase();
			String value = param.getAttribute("value");
			if (paramName.equals("driver")) {
				try {
					pooledDataSource.setDriverClass(value);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
			} else if (paramName.equals("url")) {
				pooledDataSource.setJdbcUrl(value);
			} else if (paramName.equals("user")) {
				pooledDataSource.setUser(value);
			} else if (paramName.equals("password")) {
				pooledDataSource.setPassword(value);
			} else if (paramName.equals("initial_size")) {
				pooledDataSource.setInitialPoolSize(Integer.parseInt(value));
			} else if (paramName.equals("max_size")) {
				pooledDataSource.setMaxPoolSize(Integer.parseInt(value));
			} else if (paramName.equals("min_size")) {
				pooledDataSource.setMinPoolSize(Integer.parseInt(value));
			} else if (paramName.equals("idle_time")) {
				pooledDataSource.setMaxIdleTime(Integer.parseInt(value));
			}
		}

		if (name.toLowerCase().equals("inner")) {
			Properties systemP = InMemDBServer.getInstance()
					.getSystemProperties();
			try {
				pooledDataSource.setDriverClass(systemP.getProperty("memdb_driver"));
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
			pooledDataSource.setUser(systemP.getProperty("memdb_user"));
			pooledDataSource.setJdbcUrl(systemP.getProperty("memdb_url"));
			pooledDataSource.setPassword(systemP.getProperty("memdb_password"));
		}
		pooledDataSource.setIdleConnectionTestPeriod(60);
		pooledDataSource.setAutoCommitOnClose(true);
		pooledDataSource.setTestConnectionOnCheckout(true);
		pooledDataSource.setTestConnectionOnCheckin(true);
		//定义在从数据库获取新连接失败后重复尝试的次数。Default: 30 acquireRetryAttempts
		pooledDataSource.setAcquireRetryAttempts(300000);
		//两次连接中间隔时间，单位毫秒。Default: 1000 acquireRetryDelay
		pooledDataSource.setAcquireRetryDelay(3000);
		pooledDataSource.setAutomaticTestTable("INMEMDB_POOL_DO_NOT_DELETE");
		pooledDataSource.setAutoCommitOnClose(true); 
	}

	/**
	 * 获取数据库连接，取的是默认的数据源中的链接, 默认都取消autocommit
	 * 
	 * @return 数据库连接
	 */
	public synchronized static Connection getConnection() {
		return getConnection(defaultSource);
		// Session session =
		// InMemDBServer.getInstance().createDataBaseSession();
		// return session.createConnection(false);
	}

	/**
	 * 获取数据库连接，取的是指定的数据源中的链接,默认都取消autocommit
	 * 
	 * @param 数据源的名称
	 * @return 数据库连接
	 */
	public synchronized static Connection getConnection(String poolName) {
		Connection conn = null;
		try {
			DataSource datasource = dataSources.get(poolName);
			if (datasource == null) {
				DBLogger.log(DBLogger.ERROR, "获取数据库连接错误,无指定名称的数据源：" + poolName);
				return null;
			}
			conn = datasource.getConnection();
			if (conn != null) {
				// 所有事物都必须手动开启，auto Commit取消
				conn.setAutoCommit(false);
			}
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "获取数据库连接错误，pool: " + poolName, e);
		}
		return conn;
	}

	/**
	 * 释放数据库连接
	 * 
	 * @param conn
	 *            待释放的连接
	 */
	public synchronized static void releaseConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();// 在NewProxyConnection中，真正Connection对象被释放前，将其设置为slave=false
			} catch (SQLException e) {
				DBLogger.log(DBLogger.ERROR, "释放数据库连接错误", e);
			}
		}
	}

	/**
	 * 回滚事务
	 * 
	 * @param conn
	 */
	public synchronized static void rollbackConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				DBLogger.log(DBLogger.ERROR, "回滚数据库连接错误", e);
			}
		}
	}

	/**
	 * 获取持久层的连接
	 * 
	 * @return
	 */
	public synchronized static Connection getPersistentConnection() {
		return getConnection(PersistentBackupRestoreIfc.DATASOURCE_NAME);
	}

}
