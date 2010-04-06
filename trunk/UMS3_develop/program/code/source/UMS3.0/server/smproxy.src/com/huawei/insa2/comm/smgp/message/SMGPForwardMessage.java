/*     */ package com.huawei.insa2.comm.smgp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SMGPForwardMessage extends SMGPMessage
/*     */ {
/*     */   private StringBuffer strBuf;
/*     */ 
/*     */   public SMGPForwardMessage(byte[] msgId, String destSMGWNo, String srcSMGWNo, String smcNo, int msgType, int reportFlag, int priority, String serviceId, String feeType, String feeCode, String fixedFee, int msgFormat, Date validTime, Date atTime, String srcTermId, String destTermId, String chargeTermId, String msgContent, String reserve)
/*     */     throws IllegalArgumentException
/*     */   {
/*  68 */     if (msgId == null)
/*     */     {
/*  70 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":MsgID ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  73 */     if (msgId.length > 10)
/*     */     {
/*  75 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":MsgID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("10"))));
/*     */     }
/*     */ 
/*  79 */     if (destSMGWNo == null)
/*     */     {
/*  81 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":DestSMGWNo ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  84 */     if (destSMGWNo.length() > 6)
/*     */     {
/*  86 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":DestSMGWNo ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/*  90 */     if (srcSMGWNo == null)
/*     */     {
/*  92 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":SrcSMGWNo ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  95 */     if (srcSMGWNo.length() > 6)
/*     */     {
/*  97 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":SrcSMGWNo ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 101 */     if (smcNo == null)
/*     */     {
/* 103 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":SMCNo ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 106 */     if (smcNo.length() > 6)
/*     */     {
/* 108 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":SMCNo ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 113 */     if ((msgType < 0) || (msgType > 255))
/*     */     {
/* 115 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":MsgType ").append(SMGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 118 */     if ((reportFlag < 0) || (reportFlag > 255))
/*     */     {
/* 120 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":ReportFlag ").append(SMGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 124 */     if ((priority < 0) || (priority > 9))
/*     */     {
/* 126 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":").append(SMGPConstant.PRIORITY_ERROR))));
/*     */     }
/*     */ 
/* 130 */     if (serviceId == null)
/*     */     {
/* 132 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":ServiceID ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 136 */     if (serviceId.length() > 10)
/*     */     {
/* 138 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":ServiceID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("10"))));
/*     */     }
/*     */ 
/* 142 */     if (feeType == null)
/*     */     {
/* 144 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":FeeType ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 148 */     if (feeType.length() > 2)
/*     */     {
/* 150 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":FeeType ").append(SMGPConstant.STRING_LENGTH_GREAT).append("2"))));
/*     */     }
/*     */ 
/* 154 */     if (feeCode == null)
/*     */     {
/* 156 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":FeeCode ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 160 */     if (feeCode.length() > 6)
/*     */     {
/* 162 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":FeeCode ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 166 */     if (fixedFee == null)
/*     */     {
/* 168 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":FixedFee ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 172 */     if (fixedFee.length() > 6)
/*     */     {
/* 174 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":FixedFee ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 178 */     if ((msgFormat < 0) || (msgFormat > 255))
/*     */     {
/* 180 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":MsgFormat ").append(SMGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 184 */     if (srcTermId == null)
/*     */     {
/* 186 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":SrcTermID ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 190 */     if (srcTermId.length() > 21)
/*     */     {
/* 192 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":SrcTermID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 196 */     if (destTermId == null)
/*     */     {
/* 198 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":DestTermId ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 202 */     if (destTermId.length() > 21)
/*     */     {
/* 204 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":DestTermId ").append(SMGPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 208 */     if (chargeTermId == null)
/*     */     {
/* 210 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":ChargeTermID ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 214 */     if (chargeTermId.length() > 21)
/*     */     {
/* 216 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":ChargeTermID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 220 */     if (msgContent == null)
/*     */     {
/* 222 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":MsgContent ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 225 */     if (msgContent.length() > 252)
/*     */     {
/* 227 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":MsgContent ").append(SMGPConstant.STRING_LENGTH_GREAT).append("252"))));
/*     */     }
/*     */ 
/* 230 */     if ((reserve != null) && 
/* 232 */       (reserve.length() > 8))
/*     */     {
/* 234 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.FORWARD_INPUT_ERROR))).append(":reserve ").append(SMGPConstant.STRING_LENGTH_GREAT).append("8"))));
/*     */     }
/*     */ 
/* 242 */     int len = 174 + msgContent.length();
/*     */ 
/* 244 */     this.buf = new byte[len];
/*     */ 
/* 247 */     TypeConvert.int2byte(len, this.buf, 0);
/* 248 */     TypeConvert.int2byte(5, this.buf, 4);
/*     */ 
/* 253 */     System.arraycopy(msgId, 0, this.buf, 12, 10);
/*     */ 
/* 255 */     System.arraycopy(destSMGWNo.getBytes(), 0, this.buf, 22, destSMGWNo.length());
/*     */ 
/* 257 */     System.arraycopy(srcSMGWNo.getBytes(), 0, this.buf, 28, srcSMGWNo.length());
/*     */ 
/* 259 */     System.arraycopy(smcNo.getBytes(), 0, this.buf, 34, smcNo.length());
/*     */ 
/* 262 */     this.buf[40] = (byte)msgType;
/* 263 */     this.buf[41] = (byte)reportFlag;
/* 264 */     this.buf[42] = (byte)priority;
/* 265 */     System.arraycopy(serviceId.getBytes(), 0, this.buf, 43, serviceId.length());
/* 266 */     System.arraycopy(feeType.getBytes(), 0, this.buf, 53, feeType.length());
/* 267 */     System.arraycopy(feeCode.getBytes(), 0, this.buf, 55, feeCode.length());
/* 268 */     System.arraycopy(fixedFee.getBytes(), 0, this.buf, 61, fixedFee.length());
/* 269 */     this.buf[67] = (byte)msgFormat;
/*     */ 
/* 273 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
/*     */     String tmpTime;
/* 275 */     if (validTime != null) {
/* 276 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(validTime))).concat("032+");
/* 277 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, 68, 16);
/*     */     }
/*     */ 
/* 281 */     if (atTime != null) {
/* 282 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(atTime))).concat("032+");
/* 283 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, 85, 16);
/*     */     }
/*     */ 
/* 286 */     System.arraycopy(srcTermId.getBytes(), 0, this.buf, 102, srcTermId.length());
/*     */ 
/* 288 */     System.arraycopy(destTermId.getBytes(), 0, this.buf, 123, destTermId.length());
/*     */ 
/* 290 */     System.arraycopy(chargeTermId.getBytes(), 0, this.buf, 144, chargeTermId.length());
/*     */ 
/* 293 */     this.buf[165] = (byte)msgContent.length();
/*     */ 
/* 295 */     System.arraycopy(msgContent.getBytes(), 0, this.buf, 166, msgContent.length());
/*     */ 
/* 297 */     if (reserve != null)
/*     */     {
/* 299 */       System.arraycopy(reserve.getBytes(), 0, this.buf, 166 + msgContent.length(), reserve.length());
/*     */     }
/*     */ 
/* 303 */     this.strBuf = new StringBuffer(600);
/* 304 */     this.strBuf.append(",DestSMGWNo=".concat(String.valueOf(String.valueOf(destSMGWNo))));
/* 305 */     this.strBuf.append(",SrcSMGWNo=".concat(String.valueOf(String.valueOf(srcSMGWNo))));
/* 306 */     this.strBuf.append(",SMCNo=".concat(String.valueOf(String.valueOf(smcNo))));
/* 307 */     this.strBuf.append(",MsgType=".concat(String.valueOf(String.valueOf(msgType))));
/* 308 */     this.strBuf.append(",ReportFlag=".concat(String.valueOf(String.valueOf(reportFlag))));
/* 309 */     this.strBuf.append(",Priority=".concat(String.valueOf(String.valueOf(priority))));
/* 310 */     this.strBuf.append(",ServiceID=".concat(String.valueOf(String.valueOf(serviceId))));
/* 311 */     this.strBuf.append(",FeeType=".concat(String.valueOf(String.valueOf(feeType))));
/* 312 */     this.strBuf.append(",FeeCode=".concat(String.valueOf(String.valueOf(feeCode))));
/* 313 */     this.strBuf.append(",fixedFee=".concat(String.valueOf(String.valueOf(fixedFee))));
/* 314 */     this.strBuf.append(",MsgFormat=".concat(String.valueOf(String.valueOf(msgFormat))));
/* 315 */     if (validTime != null) {
/* 316 */       this.strBuf.append(",ValidTime=".concat(String.valueOf(String.valueOf(dateFormat.format(validTime)))));
/*     */     }
/*     */     else
/*     */     {
/* 320 */       this.strBuf.append(",ValidTime=null");
/*     */     }
/* 322 */     if (atTime != null)
/*     */     {
/* 324 */       this.strBuf.append(",AtTime=".concat(String.valueOf(String.valueOf(dateFormat.format(atTime)))));
/*     */     }
/*     */     else
/*     */     {
/* 328 */       this.strBuf.append(",at_Time=null");
/*     */     }
/* 330 */     this.strBuf.append(",SrcTermID=".concat(String.valueOf(String.valueOf(srcTermId))));
/* 331 */     this.strBuf.append(",DestTermID=".concat(String.valueOf(String.valueOf(destTermId))));
/* 332 */     this.strBuf.append(",ChargeTermID=".concat(String.valueOf(String.valueOf(chargeTermId))));
/* 333 */     this.strBuf.append(",MsgLength=".concat(String.valueOf(String.valueOf(msgContent.length()))));
/* 334 */     this.strBuf.append(",MsgContent=".concat(String.valueOf(String.valueOf(msgContent))));
/* 335 */     this.strBuf.append(",Reserve=".concat(String.valueOf(String.valueOf(reserve))));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 342 */     StringBuffer outBuf = new StringBuffer(600);
/* 343 */     outBuf.append("SMGPForwardMessage: ");
/* 344 */     outBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(this.buf.length))));
/* 345 */     outBuf.append(",RequestID=5");
/* 346 */     outBuf.append(",SequenceID=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 347 */     if (this.strBuf != null)
/*     */     {
/* 349 */       outBuf.append(this.strBuf.toString());
/*     */     }
/* 351 */     return outBuf.toString();
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/* 358 */     return 5;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPForwardMessage
 * JD-Core Version:    0.5.3
 */