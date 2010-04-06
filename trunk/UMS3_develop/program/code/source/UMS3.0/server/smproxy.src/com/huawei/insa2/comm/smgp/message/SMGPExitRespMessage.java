/*    */ package com.huawei.insa2.comm.smgp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SMGPExitRespMessage extends SMGPMessage
/*    */ {
/*    */   public SMGPExitRespMessage()
/*    */     throws IllegalArgumentException
/*    */   {
/* 20 */     int len = 12;
/*    */ 
/* 22 */     this.buf = new byte[len];
/*    */ 
/* 25 */     TypeConvert.int2byte(len, this.buf, 0);
/* 26 */     TypeConvert.int2byte(-2147483642, this.buf, 4);
/*    */   }
/*    */ 
/*    */   public SMGPExitRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 36 */     this.buf = new byte[4];
/* 37 */     if (buf.length != 4) {
/* 38 */       throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 40 */     System.arraycopy(buf, 0, this.buf, 0, 4);
/* 41 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 48 */     StringBuffer strBuf = new StringBuffer(100);
/* 49 */     strBuf.append("SMGPExitRespMessage: ");
/* 50 */     strBuf.append("Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 51 */     return strBuf.toString();
/*    */   }
/*    */ 
/*    */   public int getRequestId()
/*    */   {
/* 58 */     return -2147483642;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPExitRespMessage
 * JD-Core Version:    0.5.3
 */