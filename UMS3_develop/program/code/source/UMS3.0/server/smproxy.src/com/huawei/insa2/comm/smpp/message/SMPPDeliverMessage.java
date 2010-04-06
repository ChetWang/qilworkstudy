/*     */ package com.huawei.insa2.comm.smpp.message;
/*     */ 
/*     */ import com.huawei.insa2.comm.smpp.SMPPConstant;
/*     */ 
/*     */ public class SMPPDeliverMessage extends SMPPMessage
/*     */ {
/*     */   private String serviceType;
/*     */   private int sourceAddrTon;
/*     */   private int sourceAddrNpi;
/*     */   private String sourceAddr;
/*     */   private int destAddrTon;
/*     */   private int destAddrNpi;
/*     */   private String destinationAddr;
/*     */   private int esmClass;
/*     */   private int protocolId;
/*     */   private int priorityFlag;
/*     */   private int registeredDelivery;
/*     */   private int dataCoding;
/*     */   private int smLength;
/*     */   private String shortMessage;
/*     */ 
/*     */   public SMPPDeliverMessage(byte[] buf)
/*     */     throws IllegalArgumentException
/*     */   {
/*  39 */     int len = buf.length;
/*  40 */     if ((buf.length > 332) || (buf.length < 33)) {
/*  41 */       throw new IllegalArgumentException(SMPPConstant.SMC_MESSAGE_ERROR);
/*     */     }
/*  43 */     this.buf = new byte[len];
/*  44 */     System.arraycopy(buf, 0, this.buf, 0, buf.length);
/*     */ 
/*  49 */     int pos = 16;
/*  50 */     this.serviceType = getFirstStr(buf, pos);
/*     */ 
/*  52 */     pos = pos + this.serviceType.length() + 1;
/*  53 */     this.sourceAddrTon = buf[pos];
/*  54 */     this.sourceAddrNpi = buf[(pos + 1)];
/*     */ 
/*  56 */     pos += 2;
/*  57 */     this.sourceAddr = getFirstStr(buf, pos);
/*     */ 
/*  59 */     pos = pos + this.sourceAddr.length() + 1;
/*  60 */     this.destAddrTon = buf[pos];
/*  61 */     this.destAddrNpi = buf[(pos + 1)];
/*     */ 
/*  63 */     pos += 2;
/*  64 */     this.destinationAddr = getFirstStr(buf, pos);
/*     */ 
/*  66 */     pos = pos + this.destinationAddr.length() + 1;
/*  67 */     this.esmClass = buf[pos];
/*  68 */     this.protocolId = buf[(pos + 1)];
/*  69 */     this.priorityFlag = buf[(pos + 2)];
/*  70 */     this.registeredDelivery = buf[(pos + 5)];
/*  71 */     this.dataCoding = buf[(pos + 7)];
/*  72 */     this.smLength = buf[(pos + 9)];
/*     */ 
/*  74 */     byte[] tmpBuf = new byte[this.smLength];
/*  75 */     System.arraycopy(buf, pos + 10, tmpBuf, 0, this.smLength);
/*  76 */     this.shortMessage = new String(tmpBuf);
/*     */   }
/*     */ 
/*     */   private String getFirstStr(byte[] buf, int sPos)
/*     */   {
/*  83 */     int deli = 0;
/*     */ 
/*  86 */     byte[] tmpBuf = new byte[21];
/*  87 */     int pos = sPos;
/*  88 */     while ((buf[pos] != 0) && (pos < buf.length)) {
/*  89 */       tmpBuf[(pos - sPos)] = buf[pos];
/*  90 */       pos += 1;
/*     */     }
/*  92 */     if (pos == sPos) {
/*  93 */       return "";
/*     */     }
/*     */ 
/*  96 */     String tmpStr = new String(tmpBuf);
/*  97 */     return tmpStr.substring(0, pos - sPos);
/*     */   }
/*     */ 
/*     */   public String getServiceType()
/*     */   {
/* 105 */     return this.serviceType;
/*     */   }
/*     */ 
/*     */   public int getSourceAddrTon()
/*     */   {
/* 112 */     return this.sourceAddrTon;
/*     */   }
/*     */ 
/*     */   public int getSourceAddrNpi()
/*     */   {
/* 119 */     return this.sourceAddrNpi;
/*     */   }
/*     */ 
/*     */   public String getSourceAddr()
/*     */   {
/* 126 */     return this.sourceAddr;
/*     */   }
/*     */ 
/*     */   public int getDestAddrTon()
/*     */   {
/* 133 */     return this.destAddrTon;
/*     */   }
/*     */ 
/*     */   public int getDestAddrNpi()
/*     */   {
/* 140 */     return this.destAddrNpi;
/*     */   }
/*     */ 
/*     */   public String getDestinationAddr()
/*     */   {
/* 147 */     return this.destinationAddr;
/*     */   }
/*     */ 
/*     */   public int getEsmClass()
/*     */   {
/* 154 */     return this.esmClass;
/*     */   }
/*     */ 
/*     */   public int getProtocolId()
/*     */   {
/* 161 */     return this.protocolId;
/*     */   }
/*     */ 
/*     */   public int getPriorityFlag()
/*     */   {
/* 168 */     return this.priorityFlag;
/*     */   }
/*     */ 
/*     */   public int getRegisteredDelivery()
/*     */   {
/* 175 */     return this.registeredDelivery;
/*     */   }
/*     */ 
/*     */   public int getDataCoding()
/*     */   {
/* 182 */     return this.dataCoding;
/*     */   }
/*     */ 
/*     */   public int getSmLength()
/*     */   {
/* 189 */     return this.smLength;
/*     */   }
/*     */ 
/*     */   public String getShortMessage()
/*     */   {
/* 197 */     return this.shortMessage;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 205 */     StringBuffer strBuf = new StringBuffer(600);
/* 206 */     strBuf.append("SMPPDeliverMessage: ");
/* 207 */     strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.getMsgLength()))));
/* 208 */     strBuf.append(",CommandID=".concat(String.valueOf(String.valueOf(super.getCommandId()))));
/* 209 */     strBuf.append(",Status=".concat(String.valueOf(String.valueOf(super.getStatus()))));
/* 210 */     strBuf.append(",Sequence_Id=".concat(String.valueOf(String.valueOf(super.getSequenceId()))));
/* 211 */     strBuf.append(",Service_Type=".concat(String.valueOf(String.valueOf(getServiceType()))));
/* 212 */     strBuf.append(",SourceAddrTon=".concat(String.valueOf(String.valueOf(getSourceAddrTon()))));
/* 213 */     strBuf.append(",SourceAddrNpi=".concat(String.valueOf(String.valueOf(getSourceAddrNpi()))));
/* 214 */     strBuf.append(",SourceAddr=".concat(String.valueOf(String.valueOf(getSourceAddr()))));
/* 215 */     strBuf.append(",DestAddrTon=".concat(String.valueOf(String.valueOf(getDestAddrTon()))));
/* 216 */     strBuf.append(",DestAddrNpi=".concat(String.valueOf(String.valueOf(getDestAddrNpi()))));
/* 217 */     strBuf.append(",DestinationAddr=".concat(String.valueOf(String.valueOf(getDestinationAddr()))));
/* 218 */     strBuf.append(",EsmClass=".concat(String.valueOf(String.valueOf(getEsmClass()))));
/* 219 */     strBuf.append(",ProtocolId=".concat(String.valueOf(String.valueOf(getProtocolId()))));
/* 220 */     strBuf.append(",PriorityFlag=".concat(String.valueOf(String.valueOf(getPriorityFlag()))));
/* 221 */     strBuf.append(",RegisteredDelivery=".concat(String.valueOf(String.valueOf(getRegisteredDelivery()))));
/* 222 */     strBuf.append(",DataCoding=".concat(String.valueOf(String.valueOf(getDataCoding()))));
/* 223 */     strBuf.append(",SmLength=".concat(String.valueOf(String.valueOf(getSmLength()))));
/* 224 */     strBuf.append(",ShortMessage=".concat(String.valueOf(String.valueOf(getShortMessage()))));
/* 225 */     return strBuf.toString();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.message.SMPPDeliverMessage
 * JD-Core Version:    0.5.3
 */