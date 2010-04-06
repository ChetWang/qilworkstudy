/*     */ package com.huawei.insa2.comm.smgp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.smgp.SMGPConstant;
/*     */ import com.huawei.insa2.util.TypeConvert;
/*     */ 
/*     */ public class SMGPMoRouteUpdateMessage extends SMGPMessage
/*     */ {
/*     */   private StringBuffer strBuf;
/*     */ 
/*     */   public SMGPMoRouteUpdateMessage(int updateType, int routeId, String srcGatewayId, String srcGatewayIP, int srcGatewayPort, String srcTermId)
/*     */     throws IllegalArgumentException
/*     */   {
/*  41 */     if ((updateType < 0) || (updateType > 255)) {
/*  42 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MOROUTEUPDATE_INPUT_ERROR))).append(":UpdateType ").append(SMGPConstant.INT_SCOPE_ERROR))));
/*     */     }
/*     */ 
/*  46 */     if (srcGatewayId == null)
/*     */     {
/*  48 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MOROUTEUPDATE_INPUT_ERROR))).append(":SrcGatewayId ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  52 */     if (srcGatewayId.length() > 6)
/*     */     {
/*  54 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MOROUTEUPDATE_INPUT_ERROR))).append(":SrcGatewayId ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
/*     */     }
/*     */ 
/*  58 */     if (srcGatewayIP == null)
/*     */     {
/*  60 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MOROUTEUPDATE_INPUT_ERROR))).append(":SrcGatewayIP ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  64 */     if (srcGatewayIP.length() > 15)
/*     */     {
/*  66 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MOROUTEUPDATE_INPUT_ERROR))).append(":SrcGatewayIP ").append(SMGPConstant.STRING_LENGTH_GREAT).append("15"))));
/*     */     }
/*     */ 
/*  70 */     if (srcTermId == null)
/*     */     {
/*  72 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MOROUTEUPDATE_INPUT_ERROR))).append(":SrcTermId ").append(SMGPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  76 */     if (srcTermId.length() > 21)
/*     */     {
/*  78 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.MOROUTEUPDATE_INPUT_ERROR))).append(":SrcTermId ").append(SMGPConstant.STRING_LENGTH_GREAT).append("21"))));
/*     */     }
/*     */ 
/*  83 */     int len = 61;
/*     */ 
/*  85 */     this.buf = new byte[len];
/*     */ 
/*  89 */     TypeConvert.int2byte(len, this.buf, 0);
/*     */ 
/*  91 */     TypeConvert.int2byte(8, this.buf, 4);
/*     */ 
/*  95 */     this.buf[12] = (byte)updateType;
/*     */ 
/*  97 */     TypeConvert.int2byte(routeId, this.buf, 13);
/*     */ 
/*  99 */     System.arraycopy(srcGatewayId.getBytes(), 0, this.buf, 17, srcGatewayId.length());
/*     */ 
/* 101 */     System.arraycopy(srcGatewayIP.getBytes(), 0, this.buf, 23, srcGatewayIP.length());
/*     */ 
/* 103 */     TypeConvert.short2byte(srcGatewayPort, this.buf, 38);
/*     */ 
/* 105 */     System.arraycopy(srcTermId.getBytes(), 0, this.buf, 40, srcTermId.length());
/*     */ 
/* 108 */     this.strBuf = new StringBuffer(300);
/* 109 */     this.strBuf.append(",updateType=".concat(String.valueOf(String.valueOf(updateType))));
/* 110 */     this.strBuf.append(",RouteID=".concat(String.valueOf(String.valueOf(routeId))));
/* 111 */     this.strBuf.append(",SrcGatewayID=".concat(String.valueOf(String.valueOf(srcGatewayId))));
/* 112 */     this.strBuf.append(",SrcGatewayIP=".concat(String.valueOf(String.valueOf(srcGatewayIP))));
/* 113 */     this.strBuf.append(",SrcGatewayPort=".concat(String.valueOf(String.valueOf(srcGatewayPort))));
/* 114 */     this.strBuf.append(",SrcTermId=".concat(String.valueOf(String.valueOf(srcTermId))));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 121 */     StringBuffer outStr = new StringBuffer(300);
/* 122 */     outStr.append("SMGPMtRouteUpdateMessage:");
/* 123 */     outStr.append("PacketLength=".concat(String.valueOf(String.valueOf(this.buf.length))));
/* 124 */     outStr.append(",RequestID=9");
/* 125 */     outStr.append(",Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 126 */     if (this.strBuf != null)
/*     */     {
/* 128 */       outStr.append(this.strBuf.toString());
/*     */     }
/* 130 */     return outStr.toString();
/*     */   }
/*     */ 
/*     */   public int getRequestId()
/*     */   {
/* 137 */     return 9;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.message.SMGPMoRouteUpdateMessage
 * JD-Core Version:    0.5.3
 */