/*
 * @(#)PersistentMaxRowLimiter.java	1.0  09/02/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.persistent;

import java.sql.Connection;

import com.creaway.inmemdb.table.MaxRowLimiter;
import com.creaway.inmemdb.table.TableSupport;
import com.creaway.inmemdb.util.ConnectionManager;

/**
 * 持久层的行数限制器，适配Oracle数据库
 * @author Qil.Wong
 *
 */
public class PersistentMaxRowLimiter extends MaxRowLimiter {

	private String templateSQL;

	public PersistentMaxRowLimiter() {
		setName("Persistent Row Limiter");
		templateSQL = "DELETE FROM " + regExp_tableName + " WHERE "
				+ TableSupport.AUTO_INCREMENT_COLUMN + " >= (SELECT MIN("
				+ TableSupport.AUTO_INCREMENT_COLUMN + ") FROM (SELECT "
				+ TableSupport.AUTO_INCREMENT_COLUMN + " FROM (SELECT "
				+ TableSupport.AUTO_INCREMENT_COLUMN + " FROM "
				+ regExp_tableName + " ORDER BY "
				+ TableSupport.AUTO_INCREMENT_COLUMN + " DESC "
				+ ") WHERE ROWNUM<=" + regExp_count + " ))";
	}

	@Override
	public Connection getConnection() {
		return ConnectionManager.getPersistentConnection();
	}

	@Override
	public String getTemplateDeleteSQL(String tableName, int topCount) {
		return templateSQL.replaceAll(regExp_tableName, tableName).replaceAll(
				regExp_count, String.valueOf(topCount));
	}

	
}
