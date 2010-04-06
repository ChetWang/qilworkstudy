/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PSocketConnection;
/*     */ import com.huawei.insa2.comm.cngp.CNGPConnection;
/*     */ import com.huawei.insa2.comm.cngp.CNGPTransaction;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPDeliverMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPDeliverRespMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class CNGPSMProxy
/*     */ {
/*     */   private CNGPConnection conn;
/*     */ 
/*     */   public CNGPSMProxy(Map args)
/*     */   {
/*  25 */     this(new Args(args));
/*     */   }
/*     */ 
/*     */   public CNGPSMProxy(Args args)
/*     */   {
/*  33 */     this.conn = new CNGPConnection(args);
/*     */ 
/*  36 */     this.conn.addEventListener(new CNGPEventAdapter(this));
/*  37 */     this.conn.waitAvailable();
/*  38 */     if (!(this.conn.available()))
/*  39 */       throw new IllegalStateException(this.conn.getError());
/*     */   }
/*     */ 
/*     */   public CNGPMessage send(CNGPMessage message)
/*     */     throws IOException
/*     */   {
/*  49 */     if (message == null) {
/*  50 */       return null;
/*     */     }
/*     */ 
/*  53 */     CNGPTransaction t = (CNGPTransaction)this.conn.createChild();
/*     */     try {
/*  55 */       t.send(message);
/*  56 */       t.waitResponse();
/*  57 */       CNGPMessage rsp = t.getResponse();
/*  58 */       return rsp;
/*     */     }
/*     */     finally {
/*  61 */       t.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onTerminate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CNGPMessage onDeliver(CNGPDeliverMessage msg)
/*     */   {
/*  79 */     return new CNGPDeliverRespMessage(msg.getMsgId(), 0);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  86 */     this.conn.close();
/*     */   }
/*     */ 
/*     */   public CNGPConnection getConn()
/*     */   {
/*  93 */     return this.conn;
/*     */   }
/*     */ 
/*     */   public String getConnState()
/*     */   {
/* 100 */     return this.conn.getError();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.smproxy.CNGPSMProxy
 * JD-Core Version:    0.5.3
 */