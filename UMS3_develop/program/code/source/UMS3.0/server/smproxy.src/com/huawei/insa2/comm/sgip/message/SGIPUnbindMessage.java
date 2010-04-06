/*    */ package com.huawei.insa2.comm.sgip.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.sgip.SGIPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SGIPUnbindMessage extends SGIPMessage
/*    */ {
/*    */   public SGIPUnbindMessage()
/*    */   {
/* 20 */     int len = 20;
/* 21 */     this.buf = new byte[len];
/*    */ 
/* 24 */     TypeConvert.int2byte(len, this.buf, 0);
/* 25 */     TypeConvert.int2byte(2, this.buf, 4);
/*    */   }
/*    */ 
/*    */   public SGIPUnbindMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 34 */     this.buf = new byte[12];
/* 35 */     if (buf.length != 12) {
/* 36 */       throw new IllegalArgumentException(SGIPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/*    */ 
/* 39 */     System.arraycopy(buf, 0, this.buf, 0, 12);
/* 40 */     this.src_node_Id = TypeConvert.byte2int(this.buf, 0);
/* 41 */     this.time_Stamp = TypeConvert.byte2int(this.buf, 4);
/* 42 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 8);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 49 */     String tmpStr = "SGIP_UNBIND: ";
/* 50 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 51 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 58 */     return 2;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.message.SGIPUnbindMessage
 * JD-Core Version:    0.5.3
 */