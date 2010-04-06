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
	 * ������ģʽ��ָ������������Ӧģʽ����UMS�з����ࣺ
	 * ChannelModel.CHANNEL_MODE_SCAN----ͨ����ʱɨ��ȥ�õ�������Ϣ
	 * ChannelModel.CHANNEL_MODE_LISTENER----ͨ��������ʽ�õ�������Ϣ
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
//				 StringBuffer("���������̣߳�").append(mediaInfo.getMediaId()).append("��ʼ���ղ������ⲿ��Ϣ...").toString());
				System.out.println(new StringBuffer("���������̣߳�").append(
						mediaInfo.getMediaId()).append("��ʼ���ղ������ⲿ��Ϣ...")
						.toString());
				UMSMsg[] msgs = new UMSMsg[16];
				int msgCount = this.iniMsgs(msgs);
				if (msgCount > 0) {
					// ���õ��Ĳ�����Ϣ�洢�����ݿⲦ���
					this.insertTable(msgs, msgCount);
				} else if (this.channelMode == ChannelMode.CHANNEL_MODE_SCAN) {
                                    //�����ɨ�����͵��̣߳�ǰһ�׶����û�еõ���Ϣ����˯��һ��ʱ�䣻�������Ϣ�������ɨ��
					Thread.sleep(2000);
				}
			} catch (InterruptedException ex) {
				System.out.println(ex.getMessage());
				Res.log(Res.ERROR, new StringBuffer("���������̣߳�").append(
						mediaInfo.getMediaId()).append("���ж�.").append(
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
			Res.log(Res.ERROR, new StringBuffer("��������:").append(
					mediaInfo.getMediaId()).append("���ݿ������쳣.").append(
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
				Res.log(Res.ERROR, new StringBuffer("��������:").append(
						mediaInfo.getMediaId()).append("���ݿ����ӹر��쳣.").append(
						ex.getMessage()).toString());
                                Res.logExceptionTrace(ex);
			}
		}
	}
}