/**
 * <p>Title: CMPPOutChannel.java</p>
 * <p>Description:
 *    CMPPOutChannel外拨渠道类，继承OutChannel
 *    实现移动信息的发送过程
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   2003 Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.channel.outchannel;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;

import com.commerceware.cmpp.*;
import com.nci.ums.exception.*;
import com.nci.ums.channel.channelinfo.*;
import com.nci.ums.util.*;
import com.nci.ums.util.MD5;

public class CMPPOutChannel extends OutChannel {

	private conn_desc con;
	private CMPP cmpp;
	private MsgInfo[] msgInfos;
	private boolean responseFlag;
	private int submitCount;
	private int responseCount;
	private int msgCount;
	private int activeTime = 0;

	// 定时器设为一分钟
	private javax.swing.Timer my_timer;
	private CMPPOutChannel.ResponseOutTime responseOutTime = new CMPPOutChannel.ResponseOutTime();

	/*
	 * 构造函数
	 */
	public CMPPOutChannel(MediaInfo mediaInfo) {
		super(mediaInfo);
		this.mediaInfo = mediaInfo;
		cmpp = new CMPP();
		msgInfos = new MsgInfo[16];
	}

	// 向滑动窗口增加外发消息

	public void run() {
		my_timer = new javax.swing.Timer(1000 * 60 * 1, responseOutTime); // 1分钟后未响应重启
		my_timer.setRepeats(false);
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
			// 建立连接。

			// 测试时为备注
			getConnection();

			if (stop)
				break;
			Res.log(Res.DEBUG, "成功建立连接！");

			// 发送消息
			try {
				msgInfos = new MsgInfo[16];
				msgCount = getMsgInfos(msgInfos); // 需要修改,详细可以阅读此函数里面
				// 发送滑动窗口中消息
				submitCount = 0;
				for (int i = 0; i < msgCount; i++) {
					// 测试时为备注
					int response = sendMsg(msgInfos[i]);
					// int response=0;
					if (response == 0) {
						submitCount = submitCount + 1;
					}
				}
				// 等待回应

				responseFlag = false;
				responseCount = 0;
				if (submitCount > 0) {
					my_timer.start();
					while (!Thread.interrupted() && !quitFlag.getLockFlag()
							&& !stop && !responseFlag) {
						// testResponse();
						// 测试时为备注
						readPa(con);
					}
					my_timer.stop();
					Res.log(Res.DEBUG, "receive response end");
					endSubmit(msgInfos, msgCount);
				}
			} catch (Exception e) {
				errorLogout();
				Res.log(Res.ERROR, "cmppoutchannel run error:" + e);
                                Res.logExceptionTrace(e);
				endSubmit(msgInfos, msgCount);
				sleepTime(1000);
			}
			if (msgCount == 0)
				sleepTime(1000);

		}
	}

	private void errorLogout() {
		Res.log(Res.DEBUG, "开始启动计划工作，退出与服务器的连接");
		try {
			cmpp.cmpp_logout(con);
			Res.log(Res.DEBUG, "正常退出");
		} catch (Exception ee) {
			Res.log(Res.DEBUG, "退出异常");
		} finally {
			cmpp.cmpp_disconnect_from_ismg(con);
			con = null;
		}

	}

	// 测试
	private void testResponse() {

		OpData(0, msgInfos[responseCount], "");
		// msgInfos[responseCount].setStatus(1);
		// msgInfos[responseCount].setRetCode(0);
		responseCount = responseCount + 1;
		if (responseCount == submitCount)
			responseFlag = true;
	}

	// 测试

	/*
	 * 用于读取回复信息的函数。 返回0的时候，表示正常 返回非0的时候，表示异常。
	 * 其中返回8001的时候表示收到未知格式的包，返回8000的时候，表示网络错误，均需要重新连接！
	 */
	private int readPa(conn_desc con) throws IOException {
		cmppe_result cr = null;
		int result = 0;
		try {
			cr = cmpp.readResPack(con);
			System.out.println("开始发送");
			switch (cr.pack_id) {

			case CMPP.CMPPE_NACK_RESP:
				Res.log(Res.DEBUG, "收到 nack pack数据包");
				break;

			case CMPP.CMPPE_LOGIN_RESP:
				cmppe_login_result cl;
				cl = (cmppe_login_result) cr;
				// Res.log(Res.DEBUG, "收到 login_resp数据包 :返回状态为 " + cl.stat);
				result = cr.stat;
				break;

			case CMPP.CMPPE_LOGOUT_RESP:
				// Res.log(Res.DEBUG, "收到 login_resp数据包 :返回状态为 " + cr.stat);
				result = cr.stat;

				con = null;
				break;

			case CMPP.CMPPE_SUBMIT_RESP:

				cmppe_submit_result sr;
				sr = (cmppe_submit_result) cr;
				String msgID = new String(sr.msg_id).trim();
				// Res.log(Res.DEBUG, "收到 submit_resp 数据包 :返回状态为 " + sr.stat);
				result = sr.stat;
				// 处理成功返回操作
				Res.log(Res.DEBUG, "send success:" + sr.stat);
				MsgInfo msgInfo = getMsgInfo(sr.seq);
				if (result == 0 || result == 42) {
					if (msgInfo != null) {
						// 若需回执，取回执MSGID号
						// if(msgInfo.getAck()==1)
						// msgInfo.setReplyDes(new
						// String(sr.msg_id).substring(0,16));
						OpData(result, msgInfo, msgID);
						msgInfo.setStatus(0);
						msgInfo.setRetCode(result);
					}
				} else if (result == 40) {
					if (msgInfo != null) {
						OpData(result, msgInfo, "");
						msgInfo.setStatus(0);
					}
					msgInfo.setRetCode(result);
					responseFlag = true;
				} else {
					msgInfo.setStatus(1);
					msgInfo.setRetCode(result);
				}

				responseCount = responseCount + 1;
				// Res.log(Res.DEBUG,"记数："+responseCount);
				if (responseCount == submitCount)
					responseFlag = true;
				break;
			case CMPP.CMPPE_ACTIVE_RESP:
				// Res.log(Res.DEBUG, "收到 active_resp 数据包 :返回状态为 " + cr.stat);
				result = cr.stat;
				break;
			default:
				// Res.log(Res.DEBUG, "收到 未知的数据包 :返回状态为 " + cr.stat);
				break;
			}
		} catch (UnknownPackException e) {
			e.printStackTrace();
			result = 8001;
			Res.log(Res.ERROR, "收到了未知格式的包" + e);
                        Res.logExceptionTrace(e);
		} catch (DeliverFailException e) {
			e.printStackTrace();
			result = 8000;
			Res.log(Res.ERROR, "出现网络异常" + e);
                        Res.logExceptionTrace(e);
		} catch (IOException e) {
			e.printStackTrace();
			result = 8000;
			Res.log(Res.ERROR, "出现网络异常" + e);
                        Res.logExceptionTrace(e);
			throw e;
		}
		return result;
	}

	private MsgInfo getMsgInfo(int seq) {
		MsgInfo result = null;
		for (int i = 0; i < msgCount; i++) {
			if (msgInfos[i].getSeq() == seq)
				result = msgInfos[i];
		}
		return result;
	}

	/*
	 * 发送一条信息，返回数值为整数。 返回0表示发送成功， 返回8000表示网络错误，需要重新连接。
	 * 其中返回8001的时候表示收到未知格式的包，需要重新连接。 返回8002的时候表示参数越界，不需要重新连接。
	 */
	public int sendMsg(MsgInfo msgInfo) throws UMSConnectionException {
		int result = 1;
		try {
			byte[] short_msg = Util.getByte(msgInfo.getContent(), msgInfo
					.getContentMode());
			// 处理群发手机号
			String[] temp_dst = msgInfo.getRecvID().split(",");
			int count = temp_dst.length;
			byte[][] dstCode = new byte[count + 1][15];
			for (int i = 0; i < count; i++) {
				dstCode[i] = Util.getByteString(temp_dst[i]);
			}
			// 群发处理完毕
			// 单发dstCode[0] = Util.getByteString(dst);
			byte[][] srcCode = new byte[2][15];
			srcCode[0] = Util.getByteString(msgInfo.getSendID());
			int sm_len = short_msg.length;
			result = sendMsg(dstCode, sm_len, short_msg, srcCode, (byte) count,
					msgInfo);
		} catch (Exception e) {

		}
		return result;
	}

	/*
	 * 发送一条信息，返回数值为整数。 返回0表示发送成功， 返回8000表示网络错误，需要重新连接。 返回8002的时候表示参数越界，不需要重新连接。
	 */
	public int sendMsg(byte dst_addr[][], int sm_len, byte short_msg[],
			byte src_addr[][], byte count, MsgInfo msgInfo)
			throws UMSConnectionException {
		cmppe_submit sub = new cmppe_submit();
		int result = 0;
		// sp ID号码，最大7字节
		byte icp_id[] = new byte[7];
		icp_id[0] = 0x39;
		icp_id[1] = 0x31;
		icp_id[2] = 0x31;
		icp_id[3] = 0x33;
		icp_id[4] = 0x30;
		icp_id[5] = 0x37;
		icp_id[6] = 0x0;
		// 服务类型
		byte svc_type[] = new byte[10];
		svc_type[0] = 0x39;
		svc_type[1] = 0x33;
		svc_type[2] = 0x31;
		svc_type[3] = 0x30;
		svc_type[5] = 0x0;
		byte fee_type = 1;
		if (msgInfo.getFeeType() != 0)
			fee_type = 2; // 资费类别域
		// 以分为单位
		int info_fee = 0;
		if (msgInfo.getFeeType() != 0)
			// info_fee=(int)msgInfo.getFee()*100;
			info_fee = msgInfo.getFee();
		// int info_fee = 0; //资费代码
		byte proto_id = 1; // 协议标识域
		byte msg_mode = 0;

		// 要求回执
		if (msgInfo.getAck() == 1 || msgInfo.getAck() == 3)
			msg_mode = 1; // Submit包的”是否要求返回状态确认报告(0--不需要，1--需要)”域。

		byte priority = 8; // Submit包的信息级别(0—9)域
		byte fee_utype = 2; // 计费用户类别
		if (msgInfo.getFeeType() != 0)
			fee_utype = 0;
		byte fee_user[] = new byte[CMPP.CMPPE_MAX_MSISDN_LEN]; // Submit包的计费用户
		fee_user[0] = 0x0;

		byte validate[] = new byte[10];
		validate[0] = 0; // Submit包的存活有效期
		byte schedule[] = new byte[2];
		schedule[0] = 0; // 定时发送时间

		byte du_count = count; // Submit包中目的手机的数量（大于0）

		byte data_coding = (byte) msgInfo.getContentMode(); // GB2312编码
		try {
			sub.set_icpid(icp_id);
			sub.set_svctype(svc_type);
			sub.set_feetype(fee_type);
			sub.set_infofee(info_fee);
			sub.set_protoid(proto_id);
			sub.set_msgmode(msg_mode);
			sub.set_priority(priority);
			sub.set_validate(validate);
			sub.set_schedule(schedule);
			sub.set_feeutype(fee_utype);
			sub.set_feeuser(fee_user);
			sub.set_srcaddr(src_addr[0]);
			sub.set_ducount(du_count);
			sub.set_dstaddr(dst_addr);
			sub.set_msg(data_coding, sm_len, short_msg);
			// 正式代码

			msgInfo.setSeq(con.seq);
			msgInfo.setStatus(1);
			cmpp.cmpp_submit(con, sub);

			// 正式代码

			// 测试代码
			/*
			 * CMPPTest cmppTest=new CMPPTest("192.168.0.175",10001);
			 * cmppTest.cmpp_submit(sub);
			 */
			// 测试代码
		} catch (OutOfBoundsException e) {
			e.printStackTrace();
			result = 8002; // 参数越界；
			Res.log(Res.ERROR, "参数越界" + e);
                        Res.logExceptionTrace(e);
		} catch (IOException e) {
			e.printStackTrace();
			result = 8000; // 连接过程中故障；
			Res.log(Res.ERROR, "8002" + e);
                        Res.logExceptionTrace(e);
			errorLogout();
			// throw new UMSConnectionException(e);
		}
		return result;
	}

	/*
	 * 和服务器做连接，如果连不上则做循环，并sleep 如果线程断开或者程序退出标志被设置或者线程推出标志被设置的时候，直接退出连接。
	 * 和服务器做身份验证，如果验证不成功，重新连接服务器。
	 */
	public void getConnection() {
		boolean flag = false;
		int i = 0;
		while ((!Thread.interrupted() && !quitFlag.getLockFlag() && !stop)
				&& (!flag)) {
			// 表示以前连接就是断开的
			if (con == null) {
				con = new conn_desc();
				if (i++ == 0)
					Res.log(Res.DEBUG, mediaInfo.getMediaName() + "开始连接服务器:");
				Res.log(Res.DEBUG, mediaInfo.getMediaName() + "第" + i
						+ "次连接服务器:");
				try {
					System.out.println(mediaInfo.getIp() + mediaInfo.getPort()
							+ "");
					cmpp.cmpp_connect_to_ismg(mediaInfo.getIp(), mediaInfo
							.getPort(), con);
					String inetAddr_str = con.sock.getInetAddress().toString();
					String localAddr_str = con.sock.getLocalAddress().toString();
					int port = con.sock.getPort();
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
					Res.log(Res.DEBUG, mediaInfo.getMediaName() + "第" + i
							+ "次连接服务器失败！");
					closeConnection();
				}
				// 如果建立了连接，再验证用户的合法性。
				if (flag == true) {
					Res.log(Res.DEBUG, mediaInfo.getMediaName() + "第" + i
							+ "次连接服务器成功！");
					Res.log(Res.DEBUG, mediaInfo.getMediaName() + "开始和服务器验证！");
					flag = login();
					if (flag)
						Res.log(Res.DEBUG, mediaInfo.getMediaName()
								+ "和服务器验证成功！");
					else
						Res.log(Res.DEBUG, mediaInfo.getMediaName()
								+ "和服务器验证失败！");
				}
				// 如果验证身份失败，关闭连接。并且sleep一段时间，再与服务器连接并做验证
				if (flag == false) {
					Res.log(Res.INFO, mediaInfo.getMediaName()
							+ "将间隔一分钟后，重新连接服务器");
					closeConnection();
					// sleep一分钟。
					try {
						Thread.sleep(60 * 1000);
					} catch (Exception e) {
						e.printStackTrace();
						Res.log(Res.INFO, "线程被中断");
						pleaseStop();
					}
				}
			} else {
				try {

					if (activeTime > 0) {
						Res.log(Res.DEBUG, "time"
								+ (Util.getUTCTime() - activeTime));
						if (Util.getUTCTime() - activeTime >= 1 * 54) {
							activeTime = Util.getUTCTime();
							cmpp.cmpp_active_test(con);
							if (readPa(con) == 0) {
								flag = true;
							} else {
								flag = false;
								con = null;
							}
						} else {
							flag = true;
						}

					} else {
						activeTime = Util.getUTCTime();
						flag = true;
					}

				} catch (Exception e) {
				}

			}
			// con!=null表示已经建立了连接
		}
	}

	public void pleaseStop() {
		stop = true;
	}

	/*
	 * 登陆函数，用于和服务器进行身份验证 返回为true表示验证成功，返回false表示验证失败。
	 */
	private boolean login() {
		try {
			// 0 是只发型， 0x12是版本号码， 最后一个是时间戳。
			cmpp.cmpp_login(con, mediaInfo.getLoginName(), mediaInfo.getLoginPassword(),
					(byte) 0, 0x20, Util.getUTCTime());

			if (readPa(con) == 0)
				return true;
		} catch (OutOfBoundsException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "参数超出规定范围！");
                        Res.logExceptionTrace(e);
		} catch (IOException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "8002", mediaInfo.getMediaName());
                        Res.logExceptionTrace(e);
		}
		// 如果readPa返回数值不为0或者中途出现异常，则表示登陆失败，需要重新登陆。
		return false;
	}

	/*
	 * 退出登陆的函数，用于通知服务器，登陆退出 返回为true表示登陆成功，返回false表示登陆失败。
	 */
	private boolean logout() {
		try {
			cmpp.cmpp_logout(con);
			readPa(con);
		} catch (Exception e) {
			Res.log(Res.ERROR, "8002", mediaInfo.getMediaName());
                        Res.logExceptionTrace(e);
		}
		return true;
	}

	/*
	 * 关闭和服务器的连接
	 */
	public void closeConnection() {
		if (con != null) {
			logout();
			cmpp.cmpp_disconnect_from_ismg(con);
			con = null;
		}
	}

	class ResponseOutTime implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			errorLogout();
		};
	}
	
	public static void main(String[] args){
		byte icp_id[] = new byte[7];
		icp_id[0] = 0x39;
		icp_id[1] = 0x31;
		icp_id[2] = 0x31;
		icp_id[3] = 0x33;
		icp_id[4] = 0x30;
		icp_id[5] = 0x37;
		icp_id[6] = 0x0;
		System.out.println(new String(icp_id));
	}

	public LockFlag getLockFlag() {
		// TODO Auto-generated method stub
		return CMPPThreadLockFlag.getInstance();
	}

}