/*    */ package com.huawei.insa2.comm.sgip;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.PReader;
/*    */ import com.huawei.insa2.comm.sgip.message.SGIPBindMessage;
/*    */ import com.huawei.insa2.comm.sgip.message.SGIPBindRepMessage;
/*    */ import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
/*    */ import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;
/*    */ import com.huawei.insa2.comm.sgip.message.SGIPSubmitRepMessage;
/*    */ import com.huawei.insa2.comm.sgip.message.SGIPUnbindMessage;
/*    */ import com.huawei.insa2.comm.sgip.message.SGIPUnbindRepMessage;
/*    */ import com.huawei.insa2.comm.sgip.message.SGIPUserReportMessage;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class SGIPReader extends PReader
/*    */ {
/*    */   protected DataInputStream in;
/*    */ 
/*    */   public SGIPReader(InputStream is)
/*    */   {
/* 28 */     this.in = new DataInputStream(is);
/*    */   }
/*    */ 
/*    */   public PMessage read()
/*    */     throws IOException
/*    */   {
/* 37 */     int total_Length = this.in.readInt();
/* 38 */     int command_Id = this.in.readInt();
/*    */ 
/* 41 */     byte[] buf = new byte[total_Length - 8];
/* 42 */     this.in.readFully(buf);
/*    */ 
/* 44 */     if (command_Id == -2147483647)
/*    */     {
/* 46 */       return new SGIPBindRepMessage(buf);
/*    */     }
/* 48 */     if (command_Id == 1)
/*    */     {
/* 50 */       return new SGIPBindMessage(buf);
/*    */     }
/* 52 */     if (command_Id == 4)
/*    */     {
/* 54 */       return new SGIPDeliverMessage(buf);
/*    */     }
/* 56 */     if (command_Id == -2147483645)
/*    */     {
/* 58 */       return new SGIPSubmitRepMessage(buf);
/*    */     }
/* 60 */     if (command_Id == 5)
/*    */     {
/* 62 */       return new SGIPReportMessage(buf);
/*    */     }
/* 64 */     if (command_Id == 17)
/*    */     {
/* 66 */       return new SGIPUserReportMessage(buf);
/*    */     }
/* 68 */     if (command_Id == 2)
/*    */     {
/* 70 */       return new SGIPUnbindMessage(buf);
/*    */     }
/* 72 */     if (command_Id == -2147483646)
/*    */     {
/* 74 */       return new SGIPUnbindRepMessage(buf);
/*    */     }
/*    */ 
/* 77 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.SGIPReader
 * JD-Core Version:    0.5.3
 */