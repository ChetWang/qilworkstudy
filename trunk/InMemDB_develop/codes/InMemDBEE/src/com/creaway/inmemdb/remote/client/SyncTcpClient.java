/*
 * @(#)SyncTcpClient.java	1.0  10/08/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.remote.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.sql.RowSet;

import com.creaway.inmemdb.util.Functions;
import com.creaway.inmemdb.util.IOToolkit;
import com.creaway.inmemdb.util.ProtocolUtils;
import com.sun.rowset.WebRowSetImpl;

/**
 * 同步、阻塞TCP方式客户端接入的Java实现
 * 
 * @author Qil.Wong
 * 
 */
public class SyncTcpClient extends TcpClient {

	public SyncTcpClient() {

	}

	public UserMessage sendUserMessage(UserMessage message) throws IOException {
		if (!connected) {
			throw new ClientNotConnectedException();
		}
		UserMessage result = new UserMessage();
		synchronized (syncLock) {
			os.write(getUserMessageData(message));
			byte[] head = new byte[1];
			IOToolkit.readFull(is, head);
			if (head[0] == UserMessage.HEAD_ERROR) {
				dealError();
				return null;
			} else {
				byte[] data = new byte[13];
				byte[] temp = new byte[12];
				data[0] = head[0];
				IOToolkit.readFull(is, temp);
				System.arraycopy(temp, 0, data, 1, temp.length);
				result.setCommand(ProtocolUtils.getCommand(data));
				result.setHead(ProtocolUtils.getHead(data));
				result.setSerial(ProtocolUtils.getSerial(data));
				result.setSessionId(ProtocolUtils.getSessionID(data));
				int len = ProtocolUtils.getDataLength(data);
				byte[] userObj = new byte[len];
				System.out.println("userObject len=" + len);
				IOToolkit.readFull(is, userObj);
				result.setUserObject(userObj);
			}
		}
		return result;
	}

	/**
	 * 发送心跳连接，保持连接有效性
	 */
	protected void heartBeat() {
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

	public RowSet query(String sql) throws IOException, SQLException {
		UserMessage message = new UserMessage();
		message.setCommand(2001); // 系统设定查询命令2001
		message.setSerial(0);
		message.setSessionId(0);
		message.setUserObject(sql.getBytes());
		UserMessage result = sendUserMessage(message);
		if (result != null) {
			WebRowSetImpl rowset = new WebRowSetImpl();
			InputStream ins = new ByteArrayInputStream(result.getUserObject());
			rowset.readXml(ins); // 这一步并发执行效率比较受影响
			ins.close();
			return rowset;
		}
		return null;
	}

	/**
	 * 更新操作
	 * 
	 * @param sql
	 * @return
	 * @throws IOException
	 */
	public int update(String sql) throws IOException {
		UserMessage message = new UserMessage();
		message.setCommand(2002); // 系统设定更新/删除命令2002
		message.setSerial(0);
		message.setSessionId(0);
		message.setUserObject(sql.getBytes());
		UserMessage result = sendUserMessage(message);
		if (result != null) {
			return Functions.bytes2Int(result.getUserObject());
		} else {
			return -1;
		}
	}

	/**
	 * 插入数据操作
	 * 
	 * @param sql
	 * @return
	 * @throws IOException
	 */
	public boolean insert(String sql) throws IOException {
		UserMessage message = new UserMessage();
		message.setCommand(2003); // 系统设定插入命令2003
		message.setSerial(0);
		message.setSessionId(0);
		message.setUserObject(sql.getBytes());
		UserMessage result = sendUserMessage(message);
		if (result != null) {
			int r = Functions.bytes2Int(result.getUserObject());
			if (r == 1) { // 1是成功
				return true;
			} else if (r == 2) {// 2是失败
				return false;
			}
		}
		return false;
	}

}
