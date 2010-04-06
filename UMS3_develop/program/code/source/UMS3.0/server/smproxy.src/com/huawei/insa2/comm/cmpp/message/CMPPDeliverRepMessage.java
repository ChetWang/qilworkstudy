/*    */ package com.huawei.insa2.comm.cmpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class CMPPDeliverRepMessage extends CMPPMessage
/*    */ {
/*    */   private String outStr;
/*    */ 
/*    */   public CMPPDeliverRepMessage(byte[] msg_Id, int result)
/*    */     throws IllegalArgumentException
/*    */   {
/* 28 */     if (msg_Id.length > 8) {
/* 29 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.DELIVER_REPINPUT_ERROR))).append(":msg_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("8"))));
/*    */     }
/* 31 */     if ((result < 0) || (result > 255)) {
/* 32 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.DELIVER_REPINPUT_ERROR))).append(":result").append(CMPPConstant.INT_SCOPE_ERROR))));
/*    */     }
/*    */ 
/* 37 */     int len = 21;
/* 38 */     this.buf = new byte[len];
/*    */ 
/* 41 */     TypeConvert.int2byte(len, this.buf, 0);
/* 42 */     TypeConvert.int2byte(-2147483643, this.buf, 4);
/*    */ 
/* 47 */     System.arraycopy(msg_Id, 0, this.buf, 12, msg_Id.length);
/*    */ 
/* 49 */     this.buf[20] = (byte)result;
/*    */ 
/* 53 */     this.outStr = ",result=".concat(String.valueOf(String.valueOf(result)));
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 61 */     String tmpStr = "CMPP_Deliver_REP: ";
/* 62 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 63 */     tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
/* 64 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 71 */     return -2147483643;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPDeliverRepMessage
 * JD-Core Version:    0.5.3
 */