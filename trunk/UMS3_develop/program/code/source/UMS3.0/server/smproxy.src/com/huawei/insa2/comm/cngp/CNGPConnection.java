/*     */ package com.huawei.insa2.comm.cngp;
/*     */ 
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PMessage;
/*     */ import com.huawei.insa2.comm.PReader;
/*     */ import com.huawei.insa2.comm.PSocketConnection;
/*     */ import com.huawei.insa2.comm.PWriter;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPActiveTestMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPActiveTestRespMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPExitMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPLoginMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPLoginRespMessage;
/*     */ import com.huawei.insa2.comm.cngp.message.CNGPMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import com.huawei.insa2.util.Resource;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class CNGPConnection extends PSocketConnection
/*     */ {
/*  20 */   private int degree = 0;
/*     */ 
/*  22 */   private int hbnoResponseOut = 3;
/*     */ 
/*  24 */   private String clientid = null;
/*     */ 
/*  26 */   private int loginMode = 0;
/*     */   private int version;
/*     */   private String shared_secret;
/*     */ 
/*     */   public CNGPConnection(Args args)
/*     */   {
/*  37 */     this.hbnoResponseOut = args.get("heartbeat-noresponseout", 3);
/*  38 */     this.clientid = args.get("clientid", "huawei");
/*  39 */     this.loginMode = args.get("loginmode", 0);
/*  40 */     this.version = args.get("version", 1);
/*  41 */     this.shared_secret = args.get("shared-secret", "");
/*  42 */     CNGPConstant.debug = args.get("debug", false);
/*     */ 
/*  44 */     CNGPConstant.initConstant(getResource());
/*  45 */     super.init(args);
/*     */   }
/*     */ 
/*     */   protected PWriter getWriter(OutputStream out)
/*     */   {
/*  55 */     return new CNGPWriter(out);
/*     */   }
/*     */ 
/*     */   protected PReader getReader(InputStream in)
/*     */   {
/*  65 */     return new CNGPReader(in);
/*     */   }
/*     */ 
/*     */   public int getChildId(PMessage message)
/*     */   {
/*  74 */     CNGPMessage mes = (CNGPMessage)message;
/*  75 */     int sequenceId = mes.getSequenceId();
/*     */ 
/*  77 */     if (mes.getRequestId() == 3)
/*     */     {
/*  79 */       return -1;
/*     */     }
/*  81 */     return sequenceId;
/*     */   }
/*     */ 
/*     */   public PLayer createChild()
/*     */   {
/*  89 */     return new CNGPTransaction(this);
/*     */   }
/*     */ 
/*     */   public int getTransactionTimeout()
/*     */   {
/*  97 */     return this.transactionTimeout;
/*     */   }
/*     */ 
/*     */   public Resource getResource()
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 106 */       return new Resource(super.getClass(), "resource");
/*     */     }
/*     */     catch (IOException e) {
/* 109 */       e.printStackTrace();
/* 110 */       localObject = null; } return localObject;
/*     */   }
/*     */ 
/*     */   public synchronized void waitAvailable()
/*     */   {
/*     */     try
/*     */     {
/* 120 */       if (super.getError() == PSocketConnection.NOT_INIT)
/* 121 */         super.wait(this.transactionTimeout);
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
/* 134 */       CNGPExitMessage msg = new CNGPExitMessage();
/* 135 */       super.send(msg);
/*     */     }
/*     */     catch (PException localPException)
/*     */     {
/*     */     }
/* 140 */     super.close();
/*     */   }
/*     */ 
/*     */   protected void heartbeat()
/*     */     throws IOException
/*     */   {
/* 150 */     CNGPTransaction t = (CNGPTransaction)createChild();
/* 151 */     CNGPActiveTestMessage hbmes = new CNGPActiveTestMessage();
/* 152 */     t.send(hbmes);
/* 153 */     t.waitResponse();
/* 154 */     CNGPActiveTestRespMessage rsp = (CNGPActiveTestRespMessage)t.getResponse();
/* 155 */     if (rsp == null) {
/* 156 */       this.degree += 1;
/* 157 */       if (this.degree == this.hbnoResponseOut) {
/* 158 */         this.degree = 0;
/* 159 */         throw new IOException(CNGPConstant.HEARTBEAT_ABNORMITY);
/*     */       }
/*     */     }
/*     */     else {
/* 163 */       this.degree = 0;
/*     */     }
/* 165 */     t.close();
/*     */   }
/*     */ 
/*     */   protected synchronized void connect()
/*     */   {
/* 174 */     super.connect();
/* 175 */     if (!(super.available())) {
/* 176 */       return;
/*     */     }
/*     */ 
/* 180 */     CNGPLoginMessage request = null;
/*     */ 
/* 182 */     CNGPLoginRespMessage rsp = null;
/*     */     try
/*     */     {
/* 185 */       request = new CNGPLoginMessage(this.clientid, this.shared_secret, 0, new Date(), this.version);
/*     */     }
/*     */     catch (IllegalArgumentException e)
/*     */     {
/* 190 */       e.printStackTrace();
/* 191 */       close();
/* 192 */       super.setError(CNGPConstant.CONNECT_INPUT_ERROR);
/*     */     }
/*     */ 
/* 196 */     CNGPTransaction t = (CNGPTransaction)createChild();
/*     */     try {
/* 198 */       t.send(request);
/* 199 */       PMessage m = this.in.read();
/* 200 */       super.onReceive(m);
/*     */     }
/*     */     catch (IOException e) {
/* 203 */       e.printStackTrace();
/* 204 */       close();
/* 205 */       super.setError(String.valueOf(String.valueOf(CNGPConstant.LOGIN_ERROR)).concat(String.valueOf(String.valueOf(super.explain(e)))));
/*     */     }
/*     */ 
/* 208 */     rsp = (CNGPLoginRespMessage)t.getResponse();
/* 209 */     if (rsp == null)
/*     */     {
/* 211 */       close();
/* 212 */       super.setError(CNGPConstant.CONNECT_TIMEOUT);
/*     */     }
/* 214 */     t.close();
/*     */ 
/* 217 */     if ((rsp != null) && 
/* 219 */       (rsp.getStatus() != 0))
/*     */     {
/* 221 */       close();
/*     */ 
/* 223 */       super.setError("Fail to login,the status code id ".concat(String.valueOf(String.valueOf(rsp.getStatus()))));
/*     */     }
/*     */ 
/* 226 */     super.notifyAll();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cngp.CNGPConnection
 * JD-Core Version:    0.5.3
 */