package com.nci.ums.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import com.nci.ums.v3.service.ServiceInfo;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

/**
 * UMS服务器中关于服务的实用类，包含活动的服务和转发服务定义信息等。
 * 
 * @author Qil.Wong
 * 
 */
public class AppServiceUtil {

	/**
	 * 有效的、正在活动的服务信息
	 */
	private static Map activeServiceMap;
	/**
	 * 转发服务定义信息
	 */
	private static Map forwardServiceMap;

	/**
	 * 仅用于测试的APP
	 */
	private static Map testAppMap;

	private static byte[] lock = new byte[0];

	public static void load(){
		synchronized (lock) {
			Connection conn = null;
			activeServiceMap = new ConcurrentHashMap();
			forwardServiceMap = new ConcurrentHashMap();
			try {
				conn = Res.getConnection();
				Statement stmt1 = conn.createStatement();
				ResultSet activeServiceResultSet = stmt1
						.executeQuery("SELECT * FROM UMS_SERVICE");
				while (activeServiceResultSet.next()) {
					ServiceInfo appService = new ServiceInfo();
					appService.setAppKey(activeServiceResultSet
							.getString("APP_KEY"));
					appService.setServiceID(activeServiceResultSet
							.getString("SERVICE_ID"));
					appService.setServiceName(activeServiceResultSet
							.getString("SERVICE_NAME"));
					appService.setServiceKey(activeServiceResultSet
							.getString("SEQKEY"));
					activeServiceMap.put(appService.getServiceKey(), appService);
				}
				activeServiceResultSet.close();
				stmt1.close();
				Statement stmt2 = conn.createStatement();
				ResultSet forwardServiceResultSet = stmt2
						.executeQuery("SELECT * FROM UMS_FORWARD_SERVICE WHERE FORWARD_STATUSFLAG='0'");
				while (forwardServiceResultSet.next()) {
					forwardServiceMap.put(forwardServiceResultSet
							.getString("FORWARD_CONTENT"),
							forwardServiceResultSet.getString("SERVICE_KEY"));
				}
				forwardServiceResultSet.close();
				stmt2.close();
				forwardServiceResultSet.close();
				stmt2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取有效的、正在活动的服务信息
	 * 
	 * @return Map 服务信息集合，key是服务ID，Object是ServiceInfo
	 */
	public static Map getActiveAppServiceMap() {
		return activeServiceMap;
	}

	/**
	 * 获取转发服务定义信息
	 * 
	 * @return Map 服务信息集合，key是服务ID，Object是ServiceInfo
	 */
	public static Map getForwardServiceMap() {
		return forwardServiceMap;
	}

	/**
	 * 获取现有已注册进来的应用，包括有效无效的
	 * 
	 * @return 应用集合信息
	 */
	public static Map getAppMap() {
		return LoginUtil_V3.getAppMap();
	}

	public static synchronized boolean isTestApp(String appid) {
		if (testAppMap == null) {
			try {
				testAppMap = new ConcurrentHashMap();
				Properties p = new Properties();
				InputStream is = new DynamicUMSStreamReader()
						.getInputStreamFromFile("/resources/testApps.prop");
				p.load(is);
				is.close();
				testAppMap.putAll(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (testAppMap.containsKey(appid)) {
			return true;
		} else {
			return false;
		}
	}
}
