/*    */ package com.huawei.insa2.comm;
/*    */ 
/*    */ public class PEventAdapter
/*    */   implements PEventListener
/*    */ {
/*    */   public void handle(PEvent e)
/*    */   {
/* 19 */     switch (e.getType())
/*    */     {
/*    */     case 2:
/* 21 */       childCreated((PLayer)e.getData());
/*    */ 
/* 46 */       break;
/*    */     case 1:
/* 24 */       created();
/*    */ 
/* 46 */       break;
/*    */     case 4:
/* 27 */       deleted();
/*    */ 
/* 46 */       break;
/*    */     case 64:
/* 30 */       messageDispatchFail((PMessage)e.getData());
/*    */ 
/* 46 */       break;
/*    */     case 32:
/* 33 */       messageDispatchFail((PMessage)e.getData());
/*    */ 
/* 46 */       break;
/*    */     case 8:
/* 36 */       messageDispatchFail((PMessage)e.getData());
/*    */ 
/* 46 */       break;
/*    */     case 16:
/* 39 */       messageDispatchFail((PMessage)e.getData());
/*    */     }
/*    */   }
/*    */ 
/*    */   public void childCreated(PLayer child)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void messageSendError(PMessage msg)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void messageSendSuccess(PMessage msg)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void messageDispatchFail(PMessage msg)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void messageDispatchSuccess(PMessage msg)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void created()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void deleted()
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.PEventAdapter
 * JD-Core Version:    0.5.3
 */