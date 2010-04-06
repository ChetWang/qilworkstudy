/*    */ package com.huawei.insa2.comm.cngp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cngp.CNGPConstant;
/*    */ 
/*    */ public class CNGPExitMessage extends CNGPMessage
/*    */ {
/*    */   public CNGPExitMessage()
/*    */   {
/* 20 */     int len = 16;
/* 21 */     this.buf = new byte[len];
/*    */ 
/* 24 */     super.setMsgLength(len);
/* 25 */     super.setRequestId(6);
/*    */   }
/*    */ 
/*    */   public CNGPExitMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 33 */     this.buf = new byte[16];
/* 34 */     if (buf.length != 16) {
/* 35 */       throw new IllegalArgumentException(CNGPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 37 */     System.arraycopy(buf, 0, this.buf, 0, 16);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 47 */     StringBuffer strBuf = new StringBuffer(100);
/* 48 */     strBuf.append("CNGPExitMessage: ");
/* 49 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 50 */     strBuf.append(",RequestID=".concat(String.valueOf(String.valueOf(super.getRequestId()))));
/* 51 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 52 */     strBuf.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 53 */     return strBuf.toString();
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.message.CNGPExitMessage
 * JD-Core Version:    0.5.3
 */