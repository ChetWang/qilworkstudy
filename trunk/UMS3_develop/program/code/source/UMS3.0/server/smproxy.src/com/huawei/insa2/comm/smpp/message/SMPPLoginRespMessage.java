/*    */ package com.huawei.insa2.comm.smpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smpp.SMPPConstant;
/*    */ 
/*    */ public class SMPPLoginRespMessage extends SMPPMessage
/*    */ {
/*    */   public SMPPLoginRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 21 */     this.buf = new byte[buf.length];
/*    */ 
/* 23 */     if ((buf.length < 17) || (buf.length > 32)) {
/* 24 */       throw new IllegalArgumentException(SMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 26 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/*    */   }
/*    */ 
/*    */   public String getSystemId()
/*    */   {
/* 33 */     byte[] tmpbuf = new byte[this.buf.length - 16 - 1];
/* 34 */     System.arraycopy(this.buf, 16, tmpbuf, 0, this.buf.length - 17);
/* 35 */     return new String(tmpbuf);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 42 */     StringBuffer strBuf = new StringBuffer(300);
/* 43 */     strBuf.append("SMPPLoginRespMessage: ");
/* 44 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(this.buf.length))));
/* 45 */     strBuf.append(",CommandID=".concat(String.valueOf(String.valueOf(super.getCommandId()))));
/* 46 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 47 */     strBuf.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 48 */     strBuf.append(",SystemId=".concat(String.valueOf(String.valueOf(getSystemId()))));
/* 49 */     return strBuf.toString();
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.message.SMPPLoginRespMessage
 * JD-Core Version:    0.5.3
 */