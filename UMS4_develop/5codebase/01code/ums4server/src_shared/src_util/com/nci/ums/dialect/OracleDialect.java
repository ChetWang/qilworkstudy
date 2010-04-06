package com.nci.ums.dialect;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.nci.ums.util.Res;
import com.nci.ums.v3.message.basic.BasicMsg;

import oracle.sql.BLOB;

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
		return "SELECT * FROM UMS_SEND_READY WHERE STATUSFLAG = 0 AND TIMESETFLAG<>1 AND DOCOUNT<4 AND MEDIAID = ? AND ROWNUM<=16 ORDER BY PRIORITY DESC,BATCHNO,SERIALNO,SEQUENCENO";
	}

//	public synchronized String getAppServerMsgProcessSQL_V2() {
//		return "select * from in_ready where statusFlag = 0 and rownum<=15";
//	}

	public synchronized String getCacheData_NextMsgSQL_V2() {
		return "select * from in_ready where rownum<=30 and appID='";
	}

	public String getInMsgReceiveSQL_V3(List services) {
		StringBuffer sbf = new StringBuffer();
		sbf
				.append("select * from(select BATCHNO,SERIALNO,SEQUENCENO,RETCODE,ERRMSG,STATUSFLAG,SERVICEID,APPSERIALNO,MEDIAID,SENDID,RECVID,SUBMITDATE,SUBMITTIME,FINISHDATE,FINISHTIME,CONTENTSUBJECT,CONTENT,MSGID,ACK,REPLYNO,DOCOUNT,MSGTYPE,SENDERIDTYPE,RECEIVERIDTYPE,ROW_NUMBER() over(PARTITION by serviceid order by batchno desc) top5 from UMS_RECEIVE_READY)where top5<=5 and serviceid in(");
		for (int i = 0; i < services.size() - 1; i++) {
			sbf.append("'").append(services.get(i)).append("',");
		}
		sbf.append("'").append(services.get(services.size() - 1)).append("')");
		return sbf.toString();
	}

	public String getChartShowCountsOutSQL_V3() {
		return "SELECT COUNT(*) FROM (SELECT OUT_OK.BATCHNO FROM OUT_OK WHERE OUT_OK.MEDIAID=? AND (OUT_OK.FINISHDATE=? AND OUT_OK.FINISHTIME>=? AND OUT_OK.FINISHTIME<?) UNION ALL (SELECT UMS_SEND_OK.BATCHNO FROM UMS_SEND_OK WHERE UMS_SEND_OK.MEDIAID=? AND (UMS_SEND_OK.FINISHDATE=? AND UMS_SEND_OK.FINISHTIME>=? AND UMS_SEND_OK.FINISHTIME<?)))";
	}

	public String getChartShowCountsInSQL_V3() {
		return "SELECT COUNT(*) FROM (SELECT IN_OK.BATCHNO FROM IN_OK WHERE IN_OK.MEDIAID=? AND (IN_OK.FINISHDATE=? AND IN_OK.FINISHTIME>=? AND IN_OK.FINISHTIME<?) UNION ALL (SELECT UMS_RECEIVE_OK.BATCHNO FROM UMS_RECEIVE_OK WHERE UMS_RECEIVE_OK.MEDIAID=? AND (UMS_RECEIVE_OK.FINISHDATE=? AND UMS_RECEIVE_OK.FINISHTIME>=? AND UMS_RECEIVE_OK.FINISHTIME<?)))";
	}

	public String getAckMsgSQL_V3() {
		return "SELECT UMS_SEND_OK.BATCHNO,UMS_SEND_OK.SERIALNO,UMS_SEND_OK.SEQUENCENO,UMS_SEND_OK.ERRMSG, UMS_SEND_OK.SERVICEID, UMS_SEND_OK.SEQKEY FROM UMS_SEND_OK WHERE ROWNUM<=100 AND UMS_SEND_OK.ACK="
				+ BasicMsg.UMSMsg_ACK_SUCCESS
				+ " ORDER BY UMS_SEND_OK.SERVICEID";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.ums.dialect.UMSDialect#setBlob(java.sql.Connection,
	 * java.lang.String, java.lang.String, byte[])
	 */
	public boolean setBlob(Connection conn, String tableName, String keyField,
			String id, String field, byte[] content) {
		StringBuffer sqlEmptyBlob = new StringBuffer();
		sqlEmptyBlob.append("UPDATE ").append(tableName).append(" SET ")
				.append(field).append("=EMPTY_BLOB()").append(" WHERE ")
				.append(keyField).append("='").append(id).append("'");
		StringBuffer sqlSelectBlob = new StringBuffer();
		sqlSelectBlob.append("SELECT ").append(field).append(" FROM ").append(
				tableName).append(" WHERE ").append(keyField).append("='")
				.append(id).append("'");
		try {
			// 为指定字段建立空的BLOB字段
			PreparedStatement presm = conn.prepareStatement(sqlEmptyBlob
					.toString());
			presm.executeUpdate();
			presm.clearParameters();
			if (presm != null)
				presm.close();

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlSelectBlob.toString());

			// 首先获取blob对象
			while (rs.next()) {
				BLOB blob = (BLOB) rs.getBlob(field);
				OutputStream out = blob.getBinaryOutputStream(); // 建立输出流
				out.write(content);
				out.close();
			}
			// 关闭数据集以提交
			if (rs != null)
				rs.close();
		} catch (Exception ex) {
			Res.logExceptionTrace(ex);
			return false;
		}
		return true;
	}

	public String getOptimizeTableSQLFileName(String tableName) {

		return tableName + "_Oracle.sql";
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
		if(table.equalsIgnoreCase("UMS_TRANSMIT_ERR")){
			constraint = "PK_UMS_TRANSMIT_ERR";
		}else if(table.equalsIgnoreCase("UMS_TRANSMIT_MES")){
			constraint = "PK_UMS_TRANSMIT_MES";
		}
		sqls[0] = "ALTER TABLE " + table + " DROP CONSTRAINT "
				+ constraint;

		sqls[1] = "ALTER TABLE " + table + " RENAME TO " + table + suffix;
		return sqls;
	}
}
