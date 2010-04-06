/*    */ package com.huawei.insa2.comm.smpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smpp.SMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SMPPEnquireLinkRespMessage extends SMPPMessage
/*    */ {
/*    */   public SMPPEnquireLinkRespMessage()
/*    */   {
/* 19 */     int len = 16;
/* 20 */     this.buf = new byte[len];
/*    */ 
/* 23 */     super.setMsgLength(len);
/* 24 */     super.setCommandId(-2147483627);
/*    */ 
/* 26 */     super.setStatus(0);
/*    */   }
/*    */ 
/*    */   public SMPPEnquireLinkRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 35 */     this.buf = new byte[16];
/* 36 */     if (buf.length != 16) {
/* 37 */       throw new IllegalArgumentException(SMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/*    */ 
/* 40 */     System.arraycopy(buf, 0, this.buf, 0, 16);
/* 41 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 12);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 49 */     StringBuffer strBuf = new StringBuffer(100);
/* 50 */     strBuf.append("SMPPEnquireLinkRespMessage: ");
/* 51 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 52 */     strBuf.append(",CommandID=".concat(String.valueOf(String.valueOf(getCommandId()))));
/* 53 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 54 */     strBuf.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 55 */     return strBuf.toString();
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 62 */     return -2147483627;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.message.SMPPEnquireLinkRespMessage
 * JD-Core Version:    0.5.3
 */