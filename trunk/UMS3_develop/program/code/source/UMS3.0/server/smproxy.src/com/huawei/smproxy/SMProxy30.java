/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PSocketConnection;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*     */ import com.huawei.insa2.comm.cmpp30.CMPP30Connection;
/*     */ import com.huawei.insa2.comm.cmpp30.CMPP30Transaction;
/*     */ import com.huawei.insa2.comm.cmpp30.message.CMPP30DeliverMessage;
/*     */ import com.huawei.insa2.comm.cmpp30.message.CMPP30DeliverRepMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class SMProxy30
/*     */ {
/*     */   private CMPP30Connection conn;
/*     */ 
/*     */   public SMProxy30(Map args)
/*     */   {
/*  28 */     this(new Args(args));
/*     */   }
/*     */ 
/*     */   public SMProxy30(Args args)
/*     */   {
/*  36 */     this.conn = new CMPP30Connection(args);
/*     */ 
/*  39 */     this.conn.addEventListener(new CMPP30EventAdapter(this));
/*  40 */     this.conn.waitAvailable();
/*  41 */     if (!(this.conn.available()))
/*  42 */       throw new IllegalStateException(this.conn.getError());
/*     */   }
/*     */ 
/*     */   public CMPPMessage send(CMPPMessage message)
/*     */     throws IOException
/*     */   {
/*  52 */     if (message == null) {
/*  53 */       return null;
/*     */     }
/*     */ 
/*  56 */     CMPP30Transaction t = (CMPP30Transaction)this.conn.createChild();
/*     */     try {
/*  58 */       t.send(message);
/*  59 */       t.waitResponse();
/*  60 */       CMPPMessage rsp = t.getResponse();
/*  61 */       return rsp;
/*     */     } finally {
/*  63 */       t.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onTerminate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CMPPMessage onDeliver(CMPP30DeliverMessage msg)
/*     */   {
/*  82 */     return new CMPP30DeliverRepMessage(msg.getMsgId(), 0);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  90 */     this.conn.close();
/*     */   }
/*     */ 
/*     */   public CMPP30Connection getConn()
/*     */   {
/*  97 */     return this.conn;
/*     */   }
/*     */ 
/*     */   public String getConnState()
/*     */   {
/* 105 */     return this.conn.getError();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.smproxy.SMProxy30
 * JD-Core Version:    0.5.3
 */