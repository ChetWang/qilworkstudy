/*    */ package demo30;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ public class SendReqThread30 extends Thread
/*    */ {
/* 15 */   private boolean alive = true;
/*    */ 
/* 18 */   public static final ThreadGroup tg = new ThreadGroup("Req-thread");
/*    */ 
/* 21 */   private Demo30 myDemo = null;
/*    */ 
/* 24 */   private long timeLong = 0L;
/* 25 */   private int sleepInterval = 0;
/* 26 */   private boolean IsSleep = false;
/*    */ 
/*    */   public SendReqThread30(String name, Demo30 demo, int timelong, int sleepinterval)
/*    */   {
/* 34 */     super(tg, name);
/* 35 */     super.setDaemon(true);
/* 36 */     this.myDemo = demo;
/* 37 */     this.timeLong = (timelong * 60 * 1000);
/* 38 */     if (this.sleepInterval == 0)
/*    */       return;
/* 40 */     this.sleepInterval = (sleepinterval * 1000);
/* 41 */     this.IsSleep = true;
/*    */   }
/*    */ 
/*    */   public final void run()
/*    */   {
/* 50 */     long beginTime = new Date().getTime();
/*    */ 
/* 52 */     if (!(this.alive))
/*    */       return;
/*    */     try
/*    */     {
/* 56 */       this.myDemo.Task();
/* 57 */       if (this.IsSleep)
/*    */       {
/* 59 */         Thread.sleep(this.sleepInterval);
/*    */       }
/*    */ 
/* 62 */       if (new Date().getTime() - beginTime < this.timeLong);
/*    */     }
/*    */     catch (Exception ex)
/*    */     {
/* 70 */       ex.printStackTrace();
/*    */     }
/*    */     catch (Throwable t)
/*    */     {
/* 75 */       t.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void Kill()
/*    */   {
/* 83 */     this.alive = false;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     demo30.SendReqThread30
 * JD-Core Version:    0.5.3
 */