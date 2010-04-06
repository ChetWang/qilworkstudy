/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PEventAdapter;
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.smgp.SMGPConnection;
/*     */ import com.huawei.insa2.comm.smgp.SMGPTransaction;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPActiveTestRespMessage;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPDeliverMessage;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPExitRespMessage;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPMessage;
/*     */ 
/*     */ class SMGPEventAdapter extends PEventAdapter
/*     */ {
/* 112 */   private SMGPSMProxy smProxy = null;
/* 113 */   private SMGPConnection conn = null;
/*     */ 
/*     */   public SMGPEventAdapter(SMGPSMProxy smProxy) {
/* 116 */     this.smProxy = smProxy;
/* 117 */     this.conn = smProxy.getConn();
/*     */   }
/*     */ 
/*     */   public void childCreated(PLayer child)
/*     */   {
/* 126 */     SMGPTransaction t = (SMGPTransaction)child;
/* 127 */     SMGPMessage msg = t.getResponse();
/*     */ 
/* 131 */     SMGPMessage resmsg = null;
/* 132 */     if (msg.getRequestId() == 6)
/*     */     {
/* 134 */       resmsg = new SMGPExitRespMessage();
/* 135 */       this.smProxy.onTerminate();
/*     */     }
/* 137 */     else if (msg.getRequestId() == 4)
/*     */     {
/* 139 */       resmsg = new SMGPActiveTestRespMessage();
/*     */     }
/* 141 */     else if (msg.getRequestId() == 3)
/*     */     {
/* 143 */       SMGPDeliverMessage tmpmes = (SMGPDeliverMessage)msg;
/*     */ 
/* 145 */       resmsg = this.smProxy.onDeliver(tmpmes);
/*     */     }
/*     */     else
/*     */     {
/* 152 */       t.close();
/*     */     }
/*     */ 
/* 156 */     if (resmsg != null)
/*     */     {
/*     */       try {
/* 159 */         t.send(resmsg);
/*     */       }
/*     */       catch (PException e) {
/* 162 */         e.printStackTrace();
/*     */       }
/* 164 */       t.close();
/*     */     }
/*     */ 
/* 167 */     if (msg.getRequestId() != 6)
/*     */       return;
/* 169 */     this.conn.close();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.smproxy.SMGPEventAdapter
 * JD-Core Version:    0.5.3
 */