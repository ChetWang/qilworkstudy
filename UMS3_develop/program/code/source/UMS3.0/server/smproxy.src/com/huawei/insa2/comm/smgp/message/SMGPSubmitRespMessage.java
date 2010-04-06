/*    */ package com.huawei.insa2.comm.smgp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SMGPSubmitRespMessage extends SMGPMessage
/*    */ {
/*    */   public SMGPSubmitRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 21 */     this.buf = new byte[18];
/* 22 */     if (buf.length != 18) {
/* 23 */       throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/*    */ 
/* 26 */     System.arraycopy(buf, 0, this.buf, 0, 18);
/* 27 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public byte[] getMsgId()
/*    */   {
/* 34 */     byte[] tmpMsgId = new byte[10];
/* 35 */     System.arraycopy(this.buf, 4, tmpMsgId, 0, 10);
/* 36 */     return tmpMsgId;
/*    */   }
/*    */ 
/*    */   public int getStatus()
/*    */   {
/* 44 */     return TypeConvert.byte2int(this.buf, 14);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 51 */     StringBuffer strBuf = new StringBuffer(200);
/* 52 */     strBuf.append("SMGPSubmitRespMessage: ");
/* 53 */     strBuf.append("Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 54 */     strBuf.append(",MsgId=".concat(String.valueOf(String.valueOf(new String(getMsgId())))));
/* 55 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(getStatus()))));
/* 56 */     return strBuf.toString();
/*    */   }
/*    */ 
/*    */   public int getRequestId()
/*    */   {
/* 63 */     return -2147483646;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPSubmitRespMessage
 * JD-Core Version:    0.5.3
 */