/*
 * @(#)TableSupportMBean.java	1.0  08/27/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.table;

import com.creaway.inmemdb.core.DBModule;

/**
 * 表支持模块提供的JMX远程操作对象
 * @author Qil.Wong
 *
 */
public interface TableSupportMBean extends DBModule {

	/**
	 * 重新构建表
	 * @param user
	 * @param password
	 * @param tableName
	 */
	public void rebuildTable(String user, String password, String tableName);

}
