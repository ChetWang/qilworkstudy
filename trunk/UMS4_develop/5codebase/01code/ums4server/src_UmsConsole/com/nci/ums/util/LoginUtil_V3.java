package com.nci.ums.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.nci.ums.periphery.application.AppInfo;

public class LoginUtil_V3 {

	private static Map appMap;

	private static byte[] lock = new byte[0];

	public static void load() {
		synchronized (lock) {
			if (appMap == null)
				appMap = new ConcurrentHashMap();
			Connection conn = null;
			try {
				conn = Res.getConnection();
				ResultSet rs = conn.createStatement().executeQuery(
						"SELECT * FROM UMS_APPLICATION");
				while (rs.next()) {
					AppInfo app = new AppInfo();
					app.setAppID(rs.getString("APPT_ID"));
					app.setAppName(rs.getString("APPT_NAME"));
					
					String password = rs.getString("APPT_PASSWORD");
					if(password == null){
						password = "";
					}
					app.setPassword(password);
					app.setStatus(rs.getString("APPT_STATUS"));
					app.setIp(rs.getString("APPT_IP"));
//					app.setTransmitApplicationPort(rs.getInt("APPT_PORT"));
					appMap.put(app.getAppID(), app);
				}
			} catch (SQLException e) {
				Res.logExceptionTrace(e);
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
