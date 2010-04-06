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

import java.sql.ResultSet;
import java.sql.SQLException;
import com.nci.nbport.ums.NBSmsConnect;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;

public class NBPortOutChannel extends OutChannel {
	NBSmsConnect dbconnect=null;

	private MsgInfo[] msgInfos;
	private boolean responseFlag;
	private int submitCount;
	private int responseCount;
	private int msgCount;
	private int activeTime = 0;

	//定时器设为一分钟
	private javax.swing.Timer my_timer;
	private NBPortOutChannel.ResponseOutTime  responseOutTime=new NBPortOutChannel.ResponseOutTime();

	/**
	 * @param mediaInfo
	 */
	public NBPortOutChannel(MediaInfo mediaInfo) {
		super(mediaInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		//System.out.println("running...");
        //my_timer = new javax.swing.Timer(1000 * 60 * 1, responseOutTime); //1分钟后未响应重启
		//my_timer.setRepeats(false);
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
			//1.建立连接
//____________________测试---------------------
			stop = true;
			if (stop)
				break;
//---------------------------------------------
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
				
				//my_timer.stop();				
				//endSubmit(msgInfos,msgCount);
				
				
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
				//endSubmit(msgInfos,msgCount);
				logout();
				Res.log(Res.ERROR, "HuaWeiOutChannel run error:" + e);
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
			
			dbconnect=new NBSmsConnect();

			String sourceAddr = msgInfo.getSendID();
			String destAddr = "86"+msgInfo.getRecvID();
			String content = msgInfo.getContent();
			//int needStateReport = Integer.parseInt(msgInfo.getReply());
			String serviceID = "";
			String feeType = msgInfo.getFeeType() + "";
			String feeCode = msgInfo.getFee() + "";
			int sm_id=getSequenceNO(dbconnect);

			dbconnect.prepareStatement("insert into zhangfan.SM_SEND(SM_ID,ORGADDR,DESTADDR,MSG,MSG_TYPE,INPUT_TIME,SCHEDULE) values(?,?,?,?,?,?,?)");
			
			dbconnect.setString(1,"s"+sm_id);
			dbconnect.setString(2,sourceAddr);
			dbconnect.setString(3,destAddr);
			dbconnect.setString(4,content);
			dbconnect.setInt(5,1);
			
			dbconnect.setTimestamp(6,Util.getTimeStamp());
			dbconnect.setTimestamp(7,Util.getTimeStamp());
			dbconnect.executeUpdate();
			OpData(0,msgInfo,"");
			Res.log(Res.DEBUG, result + " message sent in progress...");
			
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "NBPortOutChannel sendMsg error:数据库链接异常!"); 
		} catch (Exception e) {
			// TODO 自动生成 catch 块
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
			dbconnect=new NBSmsConnect();
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
		MediaInfo mediaInfo = new MediaInfo("1", "华为信息机",
				"com.nci.ums.channel.outchannel.HuaWeiOutChannel",
				"192.168.0.74", 80, 60, 5, 1, 1, 1, "ums", "ums", 60, 0);

		NBPortOutChannel out = new NBPortOutChannel(mediaInfo);
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
	
	
	private int getSequenceNO(NBSmsConnect connect){
		int result=0;
		ResultSet rs=null;		
		try {
			rs = connect.executeQuery("select zhangfan.sms_send.nextval from dual");
			if(rs.next()){
				result=rs.getInt(1);
			}
			rs.close();
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			try{

				connect.executeUpdate("create sequence zhangfan.sms_send");
				
				rs = connect.executeQuery("select zhangfan.sms_send.nextval from dual");
				if(rs.next()){
					result=rs.getInt(1);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			if(rs!=null){try{rs.close();}catch(Exception e){}}
		}
		
		return result;
		
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