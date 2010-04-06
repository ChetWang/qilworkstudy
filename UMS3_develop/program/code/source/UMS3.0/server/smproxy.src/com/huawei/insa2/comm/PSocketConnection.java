/*     */ package com.huawei.insa2.comm;
/*     */ 
/*     */ import com.huawei.insa2.util.Args;
/*     */ import com.huawei.insa2.util.Resource;
/*     */ import com.huawei.insa2.util.WatchThread;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NoRouteToHostException;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ public abstract class PSocketConnection extends PLayer
/*     */ {
/*     */   protected static String NOT_INIT;
/*     */   protected static String CONNECTING;
/*     */   protected static String RECONNECTING;
/*     */   protected static String CONNECTED;
/*     */   protected static String HEARTBEATING;
/*     */   protected static String RECEIVEING;
/*     */   protected static String CLOSEING;
/*     */   protected static String CLOSED;
/*     */   protected static String UNKNOWN_HOST;
/*     */   protected static String PORT_ERROR;
/*     */   protected static String CONNECT_REFUSE;
/*     */   protected static String NO_ROUTE_TO_HOST;
/*     */   protected static String RECEIVE_TIMEOUT;
/*     */   protected static String CLOSE_BY_PEER;
/*     */   protected static String RESET_BY_PEER;
/*     */   protected static String CONNECTION_CLOSED;
/*     */   protected static String COMMUNICATION_ERROR;
/*     */   protected static String CONNECT_ERROR;
/*     */   protected static String SEND_ERROR;
/*     */   protected static String RECEIVE_ERROR;
/*     */   protected static String CLOSE_ERROR;
/*     */   private String error;
/*  84 */   protected Date errorTime = new Date();
/*     */   protected String name;
/*     */   protected String host;
/*  93 */   protected int port = -1;
/*     */   protected String localHost;
/*  99 */   protected int localPort = -1;
/*     */   protected int heartbeatInterval;
/*     */   protected PReader in;
/*     */   protected PWriter out;
/* 111 */   protected static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
/*     */   protected int readTimeout;
/*     */   protected int reconnectInterval;
/*     */   protected Socket socket;
/*     */   protected WatchThread heartbeatThread;
/*     */   protected WatchThread receiveThread;
/*     */   protected int transactionTimeout;
/*     */   protected Resource resource;
/*     */ 
/*     */   public PSocketConnection(Args args)
/*     */   {
/* 155 */     super(null);
/* 156 */     init(args);
/*     */   }
/*     */ 
/*     */   protected PSocketConnection()
/*     */   {
/* 163 */     super(null);
/*     */   }
/*     */ 
/*     */   protected void init(Args args)
/*     */   {
/* 171 */     this.resource = getResource();
/* 172 */     initResource();
/* 173 */     this.error = NOT_INIT;
/* 174 */     setAttributes(args);
/*     */ 
/* 199 */     if (this.heartbeatInterval > 0) {
/* 200 */       this.heartbeatThread = new 1.HeartbeatThread();
/* 201 */       this.heartbeatThread.start();
/*     */     }
/*     */ 
/* 237 */     this.receiveThread = new 1.ReceiveThread();
/* 238 */     this.receiveThread.start();
/*     */   }
/*     */ 
/*     */   public void setAttributes(Args args)
/*     */   {
/* 251 */     if ((this.name != null) && (this.name.equals(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(String.valueOf(this.host)))).append(':').append(this.port)))))) {
/* 252 */       this.name = null;
/*     */     }
/*     */ 
/* 256 */     String oldHost = this.host;
/* 257 */     int oldPort = this.port;
/* 258 */     String oldLocalHost = this.localHost;
/* 259 */     int oldLocalPort = this.localPort;
/*     */ 
/* 264 */     this.host = args.get("host", null);
/*     */ 
/* 267 */     this.port = args.get("port", -1);
/*     */ 
/* 270 */     this.localHost = args.get("local-host", null);
/*     */ 
/* 273 */     this.localPort = args.get("local-port", -1);
/*     */ 
/* 278 */     this.name = args.get("name", null);
/* 279 */     if (this.name == null) {
/* 280 */       this.name = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.host))).append(':').append(this.port)));
/*     */     }
/*     */ 
/* 284 */     this.readTimeout = (1000 * args.get("read-timeout", this.readTimeout / 1000));
/*     */ 
/* 287 */     if (this.socket != null)
/*     */       try {
/* 289 */         this.socket.setSoTimeout(this.readTimeout);
/*     */       }
/*     */       catch (SocketException localSocketException)
/*     */       {
/*     */       }
/* 294 */     this.reconnectInterval = (1000 * args.get("reconnect-interval", -1));
/*     */ 
/* 297 */     this.heartbeatInterval = (1000 * args.get("heartbeat-interval", -1));
/*     */ 
/* 300 */     this.transactionTimeout = (1000 * args.get("transaction-timeout", -1));
/*     */ 
/* 303 */     if ((this.error != null) || (this.host == null) || (this.port == -1) || ((this.host.equals(oldHost)) && (this.port == this.port) && (this.host.equals(oldHost)) && (this.port == this.port)))
/*     */     {
/*     */       return;
/*     */     }
/*     */ 
/* 308 */     setError(this.resource.get("comm/need-reconnect"));
/* 309 */     this.receiveThread.interrupt();
/*     */   }
/*     */ 
/*     */   public void send(PMessage message)
/*     */     throws PException
/*     */   {
/* 319 */     if (this.error != null)
/* 320 */       throw new PException(String.valueOf(String.valueOf(SEND_ERROR)).concat(String.valueOf(String.valueOf(getError()))));
/*     */     try
/*     */     {
/* 323 */       this.out.write(message);
/* 324 */       super.fireEvent(new PEvent(8, this, message));
/*     */     } catch (PException ex) {
/* 326 */       super.fireEvent(new PEvent(16, this, message));
/* 327 */       setError(String.valueOf(String.valueOf(SEND_ERROR)).concat(String.valueOf(String.valueOf(explain(ex)))));
/* 328 */       throw ex;
/*     */     } catch (Exception ex) {
/* 330 */       super.fireEvent(new PEvent(16, this, message));
/* 331 */       setError(String.valueOf(String.valueOf(SEND_ERROR)).concat(String.valueOf(String.valueOf(explain(ex)))));
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 340 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 348 */     return this.host;
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 356 */     return this.port;
/*     */   }
/*     */ 
/*     */   public int getReconnectInterval()
/*     */   {
/* 364 */     return (this.reconnectInterval / 1000);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 372 */     return String.valueOf(String.valueOf(new StringBuffer("PSocketConnection:").append(this.name).append('(').append(this.host).append(':').append(this.port).append(')')));
/*     */   }
/*     */ 
/*     */   public int getReadTimeout()
/*     */   {
/* 380 */     return (this.readTimeout / 1000);
/*     */   }
/*     */ 
/*     */   public boolean available()
/*     */   {
/* 388 */     return (this.error == null);
/*     */   }
/*     */ 
/*     */   public String getError()
/*     */   {
/* 396 */     return this.error;
/*     */   }
/*     */ 
/*     */   public Date getErrorTime()
/*     */   {
/* 404 */     return this.errorTime;
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */   {
/*     */     try
/*     */     {
/* 412 */       if (this.socket != null)
/*     */       {
/* 416 */         this.socket.close();
/* 417 */         this.in = null;
/* 418 */         this.out = null;
/* 419 */         this.socket = null;
/*     */ 
/* 422 */         if (this.heartbeatThread != null) {
/* 423 */           this.heartbeatThread.kill();
/*     */         }
/* 425 */         this.receiveThread.kill(); }
/*     */     } catch (Exception localException) {
/*     */     }
/* 428 */     setError(NOT_INIT);
/*     */   }
/*     */ 
/*     */   protected synchronized void connect()
/*     */   {
/* 437 */     if (this.error == NOT_INIT)
/* 438 */       this.error = CONNECTING;
/* 439 */     else if (this.error == null) {
/* 440 */       this.error = RECONNECTING;
/*     */     }
/* 442 */     this.errorTime = new Date();
/*     */ 
/* 444 */     if (this.socket != null) {
/*     */       try {
/* 446 */         this.socket.close();
/*     */       }
/*     */       catch (IOException localIOException1)
/*     */       {
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 454 */       if ((this.port <= 0) || (this.port > 65535)) {
/* 455 */         setError(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(PORT_ERROR))).append("port:").append(this.port))));
/* 456 */         return;
/*     */       }
/*     */ 
/* 460 */       if ((this.localPort < -1) || (this.localPort > 65535)) {
/* 461 */         setError(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(PORT_ERROR))).append("local-port:").append(this.localPort))));
/* 462 */         return;
/*     */       }
/*     */ 
/* 467 */       if (this.localHost != null) {
/* 468 */         boolean isConnected = false;
/* 469 */         InetAddress localAddr = InetAddress.getByName(this.localHost);
/* 470 */         if (this.localPort == -1) {
/* 471 */           label254: for (int p = (int)(Math.random() * 64500); p < 903000; p += 13) {
/*     */             try {
/* 473 */               this.socket = new Socket(this.host, this.port, localAddr, 1025 + p % 64500);
/* 474 */               isConnected = true;
/*     */             } catch (IOException localIOException2) {
/* 476 */               break label254:
/*     */             }
/*     */             catch (SecurityException localSecurityException)
/*     */             {
/*     */             }
/*     */           }
/* 482 */           if (!(isConnected))
/* 483 */             throw new SocketException("Can not find an avaliable local port");
/*     */         }
/*     */         else {
/* 486 */           this.socket = new Socket(this.host, this.port, localAddr, this.localPort);
/*     */         }
/*     */       } else {
/* 489 */         this.socket = new Socket(this.host, this.port);
/*     */       }
/*     */ 
/* 494 */       this.socket.setSoTimeout(this.readTimeout);
/*     */ 
/* 498 */       this.out = getWriter(this.socket.getOutputStream());
/*     */ 
/* 502 */       this.in = getReader(this.socket.getInputStream());
/*     */ 
/* 506 */       setError(null);
/*     */     } catch (IOException ex) {
/* 508 */       setError(String.valueOf(String.valueOf(CONNECT_ERROR)).concat(String.valueOf(String.valueOf(explain(ex)))));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setError(String desc)
/*     */   {
/* 519 */     if (((this.error == null) && (desc == null)) || ((desc != null) && (desc.equals(this.error)))) {
/* 520 */       return;
/*     */     }
/* 522 */     this.error = desc;
/* 523 */     this.errorTime = new Date();
/*     */ 
/* 527 */     if (desc == null)
/* 528 */       desc = CONNECTED;
/*     */   }
/*     */ 
/*     */   protected abstract PWriter getWriter(OutputStream paramOutputStream);
/*     */ 
/*     */   protected abstract PReader getReader(InputStream paramInputStream);
/*     */ 
/*     */   protected abstract Resource getResource();
/*     */ 
/*     */   protected void heartbeat()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void initResource()
/*     */   {
/* 563 */     NOT_INIT = this.resource.get("comm/not-init");
/* 564 */     CONNECTING = this.resource.get("comm/connecting");
/* 565 */     RECONNECTING = this.resource.get("comm/reconnecting");
/* 566 */     CONNECTED = this.resource.get("comm/connected");
/* 567 */     HEARTBEATING = this.resource.get("comm/heartbeating");
/* 568 */     RECEIVEING = this.resource.get("comm/receiveing");
/* 569 */     CLOSEING = this.resource.get("comm/closeing");
/* 570 */     CLOSED = this.resource.get("comm/closed");
/* 571 */     UNKNOWN_HOST = this.resource.get("comm/unknown-host");
/* 572 */     PORT_ERROR = this.resource.get("comm/port-error");
/* 573 */     CONNECT_REFUSE = this.resource.get("comm/connect-refused");
/* 574 */     NO_ROUTE_TO_HOST = this.resource.get("comm/no-route");
/* 575 */     RECEIVE_TIMEOUT = this.resource.get("comm/receive-timeout");
/* 576 */     CLOSE_BY_PEER = this.resource.get("comm/close-by-peer");
/* 577 */     RESET_BY_PEER = this.resource.get("comm/reset-by-peer");
/* 578 */     CONNECTION_CLOSED = this.resource.get("comm/connection-closed");
/* 579 */     COMMUNICATION_ERROR = this.resource.get("comm/communication-error");
/* 580 */     CONNECT_ERROR = this.resource.get("comm/connect-error");
/* 581 */     SEND_ERROR = this.resource.get("comm/send-error");
/* 582 */     RECEIVE_ERROR = this.resource.get("comm/receive-error");
/* 583 */     CLOSE_ERROR = this.resource.get("comm/close-error");
/*     */   }
/*     */ 
/*     */   protected String explain(Exception ex)
/*     */   {
/* 591 */     String msg = ex.getMessage();
/* 592 */     if (msg == null) {
/* 593 */       msg = "";
/*     */     }
/* 595 */     if (ex instanceof PException)
/* 596 */       return ex.getMessage();
/* 597 */     if (ex instanceof EOFException)
/* 598 */       return CLOSE_BY_PEER;
/* 599 */     if (msg.indexOf("Connection reset by peer") != -1)
/* 600 */       return RESET_BY_PEER;
/* 601 */     if (msg.indexOf("SocketTimeoutException") != -1)
/* 602 */       return RECEIVE_TIMEOUT;
/* 603 */     if (ex instanceof NoRouteToHostException)
/* 604 */       return NO_ROUTE_TO_HOST;
/* 605 */     if (ex instanceof ConnectException)
/* 606 */       return CONNECT_REFUSE;
/* 607 */     if (ex instanceof UnknownHostException)
/* 608 */       return UNKNOWN_HOST;
/* 609 */     if (msg.indexOf("errno: 128") != -1)
/*     */     {
/* 611 */       return NO_ROUTE_TO_HOST;
/*     */     }
/* 613 */     ex.printStackTrace();
/* 614 */     return ex.toString();
/*     */   }
/*     */ 
/*     */   class 1$HeartbeatThread extends WatchThread
/*     */   {
/*     */     public 1$HeartbeatThread()
/*     */     {
/* 179 */       super(String.valueOf(String.valueOf(PSocketConnection.this.name)).concat("-heartbeat"));
/* 180 */       super.setState(PSocketConnection.HEARTBEATING); }
/*     */ 
/*     */     public void task() {
/*     */       try {
/* 184 */         Thread.sleep(PSocketConnection.this.heartbeatInterval);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/* 188 */       if ((PSocketConnection.this.error != null) || (PSocketConnection.this.out == null)) return;
/*     */       try {
/* 190 */         PSocketConnection.this.heartbeat();
/*     */       } catch (IOException ex) {
/* 192 */         PSocketConnection.this.setError(String.valueOf(String.valueOf(PSocketConnection.SEND_ERROR)).concat(String.valueOf(String.valueOf(PSocketConnection.this.explain(ex)))));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class 1$ReceiveThread extends WatchThread
/*     */   {
/*     */     public 1$ReceiveThread()
/*     */     {
/* 207 */       super(String.valueOf(String.valueOf(PSocketConnection.this.name)).concat("-receive")); }
/*     */ 
/*     */     public void task() {
/*     */       try {
/* 211 */         if (PSocketConnection.this.error == null) {
/* 212 */           PMessage m = PSocketConnection.this.in.read();
/* 213 */           if ((m == null) || 
/* 215 */             (m == null)) return;
/* 216 */           PSocketConnection.this.onReceive(m); return;
/*     */         }
/*     */ 
/* 219 */         if (PSocketConnection.this.error != PSocketConnection.NOT_INIT)
/*     */           try {
/* 221 */             Thread.sleep(PSocketConnection.this.reconnectInterval);
/*     */           }
/*     */           catch (InterruptedException localInterruptedException)
/*     */           {
/*     */           }
/* 226 */         PSocketConnection.this.connect();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.PSocketConnection
 * JD-Core Version:    0.5.3
 */