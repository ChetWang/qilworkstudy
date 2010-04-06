/*
 * UMSCommandCenter.java
 *
 * Created on 2007-9-27, 16:44:24
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.ums.v3.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.channel.outchannel.Filter;
import com.nci.ums.channel.outchannel.OutChannel_V3;
import com.nci.ums.periphery.exception.OutOfMaxSequenceException;
import com.nci.ums.util.AppServiceUtil;
import com.nci.ums.util.DBUtil_V3;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.FeeUtil;
import com.nci.ums.util.Res;
import com.nci.ums.util.SequenceNO;
import com.nci.ums.util.SerialNO;
import com.nci.ums.util.Util;
import com.nci.ums.v3.fee.FeeBean;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.policy.ChannelPolicy;
import com.nci.ums.v3.policy.Policy;
import com.nci.ums.v3.service.ServiceInfo;
import com.nci.ums.v3.service.common.CommandCenter;

/**
 * 
 * @author Qil.Wong
 */
public class UMSCommandCenter implements CommandCenter {

	/**
	 * 消息插入操作的sql语句
	 */
	private String insertMsgSQL = "";
	/**
	 * 消息插入操作时，针对有附件情况的sql,该sql用于处理附件
	 */
	private String insertAttachSQL = "";
	/**
	 * 数据库操作对象
	 */
	private DBUtil_V3 dbUtil = null;

	public UMSCommandCenter() {
		dbUtil = new DBUtil_V3();
		this.setMsgInsertSQL(DBUtil_V3
				.createOutMsgInsertSQL(DBUtil_V3.MESSAGE_OUT_READY_V3));
		this.setInsertAttachSQL(DBUtil_V3.createAttachInsertSQL());
	}

