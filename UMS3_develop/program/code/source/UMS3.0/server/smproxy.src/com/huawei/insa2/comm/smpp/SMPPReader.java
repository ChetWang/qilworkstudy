/*    */ package com.huawei.insa2.comm.smpp;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.comm.PReader;
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPDeliverMessage;
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPEnquireLinkMessage;
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPEnquireLinkRespMessage;
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPLoginRespMessage;
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPSubmitRespMessage;
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPUnbindMessage;
/*    */ import com.huawei.insa2.comm.smpp.message.SMPPUnbindRespMessage;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class SMPPReader extends PReader
/*    */ {
/*    */   protected DataInputStream in;
/*    */ 
/*    */   public SMPPReader(InputStream is)
/*    */   {
/* 27 */     this.in = new DataInputStream(is);
/*    */   }
/*    */ 
/*    */   public PMessage read()
/*    */     throws IOException
/*    */   {
/* 36 */     int total_Length = this.in.readInt();
/* 37 */     int command_Id = this.in.readInt();
/*    */ 
/* 40 */     byte[] tmpbuf = new byte[total_Length - 8];
/* 41 */     this.in.readFully(tmpbuf);
/*    */ 
/* 43 */     byte[] buf = new byte[total_Length];
/* 44 */     System.arraycopy(tmpbuf, 0, buf, 8, tmpbuf.length);
/* 45 */     TypeConvert.int2byte(total_Length, buf, 0);
/* 46 */     TypeConvert.int2byte(command_Id, buf, 4);
/*    */ 
/* 48 */     if ((command_Id == -2147483647) || (command_Id == -2147483646))
/*    */     {
/* 51 */       return new SMPPLoginRespMessage(buf);
/*    */     }
/* 53 */     if (command_Id == 5)
/*    */     {
/* 55 */       return new SMPPDeliverMessage(buf);
/*    */     }
/* 57 */     if (command_Id == -2147483644)
/*    */     {
/* 59 */       return new SMPPSubmitRespMessage(buf);
/*    */     }
/* 61 */     if (command_Id == 21)
/*    */     {
/* 63 */       return new SMPPEnquireLinkMessage(buf);
/*    */     }
/* 65 */     if (command_Id == -2147483627)
/*    */     {
/* 67 */       return new SMPPEnquireLinkRespMessage(buf);
/*    */     }
/* 69 */     if (command_Id == 6)
/*    */     {
/* 71 */       return new SMPPUnbindMessage(buf);
/*    */     }
/* 73 */     if (command_Id == -2147483642)
/*    */     {
/* 75 */       return new SMPPUnbindRespMessage(buf);
/*    */     }
/*    */ 
/* 78 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.SMPPReader
 * JD-Core Version:    0.5.3
 */