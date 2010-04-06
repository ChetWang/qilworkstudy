/*    */ package com.huawei.insa2.comm.cngp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cngp.CNGPConstant;
/*    */ 
/*    */ public class CNGPDeliverRespMessage extends CNGPMessage
/*    */ {
/*    */   private StringBuffer strBuf;
/*    */ 
/*    */   public CNGPDeliverRespMessage(byte[] msgId, int congestionState)
/*    */     throws IllegalArgumentException
/*    */   {
/* 28 */     if (msgId.length > 10) {
/* 29 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.DELIVER_REPINPUT_ERROR))).append(":msg_Id").append(CNGPConstant.STRING_LENGTH_GREAT).append("10"))));
/*    */     }
/* 31 */     if ((congestionState < 0) || (congestionState > 255)) {
/* 32 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.DELIVER_REPINPUT_ERROR))).append(":congestionState ").append(CNGPConstant.INT_SCOPE_ERROR))));
/*    */     }
/*    */ 
/* 38 */     int len = 31;
/* 39 */     this.buf = new byte[31];
/*    */ 
/* 42 */     super.setMsgLength(len);
/* 43 */     super.setRequestId(-2147483645);
/*    */ 
/* 49 */     System.arraycopy(msgId, 0, this.buf, 16, msgId.length);
/*    */ 
/* 51 */     this.buf[30] = (byte)congestionState;
/*    */ 
/* 55 */     this.strBuf = new StringBuffer(100);
/* 56 */     this.strBuf.append(",MsgId=".concat(String.valueOf(String.valueOf(msgId))));
/* 57 */     this.strBuf.append(",congestionState=".concat(String.valueOf(String.valueOf(congestionState))));
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 65 */     StringBuffer outStr = new StringBuffer(100);
/* 66 */     outStr.append("CNGPDeliverRespMessage:");
/* 67 */     this.strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 68 */     this.strBuf.append(",RequestID=".concat(String.valueOf(String.valueOf(super.getRequestId()))));
/* 69 */     this.strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 70 */     outStr.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 71 */     if (this.strBuf != null)
/*    */     {
/* 73 */       outStr.append(this.strBuf.toString());
/*    */     }
/* 75 */     return outStr.toString();
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.message.CNGPDeliverRespMessage
 * JD-Core Version:    0.5.3
 */