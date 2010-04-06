/*    */ package demo;
/*    */ 
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPDeliverMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPDeliverRespMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPMessage;
/*    */ import com.huawei.insa2.util.Args;
/*    */ import com.huawei.smproxy.SMGPSMProxy;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class MySMGPSMProxy extends SMGPSMProxy
/*    */ {
/*    */   public MySMGPSMProxy(Args args)
/*    */   {
/* 23 */     super(args);
/*    */   }
/*    */ 
/*    */   public SMGPMessage onDeliver(SMGPDeliverMessage msg)
/*    */   {
/* 28 */     byte[] msgId = msg.getMsgId();
/*    */ 
/* 31 */     int result = 0;
/*    */ 
/* 34 */     return new SMGPDeliverRespMessage(msgId, result);
/*    */   }
/*    */ 
/*    */   public void OnTerminate()
/*    */   {
/* 42 */     System.out.println("Connection have been breaked! ");
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     demo.MySMGPSMProxy
 * JD-Core Version:    0.5.3
 */