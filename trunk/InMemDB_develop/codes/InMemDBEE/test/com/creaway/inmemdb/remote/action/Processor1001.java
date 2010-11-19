package com.creaway.inmemdb.remote.action;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.creaway.inmemdb.api.datapush.RemotePushActionListener;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.trigger.InMemDBTrigger;

/**
 * FOR TEST, 远程推送注册
 * 
 * @author Qil.Wong
 * 
 */
public class Processor1001 extends Processor<Object> implements
		RemotePushActionListener {

	@Override
	public Object process(int serialNO, int sessionID, byte[] data,
			SelectionKey key) {
		return null;
	}

	@Override
	public void done(SelectionKey key, Object result, int serialNO,
			int sessionID) {
		System.out.println("done");
		boolean flag1 = InMemDBServer
				.getInstance()
				.getPushService()
				.addRemoteSocketChannelListener(getCommand(),
						InMemDBTrigger.TYPE_INSERT, key, this);
		boolean flag2 = InMemDBServer
				.getInstance()
				.getPushService()
				.addRemoteSocketChannelListener(getCommand(),
						InMemDBTrigger.TYPE_UPDATE, key, this);
	}

	@Override
	public void remoteActionPerformed(int command, Object result,
			SelectionKey key) {
		try {
			sendResult(false, key, result.toString().getBytes(), 0, 0);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
