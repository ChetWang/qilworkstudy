/*
 * @(#)PushActionListener.java	1.0  09/24/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.api.datapush;

/**
 * 本地数据推送服务的监听器对象
 * @author Qil.Wong
 *
 */
public interface PushActionListener {

	/**
	 * 数据符合条件，准备推送给注册方
	 * @param result 符合条件的数据结果
	 */
	public void actionPerformed(Object result);

	
}
