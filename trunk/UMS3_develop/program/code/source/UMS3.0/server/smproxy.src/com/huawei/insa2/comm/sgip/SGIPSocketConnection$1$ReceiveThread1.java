/*     */ package com.huawei.insa2.comm.sgip;
/*     */ 
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PMessage;
/*     */ import com.huawei.insa2.comm.PReader;
/*     */ import com.huawei.insa2.util.WatchThread;
/*     */ import java.io.IOException;
/*     */ 
/*     */ class SGIPSocketConnection$1$ReceiveThread1 extends WatchThread
/*     */ {
/*     */   private final SGIPSocketConnection this$0;
/*     */ 
/*     */   public SGIPSocketConnection$1$ReceiveThread1(SGIPSocketConnection this$0)
/*     */   {
/* 250 */     super(String.valueOf(String.valueOf(this$0.name)).concat("-receive"));
/*     */ 
/* 249 */     this.this$0 = this$0;
/*     */   }
/*     */ 
/*     */   public void task() {
/*     */     try {
/* 254 */       if (SGIPSocketConnection.access$0(this.this$0) == null) {
/* 255 */         PMessage m = this.this$0.in.read();
/* 256 */         if ((m == null) || 
/* 258 */           (m == null)) return;
/* 259 */         this.this$0.onReceive(m);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 267 */       this.this$0.setError(this.this$0.explain(ex));
/* 268 */       if (SGIPSocketConnection.access$0(this.this$0) == SGIPSocketConnection.RECEIVE_TIMEOUT) {
/* 269 */         this.this$0.setError(null);
/* 270 */         this.this$0.onReadTimeOut();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.SGIPSocketConnection.1.ReceiveThread1
 * JD-Core Version:    0.5.3
 */