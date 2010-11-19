/*
 * @(#)InMemMaxRowLimiter.java	1.0  09/26/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.table;

import java.sql.Connection;

import com.creaway.inmemdb.cluster.SlaveConnectionFactory;

/**
 * 内存数据库表的行数限制器
 * 
 * @author Qil.Wong
 * 
 */
public class InMemMaxRowLimiter extends MaxRowLimiter {
	/**
	 * 限制行数的sql模板
	 */
	private String templateSQL;

	public InMemMaxRowLimiter() {
		setName("InMem Row Limiter");
		templateSQL = "DELETE FROM " + regExp_tableName + " WHERE "
				+ TableSupport.AUTO_INCREMENT_COLUMN + " >= (SELECT MIN("
				+ TableSupport.AUTO_INCREMENT_COLUMN + ") FROM (SELECT TOP "
				+ regExp_count + " (" + TableSupport.AUTO_INCREMENT_COLUMN
				+ ")  FROM " + regExp_tableName + "  ORDER BY "
				+ TableSupport.AUTO_INCREMENT_COLUMN + " DESC))";
	}

	@Override
	public Connection getConnection() {
		//各个集群服务器都有同类线程操作，此操作不影响集群和持久层，所以需要用slave connection
		return SlaveConnectionFactory.getSlaveConnection();
	}

	protected void releaseConnection(Connection conn) {
		SlaveConnectionFactory.releaseSlaveConnection(conn);
	}

	@Override
	public String getTemplateDeleteSQL(String tableName, int topCount) {
		return templateSQL.replaceAll(regExp_tableName, tableName).replaceAll(
				regExp_count, String.valueOf(topCount));
	}

}
