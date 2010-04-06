package com.nci.ums.periphery.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nci.ums.util.ClientWSUtil;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;
import com.nci.ums.v3.message.ack.UMSAckMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.service.common.UMSClientWS;

public class AckCenter extends Thread {

	private boolean stop;
	private Map clientWSArr;

	private String queryAckMsgSQL;
	private static AckCenter instance;

	private AckCenter() {
		stop = false;
		clientWSArr = null;

		Res.log(Res.INFO, "回执守护线程启动！");
		clientWSArr = ClientWSUtil.getClientWSMap();
		queryAckMsgSQL = Util.getDialect().getAckMsgSQL_V3();
		setName("UMS回执线程");
	}

	public static synchronized AckCenter getInstance() {
		if (instance == null)
			instance = new AckCenter();
		return instance;
	}

	public void pleaseStop() {
		stop = true;
		instance = null;
	}

	public void run() {
		while (!stop) {
			boolean ws_hasnext = false;
			//按serviceid获取回执信息
			Map msgsMap = getAck4Service();
			List msgs;
			int flag;
			Iterator it = msgsMap.keySet().iterator();
			while (it.hasNext()) {

				String serviceid = (String) it.next();
				msgs = (List) msgsMap.get(serviceid);

				String xml = parseXML(msgs);
				Res.log(Res.DEBUG, "给"+serviceid+"发送回执："+xml);
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

	private void updateAckSent(List msgs, int flag) {
		StringBuffer sb;
		Connection conn;
		sb = (new StringBuffer("update out_ok_v3 set ack=")).append(flag)
				.append(" where seqkey in(");
		for (int i = 0; i < msgs.size(); i++) {
			sb.append(((UMSAckMsg) msgs.get(i)).getSeqKey());
			if (i != msgs.size() - 1)
				sb.append(",");
			else
				sb.append(")");
		}

		conn = null;
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
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
	 * 转换成回执xml
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

	private Map getAck4Service() {
		Connection conn;
		Map map;
		conn = null;
		map = new HashMap();
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(queryAckMsgSQL);
			String previousService = "";
			List msgs = null;
			String status;
			while(rs.next()){
				String serviceid = rs.getString("serviceid");
				status = rs.getString("errmsg");
				if (!previousService.equals(serviceid)) {
					msgs = new ArrayList();
					map.put(serviceid, msgs);
				}
				msgs.add(new UMSAckMsg(rs.getString("batchno"),
						rs.getInt("serialno"), rs.getInt("sequenceno"), rs
								.getString("seqkey"), status));
			}
		} catch (Exception e) {
			Res.logExceptionTrace(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
}
