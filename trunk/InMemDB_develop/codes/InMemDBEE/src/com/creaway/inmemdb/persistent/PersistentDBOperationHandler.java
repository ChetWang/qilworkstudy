/*
 * @(#)ClusterDBOperationHandler.java	1.0  09/02/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.persistent;

import java.sql.Connection;
import java.sql.SQLException;

import com.creaway.inmemdb.cluster.DBOperationNote;
import com.creaway.inmemdb.trigger.InMemDBTrigger;
import com.creaway.inmemdb.util.DBLogger;

/**
 * 集群操作下，从属连接的相应同步操作
 * 
 * @author Qil.Wong
 * 
 */
public class PersistentDBOperationHandler extends PersistentTransactionHandler {

	public PersistentDBOperationHandler(PersistentBackupRestoreIfc backupRestore) {
		super(backupRestore);
	}

	/**
	 * 处理数据库消息
	 * 
	 * @param operNote
	 */
	public void handleDBOperations(DBOperationNote operNote) {
		// DBLogger.log(DBLogger.INFO,
		// "处理集群hanleDBOperations,TABLE = " + operNote.getTableName()
		// + ". Master session NO=" + operNote.getSessionNO());
		// InMemDBServer.getInstance().getThreadPool()
		// .execute(new ClusterDBOperRunnable(operNote));
		new PersistentDBOperRunnable(operNote).run();
	}

	/**
	 * 持久化处理类，支持线程池
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class PersistentDBOperRunnable implements Runnable {

		private DBOperationNote operNote;

		private PersistentDBOperRunnable(DBOperationNote operNote) {
			this.operNote = operNote;
		}

		@Override
		public void run() {
			// 集群主数据库发起的连接序号
			try {
				Connection uncommitConn = getUncommitedConnection(
						operNote.getSessionNO(), false);
				switch (operNote.getType()) {
				case InMemDBTrigger.TYPE_INSERT:
					executeInsert(uncommitConn, operNote);
					break;
				case InMemDBTrigger.TYPE_DELETE:
					executeDelete(uncommitConn, operNote);
					break;
				case InMemDBTrigger.TYPE_UPDATE:
					executeUpdate(uncommitConn, operNote);
					break;
				// case InMemDBTrigger.TYPE_SELECT:
				// break;
				default:
					break;
				}
			} catch (SQLException e) {
				DBLogger.log(DBLogger.WARN,
						"持久化连接操作数据失败，操作类型：" + operNote.getType(), e);
			} finally {
				// 这里不用释放连接，释放连接应该在Commit/Rollback是进行
			}
		}

	}
}
