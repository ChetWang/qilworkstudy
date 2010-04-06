/*     */ package cngpdemo;
/*     */ 
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPDeliverMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPDeliverRespMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPSubmitMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import com.huawei.insa2.util.Cfg;
/*     */ import com.huawei.smproxy.CNGPSMProxy;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class SMReceiver extends CNGPSMProxy
/*     */   implements Runnable
/*     */ {
/*  14 */   private static Args arg = Env.getConfig().getArgs("CNGPConnect");
/*     */   private static Thread instance;
/*     */ 
/*     */   public static Thread getInstance()
/*     */   {
/*  19 */     if (instance == null) {
/*  20 */       instance = new Thread(new SMReceiver());
/*     */     }
/*  22 */     return instance;
/*     */   }
/*     */ 
/*     */   protected SMReceiver() {
/*  26 */     super(arg);
/*     */   }
/*     */ 
/*     */   public void OnTerminate()
/*     */   {
/*  34 */     System.out.println("Connection have been breaked! ");
/*     */   }
/*     */ 
/*     */   public CNGPMessage onDeliver(CNGPDeliverMessage msg)
/*     */   {
/*  44 */     if (msg.getIsReport() != 1) {
/*  45 */       System.out.println("\n**************************Received a new message!***************************");
/*  46 */       System.out.println(msg.toString());
/*  47 */       System.out.println("The Sender is: ".concat(String.valueOf(String.valueOf(msg.getSrcTermID()))));
/*  48 */       System.out.println("***************************End new message! **************************\n");
/*     */     }
/*     */     else
/*     */     {
/*  85 */       System.out.println("\n++++++++++++++++++++++++++Received a new report!+++++++++++++++++++++++++");
/*  86 */       System.out.println(msg.toString());
/*  87 */       System.out.println("++++++++++++++++++++++++++End a new report!+++++++++++++++++++++++++\n");
/*     */     }
/*  89 */     return new CNGPDeliverRespMessage(msg.getMsgId(), 0);
/*     */   }
/*     */ 
/*     */   public boolean send(CNGPSubmitMessage msg)
/*     */   {
/* 118 */     if (msg == null)
/* 119 */       return false;
/*     */     try
/*     */     {
/* 122 */       super.send(msg);
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 125 */     return true;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try {
/* 131 */       Thread.sleep(500L);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 142 */     Thread receiver = getInstance();
/* 143 */     receiver.start();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     cngpdemo.SMReceiver
 * JD-Core Version:    0.5.3
 */