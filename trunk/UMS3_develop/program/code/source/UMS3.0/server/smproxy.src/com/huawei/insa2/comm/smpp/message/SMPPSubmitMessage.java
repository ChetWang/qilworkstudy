/*     */ package com.huawei.insa2.comm.smpp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.smpp.SMPPConstant;
/*     */ 
/*     */ public class SMPPSubmitMessage extends SMPPMessage
/*     */ {
/*     */   private StringBuffer strBuf;
/*     */ 
/*     */   public SMPPSubmitMessage(String serviceType, byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr, byte destAddrTon, byte destAddrNpi, String destinationAddr, byte esmClass, byte protocolId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod, byte registeredDelivery, byte replaceIfPresentFlag, byte dataCoding, byte smDefaultMsgId, byte smLength, String shortMessage)
/*     */     throws IllegalArgumentException
/*     */   {
/*  68 */     if (serviceType.length() > 5)
/*     */     {
/*  70 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR))).append(":serviceType ").append(SMPPConstant.STRING_LENGTH_GREAT).append("5"))));
/*     */     }
/*     */ 
/*  75 */     if (sourceAddr.length() > 20)
/*     */     {
/*  77 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR))).append(":sourceAddr ").append(SMPPConstant.STRING_LENGTH_GREAT).append("20"))));
/*     */     }
/*     */ 
/*  81 */     if (destinationAddr == null)
/*     */     {
/*  83 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR))).append(":destinationAddr ").append(SMPPConstant.STRING_NULL))));
/*     */     }
/*     */ 
/*  87 */     if (destinationAddr.length() > 20)
/*     */     {
/*  89 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR))).append(":destinationAddr ").append(SMPPConstant.STRING_LENGTH_GREAT).append("20"))));
/*     */     }
/*     */ 
/*  93 */     if ((scheduleDeliveryTime.length() != 0) && (scheduleDeliveryTime.length() != 16))
/*     */     {
/*  95 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR))).append(":scheduleDeliveryTime ").append(SMPPConstant.STRING_LENGTH_NOTEQUAL).append("16"))));
/*     */     }
/*     */ 
/*  99 */     if ((validityPeriod.length() != 0) && (validityPeriod.length() != 16))
/*     */     {
/* 101 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR))).append(":validityPeriod ").append(SMPPConstant.STRING_LENGTH_NOTEQUAL).append("16"))));
/*     */     }
/*     */ 
/* 105 */     if (smLength > 254)
/*     */     {
/* 107 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR))).append(":smLength ").append(SMPPConstant.STRING_LENGTH_GREAT).append("254"))));
/*     */     }
/*     */ 
/* 111 */     if (shortMessage.length() > 254)
/*     */     {
/* 113 */       throw new IllegalArgumentException(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR))).append(":shortMessage ").append(SMPPConstant.STRING_LENGTH_GREAT).append("254"))));
/*     */     }
/*     */ 
/* 120 */     int len = 33 + serviceType.length() + sourceAddr.length() + destinationAddr.length() + scheduleDeliveryTime.length() + validityPeriod.length() + smLength;
/*     */ 
/* 123 */     this.buf = new byte[len];
/*     */ 
/* 126 */     super.setMsgLength(len);
/* 127 */     super.setCommandId(4);
/*     */ 
/* 129 */     super.setStatus(0);
/*     */ 
/* 133 */     int pos = 16;
/*     */ 
/* 135 */     System.arraycopy(serviceType.getBytes(), 0, this.buf, pos, serviceType.length());
/*     */ 
/* 137 */     pos = pos + serviceType.length() + 1;
/* 138 */     this.buf[pos] = sourceAddrTon;
/*     */ 
/* 140 */     pos += 1;
/* 141 */     this.buf[pos] = sourceAddrNpi;
/*     */ 
/* 143 */     pos += 1;
/* 144 */     System.arraycopy(sourceAddr.getBytes(), 0, this.buf, pos, sourceAddr.length());
/*     */ 
/* 146 */     pos = pos + sourceAddr.length() + 1;
/* 147 */     this.buf[pos] = destAddrTon;
/*     */ 
/* 149 */     pos += 1;
/* 150 */     this.buf[pos] = destAddrNpi;
/*     */ 
/* 152 */     pos += 1;
/* 153 */     System.arraycopy(destinationAddr.getBytes(), 0, this.buf, pos, destinationAddr.length());
/*     */ 
/* 155 */     pos = pos + destinationAddr.length() + 1;
/* 156 */     this.buf[pos] = esmClass;
/*     */ 
/* 158 */     pos += 1;
/* 159 */     this.buf[pos] = protocolId;
/*     */ 
/* 161 */     pos += 1;
/* 162 */     this.buf[pos] = priorityFlag;
/*     */ 
/* 164 */     pos += 1;
/* 165 */     System.arraycopy(scheduleDeliveryTime.getBytes(), 0, this.buf, pos, scheduleDeliveryTime.length());
/*     */ 
/* 167 */     pos = pos + scheduleDeliveryTime.length() + 1;
/* 168 */     System.arraycopy(validityPeriod.getBytes(), 0, this.buf, pos, validityPeriod.length());
/*     */ 
/* 170 */     pos += 1;
/* 171 */     this.buf[pos] = registeredDelivery;
/*     */ 
/* 173 */     pos += 1;
/* 174 */     this.buf[pos] = replaceIfPresentFlag;
/*     */ 
/* 176 */     pos += 1;
/* 177 */     this.buf[pos] = dataCoding;
/*     */ 
/* 179 */     pos += 1;
/* 180 */     this.buf[pos] = smDefaultMsgId;
/*     */ 
/* 182 */     pos += 1;
/* 183 */     this.buf[pos] = smLength;
/*     */ 
/* 185 */     pos += 1;
/* 186 */     System.arraycopy(shortMessage.getBytes(), 0, this.buf, pos, smLength);
/*     */ 
/* 189 */     this.strBuf = new StringBuffer(600);
/* 190 */     this.strBuf.append(",serviceType=".concat(String.valueOf(String.valueOf(serviceType))));
/* 191 */     this.strBuf.append(",sourceAddrTon=".concat(String.valueOf(String.valueOf(sourceAddrTon))));
/* 192 */     this.strBuf.append(",sourceAddrNpi=".concat(String.valueOf(String.valueOf(sourceAddrNpi))));
/* 193 */     this.strBuf.append(",sourceAddr=".concat(String.valueOf(String.valueOf(sourceAddr))));
/* 194 */     this.strBuf.append(",destAddrTon=".concat(String.valueOf(String.valueOf(destAddrTon))));
/* 195 */     this.strBuf.append(",destAddrNpi=".concat(String.valueOf(String.valueOf(destAddrNpi))));
/* 196 */     this.strBuf.append(",destinationAddr=".concat(String.valueOf(String.valueOf(destinationAddr))));
/* 197 */     this.strBuf.append(",esmClass=".concat(String.valueOf(String.valueOf(esmClass))));
/* 198 */     this.strBuf.append(",protocolId=".concat(String.valueOf(String.valueOf(protocolId))));
/* 199 */     this.strBuf.append(",priorityFlag=".concat(String.valueOf(String.valueOf(priorityFlag))));
/* 200 */     this.strBuf.append(",scheduleDeliveryTime=".concat(String.valueOf(String.valueOf(scheduleDeliveryTime))));
/* 201 */     this.strBuf.append(",validityPeriod=".concat(String.valueOf(String.valueOf(validityPeriod))));
/* 202 */     this.strBuf.append(",registeredDelivery=".concat(String.valueOf(String.valueOf(registeredDelivery))));
/* 203 */     this.strBuf.append(",replaceIfPresentFlag=".concat(String.valueOf(String.valueOf(replaceIfPresentFlag))));
/* 204 */     this.strBuf.append(",dataCoding=".concat(String.valueOf(String.valueOf(dataCoding))));
/* 205 */     this.strBuf.append(",smDefaultMsgId=".concat(String.valueOf(String.valueOf(smDefaultMsgId))));
/* 206 */     this.strBuf.append(",smLength=".concat(String.valueOf(String.valueOf(smLength))));
/* 207 */     this.strBuf.append(",shortMessage=".concat(String.valueOf(String.valueOf(shortMessage))));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 215 */     StringBuffer outBuf = new StringBuffer(600);
/* 216 */     outBuf.append("SMPPSubmitMessage: ");
/* 217 */     outBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 218 */     outBuf.append(",CommandID=".concat(String.valueOf(String.valueOf(super.getCommandId()))));
/* 219 */     outBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 220 */     outBuf.append(",SequenceID=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 221 */     if (this.strBuf != null)
/*     */     {
/* 223 */       outBuf.append(this.strBuf.toString());
/*     */     }
/* 225 */     return outBuf.toString();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.message.SMPPSubmitMessage
 * JD-Core Version:    0.5.3
 */