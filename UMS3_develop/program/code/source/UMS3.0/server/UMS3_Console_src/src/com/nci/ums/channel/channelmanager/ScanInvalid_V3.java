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
 * ����ɨ���ࡣ���ฺ��UMS3.0���漰�������ݵ���Ч���жϣ��������͵���Ϣ�����յ���Ϣ���ڴ��е���Ϣ�ȡ�
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
	 * ��ҵ�û��ӿ�
	 */
	private EnterpriseUserInfo eui;
	/**
	 * MonitorServer_V3����������Ҫ�������͸澯��Ϣ��
	 */
	private MonitorServer_V3 warnSender = MonitorServer_V3.getInstance();// ������Ϊ�����ʹ���澯�ʼ�

	public ScanInvalid_V3() {
		quitFlag = QuitLockFlag.getInstance();
		inMsgScanSQL = "SELECT BATCHNO, SERIALNO, SEQUENCENO, SENDID, SENDERIDTYPE,CONTENT,SERVICEID FROM IN_READY_V3 WHERE SERVICEID NOT IN (SELECT SERVICEID FROM SERVICE_V3 WHERE STATUS = "
				+ ServiceInfo.SERVICE_STATUS_VALID + ")";
		eui = Util.getEuiObject();
		Res.log(Res.INFO, "UMS3ɨ��ʧЧ�����̶߳������");
	}

	public void start() {
		runner = new Thread(this, "ScanInvalid_V3");
		runner.start();
		Res.log(Res.INFO, "UMS3ɨ��ʧЧ�����߳�����");
	}

	public void stop() {
		runner.interrupt();
		runner = null;
		Res.log(Res.INFO, "UMS3ɨ��ʧЧ�����߳̽���");
	}

	public void run() {
		while (!Thread.interrupted() && !quitFlag.getLockFlag()) {
			scanTable();
			scanInMsg();
			scanInvalidServices();
			try {
				// Res.log(Res.INFO, "UMS3ɨ��ʧЧ�����߳̽�˯��1Сʱ");
				Thread.sleep(60 * 60 * 1000);
			} catch (InterruptedException e) {
				quitFlag.setLockFlag(true);
				Res.log(Res.ERROR, "UMS3ɨ��ʧЧ�����߳�˯�߱��жϣ�");
			}
		}
	}

	/**
	 * ���������Ϣ���������õ����ݣ�����ĳ��Service��ͣ���ˣ�����������Ȼ���ڡ�
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
	 * ɨ�貦����Ϣ�ı�. 1. �û������serviceID��UMSֻ�ܲ�������ڵ�ServiceID
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
					StringBuffer mail = new StringBuffer("�����͸�UMS�ķ���ID��")
							.append(serviceID).append(")�����ڣ�����ֹͣ���С�\r\n\r\n").append(
									"ԭ��Ϣ����Ϊ��\r\n").append(content);
					try {

						warnSender.sendWarning("UMS WARNING", mail.toString(),
								senderID);
					} catch (ApplicationException e) {
						Res.log(Res.ERROR, "���ͽ�����Ϣ�澯����." + e.getMessage());
                                                Res.logExceptionTrace(e);
					}
				}
			}
			conn.commit();
			rs.close();
			deleteMsgPrep.close();
			deleteAttPrep.close();
		} catch (SQLException e) {
			Res.log(Res.ERROR, "���ͽ�����Ϣ�澯ʱ���ݿ����." + e.getMessage());
                        Res.logExceptionTrace(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Res.log(Res.ERROR, "���ͽ�����Ϣ�澯ʱ�޷��ر����ݿ�����." + e.getMessage());
				}
		}
	}

	/**
	 * ɨ���ⷢ��Ϣ�ı�
	 */
	public void scanTable() {
		// out_ready_v3
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		// ���ڲ��뵽out_ok_v3
		PreparedStatement insertStmt = null;
		// ����ɾ��out_ready_v3
		PreparedStatement deleteStmt = null;
		// ���ڸ�������
		PreparedStatement attPrep = null;
		// ���ڻ�ִ����
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
			// ������ݼ���Ϊ�յĻ�����������Ĺ�����
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
			Res.log(Res.ERROR, "��ȡ������Ϣ����");
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
	 * �������� ֻ�Ǽ򵥵İ�ʧЧ�����ݴ�out_readyȡ�����ŵ�out_ok���С� ע��errMsg ��retCode ����ֵ
	 */
	private void process(Connection conn, ResultSet rs,
			PreparedStatement attPrep, PreparedStatement insertStmt,
			PreparedStatement deleteStmt, PreparedStatement ackNeededInPrep)
			throws SQLException, IOException {
		ArrayList msgArr = dbUtil.queryOutMsgs(rs, attPrep);
		for (int i = 0; i < msgArr.size(); i++) {
			UMSMsg msg = (UMSMsg) msgArr.get(i);
			// ��ʧ�ܵ���Ϣ����out_ok_v3��
			Res.log(Res.DEBUG, "����ʧ����Ϣ������Ϣ����OUT_ERROR_V3");
			dbUtil.executeOutMsgInsertStatement(insertStmt, null, msg,
					DBUtil_V3.MESSAGE_OUT_ERROR_V3, conn);
			// ��ʧ����Ϣ��out_ready_v3����ɾ��
			Res.log(Res.DEBUG, "����ʧ����Ϣ������Ϣ��OUT_READY_V3ɾ��");
			dbUtil.excuteMsgDeleteStatement(msg.getBatchNO(),
					msg.getSerialNO(), msg.getSequenceNO(), deleteStmt);
			// �������ִ��ʧ����Ϣ�������Ͳ��ɹ�����Ϣ������Ӧ��
			if (msg.getBasicMsg().getAck() == BasicMsg.UMSMsg_ACK_YES) {
				Res.log(Res.DEBUG, "�������ִ��ʧ����Ϣ������Ϣʧ�ܱ�־������Ӧ��");
				dbUtil.executeInMsgInsertStatement(ackNeededInPrep, null, msg,
						DBUtil_V3.MESSAGE_IN_READY_V3, conn);
			}
		}
	}
}
