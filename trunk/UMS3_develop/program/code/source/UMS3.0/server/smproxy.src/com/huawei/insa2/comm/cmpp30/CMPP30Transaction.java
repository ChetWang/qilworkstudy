/*    */ package com.huawei.insa2.comm.cmpp30;
/*    */ 
/*    */ import com.huawei.insa2.comm.PException;
/*    */ import com.huawei.insa2.comm.PLayer;
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*    */ import com.huawei.insa2.util.Debug;
/*    */ 
/*    */ public class CMPP30Transaction extends PLayer
/*    */ {
/*    */   private CMPPMessage receive;
/*    */   private int sequenceId;
/*    */ 
/*    */   public CMPP30Transaction(PLayer connection)
/*    */   {
/* 26 */     super(connection);
/* 27 */     this.sequenceId = this.id;
/*    */   }
/*    */ 
/*    */   public synchronized void onReceive(PMessage msg)
/*    */   {
/* 35 */     this.receive = ((CMPPMessage)msg);
/* 36 */     this.sequenceId = this.receive.getSequenceId();
/* 37 */     if (CMPPConstant.debug == true)
/*    */     {
/* 39 */       Debug.dump(this.receive.toString());
/*    */     }
/*    */ 
/* 43 */     super.notifyAll();
/*    */   }
/*    */ 
/*    */   public void send(PMessage message)
/*    */     throws PException
/*    */   {
/* 53 */     CMPPMessage mes = (CMPPMessage)message;
/* 54 */     mes.setSequenceId(this.sequenceId);
/* 55 */     this.parent.send(message);
/* 56 */     if (CMPPConstant.debug != true)
/*    */       return;
/* 58 */     Debug.dump(mes.toString());
/*    */   }
/*    */ 
/*    */   public CMPPMessage getResponse()
/*    */   {
/* 67 */     return this.receive;
/*    */   }
/*    */ 
/*    */   public synchronized void waitResponse()
/*    */   {
/* 74 */     if (this.receive != null) return;
/*    */     try {
/* 76 */       super.wait(((CMPP30Connection)this.parent).getTransactionTimeout());
/*    */     }
/*    */     catch (InterruptedException localInterruptedException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp30.CMPP30Transaction
 * JD-Core Version:    0.5.3
 */