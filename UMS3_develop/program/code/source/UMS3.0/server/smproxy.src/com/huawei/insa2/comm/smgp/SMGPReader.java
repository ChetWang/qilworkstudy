/*    */ package com.huawei.insa2.comm.smgp;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.PReader;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPActiveTestMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPActiveTestRespMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPDeliverMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPExitMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPExitRespMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPForwardRespMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPLoginRespMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPMoRouteUpdateRespMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPMtRouteUpdateRespMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPQueryRespMessage;
/*    */ import com.huawei.insa2.comm.smgp.message.SMGPSubmitRespMessage;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class SMGPReader extends PReader
/*    */ {
/*    */   protected DataInputStream in;
/*    */ 
/*    */   public SMGPReader(InputStream is)
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
/* 44 */       return new SMGPLoginRespMessage(buf);
/*    */     }
/* 46 */     if (command_Id == 3)
/*    */     {
/* 48 */       return new SMGPDeliverMessage(buf);
/*    */     }
/* 50 */     if (command_Id == -2147483646)
/*    */     {
/* 52 */       return new SMGPSubmitRespMessage(buf);
/*    */     }
/* 54 */     if (command_Id == -2147483643)
/*    */     {
/* 56 */       return new SMGPForwardRespMessage(buf);
/*    */     }
/* 58 */     if (command_Id == -2147483641)
/*    */     {
/* 60 */       return new SMGPQueryRespMessage(buf);
/*    */     }
/* 62 */     if (command_Id == -2147483644)
/*    */     {
/* 64 */       return new SMGPActiveTestRespMessage(buf);
/*    */     }
/* 66 */     if (command_Id == 4)
/*    */     {
/* 68 */       return new SMGPActiveTestMessage(buf);
/*    */     }
/* 70 */     if (command_Id == 6)
/*    */     {
/* 72 */       return new SMGPExitMessage(buf);
/*    */     }
/* 74 */     if (command_Id == -2147483642)
/*    */     {
/* 76 */       return new SMGPExitRespMessage(buf);
/*    */     }
/* 78 */     if (command_Id == -2147483640)
/*    */     {
/* 80 */       return new SMGPMtRouteUpdateRespMessage(buf);
/*    */     }
/* 82 */     if (command_Id == -2147483639)
/*    */     {
/* 84 */       return new SMGPMoRouteUpdateRespMessage(buf);
/*    */     }
/*    */ 
/* 87 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.SMGPReader
 * JD-Core Version:    0.5.3
 */