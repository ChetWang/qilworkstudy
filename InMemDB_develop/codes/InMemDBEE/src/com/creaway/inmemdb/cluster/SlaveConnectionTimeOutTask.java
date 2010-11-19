/*
 * @(#)SlaveConnectionTimeOutTask.java	1.0  09/03/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimerTask;

import com.creaway.inmemdb.util.DBLogger;

/**
 * 从属连接的时效性监听器,长时间连接未提交将会移除出从属连接缓存
 * 
 * @author Qil.Wong
 * 
 */
public class SlaveConnectionTimeOutTask extends TimerTask {

	/**
	 * 从属连接序号
	 */
	private int masterSessionNO;

	/**
	 * 集群支持对象
	 */
	private ClusterIfc cluster;

	public SlaveConnectionTimeOutTask(ClusterIfc cluster, int masterSessionNO) {
		this.masterSessionNO = masterSessionNO;
		this.cluster = cluster;
	}

	@Override
	public void run() {
		// 时效性已过，释放连接，并从缓存中移除
		DBLogger.log(DBLogger.WARN, "时效性已过，释放连接，并从缓存中移除  slaveConn = "
				+ masterSessionNO);
		Connection conn = cluster.getSlaveConnections().get(masterSessionNO);
		try {
			conn.rollback();
		} catch (SQLException e) {
		}
		SlaveConnectionFactory.releaseSlaveConnection(conn);
		cluster.getSlaveConnections().remove(masterSessionNO);
	}

	public int getMasterSessionNO() {
		return masterSessionNO;
	}

	public ClusterIfc getCluster() {
		return cluster;
	}

}