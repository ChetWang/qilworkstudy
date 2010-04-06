/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PSocketConnection;
/*     */ import com.huawei.insa2.comm.smpp.SMPPConnection;
/*     */ import com.huawei.insa2.comm.smpp.SMPPTransaction;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPDeliverMessage;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPDeliverRespMessage;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class SMPPSMProxy
/*     */ {
/*     */   private SMPPConnection conn;
/*     */ 
/*     */   public SMPPSMProxy(Map args)
/*     */   {
/*  25 */     this(new Args(args));
/*     */   }
/*     */ 
/*     */   public SMPPSMProxy(Args args)
/*     */   {
/*  33 */     this.conn = new SMPPConnection(args);
/*     */ 
/*  36 */     this.conn.addEventListener(new SMPPEventAdapter(this));
/*  37 */     this.conn.waitAvailable();
/*  38 */     if (!(this.conn.available()))
/*  39 */       throw new IllegalStateException(this.conn.getError());
/*     */   }
/*     */ 
/*     */   public SMPPMessage send(SMPPMessage message)
/*     */     throws IOException
/*     */   {
/*  49 */     if (message == null) {
/*  50 */       return null;
/*     */     }
/*     */ 
/*  53 */     SMPPTransaction t = (SMPPTransaction)this.conn.createChild();
/*     */     try {
/*  55 */       t.send(message);
/*  56 */       t.waitResponse();
/*  57 */       SMPPMessage rsp = t.getResponse();
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
/*     */   public SMPPMessage onDeliver(SMPPDeliverMessage msg)
/*     */   {
/*  79 */     return new SMPPDeliverRespMessage(0);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  86 */     this.conn.close();
/*     */   }
/*     */ 
/*     */   public SMPPConnection getConn()
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
 * Qualified Name:     com.huawei.smproxy.SMPPSMProxy
 * JD-Core Version:    0.5.3
 */