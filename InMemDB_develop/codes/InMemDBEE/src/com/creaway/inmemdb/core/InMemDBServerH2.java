/*
 * @(#)InMemDBServerH2.java	1.0  08/24/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.h2.engine.ConnectionInfo;
import org.h2.engine.SessionRemote;
import org.h2.tools.Server;

import com.creaway.inmemdb.InMemDriver;
import com.creaway.inmemdb.InMemSession;
import com.creaway.inmemdb.util.DBLogger;

/**
 * 内存数据库主服务类，无论是jndi，还是servlet，还是applicaiton都要启动<b><code>InMemDBServer</code>
 * </b>才能运行内存数据库
 * 
 * @author Qil.Wong
 * 
 */
public final class InMemDBServerH2 extends InMemDBServer {

	/**
	 * web服务页面的控制工具服务器
	 */
	private Server webserver;
	
	public InMemDBServerH2(){
		
	}


	@Override
	public void startModule(Map<?, ?> params) {
		super.startModule(params);
		try {
			webserver = Server.createWebServer(
					new String[] { "-webPort",
							getSystemProperties().getProperty("webport"),"-webAllowOthers" })
					.start();
			try {
				DBLogger.log(DBLogger.INFO, "创建内存数据库Web服务器控制台. 访问http://"
						+ InetAddress.getLocalHost().getHostAddress() + ":"
						+ getSystemProperties().getProperty("webport"));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "创建内存数据库服务器失败", e);
		}
		
		DBLogger.log(DBLogger.INFO, "内存数据库启动完成");
	}

	@Override
	public void shutdownModule(Map<?, ?> params) {
		super.shutdownModule(params);
		if (webserver != null)
			webserver.stop();
	}

	@Override
	public InMemSession createDataBaseSession() {
		Properties p = new Properties();
		p.put("user", getSystemProperties().getProperty("memdb_user"));
		p.put("password", getSystemProperties().getProperty("memdb_password"));
		ConnectionInfo ci = new ConnectionInfo(
				InMemDriver.parseURL(getSystemProperties().getProperty(
						"memdb_url")), p);
		return (InMemSession) new SessionRemote(ci).createSession(ci);
	}
	
	public static void main(String[] xxx) {
		new InMemDBServerH2().startModule(null);
	}

}
