/*     */ package com.huawei.insa2.comm.sgip.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.sgip.SGIPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ 
/*     */ public class SGIPDeliverMessage extends SGIPMessage
/*     */ {
/*     */   public SGIPDeliverMessage(byte[] buf)
/*     */     throws IllegalArgumentException
/*     */   {
/*  26 */     int len = TypeConvert.byte2int(buf, 57);
/*  27 */     len = 69 + len;
/*     */ 
/*  29 */     if (buf.length != len) {
/*  30 */       throw new IllegalArgumentException(SGIPConstant.SMC_MESSAGE_ERROR);
/*     */     }
/*  32 */     this.buf = new byte[len];
/*  33 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/*  34 */     this.src_node_Id = TypeConvert.byte2int(this.buf, 0);
/*  35 */     this.time_Stamp = TypeConvert.byte2int(this.buf, 4);
/*  36 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 8);
/*     */   }
/*     */ 
/*     */   public String getUserNumber()
/*     */   {
/*  43 */     byte[] tmpId = new byte[21];
/*  44 */     System.arraycopy(this.buf, 12, tmpId, 0, 21);
/*     */ 
/*  46 */     String tmpStr = new String(tmpId).trim();
/*  47 */     if (tmpStr.indexOf(0) >= 0) {
/*  48 */       return tmpStr.substring(0, tmpStr.indexOf(0));
/*     */     }
/*  50 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public String getSPNumber()
/*     */   {
/*  57 */     byte[] tmpId = new byte[21];
/*  58 */     System.arraycopy(this.buf, 33, tmpId, 0, 21);
/*     */ 
/*  60 */     String tmpStr = new String(tmpId).trim();
/*  61 */     if (tmpStr.indexOf(0) >= 0) {
/*  62 */       return tmpStr.substring(0, tmpStr.indexOf(0));
/*     */     }
/*  64 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getTpPid()
/*     */   {
/*  71 */     int tmpId = this.buf[54];
/*  72 */     return tmpId;
/*     */   }
/*     */ 
/*     */   public int getTpUdhi()
/*     */   {
/*  79 */     int tmpId = this.buf[55];
/*  80 */     return tmpId;
/*     */   }
/*     */ 
/*     */   public int getMsgFmt()
/*     */   {
/*  87 */     int tmpFmt = this.buf[56];
/*  88 */     return tmpFmt;
/*     */   }
/*     */ 
/*     */   public int getMsgLength()
/*     */   {
/*  95 */     return TypeConvert.byte2int(this.buf, 57);
/*     */   }
/*     */ 
/*     */   public byte[] getMsgContent()
/*     */   {
/* 102 */     int len = getMsgLength();
/* 103 */     byte[] tmpContent = new byte[len];
/* 104 */     System.arraycopy(this.buf, 61, tmpContent, 0, len);
/* 105 */     return tmpContent;
/*     */   }
/*     */ 
/*     */   public String getReserve()
/*     */   {
/* 112 */     int loc = 61 + getMsgLength();
/* 113 */     byte[] tmpReserve = new byte[8];
/* 114 */     System.arraycopy(this.buf, loc, tmpReserve, 0, 8);
/* 115 */     return new String(tmpReserve).trim();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 122 */     String tmpStr = "SGIP_DELIVER: ";
/* 123 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 124 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",UserNumber=").append(getUserNumber())));
/* 125 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SPNumber=").append(getSPNumber())));
/* 126 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",TpPid=").append(getTpPid())));
/* 127 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",TpUdhi=").append(getTpUdhi())));
/* 128 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgFmt=").append(getMsgFmt())));
/* 129 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgLength=").append(getMsgLength())));
/* 130 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgContent=").append(new String(getMsgContent()))));
/* 131 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Reserve=").append(getReserve())));
/* 132 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getCommandId()
/*     */   {
/* 139 */     return 4;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage
 * JD-Core Version:    0.5.3
 */