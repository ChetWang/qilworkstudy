/*     */ package com.huawei.insa2.comm.sgip.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.sgip.SGIPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SGIPSubmitMessage extends SGIPMessage
/*     */ {
/*     */   private String outStr;
/*     */ 
/*     */   public SGIPSubmitMessage(String SPNumber, String ChargeNumber, String[] UserNumber, String CorpId, String ServiceType, int FeeType, String FeeValue, String GivenValue, int AgentFlag, int MorelatetoMTFlag, int Priority, Date ExpireTime, Date ScheduleTime, int ReportFlag, int TP_pid, int TP_udhi, int MessageCoding, int MessageType, int MessageLen, byte[] MessageContent, String reserve)
/*     */     throws IllegalArgumentException
/*     */   {
/*  72 */     if (SPNumber.length() > 21)
/*     */     {
/*  74 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":SPNumber").append(SGIPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/*  78 */     if (ChargeNumber.length() > 21)
/*     */     {
/*  80 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":ChargeNumber").append(SGIPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/*  84 */     if (UserNumber.length > 100)
/*     */     {
/*  86 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":UserNumber").append(SGIPConstant.STRING_LENGTH_GREAT).append("100"))));
/*     */     }
/*     */ 
/*  90 */     for (int i = 0; i < UserNumber.length; ++i)
/*     */     {
/*  92 */       if (UserNumber[i].length() <= 21)
/*     */         continue;
/*  94 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":UserNumber[").append(i).append("]").append(SGIPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/*  99 */     if (CorpId.length() > 5)
/*     */     {
/* 101 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":CorpId").append(SGIPConstant.STRING_LENGTH_GREAT).append("5"))));
/*     */     }
/*     */ 
/* 105 */     if (ServiceType.length() > 10)
/*     */     {
/* 107 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":ServiceType").append(SGIPConstant.STRING_LENGTH_GREAT).append("10"))));
/*     */     }
/*     */ 
/* 111 */     if ((FeeType < 0) || (FeeType > 255))
/*     */     {
/* 113 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":FeeType").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 117 */     if (FeeValue.length() > 6)
/*     */     {
/* 119 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":FeeValue").append(SGIPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 123 */     if (GivenValue.length() > 6)
/*     */     {
/* 125 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":GivenValue").append(SGIPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 129 */     if ((AgentFlag < 0) || (AgentFlag > 255))
/*     */     {
/* 131 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":AgentFlag").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 135 */     if ((MorelatetoMTFlag < 0) || (MorelatetoMTFlag > 255))
/*     */     {
/* 137 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":MorelatetoMTFlag").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 141 */     if ((MorelatetoMTFlag < 0) || (MorelatetoMTFlag > 255))
/*     */     {
/* 143 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":MorelatetoMTFlag").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 147 */     if ((Priority < 0) || (Priority > 255))
/*     */     {
/* 149 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":Priority").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 153 */     if ((ReportFlag < 0) || (ReportFlag > 255))
/*     */     {
/* 155 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":ReportFlag").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 159 */     if ((TP_pid < 0) || (TP_pid > 255))
/*     */     {
/* 161 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":TP_pid").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 165 */     if ((TP_udhi < 0) || (TP_udhi > 255))
/*     */     {
/* 167 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":TP_udhi").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 171 */     if ((MessageCoding < 0) || (MessageCoding > 255))
/*     */     {
/* 173 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":MessageCoding").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 177 */     if ((MessageType < 0) || (MessageType > 255))
/*     */     {
/* 179 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":MessageType").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 183 */     if ((MessageLen < 0) || (MessageLen > 160))
/*     */     {
/* 185 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":MessageLen").append(SGIPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 189 */     if (MessageContent.length > 160)
/*     */     {
/* 191 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":MessageContent").append(SGIPConstant.STRING_LENGTH_GREAT).append("160"))));
/*     */     }
/*     */ 
/* 195 */     if (reserve.length() > 8)
/*     */     {
/* 197 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))).append(":reserve").append(SGIPConstant.STRING_LENGTH_GREAT).append("8"))));
/*     */     }
/*     */ 
/* 205 */     int len = 143 + 21 * UserNumber.length + MessageLen;
/*     */ 
/* 207 */     this.buf = new byte[len];
/*     */ 
/* 210 */     TypeConvert.int2byte(len, this.buf, 0);
/* 211 */     TypeConvert.int2byte(3, this.buf, 4);
/*     */ 
/* 214 */     System.arraycopy(SPNumber.getBytes(), 0, this.buf, 20, SPNumber.length());
/* 215 */     System.arraycopy(ChargeNumber.getBytes(), 0, this.buf, 41, ChargeNumber.length());
/* 216 */     this.buf[62] = (byte)UserNumber.length;
/*     */ 
/* 218 */     int i = 0;
/* 219 */     for (i = 0; i < UserNumber.length; ++i)
/*     */     {
/* 221 */       System.arraycopy(UserNumber[i].getBytes(), 0, this.buf, 63 + i * 21, UserNumber[i].length());
/*     */     }
/* 223 */     int loc = 63 + i * 21;
/*     */ 
/* 225 */     System.arraycopy(CorpId.getBytes(), 0, this.buf, loc, CorpId.length());
/* 226 */     loc += 5;
/* 227 */     System.arraycopy(ServiceType.getBytes(), 0, this.buf, loc, ServiceType.length());
/* 228 */     loc += 10;
/* 229 */     this.buf[(loc++)] = (byte)FeeType;
/* 230 */     System.arraycopy(FeeValue.getBytes(), 0, this.buf, loc, FeeValue.length());
/* 231 */     loc += 6;
/* 232 */     System.arraycopy(GivenValue.getBytes(), 0, this.buf, loc, GivenValue.length());
/* 233 */     loc += 6;
/* 234 */     this.buf[(loc++)] = (byte)AgentFlag;
/* 235 */     this.buf[(loc++)] = (byte)MorelatetoMTFlag;
/* 236 */     this.buf[(loc++)] = (byte)Priority;
/*     */ 
/* 238 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
/*     */     String tmpTime;
/* 239 */     if (ExpireTime != null) {
/* 240 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(ExpireTime))).concat("032+");
/* 241 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, loc, 16);
/*     */     }
/* 243 */     loc += 16;
/* 244 */     if (ScheduleTime != null) {
/* 245 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(ScheduleTime))).concat("032+");
/* 246 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, loc, 16);
/*     */     }
/* 248 */     loc += 16;
/*     */ 
/* 250 */     this.buf[(loc++)] = (byte)ReportFlag;
/* 251 */     this.buf[(loc++)] = (byte)TP_pid;
/* 252 */     this.buf[(loc++)] = (byte)TP_udhi;
/* 253 */     this.buf[(loc++)] = (byte)MessageCoding;
/* 254 */     this.buf[(loc++)] = (byte)MessageType;
/* 255 */     TypeConvert.int2byte(MessageLen, this.buf, loc);
/* 256 */     loc += 4;
/* 257 */     System.arraycopy(MessageContent, 0, this.buf, loc, MessageLen);
/*     */ 
/* 259 */     loc += MessageLen;
/*     */ 
/* 261 */     System.arraycopy(reserve.getBytes(), 0, this.buf, loc, reserve.length());
/*     */ 
/* 264 */     this.outStr = ",SPNumber=".concat(String.valueOf(String.valueOf(SPNumber)));
/* 265 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",ChargeNumber=").append(ChargeNumber)));
/* 266 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",UserCount=").append(UserNumber.length)));
/* 267 */     for (int t = 0; t < UserNumber.length; ++t)
/*     */     {
/* 269 */       this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",UserNumber[").append(t).append("]=").append(UserNumber[t])));
/*     */     }
/* 271 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",CorpId=").append(CorpId)));
/* 272 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",ServiceType=").append(ServiceType)));
/* 273 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",FeeType=").append(FeeType)));
/* 274 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",FeeValue=").append(FeeValue)));
/* 275 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",GivenValue=").append(GivenValue)));
/* 276 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",AgentFlag=").append(AgentFlag)));
/* 277 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",MorelatetoMTFlag=").append(MorelatetoMTFlag)));
/* 278 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",Priority=").append(Priority)));
/* 279 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",ExpireTime=").append(ExpireTime)));
/* 280 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",ScheduleTime=").append(ScheduleTime)));
/* 281 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",ReportFlag=").append(ReportFlag)));
/* 282 */     if (ExpireTime != null) {
/* 283 */       this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",ExpireTime=").append(dateFormat.format(ExpireTime))));
/*     */     }
/*     */     else {
/* 286 */       this.outStr = String.valueOf(String.valueOf(this.outStr)).concat(",ExpireTime=null");
/*     */     }
/* 288 */     if (ScheduleTime != null) {
/* 289 */       this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",ScheduleTime=").append(dateFormat.format(ScheduleTime))));
/*     */     }
/*     */     else {
/* 292 */       this.outStr = String.valueOf(String.valueOf(this.outStr)).concat(",ScheduleTime=null");
/*     */     }
/* 294 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",ReportFlag=").append(ReportFlag)));
/* 295 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",TP_pid=").append(TP_pid)));
/* 296 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",TP_udhi=").append(TP_udhi)));
/* 297 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",MessageCoding=").append(MessageCoding)));
/* 298 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",MessageType=").append(MessageType)));
/* 299 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",MessageLength=").append(MessageLen)));
/* 300 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",MessageContent=").append(new String(MessageContent))));
/* 301 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",reserve=").append(reserve)));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 308 */     String tmpStr = "SGIP_Submit: ";
/* 309 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 310 */     tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
/* 311 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getCommandId()
/*     */   {
/* 318 */     return 3;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.message.SGIPSubmitMessage
 * JD-Core Version:    0.5.3
 */