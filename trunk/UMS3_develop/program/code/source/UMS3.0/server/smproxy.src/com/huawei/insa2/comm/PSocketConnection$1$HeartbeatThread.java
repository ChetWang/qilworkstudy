/*     */ package com.huawei.insa2.comm;
/*     */ 
/*     */ import com.huawei.insa2.util.WatchThread;
/*     */ import java.io.IOException;
/*     */ 
/*     */ class PSocketConnection$1$HeartbeatThread extends WatchThread
/*     */ {
/*     */   private final PSocketConnection this$0;
/*     */ 
/*     */   public PSocketConnection$1$HeartbeatThread(PSocketConnection this$0)
/*     */   {
/* 179 */     super(String.valueOf(String.valueOf(this$0.name)).concat("-heartbeat"));
/*     */ 
/* 178 */     this.this$0 = this$0;
/*     */ 
/* 180 */     super.setState(PSocketConnection.HEARTBEATING); }
/*     */ 
/*     */   public void task() {
/*     */     try {
/* 184 */       Thread.sleep(this.this$0.heartbeatInterval);
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {
/*     */     }
/* 188 */     if ((PSocketConnection.access$0(this.this$0) != null) || (this.this$0.out == null)) return;
/*     */     try {
/* 190 */       this.this$0.heartbeat();
/*     */     } catch (IOException ex) {
/* 192 */       this.this$0.setError(String.valueOf(String.valueOf(PSocketConnection.SEND_ERROR)).concat(String.valueOf(String.valueOf(this.this$0.explain(ex)))));
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.PSocketConnection.1.HeartbeatThread
 * JD-Core Version:    0.5.3
 */