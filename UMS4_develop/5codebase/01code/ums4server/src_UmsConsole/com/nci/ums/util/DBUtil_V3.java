package com.nci.ums.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import oracle.sql.BLOB;

import com.nci.ums.periphery.application.AppInfo;
import com.nci.ums.policy.Policy;
import com.nci.ums.util.uid.UMSUID;
import com.nci.ums.v3.fee.FeeBean;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.service.ServiceInfo;
import com.nci.ums.v3.service.impl.UMSSendService;

public class DBUtil_V3 {

	/**
	 * UMS消息表类型，对应UMS_SEND_READY
	 */
	public static final int MESSAGE_UMS_SEND_READY = 1;
	/**
	 * UMS消息表类型，对应OUT_OK_V3
	 */
	public static final int MESSAGE_OUT_OK_V3 = 2;
	/**
	 * UMS消息表类型，对应OUT_REPLY_V3
	 */
	public static final int MESSAGE_OUT_REPLY_V3 = 3;
	/**
	 * UMS消息表类型，对应OUT_ERROR_V3
	 */
	public static final int MESSAGE_OUT_ERROR_V3 = 4;
	/**
	 * UMS消息表类型，对应IN_OUT_ATTACH_V3
	 */
	public static final int MESSAGE_ATTACHMENTS = 5;
	/**
	 * UMS消息表类型，对应UMS_RECEIVE_READY
	 */
	public static final int MESSAGE_UMS_RECEIVE_READY = 10;
	/**
	 * UMS消息表类型，对应UMS_RECEIVE_OK
	 */
	public static final int MESSAGE_UMS_RECEIVE_OK = 11;
	/**
	 * ums的渠道策略，0表示应用优先，1表示个人优先
	 */
	private static int UMSChannelPolicy = 0;
	/**
	 * 应用登录错误，应用已停止
	 */
	public static final int LOGIN_ERR_APPDISABLED = 1;
	/**
	 * 应用登录错误，密码错误
	 */
	public static final int LOGIN_ERR_PASSWORD = 2;
	/**
	 * 应用登录错误，应用不存在
	 */
	public static final int LOGIN_ERR_NO_SUCH_APP = 3;
	/**
	 * 应用登录成功
	 */
	public static final int LOGIN_SUCCESS = 0;

	/**
	 * create the sql string for out messages to be inserted into out_tables in
	 * database
	 * 
	 * @param msgStorageTableType
	 *            there are four table types:
	 *            MESSAGE_UMS_SEND_READY,MESSAGE_OUT_OK_V3,
	 *            MESSAGE_OUT_REPLY_V3,MESSAGE_OUT_ERROR_V3, represent each
	 *            table in database.
	 * @return the out message insert sql string
	 */
	public static String getOutMsgSeqkey(Connection conn,
			int msgStorageTableType) {
		// String seqkeysql = "";
		// if (DialectUtil.getDataBaseType() == DialectUtil.UMS_DATABASE_ORACLE)
		// {
		// String seqkey = "S_" + getTableName(msgStorageTableType)
		// + ".nextval";
		// seqkeysql = "SELECT " + seqkey + " FROM DUAL";
		// } else {
		// seqkeysql = "SELECT MAX(seqkey) FROM "
		// + getTableName(msgStorageTableType);
		// }
		// String resultseqkey = null;
		// Statement statement = null;
		// try {
		// statement = conn.createStatement();
		// ResultSet result = statement.executeQuery(seqkeysql);
		// if (result.next()) {
		// resultseqkey = result.getString(1);
		// if (resultseqkey == null || resultseqkey.equals("")) {
		// resultseqkey = "0";
		// }
		// if (DialectUtil.getDataBaseType() != DialectUtil.UMS_DATABASE_ORACLE)
		// {
		// resultseqkey = String.valueOf(Integer
		// .parseInt(resultseqkey) + 1);
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// if (statement != null) {
		// try {
		// statement.close();
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		// }
		// } finally {
		// if (statement != null) {
		// try {
		// statement.close();
		// } catch (Exception e2) {
		// e2.printStackTrace();
		// }
		// }
		// }
		//
		// return resultseqkey;

		return UMSUID.getUMSUID(UMSUID.UMSUID_SERVER);
	}

