/*    */ package cngpdemo;
/*    */ 
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPDeliverMessage;
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPDeliverRespMessage;
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPMessage;
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPSubmitMessage;
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPSubmitRespMessage;
/*    */ import com.huawei.insa2.util.Args;
/*    */ import com.huawei.insa2.util.Cfg;
/*    */ import com.huawei.smproxy.CNGPSMProxy;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.sql.PreparedStatement;
/*    */ 
/*    */ public class SMSender extends CNGPSMProxy
/*    */ {
/* 18 */   private static Args arg = Env.getConfig().getArgs("CNGPConnect");
/*    */   private static SMSender instance;
/*    */ 
/*    */   public static SMSender getInstance()
/*    */   {
/* 24 */     if (instance == null)
/*    */     {
/* 26 */       instance = new SMSender();
/*    */     }
/* 28 */     return instance;
/*    */   }
/*    */ 
/*    */   protected SMSender()
/*    */   {
/* 33 */     super(arg);
/*    */   }
/*    */ 
/*    */   public void OnTerminate()
/*    */   {
/* 41 */     System.out.println("Connection have been breaked! ");
/*    */   }
/*    */ 
/*    */   public CNGPMessage onDeliver(CNGPDeliverMessage msg)
/*    */   {
/* 50 */     return new CNGPDeliverRespMessage(msg.getMsgId(), 0);
/*    */   }
/*    */ 
/*    */   public boolean send(CNGPSubmitMessage msg)
/*    */   {
/* 59 */     if (msg == null) {
/* 60 */       return false;
/*    */     }
/* 62 */     CNGPSubmitRespMessage reportMsg = null;
/* 63 */     PreparedStatement stat = null;
/*    */     try {
/* 65 */       reportMsg = (CNGPSubmitRespMessage)super.send(msg);
/*    */     }
/*    */     catch (IOException ex) {
/* 68 */       ex.printStackTrace();
/* 69 */       return 0;
/*    */     }
/* 71 */     return true;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     cngpdemo.SMSender
 * JD-Core Version:    0.5.3
 */