	/**
	 * 外部消息接收、校验、策略应用和存储的方法
	 * 
	 * @param basicMsgs
	 * @return ums接收消息结果状态
	 * @throws SQLException
	 * @throws OutOfMaxSequenceException
	 */
	public String start(BasicMsg[] basicMsgs) throws SQLException,
			OutOfMaxSequenceException {
		UMSMsg[] msgs = new UMSMsg[basicMsgs.length];

		boolean firstAck = true;
		for (int i = 0; i < basicMsgs.length; i++) {
			// 消息策略处理
			msgs[i] = messagePolicier(basicMsgs[i]);
			// 检查消息的有效性,check==null才是有效的消息
			String check = this.checkMsg(msgs[i]);
			if (check != null) {
				return check;
			}
			// 渠道策略处理
			msgs[i] = channelPolicier(msgs[i]);

		}
		// 交易处理，并存入UMS数据库，等待相应的渠道发送
		String result = transmit(msgs);
		if (result.equals(UMSSendService.RETCODE_SEND_SUCCESS)) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < msgs.length; i++) {
				// 添加回执标识
				if (msgs[i].getBasicMsg().getAck() == BasicMsg.UMSMsg_ACK) {

					int[] seq = msgs[i].getSequenceNO();
					for (int n = 0; n < seq.length; n++) {
						if (!firstAck) {
							sb.append(",");
						} else {
							firstAck = false;
						}
						sb.append(msgs[i].getBatchNO()).append("-").append(
								msgs[i].getSerialNO()).append("-").append(
								seq[n]);
					}

				}
			}
			result = result + sb.toString();
		}
		return result;
	}

	private Connection getDBConn() {
		try {
			return DriverManager.getConnection(DataBaseOp.getPoolName());
		} catch (SQLException e) {
			Res.log(Res.ERROR, "数据库连接获取失败：" + e.getMessage());
			Res.logExceptionTrace(e);
		}
		return null;
	}

	/**
	 * @param msg
	 *            the message to be checked
	 * @rerurn the result of message checking for illegal words and attachment
	 *         size
	 */
	private String checkMsg(UMSMsg msg) {

		if (msg.getBasicMsg().getInvalidDate().equals("")) {
			msg.getBasicMsg()
					.setInvalidDate(Util.getCurrentTimeStr("yyyyMMdd"));
		}
		if (msg.getBasicMsg().getInvalidTime().equals("")) {
			msg.getBasicMsg().setInvalidTime(Util.getCurrentTimeStr("HHmmss"));
		}
		if (msg.getStatusFlag() == UMSMsg.UMSMSG_STATUS_INVALID
				&& msg.getErrMsg().equalsIgnoreCase("Illegal Words")) {
			// 这是在消息策略使用之后进行判断检测的，而消息策略中，一旦有非法的字符，就将该消息的状态更改为INVALID
			storeErrorMsg(msg);
			return UMSSendService.RETCODE_SEND_ILLEGAL_WORDS;
		}
		// 如果是直接发送的（不按指定策略），则必须要指定渠道号。如果指定的渠道停止工作了，则应给予相应的返回值提示。
		if (msg.getBasicMsg().getDirectSendFlag() == BasicMsg.BASICMSG_DIRECTSEND_YES) {
			if (msg.getBasicMsg().getMediaID().equals("")) {
				return UMSSendService.RETCODE_CHANNEL_NEEDED;
			}
			if (ChannelManager.getOutMediaInfoHash().get(
					msg.getBasicMsg().getMediaID()) == null) {
				return UMSSendService.RETCODE_MEDIA_CHANNEL_STOPPED;
			}
			if (!msg.getBasicMsg().getAppSerialNO().equalsIgnoreCase(
					"AppSerialNO")
					&& msg.getBasicMsg().getMediaID().equals(
							OutChannel_V3.nciV3Media_ID)) {
				if (msg.getBasicMsg().getAppSerialNO().length() > 4)
					return UMSSendService.RETCODE_APPSERIAL_MOBILE_LARGER_THAN_FOUR;
				try {
					if (msg.getBasicMsg().getAppSerialNO().length() > 0)
						Float.parseFloat(msg.getBasicMsg().getAppSerialNO());
				} catch (Exception e) {
					return UMSSendService.RETCODE_APPSERIAL_MOBILE_ISNOT_NUMBERIC;
				}
			}
		}
		// 定时发送的话就要进行时间有效性判断
		if (msg.getBasicMsg().getTimeSetFlag() == BasicMsg.BASICMSG_SENDTIME_CUSTOM) {
			String sendDateSet = msg.getBasicMsg().getSetSendDate();
			String sendTimeSet = msg.getBasicMsg().getSetSendTime();
			if (sendDateSet.equals("")) {
				return UMSSendService.RETCODE_SET_DATE_INVALID;
			}
			String current = Util.getCurrentTimeStr("yyyyMMddHHmmss");
			long setTimeLong = Long.parseLong(sendDateSet + sendTimeSet);
			long currentLong = Long.parseLong(current);
			if (currentLong > setTimeLong) {
				return UMSSendService.RETCODE_SET_DATE_INVALID;
			}
		} else {// 如果不是定时发送的消息，却给消息的定时发送日期和时间赋了值，应清空。
			msg.getBasicMsg().setSetSendDate("");
			msg.getBasicMsg().setSetSendTime("");
			msg.getBasicMsg().setTimeSetFlag(
					BasicMsg.BASICMSG_SENDTIME_NOCUSTOM);
		}
		if (msg.getBasicMsg().getMsgContent().getSubject() != null
				&& msg.getBasicMsg().getMsgContent().getSubject().length() > 50) {
			return UMSSendService.RETCODE_SUBJECT_TOO_LONG;
		}
		if (msg.getBasicMsg().getMsgContent().getContent() != null
				&& msg.getBasicMsg().getMsgContent().getContent().length() > 4000) {
			return UMSSendService.RETCODE_CONTENT_TOO_LONG;
		}
		return null;
	}

	/**
	 * UMS存储错误的消息，这里指存在非法字符的消息
	 * 
	 * @param msg
	 *            错误的消息
	 */
	private void storeErrorMsg(UMSMsg msg) {
		Connection conn = null;
		try {
			conn = getDBConn();
			PreparedStatement msgPrep = conn.prepareStatement(DBUtil_V3
					.createOutMsgInsertSQL(DBUtil_V3.MESSAGE_OUT_ERROR_V3));
			dbUtil.executeOutMsgInsertStatement(msgPrep, null, msg,
					DBUtil_V3.MESSAGE_OUT_ERROR_V3, conn);
			msgPrep.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
			}
		}
	}

	private void storeSendMsg(Connection conn, PreparedStatement msgPrep,
			PreparedStatement attachPrep, UMSMsg msg) throws SQLException,
			IOException {
		dbUtil.executeOutMsgInsertStatement(msgPrep, attachPrep, msg,
				DBUtil_V3.MESSAGE_OUT_READY_V3, conn);
	}

	public String transmit(UMSMsg[] msgs) throws OutOfMaxSequenceException {
		Connection msgConn = this.getDBConn();
		try {
			String feeServiceNO = msgs[0].getBasicMsg().getFeeServiceNO();
			FeeBean feeInfo = null;
			if (feeServiceNO == null || feeServiceNO.equals("")) {
				feeInfo = new FeeBean(null, 0, 0);
			} else {
				feeInfo = (FeeBean) FeeUtil.getFeeMap().get(feeServiceNO);
			}
			String batchNO = Util.getCurrentTimeStr("yyyyMMddHHmmss");
			msgConn.setAutoCommit(false);
			PreparedStatement msgPrep = msgConn.prepareStatement(this
					.getMsgInsertSQL());
			PreparedStatement attachPrep = msgConn.prepareStatement(this
					.getInsertAttachSQL());
			for (int i = 0; i < msgs.length; i++) {
				String serialNO = SerialNO.getInstance().getSerial();
				if (msgs.length > 1) {
					msgs[i].setBatchMode(UMSMsg.UMSMSG_BATCHMODE_MULTIPLE);
				} else {
					msgs[i].setBatchMode(UMSMsg.UMSMSG_BATCHMODE_SINGLE);
				}
				msgs[i].setBatchNO(batchNO);
				msgs[i].setSerialNO(Integer.valueOf(serialNO).intValue());
				BasicMsg basicMsg = msgs[i].getBasicMsg();
				int[] sequenceNO = SequenceNO.getSequenceNO(basicMsg
						.getReceivers().length);
				msgs[i].setSequenceNO(sequenceNO);
				msgs[i].setStatusFlag(UMSMsg.UMSMSG_STATUS_VALID);
				msgs[i].setFeeInfo(feeInfo);
				try {
					this.storeSendMsg(msgConn, msgPrep, attachPrep, msgs[i]);
					MediaInfo mediaInfo = (MediaInfo) ChannelManager
							.getOutMediaInfoHash().get(
									msgs[i].getBasicMsg().getMediaID());
					if (mediaInfo == null) {
						Res.log(Res.ERROR, "消息指定的渠道:"
								+ msgs[i].getBasicMsg().getMediaID()
								+ "不存在，或并未启动。");
					} else {
						mediaInfo.getChannelObject().getDataLockFlag()
								.setLockFlag(false);
					}
				} catch (IOException e) {
					Res.log(Res.ERROR, "读写消息附件出错" + e.getMessage());
					e.printStackTrace();
					Res.logExceptionTrace(e);
				}
			}
			msgPrep.close();
			attachPrep.close();
			msgConn.commit();
		} catch (SQLException ex) {
			// ex.printStackTrace();
			Res.log(Res.ERROR, "消息存储失败：" + ex.getMessage());
			Res.logExceptionTrace(ex);
			return UMSSendService.RETCODE_DATABASE_ERROR;
		} finally {
			try {
				if (msgConn != null) {
					msgConn.close();
				}
			} catch (SQLException ex) {
				Res.log(Res.ERROR, "数据库连接关闭失败：" + ex.getMessage());
				Res.logExceptionTrace(ex);
				return UMSSendService.RETCODE_DATABASE_ERROR;
			}
		}

		return UMSSendService.RETCODE_SEND_SUCCESS;
	}

	public UMSMsg channelPolicier(UMSMsg msg) throws SQLException {
		ChannelPolicy channelPolicy = ChannelPolicy.getInstance();
		msg = channelPolicy.implChannelPolicy(msg, DBUtil_V3
				.getUMSMainChannelPolicy(), Policy.POLICY_NEXT_PRIORITY_NO);
		if (msg.getBasicMsg().getDirectSendFlag() == BasicMsg.BASICMSG_DIRECTSEND_YES
				&& msg.getBasicMsg().getMediaID() == null) {
			msg.setStatusFlag(UMSMsg.UMSMSG_STATUS_MEDIA_STOPPED);
		} else if (msg.getBasicMsg().getMediaID().equals("")) {
			// the lowest priority channel has used to send this message, and
			// failed
			msg.setStatusFlag(UMSMsg.UMSMSG_STATUS_INVALID);
		}
		return msg;
	}

	public UMSMsg messagePolicier(BasicMsg basicMsg) {
		// TODO message policy ...
		// Statusflag,ErrMsg attribute来区分消息正常不正常
		UMSMsg msg = new UMSMsg();
		String subject = basicMsg.getMsgContent().getSubject();
		String content = basicMsg.getMsgContent().getContent();
		boolean filterFlag = Filter.filter(subject) || Filter.filter(content);
		if (filterFlag) {
			msg.setStatusFlag(UMSMsg.UMSMSG_STATUS_INVALID);
			msg.setErrMsg("Illegal Words");
		}
		msg.setBasicMsg(basicMsg);
		return msg;
	}

	public String getMsgInsertSQL() {
		return insertMsgSQL;
	}

	public void setMsgInsertSQL(String insertSQL) {
		this.insertMsgSQL = insertSQL;
	}

	public String getInsertAttachSQL() {
		return insertAttachSQL;
	}

	public void setInsertAttachSQL(String insertAttachSQL) {
		this.insertAttachSQL = insertAttachSQL;
	}
}