	/**
	 * 获取外发消息插入数据库的sql。
	 * 
	 * @param msgStorageTableType
	 *            ，数据库表类型，参见DBUtil_V3对表的类型的分类
	 * @return 相应得sql
	 */
	public static String createOutMsgInsertSQL(int msgStorageTableType) {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append(getTableName(msgStorageTableType));
		sb.append(" (BatchNO,SerialNO,SequenceNO,BatchMode,");
		sb.append("AppSerialNO,RetCode,ErrMsg,StatusFlag,ServiceID,MediaID,");
		sb
				.append("SendDirectly,SendID,SenderType,RecvID,ReceiverType,SubmitDate,SubmitTime,");
		sb
				.append("FinishDate,FinishTime,Rep,DoCount,Priority,ContentSubject,Content,");
		sb
				.append("MsgID,TimeSetFlag,SetDate,SetTime,InvalidDate,InvalidTime,Ack,");
		sb
				.append("ReplyDestination,NeedReply,FeeServiceNO,Fee,FeeType,UmsFlag,CompanyID,Seqkey,SenderIDType,ReceiverIDType,ContentMode)");
		sb
				.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return sb.toString();
	}

	/**
	 * 获取数据库所需的seqkey值，该值用于jfw框架所需的各类要求
	 * 
	 * @param conn
	 * @param msgStorageTableType
	 * @return
	 */
	public static String getInMsgSeqkey(Connection conn, int msgStorageTableType) {

		return UMSUID.getUMSUID(UMSUID.UMSUID_SERVER);
	}

	public static String createInMsgInsertSQL(int msgStorageTableType) {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO ");
		if (msgStorageTableType == DBUtil_V3.MESSAGE_UMS_RECEIVE_READY) {
			sb.append("UMS_RECEIVE_READY");
		}
		if (msgStorageTableType == DBUtil_V3.MESSAGE_UMS_RECEIVE_OK) {
			sb.append("UMS_RECEIVE_OK");
		}
		sb.append("(BATCHNO,SERIALNO,SEQUENCENO,RETCODE,");
		sb
				.append("ERRMSG,STATUSFLAG,SERVICEID,APPSERIALNO,MEDIAID,SENDID,RECVID,");
		sb
				.append("SUBMITDATE,SUBMITTIME,FINISHDATE,FINISHTIME,CONTENTSUBJECT,");
		sb
				.append("CONTENT,MSGID,ACK,REPLYNO,DOCOUNT,MSGTYPE,seqkey,SENDERIDTYPE)");
		sb.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return sb.toString();
	}

	/**
	 * 
	 * create the delete msg sql string.
	 * 
	 * @param msg
	 *            UMSMsg Object, attribute batchNO,serialNO,sequenceNO is
	 *            necessary.
	 * @param msgStorageTableType
	 * @return the delete msg sql string
	 */
	public static String createDeleteMsgSQL(int msgStorageTableType) {
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM ");
		sb.append(getTableName(msgStorageTableType));
		sb.append(" WHERE BATCHNO = ? AND SERIALNO = ? AND SEQUENCENO = ?");
		return sb.toString();
	}

	public static String createAttachInsertSQL() {
		return "INSERT INTO UMS_ATTACHMENTS (BATCHNO,SERIALNO,SEQUENCENO,FILENAME,FILECONTENT) VALUES(?,?,?,?,?)";
	}

	/**
	 * 根据指定的数据库表类型分类来获取表名称
	 * 
	 * @param msgStorageTableType
	 *            数据库表类，参见DBUtil_V3对表类型的定义
	 * @return 对应的表名称
	 */
	private static String getTableName(int msgStorageTableType) {
		String tableName = "";
		switch (msgStorageTableType) {
		case DBUtil_V3.MESSAGE_UMS_SEND_READY:
			tableName = "UMS_SEND_READY";
			break;
		case DBUtil_V3.MESSAGE_OUT_REPLY_V3:
			tableName = "UMS_SEND_REPLY";
			break;
		case DBUtil_V3.MESSAGE_OUT_OK_V3:
			tableName = "UMS_SEND_OK";
			break;
		case DBUtil_V3.MESSAGE_OUT_ERROR_V3:
			tableName = "UMS_SEND_ERROR";
			break;
		case DBUtil_V3.MESSAGE_UMS_RECEIVE_READY:
			tableName = "UMS_RECEIVE_READY";
			break;
		case DBUtil_V3.MESSAGE_UMS_RECEIVE_OK:
			tableName = "UMS_RECEIVE_OK";
			break;
		case DBUtil_V3.MESSAGE_ATTACHMENTS:
			tableName = "UMS_ATTACHMENTS";
			break;
		}
		return tableName;
	}

