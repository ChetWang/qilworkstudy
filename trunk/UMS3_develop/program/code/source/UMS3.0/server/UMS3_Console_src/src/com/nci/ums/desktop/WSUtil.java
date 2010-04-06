package com.nci.ums.desktop;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import com.nci.ums.desktop.bean.MediaBean;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Res;
import com.nci.ums.v3.service.impl.InterCommandCenter;

public class WSUtil {

	public static String adminApp = "";

	public static String adminAppPsw = "";

	public static String endpoint_manage = "";

//	public static UMS3_ManageStub stub;

	static {
		Properties props = new Properties();
		InputStream is = ClassLoader
				.getSystemResourceAsStream("com/nci/ums/desktop/ws.props");
		try {
			props.load(is);
			is.close();
		} catch (IOException e) {
			Res.logExceptionTrace(e);
		}
		adminApp = props.getProperty("adminApp");
		adminAppPsw = props.getProperty("adminAppPsw");
		endpoint_manage = props.getProperty("endpoint_manage");
//		try {
//			stub = new UMS3_ManageStub(endpoint_manage);
//		} catch (AxisFault e) {
//			e.printStackTrace();
//			ConsoleLogger.log(ConsoleLogger.ERROR, "建立WebService连接失败");
//			ConsoleLogger.logExceptionTrace(e);
//		}
	}

	public static boolean startChannel(MediaBean media) {
		Res.log(Res.INFO, "正在开启渠道:" + media.getMediaID()
				+ "...");
		InterCommandCenter interCenter = new InterCommandCenter();
		Connection conn = null;
		boolean flag = false;
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			conn.setAutoCommit(false);
			PreparedStatement prep = conn
					.prepareStatement("UPDATE MEDIA SET STATUSFLAG="
							+ MediaBean.STATUS_RUNNING
							+ " WHERE MEDIA_ID=? AND TYPE=?");
			prep.setString(1, media.getMediaID());
			prep.setInt(2, media.getMediaType());
			prep.executeUpdate();
			flag = interCenter.startChannel(adminApp, adminAppPsw, media
					.getMediaID(), media.getMediaName(), media.getMediaType());
			if (flag) {
				Res.log(Res.INFO, "开启渠道:"
						+ media.getMediaID() + "成功!");
				conn.commit();
			} else {
				Res.log(Res.INFO, "开启渠道:"
						+ media.getMediaID() + "失败!");
				conn.rollback();
			}
		} catch (Exception e) {
			Res.logExceptionTrace(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Res.logExceptionTrace(e);
				}
			}
		}
		return flag;
	}

	public static boolean stopChannel(MediaBean media) {
		Res.log(Res.INFO, "正在停止渠道:" + media.getMediaID()
				+ "...");
		Connection conn = null;
		boolean flag = false;
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			conn.setAutoCommit(false);
			PreparedStatement prep = conn
					.prepareStatement("UPDATE MEDIA SET STATUSFLAG="
							+ MediaBean.STATUS_STOPPED
							+ " WHERE MEDIA_ID=? AND TYPE=?");
			prep.setString(1, media.getMediaID());
			prep.setInt(2, media.getMediaType());
			prep.executeUpdate();
			InterCommandCenter interCenter = new InterCommandCenter();
			flag = interCenter.stopChannel(adminApp, adminAppPsw, media
					.getMediaID(), media.getMediaName(), media.getMediaType());
			if (flag) {
				conn.commit();
				Res.log(Res.INFO, "停止渠道:"
						+ media.getMediaID() + "成功!");
			} else {
				conn.rollback();
				Res.log(Res.INFO, "停止渠道:"
						+ media.getMediaID() + "失败!");
			}
		} catch (Exception e) {
			Res.logExceptionTrace(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Res.logExceptionTrace(e);
				}
			}
		}

		return flag;
	}

	

}
