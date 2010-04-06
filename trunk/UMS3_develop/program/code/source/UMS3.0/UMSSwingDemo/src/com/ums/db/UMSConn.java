/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ums.db;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import java.util.Properties;
import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;


public class UMSConn {

	private static String connectURI;
	private static String pool_name;
	private static String user;
	private static String password;
	private static String drivers;
	private static int maxActive;
	private static byte whenExhaustedAction = new Byte("1").byteValue();
	private static long maxWait;
	private static int maxIdle;

	public static String getDrivers() {
		return drivers;
	}

	public static String getUserName() {
		return user;
	}

	public static String getPassWord() {
		return password;
	}

	public static String getConURI() {
		return connectURI;
	}

	private static void readConfigInfo(Properties props) {
		drivers = props.getProperty("drivers");
		if (drivers == null || drivers.equals("")) {
			throw new NullPointerException("drivers  must not null!");
		}
		System.setProperty("jdbc.drivers", drivers);

		pool_name = props.getProperty("pool_name");
		if (pool_name == null || pool_name.equals("")) {
			throw new NullPointerException("pool_name  must not null!");
		}
		connectURI = props.getProperty("url");
		if (connectURI == null || connectURI.equals("")) {
			pool_name = null;
			drivers = null;
			throw new NullPointerException("connectURI  must not null!");
		}

		user = props.getProperty("user");
		if (user == null || user.equals("")) {
			pool_name = null;
			drivers = null;
			throw new NullPointerException("user  must not null!");
		}

		password = props.getProperty("password");
		if (password == null) {
			pool_name = "";
		}

		maxActive = new Integer(props.getProperty("maxActive")).intValue();
		maxIdle = new Integer(props.getProperty("maxIdle")).intValue();
		maxWait = new Long(props.getProperty("maxWait")).longValue();

	}

	public static String getPoolName() {
		if (pool_name == null) {
			initConnection();
		}
		return "jdbc:apache:commons:dbcp:" + pool_name;
	}

	public static void main(String[] args) {
		initConnection();
	}

	public static synchronized void initConnection() {
		// ���Ϊ�գ���ʾ�Ѿ����й�һ�����ӳؽ����ˣ�ֱ�ӷ��ء�
		if (pool_name != null) {
			return;
		}
		Properties dbProps_ = new Properties();
		System.out.println(System.getProperty("user.dir"));
		try {
			InputStream is = new FileInputStream("conf/umsdb.props");
			dbProps_.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		readConfigInfo(dbProps_);
		setupDriver();
	}

	private static void setupDriver() {
		try {
			ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
					connectURI, user, password);
			ObjectPool connectionPool = new GenericObjectPool(null, maxActive,
					whenExhaustedAction, maxWait, maxIdle, false, false, 3000,
					5, 1000, false);

			try {
				PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
						connectionFactory, connectionPool, null, null, false,
						true);
				// connectionPool.setFactory(poolableConnectionFactory);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Finally, we create the PoolingDriver itself...
			//
			PoolingDriver driver = new PoolingDriver();

			//
			// ...and register our pool with it.
			//
			driver.registerPool(pool_name, connectionPool);
			DriverManager.registerDriver(driver);
			//
			// Now we can just use the connect string
			// "jdbc:apache:commons:dbcp:example"
			// to access our pool of Connections.
			//
		} catch (SQLException ex) {
			Logger.getLogger(UMSConn.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		//
		// Now we can just use the connect string
		// "jdbc:apache:commons:dbcp:example"
		// to access our pool of Connections.
		//
	}
}