	public static String createFailedMsgUpdateSQL() {
		StringBuffer sb = new StringBuffer();
		sb
				.append("UPDATE UMS_SEND_READY SET MEDIAID = ?,RECVID = ?,RECEIVERTYPE = ?,REP = ?,DOCOUNT = ?,STATUSFLAG = ?,RETCODE = ?,ERRMSG = ?");
		sb.append(" WHERE ");
		sb.append("BATCHNO = ? AND SERIALNO = ? AND SEQUENCENO = ?");
		return sb.toString();
	}

	/**
	 * Get the type of channel policy in UMS server. Currently, there are two
	 * types: APPService or Personal.
	 * 
	 * @see com.nci.ums.policy.Policy
	 * @return Main Channel Policy
	 */
	public static int getUMSMainChannelPolicy() {
		if (UMSChannelPolicy == 0) {
			return Policy.POLICY_APPSERVICE;
		}
		// TODO this is finnaly will be put into a property file or database
		// result.
		return -1;
	}

	/**
	 * certificate the application calling UMS Web Service
	 * 
	 * @param appID
	 *            application id
	 * @param appPassword
	 *            application password
	 * @return Result of application login
	 */
	public static int login(String appID, String appPassword) {
		// 平台内部操作时，以DESKTOPADMIN为标记，无需数据库验证。
		if (appID.equals("DESKTOPADMIN")) {
			return LOGIN_SUCCESS;
		}
		Map appMap = LoginUtil_V3.getAppMap();
		AppInfo app = (AppInfo) appMap.get(appID);
		if (app == null) {
			return LOGIN_ERR_NO_SUCH_APP;
		} else {
			if (app.getStatus().equals(AppInfo.STATUS_DISABLE)) {
				return LOGIN_ERR_APPDISABLED;
			}
			String truePSW = app.getPassword();
			if (truePSW != null && truePSW.equals(appPassword)) {
				return LOGIN_SUCCESS;
			} else {
				return LOGIN_ERR_PASSWORD;
			}
		}
	}

	public static String parseLoginErrResult(int loginResult) {
		switch (loginResult) {
		case LOGIN_ERR_APPDISABLED:
			return UMSSendService.RETCODE_APP_DISABLED;
		case LOGIN_ERR_NO_SUCH_APP:
			return UMSSendService.RETCODE_APP_NOT_EXIST;
		case LOGIN_ERR_PASSWORD:
			return UMSSendService.RETCODE_APP_WRONG_PASSWORD;
		}
		return "Failed";
	}

	public void executeFailedMsgUpdate(PreparedStatement updatePrep, UMSMsg msg)
			throws SQLException {
		for (int i = 0; i < msg.getBasicMsg().getReceivers().length; i++) {
			updatePrep.setString(1, msg.getBasicMsg().getMediaID());
			updatePrep.setString(2, msg.getBasicMsg().getReceivers()[i]
					.getParticipantID());
			updatePrep.setInt(3, msg.getBasicMsg().getReceivers()[i]
					.getIDType());
			updatePrep.setInt(4, msg.getRep());
			updatePrep.setInt(5, msg.getDoCount());
			updatePrep.setInt(6, msg.getStatusFlag());
			updatePrep.setString(7, msg.getReturnCode());
			updatePrep.setString(8, msg.getErrMsg());
			updatePrep.setString(9, msg.getBatchNO());
			updatePrep.setInt(10, msg.getSerialNO());
			updatePrep.setInt(11, msg.getSequenceNO()[i]);
			updatePrep.executeUpdate();
		}
	}

