package com.nci.ums.channel.outchannel;

import java.io.IOException;
import java.sql.Connection;

import javax.swing.Timer;

import com.commerceware.cmpp.CMPP;
import com.commerceware.cmpp.DeliverFailException;
import com.commerceware.cmpp.UnknownPackException;
import com.commerceware.cmpp.cmppe_result;
import com.commerceware.cmpp.cmppe_submit;
import com.commerceware.cmpp.cmppe_submit_result;
import com.commerceware.cmpp.conn_desc;
import com.nci.ums.channel.channelinfo.CMPP_V3DataLockFlag;
import com.nci.ums.channel.channelinfo.CMPP_V3ThreadLockFlag;
import com.nci.ums.channel.channelinfo.LockFlag;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.util.CMPPUtil_V3;
import com.nci.ums.util.Res;
import com.nci.ums.v3.message.UMSMsg;

public class CMPPOutChannel_V3 extends OutChannel {

	protected CMPP_V3DataLockFlag cmpp_V3DataLockFlag;
	protected CMPP_V3ThreadLockFlag cmpp_V3ThreadLockFlag;

	private boolean responseFlag = false;

	private ResponseOutTime outTimeResp = new ResponseOutTime();

	private Timer timer;

	public CMPPOutChannel_V3(MediaInfo mediaInfo) {
		super(mediaInfo);
		cmpp_V3DataLockFlag = CMPP_V3DataLockFlag.getInstance();
		cmpp_V3ThreadLockFlag = CMPP_V3ThreadLockFlag.getInstance();
	}
	
	public LockFlag getDataLockFlag(){
		return cmpp_V3DataLockFlag;
	}

	public void sendViaChannel(UMSMsg[] msgs) {
		int submitCount = 0;
		if (msgs[0] != null) {
			conn_desc con = CMPPUtil_V3.getCMPPConn_desc(mediaInfo);
			for (int i = 0; i < msgs.length; i++) {
				if (msgs[i] != null) {
					try {
						int response = sendMsg(con, msgs[i]);
						if (response == 0) {
							submitCount++;
						}
						responseFlag = false;
						int responseCount = 0;
						if (submitCount > 0) {
							timer = new Timer(1000 * 60, outTimeResp);
							timer.start();
							while (!Thread.interrupted()
									&& !quitFlag.getLockFlag() && !stop
									&& !responseFlag) {
								cmppe_submit_result sr = (cmppe_submit_result) CMPPUtil_V3.getCMPP(
										mediaInfo).readResPack(con);

								String msgID = new String(sr.msg_id).trim();
								// Res.log(Res.DEBUG, "收到 submit_resp 数据包 :返回状态为
								// " +
								// sr.stat);
								int result = sr.stat;
								// 处理成功返回操作
								Res.log(Res.DEBUG, "send success:" + sr.stat);
								UMSMsg msg = getUMSMsg(con, sr.seq, msgs);
								msg.setMsgID(msgID);
								OpData(result, msg);
								// if (result == 0 || result == 42) {
								// if (msg != null) {
								// // 若需回执，取回执MSGID号
								// // if(msgInfo.getAck()==1)
								// // msgInfo.setReplyDes(new
								// // String(sr.msg_id).substring(0,16));
								// // OpData(result, msgInfo, msgID);
								// // msgInfo.setStatus(0);
								// // msgInfo.setRetCode(result);
								// OpData(result, msg);
								// }
								// } else if (result == 40) {
								// if (msg != null) {
								// OpData(result, msgInfo, "");
								// msgInfo.setStatus(0);
								// }
								// msgInfo.setRetCode(result);
								// responseFlag = true;
								// } else {
								// msgInfo.setStatus(1);
								// msgInfo.setRetCode(result);
								// }

								responseCount = responseCount + 1;
								if (responseCount == submitCount)
									responseFlag = true;
								break;
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnknownPackException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DeliverFailException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private UMSMsg getUMSMsg(conn_desc con, int msgSeq, UMSMsg[] msgs) {
		for (int i = 0; i < msgs.length; i++) {
			if (msgs[i].getSmsMsgSeq() == con.seq)
				return msgs[i];
		}
		return null;
	}

	private int sendMsg(conn_desc con, UMSMsg msg) {
		int result = 0;
		cmppe_submit sub = new cmppe_submit();
		// sub.setXXX();

		CMPP cmpp = CMPPUtil_V3.getCMPP(mediaInfo);
		try {
			cmpp.cmpp_submit(con, sub);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void processAckMsg(UMSMsg msg, Connection conn) {
		// TODO Auto-generated method stub
	}

	public void setLocked(boolean flag) {
		this.cmpp_V3ThreadLockFlag.setLockFlag(flag);
	}

	public boolean isLocked() {
		return this.cmpp_V3DataLockFlag.getLockFlag()
				|| this.cmpp_V3ThreadLockFlag.getLockFlag();
	}
	
	public String getMsgFmtFile() {
		return null;
	}

	class ResponseOutTime implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			CMPPUtil_V3.errorLogout(mediaInfo);
		};
	}

}
