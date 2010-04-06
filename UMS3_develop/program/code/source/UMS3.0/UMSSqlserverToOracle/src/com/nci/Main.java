/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.nci.message.SQLServerMsg;
import com.nci.ums.client.ws.UMS3_MsgSendStub;
import com.nci.ums.client.ws.UMS3_MsgSendStub.SendWithAckResponse;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.util.ConnectionManager;
import com.nci.util.KnoLogger;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author Qil.Wong
 */
public class Main {

	private SimpleDateFormat sdf = null;
	private String ums_endpoint = null;
	private String appID = null;
	private String appPsw = null;
	private boolean timmerMsgExist = false;
	private XStream xstream = null;
	private UMS3_MsgSendStub stub;
	private UMS3_MsgSendStub.SendWithAck send;
	

	public Main() throws AxisFault {
		FileInputStream fis = null;
		sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			fis = new FileInputStream(System.getProperty("user.dir")
					+ "/conf/ums.prop");
			Properties prop = new Properties();
			prop.load(fis);
			ums_endpoint = prop.getProperty("endpoint_send");
			appID = prop.getProperty("appID");
			appPsw = prop.getProperty("appPsw");
			fis.close();
			fis = new FileInputStream(System.getProperty("user.dir")
					+ "/conf/timmerMsg.prop");
			prop.load(fis);
			timmerMsgExist = prop.get("timmerMsgExist").equals("true");
		} catch (Exception ex) {
			KnoLogger.log(Level.ERROR, ex.getMessage(), ex);
		} 
		xstream = new XStream();
		xstream.setClassLoader(getClass().getClassLoader());
		xstream.alias("UMSMsg", UMSMsg.class);
		xstream.alias("BasicMsg", BasicMsg.class);
		xstream.alias("MsgAttachment", MsgAttachment.class);
		xstream.alias("MsgContent", MsgContent.class);
		xstream.alias("Participant", Participant.class);
		// xstream.alias("EmailMsgPlus", EmailMsgPlus.class);
		stub = new UMS3_MsgSendStub(ums_endpoint);
		send = new UMS3_MsgSendStub.SendWithAck();
		send.setAppID(appID);
		send.setPassword(appPsw);
	}

	public ArrayList<SQLServerMsg> getSQLServerMsgs() {
		ArrayList<SQLServerMsg> results = new ArrayList<SQLServerMsg>();
		Connection sqlserverConn = null;
		try {
			sqlserverConn = DriverManager.getConnection(ConnectionManager
					.getPoolName());
			// sqlserverConn =
			// DBConnectionManager.getInstance().getConnection(Constants.POOLNAME_SQLSERVER);
			ResultSet rs = sqlserverConn.createStatement().executeQuery(
					"SELECT TOP 16 * FROM tbl_smsendTask");
			while (rs.next()) {
				SQLServerMsg sqlserverMsg = new SQLServerMsg();
				sqlserverMsg.setCount(rs.getInt("Count"));
				sqlserverMsg.setCreatorID(rs.getString("CreatorID"));
				sqlserverMsg.setDestAddr(rs.getString("DestAddr"));
				sqlserverMsg.setDestAddrType(rs.getInt("DestAddrType"));
				sqlserverMsg.setFeeCode(rs.getString("FeeCode"));
				sqlserverMsg.setFeeType(rs.getString("FeeType"));
				sqlserverMsg.setID(rs.getInt("ID"));
				sqlserverMsg.setMessageID(rs.getString("MessageID"));
				sqlserverMsg.setMsgID(rs.getString("MsgID"));
				sqlserverMsg.setNeedStateReport(rs.getInt("NeedStateReport"));
				sqlserverMsg.setOperationType(rs.getString("OperationType"));
				sqlserverMsg.setOrgAddr(rs.getString("OrgAddr"));
				sqlserverMsg.setSendLevel(rs.getInt("SendLevel"));
				sqlserverMsg.setSendState(rs.getInt("SendState"));
				sqlserverMsg.setSendTime(rs.getTimestamp("SendTime"));
				sqlserverMsg.setSendType(rs.getInt("SendType"));
				sqlserverMsg.setServiceID(rs.getString("ServiceID"));
				sqlserverMsg.setSmSendedNum(rs.getInt("SmSendedNum"));
				sqlserverMsg.setSmType(rs.getInt("smType"));
				sqlserverMsg.setSm_Content(rs.getString("Sm_Content"));
				sqlserverMsg.setSubOperationType(rs
						.getString("SubOperationType"));
				sqlserverMsg.setSubTime(rs.getTimestamp("SubTime"));
				sqlserverMsg.setSuccessID(rs.getInt("SuccessID"));
				sqlserverMsg.setTaskName(rs.getString("TaskName"));
				sqlserverMsg.setTaskStatus(rs.getInt("TaskStatus"));
				sqlserverMsg.setTrytimes(rs.getInt("Trytimes"));
				results.add(sqlserverMsg);
			}
		} catch (Exception e) {
			KnoLogger.log(Level.ERROR, e.getMessage(), e);
			try {
				sqlserverConn.close();
			} catch (SQLException e1) {
				KnoLogger.log(Level.ERROR, "关闭数据库连接出错！" + e1.getMessage(), e1);
			}
		} finally {
			if (sqlserverConn != null) {
				try {
					sqlserverConn.close();
				} catch (SQLException e) {
					KnoLogger
							.log(Level.ERROR, "关闭数据库连接出错！" + e.getMessage(), e);
				}
			}
		}
		return results;
	}

	public void deleteSQLServerMsgAndSendToUMS(ArrayList<SQLServerMsg> results) {
		Connection sqlserverConn = null;
		try {
			sqlserverConn = DriverManager.getConnection(ConnectionManager
					.getPoolName());
			sqlserverConn.setAutoCommit(false);
			PreparedStatement prep = sqlserverConn
					.prepareStatement("DELETE FROM tbl_smsendTask WHERE ID = ?");
			for (int i = 0; i < results.size(); i++) {
				prep.setInt(1, results.get(i).getID());
				prep.executeUpdate();
			}
			// int flag = 0;
			int flag = this.sendToUMS(results);
			if (flag == 0) {
				sqlserverConn.commit();
			} else {
				sqlserverConn.rollback();
			}
		} catch (Exception e) {
			try {
				KnoLogger.log(Level.ERROR, e.getMessage(), e);
				sqlserverConn.rollback();
			} catch (SQLException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.ERROR,
						ex.getMessage(), ex);
				try {
					sqlserverConn.close();
				} catch (SQLException e1) {
					KnoLogger.log(Level.ERROR, "关闭数据库连接出错！" + e1.getMessage(),
							e1);
				}
			}
		} finally {
			if (sqlserverConn != null) {
				try {
					sqlserverConn.close();
				} catch (SQLException e) {
					KnoLogger
							.log(Level.ERROR, "关闭数据库连接出错！" + e.getMessage(), e);
				}
			}
		}
	}

	private int sendToUMS(ArrayList<SQLServerMsg> results) throws SQLException,
			RemoteException {
		BasicMsg[] basicMsgs = new BasicMsg[results.size()];
		SQLServerMsg sqlMsg = null;
		for (int i = 0; i < results.size(); i++) {
			sqlMsg = results.get(i);
			basicMsgs[i] = new BasicMsg();
			basicMsgs[i].setAck(BasicMsg.UMSMsg_ACK_NO);
			basicMsgs[i].setContentMode(BasicMsg.BASICMSG_CONTENTE_MODE_GBK);
			basicMsgs[i].setDirectSendFlag(BasicMsg.BASICMSG_DIRECTSEND_YES);
			basicMsgs[i].setMediaID("015");
			basicMsgs[i].setMsgContent(new MsgContent("", results.get(i)
					.getSm_Content()));
			basicMsgs[i].setPriority(BasicMsg.UMSMsg_PRIORITY_NORMAL);
			Participant[] receivers = new Participant[1];
			receivers[0] = new Participant(sqlMsg.getDestAddr(),
					Participant.PARTICIPANT_MSG_TO,
					Participant.PARTICIPANT_ID_MOBILE);
			basicMsgs[i].setReceivers(receivers);
			basicMsgs[i].setSender(new Participant(sqlMsg.getOrgAddr(),
					Participant.PARTICIPANT_MSG_FROM,
					Participant.PARTICIPANT_ID_MOBILE));
			basicMsgs[i].setServiceID(sqlMsg.getCreatorID());
			String s = sdf.format(new Date());
			basicMsgs[i].setSubmitDate(s.substring(0, 8));
			basicMsgs[i].setSubmitTime(s.substring(8, 14));
			if (timmerMsgExist && sqlMsg.getSendTime() != null
					&& sqlMsg.getSendTime().after(new Date())) {
				s = sdf.format(sqlMsg.getSendTime());
				String logInfo = "定时消息，发送时间为:" + s;
				System.out.println(logInfo);
				KnoLogger.log(Level.DEBUG, logInfo, null);
				basicMsgs[i].setTimeSetFlag(BasicMsg.BASICMSG_SENDTIME_CUSTOM);
				basicMsgs[i].setSetSendDate(s.substring(0, 8));
				basicMsgs[i].setSetSendTime(s.substring(8, 14));
			} else {
				basicMsgs[i]
						.setTimeSetFlag(BasicMsg.BASICMSG_SENDTIME_NOCUSTOM);
			}

		}
		send.setBasicMsgsXML(xstream.toXML(basicMsgs));
		SendWithAckResponse resp = stub.sendWithAck(send);
		String returnFlag = resp.get_return();
		if (returnFlag.equals("WSV_SUCCESS")) {
			return 0;
		} else {
			if (returnFlag.equals("WSV_DATE_ERR")) {
				String err = "定时消息日期错误，定时日期必须晚于当前日期，消息无效！";
				KnoLogger.log(Level.INFO, err, null);
				return 0;
			}
			return 1;
		}
	}

	public static void main(String[] args) throws AxisFault,
			InterruptedException {
		KnoLogger.log(Level.INFO, "华为信息机数据库转发服务启动！", null);
		Main m = new Main();
		while (true) {
			System.out.println("Querying message(s) from SQL Server...");
			ArrayList<SQLServerMsg> results = m.getSQLServerMsgs();
			System.out.println("Received " + results.size() + " message(s).");
			if (results.size() > 0) {
				System.out.println("Sending these message(s) to UMS...");
				m.deleteSQLServerMsgAndSendToUMS(results);
				System.out.println("Sending complete!");
			} else {
				Thread.sleep(3000);
			}
		}
	}
}
