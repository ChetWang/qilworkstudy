/*    */ package demo;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPSubmitMessage;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class CmppDemo
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 19 */     System.out.print("Create 100 receiver Mobile No...");
/* 20 */     String[] rcvMobile = new String[100];
/* 21 */     int count = 0;
/* 22 */     for (int i = 0; i < 10; ++i) {
/* 23 */       for (int j = 0; j < 10; ++j) {
/* 24 */         rcvMobile[count] = String.valueOf(String.valueOf(new StringBuffer("136000000").append(i).append(j)));
/* 25 */         ++count;
/*    */       }
/*    */     }
/* 28 */     System.out.println("OK");
/*    */ 
/* 30 */     System.out.print("new CMPPMessage...");
/* 31 */     CMPPSubmitMessage msg = new CMPPSubmitMessage(1, 1, 1, 1, "websms", 1, "", 0, 0, 0, "websms", "02", "10", new Date(System.currentTimeMillis() + 172800000), null, "888813512345678", rcvMobile, "testmessage".getBytes(), "");
/*    */ 
/* 53 */     System.out.println("OK");
/* 54 */     int sendcount = 1;
/* 55 */     int sendinterval = 1;
/*    */     try
/*    */     {
/* 58 */       sendcount = Integer.parseInt(args[0]);
/*    */     } catch (Exception ex) {
/* 60 */       sendcount = 1;
/*    */     }
/*    */     try
/*    */     {
/* 64 */       sendinterval = Integer.parseInt(args[1]);
/*    */     } catch (Exception ex) {
/* 66 */       sendinterval = 1;
/*    */     }
/*    */ 
/* 69 */     for (int i = 0; i < sendcount; ++i) {
/* 70 */       System.out.print("Send Message...");
/* 71 */       if (WebSMSender.getInstance().send(msg))
/* 72 */         System.out.println("Success");
/*    */       else {
/* 74 */         System.out.println("Fail");
/*    */       }
/*    */       try
/*    */       {
/* 78 */         Thread.sleep(1000 * sendinterval);
/*    */       }
/*    */       catch (Exception localException1)
/*    */       {
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     demo.CmppDemo
 * JD-Core Version:    0.5.3
 */