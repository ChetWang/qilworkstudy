/*    */ package com.huawei.insa2.comm.cngp;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.PReader;
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPDeliverMessage;
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPLoginRespMessage;
/*    */ import com.huawei.insa2.comm.cngp.message.CNGPSubmitRespMessage;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class CNGPReader extends PReader
/*    */ {
/*    */   protected DataInputStream in;
/*    */ 
/*    */   public CNGPReader(InputStream is)
/*    */   {
/* 27 */     this.in = new DataInputStream(is);
/*    */   }
/*    */ 
/*    */   public PMessage read()
/*    */     throws IOException
/*    */   {
/* 36 */     int totalLength = this.in.readInt();
/* 37 */     int commandId = this.in.readInt();
/*    */ 
/* 39 */     byte[] tmp = new byte[totalLength - 8];
/* 40 */     this.in.readFully(tmp);
/*    */ 
/* 43 */     byte[] buf = new byte[totalLength];
/* 44 */     TypeConvert.int2byte(totalLength, buf, 0);
/* 45 */     TypeConvert.int2byte(commandId, buf, 4);
/* 46 */     System.arraycopy(tmp, 0, buf, 8, totalLength - 8);
/*    */ 
/* 48 */     if (commandId == -2147483647)
/*    */     {
/* 50 */       return new CNGPLoginRespMessage(buf);
/*    */     }
/* 52 */     if (commandId == 3)
/*    */     {
/* 54 */       return new CNGPDeliverMessage(buf);
/*    */     }
/* 56 */     if (commandId == -2147483646)
/*    */     {
/* 58 */       return new CNGPSubmitRespMessage(buf);
/*    */     }
/*    */ 
/* 61 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.CNGPReader
 * JD-Core Version:    0.5.3
 */