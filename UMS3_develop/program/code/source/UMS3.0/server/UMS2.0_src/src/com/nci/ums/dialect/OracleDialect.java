package com.nci.ums.dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.nci.ums.v3.message.basic.BasicMsg;

public class OracleDialect implements UMSDialect {

	public synchronized String getScanInvalid_scanReplySQL(ResultSet rs)
			throws SQLException {
		StringBuffer sql = new StringBuffer(
				"select * from in_ready where appid='").append(
				rs.getString("appid")).append(
				"' and rownum<=1000 order by batchno desc");
		return sql.toString();
	}

	public synchronized String getInchannel_replyFlagWithMsgIDSQL() {
		return "select * from out_reply where msgID=? and rownum<2 order by batchNO desc ";
	}

	public synchronized String getInchannel_replyFlagWithoutMsgIDSQL() {
		return "select * from out_reply where replyDes=? and rownum<2 order by batchNO desc";
	}

	public synchronized String getOutMsgSelectSQL_V2() {
		return "select * from out_ready where statusFlag=0 and timesetflag=0 and docount<4 and mediaID=? and  rownum<=16 order by serialNO";
	}

	public synchronized String getOutMsgSelectSQL_V3() {
		return "SELECT * FROM OUT_READY_V3 WHERE STATUSFLAG = 0 AND TIMESETFLAG<>1 AND DOCOUNT<4 AND MEDIAID = ? AND ROWNUM<=16 ORDER BY PRIORITY DESC,BATCHNO,SERIALNO,SEQUENCENO";
	}

	public synchronized String getAppServerMsgProcessSQL_V2() {
		return "select * from in_ready where statusFlag = 0 and rownum<=15";
	}

	public synchronized String getCacheData_NextMsgSQL_V2() {
		return "select * from in_ready where rownum<=30 and appID='";
	}

	public String getInMsgReceiveSQL_V3(List services) {
		StringBuffer sbf = new StringBuffer();
		sbf
				.append("select * from(select BATCHNO,SERIALNO,SEQUENCENO,RETCODE,ERRMSG,STATUSFLAG,SERVICEID,APPSERIALNO,MEDIAID,SENDID,RECVID,SUBMITDATE,SUBMITTIME,FINISHDATE,FINISHTIME,CONTENTSUBJECT,CONTENT,MSGID,ACK,REPLYNO,DOCOUNT,MSGTYPE,SENDERIDTYPE,RECEIVERIDTYPE,ROW_NUMBER() over(PARTITION by serviceid order by batchno desc) top5 from in_ready_v3)where top5<=5 and serviceid in(");
		for (int i = 0; i < services.size() - 1; i++) {
			sbf.append("'").append(services.get(i)).append("',");
		}
		sbf.append("'").append(services.get(services.size() - 1)).append("')");
		return sbf.toString();
	}

	public String getChartShowCountsOutSQL_V3() {
		return "SELECT COUNT(*) FROM (SELECT OUT_OK.BATCHNO FROM OUT_OK WHERE OUT_OK.MEDIAID=? AND (OUT_OK.FINISHDATE=? AND OUT_OK.FINISHTIME>=? AND OUT_OK.FINISHTIME<?) UNION ALL (SELECT OUT_OK_V3.BATCHNO FROM OUT_OK_V3 WHERE OUT_OK_V3.MEDIAID=? AND (OUT_OK_V3.FINISHDATE=? AND OUT_OK_V3.FINISHTIME>=? AND OUT_OK_V3.FINISHTIME<?)))";
	}

	public String getChartShowCountsInSQL_V3() {
		return "SELECT COUNT(*) FROM (SELECT IN_OK.BATCHNO FROM IN_OK WHERE IN_OK.MEDIAID=? AND (IN_OK.FINISHDATE=? AND IN_OK.FINISHTIME>=? AND IN_OK.FINISHTIME<?) UNION ALL (SELECT IN_OK_V3.BATCHNO FROM IN_OK_V3 WHERE IN_OK_V3.MEDIAID=? AND (IN_OK_V3.FINISHDATE=? AND IN_OK_V3.FINISHTIME>=? AND IN_OK_V3.FINISHTIME<?)))";
	}

	public String getAckMsgSQL_V3() {
		return "SELECT OUT_OK_V3.BATCHNO,OUT_OK_V3.SERIALNO,OUT_OK_V3.SEQUENCENO,OUT_OK_V3.ERRMSG, OUT_OK_V3.SERVICEID, OUT_OK_V3.SEQKEY FROM OUT_OK_V3 WHERE ROWNUM<=100 AND OUT_OK_V3.ACK="
				+ BasicMsg.UMSMsg_ACK_SUCCESS
				+ " ORDER BY OUT_OK_V3.SERVICEID";
	}
}
