package com.nci.ums.tactics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.nci.ums.util.DialectUtil;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.message.euis.EnterpriseUserInfo;

/**
 * <p>
 * ��Ŀ����: ums4server
 * </p>
 * <p>
 * package com.nci.ums.tactics;
 * </p>
 * <p>
 * ����: ChannelPolicy.java
 * </p>
 * <p>
 * ����:
 * <p>
 * �汾:Version 1.0
 * </p>
 * <p>
 * ��Ȩ: 2003 Hangzhou NCI System Engineering�� Ltd.
 * </p>
 * <p>
 * ��˾: Hangzhou NCI System Engineering�� Ltd.
 * </p>
 * 
 * @author ����:shenyg E-mail: shenyg@nci.com.cn
 * @version ����ʱ�䣺2009-10-19 ����10:59:43 �޸ļ�¼ ���� ���� �޸����� ����
 * @version �޸�ʱ�䣺2009-11-02 �����޸�
 */
public class ChannelPolicy {

	private static EnterpriseUserInfo eui = Util.getEuiObject();

	/**
	 * 
	 * implChannelPolicy:����ѡ��
	 * 
	 * @param umsMsg
	 * @param isNextPriority
	 *            boolean ǰ���Ƿ��Ѿ���������
	 * 
	 * @return void
	 * @throws SQL����ʧ����Ϣ
	 */
	public static void implChannelPolicy(UMSMsg umsMsg, boolean isNextPriority)
			throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// ��ȡ���ݿ�����
			conn = Res.getConnection();
			// ������Ϣ��������ж����� ��ȡ���˱�� �ڻ�ȡ��ǰ���˲�����Ϣ
			Participant[] receviers = umsMsg.getBasicMsg().getReceivers();
			int recevierSize = receviers.length;

