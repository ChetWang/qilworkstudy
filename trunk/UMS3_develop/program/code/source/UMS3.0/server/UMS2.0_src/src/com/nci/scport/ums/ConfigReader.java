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

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
	private static String db_drivers;
	private static String db_connURL;
	private static String db_user;
	private static String db_password;
	private static String db_poolName;
	
	public synchronized static void init(String configFile)  throws Exception{
//		FileInputStream fl = new FileInputStream(configFile);
		Properties props = new Properties();
		props.load(ClassLoader.getSystemResourceAsStream(configFile));
		
		db_drivers = props.getProperty("db_drivers");
	    if (db_drivers == null || db_drivers.equals(""))
	        throw new NullPointerException("db_drivers  must not null!");
		
		db_connURL = props.getProperty("db_connURL");
	    if (db_connURL == null || db_connURL.equals(""))
	        throw new NullPointerException("db_connURL  must not null!");
		
		db_user = props.getProperty("db_user");
	    if (db_user == null || db_user.equals(""))
	        throw new NullPointerException("db_user  must not null!");
		
		db_password = props.getProperty("db_password");
	    if (db_password == null)
	    	db_password = "";
		
		db_poolName = props.getProperty("db_poolName");
	    if (db_poolName == null || db_poolName.equals(""))
	        throw new NullPointerException("db_poolName  must not null!");
	}
	
	public static String getDB_drivers(){
		return db_drivers;
	}
	
	public static String getdDB_connURL(){
		return db_connURL;
	}
	
	public static String getDB_user(){
		return db_user;
	}
	
	public static String getDB_password(){
		return db_password;
	}
	
	public static String getDB_poolName(){
		return db_poolName;
	}
	
	public static void main(String[] args) {
		// TODO 自动生成方法存根

	}

}
