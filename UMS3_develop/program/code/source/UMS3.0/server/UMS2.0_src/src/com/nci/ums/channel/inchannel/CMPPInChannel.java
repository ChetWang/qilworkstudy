/**
 * <p>Title: CMPPOutChannel.java</p>
 * <p>Description:
 *    CMPPOutChannel拨入渠道类，继承InChannel
 *    实现移动信息的发送过程
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   2003 Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV.  5 2003   张志勇       Created
 * @version 1.0
 */

package com.nci.ums.channel.inchannel;

import java.io.*;
import com.commerceware.cmpp.*;
import com.nci.ums.channel.channelinfo.*;
import com.nci.ums.util.*;

public class CMPPInChannel extends InChannel {

	private MediaInfo mediaInfo;
	private conn_desc con;
	private CMPP cmpp;

	// private Test_Conn_to_MOBILE testconntomobile=new Test_Conn_to_MOBILE();

	public CMPPInChannel(MediaInfo mediaInfo) {
		super(mediaInfo);
		this.mediaInfo = mediaInfo;
		cmpp = new CMPP();
		// cmppActiveTest = new CMPPActiveTest(mediaInfo.getTimeOut(), cmpp);

	}

	public void run() {
		// my_timer = new javax.swing.Timer(1000 * 60 * 5, testconntomobile);
		// //5分钟后重启一次
		// my_timer.setRepeats(false);
		// 先把退出标志设置为false;
		setIsQuit(false);
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit) {
			// 建立连接。
			getConnection();
			// 启动测试线程
			// cmppActiveTest.run();
			// my_timer.start();
			while (!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit) {
				// 表示出现了网络异常或者严重的错误，需要重新连接服务器。
				if (readPa(con) < 0) {
					Res.log(Res.DEBUG, mediaInfo.getMediaName()
							+ "出现了网络异常或者严重的错误");
					break;
				}
			}
			// my_timer.stop();
		}
		closeConnection();
		// 把状态设置为true,表示已经退出了线程,以便于后来的查询线程状态。
		setIsQuit(true);
	}

	/*
	 * 和服务器做连接，如果连不上则做循环，并sleep 如果线程断开或者程序退出标志被设置或者线程推出标志被设置的时候，直接退出连接。
	 * 和服务器做身份验证，如果验证不成功，重新连接服务器。
	 */
	public void getConnection() {
		boolean flag = false;
		int i = 0;
		while ((!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit)
				&& (!flag)) {
			// 表示以前连接就是断开的
			if (con == null) {
				con = new conn_desc();
				if (i++ == 0)
					Res.log(Res.DEBUG, mediaInfo.getMediaName() + "开始连接服务器:");
				Res.log(Res.DEBUG, mediaInfo.getMediaName() + "第" + i
						+ "次连接服务器:");
				try {
					cmpp.cmpp_connect_to_ismg(mediaInfo.getIp(), mediaInfo
							.getPort(), con);
					flag = true;
				} catch (IOException e) {
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
					Res.log(Res.INFO, mediaInfo.getMediaName() + "将间隔"
							+ mediaInfo.getSleepTime() + "秒后，重新连接服务器");
					closeConnection();
					// sleep一段时间。
					try {
						Thread.sleep(mediaInfo.getSleepTime() * 1000);
					} catch (Exception e) {
						e.printStackTrace();
						Res.log(Res.INFO, "线程被中断");
						setIsQuit(true);
					}
				}
			}
			// con!=null表示已经建立了连接
			else
				flag = true;
		}
	}

	/*
	 * 登陆函数，用于和服务器进行身份验证 返回为true表示验证成功，返回false表示验证失败。
	 */
	private boolean login() {
		try {
			cmpp.cmpp_login(con, mediaInfo.getLoginName(), mediaInfo
					.getLoginPassword(), (byte) 1, 0x12, Util.getUTCTime());
			// 这个时间戳需要修改！！！
			if (readPa(con) == 0)
				return true;
			else
				return false;
		} catch (OutOfBoundsException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "发送的信息包中参数超过规定的数值！");
                        Res.logExceptionTrace(e);
		} catch (IOException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "出现登陆错误！");
                        Res.logExceptionTrace(e);
		}
		return false;
	}

	/*
	 * 关闭和服务器的连接
	 */
	public void closeConnection() {
		if (con != null) {
			// logout();
			cmpp.cmpp_disconnect_from_ismg(con);
			con = null;
		}
	}

	public int readPa(conn_desc con) {
		String sendNumber = null; // send手机号；
		String recvNumber = null; // recv手机号；
		String smtxt = null; // 短信内容；

		cmppe_result cr = null;

		int result = 0;

		try {
			cr = cmpp.readResPack(con);
			Res.log(Res.DEBUG, "收到数据包");
			result = cr.stat;

			switch (cr.pack_id) {
			case CMPP.CMPPE_NACK_RESP:
				Res.log(Res.DEBUG, "收到 nack pack数据包");
				break;

			case CMPP.CMPPE_LOGIN_RESP:
				cmppe_login_result cl;
				cl = (cmppe_login_result) cr;
				Res.log(Res.DEBUG, "收到 login_resp数据包 :返回状态为 " + cl.stat);
				break;

			case CMPP.CMPPE_LOGOUT_RESP:
				Res.log(Res.DEBUG, "收到 login_resp数据包 :返回状态为 " + cr.stat);
				break;

			case CMPP.CMPPE_DELIVER:
				Res.log(Res.DEBUG, "收到 deliver 数据包 :返回状态为 " + cr.stat);

				cmppe_deliver_result cd = (cmppe_deliver_result) cr;
				// 若为回执，取消息标识
				String msgID = new String(cd.msg_id);
				int status = 0;
				// String msgID="";
				// Res.log(Res.ERROR,"msgid:"+new String(cd.msg_id));
				// 2004年2月11日增加了回执报告消息ID
				// if(cd.status_from_rpt==1)
				// msgID=new String(cd.msg_id).substring(0,16);
				if (cd.status_rpt == 1)
					status = cd.stat;
				sendNumber = new String(cd.src_addr, 0, 11); // 源手机号码；
				recvNumber = new String(cd.dst_addr, 0, 11); // 目标手机号码；
				// 将短信内容解码出来。
				int sm_len = cd.sm_len;

				if (sm_len < 0)
					sm_len = 256 + sm_len;
				int data_code = cd.data_coding;

				// Res.log(Res.ERROR,"data_code"+data_code);
				switch (data_code) {
				case 21: {
					smtxt = Util.getASCII(cd.short_msg, sm_len);
					break;
				}
				case 15: {
					smtxt = new String(cd.short_msg, 0, sm_len, "UnicodeBig");
					break;
				}
				case 0: {
					smtxt = new String(cd.short_msg, 0, sm_len, "GBK");
					break;
				}
				case 4: {
					smtxt = Util.getASCII(cd.short_msg, sm_len);
					break;
				}
				default:
					smtxt = new String(cd.short_msg, 0, sm_len, "UnicodeBig");
					break;
				}
				// 2004年2月11日增加了msgID参数
				cmpp.cmpp_send_deliver_resp(con, cd.seq, cd.stat);
				// Res.log(Res.DEBUG, "收到的手机号码为：" + sendNumber + " 收到的信息内容为：" +
				// smtxt);
				insertTable(status, sendNumber, recvNumber, smtxt, mediaInfo
						.getMediaId(), msgID, data_code);
				break;

			case CMPP.CMPPE_CANCEL_RESP:
				Res.log(Res.DEBUG, "收到 cancel 数据包 :返回状态为 " + cr.stat);

			case CMPP.CMPPE_ACTIVE_RESP:
				Res.log(Res.DEBUG, "收到 active_resp 数据包 :返回状态为 " + cr.stat);
				break;
			case CMPP.CMPPE_ACTIVE:
				Res.log(Res.DEBUG, "收到 active 数据包 :返回状态为 " + cr.stat);
				break;

			default:
				Res.log(Res.DEBUG, "收到 未知的数据包 :返回状态为 " + cr.stat);
				break;
			}

		} catch (UnknownPackException e) {
			e.printStackTrace();
			result = -8001;
			Res.log(Res.DEBUG, "收到了未知格式的包");
		} catch (DeliverFailException e) {
			e.printStackTrace();
			result = -8000;
			Res.log(Res.DEBUG, "出现网络异常");
			errorLogout();
		} catch (IOException e) {
			e.printStackTrace();
			result = -8000;
			Res.log(Res.DEBUG, "出现网络异常");
			errorLogout();
		}
		return result;
	}

	private void errorLogout() {
		Res.log(Res.INFO, "开始启动计划工作，退出与服务器的连接");
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

	class Test_Conn_to_MOBILE implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			errorLogout();
		};
	}

}