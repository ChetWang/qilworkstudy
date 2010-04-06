package com.nci.ums.channel.channelinfo;

public class CMPP_V3ThreadLockFlag extends LockFlag {
	
	static CMPP_V3ThreadLockFlag flag;

	private CMPP_V3ThreadLockFlag() {
		super();
	}
	
	public static CMPP_V3ThreadLockFlag getInstance(){
		if(flag == null){
			flag = new CMPP_V3ThreadLockFlag();
		}
		return flag;
	}
}
