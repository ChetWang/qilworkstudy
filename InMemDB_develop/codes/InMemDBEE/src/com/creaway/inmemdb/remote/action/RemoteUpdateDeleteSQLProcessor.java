/*
 * @(#)RemoteUpdateDeleteSQLProcessor.java	1.0  09/20/2010
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
 * 远程更新/删除操作处理器，暂不支持实务操作
 * 
 * @author Qil.Wong
 * 
 */
public class RemoteUpdateDeleteSQLProcessor extends Processor<Integer> {

	@Override
	protected Integer process(int serialNO, int sessionID, byte[] data,
			SelectionKey key) {
		Connection conn = ConnectionManager.getConnection();
		try {
			String sql = new String(data, "utf-8");
			Statement st = conn.createStatement();
			int result = st.executeUpdate(sql);

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
	protected void done(SelectionKey key, Integer result, int serialNO,
			int sessionID) {
		try {
			sendResult(false,key, ProtocolUtils.getDataLength(result), serialNO,
					sessionID);
		} catch (IOException e) {
			key.cancel();
			DBLogger.log(DBLogger.ERROR, "", e);
		}
	}

}
