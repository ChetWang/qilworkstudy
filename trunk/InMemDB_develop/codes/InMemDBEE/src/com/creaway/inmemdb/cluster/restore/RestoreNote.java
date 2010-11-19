/*
 * @(#)RestoreNote.java	1.0  09/08/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster.restore;

import java.io.Serializable;

/**
 * 数据恢复单行对象，由Master服务发出，Slave恢复服务接受
 * 
 * @author Qil.Wong
 * 
 */
public class RestoreNote implements Serializable {

	private Serializable slaveAddress;
	private String tableName;
	private Object[] rows;
	private int masterSessionId;

	public RestoreNote() {

	}

	

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Object[] getRows() {
		return rows;
	}

	public void setRows(Object[] rows) {
		this.rows = rows;
	}



	public Serializable getSlaveAddress() {
		return slaveAddress;
	}



	public void setSlaveAddress(Serializable slaveAddress) {
		this.slaveAddress = slaveAddress;
	}



	public int getSessionNO() {
		return masterSessionId;
	}



	public void setMasterSessionId(int masterSessionId) {
		this.masterSessionId = masterSessionId;
	}

}
