/*    */ package com.huawei.insa2.comm.smgp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SMGPMtRouteUpdateRespMessage extends SMGPMessage
/*    */ {
/*    */   public SMGPMtRouteUpdateRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 21 */     this.buf = new byte[5];
/* 22 */     if (buf.length != 5) {
/* 23 */       throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/*    */ 
/* 26 */     System.arraycopy(buf, 0, this.buf, 0, 5);
/* 27 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public int getStatus()
/*    */   {
/* 35 */     return this.buf[4];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 42 */     StringBuffer strBuf = new StringBuffer(100);
/* 43 */     strBuf.append("SMGPMtRouteUpdateRespMessage: ");
/* 44 */     strBuf.append("Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 45 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(getStatus()))));
/* 46 */     return strBuf.toString();
/*    */   }
/*    */ 
/*    */   public int getRequestId()
/*    */   {
/* 53 */     return -2147483640;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPMtRouteUpdateRespMessage
 * JD-Core Version:    0.5.3
 */