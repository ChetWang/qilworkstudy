/*    */ package com.huawei.insa2.comm.smgp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SMGPExitMessage extends SMGPMessage
/*    */ {
/*    */   public SMGPExitMessage()
/*    */   {
/* 20 */     int len = 12;
/* 21 */     this.buf = new byte[len];
/*    */ 
/* 24 */     TypeConvert.int2byte(len, this.buf, 0);
/* 25 */     TypeConvert.int2byte(6, this.buf, 4);
/*    */   }
/*    */ 
/*    */   public SMGPExitMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 34 */     this.buf = new byte[4];
/* 35 */     if (buf.length != 4) {
/* 36 */       throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/*    */ 
/* 39 */     System.arraycopy(buf, 0, this.buf, 0, 4);
/* 40 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 47 */     StringBuffer strBuf = new StringBuffer(100);
/* 48 */     strBuf.append("SMGPExitMessage: ");
/* 49 */     strBuf.append("Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 50 */     return strBuf.toString();
/*    */   }
/*    */ 
/*    */   public int getRequestId()
/*    */   {
/* 57 */     return 6;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPExitMessage
 * JD-Core Version:    0.5.3
 */