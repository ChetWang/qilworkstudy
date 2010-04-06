/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PSocketConnection;
/*     */ import com.huawei.insa2.comm.smgp.SMGPConnection;
/*     */ import com.huawei.insa2.comm.smgp.SMGPTransaction;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPDeliverMessage;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPDeliverRespMessage;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class SMGPSMProxy
/*     */ {
/*     */   private SMGPConnection conn;
/*     */ 
/*     */   public SMGPSMProxy(Map args)
/*     */   {
/*  27 */     this(new Args(args));
/*     */   }
/*     */ 
/*     */   public SMGPSMProxy(Args args)
/*     */   {
/*  35 */     this.conn = new SMGPConnection(args);
/*     */ 
/*  38 */     this.conn.addEventListener(new SMGPEventAdapter(this));
/*  39 */     this.conn.waitAvailable();
/*  40 */     if (!(this.conn.available()))
/*  41 */       throw new IllegalStateException(this.conn.getError());
/*     */   }
/*     */ 
/*     */   public SMGPMessage send(SMGPMessage message)
/*     */     throws IOException
/*     */   {
/*  51 */     if (message == null) {
/*  52 */       return null;
/*     */     }
/*     */ 
/*  55 */     SMGPTransaction t = (SMGPTransaction)this.conn.createChild();
/*     */     try {
/*  57 */       t.send(message);
/*  58 */       t.waitResponse();
/*  59 */       SMGPMessage rsp = t.getResponse();
/*  60 */       return rsp;
/*     */     } finally {
/*  62 */       t.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onTerminate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SMGPMessage onDeliver(SMGPDeliverMessage msg)
/*     */   {
/*  81 */     return new SMGPDeliverRespMessage(msg.getMsgId(), 0);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  89 */     this.conn.close();
/*     */   }
/*     */ 
/*     */   public SMGPConnection getConn()
/*     */   {
/*  96 */     return this.conn;
/*     */   }
/*     */ 
/*     */   public String getConnState()
/*     */   {
/* 104 */     return this.conn.getError();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.smproxy.SMGPSMProxy
 * JD-Core Version:    0.5.3
 */