	/**
	 * 外发消息存入数据库
	 * 
	 * @param msgPrep
	 * @param attachPrep
	 * @param msg
	 * @param insertMessageType
	 * @param conn
	 * @throws SQLException
	 * @throws IOException
	 */
	public void executeOutMsgInsertStatement(PreparedStatement msgPrep,
			PreparedStatement attachPrep, UMSMsg msg, int insertMessageType,
			Connection conn) throws SQLException, IOException {
		String currentTime = Util.getCurrentTimeStr("yyyyMMddHHmmss");
		BasicMsg basicMsg = msg.getBasicMsg();
		for (int i = 0; i < basicMsg.getReceivers().length; i++) {
			msgPrep.setString(1, msg.getBatchNO());
			msgPrep.setInt(2, msg.getSerialNO());
			msgPrep.setInt(3, msg.getSequenceNO()[i]);
			msgPrep.setString(4, String.valueOf(msg.getBatchMode()));
			msgPrep.setString(5, basicMsg.getAppSerialNO());
			msgPrep.setString(6, msg.getReturnCode());
			msgPrep.setString(7, msg.getErrMsg());
			msgPrep.setInt(8, msg.getStatusFlag());
			msgPrep.setString(9, basicMsg.getServiceID());
			msgPrep.setString(10, basicMsg.getMediaID());
			msgPrep.setInt(11, basicMsg.getDirectSendFlag());
			msgPrep.setString(12, basicMsg.getSender().getParticipantID());
			msgPrep.setInt(13, basicMsg.getSender().getParticipantType());
			msgPrep
					.setString(14, basicMsg.getReceivers()[i]
							.getParticipantID());
			msgPrep.setInt(15, basicMsg.getReceivers()[i].getParticipantType());
			// configure submit date and submit time
			if (insertMessageType == DBUtil_V3.MESSAGE_UMS_SEND_READY) {
				msgPrep.setString(16, currentTime.substring(0, 8));
				msgPrep.setString(17, currentTime.substring(8, 14));
			} else {
				msgPrep.setString(16, basicMsg.getSubmitDate());
				msgPrep.setString(17, basicMsg.getSubmitTime());
			}
			// configure finishDate and finishTime
			if (insertMessageType == DBUtil_V3.MESSAGE_OUT_OK_V3
					|| insertMessageType == DBUtil_V3.MESSAGE_OUT_REPLY_V3) {
				msgPrep.setString(18, currentTime.substring(0, 8));
				msgPrep.setString(19, currentTime.substring(8, 14));
			} else {
				msgPrep.setString(18, msg.getFinishDate());
				msgPrep.setString(19, msg.getFinishTime());
			}
			msgPrep.setInt(20, msg.getRep()); // Rep
			msgPrep.setInt(21, msg.getDoCount()); // DoCount
			msgPrep.setInt(22, basicMsg.getPriority());
			msgPrep.setString(23, basicMsg.getMsgContent().getSubject());
			msgPrep.setString(24, basicMsg.getMsgContent().getContent());
			msgPrep.setString(25, msg.getMsgID());
			msgPrep.setInt(26, basicMsg.getTimeSetFlag());
			msgPrep.setString(27, basicMsg.getSetSendDate());
			msgPrep.setString(28, basicMsg.getSetSendTime());
			msgPrep.setString(29, basicMsg.getInvalidDate());
			msgPrep.setString(30, basicMsg.getInvalidTime());
			msgPrep.setInt(31, basicMsg.getAck());
			msgPrep.setString(32, basicMsg.getReplyDestination());
			msgPrep.setInt(33, basicMsg.getNeedReply());
			msgPrep.setString(34, basicMsg.getFeeServiceNO());
			msgPrep.setInt(35, msg.getFeeInfo().getFee());
			msgPrep.setInt(36, msg.getFeeInfo().getFeeType());
			msgPrep.setInt(37, basicMsg.getUmsFlag());
			msgPrep.setString(38, basicMsg.getCompanyID());
			// // 升级到ORACLE ，插入序列
			// if (DialectUtil.getDataBaseType() ==
			// DialectUtil.UMS_DATABASE_ORACLE) {
			// msgPrep.setString(39, getOutMsgSeqkey(conn, insertMessageType));
			// } else if (DialectUtil.getDataBaseType() ==
			// DialectUtil.UMS_DATABASE_SQLSERVER
			// || DialectUtil.getDataBaseType() ==
			// DialectUtil.UMS_DATABASE_SYBASE
			// || DialectUtil.getDataBaseType() ==
			// DialectUtil.UMS_DATABASE_MYSQL
			// || DialectUtil.getDataBaseType() == DialectUtil.UMS_DATABASE_DB2)
			// {
			// msgPrep.setInt(39, 0);
			// }
			msgPrep.setString(39, getOutMsgSeqkey(conn, insertMessageType));
			msgPrep.setInt(40, basicMsg.getSender().getIDType());
			msgPrep.setInt(41, basicMsg.getReceivers()[i].getIDType());
			msgPrep.setInt(42, basicMsg.getContentMode());
			// In or out messages, only in step of out_ready messages could
			// process
			// attachments
			if (insertMessageType == DBUtil_V3.MESSAGE_UMS_SEND_READY
					&& attachPrep != null) {
				if (basicMsg.getMsgAttachment() != null
						&& basicMsg.getMsgAttachment().length > 0) {
					// 防止空的附件消息
					if (!(basicMsg.getMsgAttachment().length == 1 && basicMsg
							.getMsgAttachment()[0].getFileName().equals(""))) {
						processAttachInsert(conn, attachPrep, msg, basicMsg, i);
					}
				}
			}
			msgPrep.executeUpdate();
		}
	}

