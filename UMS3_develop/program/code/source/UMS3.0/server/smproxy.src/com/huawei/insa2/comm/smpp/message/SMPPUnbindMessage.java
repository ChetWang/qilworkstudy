/*    */ package com.huawei.insa2.comm.smpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smpp.SMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SMPPUnbindMessage extends SMPPMessage
/*    */ {
/*    */   public SMPPUnbindMessage()
/*    */   {
/* 20 */     int len = 16;
/* 21 */     this.buf = new byte[len];
/*    */ 
/* 24 */     super.setMsgLength(len);
/* 25 */     super.setCommandId(6);
/* 26 */     super.setStatus(0);
/*    */   }
/*    */ 
/*    */   public SMPPUnbindMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 35 */     this.buf = new byte[4];
/* 36 */     if (buf.length != 4) {
/* 37 */       throw new IllegalArgumentException(SMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/*    */ 
/* 40 */     System.arraycopy(buf, 0, this.buf, 0, 4);
/* 41 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 48 */     String tmpStr = "SMPP_Unbind: ";
/* 49 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 50 */     return tmpStr;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.message.SMPPUnbindMessage
 * JD-Core Version:    0.5.3
 */