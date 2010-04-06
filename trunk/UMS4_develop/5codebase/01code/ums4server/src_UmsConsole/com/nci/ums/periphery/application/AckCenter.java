package com.nci.ums.periphery.application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nci.ums.basic.UMSModule;
import com.nci.ums.util.ClientWSUtil;
import com.nci.ums.util.DialectUtil;
import com.nci.ums.util.Res;
import com.nci.ums.util.db.DBOptLock;
import com.nci.ums.v3.message.ack.UMSAckMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.service.common.UMSClientWS;

public class AckCenter implements UMSModule, Runnable {

	private boolean stop;
	private Map clientWSArr;

	private String queryAckMsgSQL;
	private static AckCenter instance;

	private AckCenter() {
	}

	public static synchronized AckCenter getInstance() {
		if (instance == null)
			instance = new AckCenter();
		return instance;
	}

	public void startModule() {
		stop = false;

		Res.log(Res.INFO, "回执守护线程启动！");
		clientWSArr = ClientWSUtil.getClientWSMap();
		queryAckMsgSQL = DialectUtil.getDialect().getAckMsgSQL_V3();
		Thread t = new Thread(this);
		t.setName("UMS回执线程");
		t.start();
	}

	public void stopModule() {
		stop = true;
		clientWSArr = null;
		queryAckMsgSQL = null;
	}

	public void run() {
		while (!stop) {
			boolean ws_hasnext = false;
			// 按serviceid获取回执信息
			Map msgsMap = getAck4Service();
			List msgs;
			int flag;
			Iterator it = msgsMap.keySet().iterator();
			while (it.hasNext()) {

				String serviceid = (String) it.next();
				msgs = (List) msgsMap.get(serviceid);

				String xml = parseXML(msgs);
				System.out.println("给" + serviceid + "发送回执：" + xml);
				flag = BasicMsg.UMSMsg_ACK_ACKNOLEDGED;
				try {
					UMSClientWS clientWs = (UMSClientWS) clientWSArr
							.get(serviceid);
					if (clientWs != null)
						clientWs.sendAckMsg(xml);
					else
						flag = BasicMsg.UMSMsg_ACK_NO_SERVICE_REGESTERED;

				} catch (Exception e) {
					Res.logExceptionTrace(e);
					flag = BasicMsg.UMSMsg_ACK_COMMUNICATION_ERROR;
				}
				updateAckSent(msgs, flag);
			}
			if (!ws_hasnext)
				try {
					Thread.sleep(3000L);
				} catch (InterruptedException interruptedexception) {
				}
		}
	}

	/**
	 * 更新回执信息状态
	 * 
	 * @param msgs
	 * @param flag
	 */
	private void updateAckSent(List msgs, int flag) {
		StringBuffer sb;
		Connection conn;
		sb = (new StringBuffer("update UMS_SEND_OK set ack=")).append(flag)
				.append(" where seqkey in(");
		for (int i = 0; i < msgs.size(); i++) {
			sb.append(((UMSAckMsg) msgs.get(i)).getSeqKey());
			if (i != msgs.size() - 1)
				sb.append(",");
			else
				sb.append(")");
		}
		while (DBOptLock.dbLock == true) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		conn = null;
		try {
			conn = Res.getConnection();
			conn.createStatement().executeUpdate(sb.toString());
		} catch (Exception e) {
			Res.logExceptionTrace(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Res.logExceptionTrace(e);
				}
			}
		}

	}

	/**
	 * 将回执信息转换成XML
	 * 
	 * @param msgs
	 * @return
	 */
	private String parseXML(List msgs) {
		StringBuffer sb = new StringBuffer();
		sb.append("<ack>").append("\r\n");
		for (int i = 0; i < msgs.size(); i++) {
			UMSAckMsg msg = (UMSAckMsg) msgs.get(i);
			sb.append(msg.toString());
			sb.append("\r\n");
		}
		sb.append("</ack>");
		return sb.toString();
	}

	/**
	 * 获取回执信息
	 * 
	 * @return
	 */
	private Map getAck4Service() {
		Connection conn;
		Map map;
		conn = null;
		map = new HashMap();
		try {
			while (DBOptLock.dbLock == true) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			conn = Res.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(queryAckMsgSQL);
			String previousService = "";
			List msgs = null;
			String status;
			while(rs.next()){
				String serviceid = rs.getString("serviceid");
				if (!previousService.equals(serviceid)) {
					msgs = new ArrayList();
					map.put(serviceid, msgs);
				}
				status = rs.getString("errmsg");
				
				String batchno = rs.getString("batchno");
				int serialno = rs.getInt("serialno");
				int sequenceno = rs.getInt("sequenceno");
				String seqkey = rs.getString("seqkey");
				msgs.add(new UMSAckMsg(batchno,
						serialno, sequenceno, seqkey, status));				
				
			}
//			for (; rs.next(); msgs.add(new UMSAckMsg(rs.getString("batchno"),
//					rs.getInt("serialno"), rs.getInt("sequenceno"), rs
//							.getInt("seqkey"), status))) {
//				String serviceid = rs.getString("serviceid");
//				status = rs.getString("errmsg");
//				if (!previousService.equals(serviceid)) {
//					msgs = new ArrayList();
//					map.put(serviceid, msgs);
//				}
//			}

		} catch (Exception e) {

			Res.logExceptionTrace(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return map;
	}
}
