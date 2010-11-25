/*
 * @(#)UserMessage.java	1.0  10/08/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.remote.client;

/**
 * 用户数据Java Bean
 * 
 * @author Qil.Wong
 * 
 */
public class UserMessage {

	/**
	 * 登陆报文头
	 */
	public static final byte HEAD_LOGIN = 0x79;

	/**
	 * 注销报文头
	 */
	public static final byte HEAD_LOGOUT = 0x78;

	/**
	 * 登陆返回报文头
	 */
	public static final byte HEAD_LOGIN_RESULT = 0x77;

	/**
	 * 心跳报文头
	 */
	public static final byte HEAD_HEARTBEAT = 0x66;

	/**
	 * 错误数据报文头
	 */
	public static final byte HEAD_ERROR = 0x72;

	/**
	 * 用户数据报文头
	 */
	public static final byte HEAD_DATA = 0x71;

	/**
	 * 登陆/注销成功标记
	 */
	public static final byte LOGIN_OUT_OK = 0x01;

	/**
	 * 登陆/注销失败标记
	 */
	public static final byte LOGIN_OUT_FAILED_AUTH = 0x02;

	/**
	 * 非法的ip登录
	 */
	public static final byte LOGIN_OUT_FAILED_ILLEGAL_IP = 0x03;

	/**
	 * 用户数据起始符
	 */
	private int head = HEAD_DATA;
	/**
	 * 用户数据命令
	 */
	private int command;

	/**
	 * 用户数据操作序号
	 */
	private int serial;
	/**
	 * session id
	 */
	private int sessionId;

	/**
	 * 用户数据结果对象
	 */
	private byte[] userObject;

	public int getHead() {
		return head;
	}

	public void setHead(int head) {
		this.head = head;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public byte[] getUserObject() {
		if (userObject == null)
			return new byte[0];
		return userObject;
	}

	public void setUserObject(byte[] userObject) {
		this.userObject = userObject;
	}

	public String toString() {
		int len = userObject==null?-1:userObject.length;
		return "Head:" + head + ", command=" + command + ", serial=" + serial
				+ ", sessionId=" + sessionId + ", userObj len="
				+ len;
	}
}
