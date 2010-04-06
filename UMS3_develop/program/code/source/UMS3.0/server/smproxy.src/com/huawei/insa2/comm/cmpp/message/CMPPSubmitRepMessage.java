/*    */ package com.huawei.insa2.comm.cmpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class CMPPSubmitRepMessage extends CMPPMessage
/*    */ {
/*    */   public CMPPSubmitRepMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 21 */     this.buf = new byte[13];
/* 22 */     if (buf.length != 13) {
/* 23 */       throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/*    */ 
/* 26 */     System.arraycopy(buf, 0, this.buf, 0, 13);
/* 27 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public byte[] getMsgId()
/*    */   {
/* 34 */     byte[] tmpMsgId = new byte[8];
/* 35 */     System.arraycopy(this.buf, 4, tmpMsgId, 0, 8);
/* 36 */     return tmpMsgId;
/*    */   }
/*    */ 
/*    */   public int getResult()
/*    */   {
/* 43 */     return this.buf[12];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 50 */     String tmpStr = "CMPP_Submit_REP: ";
/* 51 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 52 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgId=").append(new String(getMsgId()))));
/* 53 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Result=").append(getResult())));
/* 54 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 61 */     return -2147483644;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPSubmitRepMessage
 * JD-Core Version:    0.5.3
 */