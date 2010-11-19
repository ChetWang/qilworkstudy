/*
 * @(#)InMemDBTrigger.java	1.0  08/25/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.trigger;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.api.Trigger;
import org.jgroups.util.GetNetworkInterfaces;

import com.creaway.inmemdb.InMemSession;

/**
 * 内存数据库触发器接口
 * 
 * @author Qil.Wong
 * 
 */
public abstract class InMemDBTrigger implements Trigger {

	protected String tableName;

	protected int type;

	public static final int TYPE_INSERT = 1;
	public static final int TYPE_UPDATE = 2;
	public static final int TYPE_DELETE = 4;
//	public static final int TYPE_SELECT = 8;

	public InMemDBTrigger() {
	}

	/**
	 * Initializes the trigger.
	 * 
	 * @param conn
	 *            a connection to the database
	 * @param schemaName
	 *            the name of the schema
	 * @param triggerName
	 *            the name of the trigger used in the CREATE TRIGGER statement
	 * @param tableName
	 *            the name of the table
	 * @param before
	 *            whether the fire method is called before or after the
	 *            operation is performed
	 * @param type
	 *            the operation type: INSERT, UPDATE, or DELETE
	 */
	public void init(Connection conn, String schemaName, String triggerName,
			String tableName, boolean before, int type) {
		this.tableName = tableName;
		this.type = type;
	}


	public abstract void fireTrigger(InMemSession session, Object[] oldRow,
			Object[] newRow) throws SQLException;

	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {
		// fireTigger(conn, oldRow, newRow);
	}

	@Override
	public void close() throws SQLException {

	}

	@Override
	public void remove() throws SQLException {
		
	}

}
