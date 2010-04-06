/*     */ package com.huawei.insa2.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ 
/*     */ public class Resource
/*     */ {
/*     */   private Cfg resource;
/*     */ 
/*     */   public Resource(String url)
/*     */     throws IOException
/*     */   {
/*  24 */     init(url);
/*     */   }
/*     */ 
/*     */   public Resource(Class c, String url)
/*     */     throws IOException
/*     */   {
/*  34 */     String className = c.getName();
/*     */ 
/*  36 */     int i = className.lastIndexOf(46);
/*  37 */     if (i > 0) {
/*  38 */       className = className.substring(i + 1);
/*     */     }
/*  40 */     URL u = new URL(c.getResource(String.valueOf(String.valueOf(className)).concat(".class")), url);
/*  41 */     init(u.toString());
/*     */   }
/*     */ 
/*     */   public void init(String url)
/*     */     throws IOException
/*     */   {
/*  48 */     String str = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(url))).append('_').append(Locale.getDefault())));
/*  49 */     InputStream in = null;
/*     */     while (true)
/*     */       try
/*     */       {
/*  53 */         this.resource = new Cfg(String.valueOf(String.valueOf(str)).concat(".xml"), false);
/*  54 */         return;
/*     */       } catch (IOException ex) {
/*  56 */         int i = str.lastIndexOf(95);
/*  57 */         if (i < 0) {
/*  58 */           throw new MissingResourceException(String.valueOf(String.valueOf(new StringBuffer("Can't find resource url:").append(url).append(".xml"))), super.getClass().getName(), null);
/*     */         }
/*     */ 
/*  61 */         str = str.substring(0, i);
/*     */       }
/*     */   }
/*     */ 
/*     */   public String get(String key)
/*     */   {
/*  74 */     return this.resource.get(key, key);
/*     */   }
/*     */ 
/*     */   public String[] childrenNames(String key)
/*     */   {
/*  84 */     return this.resource.childrenNames(key);
/*     */   }
/*     */ 
/*     */   public String get(String key, Object[] params) {
/* 103 */     String value = this.resource.get(key, key);
/*     */     String str2;
/*     */     try {
/* 105 */       return MessageFormat.format(value, params);
/*     */     }
/*     */     catch (Exception ex) {
/* 108 */       ex.printStackTrace();
/* 109 */       str2 = key; } return str2;
/*     */   }
/*     */ 
/*     */   public String get(String key, Object param)
/*     */   {
/* 119 */     return get(key, new Object[] { param });
/*     */   }
/*     */ 
/*     */   public String get(String key, Object param1, Object param2)
/*     */   {
/* 128 */     return get(key, new Object[] { param1, param2 });
/*     */   }
/*     */ 
/*     */   public String get(String key, Object param1, Object param2, Object param3)
/*     */   {
/* 138 */     return get(key, new Object[] { param1, param2, param3 });
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.util.Resource
 * JD-Core Version:    0.5.3
 */