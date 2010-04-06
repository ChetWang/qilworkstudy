/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PEventAdapter;
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPActiveRepMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPTerminateRepMessage;
/*     */ import com.huawei.insa2.comm.cmpp30.CMPP30Connection;
/*     */ import com.huawei.insa2.comm.cmpp30.CMPP30Transaction;
/*     */ import com.huawei.insa2.comm.cmpp30.message.CMPP30DeliverMessage;
/*     */ 
/*     */ class CMPP30EventAdapter extends PEventAdapter
/*     */ {
/* 113 */   private SMProxy30 smProxy = null;
/* 114 */   private CMPP30Connection conn = null;
/*     */ 
/*     */   public CMPP30EventAdapter(SMProxy30 smProxy)
/*     */   {
/* 118 */     this.smProxy = smProxy;
/* 119 */     this.conn = smProxy.getConn();
/*     */   }
/*     */ 
/*     */   public void childCreated(PLayer child)
/*     */   {
/* 128 */     CMPP30Transaction t = (CMPP30Transaction)child;
/* 129 */     CMPPMessage msg = t.getResponse();
/*     */ 
/* 133 */     CMPPMessage resmsg = null;
/* 134 */     if (msg.getCommandId() == 2)
/*     */     {
/* 136 */       resmsg = new CMPPTerminateRepMessage();
/* 137 */       this.smProxy.onTerminate();
/*     */     }
/* 139 */     else if (msg.getCommandId() == 8)
/*     */     {
/* 141 */       resmsg = new CMPPActiveRepMessage(0);
/*     */     }
/* 143 */     else if (msg.getCommandId() == 5)
/*     */     {
/* 145 */       CMPP30DeliverMessage tmpmes = (CMPP30DeliverMessage)msg;
/* 146 */       resmsg = this.smProxy.onDeliver(tmpmes);
/*     */     }
/*     */     else
/*     */     {
/* 150 */       t.close();
/*     */     }
/*     */ 
/* 154 */     if (resmsg != null)
/*     */     {
/*     */       try {
/* 157 */         t.send(resmsg);
/*     */       }
/*     */       catch (PException e) {
/* 160 */         e.printStackTrace();
/*     */       }
/* 162 */       t.close();
/*     */     }
/*     */ 
/* 165 */     if (msg.getCommandId() != 2)
/*     */       return;
/* 167 */     this.conn.close();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.smproxy.CMPP30EventAdapter
 * JD-Core Version:    0.5.3
 */