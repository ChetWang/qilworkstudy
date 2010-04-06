package com.nci.ums.channel.outchannel;

import java.io.IOException;
import java.sql.SQLException;

import com.nci.scport.ums.SCSmsConnect;
import com.nci.ums.channel.channelinfo.LockFlag;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelinfo.SCPort_V3DataLockFlag;
import com.nci.ums.channel.channelinfo.SCPort_V3ThreadLockFlag;
import com.nci.ums.util.Res;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.Participant;

public class SCPortOutChannel_V3 extends OutChannel_V3 {

	protected LockFlag dataLock = SCPort_V3DataLockFlag.getInstance();

	protected LockFlag threadLock = SCPort_V3ThreadLockFlag.getInstance();

	public SCPortOutChannel_V3(MediaInfo mediaInfo) {
		super(mediaInfo);
	}

	@Override
	public String getMsgFmtFile() {
		return "/resources/MsgFmt_SMS.xml";
	}

	@Override
	public boolean isLocked() {
		return dataLock.getLockFlag() || threadLock.getLockFlag();
	}

	@Override
	public void sendViaChannel(UMSMsg[] msgs) {
		for (int i = 0; i < msgs.length; i++) {
			SCSmsConnect dbconnect = null;

			// submitShortMessage api�ӿڣ�
			/*
			 * atTime�����Ͷ��ŵ�ʱ�䡣(Java.util.Date) sourceAddr�������Ͷ��ŵ�Դ��ַ��
			 * destAddr�������Ͷ��ŵ�Ŀ�ĵ�ַ�� content���������ݡ� needStateReport�����͸ö����Ƿ���Ҫ״̬����
			 * ����ע��ʹ��״̬�������ȷ�϶Է��Ƿ�һ���յ������ò�������ʹ������ֵ��0����ʾ����Ҫ״̬���棬1����ʾ��Ҫ״̬���档
			 * serviceID��ҵ�����͡�ҵ�����ͽ�������Ӫ�̶˶Զ��Ž��мƷ�ʱʹ�ã��ò������ܳ���10���ַ��� feeType���ʷ�����
			 * ���ò���ֻ�������¼���ֵ����01����ʾ���û���ѣ���02����ʾ���û�������ȡ��Ϣ�ѣ������շѽ�������һ������
			 * ����03����ʾ���û���������ȡ��Ϣ�ѡ�
			 * feeCode���ʷѴ��롣�ò���������һ����������ʾ�ö��ŵ���Ϣ�ѣ�ע���Է�Ϊ��λ�����ò������ܳ��������ַ��� �쳣�� 1002,
			 * 1003,100501 ��100509, 1010 ���ο��쳣�б�
			 */
			if (msgs[i] == null)
				continue;
			String sourceAddr = msgs[i].getBasicMsg().getSender()
					.getParticipantID();
			String content = msgs[i].getBasicMsg().getMsgContent().getContent();
			Participant[] receivers = msgs[i].getBasicMsg().getReceivers();
			for (int k = 0; k < receivers.length; k++) {
				try {
					dbconnect = new SCSmsConnect();
					String destAddr = receivers[i].getParticipantID();
					dbconnect
							.prepareStatement("insert into mtqueue(destTermID,srcTermID,msgContent) values(?,?,?)");

					dbconnect.setString(1, destAddr);
					dbconnect.setString(2, sourceAddr);
					dbconnect.setString(3, content);

					dbconnect.executeUpdate();
					super.OpData(0, msgs[i]);
				} catch (SQLException e) {
					e.printStackTrace();
					Res.log(Res.ERROR,
							"SCYCPortOutChannel sendMsg error:���ݿ������쳣!");
					Res.logExceptionTrace(e);
					try {
						super.OpData(4321, msgs[i]);
					} catch (IOException e1) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					Res.logExceptionTrace(e);
					e.printStackTrace();
					try {
						super.OpData(4321, msgs[i]);
					} catch (IOException e1) {
						e.printStackTrace();
					}
				} finally {
					if (dbconnect != null)
						try {
							dbconnect.close();
						} catch (Exception e1) {
							// TODO �Զ����� catch ��
							e1.printStackTrace();
						}
				}

			}
		}

	}

	@Override
	public void setLocked(boolean flag) {
		threadLock.setLockFlag(flag);
	}

	public LockFlag getDataLockFlag() {
		return dataLock;
	}

}
