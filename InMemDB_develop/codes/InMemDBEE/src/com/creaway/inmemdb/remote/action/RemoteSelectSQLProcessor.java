/*
 * @(#)RemoteSelectSQLProcessor.java	1.0  09/20/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.remote.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SelectionKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.WebRowSet;

import com.creaway.inmemdb.api.InMemDBReadOnlyRowSet;
import com.creaway.inmemdb.util.ConnectionManager;
import com.creaway.inmemdb.util.DBLogger;

/**
 * 远程SELECT查询命令处理器
 * @author Qil.Wong
 *
 */
public class RemoteSelectSQLProcessor extends Processor<WebRowSet> {

	@Override
	protected WebRowSet process(int serialNO, int sessionID, byte[] data,
			SelectionKey key) {
		Connection conn = ConnectionManager.getConnection();
		try {
			String sql = new String(data, "utf-8");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			InMemDBReadOnlyRowSet rowSet = new InMemDBReadOnlyRowSet();
			rowSet.populate(rs);
			rs.close();
			st.close();
			return rowSet;
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
	protected void done(SelectionKey key, WebRowSet result, int serialNO,
			int sessionID) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			result.writeXml(bos);
			byte[] b = bos.toByteArray();
			bos.close();
			sendResult(false,key, b, serialNO, sessionID);
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "", e);
		} catch (IOException e) {
			key.cancel();
			DBLogger.log(DBLogger.ERROR, "", e);
		}
	}

}
