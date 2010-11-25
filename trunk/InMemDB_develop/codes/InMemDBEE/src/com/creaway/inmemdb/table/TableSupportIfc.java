/*
 * @(#)TableSupportIfc.java	1.0  08/27/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.table;

import java.sql.Connection;
import java.util.Map;

/**
 * 内存数据库表支持模块，用于创建和维护用户定义的表、索引等数据库表内容
 * 
 * @author Qil.Wong
 * 
 */
public interface TableSupportIfc extends TableSupportMBean {

	/**
	 * 获取数据库中的表结构
	 * 
	 * @return 表结构的Map对象，主键是表名，值是列的顺序集合
	 */
	public Map<String, String[]> getTableStructure();

	/**
	 * 创建表支持
	 * 
	 * @param conn
	 *            指定的链接，可以是内存数据库自身链接，也可以是持久层链接
	 * @param storeTableStructure
	 *            是否创建表格式并进行缓存，只需缓存一次即可
	 * @param tableName
	 *            表名，null表示所有表
	 */
	public void createTable(Connection conn, boolean storeTableStructure,
			String tableName, boolean innerDBTable);

	public MaxRowLimiter getInMemMaxRowLimiter();

}
