/*    */ package sgipdemo;
/*    */ 
/*    */ import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
/*    */ import com.huawei.insa2.comm.sgip.message.SGIPMessage;
/*    */ import com.huawei.insa2.util.Args;
/*    */ import com.huawei.smproxy.SGIPSMProxy;
/*    */ 
/*    */ public class MySGIPSMProxy extends SGIPSMProxy
/*    */ {
/* 18 */   private SGIPDemo demo = null;
/*    */ 
/*    */   public MySGIPSMProxy(SGIPDemo demo, Args args) {
/* 21 */     super(args);
/* 22 */     super.startService("10.70.116.79", 8801);
/* 23 */     this.demo = demo;
/*    */   }
/*    */ 
/*    */   public SGIPMessage onDeliver(SGIPDeliverMessage msg)
/*    */   {
/* 32 */     this.demo.ProcessRecvDeliverMsg(msg);
/* 33 */     return super.onDeliver(msg);
/*    */   }
/*    */ 
/*    */   public void OnTerminate() {
/* 37 */     this.demo.Terminate();
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     sgipdemo.MySGIPSMProxy
 * JD-Core Version:    0.5.3
 */