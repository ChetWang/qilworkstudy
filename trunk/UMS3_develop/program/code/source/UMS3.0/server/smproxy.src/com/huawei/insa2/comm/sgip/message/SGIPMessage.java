/*     */ package com.huawei.insa2.comm.sgip.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.PMessage;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ 
/*     */ public abstract class SGIPMessage extends PMessage
/*     */   implements Cloneable
/*     */ {
/*     */   protected byte[] buf;
/*     */   protected int src_node_Id;
/*     */   protected int time_Stamp;
/*     */   protected int sequence_Id;
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     SGIPMessage localSGIPMessage1;
/*     */     try
/*     */     {
/*  37 */       SGIPMessage m = (SGIPMessage)super.clone();
/*  38 */       m.buf = ((byte[])this.buf.clone());
/*  39 */       return m;
/*     */     } catch (CloneNotSupportedException ex) {
/*  41 */       ex.printStackTrace();
/*  42 */       localSGIPMessage1 = null; } return localSGIPMessage1;
/*     */   }
/*     */ 
/*     */   public abstract String toString();
/*     */ 
/*     */   public abstract int getCommandId();
/*     */ 
/*     */   public int getSrcNodeId()
/*     */   {
/*  56 */     return this.src_node_Id;
/*     */   }
/*     */ 
/*     */   public void setSrcNodeId(int src_node_Id)
/*     */   {
/*  62 */     this.src_node_Id = src_node_Id;
/*  63 */     TypeConvert.int2byte(src_node_Id, this.buf, 8);
/*     */   }
/*     */ 
/*     */   public int getTimeStamp()
/*     */   {
/*  71 */     return this.time_Stamp;
/*     */   }
/*     */ 
/*     */   public void setTimeStamp(int time_Stamp)
/*     */   {
/*  77 */     this.time_Stamp = time_Stamp;
/*  78 */     TypeConvert.int2byte(time_Stamp, this.buf, 12);
/*     */   }
/*     */ 
/*     */   public int getSequenceId()
/*     */   {
/*  86 */     return this.sequence_Id;
/*     */   }
/*     */ 
/*     */   public void setSequenceId(int sequence_Id)
/*     */   {
/*  92 */     this.sequence_Id = sequence_Id;
/*  93 */     TypeConvert.int2byte(sequence_Id, this.buf, 16);
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/* 100 */     return this.buf;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.message.SGIPMessage
 * JD-Core Version:    0.5.3
 */