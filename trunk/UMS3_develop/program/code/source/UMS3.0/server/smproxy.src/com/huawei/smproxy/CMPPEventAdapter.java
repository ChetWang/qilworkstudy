/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PEventAdapter;
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.cmpp.CMPPConnection;
/*     */ import com.huawei.insa2.comm.cmpp.CMPPTransaction;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPActiveRepMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPDeliverMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPTerminateRepMessage;
/*     */ 
/*     */ class CMPPEventAdapter extends PEventAdapter
/*     */ {
/* 111 */   private SMProxy smProxy = null;
/* 112 */   private CMPPConnection conn = null;
/*     */ 
/*     */   public CMPPEventAdapter(SMProxy smProxy) {
/* 115 */     this.smProxy = smProxy;
/* 116 */     this.conn = smProxy.getConn();
/*     */   }
/*     */ 
/*     */   public void childCreated(PLayer child)
/*     */   {
/* 125 */     CMPPTransaction t = (CMPPTransaction)child;
/* 126 */     CMPPMessage msg = t.getResponse();
/*     */ 
/* 130 */     CMPPMessage resmsg = null;
/* 131 */     if (msg.getCommandId() == 2)
/*     */     {
/* 133 */       resmsg = new CMPPTerminateRepMessage();
/* 134 */       this.smProxy.onTerminate();
/*     */     }
/* 136 */     else if (msg.getCommandId() == 8)
/*     */     {
/* 138 */       resmsg = new CMPPActiveRepMessage(0);
/*     */     }
/* 140 */     else if (msg.getCommandId() == 5)
/*     */     {
/* 142 */       CMPPDeliverMessage tmpmes = (CMPPDeliverMessage)msg;
/*     */ 
/* 144 */       resmsg = this.smProxy.onDeliver(tmpmes);
/*     */     }
/*     */     else
/*     */     {
/* 151 */       t.close();
/*     */     }
/*     */ 
/* 155 */     if (resmsg != null)
/*     */     {
/*     */       try {
/* 158 */         t.send(resmsg);
/*     */       }
/*     */       catch (PException e) {
/* 161 */         e.printStackTrace();
/*     */       }
/* 163 */       t.close();
/*     */     }
/*     */ 
/* 166 */     if (msg.getCommandId() != 2)
/*     */       return;
/* 168 */     this.conn.close();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.smproxy.CMPPEventAdapter
 * JD-Core Version:    0.5.3
 */