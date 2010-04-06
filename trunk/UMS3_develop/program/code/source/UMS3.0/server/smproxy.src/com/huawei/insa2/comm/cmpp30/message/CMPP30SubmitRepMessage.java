/*    */ package com.huawei.insa2.comm.cmpp30.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class CMPP30SubmitRepMessage extends CMPPMessage
/*    */ {
/*    */   public CMPP30SubmitRepMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 30 */     this.buf = new byte[16];
/* 31 */     if (buf.length != 16) {
/* 32 */       throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 34 */     System.arraycopy(buf, 0, this.buf, 0, 16);
/*    */ 
/* 37 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*    */   }
/*    */ 
/*    */   public byte[] getMsgId()
/*    */   {
/* 44 */     byte[] tmpMsgId = new byte[8];
/* 45 */     System.arraycopy(this.buf, 4, tmpMsgId, 0, 8);
/* 46 */     return tmpMsgId;
/*    */   }
/*    */ 
/*    */   public int getResult()
/*    */   {
/* 53 */     return TypeConvert.byte2int(this.buf, 12);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 60 */     String tmpStr = "CMPP_Submit_REP: ";
/* 61 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 62 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgId=").append(new String(getMsgId()))));
/* 63 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Result=").append(getResult())));
/* 64 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 71 */     return -2147483644;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp30.message.CMPP30SubmitRepMessage
 * JD-Core Version:    0.5.3
 */