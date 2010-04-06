package com.nci.ums.channel.channelmanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import com.nci.ums.channel.channelinfo.QuitLockFlag;
import com.nci.ums.periphery.application.MonitorServer_V3;
import com.nci.ums.periphery.application.ReceiveCenter;
import com.nci.ums.periphery.exception.ApplicationException;
import com.nci.ums.util.AppServiceUtil;
import com.nci.ums.util.DBUtil_V3;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.message.euis.EnterpriseUserInfo;
import com.nci.ums.v3.message.euis.UUMUserInfo;
import com.nci.ums.v3.service.ServiceInfo;

/**
 * 数据扫描类。该类负责UMS3.0中涉及到的数据的有效性判断，包括发送的消息，接收的消息，内存中的消息等。
 * 
 * @author Qil.Wong
 * 
 */
public class ScanInvalid_V3 implements Runnable {

	private QuitLockFlag quitFlag;
	private Thread runner;
	private DBUtil_V3 dbUtil = new DBUtil_V3();
	private String inMsgScanSQL;
	/**
	 * 企业用户接口
	 */
	private EnterpriseUserInfo eui;
	/**
	 * MonitorServer_V3对象，这里主要用来发送告警信息。
	 */
	private MonitorServer_V3 warnSender = MonitorServer_V3.getInstance();// 仅仅作为来发送错误告警邮件

	public ScanInvalid_V3() {
		quitFlag = QuitLockFlag.getInstance();
		inMsgScanSQL = "SELECT BATCHNO, SERIALNO, SEQUENCENO, SENDID, SENDERIDTYPE,CONTENT,SERVICEID FROM IN_READY_V3 WHERE SERVICEID NOT IN (SELECT SERVICEID FROM SERVICE_V3 WHERE STATUS = "
				+ ServiceInfo.SERVICE_STATUS_VALID + ")";
		eui = Util.getEuiObject();
		Res.log(Res.INFO, "UMS3扫描失效数据线程对象产生");
	}

	public void start() {
		runner = new Thread(this, "ScanInvalid_V3");
		runner.start();
		Res.log(Res.INFO, "UMS3扫描失效数据线程启动");
	}

	public void stop() {
		runner.interrupt();
		runner = null;
		Res.log(Res.INFO, "UMS3扫描失效数据线程结束");
	}

	public void run() {
		while (!Thread.interrupted() && !quitFlag.getLockFlag()) {
			scanTable();
			scanInMsg();
			scanInvalidServices();
			try {
				// Res.log(Res.INFO, "UMS3扫描失效数据线程将睡眠1小时");
				Thread.sleep(60 * 60 * 1000);
			} catch (InterruptedException e) {
				quitFlag.setLockFlag(true);
				Res.log(Res.ERROR, "UMS3扫描失效数据线程睡眠被中断！");
			}
		}
	}

	/**
	 * 清除接收消息缓存中无用的数据，可能某个Service被停用了，但缓存中依然存在。
	 * 
	 */
	private void scanInvalidServices() {
		Map inMsgMap = ReceiveCenter.getInMsgMap();
		Map activeServices = AppServiceUtil.getActiveAppServiceMap();
		Iterator msgItera = inMsgMap.keySet().iterator();
		while (msgItera.hasNext()) {
			String msgServiceID = (String) msgItera.next();
			if (!activeServices.containsKey(msgServiceID)) {
				inMsgMap.remove(msgServiceID);
			}
		}
	}

