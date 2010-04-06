/*     */ package com.huawei.insa2.comm.cngp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.cngp.CNGPConstant;
/*     */ import com.huawei.insa2.util.SecurityTools;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class CNGPLoginMessage extends CNGPMessage
/*     */ {
/*     */   private StringBuffer strBuf;
/*     */ 
/*     */   public CNGPLoginMessage(String clientId, String shared_Secret, int loginMode, Date timeStamp, int version)
/*     */     throws IllegalArgumentException
/*     */   {
/*  36 */     if (clientId == null) {
/*  37 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.CONNECT_INPUT_ERROR))).append(":clientId ").append(CNGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  40 */     if (clientId.length() > 10) {
/*  41 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.CONNECT_INPUT_ERROR))).append(":clientId ").append(CNGPConstant.STRING_LENGTH_GREAT).append("8"))));
/*     */     }
/*     */ 
/*  44 */     if (shared_Secret.length() > 15) {
/*  45 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.CONNECT_INPUT_ERROR))).append(":shared_Secret ").append(CNGPConstant.STRING_LENGTH_GREAT).append("8"))));
/*     */     }
/*     */ 
/*  49 */     if ((loginMode < 0) || (loginMode > 255)) {
/*  50 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.CONNECT_INPUT_ERROR))).append(":loginMode ").append(CNGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  54 */     if ((version < 0) || (version > 255)) {
/*  55 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.CONNECT_INPUT_ERROR))).append(":version ").append(CNGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  60 */     int len = 48;
/*     */ 
/*  62 */     this.buf = new byte[len];
/*     */ 
/*  66 */     super.setMsgLength(len);
/*     */ 
/*  68 */     super.setRequestId(1);
/*     */ 
/*  74 */     System.arraycopy(clientId.getBytes(), 0, this.buf, 16, clientId.length());
/*     */ 
/*  76 */     if (shared_Secret != null) {
/*  77 */       len = clientId.length() + 17 + shared_Secret.length();
/*     */     }
/*     */     else {
/*  80 */       len = clientId.length() + 17;
/*     */     }
/*  82 */     byte[] tmpbuf = new byte[len];
/*  83 */     int tmploc = 0;
/*  84 */     System.arraycopy(clientId.getBytes(), 0, tmpbuf, 0, clientId.length());
/*  85 */     tmploc = clientId.length() + 7;
/*  86 */     if (shared_Secret != null) {
/*  87 */       System.arraycopy(shared_Secret.getBytes(), 0, tmpbuf, tmploc, shared_Secret.length());
/*  88 */       tmploc += shared_Secret.length();
/*     */     }
/*     */ 
/*  91 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
/*  92 */     String strTimeStamp = dateFormat.format(timeStamp).substring(2);
/*  93 */     System.arraycopy(strTimeStamp.toString().getBytes(), 0, tmpbuf, tmploc, 10);
/*  94 */     SecurityTools.md5(tmpbuf, 0, len, this.buf, 26);
/*     */ 
/*  97 */     this.buf[42] = (byte)loginMode;
/*     */ 
/* 100 */     TypeConvert.int2byte(Integer.parseInt(strTimeStamp), this.buf, 43);
/*     */ 
/* 103 */     this.buf[47] = (byte)version;
/*     */ 
/* 106 */     this.strBuf = new StringBuffer(300);
/* 107 */     this.strBuf.append(",clientId=".concat(String.valueOf(String.valueOf(clientId))));
/* 108 */     this.strBuf.append(",shared_Secret=".concat(String.valueOf(String.valueOf(shared_Secret))));
/* 109 */     this.strBuf.append(",loginMode=".concat(String.valueOf(String.valueOf(loginMode))));
/* 110 */     this.strBuf.append(",timeStamp=".concat(String.valueOf(String.valueOf(timeStamp))));
/* 111 */     this.strBuf.append(",version=".concat(String.valueOf(String.valueOf(version))));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 118 */     StringBuffer outStr = new StringBuffer(300);
/* 119 */     outStr.append("CNGPLoginMessage:");
/* 120 */     outStr.append("PacketLength=".concat(String.valueOf(String.valueOf(this.buf.length))));
/* 121 */     outStr.append(",RequestID=1");
/* 122 */     outStr.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/*     */ 
/* 124 */     if (this.strBuf != null)
/*     */     {
/* 126 */       outStr.append(this.strBuf.toString());
/*     */     }
/* 128 */     return outStr.toString();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.message.CNGPLoginMessage
 * JD-Core Version:    0.5.3
 */