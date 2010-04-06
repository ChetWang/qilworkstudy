/*    */ package com.huawei.insa2.comm.smpp.message;
/*    */ 
/*    */ public class SMPPDeliverRespMessage extends SMPPMessage
/*    */ {
/*    */   private StringBuffer strBuf;
/*    */ 
/*    */   public SMPPDeliverRespMessage(int status)
/*    */     throws IllegalArgumentException
/*    */   {
/* 25 */     int len = 17;
/* 26 */     this.buf = new byte[17];
/*    */ 
/* 29 */     super.setMsgLength(len);
/* 30 */     super.setCommandId(-2147483643);
/*    */ 
/* 32 */     super.setStatus(status);
/*    */ 
/* 37 */     this.buf[16] = 0;
/*    */ 
/* 40 */     this.strBuf = new StringBuffer(100);
/* 41 */     this.strBuf.append(",MsgId= ");
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 48 */     StringBuffer outStr = new StringBuffer(100);
/* 49 */     outStr.append("SMPPDeliverRespMessage:");
/* 50 */     this.strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 51 */     this.strBuf.append(",CommandID=".concat(String.valueOf(String.valueOf(super.getCommandId()))));
/* 52 */     this.strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 53 */     outStr.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 54 */     if (this.strBuf != null)
/*    */     {
/* 56 */       outStr.append(this.strBuf.toString());
/*    */     }
/* 58 */     return outStr.toString();
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.message.SMPPDeliverRespMessage
 * JD-Core Version:    0.5.3
 */