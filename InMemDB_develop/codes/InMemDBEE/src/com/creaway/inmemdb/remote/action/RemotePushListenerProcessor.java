package com.creaway.inmemdb.remote.action;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.SelectionKey;

import com.creaway.inmemdb.api.datapush.RemotePushActionListener;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.util.Functions;
import com.creaway.inmemdb.util.ProtocolUtils;

/**
 * 远程注册数据推送命令的处理器
 * 
 * @author Qil.Wong
 * 
 */
public class RemotePushListenerProcessor extends Processor<Boolean> implements
		RemotePushActionListener {

	@Override
	protected void done(SelectionKey key, Boolean result, int serialNO,
			int sessionID) {
		if (!result) {
			try {
				// 发送错误代码为5的错误信息
				sendResult(true, key, ProtocolUtils.getErrorCode(5), 0, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected Boolean process(int serialNO, int sessionID, byte[] data,
			SelectionKey key) {
		if (data == null || data.length != 3) {
			return false;
		}
		boolean[] flags = new boolean[] { true, true, true };
		// data必须是3个字节的长度，对应insert,update,delete类型，有效是指是1,2,4；0为补值
		for (int i = 0; i < data.length; i++) {
			if (data[i] != 0) {
				flags[i] = InMemDBServer
						.getInstance()
						.getPushService()
						.addRemoteSocketChannelListener(getCommand(), data[i],
								key, this);
			}
		}
		boolean flag = true;
		for (int i = 0; i < flags.length; i++) {
			flag = flags[i] & flag;
		}
		return flag;
	}

	@Override
	public void remoteActionPerformed(int command, Object result,
			SelectionKey key) {
		try {
			if (result instanceof String) {
				sendResult(false, key, result.toString().getBytes("utf-8"), 0,
						0);
			} else if (result instanceof byte[]) {
				sendResult(false, key, (byte[]) result, 0, 0);
			} else {
				// 其它对象转化成xml
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				XMLEncoder encoder = new XMLEncoder(bos);
				encoder.writeObject(result);
				encoder.close();
				sendResult(false, key, bos.toByteArray(), 0, 0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
