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

	  //��ʱ����Ϊһ����
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
	    	System.out.println("������Ϣ��ʼ��ʧ��: MSG:" + e.toString() );
		}
	    
	    this.submit = new MM7SubmitReq();
   	   
//	    mm7= new CMPP();          �˵�û��
	    msgInfos=new MsgInfo[16];
	  }
	
	 
	 
	 public void run()
	    {
//	        my_timer = new javax.swing.Timer(1000 * 60 * 1, responseOutTime); //1���Ӻ�δ��Ӧ����
//	        my_timer.setRepeats(false);
	        while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
	          //�������ӡ�

	          //����ʱΪ��ע
//	          getConnection();   //��������

	          if (stop)
	            break;
//	          Res.log(Res.DEBUG, "�ɹ��������ӣ�");

	          //������Ϣ
	          try {
	              msgInfos=new MsgInfo[16];
	              msgCount=getMsgInfos(msgInfos);             // ��Ҫ�޸�
	              //���ͻ�����������Ϣ
	              submitCount=0;
	              
	              for(int i=0;i<msgCount;i++)
	              {
	                  int response=sendMsg(msgInfos[i]);     // ��ȡ����
	                  //int response=0;
	                  if(response==0)
	                  {
	                    submitCount=submitCount+1;
	                  }
	              }
	              //�ȴ���Ӧ

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
            //����Ⱥ���ֻ���
            String[] temp_dst=msgInfo.getRecvID().split(",");
            int count=temp_dst.length;
            byte[][] dstCode = new byte[count+1][15];
            for(int i=0;i<count;i++){
                dstCode[i] = Util.getByteString(temp_dst[i]);
            }
            //Ⱥ���������
            //����dstCode[0] = Util.getByteString(dst);
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
     //sp ID���룬���7�ֽ�
 //    List ccList = new ArrayList();
 //��û�д���ת����
     
     this.submit.setTransactionID(msgInfo.getBatchNO()+msgInfo.getAppSerialNo());       ///*����MM7_submit.REQ/MM7_submit.RES�Եı�ʶ���ر�*/
     this.submit.setVASPID("1111") ; //����SP���룬�ر�
     this.submit.setVASID("2222") ; //���÷�����룬�ر�
     this.submit.setServiceCode("3333"); //����ҵ����룬�ر�
     this.submit.setLinkedID("4444"); /*�������ӱ�ʶ����ʶ������VASP����һ����Ч��Ϣ�Ķ�Ӧ��ϵ����ѡ*/
     this.submit.setSenderAddress(msgInfo.getSendID());  //����MMʼ�����ĵ�ַ����дSP�ķ�����룬������д���û��ظ�SP�ĳ����룬�����빹�ɣ�SP�ķ�����룫ҵ����룫�����룩���ر�
     this.submit.setChargedPartyID(msgInfo.getRecvID()); //���ø��ѷ����ֻ����룬�ر�
 //    this.submit.setTo("���շ���ַ"); //���ý��շ�MM�ĵ�ַ
     this.submit.addTo(msgInfo.getRecvID()); //���ӵ������ܷ���ַ
//     this.submit.setCc("���ͷ���ַ"); //���ó��ͷ�MM�ĵ�ַ
//     this.submit.addCc("�������ͷ���ַ"); //���ӵ������ͷ���ַ
//     this.submit.setBcc("���ͷ���ַ"); //�������ͷ�MM�ĵ�ַ
 //    this.submit.addBcc("�������ͷ���ַ"); //���ӵ������ͷ���ַ
     
    
 //    this.submit.setMessageClass("MM�����"); /*����MM��������磬��桢��Ϣ����ͼƷѣ�����ѡ�������У�Auto��Personal��Advertisement��Informational*/
  //   this.submit.setTimeStamp("�ύMM�����ں�ʱ��"); /*�ύMM��ʱ������ڣ�ʱ�����,��ʽ��2004-02-09T10��21��07����ѡ*/
//     this.submit.setExpiryDate("ָ����ʱʱ��"); /*����MMָ���ĳ�ʱʱ�䣨���Ի����ʱ�䣩����ѡ*/
 //    this.submit.setEarliestDeliveryTime("��������ʱ��"); /*���ý�MM���͸����շ�����������ʱ��(���Ի����ʱ��)����ѡ*/
     this.submit.setDeliveryReport(true); /*�����Ƿ���Ҫ���ͱ��������booleanֵ��,��ѡ*/
     this.submit.setReadReply(true); /*����ͨ��������һ����ȡ�������ȷ�ϣ���ѡ*/
//    this.submit.setReplyCharging("Ӧ��Ʒѵ�����"); /*����Ӧ��Ʒѵ�����booleanֵ������ѡ*/
//     this.submit.setReplyDeadline("�ύӦ������ʱ��"); /*������Ӧ��Ʒѵ�����£�����շ��ύӦ������ʱ�䣨���Ի����ʱ�䣩����ѡ*/
//     this.submit.setReplyChargingSize("Ӧ��MM������С"); /*������Ӧ��Ʒѵ�����£��ṩ�����շ���Ӧ��MM������С����ѡ*/
     this.submit.setPriority((byte)0); /*��Ϣ�����ȼ�����Ҫ�ԣ���0=������ȼ���1=������2=��������byte���͵�ֵ����ѡ*/
     
 //    this.submit.setAllowAdaptations("VASP�Ƿ������޸�����"); /*����VASP�Ƿ������޸����ݣ�booleanֵ��Ĭ��Ϊ�棩����ѡ*/
 //    this.submit.setChargedParty("VASP���ύMM�ĸ��ѷ�"); /*����VASP���ύMM�ĸ��ѷ������磬���ͷ������շ������ͷ��ͽ��շ��������������ѣ���ѡ��0��Sender��1��Recipients��2��Both��3��Neither��4��ThirdParty*/
   
     this.submit.setSubject("�ڲ���ʽ"); /*���ö�ý����Ϣ�ı��⣬��ѡ*/
    //  this.submit.setDistributionIndicator("�Ƿ�����·ַ�"); /*����VASP�Ƿ�����·ַ�MM�����ݣ�booleanֵ��trueΪ���ԣ�falseΪ�����ԣ�����ѡ*/
         
     //������Ϣ������
      
     MMContent content = new MMContent();
     content.setContentType(MMConstants.ContentType. MULTIPART_MIXED);   //  **���ø��������ͣ���������SMIL��ʽ���ļ�������������ΪMMConstants.ContentType. MULTIPART_MIXED��������SMIL��ʽ���ļ�������������ΪMMConstants.ContentType. MULTIPART_RELATED*/

     
     
     for(int i=0 ; i<msgInfo.getFile().size();i++)
     {
     	MMContent sub1 = MMContent.createFromFile( (String)msgInfo.getFile().get(i) );
     	sub1.setContentType(getFileType((String)msgInfo.getFile().get(i))); 
     	content.addSubContent(sub1);
     }
     
     submit.setContent(content);
     
     MM7RSRes res = mm7Sender.send(submit); 
     /*���Ը���StatusCode���жϱ��η����Ƿ�ɹ������ɹ��ܵõ�MessageID����Ϣ��StatusCode���ܵõ���ֵ������ɼ������е�����״̬��˵����*/
    
     //������Ϊ�����ã� ����Ҫ���ۺ�ȷ������Ϣ�ķ��ؿ���ͳһ��IN������������
     if(res instanceof  MM7SubmitRes)
	 {
		 MM7SubmitRes submitRes = (MM7SubmitRes)res;
		 System.out.println("StatusCode="+ submitRes.getStatusCode());
		 System.out.println("StatusText="+ submitRes.getStatusText());
		 System.out.println("MessageID="+ submitRes .getMessageID());
		 UpdateMsgId(msgInfo.getBatchNO(),msgInfo.getSerialNO(),submitRes .getMessageID());   //����messageid		 
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