	/**
	 * 扫描拨入消息的表. 1. 用户输错了serviceID，UMS只能查出不存在的ServiceID
	 */
	public void scanInMsg() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			conn.setAutoCommit(false);
			ResultSet rs = conn.createStatement().executeQuery(inMsgScanSQL);
			PreparedStatement deleteMsgPrep = conn.prepareStatement(DBUtil_V3
					.createDeleteMsgSQL(DBUtil_V3.MESSAGE_IN_READY_V3));
			PreparedStatement deleteAttPrep = conn.prepareStatement(DBUtil_V3
					.createDeleteMsgSQL(DBUtil_V3.MESSAGE_IN_OUT_ATTACH_V3));
			while (rs.next()) {
				String batchNO = rs.getString("BatchNO");
				int serialNO = rs.getInt("SerialNO");
				int sequenceNO = rs.getInt("SequenceNO");
				String senderID = rs.getString("SendID");
				int senderIDType = rs.getInt("SenderIDType");
				String content = rs.getString("Content");
				String serviceID = rs.getString("ServiceID");
				dbUtil.excuteMsgDeleteStatement(batchNO, serialNO,
						new int[] { sequenceNO }, deleteMsgPrep);
				dbUtil.excuteMsgDeleteStatement(batchNO, serialNO,
						new int[] { sequenceNO }, deleteAttPrep);
				if (senderIDType != Participant.PARTICIPANT_ID_EMAIL) {
					senderID = eui.getAccout(new Participant(senderID,
							Participant.PARTICIPANT_MSG_FROM, senderIDType),
							Participant.PARTICIPANT_ID_EMAIL);
				}
				if (senderID != null && !senderID.equals("")
						&& senderID.indexOf("@") > 0) {
					StringBuffer mail = new StringBuffer("您发送给UMS的服务ID（")
							.append(serviceID).append(")不存在，或已停止运行。\r\n\r\n").append(
									"原消息内容为：\r\n").append(content);
					try {

						warnSender.sendWarning("UMS WARNING", mail.toString(),
								senderID);
					} catch (ApplicationException e) {
						Res.log(Res.ERROR, "发送接入消息告警错误." + e.getMessage());
                                                Res.logExceptionTrace(e);
					}
				}
			}
			conn.commit();
			rs.close();
			deleteMsgPrep.close();
			deleteAttPrep.close();
		} catch (SQLException e) {
			Res.log(Res.ERROR, "发送接入消息告警时数据库错误." + e.getMessage());
                        Res.logExceptionTrace(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Res.log(Res.ERROR, "发送接入消息告警时无法关闭数据库连接." + e.getMessage());
				}
		}
	}

	/**
	 * 扫描外发消息的表
	 */
	public void scanTable() {
		// out_ready_v3
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		// 用于插入到out_ok_v3
		PreparedStatement insertStmt = null;
		// 用于删除out_ready_v3
		PreparedStatement deleteStmt = null;
		// 用于附件处理
		PreparedStatement attPrep = null;
		// 用于回执处理
		PreparedStatement ackNeededInPrep = null;
		StringBuffer sql = null;
		int currentDate = Util.getCurrentTime("yyyyMMdd");
		int currentTime = Util.getCurrentTime("HHmmss");

		sql = new StringBuffer(
				"select * from out_ready_v3 where (statusflag = 1 or (timesetflag=0 and statusflag=0))and ( InvalidDate < ")
				.append(currentDate).append(" or (InvalidDate = ").append(
						currentDate).append(" and InvalidTime < ").append(
						currentTime).append("))");
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql.toString());
			rs.setFetchSize(100);
			// 如果数据集合为空的话，则不做下面的工作。
			if (rs.next()) {
				insertStmt = conn.prepareStatement(DBUtil_V3
						.createOutMsgInsertSQL(DBUtil_V3.MESSAGE_OUT_OK_V3));
				deleteStmt = conn.prepareStatement(DBUtil_V3
						.createDeleteMsgSQL(DBUtil_V3.MESSAGE_OUT_READY_V3));
				ackNeededInPrep = conn.prepareStatement(DBUtil_V3
						.createInMsgInsertSQL(DBUtil_V3.MESSAGE_IN_READY_V3));
				attPrep = conn
						.prepareStatement("SELECT * FROM IN_OUT_ATTACHMENTS_V3 WHERE BATCHNO = ? AND SERIALNO = ? AND SEQUENCENO = ?");
				rs.beforeFirst();
				process(conn, rs, attPrep, insertStmt, deleteStmt,
						ackNeededInPrep);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "1091", e.getMessage());
                        Res.logExceptionTrace(e);
		} catch (IOException e) {
			Res.log(Res.ERROR, "读取附件信息错误");
                        Res.logExceptionTrace(e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Res.log(Res.ERROR, "1091", e.getMessage());
                                Res.logExceptionTrace(e);
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (insertStmt != null) {
					insertStmt.close();
				}
				if (attPrep != null) {
					attPrep.close();
				}
				if (deleteStmt != null) {
					deleteStmt.close();
				}
				if (ackNeededInPrep != null) {
					ackNeededInPrep.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Res.log(Res.ERROR, "1091", e.getMessage());
                                Res.logExceptionTrace(e);
			}
		}
	}

	/*
	 * 处理函数， 只是简单的把失效的数据从out_ready取出，放到out_ok表中。 注意errMsg 和retCode 的数值
	 */
	private void process(Connection conn, ResultSet rs,
			PreparedStatement attPrep, PreparedStatement insertStmt,
			PreparedStatement deleteStmt, PreparedStatement ackNeededInPrep)
			throws SQLException, IOException {
		ArrayList msgArr = dbUtil.queryOutMsgs(rs, attPrep);
		for (int i = 0; i < msgArr.size(); i++) {
			UMSMsg msg = (UMSMsg) msgArr.get(i);
			// 将失败的消息放入out_ok_v3表
			Res.log(Res.DEBUG, "处理失败消息：将消息放入OUT_ERROR_V3");
			dbUtil.executeOutMsgInsertStatement(insertStmt, null, msg,
					DBUtil_V3.MESSAGE_OUT_ERROR_V3, conn);
			// 将失败消息从out_ready_v3表中删除
			Res.log(Res.DEBUG, "处理失败消息：将消息从OUT_READY_V3删除");
			dbUtil.excuteMsgDeleteStatement(msg.getBatchNO(),
					msg.getSerialNO(), msg.getSequenceNO(), deleteStmt);
			// 处理需回执的失败消息，将发送不成功的信息反馈给应用
			if (msg.getBasicMsg().getAck() == BasicMsg.UMSMsg_ACK_YES) {
				Res.log(Res.DEBUG, "处理需回执的失败消息：将消息失败标志反馈给应用");
				dbUtil.executeInMsgInsertStatement(ackNeededInPrep, null, msg,
						DBUtil_V3.MESSAGE_IN_READY_V3, conn);
			}
		}
	}
}
