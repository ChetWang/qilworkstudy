/*
 * @(#)SlaveConnectionFactory.java	1.0  09/06/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

import com.creaway.inmemdb.InMemSession;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.util.DBLogger;

/**
 * 内部从属连接生成工厂
 * 
 * @author Qil.Wong
 * 
 */
public class SlaveConnectionFactory {

	// 缓存的内部从属连接
	private static LinkedBlockingQueue<Connection> unused;

	private static InMemSession session;

	// INNER USEAGE
	public static void init() {
		int size = Integer.parseInt(InMemDBServer.getInstance()
				.getSystemProperties().get("slave_conn_counts").toString());
		unused = new LinkedBlockingQueue<Connection>(size);
		session = InMemDBServer.getInstance().createDataBaseSession();
		session.setSlave(true);
		for (int i = 0; i < size; i++) {
			Connection c = session.createConnection();
			Connection proxy = (Connection) Proxy.newProxyInstance(c.getClass()
					.getClassLoader(), c.getClass().getInterfaces(),
					new ConnectionCloseHandler(c));
			unused.add(proxy);
		}
	}

	// INNER USAGE
	public static void destroy() {
		if (session != null)
			session.close();
		if (unused != null) {
			unused.clear();
			unused = null;
		}
	}

	/**
	 * 获取从属连接
	 * 
	 * @return 从属连接
	 */
	public synchronized static Connection getSlaveConnection() {
		try {
			DBLogger.log(DBLogger.DEBUG, "Slave Connection Pool 空闲连接数:"
					+ unused.size());
			return unused.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 释放从属连接
	 * 
	 * @param conn
	 *            从属连接
	 */
	public synchronized static void releaseSlaveConnection(Connection conn) {
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "释放从属连接失败", e);
		} finally {
			unused.add(conn);
		}
	}

	public synchronized static void rollbackSlaveConnection(Connection conn) {
		try {
			conn.rollback();
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "rollback从属连接失败", e);
		}
	}

	/**
	 * 外部调用时，需要将Connection的close方法封装为释放连接
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private static class ConnectionCloseHandler implements InvocationHandler {

		/**
		 * 将要被封装的Connectin对象
		 */
		private Connection delegate;

		public ConnectionCloseHandler(Connection delegate) {
			this.delegate = delegate;
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			Object o = null;
			if (method.getName().equals("close")) {
				SlaveConnectionFactory.releaseSlaveConnection(delegate);
			} else {
				o = method.invoke(delegate, args);
			}
			return o;
		}
	}

}
