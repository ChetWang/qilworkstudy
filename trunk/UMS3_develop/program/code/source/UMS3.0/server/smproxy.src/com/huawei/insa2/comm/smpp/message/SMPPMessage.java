/*    */ package com.huawei.insa2.comm.smpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.PMessage;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public abstract class SMPPMessage extends PMessage
/*    */   implements Cloneable
/*    */ {
/*    */   protected byte[] buf;
/*    */   protected int sequence_Id;
/*    */ 
/*    */   public Object clone()
/*    */   {
/*    */     SMPPMessage localSMPPMessage1;
/*    */     try
/*    */     {
/* 31 */       SMPPMessage m = (SMPPMessage)super.clone();
/* 32 */       m.buf = ((byte[])this.buf.clone());
/* 33 */       return m;
/*    */     } catch (CloneNotSupportedException ex) {
/* 35 */       ex.printStackTrace();
/* 36 */       localSMPPMessage1 = null; } return localSMPPMessage1;
/*    */   }
/*    */ 
/*    */   public abstract String toString();
/*    */ 
/*    */   public int getMsgLength()
/*    */   {
/* 47 */     int msgLength = TypeConvert.byte2int(this.buf, 0);
/* 48 */     return msgLength;
/*    */   }
/*    */ 
/*    */   public void setMsgLength(int msgLength)
/*    */   {
/* 53 */     TypeConvert.int2byte(msgLength, this.buf, 0);
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 58 */     int commandId = TypeConvert.byte2int(this.buf, 4);
/* 59 */     return commandId;
/*    */   }
/*    */ 
/*    */   public void setCommandId(int commandId)
/*    */   {
/* 64 */     TypeConvert.int2byte(commandId, this.buf, 4);
/*    */   }
/*    */ 
/*    */   public int getStatus()
/*    */   {
/* 69 */     int status = TypeConvert.byte2int(this.buf, 8);
/* 70 */     return status;
/*    */   }
/*    */ 
/*    */   public void setStatus(int status)
/*    */   {
/* 75 */     TypeConvert.int2byte(status, this.buf, 8);
/*    */   }
/*    */ 
/*    */   public int getSequenceId()
/*    */   {
/* 83 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 12);
/* 84 */     return this.sequence_Id;
/*    */   }
/*    */ 
/*    */   public void setSequenceId(int sequence_Id)
/*    */   {
/* 91 */     this.sequence_Id = sequence_Id;
/* 92 */     TypeConvert.int2byte(sequence_Id, this.buf, 12);
/*    */   }
/*    */ 
/*    */   public byte[] getBytes()
/*    */   {
/* 99 */     return this.buf;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.message.SMPPMessage
 * JD-Core Version:    0.5.3
 */