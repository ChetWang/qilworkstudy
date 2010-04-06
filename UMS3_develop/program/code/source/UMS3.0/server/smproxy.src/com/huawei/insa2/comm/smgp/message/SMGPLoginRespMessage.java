/*    */ package com.huawei.insa2.comm.smgp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SMGPLoginRespMessage extends SMGPMessage
/*    */ {
/*    */   public SMGPLoginRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 21 */     this.buf = new byte[25];
/* 22 */     if (buf.length != 25) {
/* 23 */       throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 25 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/* 26 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public int getStatus()
/*    */   {
/* 33 */     return TypeConvert.byte2int(this.buf, 4);
/*    */   }
/*    */ 
/*    */   public byte[] getAuthenticatorServer()
/*    */   {
/* 39 */     byte[] tmpbuf = new byte[16];
/* 40 */     System.arraycopy(this.buf, 8, tmpbuf, 0, 16);
/* 41 */     return tmpbuf;
/*    */   }
/*    */ 
/*    */   public byte getVersion()
/*    */   {
/* 48 */     return this.buf[24];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 55 */     StringBuffer strBuf = new StringBuffer(300);
/* 56 */     strBuf.append("SMGPLoginRespMessage: ");
/* 57 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(this.buf.length))));
/* 58 */     strBuf.append(",RequestID=-2147483647");
/* 59 */     strBuf.append(",Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 60 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(getStatus()))));
/* 61 */     strBuf.append(",AuthenticatorServer=".concat(String.valueOf(String.valueOf(new String(getAuthenticatorServer())))));
/* 62 */     strBuf.append(",Version=".concat(String.valueOf(String.valueOf(getVersion()))));
/* 63 */     return strBuf.toString();
/*    */   }
/*    */ 
/*    */   public int getRequestId()
/*    */   {
/* 70 */     return -2147483647;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPLoginRespMessage
 * JD-Core Version:    0.5.3
 */