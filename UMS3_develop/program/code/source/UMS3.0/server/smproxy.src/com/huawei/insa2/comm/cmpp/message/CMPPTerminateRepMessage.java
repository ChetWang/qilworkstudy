/*    */ package com.huawei.insa2.comm.cmpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class CMPPTerminateRepMessage extends CMPPMessage
/*    */ {
/*    */   public CMPPTerminateRepMessage()
/*    */   {
/* 20 */     int len = 12;
/*    */ 
/* 22 */     this.buf = new byte[len];
/*    */ 
/* 25 */     TypeConvert.int2byte(len, this.buf, 0);
/* 26 */     TypeConvert.int2byte(-2147483646, this.buf, 4);
/*    */   }
/*    */ 
/*    */   public CMPPTerminateRepMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 35 */     this.buf = new byte[4];
/* 36 */     if (buf.length != 4) {
/* 37 */       throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 39 */     System.arraycopy(buf, 0, this.buf, 0, 4);
/* 40 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 48 */     String tmpStr = "CMPP_Terminate_REP: ";
/* 49 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 50 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 57 */     return -2147483646;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPTerminateRepMessage
 * JD-Core Version:    0.5.3
 */