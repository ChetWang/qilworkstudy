/*     */ package com.huawei.insa2.util;
/*     */ 
/*     */ public class TypeConvert
/*     */ {
/*     */   public static int byte2int(byte[] b, int offset)
/*     */   {
/*  17 */     return (b[(offset + 3)] & 0xFF | (b[(offset + 2)] & 0xFF) << 8 | (b[(offset + 1)] & 0xFF) << 16 | (b[offset] & 0xFF) << 24);
/*     */   }
/*     */ 
/*     */   public static int byte2int(byte[] b)
/*     */   {
/*  27 */     return (b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24);
/*     */   }
/*     */ 
/*     */   public static long byte2long(byte[] b)
/*     */   {
/*  37 */     return (b[7] & 0xFF | (b[6] & 0xFF) << 8 | (b[5] & 0xFF) << 16 | (b[4] & 0xFF) << 24 | (b[3] & 0xFF) << 32 | (b[2] & 0xFF) << 40 | (b[1] & 0xFF) << 48 | b[0] << 56);
/*     */   }
/*     */ 
/*     */   public static long byte2long(byte[] b, int offset)
/*     */   {
/*  49 */     return (b[(offset + 7)] & 0xFF | (b[(offset + 6)] & 0xFF) << 8 | (b[(offset + 5)] & 0xFF) << 16 | (b[(offset + 4)] & 0xFF) << 24 | (b[(offset + 3)] & 0xFF) << 32 | (b[(offset + 2)] & 0xFF) << 40 | (b[(offset + 1)] & 0xFF) << 48 | b[offset] << 56);
/*     */   }
/*     */ 
/*     */   public static byte[] int2byte(int n)
/*     */   {
/*  61 */     byte[] b = new byte[4];
/*  62 */     b[0] = (byte)(n >> 24);
/*  63 */     b[1] = (byte)(n >> 16);
/*  64 */     b[2] = (byte)(n >> 8);
/*  65 */     b[3] = (byte)n;
/*  66 */     return b;
/*     */   }
/*     */ 
/*     */   public static void int2byte(int n, byte[] buf, int offset)
/*     */   {
/*  76 */     buf[offset] = (byte)(n >> 24);
/*  77 */     buf[(offset + 1)] = (byte)(n >> 16);
/*  78 */     buf[(offset + 2)] = (byte)(n >> 8);
/*  79 */     buf[(offset + 3)] = (byte)n;
/*     */   }
/*     */ 
/*     */   public static byte[] short2byte(int n)
/*     */   {
/*  88 */     byte[] b = new byte[2];
/*  89 */     b[0] = (byte)(n >> 8);
/*  90 */     b[1] = (byte)n;
/*  91 */     return b;
/*     */   }
/*     */ 
/*     */   public static void short2byte(int n, byte[] buf, int offset)
/*     */   {
/* 101 */     buf[offset] = (byte)(n >> 8);
/* 102 */     buf[(offset + 1)] = (byte)n;
/*     */   }
/*     */ 
/*     */   public static byte[] long2byte(long n)
/*     */   {
/* 111 */     byte[] b = new byte[8];
/*     */ 
/* 113 */     b[0] = (byte)(int)(n >> 56);
/* 114 */     b[1] = (byte)(int)(n >> 48);
/* 115 */     b[2] = (byte)(int)(n >> 40);
/* 116 */     b[3] = (byte)(int)(n >> 32);
/* 117 */     b[4] = (byte)(int)(n >> 24);
/* 118 */     b[5] = (byte)(int)(n >> 16);
/* 119 */     b[6] = (byte)(int)(n >> 8);
/* 120 */     b[7] = (byte)(int)n;
/* 121 */     return b;
/*     */   }
/*     */ 
/*     */   public static void long2byte(long n, byte[] buf, int offset)
/*     */   {
/* 132 */     buf[offset] = (byte)(int)(n >> 56);
/* 133 */     buf[(offset + 1)] = (byte)(int)(n >> 48);
/* 134 */     buf[(offset + 2)] = (byte)(int)(n >> 40);
/* 135 */     buf[(offset + 3)] = (byte)(int)(n >> 32);
/* 136 */     buf[(offset + 4)] = (byte)(int)(n >> 24);
/* 137 */     buf[(offset + 5)] = (byte)(int)(n >> 16);
/* 138 */     buf[(offset + 6)] = (byte)(int)(n >> 8);
/* 139 */     buf[(offset + 7)] = (byte)(int)n;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.util.TypeConvert
 * JD-Core Version:    0.5.3
 */