/*    */ package demo30;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*    */ import com.huawei.insa2.comm.cmpp30.message.CMPP30DeliverMessage;
/*    */ import com.huawei.insa2.util.Args;
/*    */ import com.huawei.smproxy.SMProxy30;
/*    */ 
/*    */ public class MySMProxy30 extends SMProxy30
/*    */ {
/* 19 */   private Demo30 demo = null;
/*    */ 
/*    */   public MySMProxy30(Demo30 demo, Args args) {
/* 22 */     super(args);
/* 23 */     this.demo = demo;
/*    */   }
/*    */ 
/*    */   public CMPPMessage onDeliver(CMPP30DeliverMessage msg)
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
 * Qualified Name:     demo30.MySMProxy30
 * JD-Core Version:    0.5.3
 */