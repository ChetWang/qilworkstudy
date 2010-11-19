/*
 * @(#)ClusterCommitRollbackHandler.java	1.0  09/02/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.TimerTask;

import com.creaway.inmemdb.util.DBLogger;

/**
 * 集群数据库连接的Commit/Rollback处理对象
 * 
 * @author Qil.Wong
 * 
 */
public class ClusterCommitRollbackHandler {

	/**
	 * 集群处理模块对象
	 */
	private ClusterIfc cluster;

	public ClusterCommitRollbackHandler(ClusterIfc cluster) {
		this.cluster = cluster;
	}

	/**
	 * 处理Commit/Rollback
	 * 
	 * @param cn
	 *            集群通知
	 */
	public void handleCommitRollback(CommitRollbackNote cn) {
		// DBLogger.log(DBLogger.INFO,
		// "收到集群CommitRollbackNote通知." + cn.toString());
		// InMemDBServer.getInstance().getThreadPool()
		// .execute(new CommitRollbackRunnable(cn));
		new CommitRollbackRunnable(cn).run();
	}

	private class CommitRollbackRunnable implements Runnable {

		private CommitRollbackNote cn;

		private CommitRollbackRunnable(CommitRollbackNote cn) {
			this.cn = cn;
		}

		@Override
		public void run() {

			Connection conn = cluster.getSlaveConnections().get(
					cn.getSessionNo());

			if (conn != null) { // 正常情况下，在commit或rollback前，已经有相应的增删改查操作，同一事务的Connection已经产生
				try {
					switch (cn.getType()) {
					case CommitRollbackNote.COMMIT:
						conn.commit();
						break;
					case CommitRollbackNote.ROLLBACK:
						conn.rollback();
						break;
					default:
						break;
					}
				} catch (SQLException e) {
					DBLogger.log(DBLogger.ERROR, "集群从属连接commit/rollback操作失败", e);
				} finally {
					//释放从属连接的同时，事务已经完成，各种从属连接、定时器等关系都需要移除
					SlaveConnectionFactory.releaseSlaveConnection(conn);
					cluster.getSlaveConnections().remove(cn.getSessionNo());
					Map<Integer, SlaveConnectionTimeOutTask> tasks = cluster
							.getSlaveConnectionTimeOutTasks();
					TimerTask currentTimeOutTask = tasks.get(cn.getSessionNo());
					if (currentTimeOutTask != null) {
						tasks.remove(cn.getSessionNo());
						currentTimeOutTask.cancel();
					} else {
						DBLogger.log(
								DBLogger.WARN,
								"无法获取集群从属连接对应的实效定时器，Master NO.="
										+ cn.getSessionNo());
					}
					DBLogger.log(
							DBLogger.DEBUG,
							"事务commit/rollback! type=" + cn.getType()
									+ "  Master session NO.="
									+ cn.getSessionNo());
				}
			} else {
				DBLogger.log(DBLogger.WARN,
						"无法获取集群从属连接,主Connection NO.=" + cn.getSessionNo());
			}

		}

	}

}
