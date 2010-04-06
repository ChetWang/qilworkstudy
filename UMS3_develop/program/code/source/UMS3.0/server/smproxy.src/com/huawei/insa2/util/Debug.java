/*     */ package com.huawei.insa2.util;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Debug
/*     */ {
/*  45 */   private static String indentString = "    ";
/*     */ 
/*  48 */   private static final String lineSeparator = System.getProperty("line.separator");
/*     */ 
/*  55 */   public static PrintWriter out = new PrintWriter(System.out);
/*     */ 
/* 630 */   private static final byte[] hexNumber = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
/*     */ 
/* 759 */   private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
/*     */ 
/* 762 */   public static final String fullInfo = "!@*#~^?'/\"";
/*     */ 
/*     */   public static final void myAssert(boolean condition)
/*     */   {
/*  39 */     if (condition) return; throw new AssertFailed();
/*     */   }
/*     */ 
/*     */   public static void setDumpStream(OutputStream os)
/*     */   {
/*  62 */     out = new PrintWriter(os);
/*     */   }
/*     */ 
/*     */   public static void setDumpStream(Writer w)
/*     */   {
/*  70 */     out = new PrintWriter(w);
/*     */   }
/*     */ 
/*     */   public static void setDumpIndent(String indent)
/*     */   {
/*  78 */     indentString = indent;
/*     */   }
/*     */ 
/*     */   public static String getDumpIndent()
/*     */   {
/*  86 */     return indentString;
/*     */   }
/*     */ 
/*     */   public static final void dump()
/*     */   {
/*  93 */     out.println(dumpHead());
/*  94 */     out.flush();
/*     */   }
/*     */ 
/*     */   public static final void dump(int i)
/*     */   {
/* 102 */     out.println(String.valueOf(String.valueOf(dumpHead())).concat(String.valueOf(String.valueOf(i))));
/* 103 */     out.flush();
/*     */   }
/*     */ 
/*     */   public static final void dump(long l)
/*     */   {
/* 111 */     out.println(String.valueOf(String.valueOf(dumpHead())).concat(String.valueOf(String.valueOf(l))));
/* 112 */     out.flush();
/*     */   }
/*     */ 
/*     */   public static final void dump(float f)
/*     */   {
/* 120 */     out.println(String.valueOf(String.valueOf(dumpHead())).concat(String.valueOf(String.valueOf(f))));
/* 121 */     out.flush();
/*     */   }
/*     */ 
/*     */   public static final void dump(double d)
/*     */   {
/* 129 */     out.println(String.valueOf(String.valueOf(dumpHead())).concat(String.valueOf(String.valueOf(d))));
/* 130 */     out.flush();
/*     */   }
/*     */ 
/*     */   public static final void dump(boolean b)
/*     */   {
/* 138 */     out.println(String.valueOf(String.valueOf(dumpHead())).concat(String.valueOf(String.valueOf(b))));
/* 139 */     out.flush();
/*     */   }
/*     */ 
/*     */   public static final void dump(char ch)
/*     */   {
/* 147 */     out.println(String.valueOf(String.valueOf(dumpHead())).concat(String.valueOf(String.valueOf(ch))));
/* 148 */     out.flush();
/*     */   }
/*     */ 
/*     */   public static final void dump(byte[] data, int offset, int length)
/*     */   {
/* 158 */     dump(dumpHead(), data, offset, length);
/*     */   }
/*     */ 
/*     */   public static final void dump(byte[] data)
/*     */   {
/* 166 */     dump(dumpHead(), data);
/*     */   }
/*     */ 
/*     */   public static final void dump(Object obj)
/*     */   {
/* 174 */     dump(dumpHead(), 3, new Vector(), obj);
/*     */   }
/*     */ 
/*     */   public static final void dump(Object obj, String prefix)
/*     */   {
/* 183 */     dump(String.valueOf(String.valueOf(prefix)).concat(String.valueOf(String.valueOf(dumpHead()))), 3, new Vector(), obj);
/*     */   }
/*     */ 
/*     */   public static final void dump(Object obj, int depth)
/*     */   {
/* 192 */     dump(dumpHead(), depth, new Vector(), obj);
/*     */   }
/*     */ 
/*     */   private static final void dump(String prefix, int depth, Vector checkCircuit, Vector v)
/*     */   {
/* 204 */     if (v == null) {
/* 205 */       dump(prefix, "null");
/* 206 */       return;
/*     */     }
/* 208 */     dumpBegin(prefix, checkCircuit, v);
/* 209 */     for (int i = 0; i < v.size(); ++i) {
/* 210 */       Object item = v.elementAt(i);
/* 211 */       StringBuffer itemPrefix = new StringBuffer();
/* 212 */       itemPrefix.append(indent(prefix));
/* 213 */       itemPrefix.append('[');
/* 214 */       itemPrefix.append(i);
/* 215 */       itemPrefix.append("] ");
/* 216 */       itemPrefix.append(formatClassName(item.getClass(), item));
/* 217 */       itemPrefix.append(" @");
/* 218 */       itemPrefix.append(System.identityHashCode(item));
/* 219 */       dump(itemPrefix.toString(), depth, checkCircuit, item);
/*     */     }
/* 221 */     dumpEnd(prefix, checkCircuit, v);
/*     */   }
/*     */ 
/*     */   private static final void dumpServletRequest(String prefix, Object request)
/*     */   {
/*     */     try
/*     */     {
/* 231 */       if (request == null) {
/* 232 */         dump(prefix, "null");
/* 233 */         return;
/*     */       }
/* 235 */       dumpBegin(prefix, new Vector(), request);
/* 236 */       Class c = request.getClass();
/* 237 */       Method m1 = null;
/* 238 */       m1 = c.getMethod("getParameterNames", new Class[0]);
/*     */ 
/* 240 */       Enumeration e = (Enumeration)m1.invoke(request, new Object[0]);
/* 241 */       while (e.hasMoreElements()) {
/* 242 */         String name = e.nextElement().toString();
/* 243 */         Method m2 = c.getMethod("getParameterValues", new Class[] { String.class });
/* 244 */         String[] values = (String[])m2.invoke(request, new Object[] { name });
/* 245 */         StringBuffer sb = new StringBuffer();
/* 246 */         for (int i = 0; i < values.length; ++i) {
/* 247 */           sb.append(values[i]);
/* 248 */           if (i != values.length - 1) {
/* 249 */             sb.append(" ; ");
/*     */           }
/*     */         }
/* 252 */         dump(indent(prefix), String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(name))).append(" = ").append(sb))));
/*     */       }
/* 254 */       dumpEnd(prefix, new Vector(), request);
/*     */     } catch (Exception ex) {
/* 256 */       ex.printStackTrace(out);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final void dump(String prefix, int depth, Vector checkCircuit, Enumeration e)
/*     */   {
/* 267 */     if (e == null) {
/* 268 */       dump(prefix, "null");
/* 269 */       return;
/*     */     }
/* 271 */     dumpBegin(prefix, checkCircuit, e);
/* 272 */     int i = 0;
/* 273 */     while (e.hasMoreElements()) {
/* 274 */       dump(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(indent(prefix)))).append('[').append(i++).append("] "))), depth, checkCircuit, e.nextElement());
/*     */     }
/* 276 */     dumpEnd(prefix, checkCircuit, e);
/*     */   }
/*     */ 
/*     */   private static final void dump(String prefix, Throwable t)
/*     */   {
/* 285 */     if (t == null) {
/* 286 */       dump(prefix, "null");
/* 287 */       return;
/*     */     }
/* 289 */     dumpBegin(prefix, new Vector(), t);
/* 290 */     t.printStackTrace(out);
/* 291 */     dumpEnd(prefix, new Vector(), t);
/*     */   }
/*     */ 
/*     */   private static final void dump(String prefix, byte[] data, int offset, int length)
/*     */   {
/* 302 */     if (data == null) {
/* 303 */       dump(prefix, "null");
/* 304 */       return;
/*     */     }
/* 306 */     if ((offset < 0) || (data.length < offset + length)) {
/* 307 */       dump(prefix, String.valueOf(String.valueOf(new StringBuffer("IndexOutOfBounds:data.length=").append(data.length).append(" offset=").append(offset).append(" length=").append(length))));
/*     */ 
/* 309 */       return;
/*     */     }
/* 311 */     dumpBegin(prefix, new Vector(), data);
/* 312 */     int end = offset + length;
/* 313 */     dump(indent(prefix), "[HEX]  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f | 0123456789abcdef");
/* 314 */     dump(indent(prefix), "------------------------------------------------------------------------");
/* 315 */     for (int i = offset; i < end; i += 16) {
/* 316 */       byte[] row = { 48, 48, 48, 48, 58, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 48, 48, 32, 124, 32, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46 };
/*     */ 
/* 324 */       setHex(row, 3, i);
/* 325 */       for (int j = i; j < i + 16; ++j) {
/* 326 */         if (j < end) {
/* 327 */           int b = data[j];
/* 328 */           if (b < 0) b += 256;
/* 329 */           setHex(row, 7 + (j - i) * 3, b);
/* 330 */           if ((b >= 32) && (b < 127))
/* 331 */             row[(56 + j - i)] = (byte)b;
/*     */         }
/*     */         else
/*     */         {
/* 335 */           row[(6 + (j - i) * 3)] = 32;
/* 336 */           row[(7 + (j - i) * 3)] = 32;
/* 337 */           row[(56 + j - i)] = 32;
/*     */         }
/*     */       }
/* 340 */       dump(indent(prefix), new String(row));
/*     */     }
/* 342 */     dumpEnd(prefix, new Vector(), data);
/*     */   }
/*     */ 
/*     */   private static final void dump(String prefix, byte[] b)
/*     */   {
/* 351 */     dump(prefix, b, 0, (b.length > 512) ? 512 : b.length);
/*     */   }
/*     */ 
/*     */   private static final void dump(String prefix, int depth, Vector checkCircuit, Map map)
/*     */   {
/* 361 */     if (map == null) {
/* 362 */       dump(prefix, "null");
/*     */     }
/* 364 */     dumpBegin(prefix, checkCircuit, map);
/* 365 */     for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
/* 366 */       Object key = i.next();
/* 367 */       Object value = map.get(key);
/* 368 */       if (value instanceof String) {
/* 369 */         dump(indent(prefix), String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(key.toString()))).append(" = ").append(value))));
/*     */       }
/* 371 */       dump(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(indent(prefix)))).append(key.toString()).append(" = "))), depth, checkCircuit, map.get(key));
/*     */     }
/*     */ 
/* 374 */     dumpEnd(prefix, checkCircuit, map);
/*     */   }
/*     */ 
/*     */   private static final void dump(String prefix, String str)
/*     */   {
/* 383 */     out.println(String.valueOf(String.valueOf(prefix)).concat(String.valueOf(String.valueOf(str))));
/* 384 */     out.flush();
/*     */   }
/*     */ 
/*     */   private static final void dump(String prefix, int depth, Vector checkCircuit, Object[] objs)
/*     */   {
/* 394 */     if (objs == null) {
/* 395 */       dump(prefix, "null");
/* 396 */       return;
/*     */     }
/* 398 */     dumpBegin(prefix, checkCircuit, objs);
/* 399 */     for (int i = 0; i < objs.length; ++i) {
/* 400 */       dump(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(indent(prefix)))).append('[').append(i).append("] "))), depth, checkCircuit, objs[i]);
/*     */     }
/* 402 */     dumpEnd(prefix, checkCircuit, objs);
/*     */   }
/*     */ 
/*     */   private static void dump(String prefix, int depth, Vector checkCircuit, Object obj)
/*     */   {
/* 413 */     if (obj == null) {
/* 414 */       dump(prefix, "null");
/* 415 */       return;
/*     */     }
/*     */     int i;
/*     */     try {
/* 419 */       if ((obj instanceof String) || (obj instanceof Number) || (obj instanceof Character) || (obj instanceof Boolean))
/*     */       {
/* 421 */         dump(prefix, obj.toString());
/* 422 */         return; }
/* 423 */       if (checkCircuit.contains(new Integer(System.identityHashCode(obj)))) {
/* 424 */         StringBuffer sb = new StringBuffer();
/* 425 */         sb.append(formatClassName(obj.getClass(), obj));
/* 426 */         sb.append(" @");
/* 427 */         sb.append(System.identityHashCode(obj));
/* 428 */         sb.append(' ');
/* 429 */         dump(prefix, " {Circle recursion!}");
/* 430 */         return; }
/* 431 */       if (getDepth(prefix) > depth) {
/* 432 */         String str = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(formatClassName(obj.getClass(), obj)))).append(" @").append(System.identityHashCode(obj))));
/*     */ 
/* 434 */         if (prefix.trim().endsWith(str.trim()))
/* 435 */           str = "";
/*     */         String toStr;
/*     */         try
/*     */         {
/* 439 */           toStr = obj.toString();
/* 440 */           if (toStr.indexOf(64) > 0)
/* 441 */             toStr = " {Stack overflow!}";
/*     */         }
/*     */         catch (StackOverflowError t) {
/* 444 */           toStr = " {Stack overflow!}";
/*     */         }
/* 446 */         dump(prefix, String.valueOf(String.valueOf(str)).concat(String.valueOf(String.valueOf(toStr))));
/* 447 */         return; }
/* 448 */       if (obj instanceof Vector) {
/* 449 */         dump(prefix, depth, checkCircuit, (Vector)obj);
/* 450 */         return; }
/* 451 */       if (obj instanceof Map) {
/* 452 */         dump(prefix, depth, checkCircuit, (Map)obj);
/* 453 */         return; }
/* 454 */       if (obj instanceof Enumeration) {
/* 455 */         dump(prefix, depth, checkCircuit, (Enumeration)obj);
/* 456 */         return; }
/* 457 */       if (obj instanceof Object[]) {
/* 458 */         dump(prefix, depth, checkCircuit, (Object[])obj);
/* 459 */         return; }
/* 460 */       if (obj instanceof Throwable) {
/* 461 */         dump(prefix, (Throwable)obj);
/* 462 */         return; }
/* 463 */       if (obj instanceof byte[]) {
/* 464 */         dump(prefix, (byte[])obj);
/* 465 */         return; }
/* 466 */       if (obj.getClass().isArray()) {
/* 467 */         int len = Array.getLength(obj);
/* 468 */         dumpBegin(prefix, checkCircuit, obj);
/* 469 */         StringBuffer content = new StringBuffer();
/* 470 */         for (i = 0; i < len; ++i) {
/* 471 */           content.append(fixLength(Array.get(obj, i).toString(), 4));
/* 472 */           if ((i % 8 == 7) && (i < len - 1)) {
/* 473 */             content.append(String.valueOf(String.valueOf(lineSeparator)).concat(String.valueOf(String.valueOf(indent(prefix)))));
/*     */           }
/*     */         }
/* 476 */         dump(indent(prefix), content.toString());
/* 477 */         dumpEnd(prefix, checkCircuit, obj);
/* 478 */         return; }
/* 479 */       if (!(Class.forName("javax.servlet.ServletRequest").isInstance(obj))) break label499;
/* 480 */       dumpServletRequest(prefix, obj);
/* 481 */       label499: return;
/*     */     }
/*     */     catch (ClassNotFoundException c)
/*     */     {
/* 488 */       dumpBegin(prefix, checkCircuit, obj);
/* 489 */       Class c = obj.getClass();
/*     */ 
/* 491 */       while (c != null) {
/*     */         Field[] f;
/*     */         try { f = c.getDeclaredFields();
/*     */         } catch (SecurityException ex2) {
/* 495 */           dump(indent(prefix), "Can't dump object member for security reason.");
/* 496 */           return;
/*     */         }
/*     */ 
/* 499 */         for (i = 0; i < f.length; ++i) {
/* 500 */           String m = Modifier.toString(f[i].getModifiers());
/* 501 */           if (m.indexOf("static") > 0)
/*     */             continue;
/* 503 */           String n = f[i].getName();
/* 504 */           Object v = "[unkonwn]";
/*     */           try {
/* 506 */             f[i].setAccessible(true); } catch (SecurityException localSecurityException1) {
/*     */           }
/*     */           try {
/* 509 */             v = f[i].get(obj);
/* 510 */             if (v != null)
/* 511 */               if (v instanceof String) {
/* 512 */                 v = String.valueOf(String.valueOf(new StringBuffer("\"").append(v).append('"')));
/* 513 */               } else if (v instanceof Character) {
/* 514 */                 char cv = ((Character)v).charValue();
/* 515 */                 if (cv < ' ') {
/* 516 */                   StringBuffer sbv = new StringBuffer();
/* 517 */                   sbv.append("\\u");
/* 518 */                   sbv.append(Integer.toHexString(cv));
/* 519 */                   while (sbv.length() < 6) {
/* 520 */                     sbv.insert(2, '0');
/*     */                   }
/* 522 */                   v = sbv;
/*     */                 }
/* 524 */                 v = String.valueOf(String.valueOf(new StringBuffer("'").append(v).append('\'')));
/*     */               }
/*     */           } catch (Exception localException) {
/*     */           }
/* 528 */           Class ct = f[i].getType();
/* 529 */           String t = formatClassName(ct, v);
/* 530 */           dump(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(indent(prefix)))).append(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(m))).append(' ').append(t).append(' ').append(n))).trim()).append(" = "))), depth, checkCircuit, v);
/*     */         }
/*     */ 
/* 533 */         c = c.getSuperclass();
/*     */       }
/* 535 */       dumpEnd(prefix, checkCircuit, obj);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void dumpBegin(String prefix, Vector checkCircuit, Object obj)
/*     */   {
/* 544 */     String className = formatClassName(obj.getClass(), obj);
/* 545 */     int address = System.identityHashCode(obj);
/* 546 */     checkCircuit.addElement(new Integer(address));
/* 547 */     if (obj instanceof Array) {
/* 548 */       className = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(className.substring(2)))).append('[').append(Array.getLength(obj)).append("] ")));
/*     */     }
/* 550 */     if (className.startsWith("java.lang.")) {
/* 551 */       className = className.substring(10);
/*     */     }
/* 553 */     if (prefix.trim().endsWith("@".concat(String.valueOf(String.valueOf(address)))))
/* 554 */       out.println(String.valueOf(String.valueOf(prefix)).concat(" {"));
/*     */     else
/* 556 */       out.println(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(prefix))).append(className).append(" @").append(address).append(" {"))));
/*     */   }
/*     */ 
/*     */   private static void dumpEnd(String prefix, Vector checkCircuit, Object obj)
/*     */   {
/* 565 */     checkCircuit.removeElement(new Integer(System.identityHashCode(obj)));
/* 566 */     int p = prefix.lastIndexOf(indentString);
/* 567 */     if (p > 0) {
/* 568 */       prefix = String.valueOf(String.valueOf(prefix.substring(0, p))).concat(String.valueOf(String.valueOf(indentString)));
/*     */     }
/* 570 */     for (int i = 0; i < prefix.length(); ++i) {
/* 571 */       char c = prefix.charAt(i);
/* 572 */       if ((c != '\t') && (c != ' ')) break;
/* 573 */       out.print(c);
/*     */     }
/*     */ 
/* 578 */     out.println("}");
/* 579 */     out.flush(); }
/*     */ 
/*     */   public static String locate(String esc) {
/* 588 */     StringWriter sw = new StringWriter();
/* 589 */     new Exception().printStackTrace(new PrintWriter(sw));
/* 590 */     StringTokenizer st = new StringTokenizer(sw.toString(), "\n");
/*     */     String str;
/*     */     do do do {
/* 591 */           if (!(st.hasMoreTokens())) break label163;
/* 592 */           str = st.nextToken(); }
/* 593 */         while (str.indexOf("Exception") != -1);
/*     */ 
/* 595 */       while (str.indexOf(Debug.class.getName()) != -1);
/*     */ 
/* 597 */     while ((esc != null) && (str.indexOf(esc) != -1));
/*     */ 
/* 599 */     if (esc == "!@*#~^?'/\"") {
/* 600 */       return str;
/*     */     }
/* 602 */     int i = str.indexOf(40);
/* 603 */     int j = str.indexOf(41);
/* 604 */     if ((i != -1) && (j != -1)) {
/* 605 */       return str.substring(i, j + 1);
/*     */     }
/*     */ 
/* 610 */     label163: return "";
/*     */   }
/*     */ 
/*     */   private static void setHex(byte[] src, int lowByte, int value)
/*     */   {
/* 620 */     for (int i = 0; i < 8; ++i) {
/* 621 */       src[(lowByte - i)] = hexNumber[(value & 0xF)];
/* 622 */       value >>>= 4;
/* 623 */       if (value == 0)
/*     */         return;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String indent(String prefix)
/*     */   {
/* 642 */     int p = prefix.lastIndexOf(indentString);
/* 643 */     if (p > 0) {
/* 644 */       prefix = String.valueOf(String.valueOf(prefix.substring(0, p))).concat(String.valueOf(String.valueOf(indentString)));
/*     */     }
/* 646 */     StringBuffer sb = new StringBuffer();
/* 647 */     for (int i = 0; i < prefix.length(); ++i) {
/* 648 */       char c = prefix.charAt(i);
/* 649 */       if ((c != '\t') && (c != ' ')) break;
/* 650 */       sb.append(c);
/*     */     }
/*     */ 
/* 655 */     sb.append(indentString);
/* 656 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private static String formatClassName(Class c, Object obj)
/*     */   {
/* 665 */     String t = c.getName();
/*     */ 
/* 668 */     if (t.charAt(t.length() - 1) == ';') {
/* 669 */       t = t.substring(0, t.length() - 1);
/*     */     }
/*     */ 
/* 673 */     boolean isArray = false;
/* 674 */     boolean firstDimension = true;
/* 675 */     while (t.startsWith("[")) {
/* 676 */       isArray = true;
/* 677 */       if ((firstDimension) && (obj != null)) {
/* 678 */         t = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(t.substring(1)))).append('[').append(Array.getLength(obj)).append(']')));
/* 679 */         firstDimension = false;
/*     */       }
/* 681 */       t = String.valueOf(String.valueOf(t.substring(1))).concat("[]");
/*     */     }
/*     */ 
/* 684 */     if (isArray) {
/* 685 */       char ch = t.charAt(0);
/* 686 */       t = t.substring(1);
/* 687 */       switch (ch) { case 'B':
/* 689 */         t = "byte".concat(String.valueOf(String.valueOf(t))); break;
/*     */       case 'C':
/* 691 */         t = "char".concat(String.valueOf(String.valueOf(t))); break;
/*     */       case 'F':
/* 693 */         t = "float".concat(String.valueOf(String.valueOf(t))); break;
/*     */       case 'I':
/* 695 */         t = "int".concat(String.valueOf(String.valueOf(t))); break;
/*     */       case 'J':
/* 697 */         t = "long".concat(String.valueOf(String.valueOf(t))); break;
/*     */       case 'S':
/* 699 */         t = "short".concat(String.valueOf(String.valueOf(t))); break;
/*     */       case 'Z':
/* 701 */         t = "boolean".concat(String.valueOf(String.valueOf(t)));
/*     */       case 'D':
/*     */       case 'E':
/*     */       case 'G':
/*     */       case 'H':
/*     */       case 'K':
/*     */       case 'L':
/*     */       case 'M':
/*     */       case 'N':
/*     */       case 'O':
/*     */       case 'P':
/*     */       case 'Q':
/*     */       case 'R':
/*     */       case 'T':
/*     */       case 'U':
/*     */       case 'V':
/*     */       case 'W':
/*     */       case 'X':
/*     */       case 'Y': }  } if (t.startsWith("java.lang."))
/* 705 */       t = t.substring(10);
/* 706 */     else if (t.startsWith("class ")) {
/* 707 */       t = t.substring(7);
/*     */     }
/* 709 */     return t;
/*     */   }
/*     */ 
/*     */   private static String fixLength(String str, int len)
/*     */   {
/* 720 */     StringBuffer sb = new StringBuffer(len);
/* 721 */     sb.append(str);
/* 722 */     int n = len - (str.length() % len);
/* 723 */     for (int i = 0; i < n; ++i) {
/* 724 */       sb.append(' ');
/*     */     }
/* 726 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private static String dumpHead()
/*     */   {
/* 734 */     StringBuffer sb = new StringBuffer();
/* 735 */     sb.append(sdf.format(new Date()));
/* 736 */     sb.append(locate(null));
/* 737 */     sb.append(' ');
/* 738 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private static int getDepth(String prefix)
/*     */   {
/* 747 */     int count = 0;
/* 748 */     int indentLen = indentString.length();
/* 749 */     int i = -indentLen;
/*     */     do {
/* 751 */       ++count;
/* 752 */       i = prefix.indexOf(indentString, i + indentLen); }
/* 753 */     while (i >= 0);
/* 754 */     return count;
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.util.Debug
 * JD-Core Version:    0.5.3
 */