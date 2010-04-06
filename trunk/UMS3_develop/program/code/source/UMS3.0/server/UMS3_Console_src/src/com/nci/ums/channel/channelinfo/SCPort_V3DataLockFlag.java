package com.nci.ums.channel.channelinfo;

public class SCPort_V3DataLockFlag extends LockFlag{
	static SCPort_V3DataLockFlag flag;

	private SCPort_V3DataLockFlag() {
		super();
	}
	
	public static SCPort_V3DataLockFlag getInstance(){
		if(flag == null){
			flag = new SCPort_V3DataLockFlag();
		}
		return flag;
	}
}
