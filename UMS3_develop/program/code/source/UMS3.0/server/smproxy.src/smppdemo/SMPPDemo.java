/*    */ package smppdemo;
/*    */ 
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPSubmitMessage;
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPUnbindMessage;
/*    */ import com.huawei.smproxy.SMPPSMProxy;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class SMPPDemo
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 19 */     System.out.print("Create 100 receiver Mobile No...");
/* 20 */     String[] rcvMobile = new String[100];
/* 21 */     int count = 0;
/* 22 */     for (int i = 0; i < 10; ++i) {
/* 23 */       for (int j = 0; j < 10; ++j) {
/* 24 */         rcvMobile[count] = String.valueOf(String.valueOf(new StringBuffer("86136030000").append(i).append(j)));
/* 25 */         ++count;
/*    */       }
/*    */     }
/* 28 */     System.out.println("OK");
/*    */ 
/* 30 */     System.out.print("new SMPPMessage...");
/*    */ 
/* 32 */     SMPPUnbindMessage exitMsg = new SMPPUnbindMessage();
/* 33 */     System.out.println("OK");
/* 34 */     int sendcount = 1;
/* 35 */     int sendinterval = 1;
/*    */     try
/*    */     {
/* 38 */       sendcount = Integer.parseInt(args[0]);
/*    */     } catch (Exception ex) {
/* 40 */       sendcount = 1;
/*    */     }
/*    */     try
/*    */     {
/* 44 */       sendinterval = Integer.parseInt(args[1]);
/*    */     } catch (Exception ex) {
/* 46 */       sendinterval = 1;
/*    */     }
/*    */ 
/* 49 */     SMSender sender = SMSender.getInstance();
/* 50 */     for (int i = 0; i < 100; ++i) {
/* 51 */       System.out.print("Send Message...");
/* 52 */       SMPPSubmitMessage msg = new SMPPSubmitMessage("", 0, 0, "8888", 1, 1, rcvMobile[i], 1, 1, 1, "", "", 1, 1, 15, 0, 20, "测试1234567890qazwsxedcr");
/*    */ 
/* 72 */       if (sender.send(msg))
/* 73 */         System.out.println("Success");
/*    */       else {
/* 75 */         System.out.println("Fail");
/*    */       }
/*    */       try
/*    */       {
/* 79 */         Thread.sleep(10 * sendinterval);
/*    */       } catch (Exception localException1) {
/*    */       }
/*    */     }
/*    */     try {
/* 84 */       sender.send(exitMsg);
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     smppdemo.SMPPDemo
 * JD-Core Version:    0.5.3
 */