	/**
	 * 处理附件的存储
	 * 
	 * @param conn
	 * @param attachPrep
	 * @param msg
	 * @param basicMsg
	 * @param receiverSeq
	 * @throws SQLException
	 * @throws IOException
	 */
	private void processAttachInsert(Connection conn,
			PreparedStatement attachPrep, UMSMsg msg, BasicMsg basicMsg,
			int receiverSeq) throws SQLException, IOException {
		for (int k = 0; k < basicMsg.getMsgAttachment().length; k++) {
			if (DialectUtil.getDataBaseType() == DialectUtil.UMS_DATABASE_ORACLE) {
				attachPrep.setString(1, msg.getBatchNO());
				attachPrep.setInt(2, msg.getSerialNO());
				attachPrep.setInt(3, msg.getSequenceNO()[receiverSeq]);
				attachPrep.setString(4, basicMsg.getMsgAttachment()[k]
						.getFileName());
				attachPrep.setBlob(5, oracle.sql.BLOB.empty_lob());
				// 先insert空的blob数据，然后再update
				attachPrep.executeUpdate();
				String selectSQL = "SELECT * FROM UMS_ATTACHMENTS WHERE BATCHNO=? AND SERIALNO=? AND SEQUENCENO=? AND FILENAME=? FOR UPDATE";
				PreparedStatement stmt2 = conn.prepareStatement(selectSQL);
				stmt2.setString(1, msg.getBatchNO());
				stmt2.setInt(2, msg.getSerialNO());
				stmt2.setInt(3, msg.getSequenceNO()[receiverSeq]);
				stmt2
						.setString(4, basicMsg.getMsgAttachment()[k]
								.getFileName());
				ResultSet rs2 = stmt2.executeQuery();
				while (rs2.next()) {
					BLOB blob = (BLOB) rs2.getBlob("FILECONTENT");
					OutputStream out = blob.getBinaryOutputStream(); // 建立输出流
					out.write(basicMsg.getMsgAttachment()[k]
							.getFileByteBase64().getBytes());
					out.close();
				}
			} else {
				attachPrep.setString(1, msg.getBatchNO());
				attachPrep.setInt(2, msg.getSerialNO());
				attachPrep.setInt(3, msg.getSequenceNO()[receiverSeq]);
				attachPrep.setString(4, basicMsg.getMsgAttachment()[k]
						.getFileName());
				InputStream is = new ByteArrayInputStream(basicMsg
						.getMsgAttachment()[k].getFileByteBase64().getBytes());
				is.close();
				attachPrep.setBinaryStream(5, is, is.available());
				attachPrep.executeUpdate();
				if (is != null) {
					is.close();
				}
			}
		}
	}