			for (int i = 0; i < recevierSize; i++) {
				stmt = conn.createStatement();
				String sql = eui.getStasticSQL(receviers[i]);
				Res.log(Res.DEBUG, sql.toString());
				rs = stmt.executeQuery(sql.toString());
				// ��ȡ���˲�������
				if (rs.next()) {
					setUmsMsgPerson(umsMsg, rs, isNextPriority, i);
				} else {
					// ��������ڸ��˲������� ��ô��ȡ�����������
					// ��ȡ������Ϣ
					String[] message;
					if (receviers.length > 0) {
						message = getUserPhoneAndEmail(
								receviers[i].getIDType(), receviers[i]
										.getParticipantID());

						// ��ȡ�����������
						StringBuffer sql2 = new StringBuffer();
						sql2
								.append("SELECT a.media_id, a.media_style ")
								.append(
										"FROM ums_media a, ums_tactics b, ums_service c ")
								.append("WHERE a.seqkey = b.media_key").append(
										" AND b.tact_type='2'").append(
										" AND b.service_key = c.seqkey")
								.append(" AND c.service_id = '").append(
										umsMsg.getBasicMsg().getServiceID())
								.append("' ORDER BY b.tact_media_priority");
						stmt = conn.createStatement();
						Res.log(Res.DEBUG, sql2.toString());
						rs = stmt.executeQuery(sql2.toString());
						// �趨������
						if (rs.next()) {
							setUmsMstApp(umsMsg, rs, message);
						}
					}
				}
			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				rs.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Res.logExceptionTrace(e);
			}
		}
	}

	/**
	 * �����û���Ϣ��ȡ��ϵͳ�и��û��ֻ���Email��Ϣ
	 * 
	 * @param idType
	 *            int ͨ������
	 * @param receive
	 *            String �����߱�ʶ
	 * @return String[2] [0]�ֻ�,[1]Email
	 */
	private static String[] getUserPhoneAndEmail(int idType, String receive) {
		String[] message = new String[2];
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT user_mobile, user_email FROM uum_user WHERE ");
		if (Participant.PARTICIPANT_ID_MOBILE == idType) {
			// ͨ�����ֻ�
			sql.append("user_mobile='").append(receive).append("'");
			message[0] = receive;
		} else if (Participant.PARTICIPANT_ID_EMAIL == idType) {
			// ͨ���ǵ���
			sql.append("user_email='").append(receive).append("'");
			message[1] = receive;
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
			Statement stmt = null;
			ResultSet rs = null;
			Connection conn = null;
			// ��ȡ���ݿ�����
			conn = Res.getConnection();
			try {
				stmt = conn.createStatement();
				Res.log(Res.DEBUG, sql.toString());
				rs = stmt.executeQuery(sql.toString());
				if (rs.next()) {
					// ��ȡ���ݿ����û���Ϣ
					if (message[0] == null) {
						message[0] = rs.getString("user_mobile");
					}
					if (message[1] == null) {
						message[1] = rs.getString("user_email");
					}
				}
			} catch (SQLException e) {
				Res.logExceptionTrace(e);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					rs.close();
					if (conn != null)
						conn.close();
				} catch (Exception e) {
					Res.logExceptionTrace(e);
				}
			}
		}

		return message;
	}

	/**
	 * @���� ���ݴ����ݿ��л�ȡ����Ӧ�ò�������������Ϣ����
	 * @param umsMsg
	 * @param rs
	 * @param message
	 *            String[2] �û���Ϣ[0]�ֻ�[1]Email
	 * @throws SQLException
	 * 
	 * @Add by ZHM 2009-11-2
	 */
	private static void setUmsMstApp(UMSMsg umsMsg, ResultSet rs,
			String[] message) throws SQLException {
		String mediaID = rs.getString("media_id");
		int idType = rs.getInt("media_style");
		umsMsg.getBasicMsg().setMediaID(mediaID);
		if (Participant.PARTICIPANT_ID_MOBILE == idType && message[0] != null) {
			// ͨ�����ֻ�
			umsMsg.getBasicMsg().getReceivers()[0].setParticipantID(message[0]);
			umsMsg.getBasicMsg().getReceivers()[0]
					.setIDType(Participant.PARTICIPANT_ID_MOBILE);
			umsMsg.getBasicMsg().getSender().setParticipantID(
					umsMsg.getBasicMsg().getServiceID());
		} else if (Participant.PARTICIPANT_ID_EMAIL == idType
				&& message[1] != null) {
			// ͨ���ǵ���
			umsMsg.getBasicMsg().getReceivers()[0].setParticipantID(message[1]);
			umsMsg.getBasicMsg().getReceivers()[0]
					.setIDType(Participant.PARTICIPANT_ID_EMAIL);
		}
		umsMsg.getBasicMsg().getReceivers()[0]
				.setParticipantType(Participant.PARTICIPANT_MSG_TO);
	}

	/**
	 * @���� ���ݴ����ݿ��л�ȡ���ĸ��˲�������������Ϣ����
	 * @param umsMsg
	 *            UMSMsg ��Ϣ����
	 * @param rs
	 *            ResultSet ��ѯ�����
	 * @param isNextPriority
	 *            boolean �ٴ������־
	 * @param index
	 *            int ���������
	 * @throws SQLException
	 * 
	 * @Add by ZHM 2009-11-2
	 */
	private static void setUmsMsgPerson(UMSMsg umsMsg, ResultSet rs,
			boolean isNextPriority, int index) throws SQLException {
		String preMediaID = umsMsg.getBasicMsg().getMediaID();
		if (isNextPriority) {
			String mediaID = rs.getString("media_id");
			while (!preMediaID.equals(mediaID)) {
				rs.next();
				mediaID = rs.getString("media_id");
			}
			rs.next();
		}

		int idType = rs.getInt("media_style");
		String mediaID = rs.getString("media_id");
		String userMobile = rs.getString("user_mobile");
		String userEmail = rs.getString("user_email");
		if (Participant.PARTICIPANT_ID_MOBILE == idType) {
			// ͨ�����ֻ�
			umsMsg.getBasicMsg().getReceivers()[index]
					.setParticipantID(userMobile);
			umsMsg.getBasicMsg().getReceivers()[index]
					.setIDType(Participant.PARTICIPANT_ID_MOBILE);
			umsMsg.getBasicMsg().getSender().setParticipantID(
					umsMsg.getBasicMsg().getServiceID());
		} else if (Participant.PARTICIPANT_ID_EMAIL == idType) {
			// ͨ���ǵ���
			umsMsg.getBasicMsg().getReceivers()[index]
					.setParticipantID(userEmail);
			umsMsg.getBasicMsg().getReceivers()[index]
					.setIDType(Participant.PARTICIPANT_ID_EMAIL);
		}
		umsMsg.getBasicMsg().setMediaID(mediaID);
		umsMsg.getBasicMsg().getReceivers()[index]
				.setParticipantType(Participant.PARTICIPANT_MSG_TO);
	}
}
