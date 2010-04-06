/*     */ package com.huawei.insa2.comm.cmpp30.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class CMPP30DeliverMessage extends CMPPMessage
/*     */ {
/*  19 */   private int deliverType = 0;
/*     */ 
/*     */   public CMPP30DeliverMessage(byte[] buf)
/*     */     throws IllegalArgumentException
/*     */   {
/*  30 */     this.deliverType = buf[79];
/*     */ 
/*  36 */     int len = 101 + (buf[80] & 0xFF);
/*     */ 
/*  39 */     if (buf.length != len) {
/*  40 */       throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
/*     */     }
/*  42 */     this.buf = new byte[len];
/*  43 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/*  44 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*     */   }
/*     */ 
/*     */   public byte[] getMsgId()
/*     */   {
/*  51 */     byte[] tmpMsgId = new byte[8];
/*  52 */     System.arraycopy(this.buf, 4, tmpMsgId, 0, 8);
/*  53 */     return tmpMsgId;
/*     */   }
/*     */ 
/*     */   public String getDestnationId()
/*     */   {
/*  60 */     byte[] tmpId = new byte[21];
/*  61 */     System.arraycopy(this.buf, 12, tmpId, 0, 21);
/*  62 */     return new String(tmpId).trim();
/*     */   }
/*     */ 
/*     */   public String getServiceId()
/*     */   {
/*  69 */     byte[] tmpId = new byte[10];
/*  70 */     System.arraycopy(this.buf, 33, tmpId, 0, 10);
/*  71 */     return new String(tmpId).trim();
/*     */   }
/*     */ 
/*     */   public int getTpPid()
/*     */   {
/*  78 */     int tmpId = this.buf[43];
/*  79 */     return tmpId;
/*     */   }
/*     */ 
/*     */   public int getTpUdhi()
/*     */   {
/*  86 */     int tmpId = this.buf[44];
/*  87 */     return tmpId;
/*     */   }
/*     */ 
/*     */   public int getMsgFmt()
/*     */   {
/*  94 */     int tmpFmt = this.buf[45];
/*  95 */     return tmpFmt;
/*     */   }
/*     */ 
/*     */   public String getSrcterminalId()
/*     */   {
/* 107 */     byte[] tmpId = new byte[32];
/* 108 */     System.arraycopy(this.buf, 46, tmpId, 0, 32);
/* 109 */     return new String(tmpId).trim();
/*     */   }
/*     */ 
/*     */   public int getSrcterminalType()
/*     */   {
/* 116 */     int tmpFmt = this.buf[78];
/* 117 */     return tmpFmt;
/*     */   }
/*     */ 
/*     */   public int getRegisteredDeliver()
/*     */   {
/* 127 */     return this.buf[79];
/*     */   }
/*     */ 
/*     */   public int getMsgLength()
/*     */   {
/* 137 */     return (this.buf[80] & 0xFF);
/*     */   }
/*     */ 
/*     */   public byte[] getMsgContent()
/*     */   {
/* 145 */     if (this.deliverType == 0)
/*     */     {
/* 151 */       int len = getMsgLength();
/* 152 */       byte[] tmpContent = new byte[len];
/* 153 */       System.arraycopy(this.buf, 81, tmpContent, 0, len);
/*     */ 
/* 155 */       return tmpContent;
/*     */     }
/*     */ 
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   public String getLinkID()
/*     */   {
/* 180 */     int loc = 81 + getMsgLength();
/* 181 */     byte[] tmpReserve = new byte[20];
/* 182 */     System.arraycopy(this.buf, loc, tmpReserve, 0, 20);
/* 183 */     return new String(tmpReserve).trim();
/*     */   }
/*     */ 
/*     */   public byte[] getStatusMsgId()
/*     */   {
/* 191 */     if (this.deliverType == 1) {
/* 192 */       byte[] tmpId = new byte[8];
/*     */ 
/* 196 */       System.arraycopy(this.buf, 81, tmpId, 0, 8);
/*     */ 
/* 198 */       return tmpId;
/*     */     }
/*     */ 
/* 201 */     return null;
/*     */   }
/*     */ 
/*     */   public String getStat()
/*     */   {
/* 209 */     if (this.deliverType == 1) {
/* 210 */       byte[] tmpStat = new byte[7];
/*     */ 
/* 214 */       System.arraycopy(this.buf, 89, tmpStat, 0, 7);
/*     */ 
/* 216 */       return new String(tmpStat).trim();
/*     */     }
/*     */ 
/* 219 */     return null;
/*     */   }
/*     */ 
/*     */   public Date getSubmitTime()
/*     */   {
/* 227 */     if (this.deliverType == 1) {
/* 228 */       byte[] tmpbyte = new byte[2];
/*     */ 
/* 252 */       System.arraycopy(this.buf, 96, tmpbyte, 0, 2);
/* 253 */       String tmpstr = new String(tmpbyte);
/* 254 */       int tmpYear = 2000 + Integer.parseInt(tmpstr);
/*     */ 
/* 256 */       System.arraycopy(this.buf, 98, tmpbyte, 0, 2);
/* 257 */       tmpstr = new String(tmpbyte);
/* 258 */       int tmpMonth = Integer.parseInt(tmpstr) - 1;
/*     */ 
/* 260 */       System.arraycopy(this.buf, 100, tmpbyte, 0, 2);
/* 261 */       tmpstr = new String(tmpbyte);
/* 262 */       int tmpDay = Integer.parseInt(tmpstr);
/*     */ 
/* 264 */       System.arraycopy(this.buf, 102, tmpbyte, 0, 2);
/* 265 */       tmpstr = new String(tmpbyte);
/* 266 */       int tmpHour = Integer.parseInt(tmpstr);
/*     */ 
/* 268 */       System.arraycopy(this.buf, 104, tmpbyte, 0, 2);
/* 269 */       tmpstr = new String(tmpbyte);
/* 270 */       int tmpMinute = Integer.parseInt(tmpstr);
/*     */ 
/* 274 */       Calendar calendar = Calendar.getInstance();
/* 275 */       calendar.set(tmpYear, tmpMonth, tmpDay, tmpHour, tmpMinute);
/*     */ 
/* 277 */       return calendar.getTime();
/*     */     }
/*     */ 
/* 280 */     return null;
/*     */   }
/*     */ 
/*     */   public Date getDoneTime()
/*     */   {
/* 288 */     if (this.deliverType == 1) {
/* 289 */       byte[] tmpbyte = new byte[2];
/*     */ 
/* 313 */       System.arraycopy(this.buf, 106, tmpbyte, 0, 2);
/* 314 */       String tmpstr = new String(tmpbyte);
/* 315 */       int tmpYear = 2000 + Integer.parseInt(tmpstr);
/*     */ 
/* 317 */       System.arraycopy(this.buf, 108, tmpbyte, 0, 2);
/* 318 */       tmpstr = new String(tmpbyte);
/* 319 */       int tmpMonth = Integer.parseInt(tmpstr) - 1;
/*     */ 
/* 321 */       System.arraycopy(this.buf, 110, tmpbyte, 0, 2);
/* 322 */       tmpstr = new String(tmpbyte);
/* 323 */       int tmpDay = Integer.parseInt(tmpstr);
/*     */ 
/* 325 */       System.arraycopy(this.buf, 112, tmpbyte, 0, 2);
/* 326 */       tmpstr = new String(tmpbyte);
/* 327 */       int tmpHour = Integer.parseInt(tmpstr);
/*     */ 
/* 329 */       System.arraycopy(this.buf, 114, tmpbyte, 0, 2);
/* 330 */       tmpstr = new String(tmpbyte);
/* 331 */       int tmpMinute = Integer.parseInt(tmpstr);
/*     */ 
/* 335 */       Calendar calendar = Calendar.getInstance();
/* 336 */       calendar.set(tmpYear, tmpMonth, tmpDay, tmpHour, tmpMinute);
/* 337 */       return calendar.getTime();
/*     */     }
/*     */ 
/* 340 */     return null;
/*     */   }
/*     */ 
/*     */   public String getDestTerminalId()
/*     */   {
/* 348 */     if (this.deliverType == 1) {
/* 349 */       byte[] tmpId = new byte[32];
/*     */ 
/* 353 */       System.arraycopy(this.buf, 116, tmpId, 0, 32);
/*     */ 
/* 355 */       return new String(tmpId).trim();
/*     */     }
/*     */ 
/* 358 */     return null;
/*     */   }
/*     */ 
/*     */   public int getSMSCSequence()
/*     */   {
/* 366 */     if (this.deliverType == 1)
/*     */     {
/* 370 */       int tmpSequence = TypeConvert.byte2int(this.buf, 148);
/*     */ 
/* 372 */       return tmpSequence;
/*     */     }
/*     */ 
/* 375 */     return -1;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 383 */     String tmpStr = "CMPP_Deliver: ";
/* 384 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 385 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgId=").append(new String(getMsgId()))));
/* 386 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",DestnationId=").append(getDestnationId())));
/* 387 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",ServiceId=").append(getServiceId())));
/* 388 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",TpPid=").append(getTpPid())));
/* 389 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",TpUdhi=").append(getTpUdhi())));
/* 390 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgFmt=").append(getMsgFmt())));
/* 391 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SrcterminalId=").append(getSrcterminalId())));
/*     */ 
/* 393 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SrcterminalType=").append(getSrcterminalType())));
/*     */ 
/* 395 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",RegisteredDeliver=").append(getRegisteredDeliver())));
/* 396 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgLength=").append(getMsgLength())));
/* 397 */     if (getRegisteredDeliver() == 1) {
/* 398 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Stat=").append(getStat())));
/* 399 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SubmitTime=").append(getSubmitTime())));
/* 400 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",DoneTime=").append(getDoneTime())));
/* 401 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",DestTerminalId=").append(getDestTerminalId())));
/* 402 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SMSCSequence=").append(getSMSCSequence())));
/*     */     }
/*     */     else {
/* 405 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgContent=").append(getMsgContent())));
/*     */     }
/*     */ 
/* 411 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",LinkID=").append(getLinkID())));
/*     */ 
/* 413 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getCommandId()
/*     */   {
/* 420 */     return 5;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp30.message.CMPP30DeliverMessage
 * JD-Core Version:    0.5.3
 */