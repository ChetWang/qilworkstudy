package com.nci.ums.transmit.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nci.ums.transmit.common.TransmitData;
import com.nci.ums.util.DialectUtil;
import com.nci.ums.util.Res;
import com.nci.ums.util.db.DBOptLock;

/**
 * <p>
 * 标题：RecordMessage.java
 * </p>
 * <p>
 * 描述： 转发平台数据库操作类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-8-26
 * @version 1.0
 */
public class RecordMessage {

	/**
	 * 保存转发的内容
	 * 
	 * @param data
	 * @return
	 */
	public boolean recordMessage(TransmitData data) {

		// FIXME just for test
		// if (data != null)
		// return true;
		// test end
		while (DBOptLock.dbLock == true) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		StringBuffer sqlBase = new StringBuffer();
		// 内容长度
		String contentLength = Integer.toString(data.getOneFrameData().length);
		if (data.isSuccess()) {
			// 保存基础信息
			sqlBase.append("INSERT INTO UMS_TRANSMIT_MES").append(
					" (seqkey, trans_source, trans_target, ").append(
					"trans_receive_time, trans_sent_time, trans_length) ")
					.append("VALUES ('").append(data.getUid()).append("', '")
					.append(data.getSourceAddress()).append("', '").append(
							data.getDestinationAddress()).append("', '")
					.append(data.getReceivedTime()).append("', '").append(
							data.getTransmitTime()).append("', '").append(
							contentLength).append("')");
		} else {
			sqlBase.append("INSERT INTO ").append("UMS_TRANSMIT_ERR").append(
					" (seqkey, trans_source, trans_target, ").append(
					"trans_receive_time, trans_sent_time, ").append(
					"trans_err_type, trans_length) VALUES ('").append(
					data.getUid()).append("', '").append(
					data.getSourceAddress()).append("', '").append(
					data.getDestinationAddress()).append("', '").append(
					data.getReceivedTime()).append("', '").append(
					data.getTransmitTime()).append("','").append(
					data.getErrorType()).append("')").append(contentLength);
		}
		boolean saveFlag = false;
		Connection conn = null;
		try {
			conn = Res.getConnection();
			conn.setAutoCommit(false);
			// 保存基础信息
			saveFlag = saveBaseInformation(conn, sqlBase.toString());
			// 保存报文内容
			if (saveFlag) {
				saveFlag = DialectUtil.getDialect().setBlob(conn,
						"ums_transmit_mes", "seqkey", data.getUid(),
						"trans_content", data.getOneFrameData());
			}

			conn.setAutoCommit(true);

			if (saveFlag) {
				conn.commit();
			} else {
				conn.rollback();
				return false;
			}
		} catch (SQLException e) {
			Res.logExceptionTrace(e);
			return false;
		} finally {
			closeConn(conn);
		}

		return true;
	}

	/**
	 * @功能 根据sql语句，保存基本信息
	 * @param conn
	 * @param sql
	 * @return
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	private boolean saveBaseInformation(Connection conn, String sql) {
		try {
			PreparedStatement presm = conn.prepareStatement(sql);
			presm.executeUpdate();
			presm.clearParameters();
			if (presm != null)
				presm.close();
			Res.log(Res.DEBUG, sql);
		} catch (SQLException e) {
			Res.logExceptionTrace(e);
			return false;
		}
		return true;
	}

	/**
	 * @功能 关闭数据库连接
	 * @param conn
	 *            Connection 数据库连接
	 * @return 是否关闭成功
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	private boolean closeConn(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			Res.logExceptionTrace(e);
			return false;
		}
		return true;
	}
}
