/*
 * @(#)Processor.java	1.0  09/21/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.remote.action;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.jgroups.annotations.Immutable;

import com.creaway.inmemdb.remote.client.UserMessage;
import com.creaway.inmemdb.util.IOToolkit;
import com.creaway.inmemdb.util.ProtocolUtils;

/**
 * 自定义协议功能命令处理对象，每个Processor对象负责处理1个或多个功能命令。
 * 多线程处理模式，类似Servlet，因此必须保证当前的Processor子类的各种处理函数线程安全
 * 
 * @author Qil.Wong
 * 
 * @param T
 *            中间过程产生对象
 */

public abstract class Processor<T> {

	/**
	 * 命令处理器的命令号
	 */
	private int command;

	public Processor() {

	}

	/**
	 * 开始处理数据
	 * 
	 * @param serialNO
	 * @param sessionID
	 * @param data
	 * @param key
	 */
	public void go(int serialNO, int sessionID, byte[] data, SelectionKey key) {
		done(key, process(serialNO, sessionID, data, key), serialNO, sessionID);
	}

	/**
	 * 完成后的动作
	 * 
	 * @param key
	 *            连接通道的注册对象SelectionKey
	 * @param result
	 *            中间产生对象，由process方法传递过来
	 * @param serialNO
	 *            远程操作的序号
	 * @param sessionID
	 *            远程操作SessionID，用作事务扩充，暂不支持
	 */
	protected abstract void done(SelectionKey key, T result, int serialNO,
			int sessionID);

	/**
	 * 数据处理的具体执行
	 * 
	 * @param serialNO
	 * @param sessionID
	 * @param data
	 * @param key
	 * @return
	 */
	protected abstract T process(int serialNO, int sessionID, byte[] data,
			SelectionKey key);

	/**
	 * 获取当前处理对象对应的命令
	 * 
	 * @return
	 */
	public int getCommand() {
		return command;
	}

	/**
	 * 设置当前处理对象对应的命令
	 * 
	 * @param command
	 */
	public void setCommand(int command) {
		this.command = command;
	}

	/**
	 * 发送数据到客户端
	 * 
	 * @param key
	 * @param result
	 * @param serialNO
	 * @param sessionID
	 * @throws IOException
	 */
	protected void sendResult(boolean error, SelectionKey key, byte[] result,
			int serialNO, int sessionID) throws IOException {

		SocketChannel sc = (SocketChannel) key.channel();
		byte[] commandB = ProtocolUtils.getCommand(getCommand());
		byte[] serialB = ProtocolUtils.getSerial(serialNO);
		byte[] sessionIdB = ProtocolUtils.getSessionID(sessionID);
		ByteBuffer bb = null;
		if (!error) {
			// // 1是头，4是数据长度
			bb = ByteBuffer.allocate(1 + commandB.length + serialB.length
					+ sessionIdB.length + 4 + result.length);
			bb.put(UserMessage.HEAD_DATA);
			bb.put(commandB);
			bb.put(serialB);
			bb.put(sessionIdB);
			bb.put(ProtocolUtils.getDataLength(result.length));
			bb.put(result); // 如果是错误信息，则result是错误码
		} else {
			bb = ByteBuffer.allocate(7);// 头，2字节command，2字节serial,2字节errorcode
			bb.put(UserMessage.HEAD_ERROR);
			bb.put(commandB);
			bb.put(serialB);
			bb.put(result);
		}
		bb.flip();
		IOToolkit.nioCompleteWrite(sc, bb);
	}
}
