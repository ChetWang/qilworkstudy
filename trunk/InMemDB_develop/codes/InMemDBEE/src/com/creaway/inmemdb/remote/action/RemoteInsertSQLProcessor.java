/*
 * @(#)RemoteInsertSQLProcessor.java	1.0  09/20/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.remote.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SelectionKey;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.creaway.inmemdb.util.ConnectionManager;
import com.creaway.inmemdb.util.DBLogger;
import com.creaway.inmemdb.util.ProtocolUtils;

/**
 * 远程Insert操作处理器，暂不支持实务操作
 * 
 * @author Qil.Wong
 * 
 */
public class RemoteInsertSQLProcessor extends Processor<Boolean> {

	/**
	 * 插入操作成功标记
	 */
	public static final int INSERT_OK = 1;

	/**
	 * 插入操作失败标记
	 */
	public static final int INSERT_FAILED = 2;

	@Override
	protected Boolean process(int serialNO, int sessionID, byte[] data,
			SelectionKey key) {
		Connection conn = ConnectionManager.getConnection();
		try {
			String sql = new String(data, "utf-8");
			Statement st = conn.createStatement();
			boolean result = st.execute(sql);
			conn.commit();
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "远程查询错误！seialNO = " + serialNO
					+ ", SessionID = " + sessionID + ", " + key.channel(), e);
		} finally {
			ConnectionManager.releaseConnection(conn);
		}
		return null;
	}

	@Override
	protected void done(SelectionKey key, Boolean result, int serialNO,
			int sessionID) {
		try {
			sendResult(false, key,
					ProtocolUtils.getDataLength(result ? INSERT_OK
							: INSERT_FAILED), serialNO, sessionID);
		} catch (IOException e) {
			key.cancel();
			DBLogger.log(DBLogger.ERROR, "", e);
		}

	}

}
