package com.nci.ums.channel.channelinfo;

public class NCI_V3DataLockFlag extends LockFlag{

	static NCI_V3DataLockFlag flag;

	private NCI_V3DataLockFlag() {
		super();
	}
	
	public static NCI_V3DataLockFlag getInstance(){
		if(flag == null){
			flag = new NCI_V3DataLockFlag();
		}
		return flag;
	}
}
