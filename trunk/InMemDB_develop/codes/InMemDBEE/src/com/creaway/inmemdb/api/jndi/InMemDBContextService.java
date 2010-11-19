/*
 * @(#)InMemDBContextService.java	1.0  09/28/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.api.jndi;

import java.sql.Connection;

import com.creaway.inmemdb.api.datapush.PushServiceIfc;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.util.ConnectionManager;

/**
 * JNDI Context中暴露给第三方应用的上下文服务
 * 
 * @author Qil.Wong
 * 
 */
public class InMemDBContextService {

	public static final String CONTEXT_NAME = "inmemdb-context-service";

	public static final String CONTEXT_FACTORY_NAME = "com.creaway.inmemdb.jndi.InMemDBContextFactory";

	public static final int DATAPUSH_TYPE_INSERT = 1;
	public static final int DATAPUSH_TYPE_UPDATE = 2;
	public static final int DATAPUSH_TYPE_DELETE = 4;
	public static final int DATAPUSH_TYPE_SELECT = 8;
	
	/**
	 * 获取内存数据库连接
	 * 
	 * @return
	 */
	public Connection getInmemDBConnection() {
		return ConnectionManager.getConnection();
	}

	/**
	 * 获取数据推送模式下的推送支持服务，可以用来根据相应的指令注册推送事件
	 * 
	 * @return
	 */
	public PushServiceIfc getDataPushService() {
		return InMemDBServer.getInstance().getPushService();
	}

}
