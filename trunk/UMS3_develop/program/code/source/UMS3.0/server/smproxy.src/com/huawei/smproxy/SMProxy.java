/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PSocketConnection;
/*     */ import com.huawei.insa2.comm.cmpp.CMPPConnection;
/*     */ import com.huawei.insa2.comm.cmpp.CMPPTransaction;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPDeliverMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPDeliverRepMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class SMProxy
/*     */ {
/*     */   private CMPPConnection conn;
/*     */ 
/*     */   public SMProxy(Map args)
/*     */   {
/*  26 */     this(new Args(args));
/*     */   }
/*     */ 
/*     */   public SMProxy(Args args)
/*     */   {
/*  34 */     this.conn = new CMPPConnection(args);
/*     */ 
/*  37 */     this.conn.addEventListener(new CMPPEventAdapter(this));
/*  38 */     this.conn.waitAvailable();
/*  39 */     if (!(this.conn.available()))
/*  40 */       throw new IllegalStateException(this.conn.getError());
/*     */   }
/*     */ 
/*     */   public CMPPMessage send(CMPPMessage message)
/*     */     throws IOException
/*     */   {
/*  50 */     if (message == null) {
/*  51 */       return null;
/*     */     }
/*     */ 
/*  54 */     CMPPTransaction t = (CMPPTransaction)this.conn.createChild();
/*     */     try {
/*  56 */       t.send(message);
/*  57 */       t.waitResponse();
/*  58 */       CMPPMessage rsp = t.getResponse();
/*  59 */       return rsp;
/*     */     } finally {
/*  61 */       t.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onTerminate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CMPPMessage onDeliver(CMPPDeliverMessage msg)
/*     */   {
/*  80 */     return new CMPPDeliverRepMessage(msg.getMsgId(), 0);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  88 */     this.conn.close();
/*     */   }
/*     */ 
/*     */   public CMPPConnection getConn()
/*     */   {
/*  95 */     return this.conn;
/*     */   }
/*     */ 
/*     */   public String getConnState()
/*     */   {
/* 103 */     return this.conn.getError();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.smproxy.SMProxy
 * JD-Core Version:    0.5.3
 */