/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.v3.message.euis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nci.ums.util.Res;
import com.nci.ums.v3.message.basic.Participant;

/**
 * 
 * @author Qil.Wong
 */
public class UUMUserInfo implements EnterpriseUserInfo {

	private static final long serialVersionUID = 4661667938215892669L;

	private static final String UUM_USER_TABLE = "UUM_USER";

	public UUMUserInfo() {
	}

	public String getAccout(Participant user_previous,
			int neededParticipantIDType) {
		if (user_previous.getIDType() == neededParticipantIDType)
			return user_previous.getParticipantID();
		String account = null;
		// 得到UUM_USER表相关列的名称
		String neededColumnName = this
				.getUUMColumnName(neededParticipantIDType);
		try {
			account = this.query(neededColumnName, user_previous);
		} catch (SQLException e) {
			Res.log(Res.ERROR, "UUM用户查询出错。");
			Res.logExceptionTrace(e);
		}
		return account;
	}

	public String getEmailAccout(Participant user_previous) {
		return getAccout(user_previous, Participant.PARTICIPANT_ID_EMAIL);
	}

	public String getLcsAccount(Participant user_previous) {
		return getAccout(user_previous, Participant.PARTICIPANT_ID_LCS);
	}

	public String getMobileAccout(Participant user_previous) {
		return getAccout(user_previous, Participant.PARTICIPANT_ID_MOBILE);
	}

	private Connection getDBConnection() throws SQLException {
		return Res.getConnection();
	}

	private String query(String neededColumnName, Participant user_previous)
			throws SQLException {
		String account = null;
		String previousColumnName = this.getUUMColumnName(user_previous
				.getIDType());
		String previousColumnValue = user_previous.getParticipantID();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(neededColumnName);
		sql.append(" FROM ");
		sql.append(UUM_USER_TABLE);
		sql.append(" WHERE ");
		sql.append(previousColumnName).append("=?");
		Connection conn = this.getDBConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setString(1, previousColumnValue);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				account = rs.getString(1);
			}
		} finally {
			if (conn != null)
				conn.close();
		}
		return account;
	}

	private String getUUMColumnName(int idType) {
		switch (idType) {

		case Participant.PARTICIPANT_ID_EMAIL:
			return "USER_EMAIL";
		case Participant.PARTICIPANT_ID_MOBILE:
			return "USER_MOBILE";
		case Participant.PARTICIPANT_ID_LCS:
			return "USER_UNUSED1";
		case Participant.PARTICIPANT_ID_UUM_USERID:
			return "USER_ID";
			// case Participant.PARTICIPANT_ID_UUM_USERID:
			// return
			// Appservice类型的用户只负责单向的收和发，没有其它option选项； WebUser只负责发。
			// 因此，这两个无需在uum中对应多个字段。
		}
		return null;
	}

	/**
	 * 获取策略的sql
	 * @param receiver
	 * @return
	 */
	public String getStasticSQL(Participant receiver) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.media_id, a.media_style,").append(
				" c.user_mobile, c.user_email").append(
				" FROM ums_media a, ums_tactics b, uum_user c").append(
				" WHERE a.seqkey = b.media_key AND b.tact_type = '1'").append(
				" AND b.person_key = c.user_id AND ");
		String userid = getUserIdSql(receiver.getIDType(), receiver
				.getParticipantID());
		sql.append(userid);
		sql.append(" ORDER BY b.tact_media_priority");
		return sql.toString();
	}

	/**
	 * @功能 获取用户编号的SQL语句
	 * @param idType
	 *            int 通道类型
	 * @param receive
	 *            String 接收者标识
	 * @return
	 * 
	 * @Add by ZHM 2009-11-2
	 */
	private String getUserIdSql(int idType, String receive) {
		StringBuffer sql = new StringBuffer();
		if (Participant.PARTICIPANT_ID_MOBILE == idType) {
			// 通道是手机
			sql.append("c.user_mobile='");
		} else if (Participant.PARTICIPANT_ID_EMAIL == idType) {
			// 通道是电邮
			sql.append("c.user_email='");
		} else if (Participant.PARTICIPANT_ID_LCS == idType) {
			sql = null;
		} else if (Participant.PARTICIPANT_ID_WEBUSER == idType) {
			sql = null;
		} else if (Participant.PARTICIPANT_ID_APPSERVICE == idType) {
			sql = null;
		} else if (Participant.PARTICIPANT_ID_UUM_USERID == idType) {
			sql = null;
		} else {
			sql = null;
		}

		if (sql != null) {
			sql.append(receive).append("'");
		} else {
			sql = new StringBuffer();
			sql.append("true = false");
		}

		return sql.toString();
	}

}
