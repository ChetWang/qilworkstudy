/**
 * <p>
 * Title: HuaWeiChannel.java
 * </p>
 * <p>
 * Description: 华为信息机外拨渠道类，继承OutChannel 实现华为信息机对信息的发送过程
 * 思路：设点定时器，定期扫描UMS..out_ready数据表中的记录， 
 * 		并把这些记录通过华为提供的API(smentry.jar)转发到华为信息机的sms..tbl_SMToSend数据表
 * </p>
 * <p>
 * Copyright: 2005 Hangzhou NCI System Engineering， Ltd.
 * </p>
 * <p>
 * Company: Hangzhou NCI System Engineering， Ltd.
 * </p>
 * Date Author Changes 2005-4-20 郑先全 Created
 * 
 * @version 1.0
 */
package com.nci.ums.channel.outchannel;

import java.util.Date;

import com.huawei.api.SMEntry;
import com.huawei.api.SMException;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.util.Res;

public class HuaWeiOutChannel extends OutChannel {
	
	private String dbHostName = "localhost";
	private String dbUserName = "ums";
	private String dbPassword = "ums";

	private MsgInfo[] msgInfos;
	private boolean responseFlag;
	private int submitCount;
	private int responseCount;
	private int msgCount;
	private int activeTime = 0;

	//定时器设为一分钟
	private javax.swing.Timer my_timer;

	/**
	 * @param mediaInfo
	 */
	public HuaWeiOutChannel(MediaInfo mediaInfo) {
		super(mediaInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		//System.out.println("running...");
		my_timer.setRepeats(false);
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
			//1.建立连接
			login();
			
			if (stop)
				break;
			Res.log(Res.DEBUG, "成功建立与华为信息机的连接！");

			//2.发送消息
			try {
				msgInfos = new MsgInfo[16];
				msgCount = getMsgInfos(msgInfos);
				//发送滑动窗口中消息
				submitCount = 0;
				for (int i = 0; i < msgCount; i++) {
					//测试时为备注
					int response = sendMsg(msgInfos[i]);
					//int response=0;
					if (response == 0) {
						submitCount = submitCount + 1;
					}
				}
				
				endSubmit(msgInfos,msgCount);
				//等待回应
				/*
				 * responseFlag = false; responseCount = 0; if (submitCount > 0) {
				 * my_timer.start(); while (!Thread.interrupted() &&
				 * !quitFlag.getLockFlag() && !stop && !responseFlag) {
				 * //testResponse(); // 测试时为备注 readPa(con); } my_timer.stop();
				 * Res.log(Res.DEBUG, "receive response end");
				 * endSubmit(msgInfos, msgCount); }
				 */
			} catch (Exception e) {
				endSubmit(msgInfos,msgCount);
				logout();
				Res.log(Res.ERROR, "HuaWeiOutChannel run error:" + e);
                                Res.logExceptionTrace(e);
				sleepTime(1);
			}
			if (msgCount == 0)
				sleepTime(1);

		}

	}

	/*
	 * 发送一条信息，返回数值为整数。
	 */
	public int sendMsg(MsgInfo msgInfo) throws UMSConnectionException {
		int result = 0;
		try {
			//submitShortMessage api接口：
			/*
			 * atTime：发送短信的时间。(Java.util.Date) sourceAddr：待发送短信的源地址。
			 * destAddr：待发送短信的目的地址。 content：短信内容。
			 * needStateReport：发送该短信是否需要状态报告。（注：使用状态报告可以确认对方是否一定收到）。该参数可以使用两个值，0：表示不需要状态报告，1：表示需要状态报告。
			 * serviceID：业务类型。业务类型将用于运营商端对短信进行计费时使用，该参数不能超过10个字符。
			 * feeType：资费类型。该参数只能是以下几个值：“01”表示对用户免费；“02”表示对用户按条收取信息费，具体收费将依据下一个参数；“03”表示对用户按包月收取信息费。
			 * feeCode：资费代码。该参数依赖上一个参数，表示该短信的信息费（注：以分为单位），该参数不能超过六个字符。 异常： 1002,
			 * 1003,100501 至100509, 1010 （参考异常列表）
			 *  
			 */
			Date atTime = new Date();
			String sourceAddr = msgInfo.getSendID();
			String destAddr = msgInfo.getRecvID();
			String content = msgInfo.getContent();
			int needStateReport = Integer.parseInt(msgInfo.getReply());
			String serviceID = "";
			String feeType = msgInfo.getFeeType() + "";
			String feeCode = msgInfo.getFee() + "";

			Res.log(Res.DEBUG, result + " message sent in progress...");
			result = SMEntry.submitShortMessage(atTime, sourceAddr, destAddr,
					content, needStateReport, serviceID, feeType, feeCode);
			Res.log(Res.DEBUG, result + " message sent ok.");
		} catch (SMException e) {
			Res.log(Res.ERROR, "HuaWeiOutChannel sendMsg error:" 
					+ ".type." + e.getErrorType()
					+ ".code." + e.getErrorCode()
					+ ".desc." + e.getErrorDesc());
                        Res.logExceptionTrace(e);
		}
		return result;
	}
	
	private void login() {
		Res.log(Res.DEBUG, "开始启动计划工作，与服务器建立连接...");
		try {
			//1.建立连接,在smentry.jar包
			SMEntry.init(dbHostName, dbUserName, dbPassword);
		} catch (SMException e1) {
			e1.printStackTrace();
		}
	}

	private void logout() {
		Res.log(Res.DEBUG, "开始启动计划工作，退出与服务器的连接...");
		try {
			SMEntry.cleanUp();
			Res.log(Res.DEBUG, "正常退出");
		} catch (Exception ee) {
			Res.log(Res.DEBUG, "退出异常");
		}
	}


	public static void main(String[] args) {
		/*
		 * MediaInfo构造函数所需要的参数 String mediaId, String mediaName,String
		 * className, String ip, int port, int timeOut, int repeatTimes, int
		 * startWorkTime,int endWorkTime, int type, String loginName, String
		 * loginPassword, int sleepTime,int status
		 */
		MediaInfo mediaInfo = new MediaInfo("1", "华为信息机",
				"com.nci.ums.channel.outchannel.HuaWeiOutChannel",
				"192.168.0.74", 80, 60, 5, 1, 1, 1, "ums", "ums", 60, 0);

		HuaWeiOutChannel out = new HuaWeiOutChannel(mediaInfo);
		MsgInfo msgInfo = new MsgInfo();
		msgInfo.setSendID("1300000001");
		msgInfo.setRecvID("1300000002");
		msgInfo.setContent("测试内容啦");
		msgInfo.setReply("1");
		//serviceID = "EIE";
		msgInfo.setFeeType(1);
		msgInfo.setFee(1);
		try {
			int result = out.sendMsg(msgInfo);
			Res.log(Res.DEBUG, "HuaWeiOutChannel 发送OK");
		} catch (UMSConnectionException e) {
			e.printStackTrace();
		}
	}
}