/*    */ package com.huawei.insa2.comm.smpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.smpp.SMPPConstant;
/*    */ 
/*    */ public class SMPPSubmitRespMessage extends SMPPMessage
/*    */ {
/*    */   public SMPPSubmitRespMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 21 */     this.buf = new byte[25];
/* 22 */     if ((buf.length < 17) || (buf.length > 25)) {
/* 23 */       throw new IllegalArgumentException(SMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 25 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/*    */   }
/*    */ 
/*    */   public String getMessageId()
/*    */   {
/* 32 */     return getFirstStr(this.buf, 16);
/*    */   }
/*    */ 
/*    */   private String getFirstStr(byte[] buf, int sPos)
/*    */   {
/* 39 */     int deli = 0;
/*    */ 
/* 42 */     byte[] tmpBuf = new byte[21];
/* 43 */     int pos = sPos;
/* 44 */     while ((buf[pos] != 0) && (pos < buf.length)) {
/* 45 */       tmpBuf[(pos - sPos)] = buf[pos];
/* 46 */       pos += 1;
/*    */     }
/* 48 */     if (pos == sPos) {
/* 49 */       return "";
/*    */     }
/*    */ 
/* 52 */     String tmpStr = new String(tmpBuf);
/* 53 */     return tmpStr.substring(0, pos - sPos);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 61 */     StringBuffer strBuf = new StringBuffer(200);
/* 62 */     strBuf.append("SMPPSubmitRespMessage: ");
/* 63 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 64 */     strBuf.append(",CommandID=".concat(String.valueOf(String.valueOf(super.getCommandId()))));
/* 65 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 66 */     strBuf.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 67 */     strBuf.append(",MessageId=".concat(String.valueOf(String.valueOf(new String(getMessageId())))));
/* 68 */     return strBuf.toString();
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.message.SMPPSubmitRespMessage
 * JD-Core Version:    0.5.3
 */