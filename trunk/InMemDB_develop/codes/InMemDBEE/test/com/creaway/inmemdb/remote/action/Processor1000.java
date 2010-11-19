package com.creaway.inmemdb.remote.action;

import java.nio.channels.SelectionKey;

public class Processor1000 extends Processor {

	@Override
	public Object process(int serialNO, int sessionID, byte[] data,SelectionKey key) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void done(SelectionKey key,Object result,int serialNO, int sessionID) {
		System.out.println("done");

	}

}
