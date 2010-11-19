/*
 * @(#)ProcessHandler.java	1.0  09/20/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.remote.action;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;

import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.remote.RemoteTcpServer;
import com.creaway.inmemdb.remote.client.UserMessage;
import com.creaway.inmemdb.util.DBLogger;
import com.creaway.inmemdb.util.Functions;
import com.creaway.inmemdb.util.IOToolkit;

/**
 * 远程数据处理如后，分配各种格式和命令
 * 
 * @author Qil.Wong
 * 
 */
public class ProcessHandler implements Runnable {

	/**
	 * 协议头
	 */
	private ByteBuffer headBuffer;

	/**
	 * 未定义或结尾流的缓存对象
	 */
	private ByteBuffer tempBuffer;

	private ByteBuffer heartBeatBuffer;

	/**
	 * 当前处理的选择器，对应相关SocketChannel
	 */
	private SelectionKey key;

	/**
	 * 远程NIO服务器
	 */
	private RemoteTcpServer nioServer;

	public ProcessHandler(RemoteTcpServer server, SelectionKey key) {
		this.key = key;
		this.nioServer = server;
	}

	public void run() {
		SocketChannel sc = (SocketChannel) key.channel();
		boolean valid = true;
		while (valid) {
			try {
				if (headBuffer == null) {
					headBuffer = ByteBuffer.allocate(1);
				}
				headBuffer.clear();
				int r = sc.read(headBuffer);
				if (r == 0) {
					break;
				}
				if (r == -1) {
					DBLogger.log(DBLogger.INFO, "连接断开" + sc);
					key.cancel();
					break;
				}
				headBuffer.flip();
				byte[] first = new byte[r];
				headBuffer.get(first, 0, r);
				switch (first[0]) {
				case UserMessage.HEAD_LOGIN:
					login(key);
					break;
				case UserMessage.HEAD_LOGOUT:
					logout(key);
					break;
				case UserMessage.HEAD_HEARTBEAT:
					sendHeartBeat(sc);
					break;// 服务端心跳不需做任何动作
				case UserMessage.HEAD_DATA:
					if (nioServer.isChannelLogin(key)) {
						handleData(key);
					} else {
						readUnused(sc);
					}
					break;
				default:// 未定义的或结尾的，统一不做处理
					readUnused(sc);
					break;
				}
			} catch (IOException e) {
				DBLogger.log(DBLogger.INFO, e.getMessage() + ", 连接断开" + sc);
				valid = false;
				try {
					key.channel().close();
				} catch (Exception ex) {
				}
				key.cancel();
			}
		}
	}

	/**
	 * 读取无用数据
	 * 
	 * @param sc
	 * @throws IOException
	 */
	private void readUnused(SocketChannel sc) throws IOException {
		if (tempBuffer == null) {
			tempBuffer = ByteBuffer.allocate(4096);
		}
		tempBuffer.clear();
		sc.read(tempBuffer);
		tempBuffer.flip();
	}

	private void sendHeartBeat(SocketChannel sc) throws IOException {
		if (heartBeatBuffer == null) {
			heartBeatBuffer = ByteBuffer
					.wrap(new byte[] { UserMessage.HEAD_HEARTBEAT });
		}
		sc.write(heartBeatBuffer);
	}

