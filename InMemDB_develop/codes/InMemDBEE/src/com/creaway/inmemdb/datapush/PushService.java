/*
 * @(#)PushService.java	1.0  09/20/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.datapush;

import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.creaway.inmemdb.api.datapush.PushActionListener;
import com.creaway.inmemdb.api.datapush.PushServiceIfc;
import com.creaway.inmemdb.api.datapush.RemotePushActionListener;
import com.creaway.inmemdb.api.datapush.TmpRegisteredListenerObj;
import com.creaway.inmemdb.util.DBLogger;

/**
 * 数据推送机制处理器
 * 
 * @author Qil.Wong
 * 
 */
public class PushService implements PushServiceIfc {

	/**
	 * 推送方式的监听，事先定义一系列规则，如果数据库的触发条件符合该规则，则进行处理，并将结果推送给定义端
	 * key是command+“-”+type组合，
	 * type是触发的类型（代表INSERT,UPDATE,DELETE，见com.creaway.inmemdb
	 * .trigger.InMemDBTrigger)
	 */
	private Map<String, InMemDBCommandPushTrigger> pushTriggers = new ConcurrentHashMap<String, InMemDBCommandPushTrigger>();

	/**
	 * 定时有效性监视器
	 */
	private Timer remoteChannelTimer;

	private List<TmpRegisteredListenerObj> tempRegisteredListeners = new CopyOnWriteArrayList<TmpRegisteredListenerObj>();

	public PushService() {
		remoteChannelTimer = new Timer(
				"Registered Remote push-channels validator", true);
		remoteChannelTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				checkRegisteredRemoteChannels();
			}
		}, 0, 30000);
	}

	/**
	 * 检查远程推送连接的有效性
	 */
	private void checkRegisteredRemoteChannels() {
		Iterator<String> commandPusher = pushTriggers.keySet().iterator();
		while (commandPusher.hasNext()) {
			String command_type = commandPusher.next();
			InMemDBCommandPushTrigger tri = pushTriggers.get(command_type);
			for (int i = tri.remoteListeners.size() - 1; i >= 0; i--) {
				SelectionKey key = tri.remoteListeners.get(i).selectionKey;
				if (!key.isValid() || !key.channel().isOpen()) {
					DBLogger.log(DBLogger.INFO, "移除无效的远程推送监听:" + key.channel());
					tri.remoteListeners.remove(i);
				}
			}
		}
	}

	@Override
	public boolean addPushCommandListener(int command, int type,
			PushActionListener pal) {
		InMemDBCommandPushTrigger trigger = pushTriggers.get(command + "-"
				+ type);
		if (trigger == null) {
			String err = "没有找到指定命令" + command + "对应的触发对象";
			DBLogger.log(DBLogger.ERROR, err, new Exception(err));
			return false;
		} else {
			trigger.addPushActionListener(pal);
			return true;
		}
	}

	/**
	 * 内部使用
	 */
	@Override
	public void addCommandTrigger(int command, int triggerType,
			InMemDBCommandPushTrigger trigger) {
		pushTriggers.put(
				String.valueOf(command) + "-" + String.valueOf(triggerType),
				trigger);
	}

	/**
	 * 内部使用
	 */
	@Override
	public void removeCommandTrigger(int command, int triggerType) {
		pushTriggers.remove(String.valueOf(command) + "-"
				+ String.valueOf(triggerType));
	}

	@Override
	public boolean addRemoteSocketChannelListener(int command, int triggerType,
			SelectionKey key, RemotePushActionListener l) {
		InMemDBCommandPushTrigger trigger = pushTriggers.get(command + "-"
				+ triggerType);
		if (trigger == null) {
			String err = "没有找到指定命令--" + command + "对应的触发对象类型--" + triggerType;
			DBLogger.log(DBLogger.ERROR, err, new Exception(err));
			return false;
		} else {
			trigger.addRemoteSocketChannelListener(key, l);
			return true;
		}
	}

	public void close() {
		pushTriggers.clear();
		remoteChannelTimer.cancel();
	}

	@Override
	public void removePushCommandListener(int command, int triggerType,
			PushActionListener pal) {
		InMemDBCommandPushTrigger trigger = pushTriggers.get(command + "-"
				+ triggerType);
		if (trigger == null) {
			String err = "没有找到指定命令" + command + "对应的触发对象";
			DBLogger.log(DBLogger.ERROR, err, new Exception(err));
		} else {
			trigger.removePushActionListener(pal);
		}
	}

	@Override
	public void removeRemoteSocketChannelListener(int command, int triggerType,
			SelectionKey key, RemotePushActionListener l) {
		InMemDBCommandPushTrigger trigger = pushTriggers.get(command + "-"
				+ triggerType);
		if (trigger == null) {
			String err = "没有找到指定命令" + command + "对应的触发对象";
			DBLogger.log(DBLogger.ERROR, err, new Exception(err));
		} else {
			trigger.removeRemoteSocketChannelListener(key, l);
		}
	}

	public List<TmpRegisteredListenerObj> getTempRegisteredListeners() {
		return tempRegisteredListeners;
	}
	
}
