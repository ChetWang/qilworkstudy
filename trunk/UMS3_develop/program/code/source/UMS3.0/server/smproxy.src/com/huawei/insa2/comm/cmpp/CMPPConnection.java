/*     */ package com.huawei.insa2.comm.cmpp;
/*     */ 
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PMessage;
/*     */ import com.huawei.insa2.comm.PReader;
/*     */ import com.huawei.insa2.comm.PSocketConnection;
/*     */ import com.huawei.insa2.comm.PWriter;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPActiveMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPActiveRepMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPConnectMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPConnectRepMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
/*     */ import com.huawei.insa2.comm.cmpp.message.CMPPTerminateMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import com.huawei.insa2.util.Resource;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class CMPPConnection extends PSocketConnection
/*     */ {
/*  18 */   private int degree = 0;
/*     */ 
/*  20 */   private int hbnoResponseOut = 3;
/*     */ 
/*  22 */   private String source_addr = null;
/*     */   private int version;
/*     */   private String shared_secret;
/*     */ 
/*     */   public CMPPConnection(Args args)
/*     */   {
/*  33 */     this.hbnoResponseOut = args.get("heartbeat-noresponseout", 3);
/*  34 */     this.source_addr = args.get("source-addr", "huawei");
/*  35 */     this.version = args.get("version", 1);
/*  36 */     this.shared_secret = args.get("shared-secret", "");
/*  37 */     CMPPConstant.debug = args.get("debug", false);
/*     */ 
/*  39 */     CMPPConstant.initConstant(getResource());
/*  40 */     super.init(args);
/*     */   }
/*     */ 
/*     */   protected PWriter getWriter(OutputStream out)
/*     */   {
/*  50 */     return new CMPPWriter(out);
/*     */   }
/*     */ 
/*     */   protected PReader getReader(InputStream in)
/*     */   {
/*  60 */     return new CMPPReader(in);
/*     */   }
/*     */ 
/*     */   public int getChildId(PMessage message)
/*     */   {
/*  69 */     CMPPMessage mes = (CMPPMessage)message;
/*  70 */     int sequenceId = mes.getSequenceId();
/*     */ 
/*  72 */     if ((mes.getCommandId() == 5) || (mes.getCommandId() == 8) || (mes.getCommandId() == 2))
/*     */     {
/*  76 */       return -1;
/*     */     }
/*  78 */     return sequenceId;
/*     */   }
/*     */ 
/*     */   public PLayer createChild()
/*     */   {
/*  86 */     return new CMPPTransaction(this);
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
/* 132 */       CMPPTerminateMessage msg = new CMPPTerminateMessage();
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
/* 147 */     CMPPTransaction t = (CMPPTransaction)createChild();
/* 148 */     CMPPActiveMessage hbmes = new CMPPActiveMessage();
/* 149 */     t.send(hbmes);
/* 150 */     t.waitResponse();
/* 151 */     CMPPActiveRepMessage rsp = (CMPPActiveRepMessage)t.getResponse();
/* 152 */     if (rsp == null) {
/* 153 */       this.degree += 1;
/* 154 */       if (this.degree == this.hbnoResponseOut) {
/* 155 */         this.degree = 0;
/* 156 */         throw new IOException(CMPPConstant.HEARTBEAT_ABNORMITY);
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
/* 187 */     CMPPConnectMessage request = null;
/*     */ 
/* 189 */     CMPPConnectRepMessage rsp = null;
/*     */     try
/*     */     {
/* 192 */       request = new CMPPConnectMessage(this.source_addr, this.version, this.shared_secret, new Date());
/*     */     }
/*     */     catch (IllegalArgumentException e)
/*     */     {
/* 196 */       e.printStackTrace();
/* 197 */       close();
/* 198 */       super.setError(CMPPConstant.CONNECT_INPUT_ERROR);
/*     */     }
/*     */ 
/* 202 */     CMPPTransaction t = (CMPPTransaction)createChild();
/*     */     try {
/* 204 */       t.send(request);
/* 205 */       PMessage m = this.in.read();
/* 206 */       super.onReceive(m);
/*     */     }
/*     */     catch (IOException e) {
/* 209 */       e.printStackTrace();
/* 210 */       close();
/* 211 */       super.setError(String.valueOf(String.valueOf(CMPPConstant.LOGIN_ERROR)).concat(String.valueOf(String.valueOf(super.explain(e)))));
/*     */     }
/*     */ 
/* 214 */     rsp = (CMPPConnectRepMessage)t.getResponse();
/* 215 */     if (rsp == null)
/*     */     {
/* 217 */       close();
/* 218 */       super.setError(CMPPConstant.CONNECT_TIMEOUT);
/*     */     }
/* 220 */     t.close();
/*     */ 
/* 223 */     if ((rsp != null) && 
/* 225 */       (rsp.getStatus() != 0))
/*     */     {
/* 227 */       close();
/*     */ 
/* 229 */       if (rsp.getStatus() == 1) {
/* 230 */         super.setError(CMPPConstant.STRUCTURE_ERROR);
/*     */       }
/* 232 */       else if (rsp.getStatus() == 2) {
/* 233 */         super.setError(CMPPConstant.NONLICETSP_ID);
/*     */       }
/* 235 */       else if (rsp.getStatus() == 3) {
/* 236 */         super.setError(CMPPConstant.SP_ERROR);
/*     */       }
/* 238 */       else if (rsp.getStatus() == 4) {
/* 239 */         super.setError(CMPPConstant.VERSION_ERROR);
/*     */       }
/*     */       else {
/* 242 */         super.setError(CMPPConstant.OTHER_ERROR);
/*     */       }
/*     */     }
/*     */ 
/* 246 */     super.notifyAll();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.cmpp.CMPPConnection
 * JD-Core Version:    0.5.3
 */