/*    */ package com.huawei.insa2.comm.cngp;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.PWriter;
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPMessage;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class CNGPWriter extends PWriter
/*    */ {
/*    */   protected OutputStream out;
/*    */ 
/*    */   public CNGPWriter(OutputStream out)
/*    */   {
/* 16 */     this.out = out;
/*    */   }
/*    */ 
/*    */   public void write(PMessage message)
/*    */     throws IOException
/*    */   {
/* 24 */     this.out.write(((CNGPMessage)message).getBytes());
/*    */   }
/*    */ 
/*    */   public void writeHeartbeat()
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.CNGPWriter
 * JD-Core Version:    0.5.3
 */