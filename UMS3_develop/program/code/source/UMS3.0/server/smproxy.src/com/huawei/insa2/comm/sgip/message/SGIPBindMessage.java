/*     */ package com.huawei.insa2.comm.sgip.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.sgip.SGIPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ 
/*     */ public class SGIPBindMessage extends SGIPMessage
/*     */ {
/*     */   private String outStr;
/*     */ 
/*     */   public SGIPBindMessage(int login_Type, String login_Name, String login_Password)
/*     */     throws IllegalArgumentException
/*     */   {
/*  33 */     if (login_Name == null) {
/*  34 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.CONNECT_INPUT_ERROR))).append(":login_Name").append(SGIPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  37 */     if (login_Name.length() > 16) {
/*  38 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.CONNECT_INPUT_ERROR))).append(":login_Name").append(SGIPConstant.STRING_LENGTH_GREAT).append("16"))));
/*     */     }
/*     */ 
/*  42 */     if (login_Password == null) {
/*  43 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.CONNECT_INPUT_ERROR))).append(":login_Password").append(SGIPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  46 */     if (login_Password.length() > 16) {
/*  47 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.CONNECT_INPUT_ERROR))).append(":login_Password").append(SGIPConstant.STRING_LENGTH_GREAT).append("16"))));
/*     */     }
/*     */ 
/*  51 */     if ((login_Type < 0) || (login_Type > 255)) {
/*  52 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.CONNECT_INPUT_ERROR))).append(":login_Type").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  57 */     int len = 61;
/*     */ 
/*  59 */     this.buf = new byte[len];
/*     */ 
/*  63 */     TypeConvert.int2byte(len, this.buf, 0);
/*     */ 
/*  65 */     TypeConvert.int2byte(1, this.buf, 4);
/*     */ 
/*  70 */     this.buf[20] = (byte)login_Type;
/*     */ 
/*  73 */     System.arraycopy(login_Name.getBytes(), 0, this.buf, 21, login_Name.length());
/*     */ 
/*  76 */     System.arraycopy(login_Password.getBytes(), 0, this.buf, 37, login_Password.length());
/*     */ 
/*  79 */     this.outStr = ",login_Type=".concat(String.valueOf(String.valueOf(login_Type)));
/*  80 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",login_Name=").append(login_Name)));
/*  81 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",login_Password=").append(login_Password)));
/*     */   }
/*     */ 
/*     */   public SGIPBindMessage(byte[] buf)
/*     */     throws IllegalArgumentException
/*     */   {
/*  89 */     this.buf = new byte[53];
/*  90 */     if (buf.length != 53) {
/*  91 */       throw new IllegalArgumentException(SGIPConstant.SMC_MESSAGE_ERROR);
/*     */     }
/*  93 */     System.arraycopy(buf, 0, this.buf, 0, 53);
/*     */ 
/*  95 */     this.src_node_Id = TypeConvert.byte2int(this.buf, 0);
/*  96 */     this.time_Stamp = TypeConvert.byte2int(this.buf, 4);
/*  97 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 8);
/*     */   }
/*     */ 
/*     */   public int getLoginType()
/*     */   {
/* 104 */     int tmpId = this.buf[12];
/* 105 */     return tmpId;
/*     */   }
/*     */ 
/*     */   public String getLoginName()
/*     */   {
/* 112 */     byte[] tmpId = new byte[16];
/* 113 */     System.arraycopy(this.buf, 13, tmpId, 0, 16);
/*     */ 
/* 115 */     String tmpStr = new String(tmpId).trim();
/* 116 */     if (tmpStr.indexOf(0) >= 0) {
/* 117 */       return tmpStr.substring(0, tmpStr.indexOf(0));
/*     */     }
/* 119 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public String getLoginPass()
/*     */   {
/* 126 */     byte[] tmpId = new byte[16];
/* 127 */     System.arraycopy(this.buf, 29, tmpId, 0, 16);
/*     */ 
/* 129 */     String tmpStr = new String(tmpId).trim();
/* 130 */     if (tmpStr.indexOf(0) >= 0) {
/* 131 */       return tmpStr.substring(0, tmpStr.indexOf(0));
/*     */     }
/* 133 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 140 */     String tmpStr = "SGIP_BIND: ";
/* 141 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 142 */     tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
/* 143 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getCommandId()
/*     */   {
/* 150 */     return 1;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.message.SGIPBindMessage
 * JD-Core Version:    0.5.3
 */