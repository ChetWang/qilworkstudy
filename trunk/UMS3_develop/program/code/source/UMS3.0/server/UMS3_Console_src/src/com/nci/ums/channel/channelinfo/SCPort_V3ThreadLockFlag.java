package com.nci.ums.channel.channelinfo;

public class SCPort_V3ThreadLockFlag extends LockFlag {

	static SCPort_V3ThreadLockFlag flag;

	private SCPort_V3ThreadLockFlag() {
		super();
	}
	
	public static SCPort_V3ThreadLockFlag getInstance(){
		if(flag == null){
			flag = new SCPort_V3ThreadLockFlag();
		}
		return flag;
	}

}
