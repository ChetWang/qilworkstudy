/*    */ package com.huawei.insa2.comm.sgip.message;
/*    */ 
/*    */ import com.huawei.insa2.comm.sgip.SGIPConstant;
/*    */ import com.huawei.insa2.util.TypeConvert;
/*    */ 
/*    */ public class SGIPUserReportMessage extends SGIPMessage
/*    */ {
/*    */   public SGIPUserReportMessage(byte[] buf)
/*    */     throws IllegalArgumentException
/*    */   {
/* 23 */     this.buf = new byte[63];
/* 24 */     if (buf.length != 63) {
/* 25 */       throw new IllegalArgumentException(SGIPConstant.SMC_MESSAGE_ERROR);
/*    */     }
/* 27 */     System.arraycopy(buf, 0, this.buf, 0, 63);
/*    */ 
/* 29 */     this.src_node_Id = TypeConvert.byte2int(this.buf, 0);
/* 30 */     this.time_Stamp = TypeConvert.byte2int(this.buf, 4);
/* 31 */     this.sequence_Id = TypeConvert.byte2int(this.buf, 8);
/*    */   }
/*    */ 
/*    */   public String getSPNumber()
/*    */   {
/* 38 */     byte[] tmpId = new byte[21];
/* 39 */     System.arraycopy(this.buf, 12, tmpId, 0, 21);
/*    */ 
/* 41 */     String tmpStr = new String(tmpId).trim();
/* 42 */     if (tmpStr.indexOf(0) >= 0) {
/* 43 */       return tmpStr.substring(0, tmpStr.indexOf(0));
/*    */     }
/* 45 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public String getUserNumber()
/*    */   {
/* 52 */     byte[] tmpId = new byte[21];
/* 53 */     System.arraycopy(this.buf, 33, tmpId, 0, 21);
/*    */ 
/* 55 */     String tmpStr = new String(tmpId).trim();
/* 56 */     if (tmpStr.indexOf(0) >= 0) {
/* 57 */       return tmpStr.substring(0, tmpStr.indexOf(0));
/*    */     }
/* 59 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getUserCondition()
/*    */   {
/* 66 */     int tmpId = this.buf[54];
/* 67 */     return tmpId;
/*    */   }
/*    */ 
/*    */   public String getReserve()
/*    */   {
/* 74 */     byte[] tmpReserve = new byte[8];
/* 75 */     System.arraycopy(this.buf, 48, tmpReserve, 0, 8);
/* 76 */     return new String(tmpReserve).trim();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 83 */     String tmpStr = "SGIP_USERREPORT: ";
/* 84 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append("Sequence_Id=").append(super.getSequenceId())));
/* 85 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",SPNumber=").append(getSPNumber())));
/* 86 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",UserNumber=").append(getUserNumber())));
/* 87 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",UserCondition=").append(getUserCondition())));
/* 88 */     tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Reserve=").append(getReserve())));
/* 89 */     return tmpStr;
/*    */   }
/*    */ 
/*    */   public int getCommandId()
/*    */   {
/* 96 */     return 17;
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.message.SGIPUserReportMessage
 * JD-Core Version:    0.5.3
 */