/*    */ package com.huawei.insa2.comm.cmpp;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.PWriter;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class CMPPWriter extends PWriter
/*    */ {
/*    */   protected OutputStream out;
/*    */ 
/*    */   public CMPPWriter(OutputStream out)
/*    */   {
/* 16 */     this.out = out;
/*    */   }
/*    */ 
/*    */   public void write(PMessage message)
/*    */     throws IOException
/*    */   {
/* 24 */     CMPPMessage msg = (CMPPMessage)message;
/* 25 */     this.out.write(msg.getBytes());
/*    */   }
/*    */ 
/*    */   public void writeHeartbeat()
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.CMPPWriter
 * JD-Core Version:    0.5.3
 */