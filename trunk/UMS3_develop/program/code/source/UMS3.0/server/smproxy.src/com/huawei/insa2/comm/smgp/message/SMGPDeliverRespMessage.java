/*    */ package com.huawei.insa2.comm.smgp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SMGPDeliverRespMessage extends SMGPMessage
/*    */ {
/*    */   private StringBuffer strBuf;
/*    */ 
/*    */   public SMGPDeliverRespMessage(byte[] msg_Id, int status)
/*    */     throws IllegalArgumentException
/*    */   {
/* 28 */     if (msg_Id.length > 10) {
/* 29 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.DELIVER_REPINPUT_ERROR))).append(":msg_Id").append(SMGPConstant.STRING_LENGTH_GREAT).append("10"))));
/*    */     }
/* 31 */     if (status < 0) {
/* 32 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(SMGPConstant.DELIVER_REPINPUT_ERROR)).concat(":result"));
/*    */     }
/*    */ 
/* 37 */     int len = 26;
/* 38 */     this.buf = new byte[len];
/*    */ 
/* 41 */     TypeConvert.int2byte(len, this.buf, 0);
/* 42 */     TypeConvert.int2byte(-2147483645, this.buf, 4);
/*    */ 
/* 47 */     System.arraycopy(msg_Id, 0, this.buf, 12, msg_Id.length);
/*    */ 
/* 49 */     TypeConvert.int2byte(status, this.buf, 22);
/*    */ 
/* 53 */     this.strBuf = new StringBuffer(100);
/* 54 */     this.strBuf.append(",status=".concat(String.valueOf(String.valueOf(status))));
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 62 */     StringBuffer outStr = new StringBuffer(100);
/* 63 */     outStr.append("SMGPDeliverRespMessage:");
/* 64 */     outStr.append(",Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/*    */ 
/* 66 */     if (this.strBuf != null)
/*    */     {
/* 68 */       outStr.append(this.strBuf.toString());
/*    */     }
/* 70 */     return outStr.toString();
/*    */   }
/*    */ 
/*    */   public int getRequestId()
/*    */   {
/* 77 */     return -2147483645;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPDeliverRespMessage
 * JD-Core Version:    0.5.3
 */