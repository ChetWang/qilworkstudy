/*    */ package com.huawei.insa2.comm.cngp;
/*    */ 
/*    */ import com.huawei.insa2.comm.PException;
/*    */ import com.huawei.insa2.comm.PLayer;
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPMessage;
/*    */ import com.huawei.insa2.util.Debug;
/*    */ 
/*    */ public class CNGPTransaction extends PLayer
/*    */ {
/*    */   private CNGPMessage receive;
/*    */   private int sequenceId;
/*    */ 
/*    */   public CNGPTransaction(PLayer connection)
/*    */   {
/* 24 */     super(connection);
/* 25 */     this.sequenceId = this.id;
/*    */   }
/*    */ 
/*    */   public synchronized void onReceive(PMessage msg)
/*    */   {
/* 33 */     this.receive = ((CNGPMessage)msg);
/* 34 */     this.sequenceId = this.receive.getSequenceId();
/* 35 */     if (CNGPConstant.debug == true)
/*    */     {
/* 37 */       Debug.dump(this.receive.toString());
/*    */     }
/*    */ 
/* 41 */     super.notifyAll();
/*    */   }
/*    */ 
/*    */   public void send(PMessage message)
/*    */     throws PException
/*    */   {
/* 51 */     CNGPMessage msg = (CNGPMessage)message;
/* 52 */     msg.setSequenceId(this.sequenceId);
/* 53 */     this.parent.send(message);
/* 54 */     if (CNGPConstant.debug != true)
/*    */       return;
/* 56 */     Debug.dump(msg.toString());
/*    */   }
/*    */ 
/*    */   public CNGPMessage getResponse()
/*    */   {
/* 65 */     return this.receive;
/*    */   }
/*    */ 
/*    */   public synchronized void waitResponse()
/*    */   {
/* 72 */     if (this.receive != null) return;
/*    */     try {
/* 74 */       super.wait(((CNGPConnection)this.parent).getTransactionTimeout());
/*    */     }
/*    */     catch (InterruptedException localInterruptedException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.CNGPTransaction
 * JD-Core Version:    0.5.3
 */