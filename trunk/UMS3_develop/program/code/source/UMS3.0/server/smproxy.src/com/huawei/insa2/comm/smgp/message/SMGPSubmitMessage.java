/*     */ package com.huawei.insa2.comm.smgp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SMGPSubmitMessage extends SMGPMessage
/*     */ {
/*     */   private StringBuffer strBuf;
/*     */ 
/*     */   public SMGPSubmitMessage(int msgType, int needReport, int priority, String serviceId, String feeType, String feeCode, String fixedFee, int msgFormat, Date validTime, Date atTime, String srcTermId, String chargeTermId, String[] destTermId, String msgContent, String reserve)
/*     */     throws IllegalArgumentException
/*     */   {
/*  61 */     if ((msgType < 0) || (msgType > 255))
/*     */     {
/*  63 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgType ").append(SMGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  67 */     if ((needReport < 0) || (needReport > 255))
/*     */     {
/*  69 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":NeedReport ").append(SMGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  73 */     if ((priority < 0) || (priority > 9))
/*     */     {
/*  75 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":").append(SMGPConstant.PRIORITY_ERROR))));
/*     */     }
/*     */ 
/*  79 */     if (serviceId == null)
/*     */     {
/*  81 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ServiceID ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  85 */     if (serviceId.length() > 10)
/*     */     {
/*  87 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ServiceID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("10"))));
/*     */     }
/*     */ 
/*  91 */     if (feeType == null)
/*     */     {
/*  93 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FeeType ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  97 */     if (feeType.length() > 2)
/*     */     {
/*  99 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FeeType ").append(SMGPConstant.STRING_LENGTH_GREAT).append("2"))));
/*     */     }
/*     */ 
/* 103 */     if (feeCode == null)
/*     */     {
/* 105 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FeeCode ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 109 */     if (feeCode.length() > 6)
/*     */     {
/* 111 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FeeCode ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 114 */     if (fixedFee == null)
/*     */     {
/* 116 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FixedFee ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 120 */     if (fixedFee.length() > 6)
/*     */     {
/* 122 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FixedFee ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 126 */     if ((msgFormat < 0) || (msgFormat > 255))
/*     */     {
/* 128 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgFormat ").append(SMGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 132 */     if (srcTermId == null)
/*     */     {
/* 134 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":SrcTermID ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 138 */     if (srcTermId.length() > 21)
/*     */     {
/* 140 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":SrcTermID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 144 */     if (chargeTermId == null)
/*     */     {
/* 146 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ChargeTermID ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 150 */     if (chargeTermId.length() > 21)
/*     */     {
/* 152 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ChargeTermID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 156 */     if (destTermId == null)
/*     */     {
/* 158 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":DestTermID ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 162 */     if (destTermId.length > 100)
/*     */     {
/* 164 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":").append(SMGPConstant.DESTTERMID_ERROR))));
/*     */     }
/*     */ 
/* 167 */     for (int i = 0; i < destTermId.length; ++i)
/*     */     {
/* 169 */       if (destTermId[i].length() <= 21)
/*     */         continue;
/* 171 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":one DestTermID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 177 */     if (msgContent == null)
/*     */     {
/* 179 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgContent ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 182 */     if (msgContent.length() > 252)
/*     */     {
/* 184 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgContent ").append(SMGPConstant.STRING_LENGTH_GREAT).append("252"))));
/*     */     }
/*     */ 
/* 187 */     if ((reserve != null) && 
/* 189 */       (reserve.length() > 8))
/*     */     {
/* 191 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":reserve ").append(SMGPConstant.STRING_LENGTH_GREAT).append("8"))));
/*     */     }
/*     */ 
/* 199 */     int len = 126 + 21 * destTermId.length + msgContent.length();
/*     */ 
/* 201 */     this.buf = new byte[len];
/*     */ 
/* 204 */     TypeConvert.int2byte(len, this.buf, 0);
/* 205 */     TypeConvert.int2byte(2, this.buf, 4);
/*     */ 
/* 209 */     this.buf[12] = (byte)msgType;
/* 210 */     this.buf[13] = (byte)needReport;
/* 211 */     this.buf[14] = (byte)priority;
/* 212 */     System.arraycopy(serviceId.getBytes(), 0, this.buf, 15, serviceId.length());
/* 213 */     System.arraycopy(feeType.getBytes(), 0, this.buf, 25, feeType.length());
/* 214 */     System.arraycopy(feeCode.getBytes(), 0, this.buf, 27, feeCode.length());
/* 215 */     System.arraycopy(fixedFee.getBytes(), 0, this.buf, 33, fixedFee.length());
/* 216 */     this.buf[39] = (byte)msgFormat;
/*     */ 
/* 219 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
/*     */     String tmpTime;
/* 221 */     if (validTime != null) {
/* 222 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(validTime))).concat("032+");
/* 223 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, 40, 16);
/*     */     }
/*     */ 
/* 227 */     if (atTime != null) {
/* 228 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(atTime))).concat("032+");
/* 229 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, 57, 16);
/*     */     }
/*     */ 
/* 232 */     System.arraycopy(srcTermId.getBytes(), 0, this.buf, 74, srcTermId.length());
/*     */ 
/* 234 */     System.arraycopy(chargeTermId.getBytes(), 0, this.buf, 95, chargeTermId.length());
/*     */ 
/* 236 */     this.buf[116] = (byte)destTermId.length;
/*     */ 
/* 238 */     int i = 0;
/* 239 */     for (i = 0; i < destTermId.length; ++i)
/*     */     {
/* 241 */       System.arraycopy(destTermId[i].getBytes(), 0, this.buf, 117 + i * 21, destTermId[i].length());
/*     */     }
/* 243 */     int loc = 117 + i * 21;
/*     */ 
/* 245 */     this.buf[loc] = (byte)msgContent.length();
/*     */ 
/* 247 */     System.arraycopy(msgContent.getBytes(), 0, this.buf, loc + 1, msgContent.length());
/*     */ 
/* 249 */     loc = loc + 1 + msgContent.length();
/* 250 */     if (reserve != null)
/*     */     {
/* 252 */       System.arraycopy(reserve.getBytes(), 0, this.buf, loc, reserve.length());
/*     */     }
/*     */ 
/* 256 */     this.strBuf = new StringBuffer(600);
/* 257 */     this.strBuf.append(",MsgType=".concat(String.valueOf(String.valueOf(msgType))));
/* 258 */     this.strBuf.append(",NeedReport=".concat(String.valueOf(String.valueOf(needReport))));
/* 259 */     this.strBuf.append(",Priority=".concat(String.valueOf(String.valueOf(priority))));
/* 260 */     this.strBuf.append(",ServiceID=".concat(String.valueOf(String.valueOf(serviceId))));
/* 261 */     this.strBuf.append(",FeeType=".concat(String.valueOf(String.valueOf(feeType))));
/* 262 */     this.strBuf.append(",FeeCode=".concat(String.valueOf(String.valueOf(feeCode))));
/* 263 */     this.strBuf.append(",FixedFee=".concat(String.valueOf(String.valueOf(fixedFee))));
/* 264 */     this.strBuf.append(",MsgFormat=".concat(String.valueOf(String.valueOf(msgFormat))));
/* 265 */     if (validTime != null) {
/* 266 */       this.strBuf.append(",ValidTime=".concat(String.valueOf(String.valueOf(dateFormat.format(validTime)))));
/*     */     }
/*     */     else
/*     */     {
/* 270 */       this.strBuf.append(",ValidTime=null");
/*     */     }
/* 272 */     if (atTime != null)
/*     */     {
/* 274 */       this.strBuf.append(",AtTime=".concat(String.valueOf(String.valueOf(dateFormat.format(atTime)))));
/*     */     }
/*     */     else
/*     */     {
/* 278 */       this.strBuf.append(",at_Time=null");
/*     */     }
/* 280 */     this.strBuf.append(",SrcTermID=".concat(String.valueOf(String.valueOf(srcTermId))));
/* 281 */     this.strBuf.append(",ChargeTermID=".concat(String.valueOf(String.valueOf(chargeTermId))));
/* 282 */     this.strBuf.append(",DestTermIDCount=".concat(String.valueOf(String.valueOf(destTermId.length))));
/* 283 */     for (int t = 0; t < destTermId.length; ++t)
/*     */     {
/* 285 */       this.strBuf.append(String.valueOf(String.valueOf(new StringBuffer(",DestTermID[").append(t).append("]=").append(destTermId[t]))));
/*     */     }
/* 287 */     this.strBuf.append(",MsgLength=".concat(String.valueOf(String.valueOf(msgContent.length()))));
/* 288 */     this.strBuf.append(",MsgContent=".concat(String.valueOf(String.valueOf(msgContent))));
/* 289 */     this.strBuf.append(",Reserve=".concat(String.valueOf(String.valueOf(reserve))));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 297 */     StringBuffer outBuf = new StringBuffer(600);
/* 298 */     outBuf.append("SMGPSubmitMessage: ");
/* 299 */     outBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(this.buf.length))));
/* 300 */     outBuf.append(",RequestID=2");
/* 301 */     outBuf.append(",SequenceID=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 302 */     if (this.strBuf != null)
/*     */     {
/* 304 */       outBuf.append(this.strBuf.toString());
/*     */     }
/* 306 */     return outBuf.toString();
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/* 313 */     return 2;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPSubmitMessage
 * JD-Core Version:    0.5.3
 */