/*
 * @(#)ClusterSlaveHandler.java	1.0  09/02/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster;

import java.sql.Connection;
import java.sql.SQLException;

import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.core.TransactionHandler;

/**
 * 集群条件下的Slave集群服务的动作
 * @author Qil.Wong
 *
 */
public class ClusterSlaveHandler extends TransactionHandler{

	protected ClusterIfc cluster;
	
	

	public ClusterSlaveHandler(ClusterIfc cluster) {
		this.cluster = cluster;
	}

	/**
	 * 获取缓存的slave 数据库连接
	 * @param masterSessionNO master主服务当前事务下对应的session id
	 * @param autoTimeout 是否自动设置timeout
	 * @return
	 * @throws SQLException
	 */
	public Connection getSlaveConnection(int masterSessionNO,
			boolean autoTimeout) throws SQLException {

		// 获取从属连接，如果为空则是第一次事务开始；当然，这也有可能是超时后的数据，这里不考虑。
		// 如果是超时数据，则需要适度把握超时时间设置！！！！！
		Connection slaveConn = cluster.getSlaveConnections().get(
				masterSessionNO);
		if (slaveConn == null) {
			slaveConn = SlaveConnectionFactory.getSlaveConnection();

			slaveConn.setAutoCommit(false);

			cluster.getSlaveConnections().put(masterSessionNO, slaveConn);
			// 增加超时监听
			if (!autoTimeout) {
				cluster.addConnectionTimeOutTimerTask(
						new SlaveConnectionTimeOutTask(cluster, masterSessionNO),
						Integer.parseInt(InMemDBServer.getInstance()
								.getSystemProperties()
								.get("slave_conn_timeout").toString()));
			}
		}
		return slaveConn;
	}
	
	

}
