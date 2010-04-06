package com.nci.ums.channel.channelinfo;

public class LCS_V3ThreadLockFlag extends LockFlag{
	static LCS_V3ThreadLockFlag flag;

	private LCS_V3ThreadLockFlag() {
		super();
	}
	
	public static LCS_V3ThreadLockFlag getInstance(){
		if(flag == null){
			flag = new LCS_V3ThreadLockFlag();
		}
		return flag;
	}

}
