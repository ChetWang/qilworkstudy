/*
 * 创建日期 2006-3-22
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.nci.nbport.ums;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class NBSmsDb {

	  private static String connectURI;
	  private static String pool_name;
	  private static String user;
	  private static String password;
	  private static String drivers;

	  
//	  private static void readConfigInfo() {
//	    String drivers ="oracle.jdbc.driver.OracleDriver";
//
////	    pool_name = "NBSMS";
////	    connectURI = "jdbc:oracle:thin:@172.16.1.6:1521:oracle";
////	    user = "fan";
////	    password ="qq";
//	    
//	    pool_name = "UMS";
//	    connectURI = "jdbc:oracle:thin:@169.169.17.253:1521:ncirk";
//	    user = "ums";
//	    password ="123456";
//
//	  }
	  private static void readConfigInfo() {
			try{
				ConfigReader.init("yddb.props");
				drivers = ConfigReader.getDB_drivers();
			    System.setProperty("jdbc.drivers", drivers);
			    pool_name = ConfigReader.getDB_poolName();
			    connectURI = ConfigReader.getdDB_connURL();
			    user = ConfigReader.getDB_user();
			    password =ConfigReader.getDB_password();
			    System.out.println(connectURI);
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
	  }

	  public static String getPoolName() {
	    if (pool_name == null) {
	      initConnection();
	    }
	    return "jdbc:apache:commons:dbcp:" + pool_name;
	  }

	  public synchronized static void initConnection() {
	    //如果不为空，表示已经进行过一次连接池建立了，直接返回。
	    if(pool_name!=null)
	      return ;
	    readConfigInfo();
	    setupDriver();
	  }

	  private static void setupDriver() {
	    ObjectPool connectionPool = new GenericObjectPool(null);

	    ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
	        connectURI, user,password);

	    try {
	      PoolableConnectionFactory poolableConnectionFactory = new
	          PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
	    }
	    catch (Exception e) {

	    }

	    PoolingDriver driver = new PoolingDriver();

	    driver.registerPool(pool_name, connectionPool);

	  }	
}
