package com.nci.ums.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.nci.ums.periphery.application.AppInfo;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

public class LoginUtil_V3 {

	private static Map appMap;

	private static byte[] lock = new byte[0];

	static {
		synchronized (lock) {
			if (appMap == null)
				appMap = new ConcurrentHashMap();
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(DataBaseOp.getPoolName());
				ResultSet rs = conn.createStatement().executeQuery(
						"SELECT * FROM APPLICATION");
				while (rs.next()) {
					AppInfo app = new AppInfo();
					app.setAppID(rs.getString("APPID"));
					app.setAppName(rs.getString("APPNAME"));
					app.setPassword(rs.getString("PASSWORD"));
					app.setStatus(rs.getString("STATUS"));
					appMap.put(app.getAppID(), app);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	}

	public static Map getAppMap() {
		return appMap;
	}
}
