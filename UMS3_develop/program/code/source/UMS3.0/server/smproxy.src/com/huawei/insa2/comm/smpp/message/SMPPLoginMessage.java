/*     */ package com.huawei.insa2.comm.smpp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.smpp.SMPPConstant;
/*     */ 
/*     */ public class SMPPLoginMessage extends SMPPMessage
/*     */ {
/*     */   private StringBuffer strBuf;
/*     */   private int loginCommandId;
/*     */ 
/*     */   public SMPPLoginMessage(int logintype, String systemId, String password, String systemType, byte interfaceVersion, byte addrTon, byte addrNpi, String addressRange)
/*     */     throws IllegalArgumentException
/*     */   {
/*  40 */     if ((logintype != 1) && (logintype != 2))
/*     */     {
/*  42 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.CONNECT_INPUT_ERROR))).append(":loginCommandId ").append(SMPPConstant.OTHER_ERROR))));
/*     */     }
/*     */ 
/*  46 */     this.loginCommandId = logintype;
/*     */ 
/*  49 */     if (systemId.length() > 15) {
/*  50 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.CONNECT_INPUT_ERROR))).append(":systemId ").append(SMPPConstant.STRING_LENGTH_GREAT).append("15"))));
/*     */     }
/*     */ 
/*  53 */     if (password.length() > 8) {
/*  54 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.CONNECT_INPUT_ERROR))).append(":password ").append(SMPPConstant.STRING_LENGTH_GREAT).append("8"))));
/*     */     }
/*     */ 
/*  57 */     if (systemType.length() > 12) {
/*  58 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.CONNECT_INPUT_ERROR))).append(":systemType ").append(SMPPConstant.STRING_LENGTH_GREAT).append("12"))));
/*     */     }
/*     */ 
/*  62 */     if (addressRange.length() > 40) {
/*  63 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.CONNECT_INPUT_ERROR))).append(":addressRange ").append(SMPPConstant.STRING_LENGTH_GREAT).append("40"))));
/*     */     }
/*     */ 
/*  68 */     int len = 23 + systemId.length() + password.length() + systemType.length() + addressRange.length();
/*     */ 
/*  70 */     this.buf = new byte[len];
/*     */ 
/*  74 */     super.setMsgLength(len);
/*     */ 
/*  76 */     super.setCommandId(this.loginCommandId);
/*     */ 
/*  78 */     super.setStatus(0);
/*     */ 
/*  82 */     int pos = 16;
/*     */ 
/*  84 */     System.arraycopy(systemId.getBytes(), 0, this.buf, pos, systemId.length());
/*     */ 
/*  86 */     pos = pos + systemId.length() + 1;
/*  87 */     System.arraycopy(password.getBytes(), 0, this.buf, pos, password.length());
/*     */ 
/*  89 */     pos = pos + password.length() + 1;
/*  90 */     System.arraycopy(systemType.getBytes(), 0, this.buf, pos, systemType.length());
/*     */ 
/*  92 */     pos = pos + systemType.length() + 1;
/*  93 */     this.buf[pos] = interfaceVersion;
/*     */ 
/*  95 */     pos += 1;
/*  96 */     this.buf[pos] = addrTon;
/*     */ 
/*  98 */     pos += 1;
/*  99 */     this.buf[pos] = addrNpi;
/*     */ 
/* 101 */     pos += 1;
/* 102 */     System.arraycopy(addressRange.getBytes(), 0, this.buf, pos, addressRange.length());
/*     */ 
/* 105 */     this.strBuf = new StringBuffer(200);
/* 106 */     this.strBuf.append(",systemID=".concat(String.valueOf(String.valueOf(systemId))));
/* 107 */     this.strBuf.append(",password=".concat(String.valueOf(String.valueOf(password))));
/* 108 */     this.strBuf.append(",systemType=".concat(String.valueOf(String.valueOf(systemType))));
/* 109 */     this.strBuf.append(",interfaceVersion=".concat(String.valueOf(String.valueOf(interfaceVersion))));
/* 110 */     this.strBuf.append(",addrTon=".concat(String.valueOf(String.valueOf(addrTon))));
/* 111 */     this.strBuf.append(",addrNpi=".concat(String.valueOf(String.valueOf(addrNpi))));
/* 112 */     this.strBuf.append(",addressRange=".concat(String.valueOf(String.valueOf(addressRange))));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 119 */     StringBuffer outStr = new StringBuffer(300);
/* 120 */     outStr.append("SMPPLoginMessage:");
/* 121 */     outStr.append("PacketLength=".concat(String.valueOf(String.valueOf(this.buf.length))));
/* 122 */     outStr.append(",CommandID=".concat(String.valueOf(String.valueOf(super.getCommandId()))));
/* 123 */     outStr.append(",SequenceId=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/*     */ 
/* 125 */     if (this.strBuf != null)
/*     */     {
/* 127 */       outStr.append(this.strBuf.toString());
/*     */     }
/* 129 */     return outStr.toString();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.message.SMPPLoginMessage
 * JD-Core Version:    0.5.3
 */