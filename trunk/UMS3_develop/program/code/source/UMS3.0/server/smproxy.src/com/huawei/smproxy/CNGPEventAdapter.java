/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PEventAdapter;
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.cngp.CNGPConnection;
/*     */ import com.huawei.insa2.comm.cngp.CNGPTransaction;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPDeliverMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPExitRespMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPMessage;
/*     */ 
/*     */ class CNGPEventAdapter extends PEventAdapter
/*     */ {
/* 107 */   private CNGPSMProxy smProxy = null;
/* 108 */   private CNGPConnection conn = null;
/*     */ 
/*     */   public CNGPEventAdapter(CNGPSMProxy smProxy) { this.smProxy = smProxy;
/* 111 */     this.conn = smProxy.getConn();
/*     */   }
/*     */ 
/*     */   public void childCreated(PLayer child)
/*     */   {
/* 121 */     CNGPTransaction t = (CNGPTransaction)child;
/* 122 */     CNGPMessage msg = t.getResponse();
/*     */ 
/* 125 */     CNGPMessage resmsg = null;
/* 126 */     if (msg.getRequestId() == 6) {
/* 127 */       resmsg = new CNGPExitRespMessage();
/* 128 */       this.smProxy.onTerminate();
/*     */     }
/* 130 */     else if (msg.getRequestId() == 3) {
/* 131 */       CNGPDeliverMessage tmpmes = (CNGPDeliverMessage)msg;
/*     */ 
/* 133 */       resmsg = this.smProxy.onDeliver(tmpmes);
/*     */     }
/*     */     else
/*     */     {
/* 139 */       t.close();
/*     */     }
/*     */ 
/* 143 */     if (resmsg == null)
/*     */       return;
/*     */     try {
/* 146 */       t.send(resmsg);
/*     */     }
/*     */     catch (PException e) {
/* 149 */       e.printStackTrace();
/*     */     }
/* 151 */     t.close();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.smproxy.CNGPEventAdapter
 * JD-Core Version:    0.5.3
 */