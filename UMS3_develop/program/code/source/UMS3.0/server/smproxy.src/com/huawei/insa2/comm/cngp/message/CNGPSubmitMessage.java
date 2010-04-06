/*     */ package com.huawei.insa2.comm.cngp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.cngp.CNGPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class CNGPSubmitMessage extends CNGPMessage
/*     */ {
/*     */   private StringBuffer strBuf;
/*     */ 
/*     */   public CNGPSubmitMessage(String SPId, int subType, int needReport, int priority, String serviceId, String feeType, int feeUserType, String feeCode, int msgFormat, Date validTime, Date atTime, String srcTermId, String chargeTermId, int destTermIdCount, String[] destTermId, int msgLength, String msgContent, int protocolValue)
/*     */     throws IllegalArgumentException
/*     */   {
/*  65 */     if (SPId == null) {
/*  66 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":SPId ").append(CNGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  70 */     if (SPId.length() > 10) {
/*  71 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":SPId ").append(CNGPConstant.STRING_LENGTH_GREAT).append("10"))));
/*     */     }
/*     */ 
/*  76 */     if ((subType < 0) || (subType > 3)) {
/*  77 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":subType ").append(CNGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  81 */     if ((needReport < 0) || (needReport > 1)) {
/*  82 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":needReport ").append(CNGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  87 */     if ((priority < 0) || (priority > 3)) {
/*  88 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":priority ").append(CNGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  92 */     if (serviceId == null) {
/*  93 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":serviceId ").append(CNGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  98 */     if (serviceId.length() > 10) {
/*  99 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":serviceId ").append(CNGPConstant.STRING_LENGTH_GREAT).append("10"))));
/*     */     }
/*     */ 
/* 104 */     if (feeType == null) {
/* 105 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":feeType ").append(CNGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 110 */     if (feeType.length() > 2) {
/* 111 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":feeType ").append(CNGPConstant.STRING_LENGTH_GREAT).append("2"))));
/*     */     }
/*     */ 
/* 116 */     if ((feeUserType < 0) || (feeUserType > 255)) {
/* 117 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":feeUserType ").append(CNGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 122 */     if (feeCode == null) {
/* 123 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":feeCode ").append(CNGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 128 */     if (feeCode.length() > 6) {
/* 129 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":feeCode ").append(CNGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 133 */     if ((msgFormat < 0) || (msgFormat > 255)) {
/* 134 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":msgFormat ").append(CNGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 138 */     if (srcTermId == null) {
/* 139 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":srcTermId ").append(CNGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 144 */     if (srcTermId.length() > 21) {
/* 145 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":srcTermId ").append(CNGPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 150 */     if (chargeTermId == null) {
/* 151 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":chargeTermId ").append(CNGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 156 */     if (chargeTermId.length() > 21) {
/* 157 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":chargeTermId ").append(CNGPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 162 */     if ((destTermIdCount < 0) || (destTermIdCount > 100)) {
/* 163 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":destTermIdCount ").append(CNGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 167 */     if (destTermId == null) {
/* 168 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":destTermId ").append(CNGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 173 */     if ((msgLength < 0) || (msgLength > 255)) {
/* 174 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":msgLength ").append(CNGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 178 */     if (msgContent == null) {
/* 179 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":msgContent ").append(CNGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 184 */     if (msgContent.length() > 254) {
/* 185 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":msgContent ").append(CNGPConstant.STRING_LENGTH_GREAT).append("254"))));
/*     */     }
/*     */ 
/* 190 */     if ((protocolValue < 0) || (protocolValue > 255)) {
/* 191 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CNGPConstant.SUBMIT_INPUT_ERROR))).append(":protocolValue ").append(CNGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 199 */     byte[] ms = null;
/*     */     try {
/* 201 */       ms = msgContent.getBytes("gb2312");
/*     */     }
/*     */     catch (UnsupportedEncodingException ex) {
/* 204 */       ex.printStackTrace();
/*     */     }
/*     */ 
/* 207 */     int len = 132 + 21 * destTermIdCount + ms.length;
/*     */ 
/* 209 */     this.buf = new byte[len];
/*     */ 
/* 212 */     super.setMsgLength(len);
/* 213 */     super.setRequestId(2);
/*     */ 
/* 218 */     System.arraycopy(SPId.getBytes(), 0, this.buf, 16, SPId.length());
/* 219 */     this.buf[26] = (byte)subType;
/* 220 */     this.buf[27] = (byte)needReport;
/* 221 */     this.buf[28] = (byte)priority;
/* 222 */     System.arraycopy(serviceId.getBytes(), 0, this.buf, 29, serviceId.length());
/* 223 */     System.arraycopy(feeType.getBytes(), 0, this.buf, 39, feeType.length());
/* 224 */     this.buf[41] = (byte)feeUserType;
/* 225 */     System.arraycopy(feeCode.getBytes(), 0, this.buf, 42, feeCode.length());
/* 226 */     this.buf[48] = (byte)msgFormat;
/*     */ 
/* 228 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
/*     */     String tmpTime;
/* 230 */     if (validTime != null) {
/* 231 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(validTime))).concat("032+");
/* 232 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, 49, 16);
/*     */     }
/* 234 */     if (atTime != null) {
/* 235 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(atTime))).concat("032+");
/* 236 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, 66, 16);
/*     */     }
/* 238 */     System.arraycopy(srcTermId.getBytes(), 0, this.buf, 83, srcTermId.length());
/* 239 */     System.arraycopy(chargeTermId.getBytes(), 0, this.buf, 104, chargeTermId.length());
/*     */ 
/* 241 */     this.buf[125] = (byte)destTermIdCount;
/* 242 */     int i = 0;
/* 243 */     for (i = 0; i < destTermIdCount; ++i) {
/* 244 */       System.arraycopy(destTermId[i].getBytes(), 0, this.buf, 126 + i * 21, destTermId[i].length());
/*     */     }
/*     */ 
/* 247 */     int pos = 126 + 21 * destTermIdCount;
/* 248 */     this.buf[pos] = (byte)ms.length;
/* 249 */     ++pos;
/*     */ 
/* 251 */     System.arraycopy(ms, 0, this.buf, pos, ms.length);
/*     */ 
/* 253 */     pos += ms.length;
/*     */ 
/* 256 */     TypeConvert.short2byte(256, this.buf, pos);
/* 257 */     pos += 2;
/*     */ 
/* 259 */     TypeConvert.short2byte(1, this.buf, pos);
/* 260 */     pos += 2;
/*     */ 
/* 262 */     this.buf[pos] = (byte)protocolValue;
/*     */ 
/* 265 */     this.strBuf = new StringBuffer(600);
/* 266 */     this.strBuf.append(",SPId=".concat(String.valueOf(String.valueOf(SPId))));
/* 267 */     this.strBuf.append(",subType=".concat(String.valueOf(String.valueOf(subType))));
/* 268 */     this.strBuf.append(",NeedReport=".concat(String.valueOf(String.valueOf(needReport))));
/* 269 */     this.strBuf.append(",Priority=".concat(String.valueOf(String.valueOf(priority))));
/* 270 */     this.strBuf.append(",ServiceID=".concat(String.valueOf(String.valueOf(serviceId))));
/* 271 */     this.strBuf.append(",FeeType=".concat(String.valueOf(String.valueOf(feeType))));
/* 272 */     this.strBuf.append(",feeUserType=".concat(String.valueOf(String.valueOf(feeUserType))));
/* 273 */     this.strBuf.append(",FeeCode=".concat(String.valueOf(String.valueOf(feeCode))));
/* 274 */     this.strBuf.append(",msgFormat=".concat(String.valueOf(String.valueOf(msgFormat))));
/* 275 */     this.strBuf.append(",validTime=".concat(String.valueOf(String.valueOf(validTime))));
/* 276 */     this.strBuf.append(",atTime=".concat(String.valueOf(String.valueOf(atTime))));
/* 277 */     this.strBuf.append(",SrcTermID=".concat(String.valueOf(String.valueOf(srcTermId))));
/* 278 */     this.strBuf.append(",ChargeTermID=".concat(String.valueOf(String.valueOf(chargeTermId))));
/* 279 */     this.strBuf.append(",DestTermIDCount=".concat(String.valueOf(String.valueOf(destTermIdCount))));
/* 280 */     for (int t = 0; t < destTermIdCount; ++t) {
/* 281 */       this.strBuf.append(String.valueOf(String.valueOf(new StringBuffer(",DestTermID[").append(t).append("]=").append(destTermId[t]))));
/*     */     }
/* 283 */     this.strBuf.append(",MsgLength=".concat(String.valueOf(String.valueOf(ms.length))));
/* 284 */     this.strBuf.append(",MsgContent=".concat(String.valueOf(String.valueOf(msgContent))));
/* 285 */     this.strBuf.append(",protocolValue=".concat(String.valueOf(String.valueOf(protocolValue))));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 293 */     StringBuffer outBuf = new StringBuffer(600);
/* 294 */     outBuf.append("CNGPSubmitMessage: ");
/* 295 */     outBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 296 */     outBuf.append(",RequestID=".concat(String.valueOf(String.valueOf(super.getRequestId()))));
/* 297 */     outBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 298 */     outBuf.append(",SequenceID=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 299 */     if (this.strBuf != null) {
/* 300 */       outBuf.append(this.strBuf.toString());
/*     */     }
/* 302 */     return outBuf.toString();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.message.CNGPSubmitMessage
 * JD-Core Version:    0.5.3
 */