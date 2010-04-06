/*    */ package com.huawei.insa2.comm.cmpp;
/*    */ 
/*    */ import com.huawei.insa2.comm.PException;
/*    */ import com.huawei.insa2.comm.PLayer;
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*    */ import com.huawei.insa2.util.Debug;
/*    */ 
/*    */ public class CMPPTransaction extends PLayer
/*    */ {
/*    */   private CMPPMessage receive;
/*    */   private int sequenceId;
/*    */ 
/*    */   public CMPPTransaction(PLayer connection)
/*    */   {
/* 24 */     super(connection);
/* 25 */     this.sequenceId = this.id;
/*    */   }
/*    */ 
/*    */   public synchronized void onReceive(PMessage msg)
/*    */   {
/* 33 */     this.receive = ((CMPPMessage)msg);
/* 34 */     this.sequenceId = this.receive.getSequenceId();
/* 35 */     if (CMPPConstant.debug == true)
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
/* 51 */     CMPPMessage mes = (CMPPMessage)message;
/* 52 */     mes.setSequenceId(this.sequenceId);
/* 53 */     this.parent.send(message);
/* 54 */     if (CMPPConstant.debug != true)
/*    */       return;
/* 56 */     Debug.dump(mes.toString());
/*    */   }
/*    */ 
/*    */   public CMPPMessage getResponse()
/*    */   {
/* 65 */     return this.receive;
/*    */   }
/*    */ 
/*    */   public synchronized void waitResponse()
/*    */   {
/* 72 */     if (this.receive != null) return;
/*    */     try {
/* 74 */       super.wait(((CMPPConnection)this.parent).getTransactionTimeout());
/*    */     }
/*    */     catch (InterruptedException localInterruptedException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.CMPPTransaction
 * JD-Core Version:    0.5.3
 */