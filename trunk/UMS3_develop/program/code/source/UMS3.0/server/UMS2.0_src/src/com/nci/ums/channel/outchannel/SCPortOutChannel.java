/**
 * <p>Title: </p>
 * <p>Description:
 *    
 * </p>
 * <p>Copyright: 2006 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date            Author          Changes          Version
   2006-9-21          荣凯           Created           1.0
 */
package com.nci.ums.channel.outchannel;

import java.sql.SQLException;
import com.nci.scport.ums.*;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.util.Res;

public class SCPortOutChannel extends OutChannel {

	/**
	 * @param args
	 */
	SCSmsConnect dbconnect=null;

	private MsgInfo[] msgInfos;
	private int submitCount;
	private int msgCount;

	//定时器设为一分钟
	private javax.swing.Timer my_timer;
	private SCPortOutChannel.ResponseOutTime  responseOutTime=new SCPortOutChannel.ResponseOutTime();

	/**
	 * @param mediaInfo
	 */
	public SCPortOutChannel(MediaInfo mediaInfo) {
		super(mediaInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
			if (stop)
				break;
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
			} catch (Exception e) {
				//endSubmit(msgInfos,msgCount);
				logout();
				Res.log(Res.ERROR, "HuaWeiOutChannel run error:" + e);
                                Res.logExceptionTrace(e);
				sleepTime(1);
			}
			if (msgCount == 0)
				sleepTime(10);
		}
	}

	/*
	 * 发送一条信息，返回数值为整数。
	 */
	public int sendMsg(MsgInfo msgInfo) throws UMSConnectionException {
		int result = 0;
		dbconnect=null;
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
			
			dbconnect=new SCSmsConnect();

			String sourceAddr = msgInfo.getSendID();
			String destAddr = msgInfo.getRecvID();
			String content = msgInfo.getContent();

			dbconnect.prepareStatement("insert into mtqueue(destTermID,srcTermID,msgContent) values(?,?,?)");
			
			dbconnect.setString(1,destAddr);
			dbconnect.setString(2,sourceAddr);
			dbconnect.setString(3,content);

			dbconnect.executeUpdate();
			OpData(0,msgInfo,"");
			Res.log(Res.DEBUG, result + " message sent in progress...");
			
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "SCYCPortOutChannel sendMsg error:数据库链接异常!"); 
                        Res.logExceptionTrace(e);
		} catch (Exception e) {
			Res.logExceptionTrace(e);
			e.printStackTrace();
		}finally{
			if(dbconnect!=null)
				try {
					dbconnect.close();
				} catch (Exception e1) {
					// TODO 自动生成 catch 块
					e1.printStackTrace();
				}
		}
		return result;
	}
	
	private void login()  {
		Res.log(Res.DEBUG, "开始启动计划工作，与服务器建立连接...");
		try {
			//1.建立连接
			dbconnect=new SCSmsConnect();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void logout() {
		Res.log(Res.DEBUG, "开始启动计划工作，退出与服务器的连接...");
		try {
			if(dbconnect!=null){
				dbconnect.close();
			}
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
		MediaInfo mediaInfo = new MediaInfo("1", "四川烟草UMS",
				"com.nci.ums.channel.outchannel.SCPortOutChannel",
				"localhost", 80, 60, 5, 1, 1, 1, "ums", "ums", 60, 0);

		SCPortOutChannel out = new SCPortOutChannel(mediaInfo);
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
	
    private void errorLogout()
    {
        Res.log(Res.DEBUG, "开始启动计划工作，退出与服务器的连接");
        try {
          if(dbconnect!=null)dbconnect.close();
          Res.log(Res.DEBUG, "正常退出");
        }
        catch (Exception ee) {
          Res.log(Res.DEBUG, "退出异常");
        }finally{
        	dbconnect=null;
        }
    }	
    class ResponseOutTime implements java.awt.event.ActionListener {
    	public void actionPerformed(java.awt.event.ActionEvent e) {
    		errorLogout();
    	};
    }

}
