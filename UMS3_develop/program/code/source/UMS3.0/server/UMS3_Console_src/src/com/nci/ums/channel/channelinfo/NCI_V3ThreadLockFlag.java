package com.nci.ums.channel.channelinfo;

public class NCI_V3ThreadLockFlag extends LockFlag {

	static NCI_V3ThreadLockFlag flag;

	private NCI_V3ThreadLockFlag() {
		super();
	}
	
	public static NCI_V3ThreadLockFlag getInstance(){
		if(flag == null){
			flag = new NCI_V3ThreadLockFlag();
		}
		return flag;
	}

}
