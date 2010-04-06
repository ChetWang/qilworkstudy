/*    */ package com.huawei.insa2.comm.smpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smpp.SMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SMPPEnquireLinkMessage extends SMPPMessage
/*    */ {
/*    */   public SMPPEnquireLinkMessage()
/*    */   {
/* 19 */     int len = 16;
/* 20 */     this.buf = new byte[len];
/*    */ 
/* 23 */     super.setMsgLength(len);
/* 24 */     super.setCommandId(21);
/* 25 */     super.setStatus(0);
/*    */   }
/*    */ 
/*    */   public SMPPEnquireLinkMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 34 */     this.buf = new byte[16];
/* 35 */     if (buf.length != 16) {
/* 36 */       throw new IllegalArgumentException(SMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/*    */ 
/* 39 */     System.arraycopy(buf, 0, this.buf, 0, 12);
/* 40 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 48 */     StringBuffer strBuf = new StringBuffer(100);
/* 49 */     strBuf.append("SMPPEnquireLinkMessage: ");
/* 50 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 51 */     strBuf.append(",CommandID=".concat(String.valueOf(String.valueOf(super.getCommandId()))));
/* 52 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 53 */     strBuf.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 54 */     return strBuf.toString();
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.message.SMPPEnquireLinkMessage
 * JD-Core Version:    0.5.3
 */