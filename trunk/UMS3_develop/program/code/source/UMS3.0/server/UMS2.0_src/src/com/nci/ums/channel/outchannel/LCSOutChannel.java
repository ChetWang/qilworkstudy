/*
 * Created on 2007-1-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.nci.ums.channel.outchannel;

import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;

 
import com.nci.ums.channel.myproxy.*;

/**
 * @author c
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LCSOutChannel extends OutChannel
{ 
	 
    private MsgInfo[] msgInfos;
	private boolean responseFlag;
	private int submitCount;
	private int responseCount;
	private int msgCount;
	private int activeTime=0;
	private  SendMessage  sender = null;

	  //定时器设为一分钟
//	  private javax.swing.Timer my_timer;
//	  private CMPPOutChannel.ResponseOutTime  responseOutTime=new CMPPOutChannel.ResponseOutTime();

	 public LCSOutChannel(MediaInfo mediaInfo) {
	    super(mediaInfo);
	    this.mediaInfo=mediaInfo;	    	    	 
	    msgInfos=new MsgInfo[16];
//	    sender = new SendMessage(mediaInfo);
	    sender = SendMessage.getInstance(mediaInfo);
	  }
	 
	  
	 public void run()
	    {
//	        my_timer = new javax.swing.Timer(1000 * 60 * 1, responseOutTime); //1分钟后未响应重启
//	        my_timer.setRepeats(false);
	 	  
	 	    long  lCount = 0;
	 	    
	 	    	 	    	     
	        while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
	          //建立连接。

	          //测试时为备注
//	          getConnection();   //建立连接

	          if (stop)
	            break;
//	          Res.log(Res.DEBUG, "成功建立连接！");

	          //发送消息
	          try {
	          	/*
	          	 * 考虑到现在的公司浙江移动的网管无法取到sequence_id  暂时单个发送
	          	 */
	              msgInfos=new MsgInfo[16];                    
	              msgCount=getMsgInfos(msgInfos);             // 需要修改
	              //发送滑动窗口中消息
	              submitCount=0;
	              
	              for(int i=0;i<msgCount;i++)
	              {
	                  int response=sendMsg(msgInfos[i]);     // 获取发送
	                  //int response=0;
	                  if(response==0)      //发送成功
	                  {
	                    submitCount=submitCount+1;
	                     
	                    MsgInfo msgInfo=msgInfos[i];
	                    
	                    OpData(0,msgInfo,"");
                        msgInfo.setStatus(0);
                        msgInfo.setRetCode(0);
	                     

	                    responseCount=responseCount+1;
	   
	                     if(responseCount==submitCount)
	                        responseFlag=true;
	                    
	                    
	                    //在此处理
	                  }
	              }
	              //等待回应要修改

	              responseFlag=false;
	              responseCount=0;
                  Res.log(Res.DEBUG,"receive response end");
	              endSubmit(msgInfos,msgCount);
	              
	          }catch(Exception e){
	              Res.log(Res.ERROR,"cmppoutchannel run error:"+e);
                      Res.logExceptionTrace(e);
	              endSubmit(msgInfos,msgCount);
	              sleepTime(1000);
	          }
	          if(msgCount==0)
	          {
	              sleepTime(1000);
	          }
	          
	          lCount = lCount + 1000;
	          if (lCount >15*6*1000){
	          	sender.close();
	      
	          	System.out.println("-----------调试已经超过3分钟，连接关闭---------------");
	          }

	      }
	    }
	 
	 
	 public int sendMsg(MsgInfo msgInfo) throws UMSConnectionException {
        int result=1;
      try{
            byte[] short_msg=Util.getByte(msgInfo.getContent(),msgInfo.getContentMode());
            //处理群发手机号
            String[] temp_dst=msgInfo.getRecvID().split(",");
            int count=temp_dst.length;
            byte[][] dstCode = new byte[count+1][15];
            for(int i=0;i<count;i++){
                dstCode[i] = Util.getByteString(temp_dst[i]);
            }
            //群发处理完毕
            //单发dstCode[0] = Util.getByteString(dst);
            byte[][] srcCode=new byte[2][15];
            srcCode[0]=Util.getByteString(msgInfo.getSendID());
            int sm_len =  short_msg.length;
            result=sendMsg(dstCode, sm_len, short_msg,srcCode,(byte)count,msgInfo);
      }catch(Exception e ){

      }
      return result;
    }
	 
// 发送单个记录（有可能是单个短消息，也有可能是群发短消息	 
   public int sendMsg(byte dst_addr[][], int sm_len, byte short_msg[],byte src_addr[][],byte count,MsgInfo msgInfo) throws UMSConnectionException 
   {
 
     int nResult = -1 ;
      
    
     nResult = sender.sendMsg(dst_addr,sm_len,short_msg,src_addr,count,msgInfo);
     
     return nResult;
   }

	 //如果对文件操作可以参考MM7OutChannel 渠道 

}
