package com.nci.ums.channel.channelmanager;

import com.nci.ums.basic.UMSModule;
import com.nci.ums.util.Res;

/**
 * 渠道对象总控类，控制渠道产生、消亡，及渠道中间过程处理
 * @author Qil.Wong
 *
 */
public class SendOut_V3 implements UMSModule {

	private ChannelManager channelManager = null;
	private ScanInvalid_V3 scanInvalid3;

	private static SendOut_V3 send_v3;

	private SendOut_V3() {
		
	}

	public static SendOut_V3 getInstance() {
		if (send_v3 == null) {
			send_v3 = new SendOut_V3();
		}
		return send_v3;
	}

	public void startModule() {
		Res.log(Res.INFO, "ChannelManager对象产生");
		channelManager = new ChannelManager();
		Res.log(Res.INFO, "ScanInvalid对象产生");
		scanInvalid3 = new ScanInvalid_V3();
		channelManager.start();
		scanInvalid3.start();
	}

	public void stopModule() {
		channelManager.stop();
		scanInvalid3.stop();
		channelManager = null;
		scanInvalid3 = null;
	}

}
