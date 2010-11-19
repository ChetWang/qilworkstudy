/*
 * @(#)SlaveRestorHandler.java	1.0  09/07/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster.restore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.creaway.inmemdb.cluster.ClusterIfc;
import com.creaway.inmemdb.cluster.ClusterSlaveHandler;
import com.creaway.inmemdb.util.DBLogger;

/**
 * Slave集群服务数据恢复处理器
 * @author Qil.Wong
 *
 */
public class SlaveRestorHandler extends ClusterSlaveHandler {

	public SlaveRestorHandler(ClusterIfc cluster) {
		super(cluster);
	}

	public void handleRestoreData(RestoreNote[] rn) {
		// 数据恢复肯定是在同一事务下进行的
		int masterSessionNO = rn[0].getSessionNO();
		// 获取从属连接，如果为空则是第一次事务开始；当然，这也有可能是超时后的数据，这里不考虑。
		// 如果是超时数据，则需要适度把握超时时间设置！！！！！
		try {
			Connection slaveConn = getSlaveConnection(masterSessionNO, true);
			executeInsert(slaveConn, rn);
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, cluster.getCurrentClusterAddress()
					+ "数据恢复时插入数据出错，Master=" + cluster.getMasterAddress(), e);
		} finally {
			// DO NOTHING HERE
		}
	}

	int x = 0;

	/**
	 * 执行数据插入操作
	 * 
	 * @param slaveConn
	 *            从属连接
	 * @param operNote
	 *            集群主连接事务发送过来的单批数据
	 */
	private void executeInsert(Connection slaveConn, RestoreNote[] rns)
			throws SQLException {
		// 插入数据在newValue中
		PreparedStatement prep = slaveConn
				.prepareStatement(createInsertSQL(rns[0].getTableName()));
		for (RestoreNote rn : rns) {
			if (rn == null)
				break;
			Object[] newRow = rn.getRows();

			for (int i = 0; i < newRow.length; i++) {
				if (newRow[i] == null)
					continue;
				prep.setObject(i + 1, newRow[i]);
			}
			prep.executeUpdate();
		}
		prep.close();
	}

}
