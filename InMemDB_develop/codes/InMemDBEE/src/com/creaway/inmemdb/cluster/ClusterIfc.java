/*
 * @(#)Cluster.java	1.0  09/02/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.creaway.inmemdb.core.DBModule;

/**
 * 集群支持接口
 * 
 * @author Qil.Wong
 * 
 */
public interface ClusterIfc extends DBModule {

	/**
	 * 连接的序号
	 */
	public static final String MASTER_SESSION_ID = "master_session_id";

	/**
	 * 内存数据库集群名称
	 */
	public static final String CLUSTER_NAME = "inmemdb";

	/**
	 * 将消息组播给各成员
	 * 
	 * @param message
	 *            消息对象
	 */
	public void sendDataToMembers(Serializable messageObj,Object destination);

	/**
	 * 获取集群从属连接集合
	 * 
	 * @return 从属连接集合，key为主连接的connectionNO，value为与主连接对应同一事务下的从属连接
	 */
	public Map<Integer, Connection> getSlaveConnections();

	/**
	 * 添加从属连接时效性监控对象
	 * @param task 时效性监控对象
	 * @param timeout 有效时间（毫秒）
	 */
	public void addConnectionTimeOutTimerTask(SlaveConnectionTimeOutTask task,
			int timeout);

	/**
	 * 获取所有正在进行实效监控的对象，在事务完成后，实效监控对象必须关闭
	 * @return
	 */
	public Map<Integer, SlaveConnectionTimeOutTask> getSlaveConnectionTimeOutTasks();

	/**
	 * 判断当前集群是不是主集群服务器
	 * @return
	 */
	public boolean isMasterCluser();

	/**
	 * 设置当前集群是不是主集群服务器
	 * @param masterCluser
	 */
	public void setMasterCluser(boolean masterCluser);

	/**
	 * 获取当前集群的地址
	 * @return
	 */
	public Serializable getCurrentClusterAddress();

	/**
	 * 获取主集群Master地址
	 * @return
	 */
	public Serializable getMasterAddress();

	/**
	 * 从集群中进行数据恢复
	 */
	public void restoreFromCluster();

	/**
	 * 注册集群监听
	 * @param cl
	 */
	public void addClusterListener(ClusterListener cl);

	/**
	 * 获取当前所有集群组员
	 * @return
	 */
	public List getMembers();

}
