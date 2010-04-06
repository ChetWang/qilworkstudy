package com.nci.ums.channel.channelinfo;

public class Email_V3DataLockFlag extends LockFlag{
	static Email_V3DataLockFlag flag;

	private Email_V3DataLockFlag() {
		super();
	}
	
	public static Email_V3DataLockFlag getInstance(){
		if(flag == null){
			flag = new Email_V3DataLockFlag();
		}
		return flag;
	}
}
