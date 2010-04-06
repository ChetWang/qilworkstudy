/*
 * UMSMsgCreator.java
 *
 * Created on 2007-9-28, 10:22:36
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.v3.message;

import com.nci.ums.util.SerialNO;
import com.nci.ums.util.Util;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Qil.Wong
 */
public class UMSMsgCreator {
	
	public static UMSMsg createUMSMsg(){
		
		UMSMsg msg = new UMSMsg();
		msg.setBasicMsg(createBasicMsg());
		msg.setBatchMode(UMSMsg.UMSMSG_BATCHMODE_SINGLE);
		msg.setBatchNO(Util.getCurrentTimeStr("yyyyMMddHHmmss"));
		msg.setSerialNO(Integer.valueOf(SerialNO.getInstance().getSerial()).intValue());
		msg.setSequenceNO(new int[]{0});
		msg.setStatusFlag(UMSMsg.UMSMSG_STATUS_VALID);
		return msg;
	}

    public static BasicMsg createBasicMsg() {
//        UMSMsg msg = new UMSMsg();
        BasicMsg basic = new BasicMsg();
//        msg.setBasicMsg(basic);
        basic.setAck(BasicMsg.UMSMsg_ACK_NO);
        basic.setAppSerialNO("kk20098");
        basic.setCompanyID("nci");
        basic.setDirectSendFlag(BasicMsg.BASICMSG_DIRECTSEND_YES);
        basic.setFeeServiceNO("FEE231");
        basic.setMediaID("025");
        basic.setSubmitDate("20080327");
        basic.setSubmitTime("112510");
        basic.setMsgContent(new MsgContent("hello", "ÄãºÃ°¡£¬Ëæxx!\r\nhttp://www.sohu.com "));
        basic.setNeedReply(BasicMsg.BASICMSG_NEEDREPLY_NO);
        basic.setPriority(BasicMsg.UMSMsg_PRIORITY_NORMAL);
        Participant[] receivers = new Participant[1];
        receivers[0] = new Participant("yingzn@demo.zj.com.yc", Participant.PARTICIPANT_MSG_TO, Participant.PARTICIPANT_ID_LCS);
//        receivers[1] = new Participant("ssszzz@nci.com.cn", Participant.PARTICIPANT_MSG_TO, Participant.PARTICIPANT_ID_EMAIL);
        basic.setReceivers(receivers);
        basic.setReplyDestination("kk");
        basic.setDirectSendFlag(BasicMsg.BASICMSG_DIRECTSEND_YES);
        basic.setSender(new Participant("ums@nci.com.cn", Participant.PARTICIPANT_MSG_FROM, Participant.PARTICIPANT_ID_EMAIL));
        basic.setServiceID("kk10034");
        basic.setTimeSetFlag(BasicMsg.BASICMSG_SENDTIME_NOCUSTOM);
        basic.setUmsFlag(BasicMsg.BASICMSG_SELF_UMS);
//        MsgAttachment[] attach = new MsgAttachment[1];
////        attach[0] = createAttachment("d:/1.doc");
////        attach[1] = createAttachment("d:/build.xml");
//        attach[0] = createAttachment("d:/umslog.txt");
//        basic.setMsgAttachment(attach);
        
        return basic;
    }

    private static MsgAttachment createAttachment(String filename) {
        //less than 1M
        InputStream in = null;
        MsgAttachment att = new MsgAttachment();
        try {            
            String filebyteBase64;
            File attFile = new File(filename);
            byte[] bytes = new byte[(int)attFile.length()];
            in = new FileInputStream(attFile);
            in.read(bytes);
            filebyteBase64 = MsgAttachment.getBASE64Encoder().encode(bytes);
//            filebyteBase64 = new BASE64Encoder().encode(bytes);
            in.close();
            att.setFileName(attFile.getName());
            att.setFileByteBase64(filebyteBase64);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return att;
    }
    
    public static void main(String[] args) throws IOException{
    	MsgAttachment att = createAttachment("d:/umslog.txt");
    	String base64String = att.getFileByteBase64();
    	byte[] b = new BASE64Decoder().decodeBuffer(base64String);
    	FileOutputStream fo = new FileOutputStream(new File("d:/xxx.txt"));
    	fo.write(b);
    	fo.close();
    }
}