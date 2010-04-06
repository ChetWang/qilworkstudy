package com.nci.ums.util;

//
// Here are the dbcp-specific classes.
// Note that they are only used in the setupDriver
// method. In normal use, your classes interact
// only with the standard JDBC API
//
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class DataBaseOp {

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
	
	public static void clear(){
		pool_name = null;
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
		// 如果不为空，表示已经进行过一次连接池建立了，直接返回。
		if (pool_name != null) {
			return;
		}
		Properties dbProps_ = new Properties();
		try {
			// InputStream is =
			// ClassLoader.getSystemResourceAsStream("../umsdb.props");
			InputStream is = new ServletTemp()
					.getInputStreamFromFile("/resources/umsdb.props");
			dbProps_.load(is);
			System.out.println(dbProps_);
			is.close();
		} catch (Exception e) {
			throw new NullPointerException("load file error!");
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
			Logger.getLogger(DataBaseOp.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		//
		// Now we can just use the connect string
		// "jdbc:apache:commons:dbcp:example"
		// to access our pool of Connections.
		//
	}

	public static String getConnectURI() {
		return connectURI;
	}

	public static void setConnectURI(String connectURI) {
		DataBaseOp.connectURI = connectURI;
	}
}