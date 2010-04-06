/*
 * Created on 2007-1-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.nci.ums.channel.outchannel;


import com.cmcc.mm7.vasp.message.*;
import com.cmcc.mm7.vasp.service.MM7Sender;
import com.cmcc.mm7.vasp.common.*;
import com.cmcc.mm7.vasp.conf.*;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;

/**
 * @author c
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MM7OutChannel extends OutChannel
{
	
	private String sPath = "c:\\ums\\config\\";
	private MM7Config mm7Config  ;
	private MM7Sender mm7Sender;
	private MM7SubmitReq submit;
	private MsgInfo[] msgInfos;
	private boolean responseFlag;
	private int submitCount;
	private int responseCount;
	private int msgCount;
	private int activeTime=0;

	  //定时器设为一分钟
//	  private javax.swing.Timer my_timer;
//	  private CMPPOutChannel.ResponseOutTime  responseOutTime=new CMPPOutChannel.ResponseOutTime();

	 public MM7OutChannel(MediaInfo mediaInfo) {
	    super(mediaInfo);
	    this.mediaInfo=mediaInfo;
	    this.mm7Config = new MM7Config(sPath+"mm7Config.xml");
	    this.mm7Config. setConnConfigName(sPath+"ConnConfig.xml");
	    
	    try{
	    	this.mm7Sender = new MM7Sender(this.mm7Config);
	    }
	    catch(Exception e)
		{
	    	System.out.println("发送消息初始化失败: MSG:" + e.toString() );
		}
	    
	    this.submit = new MM7SubmitReq();
   	   
//	    mm7= new CMPP();          此地没有
	    msgInfos=new MsgInfo[16];
	  }
	
	 
	 
	 public void run()
	    {
//	        my_timer = new javax.swing.Timer(1000 * 60 * 1, responseOutTime); //1分钟后未响应重启
//	        my_timer.setRepeats(false);
	        while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
	          //建立连接。

	          //测试时为备注
//	          getConnection();   //建立连接

	          if (stop)
	            break;
//	          Res.log(Res.DEBUG, "成功建立连接！");

	          //发送消息
	          try {
	              msgInfos=new MsgInfo[16];
	              msgCount=getMsgInfos(msgInfos);             // 需要修改
	              //发送滑动窗口中消息
	              submitCount=0;
	              
	              for(int i=0;i<msgCount;i++)
	              {
	                  int response=sendMsg(msgInfos[i]);     // 获取发送
	                  //int response=0;
	                  if(response==0)
	                  {
	                    submitCount=submitCount+1;
	                  }
	              }
	              //等待回应

	              responseFlag=false;
	              responseCount=0;
                 Res.log(Res.DEBUG,"receive response end");
	                  endSubmit(msgInfos,msgCount);
	              
	          }catch(Exception e){
	              Res.log(Res.ERROR,"cmppoutchannel run error:"+e);
                      Res.logExceptionTrace(e);
	              endSubmit(msgInfos,msgCount);
	              sleepTime(10000000);
	          }
	          if(msgCount==0)
	              sleepTime(1000);

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
	 
   public int sendMsg(byte dst_addr[][], int sm_len, byte short_msg[],byte src_addr[][],byte count,MsgInfo msgInfo) throws UMSConnectionException 
   {
//     cmppe_submit sub = new cmppe_submit();
     int result = 0;
     //sp ID号码，最大7字节
 //    List ccList = new ArrayList();
 //还没有处理转功能
     
     this.submit.setTransactionID(msgInfo.getBatchNO()+msgInfo.getAppSerialNo());       ///*设置MM7_submit.REQ/MM7_submit.RES对的标识，必备*/
     this.submit.setVASPID("1111") ; //设置SP代码，必备
     this.submit.setVASID("2222") ; //设置服务代码，必备
     this.submit.setServiceCode("3333"); //设置业务代码，必备
     this.submit.setLinkedID("4444"); /*设置链接标识，标识传送至VASP的上一个有效消息的对应关系；可选*/
     this.submit.setSenderAddress(msgInfo.getSendID());  //设置MM始发方的地址（填写SP的服务代码，或者填写让用户回复SP的长号码，长号码构成：SP的服务代码＋业务代码＋操作码），必备
     this.submit.setChargedPartyID(msgInfo.getRecvID()); //设置付费方的手机号码，必备
 //    this.submit.setTo("接收方地址"); //设置接收方MM的地址
     this.submit.addTo(msgInfo.getRecvID()); //增加单个接受方地址
