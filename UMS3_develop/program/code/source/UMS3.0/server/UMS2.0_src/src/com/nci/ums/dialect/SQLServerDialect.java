package com.nci.ums.dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.nci.ums.v3.message.basic.BasicMsg;

public class SQLServerDialect implements UMSDialect {

	public synchronized String getScanInvalid_scanReplySQL(ResultSet rs)
			throws SQLException {
		StringBuffer sql = new StringBuffer(
				"select top 1000 * from in_ready where appid ='").append(
				rs.getString("appid")).append("' order by batchno desc");
		return sql.toString();
	}

	public synchronized String getInchannel_replyFlagWithMsgIDSQL() {
		return "select top 1 * from out_reply where msgID=? order by batchNO desc ";
	}

	public synchronized String getInchannel_replyFlagWithoutMsgIDSQL() {
		return "select top 1 * from out_reply where replyDes=? order by batchNO desc ";
	}

	public synchronized String getOutMsgSelectSQL_V2() {
		return "select top 16 * from out_ready where statusFlag=0 and timesetflag=0 and docount<4 nand mediaID=? order by serialNO";
	}

	public synchronized String getOutMsgSelectSQL_V3() {
		return "SELECT TOP 16 * FROM OUT_READY_V3 WHERE STATUSFLAG = 0 AND TIMESETFLAG<>1 AND DOCOUNT<4 AND MEDIAID = ? ORDER BY PRIORITY DESC,BATCHNO,SERIALNO,SEQUENCENO";
	}

	public synchronized String getAppServerMsgProcessSQL_V2() {
		return "select top 15 * from in_ready where statusFlag = 0";
	}

	public synchronized String getCacheData_NextMsgSQL_V2() {
		return "select top 30 * from in_ready where appID='";
	}

	public String getInMsgReceiveSQL_V3(List services) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < services.size() - 1; i++) {
			sb.append("(SELECT TOP 5 * FROM IN_READY_V3 WHERE SERVICEID='");
			sb.append((String) services.get(i));
			sb.append("')");
			sb.append(" UNION ALL ");
		}
		sb.append("(SELECT TOP 5 * FROM IN_READY_V3 WHERE SERVICEID='").append(
				(String) services.get(services.size() - 1)).append("')");
		return sb.toString();
	}

	public String getChartShowCountsOutSQL_V3() {
		return "SELECT COUNT(*) FROM (SELECT OUT_OK.BATCHNO FROM OUT_OK WHERE OUT_OK.MEDIAID=? AND (OUT_OK.FINISHDATE=? AND OUT_OK.FINISHTIME>=? AND OUT_OK.FINISHTIME<?) UNION ALL (SELECT OUT_OK_V3.BATCHNO FROM OUT_OK_V3 WHERE OUT_OK_V3.MEDIAID=? AND (OUT_OK_V3.FINISHDATE=? AND OUT_OK_V3.FINISHTIME>=? AND OUT_OK_V3.FINISHTIME<?)))";
	}

	public String getChartShowCountsInSQL_V3() {
		return "SELECT COUNT(*) FROM (SELECT IN_OK.BATCHNO FROM IN_OK WHERE IN_OK.MEDIAID=? AND (IN_OK.FINISHDATE=? AND IN_OK.FINISHTIME>=? AND IN_OK.FINISHTIME<?) UNION ALL (SELECT IN_OK_V3.BATCHNO FROM IN_OK_V3 WHERE IN_OK_V3.MEDIAID=? AND (IN_OK_V3.FINISHDATE=? AND IN_OK_V3.FINISHTIME>=? AND IN_OK_V3.FINISHTIME<?)))";
	}

	public String getAckMsgSQL_V3() {
		return "SELECT TOP 100 OUT_OK_V3.BATCHNO,OUT_OK_V3.SERIALNO,OUT_OK_V3.SEQUENCENO, OUT_OK_V3.ERRMSG, OUT_OK_V3.SERVICEID, OUT_OK_V3.SEQKEY FROM OUT_OK_V3 WHERE ACK="
				+ BasicMsg.UMSMsg_ACK_SUCCESS
				+ " ORDER BY OUT_OK_V3.SERVICEID";
	}
}
