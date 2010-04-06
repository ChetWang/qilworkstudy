/*    */ package cngpdemo;
/*    */ 
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPExitMessage;
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPSubmitMessage;
/*    */ import com.huawei.smproxy.CNGPSMProxy;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class CNGPDemo
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 19 */     System.out.print("Create 100 receiver Mobile No...");
/* 20 */     String[] rcvMobile = new String[100];
/* 21 */     int count = 0;
/* 22 */     for (int i = 0; i < 10; ++i) {
/* 23 */       for (int j = 0; j < 10; ++j) {
/* 24 */         rcvMobile[count] = String.valueOf(String.valueOf(new StringBuffer("075540000").append(i).append(j)));
/* 25 */         ++count;
/*    */       }
/*    */     }
/* 28 */     System.out.println("OK");
/*    */ 
/* 30 */     System.out.print("new CNGPMessage...");
/*    */ 
/* 32 */     CNGPSubmitMessage msg = new CNGPSubmitMessage("3AAAQQQQQQ", 1, 1, 0, "abcdefghij", "01", 0, "123456", 15, null, null, "106213512345678", "075540512345678", 20, rcvMobile, 8, "测试test", 0);
/*    */ 
/* 52 */     CNGPExitMessage exitMsg = new CNGPExitMessage();
/* 53 */     System.out.println("OK");
/* 54 */     int sendcount = 1;
/* 55 */     int sendinterval = 1;
/*    */     try
/*    */     {
/* 58 */       sendcount = Integer.parseInt(args[0]);
/*    */     }
/*    */     catch (Exception ex) {
/* 61 */       sendcount = 1;
/*    */     }
/*    */     try
/*    */     {
/* 65 */       sendinterval = Integer.parseInt(args[1]);
/*    */     }
/*    */     catch (Exception ex) {
/* 68 */       sendinterval = 1;
/*    */     }
/*    */ 
/* 71 */     SMSender sender = SMSender.getInstance();
/* 72 */     for (int i = 0; i < sendcount; ++i) {
/* 73 */       System.out.print("Send Message...");
/* 74 */       if (sender.send(msg)) {
/* 75 */         System.out.println("Success");
/*    */       }
/*    */       else {
/* 78 */         System.out.println("Fail");
/*    */       }
/*    */       try
/*    */       {
/* 82 */         Thread.sleep(1000 * sendinterval);
/*    */       }
/*    */       catch (Exception localException1) {
/*    */       }
/*    */     }
/*    */     try {
/* 88 */       sender.send(exitMsg);
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     cngpdemo.CNGPDemo
 * JD-Core Version:    0.5.3
 */