/*
 * @(#)RemotePushActionListener.java	1.0  09/21/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.api.datapush;

import java.nio.channels.SelectionKey;

/**
 * 远程数据推送监听接口
 * @author Qil.Wong
 *
 */
public interface RemotePushActionListener {

	/**
	 * 符合条件时的触发动作
	 * @param command 远程触发器指令
	 * @param operationResult 条件符合时产生的结果
	 * @param key 远程NIO对应的SelectionKey对象
	 */
	public void remoteActionPerformed(int command,Object operationResult, SelectionKey key);
}
