/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PEventAdapter;
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.smpp.SMPPConnection;
/*     */ import com.huawei.insa2.comm.smpp.SMPPTransaction;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPDeliverMessage;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPMessage;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPUnbindRespMessage;
/*     */ 
/*     */ class SMPPEventAdapter extends PEventAdapter
/*     */ {
/* 107 */   private SMPPSMProxy smProxy = null;
/* 108 */   private SMPPConnection conn = null;
/*     */ 
/*     */   public SMPPEventAdapter(SMPPSMProxy smProxy) { this.smProxy = smProxy;
/* 111 */     this.conn = smProxy.getConn();
/*     */   }
/*     */ 
/*     */   public void childCreated(PLayer child)
/*     */   {
/* 121 */     SMPPTransaction t = (SMPPTransaction)child;
/* 122 */     SMPPMessage msg = t.getResponse();
/*     */ 
/* 125 */     SMPPMessage resmsg = null;
/* 126 */     if (msg.getCommandId() == 6) {
/* 127 */       resmsg = new SMPPUnbindRespMessage();
/* 128 */       this.smProxy.onTerminate();
/*     */     }
/* 130 */     else if (msg.getCommandId() == 5) {
/* 131 */       SMPPDeliverMessage tmpmes = (SMPPDeliverMessage)msg;
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
 * Qualified Name:     com.huawei.smproxy.SMPPEventAdapter
 * JD-Core Version:    0.5.3
 */