/*     */ package com.huawei.insa2.util;
/*     */ 
/*     */ public class Base64
/*     */ {
/*  11 */   private static char[] Base64Code = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*     */ 
/*  19 */   private static byte[] Base64Decode = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 63, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, 0, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };
/*     */ 
/*     */   public static String encode(byte[] b)
/*     */   {
/*  41 */     int code = 0;
/*     */ 
/*  44 */     StringBuffer sb = new StringBuffer((b.length - 1) / 3 << 6);
/*     */ 
/*  47 */     for (int i = 0; i < b.length; ++i) {
/*  48 */       code |= b[i] << 16 - (i % 3 * 8) & 255 << 16 - (i % 3 * 8);
/*  49 */       if ((i % 3 == 2) || (i == b.length - 1)) {
/*  50 */         sb.append(Base64Code[((code & 0xFC0000) >>> 18)]);
/*  51 */         sb.append(Base64Code[((code & 0x3F000) >>> 12)]);
/*  52 */         sb.append(Base64Code[((code & 0xFC0) >>> 6)]);
/*  53 */         sb.append(Base64Code[(code & 0x3F)]);
/*  54 */         code = 0;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  60 */     if (b.length % 3 > 0) {
/*  61 */       sb.setCharAt(sb.length() - 1, '=');
/*     */     }
/*  63 */     if (b.length % 3 == 1) {
/*  64 */       sb.setCharAt(sb.length() - 2, '=');
/*     */     }
/*  66 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static byte[] decode(String code)
/*     */   {
/*  77 */     if (code == null) {
/*  78 */       return null;
/*     */     }
/*  80 */     int len = code.length();
/*  81 */     if (len % 4 != 0) {
/*  82 */       throw new IllegalArgumentException("Base64 string length must be 4*n");
/*     */     }
/*  84 */     if (code.length() == 0) {
/*  85 */       return new byte[0];
/*     */     }
/*     */ 
/*  89 */     int pad = 0;
/*  90 */     if (code.charAt(len - 1) == '=') {
/*  91 */       ++pad;
/*     */     }
/*  93 */     if (code.charAt(len - 2) == '=') {
/*  94 */       ++pad;
/*     */     }
/*     */ 
/*  98 */     int retLen = len / 4 * 3 - pad;
/*     */ 
/* 101 */     byte[] ret = new byte[retLen];
/*     */ 
/* 106 */     for (int i = 0; i < len; i += 4) {
/* 107 */       int j = i / 4 * 3;
/* 108 */       char ch1 = code.charAt(i);
/* 109 */       char ch2 = code.charAt(i + 1);
/* 110 */       char ch3 = code.charAt(i + 2);
/* 111 */       char ch4 = code.charAt(i + 3);
/* 112 */       int tmp = Base64Decode[ch1] << 18 | Base64Decode[ch2] << 12 | Base64Decode[ch3] << 6 | Base64Decode[ch4];
/*     */ 
/* 114 */       ret[j] = (byte)((tmp & 0xFF0000) >> 16);
/* 115 */       if (i < len - 4) {
/* 116 */         ret[(j + 1)] = (byte)((tmp & 0xFF00) >> 8);
/* 117 */         ret[(j + 2)] = (byte)(tmp & 0xFF);
/*     */       }
/*     */       else {
/* 120 */         if (j + 1 < retLen) {
/* 121 */           ret[(j + 1)] = (byte)((tmp & 0xFF00) >> 8);
/*     */         }
/* 123 */         if (j + 2 < retLen) {
/* 124 */           ret[(j + 2)] = (byte)(tmp & 0xFF);
/*     */         }
/*     */       }
/*     */     }
/* 128 */     return ret;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.util.Base64
 * JD-Core Version:    0.5.3
 */