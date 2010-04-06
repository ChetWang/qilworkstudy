/*    */ package com.huawei.insa2.comm.cmpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class CMPPCancelMessage extends CMPPMessage
/*    */ {
/*    */   private String outStr;
/*    */ 
/*    */   public CMPPCancelMessage(byte[] msg_Id)
/*    */     throws IllegalArgumentException
/*    */   {
/* 23 */     if (msg_Id.length > 8) {
/* 24 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.CONNECT_INPUT_ERROR))).append(":msg_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("8"))));
/*    */     }
/*    */ 
/* 27 */     int len = 20;
/* 28 */     this.buf = new byte[len];
/*    */ 
/* 31 */     TypeConvert.int2byte(len, this.buf, 0);
/* 32 */     TypeConvert.int2byte(7, this.buf, 4);
/*    */ 
/* 36 */     System.arraycopy(msg_Id, 0, this.buf, 12, msg_Id.length);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 46 */     String tmpStr = "CMPP_Cancel: ";
/* 47 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/*    */ 
/* 49 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 56 */     return 7;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPCancelMessage
 * JD-Core Version:    0.5.3
 */