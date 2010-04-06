/*     */ package com.huawei.insa2.comm;
/*     */ 
/*     */ import com.huawei.insa2.util.WatchThread;
/*     */ import java.io.IOException;
/*     */ 
/*     */ class PSocketConnection$1$ReceiveThread extends WatchThread
/*     */ {
/*     */   private final PSocketConnection this$0;
/*     */ 
/*     */   public PSocketConnection$1$ReceiveThread(PSocketConnection this$0)
/*     */   {
/* 207 */     super(String.valueOf(String.valueOf(this$0.name)).concat("-receive"));
/*     */ 
/* 206 */     this.this$0 = this$0;
/*     */   }
/*     */ 
/*     */   public void task() {
/*     */     try {
/* 211 */       if (PSocketConnection.access$0(this.this$0) == null) {
/* 212 */         PMessage m = this.this$0.in.read();
/* 213 */         if ((m == null) || 
/* 215 */           (m == null)) return;
/* 216 */         this.this$0.onReceive(m); return;
/*     */       }
/*     */ 
/* 219 */       if (PSocketConnection.access$0(this.this$0) != PSocketConnection.NOT_INIT)
/*     */         try {
/* 221 */           Thread.sleep(this.this$0.reconnectInterval);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException)
/*     */         {
/*     */         }
/* 226 */       this.this$0.connect();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.PSocketConnection.1.ReceiveThread
 * JD-Core Version:    0.5.3
 */