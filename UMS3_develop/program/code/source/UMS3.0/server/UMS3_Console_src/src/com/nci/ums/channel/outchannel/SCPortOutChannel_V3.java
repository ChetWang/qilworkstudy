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

			// submitShortMessage api接口：
			/*
			 * atTime：发送短信的时间。(Java.util.Date) sourceAddr：待发送短信的源地址。
			 * destAddr：待发送短信的目的地址。 content：短信内容。 needStateReport：发送该短信是否需要状态报告
			 * 。（注：使用状态报告可以确认对方是否一定收到）。该参数可以使用两个值，0：表示不需要状态报告，1：表示需要状态报告。
			 * serviceID：业务类型。业务类型将用于运营商端对短信进行计费时使用，该参数不能超过10个字符。 feeType：资费类型
			 * 。该参数只能是以下几个值：“01”表示对用户免费；“02”表示对用户按条收取信息费，具体收费将依据下一个参数
			 * ；“03”表示对用户按包月收取信息费。
			 * feeCode：资费代码。该参数依赖上一个参数，表示该短信的信息费（注：以分为单位），该参数不能超过六个字符。 异常： 1002,
			 * 1003,100501 至100509, 1010 （参考异常列表）
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
							"SCYCPortOutChannel sendMsg error:数据库链接异常!");
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
							// TODO 自动生成 catch 块
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
