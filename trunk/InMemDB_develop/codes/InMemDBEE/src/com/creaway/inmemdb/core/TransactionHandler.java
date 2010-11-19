/*
 * @(#)TransactionHandler.java	1.0  09/20/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.creaway.inmemdb.cluster.DBOperationNote;
import com.creaway.inmemdb.table.TableSupportIfc;

/**
 * 集群SQL事务处理器，用于处理集群SQL消息
 * @author Qil.Wong
 *
 */
public class TransactionHandler {

	protected Map<String, String> tempSQL = new ConcurrentHashMap<String, String>();

	/**
	 * 获取指定的表的列集合
	 * 
	 * @param tableName
	 * @return
	 */
	protected String[] getTableColumns(String tableName) {
		TableSupportIfc dbTableSupport = (TableSupportIfc) InMemDBServer
				.getInstance().getModule(TableSupportIfc.class);
		return dbTableSupport.getTableStructure().get(tableName);
	}

	/**
	 * 生成适合preparedstatement的SQL INSERT语句
	 * 
	 * @param tableName
	 *            插入数据所在的表
	 * @return
	 */
	protected String createInsertSQL(String tableName) {
		String sql_insert = tempSQL.get("INSERT_" + tableName);
		if (sql_insert == null) {
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO ").append(tableName);
			sql.append(" (");
			String[] columns = getTableColumns(tableName);
			for (int i = 0; i < columns.length; i++) {
				if (i > 0) {
					sql.append(",");
				}
				sql.append(columns[i]);
			}
			sql.append(") VALUES (");
			for (int i = 0; i < columns.length; i++) {
				if (i > 0) {
					sql.append(",");
				}
				sql.append("?");
			}
			sql.append(")");
			sql_insert = sql.toString();
			tempSQL.put("INSERT_" + tableName, sql_insert);
		}
		return sql_insert;
	}

	/**
	 * 生成适合preparedstatement的SQL UPDATE语句
	 * 
	 * @param tableName
	 *            插入数据所在的表
	 * @return
	 */
	protected String createUpdateSQL(String tableName) {
		String sql_update = tempSQL.get("UPDATE_" + tableName);
		if (sql_update == null) {
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE ").append(tableName);
			sql.append(" SET ");
			String[] columns = getTableColumns(tableName);
			for (int i = 0; i < columns.length; i++) {
				if (i > 0) {
					sql.append(",");
				}
				sql.append(columns[i]).append("=").append("?");
			}
			sql.append(" WHERE ");
			for (int i = 0; i < columns.length; i++) {
				if (i > 0) {
					sql.append(" AND ");
				}
				sql.append(columns[i]).append("=").append("?");
			}
			sql_update = sql.toString();
			tempSQL.put("UPDATE_" + tableName, sql_update);
		}
		return sql_update;
	}

	/**
	 * 生成适合preparedstatement的SQL DELETE语句
	 * 
	 * @param tableName
	 *            插入数据所在的表
	 * @return
	 */
	protected String createDeleteSQL(String tableName) {
		String sql_delete = tempSQL.get("DELETE_" + tableName);
		if (sql_delete == null) {
			StringBuffer sql = new StringBuffer();
			sql.append("DELETE FROM ").append(tableName);
			sql.append(" WHERE ");
			String[] columns = getTableColumns(tableName);
			for (int i = 0; i < columns.length; i++) {
				if (i > 0) {
					sql.append(" AND ");
				}
				sql.append(columns[i]).append("=").append("?");
			}
			sql_delete = sql.toString();
			tempSQL.put("DELETE_" + tableName, sql_delete);
		}
		return sql_delete;
	}

	/**
	 * 执行数据插入操作
	 * 
	 * @param uncommitedConn
	 *            从属连接
	 * @param operNote
	 *            主连接事务发送过来的单批数据
	 */
	protected void executeInsert(Connection uncommitedConn, DBOperationNote operNote)
			throws SQLException {
		// 插入数据在newValue中

		Object[] newRow = operNote.getNewValue();
		String sql_insert = createInsertSQL(operNote.getTableName());
		PreparedStatement prep = uncommitedConn.prepareStatement(sql_insert);
		for (int i = 0; i < newRow.length; i++) {
			if (newRow[i] == null)
				continue;
			prep.setObject(i + 1, newRow[i]);
		}
		prep.execute();
		prep.close();
	}

	/**
	 * 更新操作
	 * 
	 * @param uncommitedConn
	 *            从属连接
	 * @param operNote
	 *            主连接事务发送过来的单批数据
	 * @throws SQLException
	 */
	protected void executeUpdate(Connection uncommitedConn, DBOperationNote operNote)
			throws SQLException {
		Object[] oldRows = operNote.getOldValue();
		Object[] newRows = operNote.getNewValue();
		String sql_update = createUpdateSQL(operNote.getTableName());
		PreparedStatement prep = uncommitedConn.prepareStatement(sql_update);
		for (int i = 0; i < newRows.length; i++) {
			if (newRows[i] == null)
				continue;
			prep.setObject(i + 1, newRows[i]);
		}
		for (int i = 0; i < oldRows.length; i++) {
			if (oldRows[i] == null)
				continue;
			prep.setObject(i + 1 + newRows.length, oldRows[i]);
		}
		prep.executeUpdate();
		prep.close();
	}

	/**
	 * 删除操作
	 * 
	 * @param uncommitedConn
	 *            从属连接
	 * @param operNote
	 *            主连接事务发送过来的单批数据
	 * @throws SQLException
	 */
	protected void executeDelete(Connection uncommitedConn, DBOperationNote operNote)
			throws SQLException {
		// 删除的数据在oldvalue中
		Object[] oldRows = operNote.getOldValue();
		String sql_delete = createDeleteSQL(operNote.getTableName());
		PreparedStatement prep = uncommitedConn.prepareStatement(sql_delete);
		for (int i = 0; i < oldRows.length; i++) {
			if (oldRows[i] == null)
				continue;
			prep.setObject(i + 1, oldRows[i]);
		}
		prep.execute();
		prep.close();
	}

}
