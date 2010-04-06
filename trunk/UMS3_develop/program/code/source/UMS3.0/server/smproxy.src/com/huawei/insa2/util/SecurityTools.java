/*     */ package com.huawei.insa2.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.DigestException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.KeySpec;
/*     */ import javax.crypto.BadPaddingException;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.IllegalBlockSizeException;
/*     */ import javax.crypto.NoSuchPaddingException;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.DESKeySpec;
/*     */ import javax.crypto.spec.DESedeKeySpec;
/*     */ 
/*     */ public class SecurityTools
/*     */ {
/*  15 */   private static final byte[] salt = "webplat".getBytes();
/*     */ 
/*     */   public static String digest(String str)
/*     */   {
/*     */     try
/*     */     {
/*  25 */       MessageDigest md5 = MessageDigest.getInstance("SHA");
/*  26 */       md5.update(salt);
/*  27 */       return Base64.encode(md5.digest(str.getBytes()));
/*     */     } catch (NoSuchAlgorithmException ex) {
/*  29 */       throw new UnsupportedOperationException(ex.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void md5(byte[] data, int offset, int length, byte[] digest, int dOffset)
/*     */   {
/*     */     try
/*     */     {
/*  44 */       MessageDigest md5 = MessageDigest.getInstance("MD5");
/*  45 */       md5.update(data, offset, length);
/*  46 */       md5.digest(digest, dOffset, 16);
/*     */     } catch (NoSuchAlgorithmException ex) {
/*  48 */       ex.printStackTrace();
/*     */     } catch (DigestException ex) {
/*  50 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static byte[] md5(byte[] data, int offset, int length)
/*     */   {
/*     */     byte[] arrayOfByte;
/*     */     try
/*     */     {
/*  63 */       MessageDigest md5 = MessageDigest.getInstance("MD5");
/*  64 */       md5.update(data, offset, length);
/*  65 */       return md5.digest();
/*     */     } catch (NoSuchAlgorithmException ex) {
/*  67 */       ex.printStackTrace();
/*  68 */       arrayOfByte = null; } return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static byte[] encrypt(byte[] key, byte[] src)
/*     */   {
/*     */     try
/*     */     {
/*  79 */       return getCipher(key, 1).doFinal(src);
/*     */     } catch (BadPaddingException ex) {
/*  81 */       throw new UnsupportedOperationException(ex.toString());
/*     */     } catch (IllegalBlockSizeException ex) {
/*  83 */       throw new UnsupportedOperationException(ex.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String encrypt(String key, String src)
/*     */   {
/*     */     try
/*     */     {
/*  95 */       return Base64.encode(getCipher(key.getBytes("UTF8"), 1).doFinal(src.getBytes("UTF8")));
/*     */     }
/*     */     catch (UnsupportedEncodingException ex) {
/*  98 */       throw new UnsupportedOperationException(ex.toString());
/*     */     } catch (BadPaddingException ex) {
/* 100 */       throw new UnsupportedOperationException(ex.toString());
/*     */     } catch (IllegalBlockSizeException ex) {
/* 102 */       throw new UnsupportedOperationException(ex.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static byte[] decrypt(byte[] key, byte[] src)
/*     */   {
/*     */     try
/*     */     {
/* 113 */       return getCipher(key, 2).doFinal(src);
/*     */     } catch (IllegalBlockSizeException ex) {
/* 115 */       throw new UnsupportedOperationException(ex.toString());
/*     */     } catch (BadPaddingException ex) {
/* 117 */       throw new UnsupportedOperationException(ex.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String decrypt(String key, String src)
/*     */   {
/*     */     try
/*     */     {
/* 128 */       return new String(getCipher(key.getBytes("UTF8"), 2).doFinal(Base64.decode(src)), "UTF8");
/*     */     }
/*     */     catch (UnsupportedEncodingException ex) {
/* 131 */       throw new UnsupportedOperationException(ex.toString());
/*     */     } catch (BadPaddingException ex) {
/* 133 */       throw new UnsupportedOperationException(ex.toString());
/*     */     } catch (IllegalBlockSizeException ex) {
/* 135 */       throw new UnsupportedOperationException(ex.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Cipher getCipher(byte[] key, int mode)
/*     */   {
/*     */     try
/*     */     {
/* 155 */       if (key.length < 8) {
/* 156 */         byte[] oldkey = key;
/* 157 */         key = new byte[8];
/* 158 */         System.arraycopy(oldkey, 0, key, 0, oldkey.length);
/*     */       }
/*     */       SecretKeyFactory keyFactory;
/*     */       KeySpec keySpec;
/*     */       Cipher c;
/* 160 */       if (key.length >= 24) {
/* 161 */         keyFactory = SecretKeyFactory.getInstance("DESede");
/* 162 */         keySpec = new DESedeKeySpec(key);
/* 163 */         c = Cipher.getInstance("DESede");
/*     */       } else {
/* 165 */         keyFactory = SecretKeyFactory.getInstance("DES");
/* 166 */         keySpec = new DESKeySpec(key);
/* 167 */         c = Cipher.getInstance("DES");
/*     */       }
/* 169 */       SecretKey k = keyFactory.generateSecret(keySpec);
/* 170 */       c.init(mode, k);
/* 171 */       return c;
/*     */     } catch (NoSuchAlgorithmException ex) {
/* 173 */       throw new UnsupportedOperationException(ex.toString());
/*     */     } catch (InvalidKeyException ex) {
/* 175 */       throw new UnsupportedOperationException(ex.toString());
/*     */     } catch (NoSuchPaddingException ex) {
/* 177 */       throw new UnsupportedOperationException(ex.toString());
/*     */     } catch (InvalidKeySpecException ex) {
/* 179 */       throw new UnsupportedOperationException(ex.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 188 */     Debug.dump(digest("hello world"));
/* 189 */     for (int i = 0; i < 1000; ++i) {
/* 190 */       decrypt("key", encrypt("key", "hello world"));
/*     */     }
/* 192 */     Debug.dump(digest("hello world"));
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.util.SecurityTools
 * JD-Core Version:    0.5.3
 */