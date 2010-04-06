package com.nci.ums.channel.channelinfo;

public class Email_V3ThreadLockFlag extends LockFlag{
	static Email_V3ThreadLockFlag flag;

	private Email_V3ThreadLockFlag() {
		super();
	}
	
	public static Email_V3ThreadLockFlag getInstance(){
		if(flag == null){
			flag = new Email_V3ThreadLockFlag();
		}
		return flag;
	}
}
