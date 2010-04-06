/*    */ package com.huawei.insa2.comm.cngp.message;
/*    */ 
/*    */ public class CNGPActiveTestMessage extends CNGPMessage
/*    */ {
/*    */   public CNGPActiveTestMessage()
/*    */   {
/* 20 */     int len = 16;
/* 21 */     this.buf = new byte[len];
/*    */ 
/* 24 */     super.setMsgLength(len);
/* 25 */     super.setRequestId(4);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 34 */     StringBuffer strBuf = new StringBuffer(100);
/* 35 */     strBuf.append("CNGPActiveTestMessage: ");
/* 36 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 37 */     strBuf.append(",RequestID=".concat(String.valueOf(String.valueOf(super.getRequestId()))));
/* 38 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 39 */     strBuf.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 40 */     return strBuf.toString();
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.message.CNGPActiveTestMessage
 * JD-Core Version:    0.5.3
 */