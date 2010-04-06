/*    */ package com.huawei.insa2.util;
/*    */ 
/*    */ public abstract class WatchThread extends Thread
/*    */ {
/* 14 */   private boolean alive = true;
/*    */ 
/* 17 */   private String state = "unknown";
/*    */ 
/* 20 */   public static final ThreadGroup tg = new ThreadGroup("watch-thread");
/*    */ 
/*    */   public WatchThread(String name)
/*    */   {
/* 27 */     super(tg, name);
/* 28 */     super.setDaemon(true);
/*    */   }
/*    */ 
/*    */   public void kill()
/*    */   {
/* 37 */     this.alive = false;
/*    */   }
/*    */ 
/*    */   public final void run()
/*    */   {
/* 46 */     while (this.alive)
/*    */       try {
/* 48 */         task();
/*    */       } catch (Exception ex) {
/* 50 */         ex.printStackTrace();
/*    */       } catch (Throwable t) {
/* 52 */         t.printStackTrace();
/*    */       }
/*    */   }
/*    */ 
/*    */   protected void setState(String newState)
/*    */   {
/* 62 */     this.state = newState;
/*    */   }
/*    */ 
/*    */   public String getState()
/*    */   {
/* 70 */     return this.state;
/*    */   }
/*    */ 
/*    */   protected abstract void task();
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.util.WatchThread
 * JD-Core Version:    0.5.3
 */