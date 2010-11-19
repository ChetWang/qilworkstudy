/*
 * @(#)InMemDBContextFactory.java	1.0  09/20/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.jndi;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import com.creaway.inmemdb.api.jndi.InMemDBContextService;
import com.creaway.inmemdb.core.InMemDBServer;

/**
 * JNDI上下文连接工厂，负责将内存数据库提供的JContext服务对象绑定到JNDI
 * @author Qil.Wong
 */
public class InMemDBContextFactory implements InitialContextFactory {

	@Override
	public Context getInitialContext(Hashtable<?, ?> environment)
			throws NamingException {
		Map<String, Object> data = new ConcurrentHashMap<String, Object>();
		data.put(InMemDBContextService.CONTEXT_NAME, InMemDBServer.getInstance().getJndiContextObject());
		return new ReadOnlyContext(environment, data);
	}
}
