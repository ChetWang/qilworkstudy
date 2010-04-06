/*    */ package com.huawei.insa2.comm.smpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smpp.SMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SMPPUnbindRespMessage extends SMPPMessage
/*    */ {
/*    */   public SMPPUnbindRespMessage()
/*    */   {
/* 20 */     int len = 16;
/*    */ 
/* 22 */     this.buf = new byte[len];
/*    */ 
/* 25 */     super.setMsgLength(len);
/* 26 */     super.setCommandId(-2147483642);
/* 27 */     super.setStatus(0);
/*    */   }
/*    */ 
/*    */   public SMPPUnbindRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 36 */     this.buf = new byte[16];
/* 37 */     if (buf.length != 16) {
/* 38 */       throw new IllegalArgumentException(SMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 40 */     System.arraycopy(buf, 0, this.buf, 0, 16);
/* 41 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 12);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 49 */     String tmpStr = "SMPP_Unbind_REP: ";
/* 50 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 51 */     return tmpStr;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.message.SMPPUnbindRespMessage
 * JD-Core Version:    0.5.3
 */