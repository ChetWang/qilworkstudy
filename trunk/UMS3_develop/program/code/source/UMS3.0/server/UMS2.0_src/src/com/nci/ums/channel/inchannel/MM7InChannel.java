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
	       
	   mm7Config.setConnConfigName("c:\\ums\\config\\ConnConfig1.xml"); //�ر�
	   
	   System.out.println("��ʼ���н��ܲ����߳�");
	   
	   MM7InChannel receiver = new MM7InChannel();
//	   receiver.setConfig(mm7Config); //�ر�
//	   receiver.start();
	  
	   System.out.println("��ʼ���н��ܲ����߳������ɹ�");
	  
	   for(;;);
	}
	
	
	 
	
	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq request)
    {
	   
	     deliveryReportReq = request;
	     System.out.println("�յ����ͷ�"+request.getSender()+"�ύ����Ϣ����MessageIDΪ��"+request.getMessageID());
	     System.out.println("MMSC�ı�ʶ��Ϊ��"+request. getMMSRelayServerID()) ;
	         
	     MM7DeliveryReportRes mm7DeliveryReportRes = new MM7DeliveryReportRes ();
	     mm7DeliveryReportRes.setStatusCode(MMConstants.RequestStatus.SUCCESS) ;/*������
	            �����״̬���ر����Ա����SP�Ѿ����յ�״̬���档һ����1000��*/
	     mm7DeliveryReportRes.setStatusText("����״̬�ı�˵��"); /*��������״̬���ı�˵����Ӧ�޶�����״̬����ѡ*/
	      //���ظ�MM7 API���Ա�API��MM7DeliveryReportRes���ظ�MMSC��
	     return mm7DeliveryReportRes;
	    
	}

    
    
    
	public MM7VASPRes doReadReply(MM7ReadReplyReq request)
    {
     
     readReplyReq = request;
     System.out.println("�յ��ֻ�"+request.getSender()+"���Ѷ���Ϣ����MessageIDΪ��"+request.getMessageID());
     System.out.println("MMSC�ı�ʶ��Ϊ��"+request. getMMSRelayServerID()) ;
     //SP��Ҫ����һЩ�������繹��MM7ReadReplyRes��Ϣ�����ظ�MM7 API
     MM7ReadReplyRes readReplyRes = new MM7ReadReplyRes ();
     readReplyRes.setStatusCode(MMConstants.RequestStatus.SUCCESS) ;/*�����������״̬���ر����Ա����SP�Ѿ����յ�����ظ����档�ɹ���1000��*/
     readReplyRes.setStatusText("����״̬�ı�˵��"); /*��������״̬���ı�˵����Ӧ�޶�����״̬����ѡ*/
     //���ظ�MM7 API���Ա�API��MM7ReadReplyRes���ظ�MMSC��
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

    //����VASP�Ķ���ظ�����
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
