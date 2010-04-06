/*    */ package com.huawei.insa2.comm.cmpp.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class CMPPQueryMessage extends CMPPMessage
/*    */ {
/*    */   private String outStr;
/*    */ 
/*    */   public CMPPQueryMessage(Date time, int query_Type, String query_Code, String reserve)
/*    */     throws IllegalArgumentException
/*    */   {
/* 34 */     if ((query_Type != 0) && (query_Type != 1)) {
/* 35 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.QUERY_INPUT_ERROR))).append(":query_Type").append(CMPPConstant.VALUE_ERROR))));
/*    */     }
/* 37 */     if (query_Code.length() > 10) {
/* 38 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.QUERY_INPUT_ERROR))).append(":query_Code").append(CMPPConstant.STRING_LENGTH_GREAT).append("10"))));
/*    */     }
/* 40 */     if (reserve.length() > 8) {
/* 41 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.QUERY_INPUT_ERROR))).append(":reserve").append(CMPPConstant.STRING_LENGTH_GREAT).append("8"))));
/*    */     }
/*    */ 
/* 44 */     int len = 39;
/*    */ 
/* 46 */     this.buf = new byte[len];
/*    */ 
/* 49 */     TypeConvert.int2byte(len, this.buf, 0);
/* 50 */     TypeConvert.int2byte(6, this.buf, 4);
/*    */ 
/* 55 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
/*    */ 
/* 57 */     System.arraycopy(dateFormat.format(time).getBytes(), 0, this.buf, 12, dateFormat.format(time).length());
/*    */ 
/* 59 */     this.buf[20] = (byte)query_Type;
/*    */ 
/* 61 */     System.arraycopy(query_Code.getBytes(), 0, this.buf, 21, query_Code.length());
/*    */ 
/* 63 */     System.arraycopy(reserve.getBytes(), 0, this.buf, 31, reserve.length());
/*    */ 
/* 66 */     this.outStr = ",time=".concat(String.valueOf(String.valueOf(dateFormat.format(time))));
/* 67 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",query_Type=").append(query_Type)));
/* 68 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",query_Code=").append(query_Code)));
/* 69 */     this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",reserve=").append(reserve)));
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 76 */     String tmpStr = "CMPP_Query: ";
/* 77 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 78 */     tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
/* 79 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 86 */     return 6;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.message.CMPPQueryMessage
 * JD-Core Version:    0.5.3
 */