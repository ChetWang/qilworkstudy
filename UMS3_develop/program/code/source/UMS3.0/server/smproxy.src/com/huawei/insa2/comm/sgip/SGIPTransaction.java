/*     */ package com.huawei.insa2.comm.sgip;
/*     */ 
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPMessage;
/*     */ import com.huawei.insa2.util.Debug;
/*     */ 
/*     */ public class SGIPTransaction extends PLayer
/*     */ {
/*     */   private SGIPMessage receive;
/*     */   private int src_nodeid;
/*     */   private int timestamp;
/*     */   private int sequenceId;
/*     */ 
/*     */   public SGIPTransaction(PLayer connection)
/*     */   {
/*  28 */     super(connection);
/*  29 */     this.sequenceId = this.id;
/*     */   }
/*     */ 
/*     */   public void setSPNumber(int spNumber)
/*     */   {
/*  37 */     this.src_nodeid = spNumber;
/*     */   }
/*     */ 
/*     */   public void setTimestamp(int timestamp)
/*     */   {
/*  45 */     this.timestamp = timestamp;
/*     */   }
/*     */ 
/*     */   public synchronized void onReceive(PMessage msg)
/*     */   {
/*  53 */     this.receive = ((SGIPMessage)msg);
/*  54 */     this.src_nodeid = this.receive.getSrcNodeId();
/*  55 */     this.timestamp = this.receive.getTimeStamp();
/*  56 */     this.sequenceId = this.receive.getSequenceId();
/*  57 */     if (SGIPConstant.debug == true)
/*     */     {
/*  59 */       Debug.dump(this.receive.toString());
/*     */     }
/*     */ 
/*  63 */     super.notifyAll();
/*     */   }
/*     */ 
/*     */   public void send(PMessage message)
/*     */     throws PException
/*     */   {
/*  73 */     SGIPMessage mes = (SGIPMessage)message;
/*  74 */     mes.setSrcNodeId(this.src_nodeid);
/*  75 */     mes.setTimeStamp(this.timestamp);
/*  76 */     mes.setSequenceId(this.sequenceId);
/*  77 */     this.parent.send(message);
/*  78 */     if (SGIPConstant.debug != true)
/*     */       return;
/*  80 */     Debug.dump(mes.toString());
/*     */   }
/*     */ 
/*     */   public SGIPMessage getResponse()
/*     */   {
/*  89 */     return this.receive;
/*     */   }
/*     */ 
/*     */   public boolean isChildOf(PLayer connection)
/*     */   {
/*  96 */     if (this.parent == null) {
/*  97 */       return false;
/*     */     }
/*  99 */     return (connection == this.parent);
/*     */   }
/*     */ 
/*     */   public PLayer getParent()
/*     */   {
/* 107 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public synchronized void waitResponse()
/*     */   {
/* 114 */     if (this.receive != null) return;
/*     */     try {
/* 116 */       super.wait(((SGIPConnection)this.parent).getTransactionTimeout());
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.SGIPTransaction
 * JD-Core Version:    0.5.3
 */