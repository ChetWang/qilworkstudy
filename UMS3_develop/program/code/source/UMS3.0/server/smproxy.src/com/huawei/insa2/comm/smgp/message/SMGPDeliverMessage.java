/*     */ package com.huawei.insa2.comm.smgp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SMGPDeliverMessage extends SMGPMessage
/*     */ {
/*     */   public SMGPDeliverMessage(byte[] buf)
/*     */     throws IllegalArgumentException
/*     */   {
/*  25 */     int len = 81 + (buf[72] & 0xFF);
/*  26 */     if (buf.length != len) {
/*  27 */       throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
/*     */     }
/*  29 */     this.buf = new byte[len];
/*  30 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/*  31 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*     */   }
/*     */ 
/*     */   public byte[] getMsgId()
/*     */   {
/*  38 */     byte[] msgId = new byte[10];
/*  39 */     System.arraycopy(this.buf, 4, msgId, 0, 10);
/*  40 */     return msgId;
/*     */   }
/*     */ 
/*     */   public int getIsReport()
/*     */   {
/*  47 */     return this.buf[14];
/*     */   }
/*     */ 
/*     */   public int getMsgFormat()
/*     */   {
/*  54 */     return this.buf[15];
/*     */   }
/*     */ 
/*     */   public Date getRecvTime()
/*     */   {
/*     */     String tmpstr;
/*     */     try
/*     */     {
/*  65 */       byte[] tmpbyte = new byte[4];
/*     */ 
/*  67 */       System.arraycopy(this.buf, 16, tmpbyte, 0, 4);
/*  68 */       tmpstr = new String(tmpbyte);
/*  69 */       int tmpYear = Integer.parseInt(tmpstr);
/*     */ 
/*  71 */       tmpbyte = new byte[2];
/*  72 */       System.arraycopy(this.buf, 20, tmpbyte, 0, 2);
/*  73 */       tmpstr = new String(tmpbyte);
/*  74 */       int tmpMonth = Integer.parseInt(tmpstr) - 1;
/*     */ 
/*  76 */       System.arraycopy(this.buf, 22, tmpbyte, 0, 2);
/*  77 */       tmpstr = new String(tmpbyte);
/*  78 */       int tmpDay = Integer.parseInt(tmpstr);
/*     */ 
/*  80 */       System.arraycopy(this.buf, 24, tmpbyte, 0, 2);
/*  81 */       tmpstr = new String(tmpbyte);
/*  82 */       int tmpHour = Integer.parseInt(tmpstr);
/*     */ 
/*  84 */       System.arraycopy(this.buf, 26, tmpbyte, 0, 2);
/*  85 */       tmpstr = new String(tmpbyte);
/*  86 */       int tmpMinute = Integer.parseInt(tmpstr);
/*     */ 
/*  88 */       System.arraycopy(this.buf, 28, tmpbyte, 0, 2);
/*  89 */       tmpstr = new String(tmpbyte);
/*  90 */       int tmpSecond = Integer.parseInt(tmpstr);
/*     */ 
/*  92 */       Calendar calendar = Calendar.getInstance();
/*  93 */       calendar.set(tmpYear, tmpMonth, tmpDay, tmpHour, tmpMinute, tmpSecond);
/*     */ 
/*  95 */       return calendar.getTime();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  99 */       tmpstr = null; } return tmpstr;
/*     */   }
/*     */ 
/*     */   public String getSrcTermID()
/*     */   {
/* 110 */     byte[] srcTermId = new byte[21];
/* 111 */     System.arraycopy(this.buf, 30, srcTermId, 0, 21);
/* 112 */     return new String(srcTermId).trim();
/*     */   }
/*     */ 
/*     */   public String getDestTermID()
/*     */   {
/* 121 */     byte[] destTermId = new byte[21];
/* 122 */     System.arraycopy(this.buf, 51, destTermId, 0, 21);
/* 123 */     return new String(destTermId).trim();
/*     */   }
/*     */ 
/*     */   public int getMsgLength()
/*     */   {
/* 128 */     return (this.buf[72] & 0xFF);
/*     */   }
/*     */ 
/*     */   public byte[] getMsgContent()
/*     */   {
/* 136 */     int len = this.buf[72] & 0xFF;
/* 137 */     byte[] content = new byte[len];
/* 138 */     System.arraycopy(this.buf, 73, content, 0, len);
/* 139 */     return content;
/*     */   }
/*     */ 
/*     */   public String getReserve()
/*     */   {
/* 147 */     int loc = 73 + (this.buf[72] & 0xFF);
/* 148 */     byte[] reserve = new byte[8];
/* 149 */     System.arraycopy(this.buf, loc, reserve, 0, 8);
/* 150 */     return new String(reserve).trim();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 158 */     StringBuffer strBuf = new StringBuffer(600);
/* 159 */     strBuf.append("SMGPDeliverMessage: ");
/* 160 */     strBuf.append("Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 161 */     strBuf.append(",MsgID=".concat(String.valueOf(String.valueOf(new String(getMsgId())))));
/* 162 */     strBuf.append(",IsReport=".concat(String.valueOf(String.valueOf(getIsReport()))));
/* 163 */     strBuf.append(",MsgFormat=".concat(String.valueOf(String.valueOf(getMsgFormat()))));
/* 164 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
/* 165 */     if (getRecvTime() != null)
/*     */     {
/* 167 */       strBuf.append(",RecvTime=".concat(String.valueOf(String.valueOf(dateFormat.format(getRecvTime())))));
/*     */     }
/*     */     else
/*     */     {
/* 171 */       strBuf.append(",RecvTime=null");
/*     */     }
/* 173 */     strBuf.append(",SrcTermID=".concat(String.valueOf(String.valueOf(getSrcTermID()))));
/* 174 */     strBuf.append(",DestTermID=".concat(String.valueOf(String.valueOf(getDestTermID()))));
/* 175 */     strBuf.append(",MsgLength=".concat(String.valueOf(String.valueOf(getMsgLength()))));
/* 176 */     strBuf.append(",MsgContent=".concat(String.valueOf(String.valueOf(new String(getMsgContent())))));
/* 177 */     strBuf.append(",reserve=".concat(String.valueOf(String.valueOf(getReserve()))));
/*     */ 
/* 179 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/* 186 */     return 3;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPDeliverMessage
 * JD-Core Version:    0.5.3
 */