/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.v3.message.euis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nci.ums.util.DataBaseOp;
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
		if(user_previous.getIDType() == neededParticipantIDType)
			return user_previous.getParticipantID();
		String account = null;
		//�õ�UUM_USER������е�����
		String neededColumnName = this
				.getUUMColumnName(neededParticipantIDType);
		try {
			account = this.query(neededColumnName, user_previous);
		} catch (SQLException e) {
			Res.log(Res.ERROR, "UUM�û���ѯ����");
                        Res.logExceptionTrace(e);
		}
		return account;
	}

	public String getEmailAccout(Participant user_previous) {		
		return getAccout(user_previous,Participant.PARTICIPANT_ID_EMAIL);
	}

	public String getLcsAccount(Participant user_previous) {
		return getAccout(user_previous,Participant.PARTICIPANT_ID_LCS);
	}

	public String getMobileAccout(Participant user_previous) {
		return getAccout(user_previous,Participant.PARTICIPANT_ID_MOBILE);
	}

//	public String getWebLoginAccount(Participant user_previous) {
//
//		return null;
//	}

	private Connection getDBConnection() throws SQLException {
		return DriverManager.getConnection(DataBaseOp.getPoolName());
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
			// Appservice���͵��û�ֻ��������պͷ���û������optionѡ� WebUserֻ���𷢡�
			// ��ˣ�������������uum�ж�Ӧ����ֶΡ�
		}
		return null;
	}

}
