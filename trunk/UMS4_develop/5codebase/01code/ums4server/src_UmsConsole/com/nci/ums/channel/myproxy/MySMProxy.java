/*
 * Created on 2007-2-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.nci.ums.channel.myproxy;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.huawei.insa2.comm.cmpp.message.CMPPDeliverMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPSubmitMessage;
import com.huawei.insa2.util.Args;
import com.huawei.smproxy.SMProxy;
import com.nci.ums.channel.ChannelIfc;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.channel.inchannel.CMPPInChannel;
import com.nci.ums.channel.outchannel.OutChannel;
import com.nci.ums.util.AppServiceUtil;
import com.nci.ums.util.DBUtil_V3;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Res;
import com.nci.ums.util.SerialNO;
import com.nci.ums.util.Util;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;

public class MySMProxy extends SMProxy {

	private SendMessage out = null;

	private String umsCenter_cm;
	private DBUtil_V3 dbUtil = new DBUtil_V3();
	

	// MediaInfo mediaInfo = null;

	// public MySMProxy(SendMessage out, Args args, MediaInfo mediaInfo) {
	// super(args);
	// this.out = out;
	// this.mediaInfo = mediaInfo;
	// }

	public MySMProxy(SendMessage out, Args args) {
		super(args);
		this.out = out;
		umsCenter_cm = out.getUmsCenter_cm();
		// this.mediaInfo = mediaInfo;
		
	}

	private void processInMsgV2(CMPPDeliverMessage msg) {
		MediaInfo mediaInfo = new MediaInfo();
		mediaInfo.setMediaID(OutChannel.nciV2Media_ID);
		CMPPInChannel tool = new CMPPInChannel(mediaInfo);
		// msg.getSrcterminalId();//消息发送者
		if (msg.getRegisteredDeliver() == 1) {
			try {
				String s = new String(msg.getMsgContent(), 0, msg
						.getMsgLength(), "UnicodeBig");
				tool.insertTable(0, msg.getSrcterminalId(), msg
						.getDestnationId(), s, OutChannel.nciV2Media_ID, "", 4);
			} catch (Exception e) {
				System.out.println("接收到一条新的消息发生异常:" + e);
			}
		} else {
			try {
				tool.insertTable(0, msg.getSrcterminalId(), msg
						.getDestnationId(), new String(msg.getMsgContent(), 0,
						msg.getMsgLength(), "GBK"), OutChannel.nciV2Media_ID, "", 4);
			} catch (Exception e) {
				System.out.println("接收到一条新的消息发生异常:" + e);
			}
		}
	}

	private void processInMsgV3(CMPPDeliverMessage msg, String serviceIDAndAppSerial) {
		UMSMsg umsMsg = new UMSMsg();
		String now = Util.getCurrentTimeStr("yyyyMMddHHmmss");
		umsMsg.setBatchNO(now);
		umsMsg.setSerialNO(new Integer(SerialNO.getInstance().getSerial())
				.intValue());
		umsMsg.setSequenceNO(new int[] { 0 });
		MsgContent msgContent = new MsgContent("", Util.getStringFromBytes(msg
				.getMsgContent(), 0, msg.getMsgLength(), msg.getMsgFmt()));
		BasicMsg basic = new BasicMsg();
		basic.setContentMode(msg.getMsgFmt());
		basic.setMediaID(OutChannel.nciV3Media_ID);
		basic.setMsgContent(msgContent);
		String srcTerminal = msg.getSrcterminalId();//发送人的手机号
		Participant sender = new Participant();
		sender.setIDType(Participant.PARTICIPANT_ID_MOBILE);
		// 中国移动的手机号收到时可能会默认加上86，也有可能不加
		sender.setParticipantID(srcTerminal.startsWith("86") ? srcTerminal
				.substring(2, srcTerminal.length()) : srcTerminal);
		sender.setParticipantType(Participant.PARTICIPANT_MSG_FROM);
		basic.setSender(sender);
		Participant receiver = new Participant();
		receiver.setIDType(Participant.PARTICIPANT_ID_MOBILE);
		receiver.setParticipantID(umsCenter_cm + serviceIDAndAppSerial);
		receiver.setParticipantType(Participant.PARTICIPANT_MSG_TO);
		basic.setReceivers(new Participant[] { receiver });
		basic.setServiceID(serviceIDAndAppSerial.substring(0,4));
		basic.setAppSerialNO(serviceIDAndAppSerial.substring(4));
		basic.setSubmitDate(now.substring(0, 8));
		basic.setSubmitTime(now.substring(8, 14));
		umsMsg.setBasicMsg(basic);
		storeInMsgV3(umsMsg);
	}

	private void storeInMsgV3(UMSMsg umsMsg) {
		Connection conn = null;
		// the statement of message inserting into database
		PreparedStatement inPrep = null;
		try {
			conn = Res.getConnection();
			inPrep = conn.prepareStatement(DBUtil_V3
					.createInMsgInsertSQL(DBUtil_V3.MESSAGE_UMS_RECEIVE_READY));
			dbUtil.executeInMsgInsertStatement(inPrep, null, umsMsg,
					DBUtil_V3.MESSAGE_UMS_RECEIVE_READY, conn);
		} catch (Exception e) {
			Res.log(Res.ERROR, "UMS3.0(" + OutChannel.nciV3Media_ID + ")接收到的消息存储错误："
					+ e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (inPrep != null) {
					inPrep.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 对ISMG主动下发的消息的处理。
	 * 
	 * @param msg
	 *            收到的消息
	 * @return 返回的相应消息。
	 */
	public CMPPMessage onDeliver(CMPPDeliverMessage msg) {
		CMPPMessage cmpp =null;
		try {
			if (msg.getMsgContent() != null) {
				Map activeServiceMap = AppServiceUtil.getActiveAppServiceMap();
				// 获取应用服务号，空的服务号视为转发消息
				String destinationID = msg.getDestnationId();
				String[] s = destinationID.split(umsCenter_cm);

				String serviceIDAndAppSerial = "";
				if (s.length > 1)
					serviceIDAndAppSerial = s[1];//四位应用号+应用分配给该消息的序列号

				// 老版本UMS2.0的接入应用：应用号不为空（老版本UMS无转发功能,转发功能无需知道应用和服务），并且应用ID不在activeServiceMap中（activeServiceMap只有UMS3.0的服务）
				if (!"".equals(serviceIDAndAppSerial)
						&& !activeServiceMap.containsKey(serviceIDAndAppSerial)) {
					this.processInMsgV2(msg);
				} else {
					// UMS3.0的接入应用
					this.processInMsgV3(msg, serviceIDAndAppSerial);
				}
			}else{
				if(msg.getRegisteredDeliver() == 1){//空内容并且是RegisteredDeliver==1的是状态报告消息
					processAckMessage(msg);				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cmpp = super.onDeliver(msg);
		}
		return cmpp;
	}
	
	private void processAckMessage(CMPPDeliverMessage msg){
		int msgId = Res.bytesToInt(msg.getStatusMsgId());
		System.out.print("terminal:"+msg.getSrcterminalId());
		System.out.println(" stat:"+msg.getStat());
		
		Connection conn = null;
		// the statement of message inserting into database
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			Statement stmt = conn.createStatement();
			String sql = "UPDATE UMS_SEND_OK SET ack="+BasicMsg.UMSMsg_ACK_SUCCESS+",errmsg='"+CMPPCode.getDesciption(msg.getStat())+"' WHERE msgid='"+msgId+"' AND ack="+BasicMsg.UMSMsg_ACK;
			Res.log(Res.DEBUG,"ack sql:"+sql);
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			Res.logExceptionTrace(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized CMPPMessage send(CMPPSubmitMessage message) throws IOException{
		return super.send(message);
	}

	/**
	 * ISMG断开连接
	 */
	public void OnTerminate() {
		Res.log(Res.INFO, "ISMG：消息网关被关闭");
		ChannelIfc channel_v3 = (ChannelIfc) ChannelManager
				.getOutMediaInfoHash().get(OutChannel.nciV3Media_ID);
		if (channel_v3 != null) {
			channel_v3.stop();
			ChannelManager.getOutMediaInfoHash().remove(OutChannel.nciV3Media_ID);
		}
		ChannelIfc channel_v2 = (ChannelIfc) ChannelManager
				.getOutMediaInfoHash().get(OutChannel.nciV2Media_ID);
		if (channel_v2 != null) {
			channel_v2.stop();
			ChannelManager.getOutMediaInfoHash().remove(OutChannel.nciV2Media_ID);
		}
	}

}