/*     */ package com.huawei.insa2.comm.sgip.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.sgip.SGIPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ 
/*     */ public class SGIPReportMessage extends SGIPMessage
/*     */ {
/*     */   public SGIPReportMessage(byte[] buf)
/*     */     throws IllegalArgumentException
/*     */   {
/*  23 */     this.buf = new byte[56];
/*  24 */     if (buf.length != 56) {
/*  25 */       throw new IllegalArgumentException(SGIPConstant.SMC_MESSAGE_ERROR);
/*     */     }
/*  27 */     System.arraycopy(buf, 0, this.buf, 0, 56);
/*     */ 
/*  29 */     this.src_node_Id = TypeConvert.byte2int(this.buf, 0);
/*  30 */     this.time_Stamp = TypeConvert.byte2int(this.buf, 4);
/*  31 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 8);
/*     */   }
/*     */ 
/*     */   public int[] getSubmitSequenceNumber()
/*     */   {
/*  38 */     int[] temp = new int[3];
/*  39 */     System.arraycopy(this.buf, 12, temp, 0, 12);
/*  40 */     return temp;
/*     */   }
/*     */ 
/*     */   public int getReportType()
/*     */   {
/*  47 */     int tmpId = this.buf[24];
/*  48 */     return tmpId;
/*     */   }
/*     */ 
/*     */   public String getUserNumber()
/*     */   {
/*  55 */     byte[] tmpId = new byte[21];
/*  56 */     System.arraycopy(this.buf, 25, tmpId, 0, 21);
/*     */ 
/*  58 */     String tmpStr = new String(tmpId).trim();
/*  59 */     if (tmpStr.indexOf(0) >= 0) {
/*  60 */       return tmpStr.substring(0, tmpStr.indexOf(0));
/*     */     }
/*  62 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/*  69 */     int tmpId = this.buf[46];
/*  70 */     return tmpId;
/*     */   }
/*     */ 
/*     */   public int getErrorCode()
/*     */   {
/*  77 */     int tmpId = this.buf[47];
/*  78 */     return tmpId;
/*     */   }
/*     */ 
/*     */   public String getReserve()
/*     */   {
/*  85 */     byte[] tmpReserve = new byte[8];
/*  86 */     System.arraycopy(this.buf, 48, tmpReserve, 0, 8);
/*  87 */     return new String(tmpReserve).trim();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  94 */     String tmpStr = "SGIP_REPORT: ";
/*  95 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/*  96 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SubmitSequenceNumber=").append(getSubmitSequenceNumber())));
/*  97 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",ReportType=").append(getReportType())));
/*  98 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",UserNumber=").append(getUserNumber())));
/*  99 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",State=").append(getState())));
/* 100 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",ErrorCode=").append(getErrorCode())));
/* 101 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Reserve=").append(getReserve())));
/* 102 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getCommandId()
/*     */   {
/* 109 */     return 5;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.message.SGIPReportMessage
 * JD-Core Version:    0.5.3
 */