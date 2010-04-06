/*    */ package com.huawei.insa2.comm.cmpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class CMPPConnectRepMessage extends CMPPMessage
/*    */ {
/*    */   public CMPPConnectRepMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 21 */     this.buf = new byte[22];
/* 22 */     if (buf.length != 22) {
/* 23 */       throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 25 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/* 26 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public byte getStatus()
/*    */   {
/* 33 */     return this.buf[4];
/*    */   }
/*    */ 
/*    */   public byte[] getAuthenticatorISMG()
/*    */   {
/* 39 */     byte[] tmpbuf = new byte[16];
/* 40 */     System.arraycopy(this.buf, 5, tmpbuf, 0, 16);
/* 41 */     return tmpbuf;
/*    */   }
/*    */ 
/*    */   public byte getVersion()
/*    */   {
/* 48 */     return this.buf[21];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 55 */     String tmpStr = "CMPP_Connect_REP: ";
/* 56 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 57 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Status=").append(getStatus())));
/* 58 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",AuthenticatorISMG=").append(new String(getAuthenticatorISMG()))));
/* 59 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Version=").append(getVersion())));
/* 60 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 67 */     return -2147483647;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPConnectRepMessage
 * JD-Core Version:    0.5.3
 */