package com.nci.ums.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import com.nci.ums.v3.service.ServiceInfo;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

/**
 * UMS�������й��ڷ����ʵ���࣬������ķ����ת����������Ϣ�ȡ�
 * 
 * @author Qil.Wong
 * 
 */
public class AppServiceUtil {

	/**
	 * ��Ч�ġ����ڻ�ķ�����Ϣ
	 */
	private static Map activeServiceMap;
	/**
	 * ת����������Ϣ
	 */
	private static Map forwardServiceMap;

	/**
	 * �����ڲ��Ե�APP
	 */
	private static Map testAppMap;

	private static byte[] lock = new byte[0];

	static {
		synchronized (lock) {
			Connection conn = null;
			activeServiceMap = new ConcurrentHashMap();
			forwardServiceMap = new ConcurrentHashMap();
			try {
				conn = DriverManager.getConnection(DataBaseOp.getPoolName());
				Statement stmt1 = conn.createStatement();
				ResultSet activeServiceResultSet = stmt1
						.executeQuery("SELECT * FROM SERVICE_V3");
				while (activeServiceResultSet.next()) {
					ServiceInfo appService = new ServiceInfo();
					appService.setAppID(activeServiceResultSet
							.getString("APPID"));
					appService.setServiceID(activeServiceResultSet
							.getString("SERVICEID"));
					appService.setServiceName(activeServiceResultSet
							.getString("SERVICENAME"));
					activeServiceMap.put(appService.getServiceID(), appService);
				}
				activeServiceResultSet.close();
				stmt1.close();
				Statement stmt2 = conn.createStatement();
				ResultSet forwardServiceResultSet = stmt2
						.executeQuery("SELECT * FROM FORWARD_SERVICELIST");
				while (forwardServiceResultSet.next()) {
					forwardServiceMap.put(forwardServiceResultSet
							.getString("FORWARDCONTENT"),
							forwardServiceResultSet.getString("SERVICEID"));
				}
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
	 * ��ȡ��Ч�ġ����ڻ�ķ�����Ϣ
	 * 
	 * @return Map ������Ϣ���ϣ�key�Ƿ���ID��Object��ServiceInfo
	 */
	public static Map getActiveAppServiceMap() {
		return activeServiceMap;
	}

	/**
	 * ��ȡת����������Ϣ
	 * 
	 * @return Map ������Ϣ���ϣ�key�Ƿ���ID��Object��ServiceInfo
	 */
	public static Map getForwardServiceMap() {
		return forwardServiceMap;
	}

	public static synchronized boolean isTestApp(String appid) {
		if (testAppMap == null) {
			try {
				testAppMap = new ConcurrentHashMap();
				Properties p = new Properties();
				InputStream is = new ServletTemp()
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
