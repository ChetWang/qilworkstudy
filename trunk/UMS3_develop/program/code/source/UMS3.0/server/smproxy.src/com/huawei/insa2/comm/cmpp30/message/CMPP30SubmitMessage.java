/*     */ package com.huawei.insa2.comm.cmpp30.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class CMPP30SubmitMessage extends CMPPMessage
/*     */ {
/*     */   private String outStr;
/*     */ 
/*     */   public CMPP30SubmitMessage(int pk_Total, int pk_Number, int registered_Delivery, int msg_Level, String service_Id, int fee_UserType, String fee_Terminal_Id, int fee_Terminal_Type, int tp_Pid, int tp_Udhi, int msg_Fmt, String msg_Src, String fee_Type, String fee_Code, Date valid_Time, Date at_Time, String src_Terminal_Id, String[] dest_Terminal_Id, int dest_Terminal_Type, byte[] msg_Content, String LinkID)
/*     */     throws IllegalArgumentException
/*     */   {
/*  82 */     if ((pk_Total < 1) || (pk_Total > 255))
/*     */     {
/*  84 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":").append(CMPPConstant.PK_TOTAL_ERROR))));
/*     */     }
/*     */ 
/*  88 */     if ((pk_Number < 1) || (pk_Number > 255))
/*     */     {
/*  90 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":").append(CMPPConstant.PK_NUMBER_ERROR))));
/*     */     }
/*     */ 
/*  95 */     if ((registered_Delivery < 0) || (registered_Delivery > 1))
/*     */     {
/*  97 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":").append(CMPPConstant.REGISTERED_DELIVERY_ERROR))));
/*     */     }
/*     */ 
/* 101 */     if ((msg_Level < 0) || (msg_Level > 255))
/*     */     {
/* 103 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":msg_Level").append(CMPPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 107 */     if (service_Id.length() > 10)
/*     */     {
/* 109 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":service_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("10"))));
/*     */     }
/*     */ 
/* 113 */     if ((fee_UserType < 0) || (fee_UserType > 3))
/*     */     {
/* 115 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":").append(CMPPConstant.FEE_USERTYPE_ERROR))));
/*     */     }
/*     */ 
/* 127 */     if (fee_Terminal_Id.length() > 32)
/*     */     {
/* 129 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":fee_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("32"))));
/*     */     }
/*     */ 
/* 135 */     if ((fee_Terminal_Type < 0) || (fee_Terminal_Type > 255))
/*     */     {
/* 137 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":fee_terminal_type").append(CMPPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 142 */     if ((tp_Pid < 0) || (tp_Pid > 255))
/*     */     {
/* 144 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":tp_Pid").append(CMPPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 148 */     if ((tp_Udhi < 0) || (tp_Udhi > 255))
/*     */     {
/* 150 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":tp_Udhi").append(CMPPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 154 */     if ((msg_Fmt < 0) || (msg_Fmt > 255))
/*     */     {
/* 156 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":msg_Fmt").append(CMPPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 160 */     if (msg_Src.length() > 6)
/*     */     {
/* 162 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":msg_Src").append(CMPPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 166 */     if (fee_Type.length() > 2)
/*     */     {
/* 168 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":fee_Type").append(CMPPConstant.STRING_LENGTH_GREAT).append("2"))));
/*     */     }
/*     */ 
/* 172 */     if (fee_Code.length() > 6)
/*     */     {
/* 174 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":fee_Code").append(CMPPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/* 178 */     if (src_Terminal_Id.length() > 21)
/*     */     {
/* 180 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":src_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/* 184 */     if (dest_Terminal_Id.length > 100)
/*     */     {
/* 186 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":dest_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("100"))));
/*     */     }
/*     */ 
/* 201 */     for (int i = 0; i < dest_Terminal_Id.length; ++i)
/*     */     {
/* 203 */       if (dest_Terminal_Id[i].length() <= 32)
/*     */         continue;
/* 205 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":dest_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("32"))));
/*     */     }
/*     */ 
/* 212 */     if ((dest_Terminal_Type < 0) || (dest_Terminal_Type > 255))
/*     */     {
/* 214 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":dest_terminal_type").append(CMPPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/* 219 */     if (msg_Fmt == 0)
/*     */     {
/* 221 */       if (msg_Content.length > 160)
/*     */       {
/* 223 */         throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":msg_Content").append(CMPPConstant.STRING_LENGTH_GREAT).append("160"))));
/*     */       }
/*     */ 
/*     */     }
/* 229 */     else if (msg_Content.length > 140)
/*     */     {
/* 231 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":msg_Content").append(CMPPConstant.STRING_LENGTH_GREAT).append("140"))));
/*     */     }
/*     */ 
/* 243 */     if (LinkID.length() > 20)
/*     */     {
/* 245 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))).append(":LinkID").append(CMPPConstant.STRING_LENGTH_GREAT).append("20"))));
/*     */     }
/*     */ 
/* 255 */     int len = 163 + 32 * dest_Terminal_Id.length + msg_Content.length;
/*     */ 
/* 257 */     this.buf = new byte[len];
/*     */ 
/* 260 */     TypeConvert.int2byte(len, this.buf, 0);
/* 261 */     TypeConvert.int2byte(4, this.buf, 4);
/*     */ 
/* 267 */     this.buf[20] = (byte)pk_Total;
/* 268 */     this.buf[21] = (byte)pk_Number;
/* 269 */     this.buf[22] = (byte)registered_Delivery;
/* 270 */     this.buf[23] = (byte)msg_Level;
/* 271 */     System.arraycopy(service_Id.getBytes(), 0, this.buf, 24, service_Id.length());
/* 272 */     this.buf[34] = (byte)fee_UserType;
/* 273 */     System.arraycopy(fee_Terminal_Id.getBytes(), 0, this.buf, 35, fee_Terminal_Id.length());
/*     */ 
/* 275 */     this.buf[67] = (byte)fee_Terminal_Type;
/*     */ 
/* 278 */     this.buf[68] = (byte)tp_Pid;
/* 279 */     this.buf[69] = (byte)tp_Udhi;
/* 280 */     this.buf[70] = (byte)msg_Fmt;
/*     */ 
/* 282 */     System.arraycopy(msg_Src.getBytes(), 0, this.buf, 71, msg_Src.length());
/*     */ 
/* 284 */     System.arraycopy(fee_Type.getBytes(), 0, this.buf, 77, fee_Type.length());
/*     */ 
/* 286 */     System.arraycopy(fee_Code.getBytes(), 0, this.buf, 79, fee_Code.length());
/*     */ 
/* 290 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
/*     */     String tmpTime;
/* 292 */     if (valid_Time != null) {
/* 293 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(valid_Time))).concat("032+");
/*     */ 
/* 295 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, 85, 16);
/*     */     }
/*     */ 
/* 300 */     if (at_Time != null) {
/* 301 */       tmpTime = String.valueOf(String.valueOf(dateFormat.format(at_Time))).concat("032+");
/*     */ 
/* 303 */       System.arraycopy(tmpTime.getBytes(), 0, this.buf, 102, 16);
/*     */     }
/*     */ 
/* 308 */     System.arraycopy(src_Terminal_Id.getBytes(), 0, this.buf, 119, src_Terminal_Id.length());
/*     */ 
/* 311 */     this.buf[140] = (byte)dest_Terminal_Id.length;
/*     */ 
/* 313 */     int i = 0;
/* 314 */     for (i = 0; i < dest_Terminal_Id.length; ++i)
/*     */     {
/* 317 */       System.arraycopy(dest_Terminal_Id[i].getBytes(), 0, this.buf, 141 + i * 32, dest_Terminal_Id[i].length());
/*     */     }
/*     */ 
/* 321 */     int loc = 141 + i * 32;
/*     */ 
/* 323 */     this.buf[loc] = (byte)dest_Terminal_Type;
/* 324 */     loc += 1;
/*     */ 
/* 326 */     this.buf[loc] = (byte)msg_Content.length;
/* 327 */     loc += 1;
/* 328 */     System.arraycopy(msg_Content, 0, this.buf, loc, msg_Content.length);
/*     */ 
/* 330 */     loc += msg_Content.length;
/* 331 */     System.arraycopy(LinkID.getBytes(), 0, this.buf, loc, LinkID.length());
/*     */ 
/* 341 */     this.outStr = ",msg_id=00000000";
/* 342 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",pk_Total=").append(pk_Total)));
/* 343 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",pk_Number=").append(pk_Number)));
/* 344 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",registered_Delivery=").append(registered_Delivery)));
/* 345 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Level=").append(msg_Level)));
/* 346 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",service_Id=").append(service_Id)));
/* 347 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",fee_UserType=").append(fee_UserType)));
/* 348 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",fee_Terminal_Id=").append(fee_Terminal_Id)));
/*     */ 
/* 350 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",fee_Terminal_type=").append(fee_Terminal_Type)));
/*     */ 
/* 352 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",tp_Pid=").append(tp_Pid)));
/* 353 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",tp_Udhi=").append(tp_Udhi)));
/* 354 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Fmt=").append(msg_Fmt)));
/* 355 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Src=").append(msg_Src)));
/* 356 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",fee_Type=").append(fee_Type)));
/* 357 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",fee_Code=").append(fee_Code)));
/* 358 */     if (valid_Time != null) {
/* 359 */       this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",valid_Time=").append(dateFormat.format(valid_Time))));
/*     */     }
/*     */     else {
/* 362 */       this.outStr = String.valueOf(String.valueOf(this.outStr)).concat(",valid_Time=null");
/*     */     }
/* 364 */     if (at_Time != null) {
/* 365 */       this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",at_Time=").append(dateFormat.format(at_Time))));
/*     */     }
/*     */     else {
/* 368 */       this.outStr = String.valueOf(String.valueOf(this.outStr)).concat(",at_Time=null");
/*     */     }
/* 370 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",src_Terminal_Id=").append(src_Terminal_Id)));
/* 371 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",destusr_Tl=").append(dest_Terminal_Id.length)));
/* 372 */     for (int t = 0; t < dest_Terminal_Id.length; ++t)
/*     */     {
/* 374 */       this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",dest_Terminal_Id[").append(t).append("]=").append(dest_Terminal_Id[t])));
/*     */     }
/*     */ 
/* 377 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",dest_Terminal_type=").append(dest_Terminal_Type)));
/*     */ 
/* 379 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Length=").append(msg_Content.length)));
/* 380 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Content=").append(new String(msg_Content))));
/*     */ 
/* 384 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",LinkID=").append(LinkID)));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 391 */     String tmpStr = "CMPP_Submit: ";
/* 392 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 393 */     tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
/* 394 */     return tmpStr;
/*     */   }
/*     */ 
/*     */   public int getCommandId()
/*     */   {
/* 401 */     return 4;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp30.message.CMPP30SubmitMessage
 * JD-Core Version:    0.5.3
 */