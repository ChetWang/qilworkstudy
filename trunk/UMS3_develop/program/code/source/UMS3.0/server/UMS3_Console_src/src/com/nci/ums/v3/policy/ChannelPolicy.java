/*
 * Policy.java
 *
 * Created on 2007-9-26, 16:30:44
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.v3.policy;

import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.message.euis.DefaultEnterpriseUserInfo;
import com.nci.ums.v3.message.euis.EnterpriseUserInfo;
import com.nci.ums.v3.message.euis.UUMUserInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Qil.Wong
 */
public class ChannelPolicy implements Policy {

	private EnterpriseUserInfo eui;
	private static ChannelPolicy channelPolicy;

	private ChannelPolicy() {
		// TODO initialize eui;
		// eui = new UUMUserInfo();
		eui = Util.getEuiObject();
	}

	public synchronized static ChannelPolicy getInstance() {
		if (channelPolicy == null) {
			channelPolicy = new ChannelPolicy();
		}
		return channelPolicy;
	}

	/**
	 * 
	 * @param umsMsg
	 * @param policyType
	 * @param isNextPriority
	 *            是否是下一个优先级。如果是第一次发送，为false， 如果是先前的渠道发送失败，需要下一个优先级的渠道进行发送，则为true
	 * @return 经过渠道优先级处理后的UMSMsg
	 * @throws SQLException
	 */
	public UMSMsg implChannelPolicy(UMSMsg umsMsg, int policyType,
			boolean isNextPriority) throws SQLException {
		String mediaID = "";
		String currentServiceID = umsMsg.getBasicMsg().getServiceID();
		// currentMediaID,进行渠道策略前该消息指定的渠道ID。
		// 如果是第一次由外部应用传递过来，则当直接发送时有效，策略发送时无效；
		// 如果是已经由UMS发送过，这次不是第一次发送，则该id是上一轮循中策略指定的id。
		String currentMediaID = umsMsg.getBasicMsg().getMediaID();
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			if (umsMsg.getRep() < 3) {// 如果还未达到单个渠道发送3次，则继续重复发送
				// 如果是直接发送，可能是第一次，也可能是第二次，第三次
				if (umsMsg.getBasicMsg().getDirectSendFlag() == BasicMsg.BASICMSG_DIRECTSEND_YES
						&& !currentMediaID.equals("")) {
					Participant[] receivers = umsMsg.getBasicMsg()
							.getReceivers();
					for (int i = 0; i < receivers.length; i++) {
						// 为避免传入的用户参数和指定渠道类型不对应，这里要强制对应
						receivers[i] = this.implPolicyToPaticipant(conn,
								receivers[i], currentMediaID);
					}
					umsMsg.getBasicMsg().setReceivers(receivers);
					return umsMsg;
				}
			}
			//已经超过三次，但直接发送
			else if (umsMsg.getRep() > 3
					&& umsMsg.getBasicMsg().getDirectSendFlag() == BasicMsg.BASICMSG_DIRECTSEND_YES) {
				umsMsg.getBasicMsg().setMediaID("");
				return umsMsg;
			}

			// 如果是按策略发送，则指定渠道也不起作用，先将上次策略指定的渠道id清空
			if (umsMsg.getBasicMsg().getDirectSendFlag() == BasicMsg.BASICMSG_DIRECTSEND_NO) {
				currentMediaID = "";
			}
			// 确定渠道是否正常，如果渠道已经不工作了，则下一个渠道发送
			boolean isMediaDead = true;
			while (isMediaDead) {
				mediaID = this.getChannelByPriority(this.getMediaIDQuerySQL(
						umsMsg.getBasicMsg().getMediaID(), currentServiceID,
						policyType, isNextPriority), conn);
				umsMsg.getBasicMsg().setMediaID(mediaID);
				if (ChannelManager.getOutMediaInfoHash().get(mediaID) != null)
					isMediaDead = false;
				else
					isNextPriority = true;
				if (mediaID.equals(""))// 所有已定义优先级的全试过了，都不成功，失败！
					break;
			}
			if (!mediaID.equals("")) {
				Participant[] receivers = umsMsg.getBasicMsg().getReceivers();
				for (int i = 0; i < receivers.length; i++) {
					receivers[i] = this.implPolicyToPaticipant(conn,
							receivers[i], mediaID);
				}
				Participant sender = umsMsg.getBasicMsg().getSender();
				// sender = this.implPolicyToPaticipant(conn, sender, mediaID);
				umsMsg.getBasicMsg().setReceivers(receivers);
				// umsMsg.getBasicMsg().setSender(sender);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (conn != null)
				conn.close();
		}
		return umsMsg;
	}

