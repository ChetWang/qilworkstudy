package com.nci.ums.mas;

import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.outchannel.MsgInfo;
import com.nci.ums.channel.outchannel.SCPortOutChannel;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.mas.ws.UMSMASSender;

public class SCPortMasOutChannel extends SCPortOutChannel {

	UMSMASSender masSender = new UMSMASSender();

	public SCPortMasOutChannel(MediaInfo mediaInfo) {
		super(mediaInfo);
	}

	public int sendMsg(MsgInfo msgInfo) throws UMSConnectionException {
		String sourceAddr = msgInfo.getSendID();
		String destAddr = msgInfo.getRecvID();
		String content = msgInfo.getContent();
		String result = masSender.sendToMas(destAddr, content, sourceAddr);
		if(!result.equals("MAS ERROR")){
			return 2239;
		}
		return 0;
	}

}
