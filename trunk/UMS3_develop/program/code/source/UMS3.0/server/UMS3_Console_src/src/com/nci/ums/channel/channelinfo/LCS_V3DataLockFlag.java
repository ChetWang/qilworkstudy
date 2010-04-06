package com.nci.ums.channel.channelinfo;

public class LCS_V3DataLockFlag extends LockFlag{
	static LCS_V3DataLockFlag flag;

	private LCS_V3DataLockFlag() {
		super();
	}
	
	public static LCS_V3DataLockFlag getInstance(){
		if(flag == null){
			flag = new LCS_V3DataLockFlag();
		}
		return flag;
	}
}
