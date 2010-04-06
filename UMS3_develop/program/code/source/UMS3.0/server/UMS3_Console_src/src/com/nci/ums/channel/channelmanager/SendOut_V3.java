package com.nci.ums.channel.channelmanager;

import com.nci.ums.util.Res;

public class SendOut_V3 extends Thread {

	private SendOut sendout;
	private ScanInvalid_V3 scanInvalid3;

	public SendOut_V3() {
		Res.log(Res.INFO, "SendOut_V3对象产生！");
		sendout = new SendOut();
		scanInvalid3 = new ScanInvalid_V3();
	}

	public void run() {
		sendout.start();
		scanInvalid3.start();
	}

	public void stopThread() {
		sendout.stopThread();
		scanInvalid3.stop();
	}
	
	
}
