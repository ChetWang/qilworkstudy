/*     */ package com.huawei.insa2.comm.sgip;
/*     */ 
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PMessage;
/*     */ import com.huawei.insa2.comm.PReader;
/*     */ import com.huawei.insa2.util.WatchThread;
/*     */ import java.io.IOException;
/*     */ 
/*     */ class SGIPSocketConnection$1$ReceiveThread extends WatchThread
/*     */ {
/*     */   private final SGIPSocketConnection this$0;
/*     */ 
/*     */   public SGIPSocketConnection$1$ReceiveThread(SGIPSocketConnection this$0)
/*     */   {
/* 180 */     super(String.valueOf(String.valueOf(this$0.name)).concat("-receive"));
/*     */ 
/* 179 */     this.this$0 = this$0;
/*     */   }
/*     */ 
/*     */   public void task() {
/*     */     try {
/* 184 */       if (SGIPSocketConnection.access$0(this.this$0) == null) {
/* 185 */         PMessage m = this.this$0.in.read();
/* 186 */         if ((m == null) || 
/* 188 */           (m == null)) return;
/* 189 */         this.this$0.onReceive(m); return;
/*     */       }
/*     */ 
/* 192 */       if (SGIPSocketConnection.access$0(this.this$0) != SGIPSocketConnection.NOT_INIT)
/*     */         try {
/* 194 */           Thread.sleep(this.this$0.reconnectInterval);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException)
/*     */         {
/*     */         }
/* 199 */       this.this$0.connect();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.SGIPSocketConnection.1.ReceiveThread
 * JD-Core Version:    0.5.3
 */