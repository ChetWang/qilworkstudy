/*
 * NCIOutChannel_V3.java
 *
 * Created on 2007-9-28, 15:21:19
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.channel.outchannel;

import com.nci.ums.channel.channelinfo.LockFlag;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelinfo.NCI_V3DataLockFlag;
import com.nci.ums.channel.channelinfo.NCI_V3ThreadLockFlag;
import com.nci.ums.channel.myproxy.SendMessage;
import com.nci.ums.util.Res;
import com.nci.ums.v3.message.UMSMsg;

import java.io.IOException;
import java.sql.Connection;

/**
 * �ƶ�����Ϣ��������
 * 
 * @Date 2007.10.04
 * @author Qil.Wong
 * @since UMS3.0
 */
public class NCIOutChannel_V3 extends OutChannel {

	protected NCI_V3DataLockFlag nci_v3DataLockFlag;
	protected NCI_V3ThreadLockFlag nci_v3ThreadLockFlag;
	private SendMessage sender = null;

	/**
	 * һ������Ϣ����3.0�Ĺ��캯��
	 * 
	 * @param nciMediaInfo
	 *            MediaInfoֵ
	 */
	public NCIOutChannel_V3(MediaInfo nciMediaInfo) {
		super(nciMediaInfo);
		// sender = new SendMessage(nciMediaInfo);
		sender = SendMessage.getInstance(mediaInfo);
		if (sender == null || sender.getMyProxy() == null) {
			stop = true;
			Res.log(Res.INFO, nciMediaInfo.getMediaName() + "�����������ʧ��,��ֹͣ����!");
		}
		nci_v3DataLockFlag = NCI_V3DataLockFlag.getInstance();
		nci_v3ThreadLockFlag = NCI_V3ThreadLockFlag.getInstance();
	}

	public void processAckMsg(UMSMsg msg, Connection conn) {
		// TODO Auto-generated method stub
	}
	
	public String getMsgFmtFile() {
		return "/resources/MsgFmt_SMS.xml";
	}

	public void sendViaChannel(UMSMsg[] msgs) {
		for (int i = 0; i < msgs.length; i++) {
			if (msgs[i] == null) {
				break;
			}
			try {
				int response = sender.sendMsg(msgs[i]);
				super.OpData(response, msgs[i]);
			} catch (IOException e) {
				Res.log(Res.ERROR, "��ȡ��Ϣ��Ϣ����" + e.getMessage());
				e.printStackTrace();
				Res.logExceptionTrace(e);
				try {
					super.OpData(4321, msgs[i]);
				} catch (Exception ex) {
				}
			} catch (IllegalArgumentException e) {
				Res.log(Res.ERROR, "��ȡ��Ϣ��Ϣ����" + e.getMessage());
				Res.logExceptionTrace(e);
				try {
					super.OpData(4321, msgs[i]);
				} catch (Exception ex) {
				}
			} catch (Exception e) {
				try {
					super.OpData(4321, msgs[i]);
				} catch (Exception ex) {
				}
			}
		}
	}

	public boolean isLocked() {
		return this.nci_v3DataLockFlag.getLockFlag()
				|| this.nci_v3ThreadLockFlag.getLockFlag();
	}

	public void setLocked(boolean flag) {
		this.nci_v3ThreadLockFlag.setLockFlag(flag);
	}
	
	public LockFlag getDataLockFlag(){
		return nci_v3DataLockFlag;
	}
}