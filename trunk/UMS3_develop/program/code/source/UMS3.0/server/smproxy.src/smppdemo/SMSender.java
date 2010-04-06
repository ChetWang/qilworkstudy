/*    */ package smppdemo;
/*    */ 
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPSubmitMessage;
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPSubmitRespMessage;
/*    */ import com.huawei.insa2.util.Args;
/*    */ import com.huawei.insa2.util.Cfg;
/*    */ import com.huawei.smproxy.SMPPSMProxy;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class SMSender extends SMPPSMProxy
/*    */ {
/* 18 */   private static Args arg = Env.getConfig().getArgs("SMPPConnect");
/*    */ 
/* 24 */   private static List msgs = new LinkedList();
/*    */   private static SMSender instance;
/*    */ 
/*    */   public static SMSender getInstance()
/*    */   {
/* 30 */     if (instance == null)
/*    */     {
/* 32 */       instance = new SMSender();
/*    */     }
/* 34 */     return instance;
/*    */   }
/*    */ 
/*    */   protected SMSender()
/*    */   {
/* 39 */     super(arg);
/*    */   }
/*    */ 
/*    */   public void OnTerminate()
/*    */   {
/* 47 */     System.out.println("Connection have been breaked! ");
/*    */   }
/*    */ 
/*    */   public boolean send(SMPPSubmitMessage msg)
/*    */   {
/* 86 */     if (msg == null) {
/* 87 */       return false;
/*    */     }
/* 89 */     SMPPSubmitRespMessage reportMsg = null;
/* 90 */     PreparedStatement stat = null;
/*    */     try {
/* 92 */       reportMsg = (SMPPSubmitRespMessage)super.send(msg);
/*    */     }
/*    */     catch (IOException ex) {
/* 95 */       ex.printStackTrace();
/* 96 */       return 0;
/*    */     }
/* 98 */     return true;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     smppdemo.SMSender
 * JD-Core Version:    0.5.3
 */