/*    */ package com.huawei.insa2.comm.cmpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class CMPPActiveMessage extends CMPPMessage
/*    */ {
/*    */   public CMPPActiveMessage()
/*    */   {
/* 20 */     int len = 12;
/* 21 */     this.buf = new byte[len];
/*    */ 
/* 24 */     TypeConvert.int2byte(len, this.buf, 0);
/* 25 */     TypeConvert.int2byte(8, this.buf, 4);
/*    */   }
/*    */ 
/*    */   public CMPPActiveMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 34 */     this.buf = new byte[4];
/* 35 */     if (buf.length != 4) {
/* 36 */       throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/*    */ 
/* 39 */     System.arraycopy(buf, 0, this.buf, 0, 4);
/* 40 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 47 */     String tmpStr = "CMPP_Active_Test: ";
/* 48 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 49 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 56 */     return 8;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPActiveMessage
 * JD-Core Version:    0.5.3
 */