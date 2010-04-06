/*    */ package com.huawei.insa2.comm.cngp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cngp.CNGPConstant;
/*    */ 
/*    */ public class CNGPSubmitRespMessage extends CNGPMessage
/*    */ {
/*    */   public CNGPSubmitRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 21 */     this.buf = new byte[31];
/* 22 */     if (buf.length != 31) {
/* 23 */       throw new IllegalArgumentException(CNGPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 25 */     System.arraycopy(buf, 0, this.buf, 0, 31);
/*    */   }
/*    */ 
/*    */   public byte[] getMsgId()
/*    */   {
/* 32 */     byte[] tmpMsgId = new byte[10];
/* 33 */     System.arraycopy(this.buf, 16, tmpMsgId, 0, 10);
/* 34 */     return tmpMsgId;
/*    */   }
/*    */ 
/*    */   public int getCongestionState()
/*    */   {
/* 41 */     return this.buf[30];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 48 */     StringBuffer strBuf = new StringBuffer(200);
/* 49 */     strBuf.append("CNGPSubmitRespMessage: ");
/* 50 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 51 */     strBuf.append(",RequestID=".concat(String.valueOf(String.valueOf(super.getRequestId()))));
/* 52 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 53 */     strBuf.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 54 */     strBuf.append(",MsgId=".concat(String.valueOf(String.valueOf(new String(getMsgId())))));
/* 55 */     strBuf.append(",CongestionState=".concat(String.valueOf(String.valueOf(getCongestionState()))));
/* 56 */     return strBuf.toString();
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.message.CNGPSubmitRespMessage
 * JD-Core Version:    0.5.3
 */