/*
 * Created on 2007-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.nci.ums.channel.inchannel;
/*
import com.nci.ums.channel.mm7manager.*;
//import com.cmcc.mm7.vasp.service.MM7Receiver;
import com.cmcc.mm7.vasp.conf.*;

import com.cmcc.mm7.vasp.message.*;
import com.cmcc.mm7.vasp.service.MM7Sender;
import com.cmcc.mm7.vasp.common.*;
import com.cmcc.mm7.vasp.service.MM7ReceiveServlet;



import java.net.*;
import java.io.*;
import java.util.*;
import org.jdom.*;
import javax.xml.parsers.*;
import sun.misc.*;
import org.apache.log4j.*;
import java.net.*;
import java.security.*;
*/

import java.io.*;
import java.util.*;
import com.cmcc.mm7.vasp.service.MM7ReceiveServlet;
import com.cmcc.mm7.vasp.message.*;
import com.cmcc.mm7.vasp.conf.*;
import com.cmcc.mm7.vasp.common.*;



/**
 * @author c
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MM7InChannel extends MM7ReceiveServlet{
	
	public MM7DeliveryReportReq deliveryReportReq;
	public MM7ReadReplyReq readReplyReq;

	
	 
	public void beginRun()
	{        
	   MM7Config mm7Config = new MM7Config("c:\\ums\\config\\mm7Config1.xml");
	       
	   mm7Config.setConnConfigName("c:\\ums\\config\\ConnConfig1.xml"); //必备
	   
	   System.out.println("开始运行接受彩信线程");
	   
	   MM7InChannel receiver = new MM7InChannel();
//	   receiver.setConfig(mm7Config); //必备
//	   receiver.start();
	  
	   System.out.println("开始运行接受彩信线程启动成功");
	  
	   for(;;);
	}
	
	
	 
	
	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq request)
    {
	   
	     deliveryReportReq = request;
	     System.out.println("收到发送方"+request.getSender()+"提交的消息，其MessageID为："+request.getMessageID());
	     System.out.println("MMSC的标识符为："+request. getMMSRelayServerID()) ;
	         
	     MM7DeliveryReportRes mm7DeliveryReportRes = new MM7DeliveryReportRes ();
	     mm7DeliveryReportRes.setStatusCode(MMConstants.RequestStatus.SUCCESS) ;/*设置请
	            求完成状态，必备，以便表明SP已经接收到状态报告。一般设1000。*/
	     mm7DeliveryReportRes.setStatusText("所用状态文本说明"); /*设置所用状态的文本说明，应限定请求状态，可选*/
	      //返回给MM7 API，以便API将MM7DeliveryReportRes返回给MMSC。
	     return mm7DeliveryReportRes;
	    
	}

    
    
    
	public MM7VASPRes doReadReply(MM7ReadReplyReq request)
    {
     
     readReplyReq = request;
     System.out.println("收到手机"+request.getSender()+"的已读消息，其MessageID为："+request.getMessageID());
     System.out.println("MMSC的标识符为："+request. getMMSRelayServerID()) ;
     //SP需要进行一些处理，例如构建MM7ReadReplyRes消息，返回给MM7 API
     MM7ReadReplyRes readReplyRes = new MM7ReadReplyRes ();
     readReplyRes.setStatusCode(MMConstants.RequestStatus.SUCCESS) ;/*设置请求完成状态，必备，以便表明SP已经接收到读后回复报告。成功设1000。*/
     readReplyRes.setStatusText("所用状态文本说明"); /*设置所用状态的文本说明，应限定请求状态，可选*/
     //返回给MM7 API，以便API将MM7ReadReplyRes返回给MMSC。
     return readReplyRes;
    }
    
    
	public MM7VASPRes doDeliver(MM7DeliverReq mm7DeliverReq) 
    {
    	  MM7DeliverRes res = new MM7DeliverRes();
    	  
    	  int i = 0;
    	    
    	    System.out.println("transactionid=" + mm7DeliverReq.getTransactionID());
    	    
    	    if(mm7DeliverReq.isToExist())
    	    {
    	    List to = mm7DeliverReq.getTo();
    	    for(i = 0;i < to.size();i ++)
    	    {
    	    System.out.println("to="+to.get(i));
    	    }
    	    }

    	    if(mm7DeliverReq.isCcExist())
    	    {
    	     List cc = mm7DeliverReq.getCc();
    	    for(i = 0;i < cc.size();i ++)
    	    {
    	    System.out.println("cc="+cc.get(i));
    	    }
    	   }

    	    if(mm7DeliverReq.isBccExist())
    	    {
    	     List bcc = mm7DeliverReq.getBcc();
    	    for(i = 0;i < bcc.size();i ++)
    	    {
    	    System.out.println("bcc="+bcc.get(i));
    	    }
    	    }
    	    
    	    if(mm7DeliverReq.isLinkedIDExist())
    	    {
    	    System.out.println("linkedid="+mm7DeliverReq.getLinkedID());
    	    }
    	    
    	    if(mm7DeliverReq.isMMSRelayServerIDExist())
    	    {
    	    System.out.println("mmsrelayserverid="+mm7DeliverReq.getMMSRelayServerID());
    	    }
    	    
    	    if(mm7DeliverReq.isPriorityExist())
    	    {
    	    System.out.println("priority="+mm7DeliverReq.getPriority());
    	    }
    	    
    	    if(mm7DeliverReq.isReplyChargingIDExist())
    	    {
    	    System.out.println("replycharging="+mm7DeliverReq.getReplyChargingID());
    	    }
    	    
    	    if(mm7DeliverReq.isSenderExist())
    	    {
    	    System.out.println("sender="+mm7DeliverReq.getSender());
    	    }
    	    
    	    if(mm7DeliverReq.isSubjectExist())
    	    {
    	    try{
    	    System.out.println("subject="+mm7DeliverReq.getSubject());
    	    }catch(Exception e)
    	    {
    	    System.err.println(e);
    	    }
    	    }
    	    
    	    if(mm7DeliverReq.isTimeStampExist())
    	    {
    	    System.out.println("timestamp="+mm7DeliverReq.getTimeStamp());
    	    }
    	    
    	if(mm7DeliverReq.isContentExist())
    	{
    	System.out.println("exist");
    	    MMContent parentContent = mm7DeliverReq.getContent();
    	    if(parentContent.isMultipart())
    	    {
    	    System.out.println("multipart");
    	    List contentList = parentContent.getSubContents();
    	    System.out.println("i="+contentList.size());
    	    for(i = 0;i < contentList.size();i ++)
    	    {
    	    MMContent mmContent = (MMContent)contentList.get(i);
    	    String contentID = mmContent.getContentID();
    	    if(contentID == null || contentID.length() == 0)
    	    contentID = "zxme" + i + ".";
    	    MMContentType mmContentType = mmContent.getContentType();
    	    System.out.println("contenttype="+mmContentType.getPrimaryType()+"/"+mmContentType.getSubType());
    	    if(mmContentType.getSubType().equalsIgnoreCase("jpeg"))
    	    contentID = contentID + "jpg";
    	    else if(mmContentType.getSubType().equalsIgnoreCase("gif"))
    	    contentID = contentID + "gif";
    	    else if(mmContentType.getSubType().equalsIgnoreCase("midi"))
    	    contentID = contentID + "mid";
    	    else if(mmContentType.getSubType().equalsIgnoreCase("png"))
    	    contentID = contentID + "png";
    	    else if(mmContentType.getPrimaryType().equalsIgnoreCase("text"))
    	    contentID = contentID + "txt";
    	    
    	    System.out.println("contentID="+contentID);
    	    byte content[] = mmContent.getContent();
    	    try
    	    {
    	    FileOutputStream fileStream = new FileOutputStream("c:\\temp\\"+contentID);
    	    fileStream.write(content);
    	    fileStream.close();
    	    }
    	    catch(IOException e)
    	    {
    	    System.err.println(e);
    	    }
    	    }
    	}
    	else
    	{
    	System.out.println("singlepart");
    	    String contentID = parentContent.getContentID();
    	    System.out.println("contentID="+contentID);
    	    if(contentID == null || contentID.length() == 0)
    	    contentID = "zxme.single";
    	    MMContentType mmContentType = parentContent.getContentType();
    	    if(mmContentType.getSubType().equalsIgnoreCase("jpeg"))
    	    contentID = contentID + "jpg";
    	    else if(mmContentType.getSubType().equalsIgnoreCase("gif"))
    	    contentID = contentID + "gif";
    	    else if(mmContentType.getSubType().equalsIgnoreCase("mid"))
    	    contentID = contentID + "mid";
    	    else if(mmContentType.getSubType().equalsIgnoreCase("png"))
    	    contentID = contentID + "png";
    	    else if(mmContentType.getPrimaryType().equalsIgnoreCase("txt"))
    	    contentID = contentID + "txt";

    	    byte content[] = parentContent.getContent();
    	    try
    	    {
    	    FileOutputStream fileStream = new FileOutputStream("c:\\temp\\"+contentID);
    	    fileStream.write(content);
    	    fileStream.close();
    	    }
    	    catch(IOException e)
    	    {
    	    System.err.println(e);
    	    e.printStackTrace();
    	    }
    	}
    	}
    	System.out.println("over");
    	    res.setTransactionID(mm7DeliverReq.getTransactionID());
    	    res.setStatusCode(1000);
    	    return res;

    	  
    	  
    //	  return(MM7VASPRes) res;
    	 
    }

	/*
    public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq) 
    {
      System.out.println("transactionid="+mm7DeliveryReportReq.getTransactionID());
      MM7DeliveryReportRes res = new MM7DeliveryReportRes();
      res.setTransactionID(mm7DeliveryReportReq.getTransactionID());
      res.setStatusCode(1000);
      return res;
    }

    //处理到VASP的读后回复报告
    public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq)
    {
      System.out.println("transactionid="+mm7ReadReplyReq.getTransactionID());
      MM7ReadReplyRes res = new MM7ReadReplyRes();
      res.setTransactionID(mm7ReadReplyReq.getTransactionID());
      res.setStatusCode(1000);
      return res;
    }
*/
    

	
}
