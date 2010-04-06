/*     */ package com.huawei.insa2.comm.cmpp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ 
/*     */ public class CMPPQueryRepMessage extends CMPPMessage
/*     */ {
/*     */   public CMPPQueryRepMessage(byte[] buf)
/*     */     throws IllegalArgumentException
/*     */   {
/*  23 */     this.buf = new byte[55];
/*  24 */     if (buf.length != 55) {
/*  25 */       throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
/*     */     }
/*  27 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/*  28 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
/*     */   }
/*     */ 
/*     */   public java.util.Date getDate()
/*     */   {
/*  37 */     byte[] tmpstr = new byte[4];
/*  38 */     System.arraycopy(this.buf, 4, tmpstr, 0, 4);
/*  39 */     String tmpYear = new String(tmpstr);
/*     */ 
/*  41 */     byte[] tmpstr1 = new byte[2];
/*  42 */     System.arraycopy(this.buf, 8, tmpstr1, 0, 2);
/*  43 */     String tmpMonth = new String(tmpstr1);
/*     */ 
/*  45 */     System.arraycopy(this.buf, 10, tmpstr1, 0, 2);
/*  46 */     String tmpDay = new String(tmpstr1);
/*     */ 
/*  48 */     java.util.Date date = java.sql.Date.valueOf(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpYear))).append("-").append(tmpMonth).append("-").append(tmpDay))));
/*  49 */     return date;
/*     */   }
/*     */ 
/*     */   public int getQueryType()
/*     */   {
/*  56 */     return this.buf[12];
/*     */   }
/*     */ 
/*     */   public String getQueryCode()
/*     */   {
/*  64 */     byte[] tmpQC = new byte[10];
/*  65 */     System.arraycopy(this.buf, 13, tmpQC, 0, 10);
/*  66 */     return new String(tmpQC).trim();
/*     */   }
/*     */ 
/*     */   public int getMtTlmsg()
/*     */   {
/*  73 */     return TypeConvert.byte2int(this.buf, 23);
/*     */   }
/*     */ 
/*     */   public int getMtTlusr()
/*     */   {
/*  80 */     return TypeConvert.byte2int(this.buf, 27);
/*     */   }
/*     */ 
/*     */   public int getMtScs()
/*     */   {
/*  87 */     return TypeConvert.byte2int(this.buf, 31);
/*     */   }
/*     */ 
/*     */   public int getMtWt()
/*     */   {
/*  94 */     return TypeConvert.byte2int(this.buf, 35);
/*     */   }
/*     */ 
/*     */   public int getMtFl()
/*     */   {
/* 101 */     return TypeConvert.byte2int(this.buf, 39);
/*     */   }
/*     */ 
/*     */   public int getMoScs()
/*     */   {
/* 108 */     return TypeConvert.byte2int(this.buf, 43);
/*     */   }
/*     */ 
/*     */   public int getMoWt()
/*     */   {
/* 115 */     return TypeConvert.byte2int(this.buf, 47);
/*     */   }
/*     */ 
/*     */   public int getMoFl()
/*     */   {
/* 122 */     return TypeConvert.byte2int(this.buf, 51);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 129 */     String tmpStr = "CMPP_Query_REP: ";
/*     */ 
/* 131 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
/*     */ 
/* 133 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 134 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Time=").append(dateFormat.format(getDate()))));
/* 135 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",AuthenticatorISMG=").append(getQueryType())));
/* 136 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",QueryCode=").append(getQueryCode())));
/* 137 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MtTlmsg=").append(getMtTlmsg())));
/* 138 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MtTlusr=").append(getMtTlusr())));
/* 139 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MtScs=").append(getMtScs())));
/* 140 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MtWt=").append(getMtWt())));
/* 141 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MtFl=").append(getMtFl())));
/* 142 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MoScs=").append(getMoScs())));
/* 143 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MoWt=").append(getMoWt())));
/* 144 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MoFl=").append(getMoFl())));
/* 145 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getCommandId()
/*     */   {
/* 153 */     return -2147483642;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPQueryRepMessage
 * JD-Core Version:    0.5.3
 */