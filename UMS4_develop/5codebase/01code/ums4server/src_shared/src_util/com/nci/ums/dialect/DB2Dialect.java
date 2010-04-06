package com.nci.ums.dialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.nci.ums.v3.message.basic.BasicMsg;

/**
 * IBM DB2数据库方言类
 * @author Qil.Wong
 *
 */
public class DB2Dialect implements UMSDialect {

	public String getAckMsgSQL_V3() {
		return "SELECT UMS_SEND_OK.BATCHNO,UMS_SEND_OK.SERIALNO,UMS_SEND_OK.SEQUENCENO,UMS_SEND_OK.ERRMSG, UMS_SEND_OK.SERVICEID, UMS_SEND_OK.SEQKEY FROM UMS_SEND_OK WHERE UMS_SEND_OK.ACK="
				+ BasicMsg.UMSMsg_ACK_SUCCESS
				+ " ORDER BY UMS_SEND_OK.SERVICEID FETCH FIRST 100 ROWS ONLY";
	}

	public String getCacheData_NextMsgSQL_V2() {
		return null;

	}

	public String getChartShowCountsInSQL_V3() {
		return "SELECT COUNT(*) FROM (SELECT IN_OK.BATCHNO FROM IN_OK WHERE IN_OK.MEDIAID=? AND (IN_OK.FINISHDATE=? AND IN_OK.FINISHTIME>=? AND IN_OK.FINISHTIME<?) UNION ALL (SELECT UMS_RECEIVE_OK.BATCHNO FROM UMS_RECEIVE_OK WHERE UMS_RECEIVE_OK.MEDIAID=? AND (UMS_RECEIVE_OK.FINISHDATE=? AND UMS_RECEIVE_OK.FINISHTIME>=? AND UMS_RECEIVE_OK.FINISHTIME<?)))";
	}

	public String getChartShowCountsOutSQL_V3() {
		return "SELECT COUNT(*) FROM (SELECT OUT_OK.BATCHNO FROM OUT_OK WHERE OUT_OK.MEDIAID=? AND (OUT_OK.FINISHDATE=? AND OUT_OK.FINISHTIME>=? AND OUT_OK.FINISHTIME<?) UNION ALL (SELECT UMS_SEND_OK.BATCHNO FROM UMS_SEND_OK WHERE UMS_SEND_OK.MEDIAID=? AND (UMS_SEND_OK.FINISHDATE=? AND UMS_SEND_OK.FINISHTIME>=? AND UMS_SEND_OK.FINISHTIME<?)))";
	}

	public String getInMsgReceiveSQL_V3(List services) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < services.size() - 1; i++) {
			sb.append("(SELECT * FROM UMS_RECEIVE_READY WHERE SERVICEID='");
			sb.append((String) services.get(i));
			sb.append("' FETCH FIRST 5 ROWS ONLY )");
			sb.append(" UNION ALL ");
		}
		sb.append("(SELECT * FROM UMS_RECEIVE_READY WHERE SERVICEID='").append(
				(String) services.get(services.size() - 1)).append(
				"' FETCH FIRST 5 ROWS ONLY)");
		return sb.toString();
	}

	public String getInchannel_replyFlagWithMsgIDSQL() {
		return "select *select * from out_reply where msgID=? order by batchNO desc fetch first 1 rows only";
	}

	public String getInchannel_replyFlagWithoutMsgIDSQL() {
		return "select * from out_reply where replyDes=? order by batchNO desc fetch first 1 rows only";
	}

	public String getOptimizeTableSQLFileName(String tableName) {
		return tableName + "_DB2.sql";
	}

	public String getOutMsgSelectSQL_V2() {
		return "select * from out_ready where statusFlag=0 and timesetflag=0 and docount<4 and mediaID=? and  order by serialNO fetch first 16 rows only";
	}

	public String getOutMsgSelectSQL_V3() {
		return "SELECT * FROM UMS_SEND_READY WHERE STATUSFLAG = 0 AND TIMESETFLAG<>1 AND DOCOUNT<4 AND MEDIAID = ? ORDER BY PRIORITY DESC,BATCHNO,SERIALNO,SEQUENCENO FETCH FIRST 16 ROWS ONLY";
	}

	public String[] getRenameSEND_OKTableSQL(String table, String suffix) {
		String[] sqls = new String[2];

		sqls[0] = "ALTER TABLE " + table + " DROP CONSTRAINT "
				+ "PK_UMS_SEND_OK";

		sqls[1] = "ALTER TABLE " + table + " RENAME TO " + table + suffix;
		return sqls;
	}

	public String[] getRenameTrantmitTableSQL(String table, String suffix) {
		String[] sqls = new String[2];

		String constraint = "";
		if (table.equalsIgnoreCase("UMS_TRANSMIT_ERR")) {
			constraint = "PK_UMS_TRANSMIT_ERR";
		} else if (table.equalsIgnoreCase("UMS_TRANSMIT_MES")) {
			constraint = "PK_UMS_TRANSMIT_MES";
		}
		sqls[0] = "ALTER TABLE " + table + " DROP CONSTRAINT " + constraint;

		sqls[1] = "ALTER TABLE " + table + " RENAME TO " + table + suffix;
		return sqls;
	}

	public String getScanInvalid_scanReplySQL(ResultSet rs) throws SQLException {
		StringBuffer sql = new StringBuffer(
				"select * from in_ready where appid='").append(
				rs.getString("appid")).append(
				"' order by batchno desc fetch first 1000 rows only ");
		return sql.toString();

	}

	public boolean setBlob(Connection conn, String tableName, String keyField,
			String id, String field, byte[] content) {

		return false;
	}

}
