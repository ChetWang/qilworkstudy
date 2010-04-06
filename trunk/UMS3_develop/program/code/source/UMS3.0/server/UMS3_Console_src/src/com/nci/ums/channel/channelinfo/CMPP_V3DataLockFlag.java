package com.nci.ums.channel.channelinfo;
/**
 * CMPP��������ǣ������趨CMPP�̵߳�״̬
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