	/**
	 * 接收消息存储的方法
	 * 
	 * @param inPrep
	 *            接收消息的PreparedStatement对象
	 * @param attachPrep
	 *            接收消息的附件PreparedStatement对象
	 * @param msg
	 *            接收的消息对象
	 * @param insertMessageType
	 *            插入的消息类型，是待接收的，还是已接收的？待接收的是MESSAGE_UMS_RECEIVE_READY =
	 *            10;已接收的是MESSAGE_UMS_RECEIVE_OK = 11;
	 * @throws java.sql.SQLException
	 *             数据库异常
	 * @throws IOException
	 *             附件IO异常
	 */
	public void executeInMsgInsertStatement(PreparedStatement inPrep,
			PreparedStatement attachPrep, UMSMsg msg, int insertMessageType,
			Connection conn) throws SQLException, IOException {
		BasicMsg basic = msg.getBasicMsg();
		for (int i = 0; i < basic.getReceivers().length; i++) {
			inPrep.setString(1, msg.getBatchNO());
			inPrep.setInt(2, msg.getSerialNO());
			inPrep.setInt(3, msg.getSequenceNO()[0]);
			inPrep.setString(4, msg.getReturnCode());
			inPrep.setString(5, msg.getErrMsg());
			inPrep.setInt(6, msg.getStatusFlag());
			// 没有指定特定的服务号，则说明这个消息是转发消息，需要根据内容判断转发的服务号
			if (basic.getServiceID() == null || basic.getServiceID().equals("")) {
				String serviceKey = (String) AppServiceUtil
						.getForwardServiceMap().get(
								basic.getMsgContent().getContent());
				ServiceInfo service = (ServiceInfo) AppServiceUtil
						.getActiveAppServiceMap().get(serviceKey);
				basic.setServiceID(service == null ? "" : service
						.getServiceID());
			}
			inPrep.setString(7, basic.getServiceID());
			inPrep.setString(8, basic.getAppSerialNO());
			inPrep.setString(9, basic.getMediaID());
			inPrep.setString(10, basic.getSender().getParticipantID());
			inPrep.setString(11, basic.getReceivers()[0].getParticipantID());
			inPrep.setString(12, basic.getSubmitDate());
			inPrep.setString(13, basic.getSubmitTime());
			inPrep.setString(14, msg.getFinishDate());
			inPrep.setString(15, msg.getFinishTime());
			inPrep.setString(16, basic.getMsgContent().getSubject());
			inPrep.setString(17, basic.getMsgContent().getContent());
			inPrep.setString(18, msg.getMsgID());
			inPrep.setInt(19, basic.getAck());
			inPrep.setString(20, basic.getReplyDestination());
			inPrep.setInt(21, msg.getDoCount());
			inPrep.setInt(22, basic.getContentMode());
			// inPrep.setString(23, "");
			// inPrep.setString(24, "");
			// inPrep.setString(25, "");
			// inPrep.setString(26, "");
			// only in_ready type message may need attachments processing
			// 升级到ORACLE ，插入序列
			if (DialectUtil.getDataBaseType() == DialectUtil.UMS_DATABASE_ORACLE) {
				inPrep.setString(23, getInMsgSeqkey(conn, insertMessageType));
			} else if (DialectUtil.getDataBaseType() == DialectUtil.UMS_DATABASE_SQLSERVER
					|| DialectUtil.getDataBaseType() == DialectUtil.UMS_DATABASE_SYBASE) {
				inPrep.setInt(23, 0);
			}
			inPrep.setInt(24, basic.getSender().getIDType());
			if (insertMessageType == DBUtil_V3.MESSAGE_UMS_RECEIVE_READY) {
				if (basic.getMsgAttachment() != null
						&& basic.getMsgAttachment().length > 0) {
					processAttachInsert(conn, attachPrep, msg, basic, i);
				}
			}
			inPrep.executeUpdate();
		}
	}

	/**
	 * delete the unique message due to the parameter given.
	 * batchNO,serialNO,sequenceNO组合是唯一的标识
	 * 
	 * @param batchNO
	 * @param serialNO
	 * @param sequenceNO
	 * @param deleteStatement
	 * @throws SQLException
	 */
	public void excuteMsgDeleteStatement(String batchNO, int serialNO,
			int[] sequenceNO, PreparedStatement deleteStatement)
			throws SQLException {
		for (int i = 0; i < sequenceNO.length; i++) {
			deleteStatement.setString(1, batchNO);
			deleteStatement.setInt(2, serialNO);
			deleteStatement.setInt(3, sequenceNO[i]);
			deleteStatement.executeUpdate();
		}
	}

	/**
	 * 初始化附件集合对象
	 * 
	 * @param rs
	 *            附件对象集合对应的消息结果集
	 * @param attPrep
	 *            附件的PreparedStatement对象
	 * @return 附件集合
	 * @throws java.sql.SQLException
	 *             数据库异常
	 * @throws java.io.IOException
	 *             附件IO异常
	 */
	private MsgAttachment[] iniMsgAttachments(ResultSet rs,
			PreparedStatement attPrep) throws SQLException, IOException {
		attPrep.setString(1, rs.getString("BatchNO"));
		attPrep.setInt(2, rs.getInt("SerialNO"));
		attPrep.setInt(3, rs.getInt("SequenceNO"));
		ResultSet attRs = attPrep.executeQuery();
		ArrayList attArr = new ArrayList();
		while (attRs.next()) {
			MsgAttachment attach = new MsgAttachment();
			// 得到附件名称
			attach.setFileName(Util.replaceNULL(attRs.getString("FILENAME")));
			// 获取附件内容，各类数据库的处理方法不同
			if (DialectUtil.getDataBaseType() == DialectUtil.UMS_DATABASE_ORACLE) {
				java.sql.Blob blob = attRs.getBlob("FILECONTENT");
				InputStream is = blob.getBinaryStream();
				StringBuffer contentSB = new StringBuffer();
				byte[] buff = new byte[2048];
				int readCounts;
				while (-1 != (readCounts = is.read(buff, 0, buff.length))) {
					contentSB.append(new String(buff, 0, readCounts));
				}
				attach.setFileByteBase64(contentSB.toString());
				is.close();
			} else {// 其他数据库，这里
				// sqlserver ,sybase
				byte[] bytes = attRs.getBytes("FILECONTENT");
				attach.setFileByteBase64(new String(bytes));
			}
			attArr.add(attach);
		}
		MsgAttachment[] attachments = new MsgAttachment[attArr.size()];
		for (int i = 0; i < attArr.size(); i++) {
			attachments[i] = (MsgAttachment) attArr.get(i);
		}
		return attachments;
	}

