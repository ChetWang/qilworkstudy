/*
 * @(#)InMemDBServerMBean.java	1.0  09/14/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.creaway.inmemdb.core;

/**
 * 内存数据库主服务提供的JMX MBean
 * 
 * @author Qil.Wong
 * 
 */
public interface InMemDBServerMBean extends DBModule {

	/**
	 * 启动服务器
	 */
	
	public void start(String user, String psw);

	/**
	 * 停止服务器
	 */
	public void stop(String user, String password);

}
