/*    */ package com.huawei.insa2.comm.sgip.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.sgip.SGIPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SGIPBindRepMessage extends SGIPMessage
/*    */ {
/*    */   private String outStr;
/*    */ 
/*    */   public SGIPBindRepMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 25 */     this.buf = new byte[21];
/* 26 */     if (buf.length != 21) {
/* 27 */       throw new IllegalArgumentException(SGIPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 29 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/* 30 */     this.src_node_Id = TypeConvert.byte2int(this.buf, 0);
/* 31 */     this.time_Stamp = TypeConvert.byte2int(this.buf, 4);
/* 32 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 8);
/*    */   }
/*    */ 
/*    */   public SGIPBindRepMessage(int result)
/*    */     throws IllegalArgumentException
/*    */   {
/* 44 */     if ((result < 0) || (result > 255)) {
/* 45 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.DELIVER_REPINPUT_ERROR))).append(":result").append(SGIPConstant.INT_SCOPE_ERROR))));
/*    */     }
/*    */ 
/* 49 */     int len = 29;
/*    */ 
/* 51 */     this.buf = new byte[len];
/*    */ 
/* 54 */     TypeConvert.int2byte(len, this.buf, 0);
/* 55 */     TypeConvert.int2byte(-2147483647, this.buf, 4);
/*    */ 
/* 60 */     this.buf[20] = (byte)result;
/*    */ 
/* 63 */     this.outStr = ",result=".concat(String.valueOf(String.valueOf(result)));
/*    */   }
/*    */ 
/*    */   public int getResult()
/*    */   {
/* 70 */     return this.buf[12];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 77 */     String tmpStr = "SGIP_BIND_REP: ";
/* 78 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 79 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Result=").append(getResult())));
/* 80 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 87 */     return -2147483647;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.message.SGIPBindRepMessage
 * JD-Core Version:    0.5.3
 */