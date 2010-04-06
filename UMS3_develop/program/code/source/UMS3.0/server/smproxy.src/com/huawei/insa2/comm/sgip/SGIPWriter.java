/*    */ package com.huawei.insa2.comm.sgip;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.PWriter;
/*    */ import com.huawei.insa2.comm.sgip.message.SGIPMessage;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class SGIPWriter extends PWriter
/*    */ {
/*    */   protected OutputStream out;
/*    */ 
/*    */   public SGIPWriter(OutputStream out)
/*    */   {
/* 16 */     this.out = out;
/*    */   }
/*    */ 
/*    */   public void write(PMessage message)
/*    */     throws IOException
/*    */   {
/* 24 */     SGIPMessage msg = (SGIPMessage)message;
/* 25 */     this.out.write(msg.getBytes());
/*    */   }
/*    */ 
/*    */   public void writeHeartbeat()
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.SGIPWriter
 * JD-Core Version:    0.5.3
 */