/*    */ package com.huawei.insa2.comm.cmpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class CMPPActiveRepMessage extends CMPPMessage
/*    */ {
/*    */   public CMPPActiveRepMessage(int success_Id)
/*    */     throws IllegalArgumentException
/*    */   {
/* 20 */     if ((success_Id <= 0) || (success_Id > 255))
/*    */     {
/* 22 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.ACTIVE_REPINPUT_ERROR))).append(":success_Id").append(CMPPConstant.INT_SCOPE_ERROR))));
/*    */     }
/*    */ 
/* 25 */     int len = 13;
/*    */ 
/* 27 */     this.buf = new byte[len];
/*    */ 
/* 30 */     TypeConvert.int2byte(len, this.buf, 0);
/* 31 */     TypeConvert.int2byte(-2147483640, this.buf, 4);
/*    */ 
/* 35 */     this.buf[12] = (byte)success_Id;
/*    */   }
/*    */ 
/*    */   public CMPPActiveRepMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 44 */     this.buf = new byte[5];
/* 45 */     if (buf.length != 5) {
/* 46 */       throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 48 */     System.arraycopy(buf, 0, this.buf, 0, 5);
/* 49 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public int getSuccessId()
/*    */   {
/* 56 */     return this.buf[4];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 63 */     String tmpStr = "CMPP_Active_Test_REP: ";
/* 64 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 65 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SuccessId=").append(String.valueOf(this.buf[4]))));
/* 66 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 73 */     return -2147483640;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPActiveRepMessage
 * JD-Core Version:    0.5.3
 */