/*     */ package com.huawei.insa2.comm.smgp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SMGPQueryMessage extends SMGPMessage
/*     */ {
/*     */   private StringBuffer strBuf;
/*     */ 
/*     */   public SMGPQueryMessage(Date time, int queryType, String queryCode)
/*     */     throws IllegalArgumentException
/*     */   {
/*  33 */     if (time == null) {
/*  34 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.QUERY_INPUT_ERROR))).append(":Time ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  38 */     if ((queryType < 0) || (queryType > 255)) {
/*  39 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.QUERY_INPUT_ERROR))).append(":QueryType ").append(SMGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  43 */     if ((queryCode != null) && 
/*  45 */       (queryCode.length() > 10)) {
/*  46 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.QUERY_INPUT_ERROR))).append(":QueryCode ").append(SMGPConstant.STRING_LENGTH_GREAT).append("10"))));
/*     */     }
/*     */ 
/*  52 */     int len = 31;
/*     */ 
/*  54 */     this.buf = new byte[len];
/*     */ 
/*  58 */     TypeConvert.int2byte(len, this.buf, 0);
/*     */ 
/*  60 */     TypeConvert.int2byte(7, this.buf, 4);
/*     */ 
/*  65 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
/*     */ 
/*  67 */     System.arraycopy(dateFormat.format(time).getBytes(), 0, this.buf, 12, dateFormat.format(time).length());
/*     */ 
/*  70 */     this.buf[20] = (byte)queryType;
/*     */ 
/*  72 */     if (queryCode != null)
/*     */     {
/*  74 */       System.arraycopy(queryCode.getBytes(), 0, this.buf, 21, queryCode.length());
/*     */     }
/*     */ 
/*  78 */     this.strBuf = new StringBuffer(200);
/*  79 */     this.strBuf.append(",time=".concat(String.valueOf(String.valueOf(time))));
/*  80 */     this.strBuf.append(",Query_Type=".concat(String.valueOf(String.valueOf(queryType))));
/*  81 */     this.strBuf.append(",Query_Code=".concat(String.valueOf(String.valueOf(queryCode))));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  89 */     StringBuffer outStr = new StringBuffer(200);
/*  90 */     outStr.append("SMGPQueryMessage:");
/*  91 */     outStr.append("PacketLength=".concat(String.valueOf(String.valueOf(this.buf.length))));
/*  92 */     outStr.append(",RequestID=7");
/*  93 */     outStr.append(",Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/*  94 */     if (this.strBuf != null)
/*     */     {
/*  96 */       outStr.append(this.strBuf.toString());
/*     */     }
/*  98 */     return outStr.toString();
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/* 105 */     return 7;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPQueryMessage
 * JD-Core Version:    0.5.3
 */