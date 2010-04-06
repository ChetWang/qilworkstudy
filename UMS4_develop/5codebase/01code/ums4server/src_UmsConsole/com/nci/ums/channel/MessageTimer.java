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
 * 定时发送的消息，需要通过UMS定时发送，UMS通过MessageTimer来每隔半分钟来更新已经符合发送要求的消息
 * 
 * @author Qil.Wong
 */
public class MessageTimer implements UMSModule, Runnable {

	/**
	 * 扫描UMS3.0数据库表下的定时发送消息的SQL语句
	 */
	private static StringBuffer timerSql_v3;
	// /**
	// * 扫描UMS2.0数据库表下的定时发送消息的SQL语句
	// */
	// private static StringBuffer timerSql_v2;
	/**
	 * 状态标记，表示线程的运行状态
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
					System.out.println("定时消息激活，数量：" + count);
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
				// System.out.println("定时消息激活，数量：" + count);
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
		t.setName("定时消息检查器");
		t.start();
	}

	public void stopModule() {
		stop = true;
	}

	public boolean isStoped() {
		return stop;
	}
}
