/*
 * Inchannel_V3.java
 *
 * Created on 2007-10-7, 20:07:31
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.channel.inchannel;

import com.nci.ums.channel.channelinfo.ChannelMode;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.util.DBUtil_V3;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Res;
import com.nci.ums.v3.message.UMSMsg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Basic InChannel definition for application to receive messages from other
 * terminals.
 * 
 * @author Qil.Wong
 */
public abstract class InChannel_V3 extends InChannel {

	protected DBUtil_V3 dbUtil = null;
	/**
	 * 渠道的模式，指拨入渠道的响应模式，在UMS中分两类：
	 * ChannelModel.CHANNEL_MODE_SCAN----通过定时扫描去得到拨入消息
	 * ChannelModel.CHANNEL_MODE_LISTENER----通过监听方式得到拨入消息
	 */
	protected int channelMode;

	public InChannel_V3(MediaInfo mediaInfo, int channelMode) {
		super(mediaInfo);
		this.channelMode = channelMode;
		dbUtil = new DBUtil_V3();
	}

	public void run() {
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit) {
			try {
//				 Res.log(Res.DEBUG, new
//				 StringBuffer("拨入渠道线程：").append(mediaInfo.getMediaId()).append("开始接收并处理外部消息...").toString());
				System.out.println(new StringBuffer("拨入渠道线程：").append(
						mediaInfo.getMediaId()).append("开始接收并处理外部消息...")
						.toString());
				UMSMsg[] msgs = new UMSMsg[16];
				int msgCount = this.iniMsgs(msgs);
				if (msgCount > 0) {
					// 将得到的拨入消息存储在数据库拨入表
					this.insertTable(msgs, msgCount);
				} else if (this.channelMode == ChannelMode.CHANNEL_MODE_SCAN) {
                                    //如果是扫描类型的线程，前一阶段如果没有得到消息，则睡眠一段时间；如果有消息，则继续扫描
					Thread.sleep(2000);
				}
			} catch (InterruptedException ex) {
				System.out.println(ex.getMessage());
				Res.log(Res.ERROR, new StringBuffer("拨入渠道线程：").append(
						mediaInfo.getMediaId()).append("被中断.").append(
						ex.getMessage()).toString());
			} catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Initialize the UMSMsg array from each child in_channel.
	 * 
	 * @param msgs
	 *            UMSMsg array, size is 16
	 * @return True message count
	 */
	public abstract int iniMsgs(UMSMsg[] msgs);

	private void insertTable(UMSMsg[] msgs, int msgCount) {
		Connection conn = null;
		// the statement of message inserting into database
		PreparedStatement inPrep = null;
		// the statement of attachments of a message inserting into database
		PreparedStatement attachPrep = null;
		try {
			conn = Res.getConnection();
			conn.setAutoCommit(false);
			inPrep = conn.prepareStatement(DBUtil_V3
					.createInMsgInsertSQL(DBUtil_V3.MESSAGE_UMS_RECEIVE_READY));
			attachPrep = conn.prepareStatement(DBUtil_V3
					.createAttachInsertSQL());
			for (int i = 0; i < msgCount; i++) {
                            if(msgs[i]!=null)
				dbUtil.executeInMsgInsertStatement(inPrep, attachPrep, msgs[i],
						DBUtil_V3.MESSAGE_UMS_RECEIVE_READY, conn);
			}
			conn.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			Res.log(Res.ERROR, new StringBuffer("拨入渠道:").append(
					mediaInfo.getMediaId()).append("数据库连接异常.").append(
					ex.getMessage()).toString());
                        Res.logExceptionTrace(ex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (inPrep != null)
					inPrep.close();
				if (attachPrep != null)
					attachPrep.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
				Res.log(Res.ERROR, new StringBuffer("拨入渠道:").append(
						mediaInfo.getMediaId()).append("数据库连接关闭异常.").append(
						ex.getMessage()).toString());
                                Res.logExceptionTrace(ex);
			}
		}
	}
}