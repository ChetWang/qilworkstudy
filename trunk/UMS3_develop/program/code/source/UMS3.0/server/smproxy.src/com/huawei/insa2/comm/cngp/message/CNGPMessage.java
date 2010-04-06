/*    */ package com.huawei.insa2.comm.cngp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public abstract class CNGPMessage extends PMessage
/*    */   implements Cloneable
/*    */ {
/*    */   protected byte[] buf;
/*    */ 
/*    */   public Object clone()
/*    */   {
/*    */     CNGPMessage localCNGPMessage1;
/*    */     try
/*    */     {
/* 28 */       CNGPMessage m = (CNGPMessage)super.clone();
/* 29 */       m.buf = ((byte[])this.buf.clone());
/* 30 */       return m;
/*    */     } catch (CloneNotSupportedException ex) {
/* 32 */       ex.printStackTrace();
/* 33 */       localCNGPMessage1 = null; } return localCNGPMessage1;
/*    */   }
/*    */ 
/*    */   public abstract String toString();
/*    */ 
/*    */   public int getMsgLength()
/*    */   {
/* 41 */     int msgLength = TypeConvert.byte2int(this.buf, 0);
/* 42 */     return msgLength;
/*    */   }
/*    */ 
/*    */   public void setMsgLength(int msgLength)
/*    */   {
/* 47 */     TypeConvert.int2byte(msgLength, this.buf, 0);
/*    */   }
/*    */ 
/*    */   public int getRequestId()
/*    */   {
/* 53 */     int requestId = TypeConvert.byte2int(this.buf, 4);
/* 54 */     return requestId;
/*    */   }
/*    */ 
/*    */   public void setRequestId(int requestId)
/*    */   {
/* 59 */     TypeConvert.int2byte(requestId, this.buf, 4);
/*    */   }
/*    */ 
/*    */   public int getStatus()
/*    */   {
/* 64 */     int status = TypeConvert.byte2int(this.buf, 8);
/* 65 */     return status;
/*    */   }
/*    */ 
/*    */   public void setStatus(int status)
/*    */   {
/* 70 */     TypeConvert.int2byte(status, this.buf, 8);
/*    */   }
/*    */ 
/*    */   public int getSequenceId()
/*    */   {
/* 78 */     int sequenceId = TypeConvert.byte2int(this.buf, 12);
/* 79 */     return sequenceId;
/*    */   }
/*    */ 
/*    */   public void setSequenceId(int sequenceId)
/*    */   {
/* 85 */     TypeConvert.int2byte(sequenceId, this.buf, 12);
/*    */   }
/*    */ 
/*    */   public byte[] getBytes()
/*    */   {
/* 92 */     return this.buf;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.message.CNGPMessage
 * JD-Core Version:    0.5.3
 */