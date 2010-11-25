/*
 * @(#)PersistentBackupRestoreMBean.java	1.0  09/02/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.persistent;

/**
 * 持久层支持的JMX MBean对象，提供几个操作模块的方法
 * 
 * @author Qil.Wong
 * 
 */
public interface PersistentBackupRestoreMBean {
	/**
	 * MBean 操作， 重构持久层表结构
	 * 
	 * @param mbeanUser
	 *            mbean的用户
	 * @param password
	 *            mbean用户密码
	 * @param rebuildTable
	 *            重建的表名，null或""表示所有表
	 */

	public void rebuildPersistenceStructure(String mbeanUser, String password,
			String rebuildTable);

	/**
	 * MBean 操作， 数据从持久层恢复
	 * 
	 * @param mbeanUser
	 *            mbean的用户
	 * @param password
	 *            mbean用户密码
	 * @param restoreTable
	 *            重建的表名，null或""表示所有表
	 */
	public void restoreFromPersistence(String mbeanUser, String password,
			String restoreTable);

}
