/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PEventAdapter;
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.sgip.SGIPConnection;
/*     */ import com.huawei.insa2.comm.sgip.SGIPTransaction;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPBindMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPBindRepMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPUnbindRepMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPUserReportMessage;
/*     */ 
/*     */ class SGIPEventAdapter extends PEventAdapter
/*     */ {
/* 231 */   private SGIPSMProxy smProxy = null;
/* 232 */   private SGIPConnection conn = null;
/*     */ 
/*     */   public SGIPEventAdapter(SGIPSMProxy smProxy, SGIPConnection conn) {
/* 235 */     this.smProxy = smProxy;
/* 236 */     this.conn = conn;
/*     */   }
/*     */ 
/*     */   public void childCreated(PLayer child)
/*     */   {
/* 245 */     SGIPTransaction t = (SGIPTransaction)child;
/* 246 */     SGIPMessage msg = t.getResponse();
/*     */ 
/* 250 */     SGIPMessage resmsg = null;
/* 251 */     if (msg.getCommandId() == 2)
/*     */     {
/* 253 */       resmsg = new SGIPUnbindRepMessage();
/* 254 */       if (t.isChildOf(this.conn)) {
/* 255 */         this.smProxy.onTerminate();
/*     */       }
/*     */     }
/* 258 */     else if (msg.getCommandId() == 1)
/*     */     {
/* 260 */       SGIPBindMessage tmpmes = (SGIPBindMessage)msg;
/* 261 */       int logintype = tmpmes.getLoginType();
/* 262 */       if ((logintype != 2) && (logintype != 11)) {
/* 263 */         resmsg = new SGIPBindRepMessage(4);
/*     */       }
/* 265 */       resmsg = new SGIPBindRepMessage(0);
/*     */     }
/* 267 */     else if (msg.getCommandId() == 4)
/*     */     {
/* 269 */       SGIPDeliverMessage tmpmes = (SGIPDeliverMessage)msg;
/*     */ 
/* 271 */       resmsg = this.smProxy.onDeliver(tmpmes);
/*     */     }
/* 273 */     else if (msg.getCommandId() == 5)
/*     */     {
/* 275 */       SGIPReportMessage tmpmes = (SGIPReportMessage)msg;
/*     */ 
/* 277 */       resmsg = this.smProxy.onReport(tmpmes);
/*     */     }
/* 279 */     else if (msg.getCommandId() == 17)
/*     */     {
/* 281 */       SGIPUserReportMessage tmpmes = (SGIPUserReportMessage)msg;
/*     */ 
/* 283 */       resmsg = this.smProxy.onUserReport(tmpmes);
/*     */     }
/*     */     else
/*     */     {
/* 287 */       t.close();
/*     */     }
/*     */ 
/* 291 */     if (resmsg != null)
/*     */     {
/*     */       try {
/* 294 */         t.send(resmsg);
/*     */       }
/*     */       catch (PException e) {
/* 297 */         e.printStackTrace();
/*     */       }
/* 299 */       t.close();
/*     */     }
/*     */ 
/* 302 */     if (msg.getCommandId() != 2)
/*     */       return;
/* 304 */     SGIPConnection theconn = (SGIPConnection)t.getParent();
/* 305 */     theconn.close();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.smproxy.SGIPEventAdapter
 * JD-Core Version:    0.5.3
 */