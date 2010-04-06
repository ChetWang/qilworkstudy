package com.nci.ums.channel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import com.nci.ums.basic.UMSModule;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.channel.outchannel.OutChannel;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;
import com.nci.ums.v3.message.basic.BasicMsg;

/**
 * ��ʱ���͵���Ϣ����Ҫͨ��UMS��ʱ���ͣ�UMSͨ��MessageTimer��ÿ��������������Ѿ����Ϸ���Ҫ�����Ϣ
 * 
 * @author Qil.Wong
 */
public class MessageTimer implements UMSModule, Runnable {

	/**
	 * ɨ��UMS3.0���ݿ���µĶ�ʱ������Ϣ��SQL���
	 */
	private static StringBuffer timerSql_v3;
	// /**
	// * ɨ��UMS2.0���ݿ���µĶ�ʱ������Ϣ��SQL���
	// */
	// private static StringBuffer timerSql_v2;
	/**
	 * ״̬��ǣ���ʾ�̵߳�����״̬
	 */
	private boolean stop = false;

	static MessageTimer mt = null;

	private MessageTimer() {

	}

	public static MessageTimer getInstance() {
		if (mt == null) {
			mt = new MessageTimer();
		}
		return mt;
	}

	public void run() {
		while (!stop) {
			Connection conn = null;
			try {
				conn = Res.getConnection();
				String current = Util.getCurrentTimeStr("yyyyMMddHHmmss");
				String currentDate = current.substring(0, 8);
				String currentTime = current.substring(8, 14);
				PreparedStatement timerPrep_v3 = conn
						.prepareStatement(timerSql_v3.toString());
				timerPrep_v3.setString(1, currentDate);
				timerPrep_v3.setString(2, currentTime);
				int count = timerPrep_v3.executeUpdate();
				timerPrep_v3.close();
				if (count > 0) {
					System.out.println("��ʱ��Ϣ���������" + count);
					Map outMap = ChannelManager.getOutMediaInfoHash();
					Iterator it = outMap.values().iterator();
					while(it.hasNext()){
						MediaInfo media = (MediaInfo)it.next();
						media.getChannelObject().getDataLockFlag().setLockFlag(false);
					}
//					((MediaInfo) ChannelManager.getOutMediaInfoHash().get(
//							OutChannel.nciV3Media_ID)).getChannelObject()
//							.getDataLockFlag().setLockFlag(false);
					
				}

				// PreparedStatement timerPrep_v2 = conn
				// .prepareStatement(timerSql_v2.toString());
				// timerPrep_v2.setString(1, currentDate);
				// timerPrep_v2.setString(2, currentTime);
				// count = timerPrep_v2.executeUpdate();
				// if (count > 0) {
				// System.out.println("��ʱ��Ϣ���������" + count);
				// ((MediaInfo) ChannelManager.getOutMediaInfoHash().get(
				// OutChannel.nciV2Media_ID)).getChannelObject()
				// .getDataLockFlag().setLockFlag(false);
				// }
				
				// timerPrep_v2.close();
			} catch (SQLException e) {
				Res.logExceptionTrace(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						Res.logExceptionTrace(e);
					}
				}
			}
			try {
				Thread.sleep(30 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void startModule() {
		timerSql_v3 = new StringBuffer("UPDATE UMS_SEND_READY SET TIMESETFLAG=")
				.append(BasicMsg.BASICMSG_SENDTIME_NOCUSTOM).append(
						" WHERE TIMESETFLAG=").append(
						BasicMsg.BASICMSG_SENDTIME_CUSTOM).append(
						" AND SETDATE<=? AND SETTIME<=?");
		Thread t = new Thread(this);
		t.setName("��ʱ��Ϣ�����");
		t.start();
	}

	public void stopModule() {
		stop = true;
	}

	public boolean isStoped() {
		return stop;
	}
}
