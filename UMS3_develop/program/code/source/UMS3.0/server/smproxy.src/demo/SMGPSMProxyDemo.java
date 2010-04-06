/*    */ package demo;
/*    */ 
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPSubmitMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPSubmitRespMessage;
/*    */ import com.huawei.insa2.util.Args;
/*    */ import com.huawei.insa2.util.Cfg;
/*    */ import com.huawei.smproxy.SMGPSMProxy;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class SMGPSMProxyDemo
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 22 */       Args cfgArgs = new Cfg("app.xml").getArgs("SMGPConnect");
/*    */ 
/* 26 */       MySMGPSMProxy mySMProxy = new MySMGPSMProxy(cfgArgs);
/*    */ 
/* 29 */       String[] rcvMobile = new String[1];
/* 30 */       rcvMobile[0] = "13988800001";
/*    */ 
/* 33 */       SMGPSubmitMessage msg = new SMGPSubmitMessage(1, 1, 1, "goodnews", "01", "999", "", 0, null, null, "3333", "13500000001", rcvMobile, "提交短消息678901234567890", "0");
/*    */ 
/* 50 */       SMGPSubmitRespMessage respMsg = (SMGPSubmitRespMessage)mySMProxy.send(msg);
/* 51 */       if (respMsg != null)
/*    */       {
/* 53 */         System.out.println("Get SubmitResp Message Success! The status = ".concat(String.valueOf(String.valueOf(respMsg.getStatus()))));
/*    */       }
/*    */       else
/*    */       {
/* 57 */         System.out.println("Get SubmitResp Message Fail!");
/*    */       }
/*    */ 
/* 61 */       mySMProxy.close();
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 65 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     demo.SMGPSMProxyDemo
 * JD-Core Version:    0.5.3
 */