/*     */ package com.huawei.insa2.comm.cmpp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class CMPPSubmitMessage extends CMPPMessage
/*     */ {
/*     */   private String outStr;
/*     */ 
/*     */   public CMPPSubmitMessage(int pk_Total, int pk_Number, int registered_Delivery, int msg_Level, String service_Id, int fee_UserType, String fee_Terminal_Id, int tp_Pid, int tp_Udhi, int msg_Fmt, String msg_Src, String fee_Type, String fee_Code, Date valid_Time, Date at_Time, String src_Terminal_Id, String[] dest_Terminal_Id, byte[] msg_Content, String reserve)
/*     */     throws IllegalArgumentException
/*     */   {
/*  69 */     if ((pk_Total < 1) || (pk_Total > 255))
/*     */     {
/*  71 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":").append(CMPPConstant.PK_TOTAL_ERROR))));
/*     */     }
/*     */ 
/*  75 */     if ((pk_Number < 1) || (pk_Number > 255))
/*     */     {
/*  77 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":").append(CMPPConstant.PK_NUMBER_ERROR))));
/*     */     }
/*     */ 
/*  81 */     if ((registered_Delivery < 0) || (registered_Delivery > 2))
/*     */     {
/*  83 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":").append(CMPPConstant.REGISTERED_DELIVERY_ERROR))));
/*     */     }
/*     */ 
/*  87 */     if ((msg_Level < 0) || (msg_Level > 255))
/*     */     {
/*  89 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":msg_Level").append(CMPPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  93 */     if (service_Id.length() > 10)
/*     */     {
/*  95 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":service_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("10"))));
/*     */     }
/*     */ 
/*  99 */     if ((fee_UserType < 0) || (fee_UserType > 3))
/*     */     {
/* 101 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":").append(CMPPConstant.FEE_USERTYPE_ERROR))));
/*     */     }
/*     */ 
/* 105 */     if (fee_Terminal_Id.length() > 21)
/*     */     {
/* 107 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":fee_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 111 */     if ((tp_Pid < 0) || (tp_Pid > 255))
/*     */     {
/* 113 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":tp_Pid").append(CMPPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 117 */     if ((tp_Udhi < 0) || (tp_Udhi > 255))
/*     */     {
/* 119 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":tp_Udhi").append(CMPPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 123 */     if ((msg_Fmt < 0) || (msg_Fmt > 255))
/*     */     {
/* 125 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":msg_Fmt").append(CMPPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 129 */     if (msg_Src.length() > 6)
/*     */     {
/* 131 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":msg_Src").append(CMPPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 135 */     if (fee_Type.length() > 2)
/*     */     {
/* 137 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":fee_Type").append(CMPPConstant.STRING_LENGTH_GREAT).append("2"))));
/*     */     }
/*     */ 
/* 141 */     if (fee_Code.length() > 6)
/*     */     {
/* 143 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":fee_Code").append(CMPPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 147 */     if (src_Terminal_Id.length() > 21)
/*     */     {
/* 149 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":src_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 153 */     if (dest_Terminal_Id.length > 100)
/*     */     {
/* 155 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":dest_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("100"))));
/*     */     }
/*     */ 
/* 159 */     for (int i = 0; i < dest_Terminal_Id.length; ++i)
/*     */     {
/* 161 */       if (dest_Terminal_Id[i].length() <= 21)
/*     */         continue;
/* 163 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":dest_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 168 */     if (msg_Fmt == 0)
/*     */     {
/* 170 */       if (msg_Content.length > 160)
/*     */       {
/* 172 */         throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":msg_Content").append(CMPPConstant.STRING_LENGTH_GREAT).append("160"))));
/*     */       }
/*     */ 
/*     */     }
/* 178 */     else if (msg_Content.length > 140)
/*     */     {
/* 180 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":msg_Content").append(CMPPConstant.STRING_LENGTH_GREAT).append("140"))));
/*     */     }
/*     */ 
/* 184 */     if (reserve.length() > 8)
/*     */     {
/* 186 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":reserve").append(CMPPConstant.STRING_LENGTH_GREAT).append("8"))));
/*     */     }
/*     */ 
/* 192 */     int len = 138 + 21 * dest_Terminal_Id.length + msg_Content.length;
/*     */ 
/* 194 */     this.buf = new byte[len];
/*     */ 
/* 197 */     TypeConvert.int2byte(len, this.buf, 0);
/* 198 */     TypeConvert.int2byte(4, this.buf, 4);
/*     */ 
/* 204 */     this.buf[20] = (byte)pk_Total;
/* 205 */     this.buf[21] = (byte)pk_Number;
/* 206 */     this.buf[22] = (byte)registered_Delivery;
/* 207 */     this.buf[23] = (byte)msg_Level;
/* 208 */     System.arraycopy(service_Id.getBytes(), 0, this.buf, 24, service_Id.length());
/* 209 */     this.buf[34] = (byte)fee_UserType;
/* 210 */     System.arraycopy(fee_Terminal_Id.getBytes(), 0, this.buf, 35, fee_Terminal_Id.length());
/* 211 */     this.buf[56] = (byte)tp_Pid;
/* 212 */     this.buf[57] = (byte)tp_Udhi;
/* 213 */     this.buf[58] = (byte)msg_Fmt;
/* 214 */     System.arraycopy(msg_Src.getBytes(), 0, this.buf, 59, msg_Src.length());
/* 215 */     System.arraycopy(fee_Type.getBytes(), 0, this.buf, 65, fee_Type.length());
/* 216 */     System.arraycopy(fee_Code.getBytes(), 0, this.buf, 67, fee_Code.length());
/*     */ 
/* 220 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
/*     */     String tmpTime;
/* 222 */     if (valid_Time != null) {
/* 223 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(valid_Time))).concat("032+");
/* 224 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, 73, 16);
/*     */     }
/*     */ 
/* 229 */     if (at_Time != null) {
/* 230 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(at_Time))).concat("032+");
/* 231 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, 90, 16);
/*     */     }
/*     */ 
/* 234 */     System.arraycopy(src_Terminal_Id.getBytes(), 0, this.buf, 107, src_Terminal_Id.length());
/*     */ 
/* 236 */     this.buf[128] = (byte)dest_Terminal_Id.length;
/*     */ 
/* 238 */     int i = 0;
/* 239 */     for (i = 0; i < dest_Terminal_Id.length; ++i)
/*     */     {
/* 241 */       System.arraycopy(dest_Terminal_Id[i].getBytes(), 0, this.buf, 129 + i * 21, dest_Terminal_Id[i].length());
/*     */     }
/*     */ 
/* 244 */     int loc = 129 + i * 21;
/* 245 */     this.buf[loc] = (byte)msg_Content.length;
/* 246 */     System.arraycopy(msg_Content, 0, this.buf, loc + 1, msg_Content.length);
/*     */ 
/* 248 */     loc = loc + 1 + msg_Content.length;
/* 249 */     System.arraycopy(reserve.getBytes(), 0, this.buf, loc, reserve.length());
/*     */ 
/* 252 */     this.outStr = ",msg_id=00000000";
/* 253 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",pk_Total=").append(pk_Total)));
/* 254 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",pk_Number=").append(pk_Number)));
/* 255 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",registered_Delivery=").append(registered_Delivery)));
/* 256 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Level=").append(msg_Level)));
/* 257 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",service_Id=").append(service_Id)));
/* 258 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",fee_UserType=").append(fee_UserType)));
/* 259 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",fee_Terminal_Id=").append(fee_Terminal_Id)));
/* 260 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",tp_Pid=").append(tp_Pid)));
/* 261 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",tp_Udhi=").append(tp_Udhi)));
/* 262 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Fmt=").append(msg_Fmt)));
/* 263 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Src=").append(msg_Src)));
/* 264 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",fee_Type=").append(fee_Type)));
/* 265 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",fee_Code=").append(fee_Code)));
/* 266 */     if (valid_Time != null) {
/* 267 */       this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",valid_Time=").append(dateFormat.format(valid_Time))));
/*     */     }
/*     */     else {
/* 270 */       this.outStr = String.valueOf(String.valueOf(this.outStr)).concat(",valid_Time=null");
/*     */     }
/* 272 */     if (at_Time != null) {
/* 273 */       this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",at_Time=").append(dateFormat.format(at_Time))));
/*     */     }
/*     */     else {
/* 276 */       this.outStr = String.valueOf(String.valueOf(this.outStr)).concat(",at_Time=null");
/*     */     }
/* 278 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",src_Terminal_Id=").append(src_Terminal_Id)));
/* 279 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",destusr_Tl=").append(dest_Terminal_Id.length)));
/* 280 */     for (int t = 0; t < dest_Terminal_Id.length; ++t)
/*     */     {
/* 282 */       this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",dest_Terminal_Id[").append(t).append("]=").append(dest_Terminal_Id[t])));
/*     */     }
/* 284 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Length=").append(msg_Content.length)));
/* 285 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Content=").append(new String(msg_Content))));
/* 286 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",reserve=").append(reserve)));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 293 */     String tmpStr = "CMPP_Submit: ";
/* 294 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 295 */     tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
/* 296 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getCommandId()
/*     */   {
/* 303 */     return 4;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPSubmitMessage
 * JD-Core Version:    0.5.3
 */