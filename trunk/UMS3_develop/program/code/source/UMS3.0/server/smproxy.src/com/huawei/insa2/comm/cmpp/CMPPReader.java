/*    */ package com.huawei.insa2.comm.cmpp;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.PReader;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPActiveMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPActiveRepMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPCancelRepMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPConnectRepMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPDeliverMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPQueryRepMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPSubmitRepMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPTerminateMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPTerminateRepMessage;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class CMPPReader extends PReader
/*    */ {
/*    */   protected DataInputStream in;
/*    */ 
/*    */   public CMPPReader(InputStream is)
/*    */   {
/* 26 */     this.in = new DataInputStream(is);
/*    */   }
/*    */ 
/*    */   public PMessage read()
/*    */     throws IOException
/*    */   {
/* 35 */     int total_Length = this.in.readInt();
/* 36 */     int command_Id = this.in.readInt();
/*    */ 
/* 39 */     byte[] buf = new byte[total_Length - 8];
/* 40 */     this.in.readFully(buf);
/*    */ 
/* 42 */     if (command_Id == -2147483647)
/*    */     {
/* 44 */       return new CMPPConnectRepMessage(buf);
/*    */     }
/* 46 */     if (command_Id == 5)
/*    */     {
/* 48 */       return new CMPPDeliverMessage(buf);
/*    */     }
/* 50 */     if (command_Id == -2147483644)
/*    */     {
/* 52 */       return new CMPPSubmitRepMessage(buf);
/*    */     }
/* 54 */     if (command_Id == -2147483642)
/*    */     {
/* 56 */       return new CMPPQueryRepMessage(buf);
/*    */     }
/* 58 */     if (command_Id == -2147483641)
/*    */     {
/* 60 */       return new CMPPCancelRepMessage(buf);
/*    */     }
/* 62 */     if (command_Id == -2147483640)
/*    */     {
/* 64 */       return new CMPPActiveRepMessage(buf);
/*    */     }
/* 66 */     if (command_Id == 8)
/*    */     {
/* 68 */       return new CMPPActiveMessage(buf);
/*    */     }
/* 70 */     if (command_Id == 2)
/*    */     {
/* 72 */       return new CMPPTerminateMessage(buf);
/*    */     }
/* 74 */     if (command_Id == -2147483646)
/*    */     {
/* 76 */       return new CMPPTerminateRepMessage(buf);
/*    */     }
/*    */ 
/* 79 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.CMPPReader
 * JD-Core Version:    0.5.3
 */