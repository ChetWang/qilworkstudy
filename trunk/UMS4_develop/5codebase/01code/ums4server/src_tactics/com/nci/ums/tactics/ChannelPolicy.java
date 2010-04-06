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
 * 项目名称: ums4server
 * </p>
 * <p>
 * package com.nci.ums.tactics;
 * </p>
 * <p>
 * 标题: ChannelPolicy.java
 * </p>
 * <p>
 * 描述:
 * <p>
 * 版本:Version 1.0
 * </p>
 * <p>
 * 版权: 2003 Hangzhou NCI System Engineering， Ltd.
 * </p>
 * <p>
 * 公司: Hangzhou NCI System Engineering， Ltd.
 * </p>
 * 
 * @author 作者:shenyg E-mail: shenyg@nci.com.cn
 * @version 创建时间：2009-10-19 上午10:59:43 修改记录 日期 作者 修改类型 描述
 * @version 修改时间：2009-11-02 整体修改
 */
public class ChannelPolicy {

	private static EnterpriseUserInfo eui = Util.getEuiObject();

	/**
	 * 
	 * implChannelPolicy:策略选择
	 * 
	 * @param umsMsg
	 * @param isNextPriority
	 *            boolean 前面是否已经发过请求
	 * 
	 * @return void
	 * @throws SQL操作失败信息
	 */
	public static void implChannelPolicy(UMSMsg umsMsg, boolean isNextPriority)
			throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// 获取数据库连接
			conn = Res.getConnection();
			// 根据消息传入类别判断数据 获取个人编号 在获取当前个人策略信息
			Participant[] receviers = umsMsg.getBasicMsg().getReceivers();
			int recevierSize = receviers.length;

			for (int i = 0; i < recevierSize; i++) {
				stmt = conn.createStatement();
				String sql = eui.getStasticSQL(receviers[i]);
				Res.log(Res.DEBUG, sql.toString());
				rs = stmt.executeQuery(sql.toString());
				// 获取个人策略数据
				if (rs.next()) {
					setUmsMsgPerson(umsMsg, rs, isNextPriority, i);
				} else {
					// 如果不存在个人策略数据 那么获取服务策略数据
					// 获取个人信息
					String[] message;
					if (receviers.length > 0) {
						message = getUserPhoneAndEmail(
								receviers[i].getIDType(), receviers[i]
										.getParticipantID());

						// 获取服务策略数据
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
						// 设定数据项
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
	 * 根据用户信息获取该系统中该用户手机和Email信息
	 * 
	 * @param idType
	 *            int 通道类型
	 * @param receive
	 *            String 接收者标识
	 * @return String[2] [0]手机,[1]Email
	 */
	private static String[] getUserPhoneAndEmail(int idType, String receive) {
		String[] message = new String[2];
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT user_mobile, user_email FROM uum_user WHERE ");
		if (Participant.PARTICIPANT_ID_MOBILE == idType) {
			// 通道是手机
			sql.append("user_mobile='").append(receive).append("'");
			message[0] = receive;
		} else if (Participant.PARTICIPANT_ID_EMAIL == idType) {
			// 通道是电邮
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
			// 获取数据库连接
			conn = Res.getConnection();
			try {
				stmt = conn.createStatement();
				Res.log(Res.DEBUG, sql.toString());
				rs = stmt.executeQuery(sql.toString());
				if (rs.next()) {
					// 获取数据库中用户信息
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
	 * @功能 根据从数据库中获取到的应用策略数据设置消息对象
	 * @param umsMsg
	 * @param rs
	 * @param message
	 *            String[2] 用户信息[0]手机[1]Email
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
			// 通道是手机
			umsMsg.getBasicMsg().getReceivers()[0].setParticipantID(message[0]);
			umsMsg.getBasicMsg().getReceivers()[0]
					.setIDType(Participant.PARTICIPANT_ID_MOBILE);
			umsMsg.getBasicMsg().getSender().setParticipantID(
					umsMsg.getBasicMsg().getServiceID());
		} else if (Participant.PARTICIPANT_ID_EMAIL == idType
				&& message[1] != null) {
			// 通道是电邮
			umsMsg.getBasicMsg().getReceivers()[0].setParticipantID(message[1]);
			umsMsg.getBasicMsg().getReceivers()[0]
					.setIDType(Participant.PARTICIPANT_ID_EMAIL);
		}
		umsMsg.getBasicMsg().getReceivers()[0]
				.setParticipantType(Participant.PARTICIPANT_MSG_TO);
	}

	/**
	 * @功能 根据从数据库中获取到的个人策略数据设置消息对象
	 * @param umsMsg
	 *            UMSMsg 消息对象
	 * @param rs
	 *            ResultSet 查询结果集
	 * @param isNextPriority
	 *            boolean 再次请求标志
	 * @param index
	 *            int 接收者序号
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
			// 通道是手机
			umsMsg.getBasicMsg().getReceivers()[index]
					.setParticipantID(userMobile);
			umsMsg.getBasicMsg().getReceivers()[index]
					.setIDType(Participant.PARTICIPANT_ID_MOBILE);
			umsMsg.getBasicMsg().getSender().setParticipantID(
					umsMsg.getBasicMsg().getServiceID());
		} else if (Participant.PARTICIPANT_ID_EMAIL == idType) {
			// 通道是电邮
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
