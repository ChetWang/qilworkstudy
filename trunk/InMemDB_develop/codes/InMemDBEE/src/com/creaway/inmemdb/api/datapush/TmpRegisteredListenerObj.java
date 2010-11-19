/*
 * @(#)TmpRegisteredListenerObj.java	1.0  10/18/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.api.datapush;

/**
 * 临时事件存储对象
 * @author Qil.Wong
 *
 */
public class TmpRegisteredListenerObj {
	/**
	 * 命令
	 */
	public int command;
	/**
	 * 触发类型
	 */
	public int triggerType;
	
	/**
	 * 事件对象，可能是本地事件，也可能是远程事件
	 */
	public Object lisObj;
}
