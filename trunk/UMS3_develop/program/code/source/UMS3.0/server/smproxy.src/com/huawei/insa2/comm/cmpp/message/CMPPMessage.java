/*    */ package com.huawei.insa2.comm.cmpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public abstract class CMPPMessage extends PMessage
/*    */   implements Cloneable
/*    */ {
/*    */   protected byte[] buf;
/*    */   protected int sequence_Id;
/*    */ 
/*    */   public Object clone()
/*    */   {
/*    */     CMPPMessage localCMPPMessage1;
/*    */     try
/*    */     {
/* 31 */       CMPPMessage m = (CMPPMessage)super.clone();
/* 32 */       m.buf = ((byte[])this.buf.clone());
/* 33 */       return m;
/*    */     } catch (CloneNotSupportedException ex) {
/* 35 */       ex.printStackTrace();
/* 36 */       localCMPPMessage1 = null; } return localCMPPMessage1;
/*    */   }
/*    */ 
/*    */   public abstract String toString();
/*    */ 
/*    */   public abstract int getCommandId();
/*    */ 
/*    */   public int getSequenceId()
/*    */   {
/* 50 */     return this.sequence_Id;
/*    */   }
/*    */ 
/*    */   public void setSequenceId(int sequence_Id)
/*    */   {
/* 56 */     this.sequence_Id = sequence_Id;
/* 57 */     TypeConvert.int2byte(sequence_Id, this.buf, 8);
/*    */   }
/*    */ 
/*    */   public byte[] getBytes()
/*    */   {
/* 64 */     return this.buf;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPMessage
 * JD-Core Version:    0.5.3
 */