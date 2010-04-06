/*    */ package com.huawei.insa2.comm.sgip.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.sgip.SGIPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SGIPUnbindRepMessage extends SGIPMessage
/*    */ {
/*    */   public SGIPUnbindRepMessage()
/*    */   {
/* 20 */     int len = 20;
/*    */ 
/* 22 */     this.buf = new byte[len];
/*    */ 
/* 25 */     TypeConvert.int2byte(len, this.buf, 0);
/* 26 */     TypeConvert.int2byte(-2147483646, this.buf, 4);
/*    */   }
/*    */ 
/*    */   public SGIPUnbindRepMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 35 */     this.buf = new byte[12];
/* 36 */     if (buf.length != 12) {
/* 37 */       throw new IllegalArgumentException(SGIPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 39 */     System.arraycopy(buf, 0, this.buf, 0, 12);
/*    */ 
/* 41 */     this.src_node_Id = TypeConvert.byte2int(this.buf, 0);
/* 42 */     this.time_Stamp = TypeConvert.byte2int(this.buf, 4);
/* 43 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 8);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 51 */     String tmpStr = "SGIP_UNBIND_REP: ";
/* 52 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 53 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 60 */     return -2147483646;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.message.SGIPUnbindRepMessage
 * JD-Core Version:    0.5.3
 */