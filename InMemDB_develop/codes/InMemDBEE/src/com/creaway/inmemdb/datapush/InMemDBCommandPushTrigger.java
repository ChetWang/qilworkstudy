/*
 * @(#)InMemDBCommandPushTrigger.java	1.0  08/25/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.datapush;

import java.nio.channels.SelectionKey;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.creaway.inmemdb.InMemSession;
import com.creaway.inmemdb.api.datapush.PushActionListener;
import com.creaway.inmemdb.api.datapush.RemotePushActionListener;
import com.creaway.inmemdb.api.datapush.TmpRegisteredListenerObj;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.trigger.InMemDBTrigger;
import com.creaway.inmemdb.util.DBLogger;
import com.creaway.inmemdb.util.Functions;

/**
 * 连入数据库的命令注册器，可以是监听对象PushActionListener，也可以是远程Socket连接（这里实现的是NIO SelectionKey）
 * 
 * @author Qil.Wong
 * 
 */
public abstract class InMemDBCommandPushTrigger extends InMemDBTrigger {

	/**
	 * 本地注册的推送监听
	 */
	protected List<PushActionListener> pals = new CopyOnWriteArrayList<PushActionListener>();

	/**
	 * 远程注册的推送监听
	 */
	protected List<RemoteActionListerObj> remoteListeners = new CopyOnWriteArrayList<RemoteActionListerObj>();

	protected int command;

	public InMemDBCommandPushTrigger() {

	}

	public void init(Connection conn, String schemaName, String triggerName,
			String tableName, boolean before, int type) {
		super.init(conn, schemaName, triggerName, tableName, before, type);
		Document triggerDoc = Functions.getXMLDocument(getClass().getResource(
				"/resources/triggers.xml"));

		Element triggerDef = Functions.findElement("//*/*[@name='"
				+ triggerName.toUpperCase() + "']",
				triggerDoc.getDocumentElement());
		command = Integer.parseInt(triggerDef.getAttribute("command"));
		InMemDBServer.getInstance().getPushService()
				.addCommandTrigger(command, type, this);
	}

	/**
	 * 条件符合时，触发监听事件
	 * 
	 * @param result
	 *            符合触发条件时的处理结果
	 */
	protected void firePushAction(Object result) {
		for (PushActionListener pal : pals) {
			pal.actionPerformed(result);
		}
		for (int i = remoteListeners.size() - 1; i >= 0; i--) {
			RemoteActionListerObj ro = remoteListeners.get(i);
			if (!ro.selectionKey.isValid()
					|| !ro.selectionKey.channel().isOpen()) {
				remoteListeners.remove(ro);
			} else {
				ro.remotePushListener.remoteActionPerformed(command, result,
						ro.selectionKey);
			}
		}
	}

	/**
	 * 是否符合触发条件，不符合返回null，符合返回计算结果
	 * 
	 * @param session
	 * @param newRow
	 * @param oldRow
	 * @return
	 */
	public abstract Object isSatisfied(InMemSession session, Object[] oldRow,
			Object[] newRow);

	public void fireTrigger(final InMemSession session, final Object[] oldRow,
			final Object[] newRow) throws SQLException {
		InMemDBServer.getInstance().getThreadPool().execute(new Runnable() {
			public void run() {
				Object result = isSatisfied(session, oldRow, newRow);
				if (result != null) {
					firePushAction(result);
				}
			}
		});		
	}

	// 内部使用，不建议外部调用
	public void addPushActionListener(PushActionListener pal) {
		pals.add(pal);
	}

	// 内部使用，不建议外部调用
	public void removePushActionListener(PushActionListener pal) {
		pals.remove(pal);
	}

	// 内部使用，不建议外部调用
	public void addRemoteSocketChannelListener(SelectionKey key,
			RemotePushActionListener l) {
		remoteListeners.add(new RemoteActionListerObj(key, l));
	}

	// 内部使用，不建议外部调用
	public void removeRemoteSocketChannelListener(SelectionKey key,
			RemotePushActionListener l) {
		for (RemoteActionListerObj o : remoteListeners) {
			if (o.remotePushListener == l
					&& o.selectionKey.channel().toString()
							.equals(key.channel().toString())) {
				remoteListeners.remove(o);
				break;
			}
		}
	}

	public void remove() throws SQLException {
		InMemDBServer.getInstance().getPushService()
				.removeCommandTrigger(command, type);
		final List<TmpRegisteredListenerObj> listeners = InMemDBServer
				.getInstance().getPushService().getTempRegisteredListeners();
		listeners.clear();
		for (PushActionListener l : pals) {
			TmpRegisteredListenerObj o = new TmpRegisteredListenerObj();
			o.command = command;
			o.triggerType = type;
			o.lisObj = l;
			listeners.add(o);
		}
		for (RemoteActionListerObj remote : remoteListeners) {
			TmpRegisteredListenerObj o = new TmpRegisteredListenerObj();
			o.command = command;
			o.triggerType = type;
			o.lisObj = remote;
			listeners.add(o);
		}

	}

	public void close() throws SQLException {
		remove();
	}

	/**
	 * RemoteAction触发对象bean，是一个SelectionKey和RemotePushActionListener的集合
	 * 
	 * @author Qil.Wong
	 * 
	 */
	public static class RemoteActionListerObj {
		public SelectionKey selectionKey;
		public RemotePushActionListener remotePushListener;

		private RemoteActionListerObj(SelectionKey k, RemotePushActionListener l) {
			if (k == null) {
				DBLogger.log(DBLogger.ERROR, "SelectionKey不能为空",
						new NullPointerException("SelectionKey不能为空"));
			} else if (l == null) {
				DBLogger.log(
						DBLogger.ERROR,
						"RemotePushActionListener不能为空",
						new NullPointerException("RemotePushActionListener不能为空"));
			} else {
				this.selectionKey = k;
				this.remotePushListener = l;
			}
		}
	}

}
