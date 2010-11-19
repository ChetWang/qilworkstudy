package com.creaway.inmemdb.remote.client;

public abstract class AsyncDataListener {

	/**
	 * 用户定义的注册推送命令
	 */
	private int command;
	
	private byte[] userObj;

	public AsyncDataListener(int command, byte[] userObj) {
		this.command = command;
		this.userObj = userObj;
	}

	public abstract void dataRecieved(byte[] data);

	public int getCommand() {
		return command;
	}
	
	public byte[] getUserObj(){
		return userObj;
	}

}
