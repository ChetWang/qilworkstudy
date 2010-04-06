/*     */ package com.huawei.insa2.util;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Args
/*     */ {
/*  17 */   public static final Args EMPTY = new Args().lock();
/*     */   boolean locked;
/*     */   Map args;
/*     */ 
/*     */   public Args()
/*     */   {
/*  27 */     this(new HashMap());
/*     */   }
/*     */ 
/*     */   public Args(Map theArgs)
/*     */   {
/*  34 */     if (theArgs == null) {
/*  35 */       throw new NullPointerException("argument is null");
/*     */     }
/*  37 */     this.args = theArgs;
/*     */   }
/*     */ 
/*     */   public String get(String key, String def)
/*     */   {
/*     */     String str2;
/*     */     try
/*     */     {
/*  47 */       return this.args.get(key).toString();
/*     */     } catch (Exception ex) {
/*  49 */       str2 = def; } return str2;
/*     */   }
/*     */ 
/*     */   public int get(String key, int def)
/*     */   {
/*     */     int j;
/*     */     try
/*     */     {
/*  60 */       return Integer.parseInt(this.args.get(key).toString());
/*     */     } catch (Exception ex) {
/*  62 */       j = def; } return j;
/*     */   }
/*     */ 
/*     */   public long get(String key, long def)
/*     */   {
/*     */     long l2;
/*     */     try
/*     */     {
/*  73 */       return Long.parseLong(this.args.get(key).toString());
/*     */     } catch (Exception ex) {
/*  75 */       l2 = def; } return l2;
/*     */   }
/*     */ 
/*     */   public float get(String key, float def)
/*     */   {
/*     */     float f2;
/*     */     try
/*     */     {
/*  86 */       return Float.parseFloat(this.args.get(key).toString());
/*     */     } catch (Exception ex) {
/*  88 */       f2 = def; } return f2;
/*     */   }
/*     */ 
/*     */   public boolean get(String key, boolean def)
/*     */   {
/*     */     boolean bool2;
/*     */     try
/*     */     {
/*  99 */       return "true".equals(this.args.get(key));
/*     */     } catch (Exception ex) {
/* 101 */       bool2 = def; } return bool2;
/*     */   }
/*     */ 
/*     */   public Object get(String key, Object def)
/*     */   {
/*     */     Object localObject1;
/*     */     try
/*     */     {
/* 112 */       Object obj = this.args.get(key);
/* 113 */       if (obj == null) {
/* 114 */         return def;
/*     */       }
/* 116 */       return obj;
/*     */     } catch (Exception ex) {
/* 118 */       localObject1 = def; } return localObject1;
/*     */   }
/*     */ 
/*     */   public Args set(String key, Object value)
/*     */   {
/* 130 */     if (this.locked) {
/* 131 */       throw new UnsupportedOperationException("Args have locked,can modify");
/*     */     }
/* 133 */     this.args.put(key, value);
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */   public Args set(String key, int value)
/*     */   {
/* 145 */     if (this.locked) {
/* 146 */       throw new UnsupportedOperationException("Args have locked,can modify");
/*     */     }
/* 148 */     this.args.put(key, new Integer(value));
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */   public Args set(String key, boolean value)
/*     */   {
/* 160 */     if (this.locked) {
/* 161 */       throw new UnsupportedOperationException("Args have locked,can modify");
/*     */     }
/* 163 */     this.args.put(key, new Boolean(value));
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */   public Args set(String key, long value)
/*     */   {
/* 175 */     if (this.locked) {
/* 176 */       throw new UnsupportedOperationException("Args have locked,can modify");
/*     */     }
/* 178 */     this.args.put(key, new Long(value));
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */   public Args set(String key, float value)
/*     */   {
/* 190 */     if (this.locked) {
/* 191 */       throw new UnsupportedOperationException("Args have locked,can modify");
/*     */     }
/* 193 */     this.args.put(key, new Float(value));
/* 194 */     return this;
/*     */   }
/*     */ 
/*     */   public Args set(String key, double value)
/*     */   {
/* 205 */     if (this.locked) {
/* 206 */       throw new UnsupportedOperationException("Args have locked,can modify");
/*     */     }
/* 208 */     this.args.put(key, new Double(value));
/* 209 */     return this;
/*     */   }
/*     */ 
/*     */   public Args lock()
/*     */   {
/* 218 */     this.locked = true;
/* 219 */     return this;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 227 */     return this.args.toString();
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.util.Args
 * JD-Core Version:    0.5.3
 */