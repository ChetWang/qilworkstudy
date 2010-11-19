/*
 * @(#)MaxRowLimitTrigger.java	1.0  09/25/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.trigger;

import java.sql.Connection;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.creaway.inmemdb.InMemSession;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.persistent.PersistentBackupRestoreIfc;
import com.creaway.inmemdb.table.TableSupportIfc;
import com.creaway.inmemdb.util.Functions;

/**
 * 对表做对大行数限制时的触发器
 * 
 * @author Qil.Wong
 * 
 */
public class MaxRowLimitTrigger extends InMemDBTrigger {

	private int maxRowCount;

	private TableSupportIfc tableSupport;

	private PersistentBackupRestoreIfc backupRestore;

	public void init(Connection conn, String schemaName, String triggerName,
			String tableName, boolean before, int type) {
		super.init(conn, schemaName, triggerName, tableName, before, type);
		Document doc = Functions.getXMLDocument(getClass().getResource(
				"/resources/tables.xml"));
		Element tableEle = Functions.findElement("/tables/table[@name='"
				+ tableName.toUpperCase() + "']", doc.getDocumentElement());
		maxRowCount = Integer.parseInt(tableEle.getAttribute("maxRowCount"));
		
	}

	@Override
	public void fireTrigger(InMemSession session, Object[] oldRow, Object[] newRow)
			throws SQLException {
		if (tableSupport == null) {
			tableSupport = (TableSupportIfc) InMemDBServer.getInstance()
					.getModule(TableSupportIfc.class);
		}
		if (tableSupport != null)
			tableSupport.getInMemMaxRowLimiter().fire(tableName, maxRowCount);
		if (backupRestore == null) {
			backupRestore = (PersistentBackupRestoreIfc) InMemDBServer
					.getInstance().getModule(PersistentBackupRestoreIfc.class);
		}
		if (backupRestore != null) // 持久层不直接触发，因为可能有一批队列还在等待持久，所以将检查信号量也加入等待数组
			backupRestore.insertSignal(new MaxRowCheckSignal(tableName,
					maxRowCount));
	}
}
