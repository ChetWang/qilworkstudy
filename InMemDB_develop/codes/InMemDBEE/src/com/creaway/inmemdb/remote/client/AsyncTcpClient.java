/*
 * @(#)AnsyncTcpClient.java	1.0  10/08/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.remote.client;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import com.creaway.inmemdb.util.Functions;
import com.creaway.inmemdb.util.IOToolkit;
import com.creaway.inmemdb.util.ProtocolUtils;

/**
 * 异步TCP交互客户端，通常用作服务器数据推送
 * 
 * @author Qil.Wong
 * 
 */
public class AsyncTcpClient extends TcpClient {

	/**
	 * 异步线程有效标记
	 */
	private boolean flag = true;

	/**
	 * 发送数据队列
	 */
	private BlockingQueue<UserMessage> buffer = new LinkedBlockingQueue<UserMessage>();

	/**
	 * 异步监听器
	 */
	private List<AsyncDataListener> ansyncListeners = new CopyOnWriteArrayList<AsyncDataListener>();

	//接收线程、外发线程
	private Thread inThread, outThread;

	public boolean connect(String ip, int port, String user, String password)
			throws IOException, IllegalClientException {
		boolean result = super.connect(ip, port, user, password);
		if (result) {
			flag = true;
			inThread = new InThread();
			outThread = new OutThread();
			inThread.start();
			outThread.start();
			// 重连时，需要将已有的异步。命令重新注册一遍
			for (AsyncDataListener l : ansyncListeners) {
				addAnsyncDataListener(l);
			}
		}
		return result;
	}

	@Override
	protected void heartBeat() {
		if (buffer.size() == 0) {
			try {
				synchronized (syncLock) {
					os.write(UserMessage.HEAD_HEARTBEAT);
				}
			} catch (IOException e) {
				try {
					close(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void addAnsyncDataListener(AsyncDataListener lis)
			throws IOException {
		if (!connected) {
			throw new ClientNotConnectedException();
		}
		ansyncListeners.add(lis);
		UserMessage message = new UserMessage();
		message.setCommand(lis.getCommand());
		message.setSerial(-1);// serial为-1默认表示注册，第三方接入也可以自定义实现
		message.setUserObject(lis.getUserObj());
		buffer.add(message);
	}

	public void removeAnsyncDataListener(AsyncDataListener lis)
			throws IOException {
		if (!connected) {
			throw new ClientNotConnectedException();
		}
		ansyncListeners.remove(lis);
		UserMessage message = new UserMessage();
		message.setCommand(lis.getCommand());
		message.setSerial(-2);// serial为-2默认表示卸载指定命令的监听，第三方接入也可以自定义实现
		message.setUserObject(lis.getUserObj());
		buffer.add(message);
	}

	/**
	 * 触发数据收到事件
	 * 
	 * @param command
	 * @param data
	 */
	protected void fireAnsyncDataReceived(int command, byte[] data) {
		for (AsyncDataListener l : ansyncListeners) {
			if (l.getCommand() == command) {
				l.dataRecieved(data);
			}
		}
	}

	public void close(boolean exception) throws IOException,
			IllegalClientException {
		flag = false;
		if (!exception) {
			ansyncListeners.clear();
			buffer.clear();
		}
		super.close(exception);
	}

	protected void onError(int command, int errorCode) {
		logger.log(Level.SEVERE, "ERROR：command=" + command + ", code="
				+ errorCode);
	}

	private class InThread extends Thread {
		public void run() {
			while (flag) {
				try {
					byte headB = is.readByte();
					if (headB == UserMessage.HEAD_DATA) { // 只识别HEAD_DATA开始的数据
						byte[] front12 = new byte[12];// 除了head外的头12个字节
						IOToolkit.readFull(is, front12);

						byte[] front = new byte[13];
						front[0] = headB;
						System.arraycopy(front12, 0, front, 1, front12.length);
						int command = ProtocolUtils.getCommand(front);
						int dataLen = ProtocolUtils.getDataLength(front);
						byte[] data = new byte[dataLen];
						IOToolkit.readFull(is, data);
						fireAnsyncDataReceived(command, data);
					} else if (headB == UserMessage.HEAD_ERROR) {
						// 2字节命令，2字节错误码
						byte[] command = new byte[2];
						IOToolkit.readFull(is, command);
						byte[] error = new byte[2];
						IOToolkit.readFull(is, error);
						onError(Functions.bytes2Int(command),
								Functions.bytes2Int(error));
					}
				} catch (IOException e) {
					logger.log(Level.SEVERE, "", e);
					try {
						close(true);
					} catch (IOException e1) {						
						e1.printStackTrace();
					} catch (IllegalClientException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

	}

	private class OutThread extends Thread {
		public void run() {
			while (flag) {
				try {
					UserMessage message = buffer.take();
					synchronized (syncLock) {
						System.out.println(message);
						os.write(message.getHead());
						os.write(ProtocolUtils.getCommand(message.getCommand()));// 功能标记（2字节）
						os.write(ProtocolUtils.getSerial(message.getSerial()));// 命令序号（2字节）
						os.write(ProtocolUtils.getSessionID(message
								.getSessionId()));// Session ID（4字节）

						os.write(ProtocolUtils.getDataLength(message
								.getUserObject().length));// 数据长度（4字节）
						os.write(message.getUserObject());
						
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					try {
						close(true);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (IllegalClientException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

}
