/*    */ package com.huawei.insa2.comm.cmpp30.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class CMPP30ConnectRepMessage extends CMPPMessage
/*    */ {
/*    */   public CMPP30ConnectRepMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 31 */     this.buf = new byte[25];
/* 32 */     if (buf.length != 25) {
/* 33 */       throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 35 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/* 36 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public int getStatus()
/*    */   {
/* 52 */     return TypeConvert.byte2int(this.buf, 4);
/*    */   }
/*    */ 
/*    */   public byte[] getAuthenticatorISMG()
/*    */   {
/* 60 */     byte[] tmpbuf = new byte[16];
/*    */ 
/* 64 */     System.arraycopy(this.buf, 8, tmpbuf, 0, 16);
/*    */ 
/* 66 */     return tmpbuf;
/*    */   }
/*    */ 
/*    */   public byte getVersion()
/*    */   {
/* 76 */     return this.buf[24];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 84 */     String tmpStr = "CMPP_Connect_REP: ";
/* 85 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 86 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Status=").append(getStatus())));
/* 87 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",AuthenticatorISMG=").append(new String(getAuthenticatorISMG()))));
/* 88 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Version=").append(getVersion())));
/* 89 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 96 */     return -2147483647;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp30.message.CMPP30ConnectRepMessage
 * JD-Core Version:    0.5.3
 */