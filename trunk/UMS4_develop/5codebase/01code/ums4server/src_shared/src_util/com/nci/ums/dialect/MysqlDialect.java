package com.nci.ums.dialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.nci.ums.v3.message.basic.BasicMsg;

public class MysqlDialect implements UMSDialect {

	public synchronized String getScanInvalid_scanReplySQL(ResultSet rs)
			throws SQLException {
		StringBuffer sql = new StringBuffer(
				"select * from in_ready where appid='").append(
				rs.getString("appid")).append(
				"' order by batchno desc limit 1000");
		return sql.toString();
	}

	public synchronized String getInchannel_replyFlagWithMsgIDSQL() {
		return "select * from out_reply where msgID=? order by batchNO desc limit 1";
	}

	public synchronized String getInchannel_replyFlagWithoutMsgIDSQL() {
		return "select * from out_reply where replyDes=? order by batchNO desc limit 1";
	}

	public synchronized String getOutMsgSelectSQL_V2() {
		return "select * from out_ready where statusFlag=0 and timesetflag=0 and docount<4 and mediaID=? order by serialNO limit 16";
	}

	public synchronized String getOutMsgSelectSQL_V3() {
		return "SELECT * FROM UMS_SEND_READY WHERE STATUSFLAG = 0 AND TIMESETFLAG<>1 AND DOCOUNT<4 AND MEDIAID = ? ORDER BY PRIORITY DESC,BATCHNO,SERIALNO,SEQUENCENO limit 16";
	}

	public synchronized String getAppServerMsgProcessSQL_V2() {
		return "select * from in_ready where statusFlag = 0 limit 15";
	}

	public synchronized String getCacheData_NextMsgSQL_V2() {
		// FIXME limit用法改进
		return "select * from in_ready limit 30 and appID='";
	}

	public String getInMsgReceiveSQL_V3(List services) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < services.size() - 1; i++) {
			sb.append("(SELECT * FROM UMS_RECEIVE_READY WHERE SERVICEID='");
			sb.append((String) services.get(i));
			sb.append("' limit 5 )");
			sb.append(" UNION ALL ");
		}
		sb.append("(SELECT * FROM UMS_RECEIVE_READY WHERE SERVICEID='").append(
				(String) services.get(services.size() - 1))
				.append("' limit 5)");
		return sb.toString();

	}

	public String getChartShowCountsOutSQL_V3() {
		return "SELECT COUNT(*) AS co FROM (SELECT oo.BATCHNO AS ob FROM OUT_OK AS oo WHERE oo.MEDIAID=? AND (oo.FINISHDATE=? AND oo.FINISHTIME>=? AND oo.FINISHTIME<?) UNION ALL (SELECT oo3.BATCHNO AS ob3 FROM UMS_SEND_OK AS oo3 WHERE oo3.MEDIAID=? AND (oo3.FINISHDATE=? AND oo3.FINISHTIME>=? AND oo3.FINISHTIME<?))) AS t";
	}

	public String getChartShowCountsInSQL_V3() {
		return "SELECT COUNT(*) AS co FROM (SELECT io.BATCHNO AS ib FROM IN_OK AS io WHERE io.MEDIAID=? AND (io.FINISHDATE=? AND io.FINISHTIME>=? AND io.FINISHTIME<?) UNION ALL (SELECT io3.BATCHNO AS ib3 FROM UMS_RECEIVE_OK AS io3 WHERE io3.MEDIAID=? AND (io3.FINISHDATE=? AND io3.FINISHTIME>=? AND io3.FINISHTIME<?))) AS t";
	}

	public String getAckMsgSQL_V3() {
		return "SELECT UMS_SEND_OK.BATCHNO,UMS_SEND_OK.SERIALNO,UMS_SEND_OK.SEQUENCENO, UMS_SEND_OK.ERRMSG, UMS_SEND_OK.SERVICEID, UMS_SEND_OK.SEQKEY FROM UMS_SEND_OK WHERE UMS_SEND_OK.ACK="+BasicMsg.UMSMsg_ACK_SUCCESS+" ORDER BY UMS_SEND_OK.SERVICEID limit 100";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.ums.dialect.UMSDialect#setBlob(java.sql.Connection,
	 *      java.lang.String, java.lang.String, byte[])
	 */
	public boolean setBlob(Connection conn, String tableName, String keyField,
			String id, String field, byte[] content) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getOptimizeTableSQLFileName(String tableName) {
		// TODO Auto-generated method stub
		return tableName+"_MySQL.sql";
	}

	public String[] getRenameSEND_OKTableSQL(String table,String suffix) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getRenameTrantmitTableSQL(String table, String suffix) {
		// TODO Auto-generated method stub
		return null;
	}

}
