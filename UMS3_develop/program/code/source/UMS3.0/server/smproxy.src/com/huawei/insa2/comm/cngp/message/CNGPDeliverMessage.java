/*     */ package com.huawei.insa2.comm.cngp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.cngp.CNGPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class CNGPDeliverMessage extends CNGPMessage
/*     */ {
/*     */   public CNGPDeliverMessage(byte[] buf)
/*     */     throws IllegalArgumentException
/*     */   {
/*  25 */     int len = 90 + (buf[84] & 0xFF);
/*  26 */     if (buf.length != len) {
/*  27 */       throw new IllegalArgumentException(CNGPConstant.SMC_MESSAGE_ERROR);
/*     */     }
/*  29 */     this.buf = new byte[len];
/*  30 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/*     */   }
/*     */ 
/*     */   public byte[] getMsgId()
/*     */   {
/*  37 */     byte[] msgId = new byte[10];
/*  38 */     System.arraycopy(this.buf, 16, msgId, 0, 10);
/*  39 */     return msgId;
/*     */   }
/*     */ 
/*     */   public int getIsReport()
/*     */   {
/*  46 */     return this.buf[26];
/*     */   }
/*     */ 
/*     */   public int getMsgFormat()
/*     */   {
/*  53 */     return this.buf[27];
/*     */   }
/*     */ 
/*     */   public Date getRecvTime()
/*     */   {
/*     */     byte[] tmpbyte;
/*     */     try
/*     */     {
/*  65 */       int tmpYear = TypeConvert.byte2int(this.buf, 27);
/*     */ 
/*  67 */       tmpbyte = new byte[2];
/*  68 */       System.arraycopy(this.buf, 31, tmpbyte, 0, 2);
/*  69 */       String tmpstr = new String(tmpbyte);
/*  70 */       int tmpMonth = Integer.parseInt(tmpstr);
/*     */ 
/*  72 */       System.arraycopy(this.buf, 33, tmpbyte, 0, 2);
/*  73 */       tmpstr = new String(tmpbyte);
/*  74 */       int tmpDay = Integer.parseInt(tmpstr);
/*     */ 
/*  76 */       System.arraycopy(this.buf, 35, tmpbyte, 0, 2);
/*  77 */       tmpstr = new String(tmpbyte);
/*  78 */       int tmpHour = Integer.parseInt(tmpstr);
/*     */ 
/*  80 */       System.arraycopy(this.buf, 37, tmpbyte, 0, 2);
/*  81 */       tmpstr = new String(tmpbyte);
/*  82 */       int tmpMinute = Integer.parseInt(tmpstr);
/*     */ 
/*  84 */       System.arraycopy(this.buf, 39, tmpbyte, 0, 2);
/*  85 */       tmpstr = new String(tmpbyte);
/*  86 */       int tmpSecond = Integer.parseInt(tmpstr);
/*     */ 
/*  88 */       Calendar calendar = Calendar.getInstance();
/*  89 */       calendar.set(tmpYear, tmpMonth, tmpDay, tmpHour, tmpMinute, tmpSecond);
/*     */ 
/*  91 */       return calendar.getTime();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  95 */       tmpbyte = null; } return tmpbyte;
/*     */   }
/*     */ 
/*     */   public String getSrcTermID()
/*     */   {
/* 106 */     byte[] srcTermId = new byte[21];
/* 107 */     System.arraycopy(this.buf, 41, srcTermId, 0, 21);
/* 108 */     return new String(srcTermId).trim();
/*     */   }
/*     */ 
/*     */   public String getDestTermID()
/*     */   {
/* 117 */     byte[] destTermId = new byte[21];
/* 118 */     System.arraycopy(this.buf, 62, destTermId, 0, 21);
/* 119 */     return new String(destTermId).trim();
/*     */   }
/*     */ 
/*     */   public int getMsgLength()
/*     */   {
/* 124 */     return (this.buf[84] & 0xFF);
/*     */   }
/*     */ 
/*     */   public String getMsgContent()
/*     */   {
/* 132 */     int len = this.buf[84] & 0xFF;
/* 133 */     byte[] content = new byte[len];
/* 134 */     System.arraycopy(this.buf, 85, content, 0, len);
/* 135 */     return new String(content).trim();
/*     */   }
/*     */ 
/*     */   public int getCongestionState()
/*     */   {
/* 144 */     int pos = 89 + (this.buf[84] & 0xFF);
/* 145 */     return this.buf[pos];
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 154 */     StringBuffer strBuf = new StringBuffer(600);
/* 155 */     strBuf.append("CNGPDeliverMessage: ");
/* 156 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(getMsgLength()))));
/* 157 */     strBuf.append(",RequestID=".concat(String.valueOf(String.valueOf(super.getRequestId()))));
/* 158 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 159 */     strBuf.append(",Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 160 */     strBuf.append(",MsgID=".concat(String.valueOf(String.valueOf(new String(getMsgId())))));
/* 161 */     strBuf.append(",IsReport=".concat(String.valueOf(String.valueOf(getIsReport()))));
/* 162 */     strBuf.append(",MsgFormat=".concat(String.valueOf(String.valueOf(getMsgFormat()))));
/* 163 */     strBuf.append(",RecvTime=".concat(String.valueOf(String.valueOf(getRecvTime()))));
/* 164 */     strBuf.append(",SrcTermID=".concat(String.valueOf(String.valueOf(getSrcTermID()))));
/* 165 */     strBuf.append(",DestTermID=".concat(String.valueOf(String.valueOf(getDestTermID()))));
/* 166 */     strBuf.append(",MsgLength=".concat(String.valueOf(String.valueOf(getMsgLength()))));
/* 167 */     strBuf.append(",MsgContent=".concat(String.valueOf(String.valueOf(new String(getMsgContent())))));
/* 168 */     strBuf.append(",CongestionState=".concat(String.valueOf(String.valueOf(getCongestionState()))));
/* 169 */     return strBuf.toString();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.message.CNGPDeliverMessage
 * JD-Core Version:    0.5.3
 */