//     this.submit.setCc("抄送方地址"); //设置抄送方MM的地址
//     this.submit.addCc("单个抄送方地址"); //增加单个抄送方地址
//     this.submit.setBcc("密送方地址"); //设置密送方MM的地址
 //    this.submit.addBcc("单个密送方地址"); //增加单个密送方地址
     
    
 //    this.submit.setMessageClass("MM的类别"); /*设置MM的类别（例如，广告、信息服务和计费），可选，具体有：Auto、Personal、Advertisement、Informational*/
  //   this.submit.setTimeStamp("提交MM的日期和时间"); /*提交MM的时间和日期（时间戳）,格式如2004-02-09T10：21：07，可选*/
//     this.submit.setExpiryDate("指定超时时间"); /*设置MM指定的超时时间（绝对或相对时间），可选*/
 //    this.submit.setEarliestDeliveryTime("最早理想时间"); /*设置将MM传送给接收方的最早理想时间(绝对或相对时间)，可选*/
     this.submit.setDeliveryReport(true); /*设置是否需要发送报告的请求（boolean值）,可选*/
     this.submit.setReadReply(true); /*设置通过请求传送一个读取报告进行确认，可选*/
//    this.submit.setReplyCharging("应答计费的请求"); /*设置应答计费的请求（boolean值），可选*/
//     this.submit.setReplyDeadline("提交应答的最迟时间"); /*设置在应答计费的情况下，向接收方提交应答的最迟时间（绝对或相对时间），可选*/
//     this.submit.setReplyChargingSize("应答MM的最大大小"); /*设置在应答计费的情况下，提供给接收方的应答MM的最大大小，可选*/
     this.submit.setPriority((byte)0); /*消息的优先级（重要性）（0=最低优先级，1=正常，2=紧急），byte类型的值，可选*/
     
 //    this.submit.setAllowAdaptations("VASP是否允许修改内容"); /*设置VASP是否允许修改内容（boolean值，默认为真），可选*/
 //    this.submit.setChargedParty("VASP所提交MM的付费方"); /*设置VASP所提交MM的付费方，例如，发送方、接收方、发送方和接收方或两方均不付费，可选，0：Sender、1：Recipients、2：Both、3：Neither、4：ThirdParty*/
   
     this.submit.setSubject("内部调式"); /*设置多媒体消息的标题，可选*/
    //  this.submit.setDistributionIndicator("是否可重新分发"); /*设置VASP是否可重新分发MM的内容（boolean值，true为可以，false为不可以），可选*/
         
     //创建消息内容体
      
     MMContent content = new MMContent();
     content.setContentType(MMConstants.ContentType. MULTIPART_MIXED);   //  **设置附件的类型，若不包含SMIL格式的文件，则设置类型为MMConstants.ContentType. MULTIPART_MIXED，若包含SMIL格式的文件，则设置类型为MMConstants.ContentType. MULTIPART_RELATED*/

     
     
     for(int i=0 ; i<msgInfo.getFile().size();i++)
     {
     	MMContent sub1 = MMContent.createFromFile( (String)msgInfo.getFile().get(i) );
     	sub1.setContentType(getFileType((String)msgInfo.getFile().get(i))); 
     	content.addSubContent(sub1);
     }
     
     submit.setContent(content);
     
     MM7RSRes res = mm7Sender.send(submit); 
     /*可以根据StatusCode来判断本次发送是否成功，若成功能得到MessageID等信息。StatusCode可能得到的值，具体可见本文中的请求状态码说明。*/
    
     //以下作为调试用， 不需要讨论后确定，消息的返回可以统一在IN渠道中来处理
     if(res instanceof  MM7SubmitRes)
	 {
		 MM7SubmitRes submitRes = (MM7SubmitRes)res;
		 System.out.println("StatusCode="+ submitRes.getStatusCode());
		 System.out.println("StatusText="+ submitRes.getStatusText());
		 System.out.println("MessageID="+ submitRes .getMessageID());
		 UpdateMsgId(msgInfo.getBatchNO(),msgInfo.getSerialNO(),submitRes .getMessageID());   //保存messageid		 
	 }
     
   
      
     return result;
   }

	private MMContentType getFileType(String  sFile)
	{
		MMContentType result= MMConstants.ContentType.TEXT;
		
		int nIndex = sFile.indexOf('.');		
		if (nIndex>0)
		{
			String sTmp  = sFile.substring(nIndex);
			
			if (sTmp.toUpperCase().equals("GIF"))
				result = MMConstants.ContentType.GIF;
			else if (sTmp.toUpperCase().equals("JPEG"))
				result = MMConstants.ContentType.JPEG;
			else if (sTmp.toUpperCase().equals("MIDI"))
				result =MMConstants.ContentType.MIDI;
							 	
		}		
		return result;
	}

}
