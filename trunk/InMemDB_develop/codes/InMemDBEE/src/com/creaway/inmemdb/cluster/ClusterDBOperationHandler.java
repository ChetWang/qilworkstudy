/*
 * @(#)ClusterDBOperationHandler.java	1.0  09/02/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster;

import java.sql.Connection;
import java.sql.SQLException;

import com.creaway.inmemdb.trigger.InMemDBTrigger;
import com.creaway.inmemdb.util.DBLogger;

/**
 * 集群操作下，从属连接的相应同步操作
 * 
 * @author Qil.Wong
 * 
 */
public class ClusterDBOperationHandler extends ClusterSlaveHandler {


	public ClusterDBOperationHandler(ClusterIfc cluster) {
		super(cluster);
	}

	public void hanleDBOperations(DBOperationNote operNote) {
		// DBLogger.log(DBLogger.INFO,
		// "处理集群hanleDBOperations,TABLE = " + operNote.getTableName()
		// + ". Master session NO=" + operNote.getSessionNO());
		// InMemDBServer.getInstance().getThreadPool()
		// .execute(new ClusterDBOperRunnable(operNote));
		new ClusterDBOperRunnable(operNote).run();
	}

	

	private class ClusterDBOperRunnable implements Runnable {

		private DBOperationNote operNote;

		private ClusterDBOperRunnable(DBOperationNote operNote) {
			this.operNote = operNote;
		}

		@Override
		public void run() {
			// 集群主数据库发起的连接序号
			try {
				Connection slaveConn = getSlaveConnection(
						operNote.getSessionNO(), false);
				switch (operNote.getType()) {
				case InMemDBTrigger.TYPE_INSERT:
					executeInsert(slaveConn, operNote);
					break;
				case InMemDBTrigger.TYPE_DELETE:
					executeDelete(slaveConn, operNote);
					break;
				case InMemDBTrigger.TYPE_UPDATE:
					executeUpdate(slaveConn, operNote);
					break;
//				case InMemDBTrigger.TYPE_SELECT:
//					break;
				}
			} catch (SQLException e) {
				DBLogger.log(DBLogger.WARN, "集群从属连接操作数据失败", e);
			} finally {
				// 这里不用释放连接，释放连接应该在Commit/Rollback是进行
			}
		}

	}
}