	/**
	 * 初始化发送的基本消息
	 * 
	 * @param rs
	 *            基本消息对象对应的数据库数据集
	 * @param attPrep
	 *            附件的PreparedStatement对象
	 * @return 基本的BasicMsg对象
	 * @throws java.sql.SQLException
	 *             数据库异常
	 * @throws java.io.IOException
	 *             附件IO异常
	 */
	private BasicMsg iniOutBasicMsg(ResultSet rs, PreparedStatement attPrep)
			throws SQLException, IOException {
		BasicMsg basic = new BasicMsg();
		basic.setAck(rs.getInt("Ack"));
		basic.setAppSerialNO(Util.replaceNULL(rs.getString("AppSerialNO")));
		basic.setCompanyID(Util.replaceNULL(rs.getString("CompanyID")));
		basic.setDirectSendFlag(rs.getInt("SendDirectly"));
		basic.setFeeServiceNO(Util.replaceNULL(rs.getString("FeeServiceNO")));
		basic.setInvalidDate(Util.replaceNULL(rs.getString("InvalidDate")));
		basic.setInvalidTime(Util.replaceNULL(rs.getString("InvalidTime")));
		basic.setMediaID(Util.replaceNULL(rs.getString("MediaID")));
		basic.setMsgAttachment(iniMsgAttachments(rs, attPrep));
		basic.setMsgContent(new MsgContent(Util.replaceNULL(rs
				.getString("ContentSubject")), Util.replaceNULL(rs
				.getString("Content"))));
		basic.setNeedReply(rs.getInt("NeedReply"));
		basic.setPriority(rs.getInt("Priority"));
		basic.setReceivers(new Participant[] { new Participant(rs
				.getString("RecvID"), rs.getInt("ReceiverType"), rs
				.getInt("ReceiverIDType")) });
		basic.setReplyDestination(Util.replaceNULL(rs
				.getString("ReplyDestination")));
		basic.setSender(new Participant(Util
				.replaceNULL(rs.getString("SendID")), rs.getInt("SenderType"),
				rs.getInt("SenderIDType")));
		basic.setServiceID(Util.replaceNULL(rs.getString("ServiceID")));
		basic.setSetSendDate(Util.replaceNULL(rs.getString("SetDate")));
		basic.setSetSendTime(Util.replaceNULL(rs.getString("SetTime")));
		basic.setSubmitDate(Util.replaceNULL(rs.getString("SubmitDate")));
		basic.setSubmitTime(Util.replaceNULL(rs.getString("SubmitTime")));
		basic.setTimeSetFlag(rs.getInt("TimeSetFlag"));
		basic.setUmsFlag(rs.getInt("UMSFlag"));
		basic.setContentMode(rs.getInt("ContentMode"));
		return basic;
	}

