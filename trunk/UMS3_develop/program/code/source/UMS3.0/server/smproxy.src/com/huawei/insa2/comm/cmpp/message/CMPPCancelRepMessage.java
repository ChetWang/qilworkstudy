/*    */ package com.huawei.insa2.comm.cmpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class CMPPCancelRepMessage extends CMPPMessage
/*    */ {
/*    */   public CMPPCancelRepMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 20 */     this.buf = new byte[5];
/* 21 */     if (buf.length != 5) {
/* 22 */       throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/*    */ 
/* 25 */     System.arraycopy(buf, 0, this.buf, 0, 5);
/* 26 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public int getSuccessId()
/*    */   {
/* 33 */     return this.buf[4];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 40 */     String tmpStr = "CMPP_Cancel_REP: ";
/* 41 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 42 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SuccessId=").append(String.valueOf(this.buf[4]))));
/* 43 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 50 */     return -2147483641;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPCancelRepMessage
 * JD-Core Version:    0.5.3
 */