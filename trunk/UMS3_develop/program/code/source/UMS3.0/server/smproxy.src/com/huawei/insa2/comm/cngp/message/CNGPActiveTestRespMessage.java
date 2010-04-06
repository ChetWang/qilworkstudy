/*    */ package com.huawei.insa2.comm.cngp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cngp.CNGPConstant;
/*    */ 
/*    */ public class CNGPActiveTestRespMessage extends CNGPMessage
/*    */ {
/*    */   public CNGPActiveTestRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 22 */     this.buf = new byte[16];
/* 23 */     if (buf.length != 16) {
/* 24 */       throw new IllegalArgumentException(CNGPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 26 */     System.arraycopy(buf, 0, this.buf, 0, 16);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 33 */     StringBuffer strBuf = new StringBuffer(100);
/* 34 */     strBuf.append("CNGPActiveTestRespMessage: ");
/* 35 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 36 */     strBuf.append(",RequestID=".concat(String.valueOf(String.valueOf(super.getRequestId()))));
/* 37 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 38 */     strBuf.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 39 */     return strBuf.toString();
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.message.CNGPActiveTestRespMessage
 * JD-Core Version:    0.5.3
 */