/*
 * @(#)ITcpClient.java	1.0  10/08/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.remote.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.creaway.inmemdb.util.Functions;
import com.creaway.inmemdb.util.IOToolkit;
import com.creaway.inmemdb.util.ProtocolUtils;

/**
 * TCP客户端对象
 * @author Qil.Wong
 *
 */
public abstract class TcpClient {

	Logger logger = Logger.getAnonymousLogger();

	protected Object syncLock = new Object();

	protected String ip, user, password;
	protected int port;

	protected Socket client;

	protected DataOutputStream os;

	protected DataInputStream is;

	protected Timer heatBeatTimer;

	protected Timer reconnectTimer;

	/**
	 * 插入操作成功标记
	 */
	public static final int INSERT_OK = 1;

	/**
	 * 插入操作失败标记
	 */
	public static final int INSERT_FAILED = 2;

	protected Properties errors = new Properties();

	protected boolean connected = false;

	public TcpClient() {
		InputStream in = ProtocolUtils.class
				.getResourceAsStream("/resources/error.conf");
		try {
			errors.clear();
			errors.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接客户端
	 * 
	 * @param ip
	 * @param port
	 * @param user
	 * @param password
	 * @return
	 * @throws IOException
	 * @throws IllegalClientException
	 */
	public boolean connect(String ip, int port, String user, String password)
			throws IOException, IllegalClientException {
		synchronized (syncLock) {
			this.ip = ip;
			this.port = port;
			this.user = user;
			this.password = password;
			try {
				client = new Socket(ip, port);
				os = new DataOutputStream(client.getOutputStream());
				is = new DataInputStream(client.getInputStream());
			} catch (IOException e) {
				throw e;
			}
			// 一旦socket建立，重连的定时器就不需要再工作
			if (reconnectTimer != null) {
				logger.log(Level.INFO, "重连成功，新连接已建立");
				reconnectTimer.cancel();
			}
			boolean result = false;
			os.write(ProtocolUtils.getLoginData(user, password));
			byte[] b = new byte[2];// 验证返回两个字节的数组
			IOToolkit.readFull(is, b);
			try {
				switch (b[1]) {
				case UserMessage.LOGIN_OUT_OK:
					result = true;
					break;
				case UserMessage.LOGIN_OUT_FAILED_AUTH:
					throw new IllegalClientException(
							IllegalClientException.ILLEGAL_USER_PASSWORD, ip,
							port, user, password);

				case UserMessage.LOGIN_OUT_FAILED_ILLEGAL_IP:
					throw new IllegalClientException(
							IllegalClientException.ILLEGAL_IP, ip, port, user,
							password);
				default:
					break;
				}
				return result;
			} finally {
				connected = result;
				if (!result) {
					close(false);
				} else {
					if (heatBeatTimer != null) {
						heatBeatTimer.cancel();
					}
					heatBeatTimer = new Timer();
					heatBeatTimer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							heartBeat();
						}
					}, 5000, 5000);
				}
			}
		}
	}

	/**
	 * 发送心跳连接，保持连接有效性
	 */
	protected abstract void heartBeat();

	/**
	 * 将定义的Message对象转化为二进制协议对象
	 * @param message
	 * @return
	 */
	public byte[] getUserMessageData(UserMessage message) {
		byte[] b = new byte[13 + message.getUserObject().length];// 前13个是固定长度，1字节头+2字节命令+2字节序号+4字节SessionID+4字节数据长度
		b[0] = (byte) message.getHead();
		byte[][] sep = new byte[5][1];
		sep[0] = ProtocolUtils.getCommand(message.getCommand());
		sep[1] = ProtocolUtils.getSerial(message.getSerial());
		sep[2] = ProtocolUtils.getSessionID(message.getSessionId());
		sep[3] = ProtocolUtils.getDataLength(message.getUserObject().length);
		sep[4] = message.getUserObject();
		int destPos = 1;
		for (int i = 0; i < sep.length; i++) {
			System.arraycopy(sep[i], 0, b, destPos, sep[i].length);
			destPos = destPos + sep[i].length;
		}
		return b;
	}

	/**
	 * 关闭
	 * 
	 * @param boolean 是否是异常断开，如果是，则需要重连
	 * @throws IOException
	 */
	public void close(boolean exception) throws IOException,
			IllegalClientException {
		if (heatBeatTimer != null) {
			heatBeatTimer.cancel();
		}
		os.close();
		is.close();
		client.close();
		client = null;
		heatBeatTimer = null;
		os = null;
		is = null;
		if (exception) {
			logger.log(Level.SEVERE, "内存数据库TCP连接断开，开始重连");
			if (reconnectTimer == null)
				reconnectTimer = new Timer();
			reconnectTimer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					try {
						logger.log(Level.INFO, "正在重连内存数据库TCP...");
						connect(ip, port, user, password);
					} catch (IOException e) {
					} catch (IllegalClientException e) {
						e.printStackTrace();
					}
				}
			}, 0, 3000);

		}
	}

	/**
	 * 处理错误
	 * 
	 * @throws IOException
	 */
	protected void dealError() throws IOException {
		byte[] error = new byte[2];
		int errorCode = Functions.bytes2Int(error);
		logger.log(Level.SEVERE, "数据交互错误", new ServerCommunicateException(
				errorCode, errors.getProperty(String.valueOf(errorCode))));
	}

}
