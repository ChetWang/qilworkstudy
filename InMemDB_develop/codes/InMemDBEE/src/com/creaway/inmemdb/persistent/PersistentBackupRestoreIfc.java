/*
 * @(#)PersistentBackupRestoreIfc.java	1.0  09/12/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.persistent;

import java.sql.Connection;
import java.util.Map;

import com.creaway.inmemdb.table.MaxRowLimiter;

/**
 * 实时数据持久存储接口
 * 
 * @author Qil.Wong
 * 
 */
public interface PersistentBackupRestoreIfc extends
		PersistentBackupRestoreMBean {

	public static final String DATASOURCE_NAME = "persistence";

	/**
	 * 获取未提交的连接
	 * 
	 * @return
	 */
	public Map<Integer, Connection> getUncommitedConnections();

	/**
	 * 初始化持久层触发器
	 */
	public void initPersistentTrigger();

	/**
	 * 获取未提交连接对应的时效监听对象
	 * 
	 * @return
	 */
	public Map<Integer, UncommitedConnectionTimeOutTask> getUncommitedConnectionTimeOutTasks();

	/**
	 * 添加未提交连接对应的时效监听对象
	 * 
	 * @param task
	 * @param timeout 时效（ms毫秒）
	 */
	public void addConnectionTimeOutTimerTask(
			UncommitedConnectionTimeOutTask task,
			int timeout);

	/**
	 * 处理数据动作。从性能上考虑，持久层不与内存数据库触发同步，因此，将数据信号传递到一个信号池中（可以是各种插入数据、commit信号等），
	 * 由单独线程处理这个池中的信号
	 * 
	 * @param o
	 */
	public void insertSignal(Object o);

	/**
	 * 获取最大行数限制器
	 * @return
	 */
	public MaxRowLimiter getPersistentMaxRowLimiter();

}
