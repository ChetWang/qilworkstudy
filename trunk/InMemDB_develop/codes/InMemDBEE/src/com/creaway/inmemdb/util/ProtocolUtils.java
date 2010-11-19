/*
 * @(#)ProtocolUtils.java	1.0  09/20/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.util;

import java.io.InputStream;
import java.util.Properties;

import com.creaway.inmemdb.remote.client.ServerCommunicateException;
import com.creaway.inmemdb.remote.client.UserMessage;

/**
 * 协议解析和包装的工具类
 * 
 * @author Qil.Wong
 * 
 */
public class ProtocolUtils {

	/**
	 * 将整数命令转成2字节数组
	 * 
	 * @param command
	 * @return
	 */
	public static byte[] getCommand(int command) {
		return intTo2Bytes(command, "功能标记");
	}

	/**
	 * 将命令序号转成2个字节数组
	 * 
	 * @param serial
	 * @return
	 */
	public static byte[] getSerial(int serial) {
		return intTo2Bytes(serial, "命令序号");
	}

	/**
	 * 将事务id转成4字节数组
	 * 
	 * @param sessionId
	 * @return
	 */
	public static byte[] getSessionID(int sessionId) {
		return intTo4Bytes(sessionId, "事务Session ID");
	}

	/**
	 * 将数据长度转成4字节数组
	 * 
	 * @param dataLen
	 * @return
	 */
	public static byte[] getDataLength(int dataLen) {
		return intTo4Bytes(dataLen, "数据长度");
	}

	private static byte[] intTo4Bytes(int i, String name) {
		byte[] commandB = null;
		byte[] b = Functions.int2Bytes(i);
		if (b.length == 1) {
			commandB = new byte[4]; // 命令是4字节长度
			commandB[0] = 0;
			commandB[1] = 0;
			commandB[2] = 0;
			commandB[3] = b[0];
		} else if (b.length == 2) {
			commandB = new byte[4]; // 命令是4字节长度
			commandB[0] = 0;
			commandB[1] = 0;
			commandB[2] = b[0];
			commandB[3] = b[1];
		} else if (b.length == 3) {
			commandB = new byte[4]; // 命令是4字节长度
			commandB[0] = 0;
			commandB[1] = b[0];
			commandB[2] = b[1];
			commandB[3] = b[2];
		} else if (b.length == 4) {
			commandB = b;
		} else {
			throw new IllegalArgumentException(name + "值\"" + i + "\"已经超出2字节范围");
		}
		return commandB;
	}

	/**
	 * 将整数转成2个字节的数组
	 * 
	 * @param i
	 * @param name
	 * @return
	 */
	private  static byte[] intTo2Bytes(int i, String name) {
		byte[] commandB = null;
		byte[] b = Functions.int2Bytes(i);
		if (b.length == 1) {
			commandB = new byte[2]; // 命令是2字节长度
			commandB[0] = 0;
			commandB[1] = b[0];
		} else if (b.length == 2) {
			commandB = b;
		} else {
			throw new IllegalArgumentException(name + "值\"" + i + "\"已经超出2字节范围");
		}
		return commandB;
	}

	/**
	 * 解析协议，获取协议头
	 * 
	 * @param b
	 * @return
	 */
	public static byte getHead(byte[] b) {
		return b[0];
	}

	/**
	 * 解析协议，获取命令
	 * 
	 * @param b
	 * @return
	 */
	public static int getCommand(byte[] b) { // 2字节
		return Functions.bytes2Int(new byte[] { b[1], b[2] });
	}

	/**
	 * 解析协议，获取序号
	 * 
	 * @param b
	 * @return
	 */
	public static int getSerial(byte[] b) {
		return Functions.bytes2Int(new byte[] { b[3], b[4] });
	}

	/**
	 * 解析协议，获取session id
	 * 
	 * @param b
	 * @return
	 */
	public static int getSessionID(byte[] b) {
		return Functions.bytes2Int(new byte[] { b[5], b[6], b[7], b[8] });
	}

	/**
	 * 解析协议，获取数据长度
	 * 
	 * @param b
	 * @return
	 */
	public static int getDataLength(byte[] b) {
		return Functions.bytes2Int(new byte[] { b[9], b[10], b[11], b[12] });
	}

	public static byte[] getLoginData(String user, String password) {
		byte[] nameLen = Functions.int2Bytes(user.getBytes().length);
		byte[] userByte = user.getBytes();
		byte[] b = new byte[1 + 1 + userByte.length + 32];
		b[0] = UserMessage.HEAD_LOGIN;
		b[1] = nameLen[0];
		System.arraycopy(userByte, 0, b, 2, userByte.length);
		String pswMD5 = Functions.md5(password);
		byte[] pswByte = pswMD5.getBytes();
		System.arraycopy(pswByte, 0, b, 2 + userByte.length, pswByte.length);
		return b;
	}
	
	public static byte[] getErrorCode(int code){
		return intTo2Bytes(code, "error code");
	}
	
	

}
