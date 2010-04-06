/*     */ package com.huawei.insa2.comm.smgp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*     */ import com.huawei.insa2.util.SecurityTools;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SMGPLoginMessage extends SMGPMessage
/*     */ {
/*     */   private StringBuffer strBuf;
/*     */ 
/*     */   public SMGPLoginMessage(String clientId, String shared_Secret, int loginMode, Date timestamp, int version)
/*     */     throws IllegalArgumentException
/*     */   {
/*  37 */     if (clientId == null) {
/*  38 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.CONNECT_INPUT_ERROR))).append(":clientId ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  41 */     if (clientId.length() > 8) {
/*  42 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.CONNECT_INPUT_ERROR))).append(":clientId ").append(SMGPConstant.STRING_LENGTH_GREAT).append("8"))));
/*     */     }
/*     */ 
/*  46 */     if ((loginMode < 0) || (loginMode > 255)) {
/*  47 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.CONNECT_INPUT_ERROR))).append(":loginMode ").append(SMGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  51 */     if ((version < 0) || (version > 255)) {
/*  52 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.CONNECT_INPUT_ERROR))).append(":version ").append(SMGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  57 */     int len = 42;
/*     */ 
/*  59 */     this.buf = new byte[len];
/*     */ 
/*  63 */     TypeConvert.int2byte(len, this.buf, 0);
/*     */ 
/*  65 */     TypeConvert.int2byte(1, this.buf, 4);
/*     */ 
/*  70 */     System.arraycopy(clientId.getBytes(), 0, this.buf, 12, clientId.length());
/*     */ 
/*  72 */     if (shared_Secret != null) {
/*  73 */       len = clientId.length() + 17 + shared_Secret.length();
/*     */     }
/*     */     else {
/*  76 */       len = clientId.length() + 17;
/*     */     }
/*  78 */     byte[] tmpbuf = new byte[len];
/*  79 */     int tmploc = 0;
/*  80 */     System.arraycopy(clientId.getBytes(), 0, tmpbuf, 0, clientId.length());
/*  81 */     tmploc = clientId.length() + 7;
/*  82 */     if (shared_Secret != null) {
/*  83 */       System.arraycopy(shared_Secret.getBytes(), 0, tmpbuf, tmploc, shared_Secret.length());
/*  84 */       tmploc += shared_Secret.length();
/*     */     }
/*  86 */     String tmptime = "0008080808";
/*  87 */     System.arraycopy(tmptime.getBytes(), 0, tmpbuf, tmploc, 10);
/*     */ 
/*  89 */     SecurityTools.md5(tmpbuf, 0, len, this.buf, 20);
/*     */ 
/*  92 */     this.buf[36] = (byte)loginMode;
/*     */ 
/*  95 */     TypeConvert.int2byte(8080808, this.buf, 37);
/*     */ 
/*  98 */     this.buf[41] = (byte)version;
/*     */ 
/* 101 */     this.strBuf = new StringBuffer(300);
/* 102 */     this.strBuf.append(",clientId=".concat(String.valueOf(String.valueOf(clientId))));
/* 103 */     this.strBuf.append(",shared_Secret=".concat(String.valueOf(String.valueOf(shared_Secret))));
/* 104 */     this.strBuf.append(",loginMode=".concat(String.valueOf(String.valueOf(loginMode))));
/* 105 */     this.strBuf.append(",version=".concat(String.valueOf(String.valueOf(version))));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 112 */     StringBuffer outStr = new StringBuffer(300);
/* 113 */     outStr.append("SMGPLoginMessage:");
/* 114 */     outStr.append("PacketLength=".concat(String.valueOf(String.valueOf(this.buf.length))));
/* 115 */     outStr.append(",RequestID=1");
/* 116 */     outStr.append(",Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/*     */ 
/* 118 */     if (this.strBuf != null)
/*     */     {
/* 120 */       outStr.append(this.strBuf.toString());
/*     */     }
/* 122 */     return outStr.toString();
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/* 129 */     return 1;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPLoginMessage
 * JD-Core Version:    0.5.3
 */