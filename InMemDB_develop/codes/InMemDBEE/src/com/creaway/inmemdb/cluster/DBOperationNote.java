/*
 * @(#)DBOperationNote.java	1.0  09/02/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster;

import java.io.Serializable;

/**
 * 集群数据库操作通知对象，包括数据的增删改，不包括commit/rollback
 * 
 * @author Qil.Wong
 * 
 */
public class DBOperationNote implements Serializable {

	private static final long serialVersionUID = 4039406229679556008L;

	/**
	 * 操作类型
	 * 
	 * @see InMemDBTrigger.TYPE_INSERT
	 * @see InMemDBTrigger.TYPE_UPDATE
	 * @see InMemDBTrigger.TYPE_DELETE
	 * @see InMemDBTrigger.TYPE_SELECT
	 */
	private int type;

	/**
	 * 集群操作所在的表名
	 */
	private String tableName;

	/**
	 * 集群数据库操作前的数据
	 */
	private Object[] oldValue;

	/**
	 * 集群数据库操作后的数据
	 */
	private Object[] newValue;
	
	/**
	 * 同一事务连接下的序号
	 */
	private int sessionNO;

	public DBOperationNote() {

	}

	/**
	 * 获取数据库操作类型
	 * 
	 * @return 操作类型
	 * @see InMemDBTrigger.TYPE_INSERT
	 * @see InMemDBTrigger.TYPE_UPDATE
	 * @see InMemDBTrigger.TYPE_DELETE
	 * @see InMemDBTrigger.TYPE_SELECT
	 * 
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置数据库操作类型
	 * 
	 * @param type 操作类型
	 * @see InMemDBTrigger.TYPE_INSERT
	 * @see InMemDBTrigger.TYPE_UPDATE
	 * @see InMemDBTrigger.TYPE_DELETE
	 * @see InMemDBTrigger.TYPE_SELECT
	 * 
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 获取数据库操作所在的表名
	 * 
	 * @return 表名
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置数据库操作所在的表名
	 * 
	 * @param tableName 表名
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 获取集群数据库操作前的一行数据
	 * 
	 * @return 操作前的一行数据
	 */
	public Object[] getOldValue() {
		return oldValue;
	}

	/**
	 * 设置集群数据库操作前的一行数据
	 * 
	 * @param oldValue 操作前的一行数据
	 */
	public void setOldValue(Object[] oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * 获取集群数据库操作后的一行数据
	 * 
	 * @return 操作后的一行数据
	 */
	public Object[] getNewValue() {
		return newValue;
	}

	/**
	 * 设置集群数据库操作后的一行数据
	 * 
	 * @param newValue 操作后的一行数据
	 */
	public void setNewValue(Object[] newValue) {
		this.newValue = newValue;
	}

	/**
	 * 获取事务连接序号
	 * @return 事务连接序号
	 */
	public int getSessionNO() {
		return sessionNO;
	}

	/**
	 * 设置事务连接序号
	 * @param sessionNO 事务连接序号
	 */
	public void setSessionNO(int sessionNO) {
		this.sessionNO = sessionNO;
	}

}
