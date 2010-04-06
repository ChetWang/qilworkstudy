/**
 * <p>Title: </p>
 * <p>Description:
 *    
 * </p>
 * <p>Copyright: 2006 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date            Author          Changes          Version
   2006-9-22          荣凯           Created           1.0
 */
package com.nci.scport.ums;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

public class SCSmsDB {
  private static String connectURI;
  private static String pool_name;
  private static String user;
  private static String password;
  private static String drivers;
  
  private static void readConfigInfo() {
	try{
		ConfigReader.init("resources/middledb.props");
		drivers = ConfigReader.getDB_drivers();
	    System.setProperty("jdbc.drivers", drivers);
	    pool_name = ConfigReader.getDB_poolName();
	    connectURI = ConfigReader.getdDB_connURL();
	    user = ConfigReader.getDB_user();
	    password =ConfigReader.getDB_password();
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
    	System.out.println(e.getMessage());
    }

    PoolingDriver driver = new PoolingDriver();

    driver.registerPool(pool_name, connectionPool);

  }	
  
	public static void main(String[] args) {
		// TODO 自动生成方法存根
	
	}
}
