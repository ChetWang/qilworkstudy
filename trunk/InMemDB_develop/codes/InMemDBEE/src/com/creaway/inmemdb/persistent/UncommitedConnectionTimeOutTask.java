/*
 * @(#)UncommitedConnectionTimeOutTask.java	1.0  09/08/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.persistent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimerTask;

import com.creaway.inmemdb.util.ConnectionManager;
import com.creaway.inmemdb.util.DBLogger;

/**
 * 持久层未提交连接时效性监听处理对象
 * @author Qil.Wong
 *
 */
public class UncommitedConnectionTimeOutTask extends TimerTask {

	/**
	 * 从属连接序号
	 */
	private int masterSessionId;

	private PersistentBackupRestoreIfc backupRestore;

	public UncommitedConnectionTimeOutTask(
			PersistentBackupRestoreIfc backupRestore, int masterSessionNO) {
		this.backupRestore = backupRestore;
		this.masterSessionId = masterSessionNO;
	}

	@Override
	public void run() {
		// 时效性已过，释放连接，并从缓存中移除
		DBLogger.log(DBLogger.WARN, "时效性已过，释放连接，并从缓存中移除  uncommitedConn = "
				+ masterSessionId);
		Connection conn = backupRestore.getUncommitedConnections().get(
				masterSessionId);
		try {
			conn.rollback();
		} catch (SQLException e) {
		}
		ConnectionManager.releaseConnection(conn);
		backupRestore.getUncommitedConnections().remove(masterSessionId);
	}

	public int getMasterSessionId() {
		return masterSessionId;
	}

	public void setMasterSessionNO(int masterSessionId) {
		this.masterSessionId = masterSessionId;
	}

}
