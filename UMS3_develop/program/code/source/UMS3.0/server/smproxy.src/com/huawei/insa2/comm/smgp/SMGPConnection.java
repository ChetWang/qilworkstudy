/*     */ package com.huawei.insa2.comm.smgp;
/*     */ 
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PMessage;
/*     */ import com.huawei.insa2.comm.PReader;
/*     */ import com.huawei.insa2.comm.PSocketConnection;
/*     */ import com.huawei.insa2.comm.PWriter;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPActiveTestMessage;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPActiveTestRespMessage;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPExitMessage;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPLoginMessage;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPLoginRespMessage;
/*     */ import com.huawei.insa2.comm.smgp.message.SMGPMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import com.huawei.insa2.util.Resource;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SMGPConnection extends PSocketConnection
/*     */ {
/*  18 */   private int degree = 0;
/*     */ 
/*  20 */   private int hbnoResponseOut = 3;
/*     */ 
/*  22 */   private String clientid = null;
/*     */   private int version;
/*     */   private String shared_secret;
/*     */ 
/*     */   public SMGPConnection(Args args)
/*     */   {
/*  33 */     this.hbnoResponseOut = args.get("heartbeat-noresponseout", 3);
/*  34 */     this.clientid = args.get("clientid", "huawei");
/*  35 */     this.version = args.get("version", 1);
/*  36 */     this.shared_secret = args.get("shared-secret", "");
/*  37 */     SMGPConstant.debug = args.get("debug", false);
/*     */ 
/*  39 */     SMGPConstant.initConstant(getResource());
/*  40 */     super.init(args);
/*     */   }
/*     */ 
/*     */   protected PWriter getWriter(OutputStream out)
/*     */   {
/*  50 */     return new SMGPWriter(out);
/*     */   }
/*     */ 
/*     */   protected PReader getReader(InputStream in)
/*     */   {
/*  60 */     return new SMGPReader(in);
/*     */   }
/*     */ 
/*     */   public int getChildId(PMessage message)
/*     */   {
/*  69 */     SMGPMessage mes = (SMGPMessage)message;
/*  70 */     int sequenceId = mes.getSequenceId();
/*     */ 
/*  72 */     if ((mes.getRequestId() == 3) || (mes.getRequestId() == 4) || (mes.getRequestId() == 6))
/*     */     {
/*  76 */       return -1;
/*     */     }
/*  78 */     return sequenceId;
/*     */   }
/*     */ 
/*     */   public PLayer createChild()
/*     */   {
/*  86 */     return new SMGPTransaction(this);
/*     */   }
/*     */ 
/*     */   public int getTransactionTimeout()
/*     */   {
/*  94 */     return this.transactionTimeout;
/*     */   }
/*     */ 
/*     */   public Resource getResource()
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 103 */       return new Resource(super.getClass(), "resource");
/*     */     }
/*     */     catch (IOException e) {
/* 106 */       e.printStackTrace();
/* 107 */       localObject = null; } return localObject;
/*     */   }
/*     */ 
/*     */   public synchronized void waitAvailable()
/*     */   {
/*     */     try
/*     */     {
/* 117 */       if (super.getError() == PSocketConnection.NOT_INIT)
/* 118 */         super.wait(this.transactionTimeout);
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
/* 132 */       SMGPExitMessage msg = new SMGPExitMessage();
/* 133 */       super.send(msg);
/*     */     }
/*     */     catch (PException localPException) {
/*     */     }
/* 137 */     super.close();
/*     */   }
/*     */ 
/*     */   protected void heartbeat()
/*     */     throws IOException
/*     */   {
/* 147 */     SMGPTransaction t = (SMGPTransaction)createChild();
/* 148 */     SMGPActiveTestMessage hbmes = new SMGPActiveTestMessage();
/* 149 */     t.send(hbmes);
/* 150 */     t.waitResponse();
/* 151 */     SMGPActiveTestRespMessage rsp = (SMGPActiveTestRespMessage)t.getResponse();
/* 152 */     if (rsp == null) {
/* 153 */       this.degree += 1;
/* 154 */       if (this.degree == this.hbnoResponseOut) {
/* 155 */         this.degree = 0;
/* 156 */         throw new IOException(SMGPConstant.HEARTBEAT_ABNORMITY);
/*     */       }
/*     */     }
/*     */     else {
/* 160 */       this.degree = 0;
/*     */     }
/*     */ 
/* 172 */     t.close();
/*     */   }
/*     */ 
/*     */   protected synchronized void connect()
/*     */   {
/* 181 */     super.connect();
/* 182 */     if (!(super.available())) {
/* 183 */       return;
/*     */     }
/*     */ 
/* 187 */     SMGPLoginMessage request = null;
/*     */ 
/* 189 */     SMGPLoginRespMessage rsp = null;
/*     */     try
/*     */     {
/* 192 */       request = new SMGPLoginMessage(this.clientid, this.shared_secret, 3, new Date(), this.version);
/*     */     }
/*     */     catch (IllegalArgumentException e)
/*     */     {
/* 196 */       e.printStackTrace();
/* 197 */       close();
/* 198 */       super.setError(SMGPConstant.CONNECT_INPUT_ERROR);
/*     */     }
/*     */ 
/* 202 */     SMGPTransaction t = (SMGPTransaction)createChild();
/*     */     try {
/* 204 */       t.send(request);
/* 205 */       PMessage m = this.in.read();
/* 206 */       super.onReceive(m);
/*     */     }
/*     */     catch (IOException e) {
/* 209 */       e.printStackTrace();
/* 210 */       close();
/* 211 */       super.setError(String.valueOf(String.valueOf(SMGPConstant.LOGIN_ERROR)).concat(String.valueOf(String.valueOf(super.explain(e)))));
/*     */     }
/*     */ 
/* 214 */     rsp = (SMGPLoginRespMessage)t.getResponse();
/* 215 */     if (rsp == null)
/*     */     {
/* 217 */       close();
/* 218 */       super.setError(SMGPConstant.CONNECT_TIMEOUT);
/*     */     }
/* 220 */     t.close();
/*     */ 
/* 223 */     if ((rsp != null) && 
/* 225 */       (rsp.getStatus() != 0))
/*     */     {
/* 227 */       close();
/*     */ 
/* 229 */       super.setError("Fail to login,the status code id ".concat(String.valueOf(String.valueOf(rsp.getStatus()))));
/*     */     }
/*     */ 
/* 232 */     super.notifyAll();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.smgp.SMGPConnection
 * JD-Core Version:    0.5.3
 */