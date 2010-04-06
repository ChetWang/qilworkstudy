/*     */ package com.huawei.insa2.comm.cmpp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class CMPPDeliverMessage extends CMPPMessage
/*     */ {
/*  18 */   private int deliverType = 0;
/*     */ 
/*     */   public CMPPDeliverMessage(byte[] buf)
/*     */     throws IllegalArgumentException
/*     */   {
/*  27 */     this.deliverType = buf[67];
/*  28 */     int len = 77 + (buf[68] & 0xFF);
/*  29 */     if (buf.length != len) {
/*  30 */       throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
/*     */     }
/*  32 */     this.buf = new byte[len];
/*  33 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/*  34 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*     */   }
/*     */ 
/*     */   public byte[] getMsgId()
/*     */   {
/*  41 */     byte[] tmpMsgId = new byte[8];
/*  42 */     System.arraycopy(this.buf, 4, tmpMsgId, 0, 8);
/*  43 */     return tmpMsgId;
/*     */   }
/*     */ 
/*     */   public String getDestnationId()
/*     */   {
/*  50 */     byte[] tmpId = new byte[21];
/*  51 */     System.arraycopy(this.buf, 12, tmpId, 0, 21);
/*  52 */     return new String(tmpId).trim();
/*     */   }
/*     */ 
/*     */   public String getServiceId()
/*     */   {
/*  59 */     byte[] tmpId = new byte[10];
/*  60 */     System.arraycopy(this.buf, 33, tmpId, 0, 10);
/*  61 */     return new String(tmpId).trim();
/*     */   }
/*     */ 
/*     */   public int getTpPid()
/*     */   {
/*  68 */     int tmpId = this.buf[43];
/*  69 */     return tmpId;
/*     */   }
/*     */ 
/*     */   public int getTpUdhi()
/*     */   {
/*  76 */     int tmpId = this.buf[44];
/*  77 */     return tmpId;
/*     */   }
/*     */ 
/*     */   public int getMsgFmt()
/*     */   {
/*  84 */     int tmpFmt = this.buf[45];
/*  85 */     return tmpFmt;
/*     */   }
/*     */ 
/*     */   public String getSrcterminalId()
/*     */   {
/*  92 */     byte[] tmpId = new byte[21];
/*  93 */     System.arraycopy(this.buf, 46, tmpId, 0, 21);
/*  94 */     return new String(tmpId).trim();
/*     */   }
/*     */ 
/*     */   public int getRegisteredDeliver()
/*     */   {
/* 101 */     return this.buf[67];
/*     */   }
/*     */ 
/*     */   public int getMsgLength()
/*     */   {
/* 108 */     return (this.buf[68] & 0xFF);
/*     */   }
/*     */ 
/*     */   public byte[] getMsgContent()
/*     */   {
/* 115 */     if (this.deliverType == 0) {
/* 116 */       int len = this.buf[68] & 0xFF;
/* 117 */       byte[] tmpContent = new byte[len];
/* 118 */       System.arraycopy(this.buf, 69, tmpContent, 0, len);
/* 119 */       return tmpContent;
/*     */     }
/*     */ 
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   public String getReserve()
/*     */   {
/* 130 */     int loc = 69 + (this.buf[68] & 0xFF);
/* 131 */     byte[] tmpReserve = new byte[8];
/* 132 */     System.arraycopy(this.buf, loc, tmpReserve, 0, 8);
/* 133 */     return new String(tmpReserve).trim();
/*     */   }
/*     */ 
/*     */   public byte[] getStatusMsgId()
/*     */   {
/* 141 */     if (this.deliverType == 1) {
/* 142 */       byte[] tmpId = new byte[8];
/* 143 */       System.arraycopy(this.buf, 69, tmpId, 0, 8);
/* 144 */       return tmpId;
/*     */     }
/*     */ 
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   public String getStat()
/*     */   {
/* 155 */     if (this.deliverType == 1) {
/* 156 */       byte[] tmpStat = new byte[7];
/* 157 */       System.arraycopy(this.buf, 77, tmpStat, 0, 7);
/* 158 */       return new String(tmpStat).trim();
/*     */     }
/*     */ 
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   public Date getSubmitTime()
/*     */   {
/* 169 */     if (this.deliverType == 1) {
/* 170 */       byte[] tmpbyte = new byte[2];
/*     */ 
/* 172 */       System.arraycopy(this.buf, 84, tmpbyte, 0, 2);
/* 173 */       String tmpstr = new String(tmpbyte);
/* 174 */       int tmpYear = 2000 + Integer.parseInt(tmpstr);
/*     */ 
/* 176 */       System.arraycopy(this.buf, 86, tmpbyte, 0, 2);
/* 177 */       tmpstr = new String(tmpbyte);
/* 178 */       int tmpMonth = Integer.parseInt(tmpstr) - 1;
/*     */ 
/* 180 */       System.arraycopy(this.buf, 88, tmpbyte, 0, 2);
/* 181 */       tmpstr = new String(tmpbyte);
/* 182 */       int tmpDay = Integer.parseInt(tmpstr);
/*     */ 
/* 184 */       System.arraycopy(this.buf, 90, tmpbyte, 0, 2);
/* 185 */       tmpstr = new String(tmpbyte);
/* 186 */       int tmpHour = Integer.parseInt(tmpstr);
/*     */ 
/* 188 */       System.arraycopy(this.buf, 92, tmpbyte, 0, 2);
/* 189 */       tmpstr = new String(tmpbyte);
/* 190 */       int tmpMinute = Integer.parseInt(tmpstr);
/*     */ 
/* 192 */       Calendar calendar = Calendar.getInstance();
/* 193 */       calendar.set(tmpYear, tmpMonth, tmpDay, tmpHour, tmpMinute);
/*     */ 
/* 195 */       return calendar.getTime();
/*     */     }
/*     */ 
/* 198 */     return null;
/*     */   }
/*     */ 
/*     */   public Date getDoneTime()
/*     */   {
/* 206 */     if (this.deliverType == 1) {
/* 207 */       byte[] tmpbyte = new byte[2];
/*     */ 
/* 209 */       System.arraycopy(this.buf, 94, tmpbyte, 0, 2);
/* 210 */       String tmpstr = new String(tmpbyte);
/* 211 */       int tmpYear = 2000 + Integer.parseInt(tmpstr);
/*     */ 
/* 213 */       System.arraycopy(this.buf, 96, tmpbyte, 0, 2);
/* 214 */       tmpstr = new String(tmpbyte);
/* 215 */       int tmpMonth = Integer.parseInt(tmpstr) - 1;
/*     */ 
/* 217 */       System.arraycopy(this.buf, 98, tmpbyte, 0, 2);
/* 218 */       tmpstr = new String(tmpbyte);
/* 219 */       int tmpDay = Integer.parseInt(tmpstr);
/*     */ 
/* 221 */       System.arraycopy(this.buf, 100, tmpbyte, 0, 2);
/* 222 */       tmpstr = new String(tmpbyte);
/* 223 */       int tmpHour = Integer.parseInt(tmpstr);
/*     */ 
/* 225 */       System.arraycopy(this.buf, 102, tmpbyte, 0, 2);
/* 226 */       tmpstr = new String(tmpbyte);
/* 227 */       int tmpMinute = Integer.parseInt(tmpstr);
/*     */ 
/* 229 */       Calendar calendar = Calendar.getInstance();
/* 230 */       calendar.set(tmpYear, tmpMonth, tmpDay, tmpHour, tmpMinute);
/* 231 */       return calendar.getTime();
/*     */     }
/*     */ 
/* 234 */     return null;
/*     */   }
/*     */ 
/*     */   public String getDestTerminalId()
/*     */   {
/* 242 */     if (this.deliverType == 1) {
/* 243 */       byte[] tmpId = new byte[21];
/* 244 */       System.arraycopy(this.buf, 104, tmpId, 0, 21);
/* 245 */       return new String(tmpId);
/*     */     }
/*     */ 
/* 248 */     return null;
/*     */   }
/*     */ 
/*     */   public int getSMSCSequence()
/*     */   {
/* 256 */     if (this.deliverType == 1) {
/* 257 */       int tmpSequence = TypeConvert.byte2int(this.buf, 125);
/* 258 */       return tmpSequence;
/*     */     }
/*     */ 
/* 261 */     return -1;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 269 */     String tmpStr = "CMPP_Deliver: ";
/* 270 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 271 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgId=").append(new String(getMsgId()))));
/* 272 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",DestnationId=").append(getDestnationId())));
/* 273 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",ServiceId=").append(getServiceId())));
/* 274 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",TpPid=").append(getTpPid())));
/* 275 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",TpUdhi=").append(getTpUdhi())));
/* 276 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgFmt=").append(getMsgFmt())));
/* 277 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SrcterminalId=").append(getSrcterminalId())));
/* 278 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",RegisteredDeliver=").append(getRegisteredDeliver())));
/* 279 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgLength=").append(getMsgLength())));
/* 280 */     if (getRegisteredDeliver() == 1) {
/* 281 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Stat=").append(getStat())));
/* 282 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SubmitTime=").append(getSubmitTime())));
/* 283 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",DoneTime=").append(getDoneTime())));
/* 284 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",DestTerminalId=").append(getDestTerminalId())));
/* 285 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SMSCSequence=").append(getSMSCSequence())));
/*     */     }
/*     */     else {
/* 288 */       tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgContent=").append(getMsgContent())));
/*     */     }
/*     */ 
/* 291 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Reserve=").append(getReserve())));
/* 292 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getCommandId()
/*     */   {
/* 299 */     return 5;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPDeliverMessage
 * JD-Core Version:    0.5.3
 */