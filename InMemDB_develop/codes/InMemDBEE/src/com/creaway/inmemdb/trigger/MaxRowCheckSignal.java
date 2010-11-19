/*
 * @(#)MaxRowCheckSignal.java	1.0  08/25/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.trigger;

/**
 * 行数限制器中出发线程执行命令的信号Bean
 * @author Qil.Wong
 *
 */
public class MaxRowCheckSignal {

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 表限制的最大行数
	 */
	private int maxRowCount;

	public MaxRowCheckSignal() {

	}

	public MaxRowCheckSignal(String tableName, int maxRowCount) {
		this.tableName = tableName;
		this.maxRowCount = maxRowCount;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getMaxRowCount() {
		return maxRowCount;
	}

	public void setMaxRowCount(int maxRowCount) {
		this.maxRowCount = maxRowCount;
	}

}
