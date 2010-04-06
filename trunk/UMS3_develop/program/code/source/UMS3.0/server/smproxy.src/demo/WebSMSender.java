/*     */ package demo;
/*     */ 
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPDeliverMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPDeliverRepMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPSubmitMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPSubmitRepMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import com.huawei.insa2.util.Cfg;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import com.huawei.smproxy.SMProxy;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class WebSMSender extends SMProxy
/*     */ {
/*  17 */   private static Args arg = Env.getConfig().getArgs("CMPPConnect");
/*     */ 
/*  19 */   public static final String service_Id = Env.getConfig().get("CMPPSubmitMessage/service_Id", "WebSM");
/*     */ 
/*  21 */   public static final String msg_Src = Env.getConfig().get("CMPPSubmitMessage/msg_Src", "WebSMS");
/*     */ 
/*  23 */   public static final String connectCode = Env.getConfig().get("CMPPSubmitMessage/src_Terminal_Id", "");
/*     */ 
/*  29 */   private static List msgs = new LinkedList();
/*     */   private static WebSMSender instance;
/*     */ 
/*     */   public static WebSMSender getInstance()
/*     */   {
/*  35 */     if (instance == null)
/*     */     {
/*  37 */       instance = new WebSMSender();
/*     */     }
/*  39 */     return instance;
/*     */   }
/*     */ 
/*     */   protected WebSMSender()
/*     */   {
/*  44 */     super(arg);
/*     */   }
/*     */ 
/*     */   public void OnTerminate()
/*     */   {
/*  52 */     System.out.println("Connection have been breaked! ");
/*     */   }
/*     */ 
/*     */   public CMPPMessage onDeliver(CMPPDeliverMessage msg)
/*     */   {
/*  61 */     byte[] msgId = msg.getMsgId();
/*     */ 
/*  63 */     if (msg.getRegisteredDeliver() == 1) {
/*  64 */       if (String.valueOf(msg.getStat()).equalsIgnoreCase("DELIVRD")) {
/*  65 */         System.out.println(String.valueOf(String.valueOf(new StringBuffer("\t\treceived DELIVRD message msgid=[").append(msg.getMsgId()).append("]"))));
/*  66 */         long submitMsgId = TypeConvert.byte2long(msg.getStatusMsgId());
/*  67 */         PreparedStatement stat = null;
/*     */         try {
/*  69 */           return new CMPPDeliverRepMessage(msgId, 0);
/*     */         }
/*     */         catch (Exception ex) {
/*  72 */           return new CMPPDeliverRepMessage(msgId, 9);
/*     */         }
/*     */       }
/*  75 */       return new CMPPDeliverRepMessage(msgId, 9);
/*     */     }
/*     */ 
/*  80 */     System.out.println(String.valueOf(String.valueOf(new StringBuffer("\t\treceived non DELIVRD message msgid=[").append(msg.getMsgId()).append("]"))));
/*  81 */     return new CMPPDeliverRepMessage(msgId, 9);
/*     */   }
/*     */ 
/*     */   public boolean send(CMPPSubmitMessage msg)
/*     */   {
/*  91 */     if (msg == null) {
/*  92 */       return false;
/*     */     }
/*  94 */     CMPPSubmitRepMessage reportMsg = null;
/*  95 */     PreparedStatement stat = null;
/*     */     try {
/*  97 */       reportMsg = (CMPPSubmitRepMessage)super.send(msg);
/*     */     }
/*     */     catch (IOException ex) {
/* 100 */       ex.printStackTrace();
/* 101 */       return 0;
/*     */     }
/* 103 */     return true;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     demo.WebSMSender
 * JD-Core Version:    0.5.3
 */