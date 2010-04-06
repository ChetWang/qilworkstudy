/*     */ package com.huawei.insa2.comm.sgip;
/*     */ 
/*     */ import com.huawei.insa2.comm.PEvent;
/*     */ import com.huawei.insa2.comm.PException;
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.PMessage;
/*     */ import com.huawei.insa2.comm.PReader;
/*     */ import com.huawei.insa2.comm.PWriter;
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
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ public abstract class SGIPSocketConnection extends PLayer
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
/*  85 */   protected Date errorTime = new Date();
/*     */   protected String name;
/*     */   protected String host;
/*  94 */   protected int port = -1;
/*     */   protected String localHost;
/* 100 */   protected int localPort = -1;
/*     */   protected int heartbeatInterval;
/*     */   protected PReader in;
/*     */   protected PWriter out;
/* 112 */   protected static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
/*     */   protected int readTimeout;
/*     */   protected int reconnectInterval;
/*     */   protected Socket socket;
/*     */   protected WatchThread heartbeatThread;
/*     */   protected WatchThread receiveThread;
/*     */   protected int transactionTimeout;
/*     */   protected Resource resource;
/*     */ 
/*     */   public SGIPSocketConnection(Args args)
/*     */   {
/* 156 */     super(null);
/* 157 */     init(args);
/*     */   }
/*     */ 
/*     */   protected SGIPSocketConnection()
/*     */   {
/* 164 */     super(null);
/*     */   }
/*     */ 
/*     */   protected void init(Args args)
/*     */   {
/* 172 */     this.resource = getResource();
/* 173 */     initResource();
/* 174 */     this.error = NOT_INIT;
/* 175 */     setAttributes(args);
/*     */ 
/* 210 */     this.receiveThread = new 1.ReceiveThread();
/* 211 */     this.receiveThread.start();
/*     */   }
/*     */ 
/*     */   protected void init(Args args, Socket socket)
/*     */   {
/* 221 */     this.resource = getResource();
/* 222 */     initResource();
/* 223 */     this.error = NOT_INIT;
/*     */ 
/* 225 */     if (socket != null) {
/* 226 */       this.socket = socket;
/*     */       try
/*     */       {
/* 230 */         this.out = getWriter(this.socket.getOutputStream());
/*     */ 
/* 234 */         this.in = getReader(this.socket.getInputStream());
/*     */ 
/* 238 */         setError(null);
/*     */       } catch (IOException ex) {
/* 240 */         setError(String.valueOf(String.valueOf(CONNECT_ERROR)).concat(String.valueOf(String.valueOf(explain(ex)))));
/*     */       }
/*     */ 
/* 243 */       if (args != null) {
/* 244 */         setAttributes1(args);
/*     */       }
/*     */ 
/* 277 */       this.receiveThread = new 1.ReceiveThread1();
/* 278 */       this.receiveThread.start();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void onReadTimeOut()
/*     */   {
/* 288 */     throw new UnsupportedOperationException("Not implement");
/*     */   }
/*     */ 
/*     */   public void setAttributes(Args args)
/*     */   {
/* 299 */     if ((this.name != null) && (this.name.equals(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(String.valueOf(this.host)))).append(':').append(this.port)))))) {
/* 300 */       this.name = null;
/*     */     }
/*     */ 
/* 304 */     String oldHost = this.host;
/* 305 */     int oldPort = this.port;
/* 306 */     String oldLocalHost = this.localHost;
/* 307 */     int oldLocalPort = this.localPort;
/*     */ 
/* 312 */     this.host = args.get("host", null);
/*     */ 
/* 315 */     this.port = args.get("port", -1);
/*     */ 
/* 318 */     this.localHost = args.get("local-host", null);
/*     */ 
/* 321 */     this.localPort = args.get("local-port", -1);
/*     */ 
/* 326 */     this.name = args.get("name", null);
/* 327 */     if (this.name == null) {
/* 328 */       this.name = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.host))).append(':').append(this.port)));
/*     */     }
/*     */ 
/* 333 */     this.readTimeout = 0;
/*     */ 
/* 336 */     if (this.socket != null) {
/*     */       try {
/* 338 */         this.socket.setSoTimeout(this.readTimeout);
/*     */       }
/*     */       catch (SocketException localSocketException)
/*     */       {
/*     */       }
/*     */     }
/* 344 */     this.heartbeatInterval = 0;
/*     */ 
/* 347 */     this.transactionTimeout = (1000 * args.get("transaction-timeout", -1));
/*     */ 
/* 350 */     if ((this.error != null) || (this.host == null) || (this.port == -1) || ((this.host.equals(oldHost)) && (this.port == this.port) && (this.host.equals(oldHost)) && (this.port == this.port)))
/*     */     {
/*     */       return;
/*     */     }
/*     */ 
/* 355 */     setError(this.resource.get("comm/need-reconnect"));
/* 356 */     this.receiveThread.interrupt();
/*     */   }
/*     */ 
/*     */   public void setAttributes1(Args args)
/*     */   {
/* 368 */     if ((this.name != null) && (this.name.equals(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(String.valueOf(this.host)))).append(':').append(this.port)))))) {
/* 369 */       this.name = null;
/*     */     }
/*     */ 
/* 375 */     this.host = args.get("host", null);
/*     */ 
/* 378 */     this.port = args.get("port", -1);
/*     */ 
/* 381 */     this.localHost = args.get("local-host", null);
/*     */ 
/* 384 */     this.localPort = args.get("local-port", -1);
/*     */ 
/* 389 */     this.name = args.get("name", null);
/* 390 */     if (this.name == null) {
/* 391 */       this.name = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.host))).append(':').append(this.port)));
/*     */     }
/*     */ 
/* 395 */     this.readTimeout = (1000 * args.get("read-timeout", this.readTimeout / 1000));
/*     */ 
/* 398 */     if (this.socket != null) {
/*     */       try {
/* 400 */         this.socket.setSoTimeout(this.readTimeout);
/*     */       }
/*     */       catch (SocketException localSocketException)
/*     */       {
/*     */       }
/*     */     }
/* 406 */     this.heartbeatInterval = 0;
/*     */ 
/* 409 */     this.transactionTimeout = (1000 * args.get("transaction-timeout", -1));
/*     */   }
/*     */ 
/*     */   public void send(PMessage message)
/*     */     throws PException
/*     */   {
/* 419 */     if (this.error != null)
/* 420 */       throw new PException(String.valueOf(String.valueOf(SEND_ERROR)).concat(String.valueOf(String.valueOf(getError()))));
/*     */     try
/*     */     {
/* 423 */       this.out.write(message);
/* 424 */       super.fireEvent(new PEvent(8, this, message));
/*     */     } catch (PException ex) {
/* 426 */       super.fireEvent(new PEvent(16, this, message));
/* 427 */       setError(String.valueOf(String.valueOf(SEND_ERROR)).concat(String.valueOf(String.valueOf(explain(ex)))));
/* 428 */       throw ex;
/*     */     } catch (Exception ex) {
/* 430 */       super.fireEvent(new PEvent(16, this, message));
/* 431 */       setError(String.valueOf(String.valueOf(SEND_ERROR)).concat(String.valueOf(String.valueOf(explain(ex)))));
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 440 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 448 */     return this.host;
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 456 */     return this.port;
/*     */   }
/*     */ 
/*     */   public int getReconnectInterval()
/*     */   {
/* 464 */     return (this.reconnectInterval / 1000);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 472 */     return String.valueOf(String.valueOf(new StringBuffer("PShortConnection:").append(this.name).append('(').append(this.host).append(':').append(this.port).append(')')));
/*     */   }
/*     */ 
/*     */   public int getReadTimeout()
/*     */   {
/* 480 */     return (this.readTimeout / 1000);
/*     */   }
/*     */ 
/*     */   public boolean available()
/*     */   {
/* 488 */     return (this.error == null);
/*     */   }
/*     */ 
/*     */   public String getError()
/*     */   {
/* 496 */     return this.error;
/*     */   }
/*     */ 
/*     */   public Date getErrorTime()
/*     */   {
/* 504 */     return this.errorTime;
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */   {
/*     */     try
/*     */     {
/* 512 */       if (this.socket != null)
/*     */       {
/* 516 */         this.socket.close();
/* 517 */         this.in = null;
/* 518 */         this.out = null;
/* 519 */         this.socket = null;
/*     */ 
/* 522 */         if (this.heartbeatThread != null) {
/* 523 */           this.heartbeatThread.kill();
/*     */         }
/* 525 */         this.receiveThread.kill(); }
/*     */     } catch (Exception localException) {
/*     */     }
/* 528 */     setError(NOT_INIT);
/*     */   }
/*     */ 
/*     */   protected synchronized void connect()
/*     */   {
/* 537 */     if (this.error == NOT_INIT)
/* 538 */       this.error = CONNECTING;
/* 539 */     else if (this.error == null) {
/* 540 */       this.error = RECONNECTING;
/*     */     }
/* 542 */     this.errorTime = new Date();
/*     */ 
/* 544 */     if (this.socket != null) {
/*     */       try {
/* 546 */         this.socket.close();
/*     */       }
/*     */       catch (IOException localIOException1)
/*     */       {
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 554 */       if ((this.port <= 0) || (this.port > 65535)) {
/* 555 */         setError(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(PORT_ERROR))).append("port:").append(this.port))));
/* 556 */         return;
/*     */       }
/*     */ 
/* 560 */       if ((this.localPort < -1) || (this.localPort > 65535)) {
/* 561 */         setError(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(PORT_ERROR))).append("local-port:").append(this.localPort))));
/* 562 */         return;
/*     */       }
/*     */ 
/* 567 */       if (this.localHost != null) {
/* 568 */         boolean isConnected = false;
/* 569 */         InetAddress localAddr = InetAddress.getByName(this.localHost);
/* 570 */         if (this.localPort == -1) {
/* 571 */           label254: for (int p = (int)(Math.random() * 64500); p < 903000; p += 13) {
/*     */             try {
/* 573 */               this.socket = new Socket(this.host, this.port, localAddr, 1025 + p % 64500);
/* 574 */               isConnected = true;
/*     */             } catch (IOException localIOException2) {
/* 576 */               break label254:
/*     */             }
/*     */             catch (SecurityException localSecurityException)
/*     */             {
/*     */             }
/*     */           }
/* 582 */           if (!(isConnected))
/* 583 */             throw new SocketException("Can not find an avaliable local port");
/*     */         }
/*     */         else {
/* 586 */           this.socket = new Socket(this.host, this.port, localAddr, this.localPort);
/*     */         }
/*     */       } else {
/* 589 */         this.socket = new Socket(this.host, this.port);
/*     */       }
/*     */ 
/* 594 */       this.socket.setSoTimeout(this.readTimeout);
/*     */ 
/* 598 */       this.out = getWriter(this.socket.getOutputStream());
/*     */ 
/* 602 */       this.in = getReader(this.socket.getInputStream());
/*     */ 
/* 606 */       setError(null);
/*     */     } catch (IOException ex) {
/* 608 */       setError(String.valueOf(String.valueOf(CONNECT_ERROR)).concat(String.valueOf(String.valueOf(explain(ex)))));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setError(String desc)
/*     */   {
/* 619 */     if (((this.error == null) && (desc == null)) || ((desc != null) && (desc.equals(this.error)))) {
/* 620 */       return;
/*     */     }
/* 622 */     this.error = desc;
/* 623 */     this.errorTime = new Date();
/*     */ 
/* 627 */     if (desc == null)
/* 628 */       desc = CONNECTED;
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
/* 663 */     NOT_INIT = this.resource.get("comm/not-init");
/* 664 */     CONNECTING = this.resource.get("comm/connecting");
/* 665 */     RECONNECTING = this.resource.get("comm/reconnecting");
/* 666 */     CONNECTED = this.resource.get("comm/connected");
/* 667 */     HEARTBEATING = this.resource.get("comm/heartbeating");
/* 668 */     RECEIVEING = this.resource.get("comm/receiveing");
/* 669 */     CLOSEING = this.resource.get("comm/closeing");
/* 670 */     CLOSED = this.resource.get("comm/closed");
/* 671 */     UNKNOWN_HOST = this.resource.get("comm/unknown-host");
/* 672 */     PORT_ERROR = this.resource.get("comm/port-error");
/* 673 */     CONNECT_REFUSE = this.resource.get("comm/connect-refused");
/* 674 */     NO_ROUTE_TO_HOST = this.resource.get("comm/no-route");
/* 675 */     RECEIVE_TIMEOUT = this.resource.get("comm/receive-timeout");
/* 676 */     CLOSE_BY_PEER = this.resource.get("comm/close-by-peer");
/* 677 */     RESET_BY_PEER = this.resource.get("comm/reset-by-peer");
/* 678 */     CONNECTION_CLOSED = this.resource.get("comm/connection-closed");
/* 679 */     COMMUNICATION_ERROR = this.resource.get("comm/communication-error");
/* 680 */     CONNECT_ERROR = this.resource.get("comm/connect-error");
/* 681 */     SEND_ERROR = this.resource.get("comm/send-error");
/* 682 */     RECEIVE_ERROR = this.resource.get("comm/receive-error");
/* 683 */     CLOSE_ERROR = this.resource.get("comm/close-error");
/*     */   }
/*     */ 
/*     */   protected String explain(Exception ex)
/*     */   {
/* 691 */     String msg = ex.getMessage();
/* 692 */     if (msg == null) {
/* 693 */       msg = "";
/*     */     }
/* 695 */     if (ex instanceof PException)
/* 696 */       return ex.getMessage();
/* 697 */     if (ex instanceof EOFException)
/* 698 */       return CLOSE_BY_PEER;
/* 699 */     if (msg.indexOf("Connection reset by peer") != -1)
/* 700 */       return RESET_BY_PEER;
/* 701 */     if (msg.indexOf("SocketTimeoutException") != -1)
/* 702 */       return RECEIVE_TIMEOUT;
/* 703 */     if (ex instanceof SocketTimeoutException)
/* 704 */       return RECEIVE_TIMEOUT;
/* 705 */     if (ex instanceof NoRouteToHostException)
/* 706 */       return NO_ROUTE_TO_HOST;
/* 707 */     if (ex instanceof ConnectException)
/* 708 */       return CONNECT_REFUSE;
/* 709 */     if (ex instanceof UnknownHostException)
/* 710 */       return UNKNOWN_HOST;
/* 711 */     if (msg.indexOf("errno: 128") != -1)
/*     */     {
/* 713 */       return NO_ROUTE_TO_HOST;
/*     */     }
/* 715 */     ex.printStackTrace();
/* 716 */     return ex.toString();
/*     */   }
/*     */ 
/*     */   class 1$ReceiveThread extends WatchThread
/*     */   {
/*     */     public 1$ReceiveThread()
/*     */     {
/* 180 */       super(String.valueOf(String.valueOf(SGIPSocketConnection.this.name)).concat("-receive")); }
/*     */ 
/*     */     public void task() {
/*     */       try {
/* 184 */         if (SGIPSocketConnection.this.error == null) {
/* 185 */           PMessage m = SGIPSocketConnection.this.in.read();
/* 186 */           if ((m == null) || 
/* 188 */             (m == null)) return;
/* 189 */           SGIPSocketConnection.this.onReceive(m); return;
/*     */         }
/*     */ 
/* 192 */         if (SGIPSocketConnection.this.error != SGIPSocketConnection.NOT_INIT)
/*     */           try {
/* 194 */             Thread.sleep(SGIPSocketConnection.this.reconnectInterval);
/*     */           }
/*     */           catch (InterruptedException localInterruptedException)
/*     */           {
/*     */           }
/* 199 */         SGIPSocketConnection.this.connect();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class 1$ReceiveThread1 extends WatchThread
/*     */   {
/*     */     public 1$ReceiveThread1()
/*     */     {
/* 250 */       super(String.valueOf(String.valueOf(SGIPSocketConnection.this.name)).concat("-receive")); }
/*     */ 
/*     */     public void task() {
/*     */       try {
/* 254 */         if (SGIPSocketConnection.this.error == null) {
/* 255 */           PMessage m = SGIPSocketConnection.this.in.read();
/* 256 */           if ((m == null) || 
/* 258 */             (m == null)) return;
/* 259 */           SGIPSocketConnection.this.onReceive(m);
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 267 */         SGIPSocketConnection.this.setError(SGIPSocketConnection.this.explain(ex));
/* 268 */         if (SGIPSocketConnection.this.error == SGIPSocketConnection.RECEIVE_TIMEOUT) {
/* 269 */           SGIPSocketConnection.this.setError(null);
/* 270 */           SGIPSocketConnection.this.onReadTimeOut();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.SGIPSocketConnection
 * JD-Core Version:    0.5.3
 */