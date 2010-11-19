/*
 * @(#)PersistentTransactionHandler.java	1.0  09/13/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.persistent;

import java.sql.Connection;
import java.sql.SQLException;

import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.core.TransactionHandler;
import com.creaway.inmemdb.util.ConnectionManager;

/**
 * 持久层事务处理器
 * 
 * @author Qil.Wong
 * 
 */
public class PersistentTransactionHandler extends TransactionHandler {

	protected PersistentBackupRestoreIfc backupRestore;

	public PersistentTransactionHandler(PersistentBackupRestoreIfc backupRestore) {
		this.backupRestore = backupRestore;
	}

	/**
	 * 获取缓存的slave 数据库连接
	 * 
	 * @param masterSessionNO
	 *            master主服务当前事务下对应的session id
	 * @param autoTimeout
	 *            是否自动设置timeout
	 * @return
	 * @throws SQLException
	 */
	public Connection getUncommitedConnection(int masterSessionNO,
			boolean autoTimeout) throws SQLException {

		// 获取从属连接，如果为空则是第一次事务开始；当然，这也有可能是超时后的数据，这里不考虑。
		// 如果是超时数据，则需要适度把握超时时间设置！！！！！
		Connection uncommitConn = backupRestore.getUncommitedConnections().get(
				masterSessionNO);
		if (uncommitConn == null) {
			// slaveConn = SlaveConnectionFactory.getSlaveConnection();
			uncommitConn = ConnectionManager.getPersistentConnection();
			backupRestore.getUncommitedConnections().put(masterSessionNO,
					uncommitConn);
			// 增加超时监听
			if (!autoTimeout) {
				backupRestore.addConnectionTimeOutTimerTask(
						new UncommitedConnectionTimeOutTask(backupRestore,
								masterSessionNO), Integer
								.parseInt(InMemDBServer.getInstance()
										.getSystemProperties()
										.get("persistence_conn_timeout")
										.toString()));
			}
		}
		return uncommitConn;
	}

}