	/**
	 * 初始化接收的消息BasicMsg对象
	 * 
	 * @param rs
	 *            接收消息对象对应的数据库数据集
	 * @param attPrep
	 *            附件PreparedStatement对象
	 * @return 基本BasicMsg对象
	 * @throws java.sql.SQLException
	 * @throws java.io.IOException
	 */
	private BasicMsg iniInBasicMsg(ResultSet rs, PreparedStatement attPrep)
			throws SQLException, IOException {
		BasicMsg basic = new BasicMsg();
		basic.setAck(rs.getInt("Ack"));
		basic.setAppSerialNO(Util.replaceNULL(rs.getString("AppSerialNO")));
		basic.setMediaID(Util.replaceNULL(rs.getString("MediaID")));
		basic.setMsgAttachment(iniMsgAttachments(rs, attPrep));
		basic.setMsgContent(new MsgContent(Util.replaceNULL(rs
				.getString("ContentSubject")), Util.replaceNULL(rs
				.getString("Content"))));
		basic.setReceivers(new Participant[] { new Participant(rs
				.getString("RecvID"), Participant.PARTICIPANT_MSG_TO, rs
				.getInt("ReceiverIDType")) });
		basic.setSender(new Participant(Util
				.replaceNULL(rs.getString("SendID")),
				Participant.PARTICIPANT_MSG_FROM, rs.getInt("SenderIDType")));
		basic.setServiceID(Util.replaceNULL(rs.getString("ServiceID")));
		basic.setSubmitDate(Util.replaceNULL(rs.getString("SubmitDate")));
		basic.setSubmitTime(Util.replaceNULL(rs.getString("SubmitTime")));
		basic.setContentMode(rs.getInt("msgType"));
		return basic;
	}

	/**
	 * get the out type messages
	 * 
	 * @param rs
	 *            must be ResultSet comes from statement: select * from (out
	 *            tables)
	 * @param attPrep
	 *            attachment preparestatement,comes form "SELECT * FROM
	 *            UMS_ATTACHMENTS WHERE BATCHNO = ? AND SERIALNO = ? AND
	 *            SEQUENCENO = ?"
	 * @return the array of UMSMsg
	 * @throws SQLException
	 * @throws IOException
	 */
	public ArrayList queryOutMsgs(ResultSet rs, PreparedStatement attPrep)
			throws SQLException, IOException {
		ArrayList arr = new ArrayList(16);
		while (rs.next()) {
			UMSMsg msg = new UMSMsg();
			msg.setBasicMsg(iniOutBasicMsg(rs, attPrep));
			msg.setBatchMode(rs.getInt("BatchMode"));
			msg.setBatchNO(Util.replaceNULL(rs.getString("BatchNO")));
			msg.setDoCount(rs.getInt("DoCount"));
			msg.setErrMsg(Util.replaceNULL(rs.getString("ErrMsg")));
			// msg.setFee(rs.getInt("Fee"));
			// msg.setFeeType(rs.getInt("FeeType"));
			FeeBean feeInfo = new FeeBean();
			feeInfo.setFeeServiceNO(rs.getString("FeeServiceNO"));
			msg.setFeeInfo(feeInfo);
			msg.setFinishDate(Util.replaceNULL(rs.getString("FinishDate")));
			msg.setFinishTime(Util.replaceNULL(rs.getString("FinishTime")));
			msg.setMsgID(Util.replaceNULL(rs.getString("MsgID")));
			msg.setRep(rs.getInt("Rep"));
			msg.setReturnCode(Util.replaceNULL(rs.getString("RetCode")));
			msg.setSequenceNO(new int[] { rs.getInt("SequenceNO") });
			msg.setSerialNO(rs.getInt("SerialNO"));
			msg.setStatusFlag(rs.getInt("StatusFlag"));
			arr.add(msg);
		}
		return arr;
	}

	/**
	 * 查询接收的消息
	 * 
	 * @param rs
	 * @param attPrep
	 * @return ArrayList接收消息集合
	 * @throws SQLException
	 * @throws IOException
	 */
	public ArrayList queryInMsgs(ResultSet rs, PreparedStatement attPrep)
			throws SQLException, IOException {
		ArrayList arr = new ArrayList(5);
		while (rs.next()) {
			UMSMsg msg = new UMSMsg();
			msg.setBasicMsg(iniInBasicMsg(rs, attPrep));
			// msg.setBatchMode(rs.getInt("BatchMode"));
			msg.setBatchNO(Util.replaceNULL(rs.getString("BatchNO")));
			msg.setSequenceNO(new int[] { rs.getInt("SequenceNO") });
			msg.setSerialNO(rs.getInt("SerialNO"));
			msg.setStatusFlag(rs.getInt("StatusFlag"));
			msg.setMsgID(Util.replaceNULL(rs.getString("MsgID")));
			msg.setFinishDate(Util.replaceNULL(rs.getString("FinishDate")));
			msg.setFinishTime(Util.replaceNULL(rs.getString("FinishTime")));
			arr.add(msg);
		}
		return arr;
	}
}
