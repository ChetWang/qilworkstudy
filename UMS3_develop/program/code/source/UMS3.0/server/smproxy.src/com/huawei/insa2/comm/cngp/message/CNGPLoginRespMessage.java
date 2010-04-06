/*    */ package com.huawei.insa2.comm.cngp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cngp.CNGPConstant;
/*    */ 
/*    */ public class CNGPLoginRespMessage extends CNGPMessage
/*    */ {
/*    */   public CNGPLoginRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 21 */     this.buf = new byte[33];
/* 22 */     if (buf.length != 33) {
/* 23 */       throw new IllegalArgumentException(CNGPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 25 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/*    */   }
/*    */ 
/*    */   public byte[] getAuthenticatorServer()
/*    */   {
/* 33 */     byte[] tmpbuf = new byte[16];
/* 34 */     System.arraycopy(this.buf, 16, tmpbuf, 0, 16);
/* 35 */     return tmpbuf;
/*    */   }
/*    */ 
/*    */   public byte getVersion()
/*    */   {
/* 42 */     return this.buf[32];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 49 */     StringBuffer strBuf = new StringBuffer(300);
/* 50 */     strBuf.append("CNGPLoginRespMessage: ");
/* 51 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(this.buf.length))));
/* 52 */     strBuf.append(",RequestID=".concat(String.valueOf(String.valueOf(super.getRequestId()))));
/* 53 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 54 */     strBuf.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 55 */     strBuf.append(",AuthenticatorServer=".concat(String.valueOf(String.valueOf(new String(getAuthenticatorServer())))));
/* 56 */     strBuf.append(",Version=".concat(String.valueOf(String.valueOf(getVersion()))));
/* 57 */     return strBuf.toString();
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.message.CNGPLoginRespMessage
 * JD-Core Version:    0.5.3
 */