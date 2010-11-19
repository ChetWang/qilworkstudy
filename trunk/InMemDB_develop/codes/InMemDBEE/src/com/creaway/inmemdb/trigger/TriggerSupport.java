/*
 * @(#)TriggerSupport.java	1.0  08/27/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.trigger;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.creaway.inmemdb.api.datapush.PushActionListener;
import com.creaway.inmemdb.api.datapush.PushServiceIfc;
import com.creaway.inmemdb.api.datapush.TmpRegisteredListenerObj;
import com.creaway.inmemdb.cluster.SlaveConnectionFactory;
import com.creaway.inmemdb.core.DBModule;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.datapush.InMemDBCommandPushTrigger.RemoteActionListerObj;
import com.creaway.inmemdb.util.ConnectionManager;
import com.creaway.inmemdb.util.DBLogger;
import com.creaway.inmemdb.util.FileWatchDog;
import com.creaway.inmemdb.util.Functions;

/**
 * 触发器支持模块
 * 
 * @author Qil.Wong
 * 
 */
public class TriggerSupport implements DBModule {

	private List<String[]> triggerList = new CopyOnWriteArrayList<String[]>();

	private static String configFile = TriggerSupport.class.getResource(
			"/resources/triggers.xml").getFile();

	public TriggerSupport() {
		FileWatchDog fwd = new FileWatchDog(configFile) {

			@Override
			protected void doOnChange() {
				TriggerSupport.this.start();
			}
		};
		fwd.setName("trigger support watch dog");
		fwd.setDelay(5000);
		fwd.start();
	}

	protected void start() {
		dropTriggers();

		DBLogger.log(DBLogger.INFO, "启动触发器支持模块，开始创建内存数据库触发器...");
		Document triggeronfig = Functions.getXMLDocument(configFile);
		NodeList triggers = triggeronfig.getDocumentElement()
				.getElementsByTagName("trigger");
		Connection conn = SlaveConnectionFactory.getSlaveConnection();
		try {
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			for (int i = 0; i < triggers.getLength(); i++) {
				Element triggerEle = (Element) triggers.item(i);
				String table = triggerEle.getAttribute("table");
				String triggerName = triggerEle.getAttribute("name");
				String type = triggerEle.getAttribute("type").toUpperCase();
				String beforeOrAfter = triggerEle.getAttribute("before")
						.toLowerCase().equals("true") ? "BEFORE" : "AFTER";
				String className = triggerEle.getAttribute("class");
				StringBuffer triggerCreate = new StringBuffer("CREATE TRIGGER ");
				triggerCreate.append(triggerName).append(" ")
						.append(beforeOrAfter).append(" ");
				triggerCreate.append(type).append(" ");
				triggerCreate.append("ON ").append(table);
				if (triggerEle.getAttribute("eachrow").toLowerCase()
						.equals("true")) {
					triggerCreate.append(" FOR EACH ROW ");
				}
				triggerCreate.append(" CALL \"").append(className).append("\"");
				DBLogger.log(DBLogger.DEBUG, "创建触发器" + triggerName + ", SQL:"
						+ triggerCreate.toString());
				// 创建触发器
				st.execute(triggerCreate.toString());
				triggerList.add(new String[] { table, triggerName });
			}
			st.close();
			conn.commit();

			reRegisterPushListeners();
			DBLogger.log(DBLogger.INFO, "内存数据库触发器创建完成");
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "创建触发器失败！", e);
			SlaveConnectionFactory.rollbackSlaveConnection(conn);
		} finally {
			SlaveConnectionFactory.releaseSlaveConnection(conn);
		}

	}

	/**
	 * 重新注册远程命令监听
	 */
	private void reRegisterPushListeners() {
		// TriggerSupport启动完后，在PushService中已经存储了CommandTrigger
		List<TmpRegisteredListenerObj> tempListeners = InMemDBServer
				.getInstance().getPushService().getTempRegisteredListeners();
		PushServiceIfc pushService = InMemDBServer.getInstance()
				.getPushService();
		for (TmpRegisteredListenerObj tmp : tempListeners) {
			Object o = tmp.lisObj;
			if (o instanceof PushActionListener) {
				pushService.addPushCommandListener(tmp.command,
						tmp.triggerType, (PushActionListener) o);
			} else if (o instanceof RemoteActionListerObj) {
				RemoteActionListerObj remote = (RemoteActionListerObj) o;
				boolean flag = pushService.addRemoteSocketChannelListener(
						tmp.command, tmp.triggerType, remote.selectionKey,
						remote.remotePushListener);
				if (!flag) {
					SocketChannel sc = (SocketChannel) remote.selectionKey
							.channel();
					try {
						sc.close(); // 重新注册失败，可能触发器已经移除，将客户端断开，至于重新注册或影响到的其它的监听，交由客户端自己处理
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		tempListeners.clear();
	}

	@Override
	public void startModule(Map<?, ?> params) {
		start();
	}

	private void dropTriggers() {
		Connection conn = SlaveConnectionFactory.getSlaveConnection();
		try {
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			if (triggerList != null) {
				for (String[] trigger : triggerList) {
					st.execute("DROP TRIGGER " + trigger[1]);
				}
			}
			st.close();
			conn.commit();
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "移除触发器失败！", e);
			SlaveConnectionFactory.rollbackSlaveConnection(conn);
		} finally {
			triggerList.clear();
			SlaveConnectionFactory.releaseSlaveConnection(conn);
		}
	}

	@Override
	public void shutdownModule(Map<?, ?> params) {
		DBLogger.log(DBLogger.INFO, "关闭数据库触发器支持模块");
		dropTriggers();
	}

	/**
	 * 获取数据库中的触发器
	 * 
	 * @return 字符串数组集合，数组有两个元素，第一个为表名，第二个为触发器名
	 */
	public List<String[]> getTriggerList() {
		return triggerList;
	}

}
