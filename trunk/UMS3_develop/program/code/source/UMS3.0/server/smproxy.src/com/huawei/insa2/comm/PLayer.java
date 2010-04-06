/*     */ package com.huawei.insa2.comm;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class PLayer
/*     */ {
/*  15 */   public static final int maxId = 1000000000;
/*     */   protected int id;
/*     */   protected int nextChildId;
/*     */   protected PLayer parent;
/*     */   private HashMap children;
/*     */   private List listeners;
/*     */ 
/*     */   protected PLayer(PLayer theParent)
/*     */   {
/*  37 */     if (theParent != null) {
/*  38 */       this.id = (++theParent.nextChildId);
/*  39 */       if (theParent.nextChildId >= 1000000000) {
/*  40 */         theParent.nextChildId = 0;
/*     */       }
/*  42 */       if (theParent.children == null) {
/*  43 */         theParent.children = new HashMap();
/*     */       }
/*  45 */       theParent.children.put(new Integer(this.id), this);
/*  46 */       this.parent = theParent;
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void send(PMessage paramPMessage)
/*     */     throws PException;
/*     */ 
/*     */   public void onReceive(PMessage message)
/*     */   {
/*  66 */     int childId = getChildId(message);
/*     */     PLayer child;
/*  67 */     if (childId == -1) {
/*  68 */       child = createChild();
/*  69 */       child.onReceive(message);
/*  70 */       fireEvent(new PEvent(2, this, child));
/*     */     } else {
/*  72 */       child = (PLayer)this.children.get(new Integer(getChildId(message)));
/*  73 */       if (child == null) {
/*  74 */         fireEvent(new PEvent(64, this, message));
/*     */       }
/*     */       else
/*  77 */         child.onReceive(message);
/*     */     }
/*     */   }
/*     */ 
/*     */   public PLayer getParent()
/*     */   {
/*  87 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public int getChildNumber()
/*     */   {
/*  95 */     if (this.children == null) {
/*  96 */       return 0;
/*     */     }
/*  98 */     return this.children.size();
/*     */   }
/*     */ 
/*     */   protected PLayer createChild()
/*     */   {
/* 106 */     throw new UnsupportedOperationException("Not implement");
/*     */   }
/*     */ 
/*     */   protected int getChildId(PMessage message)
/*     */   {
/* 118 */     throw new UnsupportedOperationException("Not implement");
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 128 */     if (this.parent == null) {
/* 129 */       throw new UnsupportedOperationException("Not implement");
/*     */     }
/* 131 */     this.parent.children.remove(new Integer(this.id));
/*     */   }
/*     */ 
/*     */   public void addEventListener(PEventListener l)
/*     */   {
/* 140 */     if (this.listeners == null) {
/* 141 */       this.listeners = new ArrayList();
/*     */     }
/* 143 */     this.listeners.add(l);
/*     */   }
/*     */ 
/*     */   public void removeEventListener(PEventListener l)
/*     */   {
/* 151 */     this.listeners.remove(l);
/*     */   }
/*     */ 
/*     */   protected void fireEvent(PEvent e)
/*     */   {
/* 160 */     if (this.listeners == null)
/*     */     {
/* 162 */       return;
/*     */     }
/*     */ 
/* 166 */     for (Iterator i = this.listeners.iterator(); i.hasNext(); )
/* 167 */       ((PEventListener)i.next()).handle(e);
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.PLayer
 * JD-Core Version:    0.5.3
 */