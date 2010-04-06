/*     */ package com.huawei.smproxy;
/*     */ 
/*     */ import com.huawei.insa2.comm.PLayer;
/*     */ import com.huawei.insa2.comm.sgip.SGIPConnection;
/*     */ import com.huawei.insa2.comm.sgip.SGIPSocketConnection;
/*     */ import com.huawei.insa2.comm.sgip.SGIPTransaction;
/*     */ import com.huawei.insa2.comm.sgip.SSEventListener;
/*     */ import com.huawei.insa2.comm.sgip.SSListener;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPDeliverRepMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPReportRepMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPUserReportMessage;
/*     */ import com.huawei.insa2.comm.sgip.message.SGIPUserReportRepMessage;
/*     */ import com.huawei.insa2.util.Args;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SGIPSMProxy
/*     */   implements SSEventListener
/*     */ {
/*     */   private SGIPConnection conn;
/*     */   private SSListener listener;
/*     */   private Args args;
/*     */   private HashMap serconns;
/*     */   private int src_nodeid;
/*     */ 
/*     */   public SGIPSMProxy(Map args)
/*     */   {
/*  36 */     this(new Args(args));
/*     */   }
/*     */ 
/*     */   public SGIPSMProxy(Args args)
/*     */   {
/*  44 */     this.args = args;
/*  45 */     this.src_nodeid = args.get("source-addr", 0);
/*     */   }
/*     */ 
/*     */   public synchronized boolean connect(String loginName, String loginPass)
/*     */   {
/*  52 */     boolean result = true;
/*     */ 
/*  54 */     if (loginName != null) {
/*  55 */       this.args.set("login-name", loginName.trim());
/*     */     }
/*  57 */     if (loginPass != null) {
/*  58 */       this.args.set("login-pass", loginPass.trim());
/*     */     }
/*     */ 
/*  61 */     this.conn = new SGIPConnection(this.args, true, null);
/*     */ 
/*  64 */     this.conn.addEventListener(new SGIPEventAdapter(this, this.conn));
/*  65 */     this.conn.waitAvailable();
/*  66 */     if (!(this.conn.available())) {
/*  67 */       result = false;
/*  68 */       throw new IllegalStateException(this.conn.getError());
/*     */     }
/*  70 */     return result;
/*     */   }
/*     */ 
/*     */   public synchronized void startService(String localhost, int localport)
/*     */   {
/*  77 */     if (this.listener != null)
/*  78 */       return;
/*     */     try
/*     */     {
/*  81 */       this.listener = new SSListener(localhost, localport, this);
/*  82 */       this.listener.beginListen();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void stopService()
/*     */   {
/*  92 */     if (this.listener == null) {
/*  93 */       return;
/*     */     }
/*  95 */     this.listener.stopListen();
/*     */ 
/*  97 */     if (this.serconns != null) {
/*  98 */       Iterator iterator = this.serconns.keySet().iterator();
/*     */ 
/* 101 */       while (iterator.hasNext()) {
/* 102 */         String addr = (String)iterator.next();
/* 103 */         SGIPConnection conn = (SGIPConnection)this.serconns.get(addr);
/* 104 */         conn.close();
/*     */       }
/* 106 */       this.serconns.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void onConnect(Socket socket)
/*     */   {
/* 115 */     String peerIP = socket.getInetAddress().getHostAddress();
/* 116 */     int port = socket.getPort();
/*     */ 
/* 118 */     if (this.serconns == null) {
/* 119 */       this.serconns = new HashMap();
/*     */     }
/* 121 */     SGIPConnection conn = new SGIPConnection(this.args, false, this.serconns);
/*     */ 
/* 124 */     conn.addEventListener(new SGIPEventAdapter(this, conn));
/* 125 */     conn.attach(this.args, socket);
/* 126 */     this.serconns.put(new String(String.valueOf(String.valueOf(peerIP)).concat(String.valueOf(String.valueOf(port)))), conn);
/*     */   }
/*     */ 
/*     */   public SGIPMessage send(SGIPMessage message)
/*     */     throws IOException
/*     */   {
/* 136 */     if (message == null) {
/* 137 */       return null;
/*     */     }
/*     */ 
/* 140 */     SGIPTransaction t = (SGIPTransaction)this.conn.createChild();
/* 141 */     t.setSPNumber(this.src_nodeid);
/* 142 */     Date nowtime = new Date();
/* 143 */     SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
/* 144 */     String tmpTime = dateFormat.format(nowtime);
/* 145 */     Integer timestamp = new Integer(tmpTime);
/* 146 */     t.setTimestamp(timestamp.intValue());
/*     */     try {
/* 148 */       t.send(message);
/* 149 */       t.waitResponse();
/* 150 */       SGIPMessage rsp = t.getResponse();
/* 151 */       return rsp;
/*     */     } finally {
/* 153 */       t.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onTerminate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SGIPMessage onDeliver(SGIPDeliverMessage msg)
/*     */   {
/* 172 */     return new SGIPDeliverRepMessage(0);
/*     */   }
/*     */ 
/*     */   public SGIPMessage onReport(SGIPReportMessage msg)
/*     */   {
/* 183 */     return new SGIPReportRepMessage(0);
/*     */   }
/*     */ 
/*     */   public SGIPMessage onUserReport(SGIPUserReportMessage msg)
/*     */   {
/* 194 */     return new SGIPUserReportRepMessage(0);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 202 */     this.conn.close();
/*     */   }
/*     */ 
/*     */   public SGIPConnection getConn()
/*     */   {
/* 210 */     return this.conn;
/*     */   }
/*     */ 
/*     */   public String getConnState()
/*     */   {
/* 218 */     if (this.conn != null) {
/* 219 */       return this.conn.getError();
/*     */     }
/*     */ 
/* 222 */     return "尚未建立连接";
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.smproxy.SGIPSMProxy
 * JD-Core Version:    0.5.3
 */