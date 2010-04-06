/*    */ package com.huawei.insa2.comm.smgp;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.PWriter;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPMessage;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class SMGPWriter extends PWriter
/*    */ {
/*    */   protected OutputStream out;
/*    */ 
/*    */   public SMGPWriter(OutputStream out)
/*    */   {
/* 16 */     this.out = out;
/*    */   }
/*    */ 
/*    */   public void write(PMessage message)
/*    */     throws IOException
/*    */   {
/* 24 */     SMGPMessage msg = (SMGPMessage)message;
/* 25 */     this.out.write(msg.getBytes());
/*    */   }
/*    */ 
/*    */   public void writeHeartbeat()
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.SMGPWriter
 * JD-Core Version:    0.5.3
 */