/*
 * @(#)IllegalClientException.java	1.0  10/08/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.remote.client;

/**
 * 非法客户端接入异常
 * 
 * @author Qil.Wong
 * 
 */
public class IllegalClientException extends Exception {

	public static final int ILLEGAL_IP = 0;
	public static final int ILLEGAL_USER_PASSWORD = 1;

	/**
	 * 构造函数
	 * 
	 * @param flag
	 *            非法的类型
	 * @param ip
	 *            接入的ip
	 * @param port
	 *            接入的端口
	 * @param user
	 *            用户
	 * @param psw
	 *            密码
	 * @see ILLEGAL_IP
	 * @see ILLEGAL_USER_PASSWORD
	 */
	public IllegalClientException(int flag, String ip, int port, String user,
			String psw) {

		super(flag == ILLEGAL_IP ? "非法IP接入" + ip + ":" + port : "用户认证失败");
	}
}
