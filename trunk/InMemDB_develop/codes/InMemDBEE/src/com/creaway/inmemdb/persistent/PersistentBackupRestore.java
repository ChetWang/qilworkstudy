/*
 * @(#)PersistentBackupRestore.java	1.0  09/12/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.management.ObjectName;

import com.creaway.inmemdb.cluster.ClusterIfc;
import com.creaway.inmemdb.cluster.CommitRollbackNote;
import com.creaway.inmemdb.cluster.DBOperationNote;
import com.creaway.inmemdb.cluster.SlaveConnectionFactory;
import com.creaway.inmemdb.core.DBModule;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.core.InMemDBServerH2;
import com.creaway.inmemdb.table.MaxRowLimiter;
import com.creaway.inmemdb.table.TableSupportIfc;
import com.creaway.inmemdb.trigger.MaxRowCheckSignal;
import com.creaway.inmemdb.util.ConnectionManager;
import com.creaway.inmemdb.util.DBLogger;

/**
 * 持久层实时数据持久化实现类，用于实时数据的持久化，对应内存数据库的所有操作
 * 
 * @author Qil.Wong
 * 
 */
public class PersistentBackupRestore implements DBModule,
		PersistentBackupRestoreIfc {

	private Map<Integer, Connection> uncomitedConnections = new ConcurrentHashMap<Integer, Connection>();

	private Map<Integer, UncommitedConnectionTimeOutTask> timeoutTasks = new ConcurrentHashMap<Integer, UncommitedConnectionTimeOutTask>();

	/**
	 * 持久操作数据队列，可能是DBOperationNote,也可能是CommitRollbackNote，也可能是MaxRow检查信号，
	 * 但要放在同一个队列里，保证执行顺序
	 */
	private LinkedBlockingQueue dbOperationQueue = new LinkedBlockingQueue();

	private boolean flag = true;

	private int MAX_WAIT_QUEUE = 150000;

	private PersistentCommitRollbackHandler commitRollbackHandler;

	private PersistentDBOperationHandler dbOperationHandler;

	private Timer uncommitedConnTimer = new Timer(
			"PersistentUncommittedConnectionTimer", true);

	private MaxRowLimiter persistentMaxRowLimiter;

	/**
	 * 同步锁
	 */
	private Object backupRestoreLock = new Object();

	@Override
	public void startModule(Map<?, ?> params) {
		DBLogger.log(DBLogger.INFO, "启动持久层支持模块");

		commitRollbackHandler = new PersistentCommitRollbackHandler(this);

		dbOperationHandler = new PersistentDBOperationHandler(this);

		MAX_WAIT_QUEUE = Integer.parseInt(InMemDBServer.getInstance()
				.getSystemProperties().getProperty("max_backup_wait_queue"));

		// 在持久层创建对应表
		// just for test
		// rebuild();

		createPersistanceStructure(); // 测试时注释掉，生产环境必须使用

		flag = true;
		initPersistentTrigger();
		try {
			ObjectName on = new ObjectName(InMemDBServerH2.MBEAN_PREFIX
					+ "Persistent Operations");

			InMemDBServer.getInstance().registerMBean(this, on);
		} catch (Exception e) {
			DBLogger.log(DBLogger.ERROR, "", e);
		}
		new Thread(new BackUp(), "InmemDB Persistent Backup").start();
		persistentMaxRowLimiter = new PersistentMaxRowLimiter();

		persistentMaxRowLimiter.start();
	}

	public void initPersistentTrigger() {
		// 添加Persistent trigger
		TableSupportIfc tableSupport = (TableSupportIfc) InMemDBServer
				.getInstance().getModule(TableSupportIfc.class);
		Set<String> tables = tableSupport.getTableStructure().keySet();
		Connection conn = ConnectionManager.getConnection();
		try {
			Statement st = conn.createStatement();
			for (String tableName : tables) {
				String cluserTiggerSQL_insert = "CREATE TRIGGER " + tableName
						+ "_INS_PERSISTENT AFTER INSERT ON " + tableName
						+ " FOR EACH ROW CALL \""
						+ BackupTrigger.class.getName() + "\"";
				String cluserTiggerSQL_update = "CREATE TRIGGER " + tableName
						+ "_UPD_PERSISTENT AFTER UPDATE ON " + tableName
						+ " FOR EACH ROW CALL \""
						+ BackupTrigger.class.getName() + "\"";
				String cluserTiggerSQL_delete = "CREATE TRIGGER " + tableName
						+ "_DEL_PERSISTENT AFTER DELETE ON " + tableName
						+ " FOR EACH ROW CALL \""
						+ BackupTrigger.class.getName() + "\"";
				DBLogger.log(DBLogger.DEBUG, "创建持久备份支持："
						+ cluserTiggerSQL_insert + ", "
						+ cluserTiggerSQL_update + ", "
						+ cluserTiggerSQL_delete);
				st.execute(cluserTiggerSQL_insert);
				st.execute(cluserTiggerSQL_update);
				st.execute(cluserTiggerSQL_delete);
			}
			st.close();
			conn.commit();
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "创建集群触发器失败", e);
		} finally {
			ConnectionManager.releaseConnection(conn);
		}
	}

	/**
	 * 在持久层建立表结构
	 */
	public void createPersistanceStructure() {
		synchronized (backupRestoreLock) {
			Connection conn = ConnectionManager.getPersistentConnection();
			try {
				TableSupportIfc tableSupport = (TableSupportIfc) InMemDBServer
						.getInstance().getModule(TableSupportIfc.class);
				Set<String> tables = tableSupport.getTableStructure().keySet();
				for (String demoTable : tables) {
					boolean structureCreated = true;
					try {
						Statement st = conn.createStatement();
						st.executeQuery("SELECT * FROM " + demoTable); // 这个语句是判断表有没有建立
						st.close();
					} catch (SQLException e) {
						structureCreated = false;
					}
					if (!structureCreated) {
						DBLogger.log(DBLogger.INFO, "开始创建持久层对应数据库表...");
						tableSupport.createTable(conn, false, null,false);
						DBLogger.log(DBLogger.INFO, "持久层对应数据库表创建完成!");
					}
				}
			} finally {
				ConnectionManager.releaseConnection(conn);
			}
		}
	}

	/**
	 * 重新创建持久层结构
	 * 
	 * @param rebuildTable
	 *            ,重建的表名，null或""表示所有表
	 */
	public void rebuildPersistenceStructure(String mbeanUser, String password,
			String rebuildTable) {
		synchronized (backupRestoreLock) {
			if (InMemDBServer.getInstance().checkJMXSecurity(mbeanUser,
					password)) {
				rebuild(rebuildTable);
			}
		}
	}

	/**
	 * 重新构造表结构
	 * 
	 * @param tableName
	 *            ,重建的表名，null或""表示所有表
	 */
	private void rebuild(String tableName) {
		Connection conn = ConnectionManager.getPersistentConnection();
		try {
			TableSupportIfc tableSupport = (TableSupportIfc) InMemDBServer
					.getInstance().getModule(TableSupportIfc.class);
			Set<String> tables = tableSupport.getTableStructure().keySet();
			for (String demoTable : tables) {
				try {
					Statement st = conn.createStatement();
					st.execute("DROP TABLE " + demoTable); // 这个语句是判断表有没有建立
					st.close();
				} catch (SQLException e) {
				}
				tableSupport.createTable(conn, false, tableName,false);
				DBLogger.log(DBLogger.INFO, "重新创建持久层对应数据库表");
			}
		} finally {
			ConnectionManager.releaseConnection(conn);
		}
	}

	public void restoreFromPersistence(String mbeanUser, String password,
			String restoreTable) {
		synchronized (backupRestoreLock) {
			if (InMemDBServer.getInstance().checkJMXSecurity(mbeanUser,
					password)) {
				// Connection conn =
				// ConnectionManager.getPersistentConnection();
				// 如果当前master是自己，则需要从持久层恢复
				ClusterIfc cluster = (ClusterIfc) InMemDBServer.getInstance()
						.getModule(ClusterIfc.class);
				if (cluster.getCurrentClusterAddress().equals(
						cluster.getMasterAddress())) {
					restore(restoreTable);
				} else {
					DBLogger.log(DBLogger.WARN,
							"只有在集群服务数为1, 并且当前Master是自身的时候才能进行持久层恢复，其他情况，集群机器都能自我恢复");
				}
			}
		}
	}

	/**
	 * 从持久层恢复
	 */
	private void restore(String restoreTable) {
		Connection pers_conn = ConnectionManager.getPersistentConnection();

		Connection in_conn = SlaveConnectionFactory.getSlaveConnection();
		try {
			Set<String> tables;
			TableSupportIfc tableSupport = (TableSupportIfc) InMemDBServer
					.getInstance().getModule(TableSupportIfc.class);
			if (restoreTable == null || restoreTable.trim().equals("")) {
				tables = tableSupport.getTableStructure().keySet();
			} else {
				tables = new HashSet<String>();
				tables.add(restoreTable.toUpperCase());
			}
			for (String demoTable : tables) {
				try {
					// 清空内存数据库表中的数据
					Statement st = in_conn.createStatement();
					st.executeUpdate("DELETE FROM " + demoTable); // 这个语句是判断表有没有建立
					in_conn.commit();
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Statement perSt = pers_conn.createStatement();
				ResultSet perRs = perSt.executeQuery("SELECT * FROM "
						+ demoTable);
				StringBuffer inSQLBuffer = new StringBuffer("INSERT INTO ");
				inSQLBuffer.append(demoTable);
				inSQLBuffer.append(" VALUES(");
				String[] columns = tableSupport.getTableStructure().get(
						demoTable);
				for (int i = 0; i < columns.length; i++) {
					if (i > 0) {
						inSQLBuffer.append(",");
					}
					inSQLBuffer.append("?");
				}
				inSQLBuffer.append(")");
				PreparedStatement inSt = in_conn.prepareStatement(inSQLBuffer
						.toString());
				while (perRs.next()) {
					for (int i = 1; i <= columns.length; i++) {
						inSt.setObject(i, perRs.getObject(i));
					}
					inSt.execute();
				}
				in_conn.commit();
				inSt.close();
				perRs.close();
				perSt.close();
			}
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "", e);
		} finally {
			ConnectionManager.releaseConnection(pers_conn);
			SlaveConnectionFactory.releaseSlaveConnection(in_conn);
		}

	}

	@Override
	public void shutdownModule(Map<?, ?> params) {
		DBLogger.log(DBLogger.INFO, "关闭数据库持久化备份/恢复支持模块");
		DBLogger.log(DBLogger.INFO, "等待持久队列数：" + dbOperationQueue.size());
		flag = false;
		persistentMaxRowLimiter.stopLimiter();
		persistentMaxRowLimiter = null;
	}

	/**
	 * 插入持久队列，只有在master时才调用这个方法
	 * 
	 * @param o
	 */
	@Override
	public void insertSignal(Object o) {
		try {
			dbOperationQueue.put(o);
		} catch (InterruptedException e) {
			DBLogger.log(DBLogger.ERROR, "", e);
		}
	}

	private class BackUp implements Runnable {

		public void run() {
			while (flag) {
				try {
					Object o = dbOperationQueue.take();
					if (o instanceof DBOperationNote) {
						dbOperationHandler
								.handleDBOperations((DBOperationNote) o);
					} else if (o instanceof CommitRollbackNote) {
						commitRollbackHandler
								.handleCommitRollback((CommitRollbackNote) o);
					} else if (o instanceof MaxRowCheckSignal) {
						MaxRowCheckSignal sig = (MaxRowCheckSignal) o;
						getPersistentMaxRowLimiter().fire(sig.getTableName(),
								sig.getMaxRowCount());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			DBLogger.log(DBLogger.INFO, "持久实时备份线程退出");
		}
	}

	@Override
	public Map<Integer, Connection> getUncommitedConnections() {
		return uncomitedConnections;
	}

	@Override
	public Map<Integer, UncommitedConnectionTimeOutTask> getUncommitedConnectionTimeOutTasks() {
		return timeoutTasks;
	}

	@Override
	public void addConnectionTimeOutTimerTask(
			UncommitedConnectionTimeOutTask uncommitedConnectionTimeOutTask,
			int timeout) {
		uncommitedConnTimer.schedule(uncommitedConnectionTimeOutTask, timeout);
		timeoutTasks.put(uncommitedConnectionTimeOutTask.getMasterSessionId(),
				uncommitedConnectionTimeOutTask);
	}

	@Override
	public MaxRowLimiter getPersistentMaxRowLimiter() {

		return persistentMaxRowLimiter;
	}

}
