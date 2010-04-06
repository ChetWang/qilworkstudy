/*    */ package com.huawei.insa2.comm.smgp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SMGPActiveTestRespMessage extends SMGPMessage
/*    */ {
/*    */   public SMGPActiveTestRespMessage()
/*    */     throws IllegalArgumentException
/*    */   {
/* 21 */     int len = 12;
/*    */ 
/* 23 */     this.buf = new byte[len];
/*    */ 
/* 26 */     TypeConvert.int2byte(len, this.buf, 0);
/* 27 */     TypeConvert.int2byte(-2147483644, this.buf, 4);
/*    */   }
/*    */ 
/*    */   public SMGPActiveTestRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 37 */     this.buf = new byte[4];
/* 38 */     if (buf.length != 4) {
/* 39 */       throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 41 */     System.arraycopy(buf, 0, this.buf, 0, 4);
/* 42 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 49 */     StringBuffer strBuf = new StringBuffer(100);
/* 50 */     strBuf.append("SMGPActiveTestRespMessage: ");
/* 51 */     strBuf.append("Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 52 */     return strBuf.toString();
/*    */   }
/*    */ 
/*    */   public int getRequestId()
/*    */   {
/* 59 */     return -2147483644;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPActiveTestRespMessage
 * JD-Core Version:    0.5.3
 */