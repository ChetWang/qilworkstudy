/*     */ package com.huawei.insa2.comm.sgip;
/*     */ 
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PMessage;
/*     */ import com.huawei.insa2.comm.PReader;
/*     */ import com.huawei.insa2.comm.PWriter;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPBindMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPBindRepMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPUnbindMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import com.huawei.insa2.util.Resource;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class SGIPConnection extends SGIPSocketConnection
/*     */ {
/*  19 */   private int degree = 0;
/*     */ 
/*  21 */   private int hbnoResponseOut = 3;
/*     */ 
/*  23 */   private String source_addr = null;
/*     */   private int version;
/*     */   private String shared_secret;
/*     */   private boolean asClient;
/*     */   private String login_name;
/*     */   private String login_pass;
/*     */   private int src_nodeid;
/*     */   private String ipaddr;
/*     */   private int port;
/*     */   private HashMap connmap;
/*     */ 
/*     */   public SGIPConnection(Args args, boolean ifasClient, HashMap connmap)
/*     */   {
/*  48 */     this.hbnoResponseOut = args.get("heartbeat-noresponseout", 3);
/*  49 */     this.source_addr = args.get("source-addr", "huawei");
/*  50 */     this.version = args.get("version", 1);
/*  51 */     this.shared_secret = args.get("shared-secret", "");
/*  52 */     SGIPConstant.debug = args.get("debug", false);
/*  53 */     this.login_name = args.get("login-name", "");
/*  54 */     this.login_pass = args.get("login-pass", "");
/*  55 */     this.src_nodeid = args.get("source-addr", 0);
/*  56 */     this.connmap = connmap;
/*     */ 
/*  58 */     SGIPConstant.initConstant(getResource());
/*  59 */     this.asClient = ifasClient;
/*  60 */     if (this.asClient)
/*  61 */       super.init(args);
/*     */   }
/*     */ 
/*     */   public synchronized void attach(Args args, Socket socket)
/*     */   {
/*  71 */     if (this.asClient) {
/*  72 */       throw new UnsupportedOperationException("Client socket can not accept connection");
/*     */     }
/*     */ 
/*  75 */     super.init(args, socket);
/*  76 */     this.ipaddr = socket.getInetAddress().getHostAddress();
/*  77 */     this.port = socket.getPort();
/*     */   }
/*     */ 
/*     */   protected void onReadTimeOut()
/*     */   {
/*  86 */     close();
/*     */   }
/*     */ 
/*     */   protected PWriter getWriter(OutputStream out)
/*     */   {
/*  96 */     return new SGIPWriter(out);
/*     */   }
/*     */ 
/*     */   protected PReader getReader(InputStream in)
/*     */   {
/* 106 */     return new SGIPReader(in);
/*     */   }
/*     */ 
/*     */   public int getChildId(PMessage message)
/*     */   {
/* 115 */     SGIPMessage mes = (SGIPMessage)message;
/* 116 */     int sequenceId = mes.getSequenceId();
/*     */ 
/* 118 */     if (!(this.asClient));
/* 124 */     if ((mes.getCommandId() == 4) || (mes.getCommandId() == 5) || (mes.getCommandId() == 17) || (mes.getCommandId() == 1) || (mes.getCommandId() == 2))
/*     */     {
/* 130 */       return -1;
/*     */     }
/* 132 */     return sequenceId;
/*     */   }
/*     */ 
/*     */   public PLayer createChild()
/*     */   {
/* 140 */     return new SGIPTransaction(this);
/*     */   }
/*     */ 
/*     */   public int getTransactionTimeout()
/*     */   {
/* 148 */     return this.transactionTimeout;
/*     */   }
/*     */ 
/*     */   public Resource getResource()
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 157 */       return new Resource(super.getClass(), "resource");
/*     */     }
/*     */     catch (IOException e) {
/* 160 */       e.printStackTrace();
/* 161 */       localObject = null; } return localObject;
/*     */   }
/*     */ 
/*     */   public synchronized void waitAvailable()
/*     */   {
/*     */     try
/*     */     {
/* 171 */       if (super.getError() == SGIPSocketConnection.NOT_INIT)
/* 172 */         super.wait(this.transactionTimeout);
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 185 */     SGIPTransaction t = (SGIPTransaction)createChild();
/* 186 */     t.setSPNumber(this.src_nodeid);
/* 187 */     Date nowtime = new Date();
/* 188 */     SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
/* 189 */     String tmpTime = dateFormat.format(nowtime);
/* 190 */     Integer timestamp = new Integer(tmpTime);
/* 191 */     t.setTimestamp(timestamp.intValue());
/*     */     try {
/* 193 */       SGIPUnbindMessage msg = new SGIPUnbindMessage();
/* 194 */       t.send(msg);
/* 195 */       t.waitResponse();
/* 196 */       SGIPMessage localSGIPMessage = t.getResponse();
/*     */     } catch (PException localPException) {
/*     */     }
/*     */     finally {
/* 200 */       t.close();
/*     */     }
/* 202 */     super.close();
/* 203 */     if ((this.asClient) || 
/* 205 */       (this.connmap == null)) return;
/* 206 */     this.connmap.remove(new String(String.valueOf(String.valueOf(this.ipaddr)).concat(String.valueOf(String.valueOf(this.port)))));
/*     */   }
/*     */ 
/*     */   protected void heartbeat()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected synchronized void connect()
/*     */   {
/* 226 */     super.connect();
/* 227 */     if (!(super.available())) {
/* 228 */       return;
/*     */     }
/*     */ 
/* 232 */     SGIPBindMessage request = null;
/*     */ 
/* 234 */     SGIPBindRepMessage rsp = null;
/*     */     try {
/* 236 */       request = new SGIPBindMessage(1, this.login_name, this.login_pass);
/*     */     }
/*     */     catch (IllegalArgumentException e)
/*     */     {
/* 240 */       e.printStackTrace();
/* 241 */       close();
/* 242 */       super.setError(SGIPConstant.CONNECT_INPUT_ERROR);
/*     */     }
/*     */ 
/* 246 */     SGIPTransaction t = (SGIPTransaction)createChild();
/*     */     try {
/* 248 */       t.send(request);
/* 249 */       PMessage m = this.in.read();
/* 250 */       super.onReceive(m);
/*     */     }
/*     */     catch (IOException e) {
/* 253 */       e.printStackTrace();
/* 254 */       close();
/* 255 */       super.setError(String.valueOf(String.valueOf(SGIPConstant.LOGIN_ERROR)).concat(String.valueOf(String.valueOf(super.explain(e)))));
/*     */     }
/*     */ 
/* 258 */     rsp = (SGIPBindRepMessage)t.getResponse();
/* 259 */     if (rsp == null)
/*     */     {
/* 261 */       close();
/* 262 */       super.setError(SGIPConstant.CONNECT_TIMEOUT);
/*     */     }
/* 264 */     t.close();
/*     */ 
/* 267 */     if ((rsp != null) && 
/* 269 */       (rsp.getResult() != 0))
/*     */     {
/* 271 */       close();
/*     */ 
/* 273 */       if (rsp.getResult() == 1) {
/* 274 */         super.setError(SGIPConstant.NONLICETSP_LOGNAME);
/*     */       }
/*     */       else {
/* 277 */         super.setError(SGIPConstant.OTHER_ERROR);
/*     */       }
/*     */     }
/*     */ 
/* 281 */     super.notifyAll();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.SGIPConnection
 * JD-Core Version:    0.5.3
 */