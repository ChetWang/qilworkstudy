package com.nci.ums.channel.channelinfo;
/**
 * CMPP数据锁标记，用来设定CMPP线程的状态
 * @since UMS3.0
 * @author Qil.Wong
 *
 */
public class CMPP_V3DataLockFlag extends LockFlag{

	static CMPP_V3DataLockFlag flag;

	private CMPP_V3DataLockFlag() {
		super();
	}
	
	public static CMPP_V3DataLockFlag getInstance(){
		if(flag == null){
			flag = new CMPP_V3DataLockFlag();
		}
		return flag;
	}
}
