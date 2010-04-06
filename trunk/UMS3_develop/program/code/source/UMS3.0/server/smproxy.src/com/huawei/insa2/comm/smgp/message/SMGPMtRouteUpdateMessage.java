/*     */ package com.huawei.insa2.comm.smgp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ 
/*     */ public class SMGPMtRouteUpdateMessage extends SMGPMessage
/*     */ {
/*     */   private StringBuffer strBuf;
/*     */ 
/*     */   public SMGPMtRouteUpdateMessage(int updateType, int routeId, String srcGatewayId, String srcGatewayIP, int srcGatewayPort, String startTermId, String endTermId, String areaCode)
/*     */     throws IllegalArgumentException
/*     */   {
/*  43 */     if ((updateType < 0) || (updateType > 255)) {
/*  44 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MTROUTEUPDATE_INPUT_ERROR))).append(":UpdateType ").append(SMGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  48 */     if (srcGatewayId == null)
/*     */     {
/*  50 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MTROUTEUPDATE_INPUT_ERROR))).append(":SrcGatewayId ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  54 */     if (srcGatewayId.length() > 6)
/*     */     {
/*  56 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MTROUTEUPDATE_INPUT_ERROR))).append(":SrcGatewayId ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/*  60 */     if (srcGatewayIP == null)
/*     */     {
/*  62 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MTROUTEUPDATE_INPUT_ERROR))).append(":SrcGatewayIP ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  66 */     if (srcGatewayIP.length() > 15)
/*     */     {
/*  68 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MTROUTEUPDATE_INPUT_ERROR))).append(":SrcGatewayIP ").append(SMGPConstant.STRING_LENGTH_GREAT).append("15"))));
/*     */     }
/*     */ 
/*  72 */     if (startTermId == null)
/*     */     {
/*  74 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MTROUTEUPDATE_INPUT_ERROR))).append(":StartTermId ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  78 */     if (startTermId.length() > 6)
/*     */     {
/*  80 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MTROUTEUPDATE_INPUT_ERROR))).append(":StartTermId ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/*  84 */     if (endTermId == null)
/*     */     {
/*  86 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MTROUTEUPDATE_INPUT_ERROR))).append(":EndTermId ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  90 */     if (endTermId.length() > 6)
/*     */     {
/*  92 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MTROUTEUPDATE_INPUT_ERROR))).append(":EndTermId ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/*  96 */     if (areaCode == null)
/*     */     {
/*  98 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MTROUTEUPDATE_INPUT_ERROR))).append(":AreaCode ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/* 102 */     if (areaCode.length() > 4)
/*     */     {
/* 104 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MTROUTEUPDATE_INPUT_ERROR))).append(":AreaCode ").append(SMGPConstant.STRING_LENGTH_GREAT).append("4"))));
/*     */     }
/*     */ 
/* 109 */     int len = 56;
/*     */ 
/* 111 */     this.buf = new byte[len];
/*     */ 
/* 115 */     TypeConvert.int2byte(len, this.buf, 0);
/*     */ 
/* 117 */     TypeConvert.int2byte(8, this.buf, 4);
/*     */ 
/* 121 */     this.buf[12] = (byte)updateType;
/*     */ 
/* 123 */     TypeConvert.int2byte(routeId, this.buf, 13);
/*     */ 
/* 125 */     System.arraycopy(srcGatewayId.getBytes(), 0, this.buf, 17, srcGatewayId.length());
/*     */ 
/* 127 */     System.arraycopy(srcGatewayIP.getBytes(), 0, this.buf, 23, srcGatewayIP.length());
/*     */ 
/* 129 */     TypeConvert.short2byte(srcGatewayPort, this.buf, 38);
/*     */ 
/* 131 */     System.arraycopy(startTermId.getBytes(), 0, this.buf, 40, startTermId.length());
/*     */ 
/* 133 */     System.arraycopy(endTermId.getBytes(), 0, this.buf, 46, endTermId.length());
/*     */ 
/* 135 */     System.arraycopy(areaCode.getBytes(), 0, this.buf, 52, areaCode.length());
/*     */ 
/* 138 */     this.strBuf = new StringBuffer(300);
/* 139 */     this.strBuf.append(",updateType=".concat(String.valueOf(String.valueOf(updateType))));
/* 140 */     this.strBuf.append(",RouteID=".concat(String.valueOf(String.valueOf(routeId))));
/* 141 */     this.strBuf.append(",SrcGatewayID=".concat(String.valueOf(String.valueOf(srcGatewayId))));
/* 142 */     this.strBuf.append(",SrcGatewayIP=".concat(String.valueOf(String.valueOf(srcGatewayIP))));
/* 143 */     this.strBuf.append(",SrcGatewayPort=".concat(String.valueOf(String.valueOf(srcGatewayPort))));
/* 144 */     this.strBuf.append(",StartTermId=".concat(String.valueOf(String.valueOf(startTermId))));
/* 145 */     this.strBuf.append(",EndTermId=".concat(String.valueOf(String.valueOf(endTermId))));
/* 146 */     this.strBuf.append(",AreaCode=".concat(String.valueOf(String.valueOf(areaCode))));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 153 */     StringBuffer outStr = new StringBuffer(300);
/* 154 */     outStr.append("SMGPMtRouteUpdateMessage:");
/* 155 */     outStr.append("PacketLength=".concat(String.valueOf(String.valueOf(this.buf.length))));
/* 156 */     outStr.append(",RequestID=8");
/* 157 */     outStr.append("Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 158 */     if (this.strBuf != null)
/*     */     {
/* 160 */       outStr.append(this.strBuf.toString());
/*     */     }
/* 162 */     return outStr.toString();
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/* 169 */     return 8;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPMtRouteUpdateMessage
 * JD-Core Version:    0.5.3
 */