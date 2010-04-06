/*    */ package com.huawei.insa2.comm.sgip.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.sgip.SGIPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SGIPReportRepMessage extends SGIPMessage
/*    */ {
/*    */   private String outStr;
/*    */ 
/*    */   public SGIPReportRepMessage(int result)
/*    */     throws IllegalArgumentException
/*    */   {
/* 27 */     if ((result < 0) || (result > 255)) {
/* 28 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.REPORT_REPINPUT_ERROR))).append(":result").append(SGIPConstant.INT_SCOPE_ERROR))));
/*    */     }
/*    */ 
/* 32 */     int len = 29;
/*    */ 
/* 34 */     this.buf = new byte[len];
/*    */ 
/* 37 */     TypeConvert.int2byte(len, this.buf, 0);
/* 38 */     TypeConvert.int2byte(-2147483643, this.buf, 4);
/*    */ 
/* 43 */     this.buf[20] = (byte)result;
/*    */ 
/* 46 */     this.outStr = ",result=".concat(String.valueOf(String.valueOf(result)));
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 54 */     String tmpStr = "SGIP_REPORT_REP: ";
/* 55 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 56 */     tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
/* 57 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 64 */     return -2147483643;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.message.SGIPReportRepMessage
 * JD-Core Version:    0.5.3
 */