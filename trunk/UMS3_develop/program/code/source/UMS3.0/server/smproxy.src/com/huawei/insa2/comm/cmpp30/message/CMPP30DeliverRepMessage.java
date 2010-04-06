/*    */ package com.huawei.insa2.comm.cmpp30.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class CMPP30DeliverRepMessage extends CMPPMessage
/*    */ {
/*    */   private String outStr;
/*    */ 
/*    */   public CMPP30DeliverRepMessage(byte[] msg_Id, int result)
/*    */     throws IllegalArgumentException
/*    */   {
/* 29 */     if (msg_Id.length > 8) {
/* 30 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.DELIVER_REPINPUT_ERROR))).append(":msg_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("8"))));
/*    */     }
/* 32 */     if ((result < 0) || (result > 2147483647)) {
/* 33 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.DELIVER_REPINPUT_ERROR))).append(":result").append(CMPPConstant.INT_SCOPE_ERROR))));
/*    */     }
/*    */ 
/* 41 */     int len = 24;
/*    */ 
/* 43 */     this.buf = new byte[len];
/*    */ 
/* 46 */     TypeConvert.int2byte(len, this.buf, 0);
/* 47 */     TypeConvert.int2byte(-2147483643, this.buf, 4);
/*    */ 
/* 52 */     System.arraycopy(msg_Id, 0, this.buf, 12, msg_Id.length);
/*    */ 
/* 57 */     TypeConvert.int2byte(result, this.buf, 20);
/*    */ 
/* 62 */     this.outStr = ",result=".concat(String.valueOf(String.valueOf(result)));
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 70 */     String tmpStr = "CMPP_Deliver_REP: ";
/* 71 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 72 */     tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
/* 73 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 80 */     return -2147483643;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp30.message.CMPP30DeliverRepMessage
 * JD-Core Version:    0.5.3
 */