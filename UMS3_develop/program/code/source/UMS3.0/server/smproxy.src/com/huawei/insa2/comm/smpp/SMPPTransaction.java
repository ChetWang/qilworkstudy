/*    */ package com.huawei.insa2.comm.smpp;
/*    */ 
/*    */ import com.huawei.insa2.comm.PException;
/*    */ import com.huawei.insa2.comm.PLayer;
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPMessage;
/*    */ import com.huawei.insa2.util.Debug;
/*    */ 
/*    */ public class SMPPTransaction extends PLayer
/*    */ {
/*    */   private SMPPMessage receive;
/*    */   private int sequenceId;
/*    */ 
/*    */   public SMPPTransaction(PLayer connection)
/*    */   {
/* 24 */     super(connection);
/* 25 */     this.sequenceId = this.id;
/*    */   }
/*    */ 
/*    */   public synchronized void onReceive(PMessage msg)
/*    */   {
/* 33 */     this.receive = ((SMPPMessage)msg);
/* 34 */     this.sequenceId = this.receive.getSequenceId();
/* 35 */     if (SMPPConstant.debug == true)
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
/* 51 */     SMPPMessage mes = (SMPPMessage)message;
/* 52 */     mes.setSequenceId(this.sequenceId);
/* 53 */     this.parent.send(message);
/* 54 */     if (SMPPConstant.debug != true)
/*    */       return;
/* 56 */     Debug.dump(mes.toString());
/*    */   }
/*    */ 
/*    */   public SMPPMessage getResponse()
/*    */   {
/* 65 */     return this.receive;
/*    */   }
/*    */ 
/*    */   public synchronized void waitResponse()
/*    */   {
/* 72 */     if (this.receive != null) return;
/*    */     try {
/* 74 */       super.wait(((SMPPConnection)this.parent).getTransactionTimeout());
/*    */     }
/*    */     catch (InterruptedException localInterruptedException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.SMPPTransaction
 * JD-Core Version:    0.5.3
 */