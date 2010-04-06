/*     */ package com.huawei.insa2.comm.cmpp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*     */ import com.huawei.insa2.util.SecurityTools;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class CMPPConnectMessage extends CMPPMessage
/*     */ {
/*     */   private String outStr;
/*     */ 
/*     */   public CMPPConnectMessage(String source_Addr, int version, String shared_Secret, Date timestamp)
/*     */     throws IllegalArgumentException
/*     */   {
/*  35 */     if (source_Addr == null) {
/*  36 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.CONNECT_INPUT_ERROR))).append(":source_Addr").append(CMPPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  39 */     if (source_Addr.length() > 6) {
/*  40 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.CONNECT_INPUT_ERROR))).append(":source_Addr").append(CMPPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/*  44 */     if ((version < 0) || (version > 255)) {
/*  45 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.CONNECT_INPUT_ERROR))).append(":version").append(CMPPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  50 */     int len = 39;
/*     */ 
/*  52 */     this.buf = new byte[len];
/*     */ 
/*  56 */     TypeConvert.int2byte(len, this.buf, 0);
/*     */ 
/*  58 */     TypeConvert.int2byte(1, this.buf, 4);
/*     */ 
/*  63 */     System.arraycopy(source_Addr.getBytes(), 0, this.buf, 12, source_Addr.length());
/*     */ 
/*  65 */     if (shared_Secret != null) {
/*  66 */       len = source_Addr.length() + 19 + shared_Secret.length();
/*     */     }
/*     */     else {
/*  69 */       len = source_Addr.length() + 19;
/*     */     }
/*  71 */     byte[] tmpbuf = new byte[len];
/*  72 */     int tmploc = 0;
/*  73 */     System.arraycopy(source_Addr.getBytes(), 0, tmpbuf, 0, source_Addr.length());
/*  74 */     tmploc = source_Addr.length() + 9;
/*  75 */     if (shared_Secret != null) {
/*  76 */       System.arraycopy(shared_Secret.getBytes(), 0, tmpbuf, tmploc, shared_Secret.length());
/*  77 */       tmploc += shared_Secret.length();
/*     */     }
/*  79 */     String tmptime = "0008080808";
/*  80 */     System.arraycopy(tmptime.getBytes(), 0, tmpbuf, tmploc, 10);
/*     */ 
/*  82 */     SecurityTools.md5(tmpbuf, 0, len, this.buf, 18);
/*     */ 
/*  85 */     this.buf[34] = (byte)version;
/*     */ 
/*  87 */     TypeConvert.int2byte(8080808, this.buf, 35);
/*     */ 
/*  90 */     this.outStr = ",source_Addr=".concat(String.valueOf(String.valueOf(source_Addr)));
/*  91 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",version=").append(version)));
/*  92 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",shared_Secret=").append(shared_Secret)));
/*  93 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",timeStamp=").append(tmptime)));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 101 */     String tmpStr = "CMPP_Connect: ";
/* 102 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 103 */     tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
/* 104 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getCommandId()
/*     */   {
/* 111 */     return 1;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPConnectMessage
 * JD-Core Version:    0.5.3
 */