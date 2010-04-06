package com.nci.ums.channel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.channel.outchannel.OutChannel_V3;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Util;
import com.nci.ums.v3.message.basic.BasicMsg;

/**
 * ��ʱ���͵���Ϣ����Ҫͨ��UMS��ʱ���ͣ�UMSͨ��MessageTimer��ÿ��������������Ѿ����Ϸ���Ҫ�����Ϣ
 * 
 * @author Qil.Wong
 */
public class MessageTimer extends Thread {

	/**
	 * ɨ��UMS3.0���ݿ���µĶ�ʱ������Ϣ��SQL���
	 */
	private static StringBuffer timerSql_v3;
	/**
	 * ɨ��UMS2.0���ݿ���µĶ�ʱ������Ϣ��SQL���
	 */
	private static StringBuffer timerSql_v2;
	/**
	 * ״̬��ǣ���ʾ�̵߳�����״̬
	 */
	private boolean stop = false;

	public MessageTimer() {
		super("��ʱ��Ϣ�����");
		timerSql_v3 = new StringBuffer("UPDATE OUT_READY_V3 SET TIMESETFLAG=")
				.append(BasicMsg.BASICMSG_SENDTIME_NOCUSTOM).append(
						" WHERE TIMESETFLAG=").append(
						BasicMsg.BASICMSG_SENDTIME_CUSTOM).append(
						" AND SETDATE<=? AND SETTIME<=?");
		timerSql_v2 = new StringBuffer("UPDATE OUT_READY SET TIMESETFLAG=")
				.append(BasicMsg.BASICMSG_SENDTIME_NOCUSTOM).append(
						" WHERE TIMESETFLAG=").append(
						BasicMsg.BASICMSG_SENDTIME_CUSTOM).append(
						" AND SETDATE<=? AND SETTIME<=?");
	}

	public void run() {
		while (!stop) {
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(DataBaseOp.getPoolName());
				String current = Util.getCurrentTimeStr("yyyyMMddHHmmss");
				String currentDate = current.substring(0, 8);
				String currentTime = current.substring(8, 14);
				PreparedStatement timerPrep_v3 = conn
						.prepareStatement(timerSql_v3.toString());
				timerPrep_v3.setString(1, currentDate);
				timerPrep_v3.setString(2, currentTime);
				int count = timerPrep_v3.executeUpdate();
				if (count > 0) {
					System.out.println("��ʱ��Ϣ���������" + count);
					((MediaInfo) ChannelManager.getOutMediaInfoHash().get(
							OutChannel_V3.nciV3Media_ID)).getChannelObject()
							.getDataLockFlag().setLockFlag(false);
				}

				PreparedStatement timerPrep_v2 = conn
						.prepareStatement(timerSql_v2.toString());
				timerPrep_v2.setString(1, currentDate);
				timerPrep_v2.setString(2, currentTime);
				count = timerPrep_v2.executeUpdate();
				if (count > 0) {
					System.out.println("��ʱ��Ϣ���������" + count);
					((MediaInfo) ChannelManager.getOutMediaInfoHash().get(
							OutChannel_V3.nciV2Media_ID)).getChannelObject()
							.getDataLockFlag().setLockFlag(false);
				}
				timerPrep_v3.close();
				timerPrep_v2.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(30 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void pleaseStop() {
		stop = true;
	}

	public boolean isStoped() {
		return stop;
	}
}
