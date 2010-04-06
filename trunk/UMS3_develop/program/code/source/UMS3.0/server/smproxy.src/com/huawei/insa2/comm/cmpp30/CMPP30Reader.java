/*    */ package com.huawei.insa2.comm.cmpp30;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.PReader;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPActiveMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPActiveRepMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPCancelRepMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPQueryRepMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPTerminateMessage;
/*    */ import com.huawei.insa2.comm.cmpp.message.CMPPTerminateRepMessage;
/*    */ import com.huawei.insa2.comm.cmpp30.message.CMPP30ConnectRepMessage;
/*    */ import com.huawei.insa2.comm.cmpp30.message.CMPP30DeliverMessage;
/*    */ import com.huawei.insa2.comm.cmpp30.message.CMPP30SubmitRepMessage;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class CMPP30Reader extends PReader
/*    */ {
/*    */   protected DataInputStream in;
/*    */ 
/*    */   public CMPP30Reader(InputStream is)
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
/* 46 */       return new CMPP30ConnectRepMessage(buf);
/*    */     }
/* 48 */     if (command_Id == 5)
/*    */     {
/* 50 */       return new CMPP30DeliverMessage(buf);
/*    */     }
/* 52 */     if (command_Id == -2147483644)
/*    */     {
/* 54 */       return new CMPP30SubmitRepMessage(buf);
/*    */     }
/* 56 */     if (command_Id == -2147483642)
/*    */     {
/* 58 */       return new CMPPQueryRepMessage(buf);
/*    */     }
/* 60 */     if (command_Id == -2147483641)
/*    */     {
/* 62 */       return new CMPPCancelRepMessage(buf);
/*    */     }
/* 64 */     if (command_Id == -2147483640)
/*    */     {
/* 66 */       return new CMPPActiveRepMessage(buf);
/*    */     }
/* 68 */     if (command_Id == 8)
/*    */     {
/* 70 */       return new CMPPActiveMessage(buf);
/*    */     }
/* 72 */     if (command_Id == 2)
/*    */     {
/* 74 */       return new CMPPTerminateMessage(buf);
/*    */     }
/* 76 */     if (command_Id == -2147483646)
/*    */     {
/* 78 */       return new CMPPTerminateRepMessage(buf);
/*    */     }
/*    */ 
/* 81 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp30.CMPP30Reader
 * JD-Core Version:    0.5.3
 */