	/**
	 * 获取与渠道相对应的接收者的ID和类型
	 * 
	 * @param conn
	 * @param paticipant
	 * @param mediaID
	 * @return 经过处理后的接收者，保证接收者信息不会有误
	 * @throws SQLException
	 */
	private Participant implPolicyToPaticipant(Connection conn,
			Participant paticipant, String mediaID) throws SQLException {
		PreparedStatement prep = conn
				.prepareStatement("SELECT MEDIASTYLE FROM MEDIA WHERE MEDIA_ID = ?");
		prep.setString(1, mediaID);
		ResultSet rs = prep.executeQuery();
		int type = -1;
		while (rs.next()) {
			type = rs.getInt("MEDIASTYLE");
		}
		if (type != paticipant.getIDType()) {
			String neededParticipantID = eui.getAccout(paticipant, type);
			paticipant.setIDType(type);
			paticipant.setParticipantID(neededParticipantID);
		}
		return paticipant;
	}

	/**
	 * 通过优先级来获取渠道类型
	 * @param mediaQuerySQL
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	private String getChannelByPriority(String mediaQuerySQL, Connection conn)
			throws SQLException {

		String mediaID = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(mediaQuerySQL);
			while (rs.next()) {
				mediaID = Util.replaceNULL(rs.getString(1));
			}
			rs.close();
			st.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			Res.log(Res.ERROR, "数据库连接建立失败,无法以优先级确定渠道!" + mediaQuerySQL);
			Res.logExceptionTrace(ex);
		}
		return mediaID;
	}

	/**
	 * get the sql string for media id query
	 * 
	 * @param mediaID
	 *            the existing media id
	 * @param currentServiceID
	 *            the service id currently defined
	 * @param policyType
	 *            policy type
	 * @param isNextPriority
	 *            if the message is the first time to be sent, it is <b>false</b>
	 *            if the message is to be sent via another channel because
	 *            sending failed before, it is <b>true</b>
	 * @return sql string
	 */
	private String getMediaIDQuerySQL(String mediaID, String currentServiceID,
			int policyType, boolean isNextPriority) {
		StringBuffer sql_mediaID = new StringBuffer();
		if (policyType == Policy.POLICY_APPSERVICE) {
			StringBuffer sql_AppID = new StringBuffer();
			sql_AppID.append("(SELECT APPID FROM SERVICE_V3 WHERE SERVICEID='");
			sql_AppID.append(currentServiceID).append("')");
			sql_mediaID
					.append("SELECT MEDIAID FROM APPCHANNELPOLICY_V3 WHERE APPID=");
			sql_mediaID.append(sql_AppID);
			if (isNextPriority) {
				// msg has been sent at least once
				StringBuffer sql_Priority = new StringBuffer();
				sql_Priority
						.append("(SELECT PRIORITY FROM APPCHANNELPOLICY_V3 WHERE APPID=");
				sql_Priority.append(sql_AppID).append("AND MEDIAID='").append(
						mediaID).append("')");
				sql_mediaID.append(" AND PRIORITY = ").append(sql_Priority)
						.append("+1");
			} else {
				// send msg at first time
				sql_mediaID.append(" AND PRIORITY = 1");
			}
		} else if (policyType == Policy.POLICY_PERSONAL) {
			// TODO action for Personal Policy
		}
		return sql_mediaID.toString();
	}
}