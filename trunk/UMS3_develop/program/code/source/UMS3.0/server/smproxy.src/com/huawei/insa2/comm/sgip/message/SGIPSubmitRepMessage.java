/*    */ package com.huawei.insa2.comm.sgip.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.sgip.SGIPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SGIPSubmitRepMessage extends SGIPMessage
/*    */ {
/*    */   public SGIPSubmitRepMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 21 */     this.buf = new byte[21];
/* 22 */     if (buf.length != 21) {
/* 23 */       throw new IllegalArgumentException(SGIPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 25 */     System.arraycopy(buf, 0, this.buf, 0, 21);
/*    */ 
/* 27 */     this.src_node_Id = TypeConvert.byte2int(this.buf, 0);
/* 28 */     this.time_Stamp = TypeConvert.byte2int(this.buf, 4);
/* 29 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 8);
/*    */   }
/*    */ 
/*    */   public int getResult()
/*    */   {
/* 37 */     int tmpId = this.buf[12];
/* 38 */     return tmpId;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 45 */     String tmpStr = "SGIP_SUBMIT_REP: ";
/* 46 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 47 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Result=").append(getResult())));
/* 48 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 55 */     return -2147483645;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.message.SGIPSubmitRepMessage
 * JD-Core Version:    0.5.3
 */