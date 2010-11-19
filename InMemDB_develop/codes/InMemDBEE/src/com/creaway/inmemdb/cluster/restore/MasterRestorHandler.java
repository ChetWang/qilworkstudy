/*
 * @(#)MasterRestorHandler.java	1.0  09/07/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster.restore;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

import com.creaway.inmemdb.InMemSession;
import com.creaway.inmemdb.cluster.ClusterIfc;
import com.creaway.inmemdb.cluster.CommitRollbackNote;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.table.TableSupportIfc;
import com.creaway.inmemdb.util.DBLogger;

/**
 * 集群Master主服务器收到从属服务器数据恢复
 * 
 * @author Qil.Wong
 * 
 */
public class MasterRestorHandler {

	private ClusterIfc cluster;

	public MasterRestorHandler(ClusterIfc cluster) {
		this.cluster = cluster;
	}

	public void sendRestoreData(MasterRestoreNotification masterNote) {
		// InMemDBServer.getInstance().getThreadPool()
		// .execute(new MasterRestoreThread(masterNote));
		//
		// new Thread(new MasterRestoreThread(masterNote) ).start();
		new MasterRestoreThread(masterNote).run();
	}

	private class MasterRestoreThread implements Runnable {

		MasterRestoreNotification masterNote;

		private MasterRestoreThread(MasterRestoreNotification masterNote) {
			this.masterNote = masterNote;
		}

		private void send(Serializable o, String tableName) {
			cluster.sendDataToMembers(o, masterNote.getSlaveAddress());
			DBLogger.log(DBLogger.DEBUG, "发送恢复数据：table=" + tableName);
		}

		@Override
		public void run() {
			DBLogger.log(DBLogger.INFO, "收到集群数据恢复通知, Slave Address="
					+ masterNote.getSlaveAddress());
			// 从所有表中获取数据
			TableSupportIfc dbTableSupport = (TableSupportIfc) InMemDBServer
					.getInstance().getModule(TableSupportIfc.class);
			Map<String, String[]> sutructure = dbTableSupport
					.getTableStructure();
			Set<String> tables = sutructure.keySet();
			// 同步事务，需要统一session id
			InMemSession masterSession = InMemDBServer.getInstance()
					.createDataBaseSession();
			Connection conn = masterSession.createConnection();
			CommitRollbackNote cr = new CommitRollbackNote();
			cr.setSessionNo(masterSession.getId());

			boolean hasData = false;
			try {
				// conn.setAutoCommit(false);

				for (String tableName : tables) {
					String sql = "SELECT * FROM " + tableName;
					String[] columns = sutructure.get(tableName);

					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					int x = 0;
					RestoreNote[] temp = new RestoreNote[50];
					while (rs.next()) {
						Object[] row = new Object[columns.length];
						for (int i = 0; i < columns.length; i++) {
							row[i] = rs.getObject(i + 1);
						}
						hasData = true;

						// 发送数据是线程不安全的，因此restorenote和message必须每次都要新的对象
						RestoreNote rn = new RestoreNote();
						rn.setSlaveAddress(masterNote.getSlaveAddress());
						rn.setMasterSessionId(masterSession.getId());
						rn.setRows(row);
						rn.setTableName(tableName);
						temp[x] = rn;
						x++;
						if (x % temp.length == 0) {
							send(temp, tableName);
							// 一次发送后需要清空temp
							temp = new RestoreNote[temp.length];
							x = 0;
						}

					}
					// 最后一批
					if (temp[0] != null) {
						send(temp, tableName);
					}
					st.close();
				}
				// conn.commit();
				cr.setType(CommitRollbackNote.COMMIT);
			} catch (SQLException e) {
				DBLogger.log(DBLogger.ERROR,
						"Master " + cluster.getCurrentClusterAddress()
								+ "给从属数据库" + masterNote.getSlaveAddress()
								+ "进行数据恢复出错", e);
				cr.setType(CommitRollbackNote.ROLLBACK);
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
				}
				masterSession.close();
				// 发送最终的commit/rollback
				if (hasData) {
					DBLogger.log(DBLogger.INFO, "发送恢复commit/rollback  type="
							+ cr.getType());
					cluster.sendDataToMembers(cr, masterNote.getSlaveAddress());
				}
			}
		}
	}

}
