package com.nci.ums.mas;

import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.outchannel.MsgInfo;
import com.nci.ums.channel.outchannel.SCPortOutChannel_V3;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.mas.ws.UMSMASSender;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.Participant;

/**
 * Mas���͵�V3�������������������̺߳��丸����SCPortOutChannel_V3�ǳ�ͻ�� ֻ������ʹ������֮һ
 * 
 * @author Qil.Wong
 * 
 */
public class SCPortMasOutChannel_V3 extends SCPortOutChannel_V3 {

	UMSMASSender masSender = new UMSMASSender();

	public SCPortMasOutChannel_V3(MediaInfo mediaInfo) {
		super(mediaInfo);
		groupMessage = false;
	}

	public int sendMsg(MsgInfo msgInfo) throws UMSConnectionException {
		String sourceAddr = msgInfo.getSendID();
		String destAddr = msgInfo.getRecvID();
		String content = msgInfo.getContent();
		String result = masSender.sendToMas(destAddr, content, sourceAddr);
		if (!result.equals("MAS ERROR")) {
			return 2239;
		}
		return 0;
	}

	public void sendViaChannel(UMSMsg[] msgs) {
		if (msgs == null || msgs.length == 0)
			return;
		for (UMSMsg msg : msgs) {
			Participant[] receivers = msg.getBasicMsg().getReceivers();
			if (receivers == null || receivers.length == 0) {
				msg.setErrMsg("�޽�����");
			} else {
				for (Participant receiver : receivers) {
					String mobile = receiver.getParticipantID();
					if (mobile.indexOf("tel:") != 0) {
						mobile = "tel:" + mobile;
					}
					String result = masSender.sendToMas(mobile, msg
							.getBasicMsg().getMsgContent().getContent(), msg
							.getBasicMsg().getServiceID()
							+ msg.getBasicMsg().getAppSerialNO());
					if (result.equals("MAS ERROR")) {
						msg.setErrMsg("Mas ���ʹ���");
						msg.setReturnCode("3322");
					} else {
						msg.setMsgID(result);
					}
				}
			}

		}
	}
}