	/**
	 * 开始登陆
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void login(SelectionKey key) throws IOException {
		SocketChannel sc = (SocketChannel) key.channel();
		ByteBuffer pswMd5Buffer = ByteBuffer.allocate(32); // 密码在头32字节
		ByteBuffer userLengthBuffer = ByteBuffer.allocate(1); // 用户名长度
		IOToolkit.nioReadFull(sc, userLengthBuffer);
		userLengthBuffer.flip();
		int userNameLen = Functions.bytes2Int(userLengthBuffer.array());
		ByteBuffer userNameBuffer = ByteBuffer.allocate(userNameLen);
		IOToolkit.nioReadFull(sc, userNameBuffer);
		userNameBuffer.flip();
		String userName = new String(userNameBuffer.array());
		IOToolkit.nioReadFull(sc, pswMd5Buffer);
		pswMd5Buffer.flip();
		String psw = new String(pswMd5Buffer.array());
		if (nioServer.checkUserLogin(userName, psw)) {
			@SuppressWarnings("unchecked")
			Map<Object, Object> att = (Map<Object, Object>) key.attachment();
			att.put(RemoteTcpServer.LOGIN_ATTACH_KEY, "true");
			ByteBuffer login_out_OK = ByteBuffer.wrap(new byte[] {
					UserMessage.HEAD_LOGIN_RESULT, UserMessage.LOGIN_OUT_OK });

			IOToolkit.nioCompleteWrite(sc, login_out_OK);
			DBLogger.log(DBLogger.INFO, "登陆成功 " + sc);
		} else {
			ByteBuffer login_out_Failed = ByteBuffer.wrap(new byte[] {
					UserMessage.HEAD_LOGIN_RESULT,
					UserMessage.LOGIN_OUT_FAILED_AUTH });
			IOToolkit.nioCompleteWrite(sc, login_out_Failed);
			DBLogger.log(DBLogger.INFO, "登陆失败 " + sc);
		}
	}

	/**
	 * 注销已登陆状态
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void logout(SelectionKey key) throws IOException {
		@SuppressWarnings("unchecked")
		Map<Object, Object> att = (Map<Object, Object>) key.attachment();
		att.put(RemoteTcpServer.LOGIN_ATTACH_KEY, "false");
		SocketChannel sc = (SocketChannel) key.channel();
		ByteBuffer login_out_OK = ByteBuffer.wrap(new byte[] {
				UserMessage.HEAD_LOGIN_RESULT, UserMessage.LOGIN_OUT_OK });
		IOToolkit.nioCompleteWrite(sc, login_out_OK);
		DBLogger.log(DBLogger.INFO, "logout:   " + sc);
	}

	/**
	 * 处理业务数据
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void handleData(final SelectionKey key) throws IOException {
		// 头（0x71），功能标记（2字节），命令序号(2字节），
		// Session ID（4字节），数据长度（4字节），行数据集合。
		try {
			ByteBuffer commandBuffer = ByteBuffer.allocate(2);
			ByteBuffer serialBuffer = ByteBuffer.allocate(2);
			ByteBuffer sessionIDBuffer = ByteBuffer.allocate(4);
			ByteBuffer lenBuffer = ByteBuffer.allocate(4);

			SocketChannel sc = (SocketChannel) key.channel();
			// 开始读取数据，依次是命令、序号、session号、数据长度
			IOToolkit.nioReadFull(sc, commandBuffer);
			IOToolkit.nioReadFull(sc, serialBuffer);
			IOToolkit.nioReadFull(sc, sessionIDBuffer);
			IOToolkit.nioReadFull(sc, lenBuffer);

			final int command = Functions.bytes2Int(commandBuffer.array());
			final int serialNO = Functions.bytes2Int(serialBuffer.array());
			final int sessionID = Functions.bytes2Int(sessionIDBuffer.array());
			int dataLength = Functions.bytes2Int(lenBuffer.array());
			DBLogger.log(DBLogger.DEBUG, "command: " + command + "  serialNO: "
					+ serialNO + "  sessionID: " + sessionID + "  dataLength: "
					+ dataLength);
			final ByteBuffer dataBuffer = ByteBuffer.allocate(dataLength);
			// 开始读取自定义数据
			IOToolkit.nioReadFull(sc, dataBuffer);
			dataBuffer.flip();
			final Processor<?> p = nioServer.getProcessors().get(command);
			if (p != null) {
				// p.go(serialNO, sessionID, dataBuffer.array(), key);
				// 数据处理比较耗时，推送到线程池中工作
				Runnable run = new Runnable() {
					public void run() {
						p.go(serialNO, sessionID, dataBuffer.array(), key);
					}
				};
				InMemDBServer.getInstance().getThreadPool().execute(run);
			} else {
				DBLogger.log(DBLogger.ERROR, "命令\"" + command + "\"还未定义处理类");
			}
		} catch (IOException e) {
			DBLogger.log(DBLogger.ERROR, "", e);
		}
	}

}
