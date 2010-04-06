package com.nci.ums.jmx.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nci.ums.media.MediaBean;
import com.nci.ums.util.Res;
import com.nci.ums.v3.service.impl.InterCommandCenter;

public class WSUtil {

	public static boolean startChannel(MediaBean media) {
		Res.log(Res.INFO, "���ڿ�������:" + media.getMediaID()
				+ "...");
		InterCommandCenter interCenter = new InterCommandCenter();
		Connection conn = null;
		boolean flag = false;
		try {
			conn = Res.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement prep = conn
					.prepareStatement("UPDATE UMS_MEDIA SET MEDIA_STATUSFLAG="
							+ MediaBean.STATUS_RUNNING
							+ " WHERE MEDIA_ID=? AND MEDIA_TYPE=?");
			prep.setString(1, media.getMediaID());
			prep.setInt(2, media.getMediaType());
			prep.executeUpdate();
			flag = interCenter.startChannel("DESKTOPADMIN", "", media
					.getMediaID(), media.getMediaName(), media.getMediaType());
			if (flag) {
				Res.log(Res.INFO, "��������:"
						+ media.getMediaID() + "�ɹ�!");
				conn.commit();
			} else {
				Res.log(Res.INFO, "��������:"
						+ media.getMediaID() + "ʧ��!");
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
		Res.log(Res.INFO, "����ֹͣ����:" + media.getMediaID()
				+ "...");
		Connection conn = null;
		boolean flag = false;
		try {
			conn = Res.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement prep = conn
					.prepareStatement("UPDATE UMS_MEDIA SET MEDIA_STATUSFLAG="
							+ MediaBean.STATUS_STOPPED
							+ " WHERE MEDIA_ID=? AND MEDIA_TYPE=?");
			prep.setString(1, media.getMediaID());
			prep.setInt(2, media.getMediaType());
			prep.executeUpdate();
			InterCommandCenter interCenter = new InterCommandCenter();
			flag = interCenter.stopChannel("DESKTOPADMIN", "", media
					.getMediaID(), media.getMediaName(), media.getMediaType());
			if (flag) {
				conn.commit();
				Res.log(Res.INFO, "ֹͣ����:"
						+ media.getMediaID() + "�ɹ�!");
			} else {
				conn.rollback();
				Res.log(Res.INFO, "ֹͣ����:"
						+ media.getMediaID() + "ʧ��!");
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
