/*     */ package com.huawei.insa2.comm.smpp;
/*     */ 
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PMessage;
/*     */ import com.huawei.insa2.comm.PReader;
/*     */ import com.huawei.insa2.comm.PSocketConnection;
/*     */ import com.huawei.insa2.comm.PWriter;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPEnquireLinkMessage;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPEnquireLinkRespMessage;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPLoginMessage;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPLoginRespMessage;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPMessage;
/*     */ import com.huawei.insa2.comm.smpp.message.SMPPUnbindMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import com.huawei.insa2.util.Resource;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class SMPPConnection extends PSocketConnection
/*     */ {
/*  18 */   private int degree = 0;
/*     */ 
/*  20 */   private int hbnoResponseOut = 3;
/*     */   private String systemId;
/*     */   private String password;
/*     */   private String systemType;
/*     */   private byte interfaceVersion;
/*     */   private byte addrTon;
/*     */   private byte addrNpi;
/*     */   private String addressRange;
/*     */ 
/*     */   public SMPPConnection(Args args)
/*     */   {
/*  37 */     this.systemId = args.get("system-id", "");
/*  38 */     this.password = args.get("password", "");
/*  39 */     this.systemType = args.get("system-type", "");
/*  40 */     this.interfaceVersion = (byte)args.get("interface-version", 34);
/*  41 */     this.addrTon = (byte)args.get("addr-ton", 0);
/*  42 */     this.addrNpi = (byte)args.get("addr-npi", 0);
/*  43 */     this.addressRange = args.get("address-range", "");
/*     */ 
/*  45 */     SMPPConstant.debug = args.get("debug", false);
/*     */ 
/*  48 */     SMPPConstant.initConstant(getResource());
/*  49 */     super.init(args);
/*     */   }
/*     */ 
/*     */   protected PWriter getWriter(OutputStream out)
/*     */   {
/*  59 */     return new SMPPWriter(out);
/*     */   }
/*     */ 
/*     */   protected PReader getReader(InputStream in)
/*     */   {
/*  69 */     return new SMPPReader(in);
/*     */   }
/*     */ 
/*     */   public int getChildId(PMessage message)
/*     */   {
/*  78 */     SMPPMessage mes = (SMPPMessage)message;
/*  79 */     int sequenceId = mes.getSequenceId();
/*     */ 
/*  81 */     if ((mes.getCommandId() == 5) || (mes.getCommandId() == 21) || (mes.getCommandId() == 6))
/*     */     {
/*  85 */       return -1;
/*     */     }
/*  87 */     return sequenceId;
/*     */   }
/*     */ 
/*     */   public PLayer createChild()
/*     */   {
/*  95 */     return new SMPPTransaction(this);
/*     */   }
/*     */ 
/*     */   public int getTransactionTimeout()
/*     */   {
/* 103 */     return this.transactionTimeout;
/*     */   }
/*     */ 
/*     */   public Resource getResource()
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 112 */       return new Resource(super.getClass(), "resource");
/*     */     }
/*     */     catch (IOException e) {
/* 115 */       e.printStackTrace();
/* 116 */       localObject = null; } return localObject;
/*     */   }
/*     */ 
/*     */   public synchronized void waitAvailable()
/*     */   {
/*     */     try
/*     */     {
/* 126 */       if (super.getError() == PSocketConnection.NOT_INIT)
/* 127 */         super.wait(this.transactionTimeout);
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */     try
/*     */     {
/* 141 */       SMPPUnbindMessage msg = new SMPPUnbindMessage();
/* 142 */       super.send(msg);
/*     */     }
/*     */     catch (PException localPException) {
/*     */     }
/* 146 */     super.close();
/*     */   }
/*     */ 
/*     */   protected void heartbeat()
/*     */     throws IOException
/*     */   {
/* 156 */     SMPPTransaction t = (SMPPTransaction)createChild();
/* 157 */     SMPPEnquireLinkMessage hbmes = new SMPPEnquireLinkMessage();
/* 158 */     t.send(hbmes);
/* 159 */     t.waitResponse();
/* 160 */     SMPPEnquireLinkRespMessage rsp = (SMPPEnquireLinkRespMessage)t.getResponse();
/* 161 */     if (rsp == null) {
/* 162 */       this.degree += 1;
/* 163 */       if (this.degree == this.hbnoResponseOut) {
/* 164 */         this.degree = 0;
/* 165 */         throw new IOException(SMPPConstant.HEARTBEAT_ABNORMITY);
/*     */       }
/*     */     }
/*     */     else {
/* 169 */       this.degree = 0;
/*     */     }
/*     */ 
/* 181 */     t.close();
/*     */   }
/*     */ 
/*     */   protected synchronized void connect()
/*     */   {
/* 190 */     super.connect();
/* 191 */     if (!(super.available())) {
/* 192 */       return;
/*     */     }
/*     */ 
/* 196 */     SMPPLoginMessage request = null;
/*     */ 
/* 198 */     SMPPLoginRespMessage rsp = null;
/*     */     try
/*     */     {
/* 201 */       request = new SMPPLoginMessage(1, this.systemId, this.password, this.systemType, this.interfaceVersion, this.addrTon, this.addrNpi, this.addressRange);
/*     */     }
/*     */     catch (IllegalArgumentException e)
/*     */     {
/* 205 */       e.printStackTrace();
/* 206 */       close();
/* 207 */       super.setError(SMPPConstant.CONNECT_INPUT_ERROR);
/*     */     }
/*     */ 
/* 211 */     SMPPTransaction t = (SMPPTransaction)createChild();
/*     */     try {
/* 213 */       t.send(request);
/* 214 */       PMessage m = this.in.read();
/* 215 */       super.onReceive(m);
/*     */     }
/*     */     catch (IOException e) {
/* 218 */       e.printStackTrace();
/* 219 */       close();
/* 220 */       super.setError(String.valueOf(String.valueOf(SMPPConstant.LOGIN_ERROR)).concat(String.valueOf(String.valueOf(super.explain(e)))));
/*     */     }
/*     */ 
/* 223 */     rsp = (SMPPLoginRespMessage)t.getResponse();
/* 224 */     if (rsp == null)
/*     */     {
/* 226 */       close();
/* 227 */       super.setError(SMPPConstant.CONNECT_TIMEOUT);
/*     */     }
/* 229 */     t.close();
/*     */ 
/* 232 */     if ((rsp != null) && 
/* 234 */       (rsp.getStatus() != 0))
/*     */     {
/* 236 */       close();
/*     */ 
/* 238 */       super.setError(SMPPConstant.OTHER_ERROR);
/*     */     }
/*     */ 
/* 241 */     super.notifyAll();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smpp.SMPPConnection
 * JD-Core Version:    0.5.3
 */