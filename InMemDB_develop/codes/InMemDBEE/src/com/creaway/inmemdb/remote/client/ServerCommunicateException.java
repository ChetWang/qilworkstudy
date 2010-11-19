/*
 * @(#)ServerCommunicateException.java	1.0  10/08/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.remote.client;

/**
 * 服务端数据交互异常
 * 
 * @author Qil.Wong
 * 
 */
public class ServerCommunicateException extends Exception {

	public static final int ILLEGAL_IP = 0;
	public static final int ILLEGAL_USER_PASSWORD = 1;

	public ServerCommunicateException(int errorCode, String errorMsg) {

		super("数据处理错误！错误号" + errorCode + "，" + errorMsg);
	}
}
