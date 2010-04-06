/*     */ package com.huawei.insa2.comm;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class PEvent
/*     */ {
/*  14 */   public static final int CREATED = 1;
/*     */ 
/*  17 */   public static final int CHILD_CREATED = 2;
/*     */ 
/*  20 */   public static final int DELETED = 4;
/*     */ 
/*  23 */   public static final int MESSAGE_SEND_SUCCESS = 8;
/*     */ 
/*  26 */   public static final int MESSAGE_SEND_FAIL = 16;
/*     */ 
/*  29 */   public static final int MESSAGE_DISPATCH_SUCCESS = 32;
/*     */ 
/*  32 */   public static final int MESSAGE_DISPATCH_FAIL = 64;
/*     */ 
/*  35 */   static final HashMap names = new HashMap();
/*     */   protected PLayer source;
/*     */   protected int type;
/*     */   protected Object data;
/*     */ 
/*     */   public PEvent(int type, PLayer source, Object data)
/*     */   {
/*  66 */     this.type = type;
/*  67 */     this.source = source;
/*  68 */     this.data = data;
/*     */   }
/*     */ 
/*     */   public PLayer getSource()
/*     */   {
/*  76 */     return this.source;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/*  84 */     return this.type;
/*     */   }
/*     */ 
/*     */   public Object getData()
/*     */   {
/*  93 */     return this.data;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 101 */     return String.valueOf(String.valueOf(new StringBuffer("PEvent:source=").append(this.source).append(",type=").append(names.get(new Integer(this.type))).append(",data=").append(this.data)));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  38 */       Field[] f = PEvent.class.getFields();
/*  39 */       int i = 0; if (i >= f.length) return;
/*  40 */       String name = f[i].getName();
/*  41 */       Object id = f[i].get(null);
/*  42 */       names.put(id, name);
/*     */ 
/*  39 */       ++i;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  45 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.PEvent
 * JD-Core Version:    0.5.3
 */