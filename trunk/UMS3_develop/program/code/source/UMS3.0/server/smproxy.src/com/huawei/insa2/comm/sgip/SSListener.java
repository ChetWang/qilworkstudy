/*    */ package com.huawei.insa2.comm.sgip;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ 
/*    */ public class SSListener extends Thread
/*    */ {
/*    */   private ServerSocket serversocket;
/*    */   private SSEventListener listener;
/*    */   private String ip;
/*    */   private int port;
/* 26 */   private int status = 1;
/* 27 */   private static final int ON = 0;
/* 28 */   private static final int OFF = 1;
/*    */ 
/*    */   public SSListener(String ip, int port, SSEventListener lis)
/*    */   {
/* 37 */     this.ip = ip;
/* 38 */     this.port = port;
/* 39 */     this.listener = lis;
/* 40 */     super.start();
/*    */   }
/*    */ 
/*    */   public void run() {
/*    */     while (true) {
/* 45 */       if (this.status == 0) {
/*    */         try {
/* 47 */           Socket incoming = this.serversocket.accept();
/* 48 */           if (this.status == 0);
/* 49 */           this.listener.onConnect(incoming);
/*    */         } catch (Exception incoming) {
/*    */         }
/* 52 */         if (this.status != 0) {
/*    */           continue;
/*    */         }
/*    */       }
/*    */ 
/* 57 */       synchronized (this) {
/*    */         try {
/* 59 */           super.wait(10000L);
/*    */         }
/*    */         catch (Exception localException1)
/*    */         {
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public synchronized void beginListen()
/*    */     throws IOException
/*    */   {
/* 71 */     if (this.status == 0)
/* 72 */       return;
/*    */     try
/*    */     {
/* 75 */       this.serversocket = new ServerSocket();
/*    */ 
/* 77 */       this.serversocket.bind(new InetSocketAddress(this.ip, this.port));
/* 78 */       this.status = 0;
/* 79 */       super.notifyAll();
/*    */     }
/*    */     catch (IOException ioex)
/*    */     {
/* 83 */       throw ioex;
/*    */     }
/*    */   }
/*    */ 
/*    */   public synchronized void stopListen()
/*    */   {
/* 91 */     if (this.status != 0) return;
/*    */     try {
/* 93 */       if (this.serversocket != null) {
/* 94 */         this.status = 1;
/* 95 */         this.serversocket.close();
/* 96 */         this.serversocket = null;
/*    */       }
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.SSListener
 * JD-Core Version:    0.5.3
 */