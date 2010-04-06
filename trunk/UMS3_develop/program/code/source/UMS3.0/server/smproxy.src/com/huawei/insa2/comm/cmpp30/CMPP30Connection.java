/*     */ package com.huawei.insa2.comm.cmpp30;
/*     */ 
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PMessage;
/*     */ import com.huawei.insa2.comm.PReader;
/*     */ import com.huawei.insa2.comm.PSocketConnection;
/*     */ import com.huawei.insa2.comm.PWriter;
/*     */ import com.huawei.insa2.comm.cmpp.CMPPConstant;
/*     */ import com.huawei.insa2.comm.cmpp.CMPPWriter;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPActiveMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPActiveRepMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPConnectMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPTerminateMessage;
/*     */ import com.huawei.insa2.comm.cmpp30.message.CMPP30ConnectRepMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import com.huawei.insa2.util.Resource;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class CMPP30Connection extends PSocketConnection
/*     */ {
/*  20 */   private int degree = 0;
/*     */ 
/*  22 */   private int hbnoResponseOut = 3;
/*     */ 
/*  24 */   private String source_addr = null;
/*     */   private int version;
/*     */   private String shared_secret;
/*     */ 
/*     */   public CMPP30Connection(Args args)
/*     */   {
/*  35 */     this.hbnoResponseOut = args.get("heartbeat-noresponseout", 3);
/*  36 */     this.source_addr = args.get("source-addr", "huawei");
/*  37 */     this.version = args.get("version", 1);
/*  38 */     this.shared_secret = args.get("shared-secret", "");
/*  39 */     CMPPConstant.debug = args.get("debug", false);
/*     */ 
/*  41 */     CMPPConstant.initConstant(getResource());
/*  42 */     super.init(args);
/*     */   }
/*     */ 
/*     */   protected PWriter getWriter(OutputStream out)
/*     */   {
/*  52 */     return new CMPPWriter(out);
/*     */   }
/*     */ 
/*     */   protected PReader getReader(InputStream in)
/*     */   {
/*  62 */     return new CMPP30Reader(in);
/*     */   }
/*     */ 
/*     */   public int getChildId(PMessage message)
/*     */   {
/*  71 */     CMPPMessage mes = (CMPPMessage)message;
/*  72 */     int sequenceId = mes.getSequenceId();
/*     */ 
/*  74 */     if ((mes.getCommandId() == 5) || (mes.getCommandId() == 8) || (mes.getCommandId() == 2))
/*     */     {
/*  78 */       return -1;
/*     */     }
/*  80 */     return sequenceId;
/*     */   }
/*     */ 
/*     */   public PLayer createChild()
/*     */   {
/*  88 */     return new CMPP30Transaction(this);
/*     */   }
/*     */ 
/*     */   public int getTransactionTimeout()
/*     */   {
/*  96 */     return this.transactionTimeout;
/*     */   }
/*     */ 
/*     */   public Resource getResource()
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 105 */       return new Resource(super.getClass(), "resource");
/*     */     }
/*     */     catch (IOException e) {
/* 108 */       e.printStackTrace();
/* 109 */       localObject = null; } return localObject;
/*     */   }
/*     */ 
/*     */   public synchronized void waitAvailable()
/*     */   {
/*     */     try
/*     */     {
/* 119 */       if (super.getError() == PSocketConnection.NOT_INIT)
/* 120 */         super.wait(this.transactionTimeout);
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
/* 134 */       CMPPTerminateMessage msg = new CMPPTerminateMessage();
/* 135 */       super.send(msg);
/*     */     }
/*     */     catch (PException localPException) {
/*     */     }
/* 139 */     super.close();
/*     */   }
/*     */ 
/*     */   protected void heartbeat()
/*     */     throws IOException
/*     */   {
/* 149 */     CMPP30Transaction t = (CMPP30Transaction)createChild();
/* 150 */     CMPPActiveMessage hbmes = new CMPPActiveMessage();
/* 151 */     t.send(hbmes);
/* 152 */     t.waitResponse();
/* 153 */     CMPPActiveRepMessage rsp = (CMPPActiveRepMessage)t.getResponse();
/* 154 */     if (rsp == null) {
/* 155 */       this.degree += 1;
/* 156 */       if (this.degree == this.hbnoResponseOut) {
/* 157 */         this.degree = 0;
/* 158 */         throw new IOException(CMPPConstant.HEARTBEAT_ABNORMITY);
/*     */       }
/*     */     }
/*     */     else {
/* 162 */       this.degree = 0;
/*     */     }
/*     */ 
/* 174 */     t.close();
/*     */   }
/*     */ 
/*     */   protected synchronized void connect()
/*     */   {
/* 183 */     super.connect();
/* 184 */     if (!(super.available())) {
/* 185 */       return;
/*     */     }
/*     */ 
/* 189 */     CMPPConnectMessage request = null;
/*     */ 
/* 191 */     CMPP30ConnectRepMessage rsp = null;
/*     */     try
/*     */     {
/* 194 */       request = new CMPPConnectMessage(this.source_addr, this.version, this.shared_secret, new Date());
/*     */     }
/*     */     catch (IllegalArgumentException e)
/*     */     {
/* 198 */       e.printStackTrace();
/* 199 */       close();
/* 200 */       super.setError(CMPPConstant.CONNECT_INPUT_ERROR);
/*     */     }
/*     */ 
/* 204 */     CMPP30Transaction t = (CMPP30Transaction)createChild();
/*     */     try {
/* 206 */       t.send(request);
/* 207 */       PMessage m = this.in.read();
/* 208 */       super.onReceive(m);
/*     */     }
/*     */     catch (IOException e) {
/* 211 */       e.printStackTrace();
/* 212 */       close();
/* 213 */       super.setError(String.valueOf(String.valueOf(CMPPConstant.LOGIN_ERROR)).concat(String.valueOf(String.valueOf(super.explain(e)))));
/*     */     }
/*     */ 
/* 216 */     rsp = (CMPP30ConnectRepMessage)t.getResponse();
/* 217 */     if (rsp == null)
/*     */     {
/* 219 */       close();
/* 220 */       super.setError(CMPPConstant.CONNECT_TIMEOUT);
/*     */     }
/* 222 */     t.close();
/*     */ 
/* 225 */     if ((rsp != null) && 
/* 227 */       (rsp.getStatus() != 0))
/*     */     {
/* 229 */       close();
/*     */ 
/* 231 */       if (rsp.getStatus() == 1) {
/* 232 */         super.setError(CMPPConstant.STRUCTURE_ERROR);
/*     */       }
/* 234 */       else if (rsp.getStatus() == 2) {
/* 235 */         super.setError(CMPPConstant.NONLICETSP_ID);
/*     */       }
/* 237 */       else if (rsp.getStatus() == 3) {
/* 238 */         super.setError(CMPPConstant.SP_ERROR);
/*     */       }
/* 240 */       else if (rsp.getStatus() == 4) {
/* 241 */         super.setError(CMPPConstant.VERSION_ERROR);
/*     */       }
/*     */       else {
/* 244 */         super.setError(CMPPConstant.OTHER_ERROR);
/*     */       }
/*     */     }
/*     */ 
/* 248 */     super.notifyAll();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp30.CMPP30Connection
 * JD-Core Version:    0.5.3
 */