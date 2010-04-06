/*     */ package com.huawei.insa2.util;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class Cfg
/*     */ {
/*     */   private static DocumentBuilderFactory factory;
/*     */   private static DocumentBuilder builder;
/*  24 */   private static final String XML_HEAD = String.valueOf(String.valueOf(new StringBuffer("<?xml version=\"1.0\" encoding=\"").append(System.getProperty("file.encoding")).append("\"?>")));
/*     */ 
/*  28 */   private static String indent = "  ";
/*     */   private boolean isDirty;
/*     */   private Document doc;
/*     */   private Element root;
/*     */   private String file;
/*     */ 
/*     */   public Cfg(String url)
/*     */     throws IOException
/*     */   {
/*  47 */     this(url, false);
/*     */   }
/*     */ 
/*     */   public Cfg(String url, boolean create)
/*     */     throws IOException
/*     */   {
/*  59 */     if (url == null) {
/*  60 */       throw new IllegalArgumentException("url is null");
/*     */     }
/*  62 */     if (url.indexOf(58) > 1)
/*  63 */       this.file = url;
/*     */     else {
/*  65 */       this.file = new File(url).toURL().toString();
/*     */     }
/*     */ 
/*  68 */     new URL(this.file);
/*     */     try {
/*  70 */       load();
/*     */     } catch (FileNotFoundException ex) {
/*  72 */       if (!(create)) {
/*  73 */         throw ex;
/*     */       }
/*  75 */       loadXMLParser();
/*  76 */       this.doc = builder.newDocument();
/*  77 */       this.root = this.doc.createElement("config");
/*  78 */       this.doc.appendChild(this.root);
/*  79 */       this.isDirty = true;
/*  80 */       flush();
/*  81 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Args getArgs(String key)
/*     */   {
/*  91 */     Map args = new HashMap();
/*  92 */     String[] children = childrenNames(key);
/*  93 */     for (int i = 0; i < children.length; ++i) {
/*  94 */       args.put(children[i], get(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(key))).append('/').append(children[i]))), null));
/*     */     }
/*  96 */     return new Args(args);
/*     */   }
/*     */ 
/*     */   private static void writeIndent(PrintWriter pw, int level)
/*     */   {
/* 105 */     for (int i = 0; i < level; ++i)
/* 106 */       pw.print(indent);
/*     */   }
/*     */ 
/*     */   private static void writeNode(Node node, PrintWriter pw, int deep)
/*     */   {
/*     */     int i;
/*     */     int i;
/* 117 */     switch (node.getNodeType())
/*     */     {
/*     */     case 8:
/* 119 */       writeIndent(pw, deep);
/* 120 */       pw.print("<!--");
/* 121 */       pw.print(node.getNodeValue());
/* 122 */       pw.println("-->");
/* 123 */       return;
/*     */     case 3:
/* 125 */       String value = node.getNodeValue().trim();
/* 126 */       if (value.length() == 0) {
/* 127 */         return;
/*     */       }
/* 129 */       writeIndent(pw, deep);
/* 130 */       for (i = 0; i < value.length(); ++i) {
/* 131 */         char c = value.charAt(i);
/* 132 */         switch (c)
/*     */         {
/*     */         case '<':
/* 134 */           pw.print("&lt;");
/* 135 */           break;
/*     */         case '>':
/* 137 */           pw.print("&lt;");
/* 138 */           break;
/*     */         case '&':
/* 140 */           pw.print("&amp;");
/* 141 */           break;
/*     */         case '\'':
/* 143 */           pw.print("&apos;");
/* 144 */           break;
/*     */         case '"':
/* 146 */           pw.print("&quot;");
/* 147 */           break;
/*     */         default:
/* 149 */           pw.print(c);
/*     */         }
/*     */       }
/* 152 */       pw.println();
/* 153 */       return;
/*     */     case 1:
/* 155 */       if (!(node.hasChildNodes())) {
/* 156 */         return;
/*     */       }
/* 158 */       for (i = 0; i < deep; ++i) {
/* 159 */         pw.print(indent);
/*     */       }
/* 161 */       String nodeName = node.getNodeName();
/* 162 */       pw.print('<');
/* 163 */       pw.print(nodeName);
/*     */ 
/* 166 */       NamedNodeMap nnm = node.getAttributes();
/* 167 */       if (nnm != null) {
/* 168 */         for (int i = 0; i < nnm.getLength(); ++i) {
/* 169 */           Node attr = nnm.item(i);
/* 170 */           pw.print(' ');
/* 171 */           pw.print(attr.getNodeName());
/* 172 */           pw.print("=\"");
/* 173 */           pw.print(attr.getNodeValue());
/* 174 */           pw.print('"');
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 179 */       if (node.hasChildNodes()) {
/* 180 */         NodeList children = node.getChildNodes();
/* 181 */         if (children.getLength() == 0) {
/* 182 */           pw.print('<');
/* 183 */           pw.print(nodeName);
/* 184 */           pw.println("/>");
/* 185 */           return;
/*     */         }
/* 187 */         if (children.getLength() == 1) {
/* 188 */           Node n = children.item(0);
/* 189 */           if (n.getNodeType() == 3) {
/* 190 */             String v = n.getNodeValue();
/* 191 */             if (v != null) {
/* 192 */               v = v.trim();
/*     */             }
/* 194 */             if ((v == null) || (v.length() == 0)) {
/* 195 */               pw.println(" />");
/* 196 */               return;
/*     */             }
/* 198 */             pw.print('>');
/* 199 */             pw.print(v);
/* 200 */             pw.print("</");
/* 201 */             pw.print(nodeName);
/* 202 */             pw.println('>');
/* 203 */             return;
/*     */           }
/*     */         }
/*     */ 
/* 207 */         pw.println(">");
/* 208 */         for (i = 0; i < children.getLength(); ++i) {
/* 209 */           writeNode(children.item(i), pw, deep + 1);
/*     */         }
/* 211 */         for (i = 0; i < deep; ++i) {
/* 212 */           pw.print(indent);
/*     */         }
/* 214 */         pw.print("</");
/* 215 */         pw.print(nodeName);
/* 216 */         pw.println(">");
/*     */       } else {
/* 218 */         pw.println("/>");
/*     */       }
/* 220 */       return;
/*     */     case 9:
/* 222 */       pw.println(XML_HEAD);
/* 223 */       NodeList nl = node.getChildNodes();
/* 224 */       for (i = 0; i < nl.getLength(); ++i) {
/* 225 */         writeNode(nl.item(i), pw, 0);
/*     */       }
/* 227 */       return;
/*     */     case 2:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     }
/*     */   }
/*     */ 
/*     */   private Node findNode(String key) {
/* 237 */     Node ancestor = this.root;
/* 238 */     StringTokenizer st = new StringTokenizer(key, "/");
/* 239 */     if (st.hasMoreTokens()) {
/* 240 */       String nodeName = st.nextToken();
/* 241 */       NodeList nl = ancestor.getChildNodes();
/* 242 */       for (int i = 0; ; ++i) { if (i < nl.getLength());
/* 243 */         Node n = nl.item(i);
/* 244 */         if (nodeName.equals(n.getNodeName())) {
/* 245 */           ancestor = n;
/* 246 */           if (!(st.hasMoreTokens()));
/* 247 */           return n;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 253 */     return null;
/*     */   }
/*     */ 
/*     */   private Node createNode(String key)
/*     */   {
/* 262 */     Node ancestor = this.root;
/*     */ 
/* 264 */     StringTokenizer st = new StringTokenizer(key, "/");
/* 265 */     if (st.hasMoreTokens()) {
/* 266 */       String nodeName = st.nextToken();
/* 267 */       NodeList nl = ancestor.getChildNodes();
/* 268 */       for (int i = 0; i < nl.getLength(); ++i) {
/* 269 */         Node n = nl.item(i);
/* 270 */         if (nodeName.equals(n.getNodeName())) {
/* 271 */           ancestor = n;
/* 272 */           if (!(st.hasMoreTokens()));
/* 275 */           return ancestor;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */       while (true)
/*     */       {
/* 282 */         Node n = this.doc.createElement(nodeName);
/* 283 */         ancestor.appendChild(n);
/* 284 */         ancestor = n;
/* 285 */         if (!(st.hasMoreTokens())) {
/* 286 */           return ancestor;
/*     */         }
/* 288 */         nodeName = st.nextToken();
/*     */       }
/*     */     }
/* 291 */     return null;
/*     */   }
/*     */ 
/*     */   private Node createNode(Node ancestor, String key)
/*     */   {
/* 303 */     StringTokenizer st = new StringTokenizer(key, "/");
/* 304 */     if (st.hasMoreTokens()) {
/* 305 */       String nodeName = st.nextToken();
/* 306 */       NodeList nl = ancestor.getChildNodes();
/* 307 */       for (int i = 0; i < nl.getLength(); ++i) {
/* 308 */         if (nodeName.equals(nl.item(i).getNodeName())) {
/* 309 */           ancestor = nl.item(i);
/*     */         }
/*     */       }
/*     */ 
/* 313 */       return null;
/*     */     }
/* 315 */     return ancestor;
/*     */   }
/*     */ 
/*     */   public String get(String key, String def)
/*     */   {
/* 325 */     if (key == null) {
/* 326 */       throw new NullPointerException("parameter key is null");
/*     */     }
/*     */ 
/* 329 */     Node node = findNode(key);
/* 330 */     if (node == null) {
/* 331 */       return def;
/*     */     }
/* 333 */     NodeList nl = node.getChildNodes();
/* 334 */     for (int i = 0; i < nl.getLength(); ++i) {
/* 335 */       if (nl.item(i).getNodeType() == 3) {
/* 336 */         return nl.item(i).getNodeValue().trim();
/*     */       }
/*     */     }
/* 339 */     node.appendChild(this.doc.createTextNode(def));
/* 340 */     return def;
/*     */   }
/*     */ 
/*     */   public void put(String key, String value)
/*     */   {
/* 350 */     if (key == null) {
/* 351 */       throw new NullPointerException("parameter key is null");
/*     */     }
/* 353 */     if (value == null) {
/* 354 */       throw new NullPointerException("parameter value is null");
/*     */     }
/* 356 */     value = value.trim();
/* 357 */     Node node = createNode(key);
/*     */ 
/* 360 */     NodeList nl = node.getChildNodes();
/* 361 */     for (int i = 0; i < nl.getLength(); ++i) {
/* 362 */       Node child = nl.item(i);
/* 363 */       if (child.getNodeType() == 3) {
/* 364 */         String childValue = child.getNodeValue().trim();
/* 365 */         if (childValue.length() == 0) {
/*     */           continue;
/*     */         }
/*     */ 
/* 369 */         if (childValue.equals(value)) {
/* 370 */           return;
/*     */         }
/* 372 */         child.setNodeValue(value);
/* 373 */         this.isDirty = true;
/* 374 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 380 */     if (nl.getLength() == 0) {
/* 381 */       node.appendChild(this.doc.createTextNode(value));
/*     */     } else {
/* 383 */       Node f = node.getFirstChild();
/* 384 */       if (f.getNodeType() == 3)
/* 385 */         f.setNodeValue(value);
/*     */       else {
/* 387 */         node.insertBefore(this.doc.createTextNode(value), f);
/*     */       }
/*     */     }
/* 390 */     this.isDirty = true;
/*     */   }
/*     */ 
/*     */   public boolean getBoolean(String key, boolean def)
/*     */   {
/* 399 */     String str = String.valueOf(def);
/*     */ 
/* 401 */     String resstr = get(key, str);
/* 402 */     Boolean resboolean = Boolean.valueOf(resstr);
/* 403 */     boolean result = resboolean.booleanValue();
/* 404 */     return result;
/*     */   }
/*     */ 
/*     */   public int getInt(String key, int def) {
/* 413 */     String str = String.valueOf(def);
/* 414 */     String resstr = get(key, str);
/*     */     int result;
/*     */     try {
/* 416 */       result = Integer.parseInt(resstr);
/*     */     } catch (NumberFormatException e) {
/* 418 */       return def;
/*     */     }
/* 420 */     return result;
/*     */   }
/*     */ 
/*     */   public float getFloat(String key, float def) {
/* 429 */     String str = String.valueOf(def);
/* 430 */     String resstr = get(key, str);
/*     */     float result;
/*     */     try {
/* 432 */       result = Float.parseFloat(resstr);
/*     */     } catch (NumberFormatException e) {
/* 434 */       return def;
/*     */     }
/* 436 */     return result;
/*     */   }
/*     */ 
/*     */   public double getDouble(String key, double def) {
/* 445 */     String str = String.valueOf(def);
/* 446 */     String resstr = get(key, str);
/*     */     double result;
/*     */     try {
/* 448 */       result = Double.parseDouble(resstr);
/*     */     } catch (NumberFormatException e) {
/* 450 */       return def;
/*     */     }
/* 452 */     return result;
/*     */   }
/*     */ 
/*     */   public long getLong(String key, long def) {
/* 461 */     String str = String.valueOf(def);
/* 462 */     String resstr = get(key, str);
/*     */     long result;
/*     */     try {
/* 464 */       result = Long.parseLong(resstr);
/*     */     } catch (NumberFormatException e) {
/* 466 */       return def;
/*     */     }
/* 468 */     return result;
/*     */   }
/*     */ 
/*     */   public byte[] getByteArray(String key, byte[] def)
/*     */   {
/* 477 */     String str = new String(def);
/* 478 */     String resstr = get(key, str);
/* 479 */     byte[] result = resstr.getBytes();
/* 480 */     return result;
/*     */   }
/*     */ 
/*     */   public void putBoolean(String key, boolean value)
/*     */   {
/* 489 */     String str = String.valueOf(value);
/*     */     try {
/* 491 */       put(key, str);
/*     */     } catch (RuntimeException e) {
/* 493 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void putInt(String key, int value)
/*     */   {
/* 502 */     String str = String.valueOf(value);
/*     */     try {
/* 504 */       put(key, str);
/*     */     } catch (RuntimeException e) {
/* 506 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void putFloat(String key, float value)
/*     */   {
/* 515 */     String str = String.valueOf(value);
/*     */     try {
/* 517 */       put(key, str);
/*     */     } catch (RuntimeException e) {
/* 519 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void putDouble(String key, double value)
/*     */   {
/* 528 */     String str = String.valueOf(value);
/*     */     try {
/* 530 */       put(key, str);
/*     */     } catch (RuntimeException e) {
/* 532 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void putLong(String key, long value)
/*     */   {
/* 541 */     String str = String.valueOf(value);
/*     */     try {
/* 543 */       put(key, str);
/*     */     } catch (RuntimeException e) {
/* 545 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void putByteArray(String key, byte[] value)
/*     */   {
/* 554 */     put(key, Base64.encode(value));
/*     */   }
/*     */ 
/*     */   public void removeNode(String key)
/*     */   {
/* 562 */     Node node = findNode(key);
/* 563 */     if (node == null) {
/* 564 */       return;
/*     */     }
/* 566 */     Node parentnode = node.getParentNode();
/* 567 */     if (parentnode != null) {
/* 568 */       parentnode.removeChild(node);
/* 569 */       this.isDirty = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear(String key)
/*     */   {
/* 578 */     Node node = findNode(key);
/* 579 */     if (node == null)
/* 580 */       throw new RuntimeException("InvalidName");
/* 581 */     Node lastnode = null;
/*     */ 
/* 583 */     while (node.hasChildNodes()) {
/* 584 */       lastnode = node.getLastChild();
/* 585 */       node.removeChild(lastnode);
/*     */     }
/*     */ 
/* 588 */     if (lastnode != null)
/* 589 */       this.isDirty = true;
/*     */   }
/*     */ 
/*     */   public String[] childrenNames(String key)
/*     */   {
/* 599 */     Node node = findNode(key);
/* 600 */     if (node == null) {
/* 601 */       return new String[0];
/*     */     }
/* 603 */     NodeList nl = node.getChildNodes();
/* 604 */     LinkedList list = new LinkedList();
/* 605 */     for (int i = 0; i < nl.getLength(); ++i) {
/* 606 */       Node child = nl.item(i);
/* 607 */       if ((child.getNodeType() == 1) && (child.hasChildNodes())) {
/* 608 */         list.add(child.getNodeName());
/*     */       }
/*     */     }
/* 611 */     String[] ret = new String[list.size()];
/* 612 */     for (int i = 0; i < ret.length; ++i) {
/* 613 */       ret[i] = ((String)list.get(i));
/*     */     }
/* 615 */     return ret;
/*     */   }
/*     */ 
/*     */   public boolean nodeExist(String key)
/*     */   {
/* 625 */     Node theNode = findNode(key);
/* 626 */     if (theNode == null) {
/* 627 */       return false;
/*     */     }
/*     */ 
/* 630 */     return (theNode.hasChildNodes());
/*     */   }
/*     */ 
/*     */   private void loadXMLParser()
/*     */     throws IOException
/*     */   {
/* 642 */     if (builder != null) return;
/*     */     try {
/* 644 */       factory = DocumentBuilderFactory.newInstance();
/* 645 */       factory.setIgnoringComments(true);
/* 646 */       builder = factory.newDocumentBuilder();
/*     */     } catch (ParserConfigurationException ex) {
/* 648 */       throw new IOException("XML Parser load error:".concat(String.valueOf(String.valueOf(ex.getLocalizedMessage()))));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void load()
/*     */     throws IOException
/*     */   {
/* 659 */     loadXMLParser();
/*     */     try
/*     */     {
/* 663 */       InputSource is = new InputSource(new InputStreamReader(new URL(this.file).openStream()));
/*     */ 
/* 665 */       is.setEncoding(System.getProperty("file.encoding"));
/* 666 */       this.doc = builder.parse(is);
/*     */     } catch (SAXException ex) {
/* 668 */       ex.printStackTrace();
/* 669 */       String message = ex.getMessage();
/* 670 */       Exception e = ex.getException();
/* 671 */       if (e != null) {
/* 672 */         message = String.valueOf(String.valueOf(message)).concat(String.valueOf(String.valueOf("embedded exception:".concat(String.valueOf(String.valueOf(e))))));
/*     */       }
/* 674 */       throw new IOException("XML file parse error:".concat(String.valueOf(String.valueOf(message))));
/*     */     }
/* 676 */     this.root = this.doc.getDocumentElement();
/* 677 */     if (!("config".equals(this.root.getNodeName())))
/* 678 */       throw new IOException("Config file format error, root node must be <config>");
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 687 */     if (this.isDirty) {
/* 688 */       String proc = new URL(this.file).getProtocol().toLowerCase();
/* 689 */       if (!(proc.equalsIgnoreCase("file"))) {
/* 690 */         throw new UnsupportedOperationException("Unsupport write config URL on protocal ".concat(String.valueOf(String.valueOf(proc))));
/*     */       }
/* 692 */       String fileName = new URL(this.file).getPath();
/* 693 */       Debug.dump(new URL(this.file).getPath());
/* 694 */       Debug.dump(new URL(this.file).getFile());
/* 695 */       BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName), 2048);
/*     */ 
/* 697 */       PrintWriter pw = new PrintWriter(bos);
/* 698 */       writeNode(this.doc, pw, 0);
/* 699 */       pw.flush();
/* 700 */       pw.close();
/* 701 */       this.isDirty = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private String change(String str)
/*     */     throws IOException
/*     */   {
/* 711 */     if ((str.indexOf(38) != -1) || (str.indexOf(60) != -1) || (str.indexOf(62) != -1)) {
/* 712 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 713 */       ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
/*     */ 
/* 715 */       byte[] ba1 = { 38, 97, 109, 112, 59 };
/* 716 */       byte[] ba2 = { 38, 108, 116, 59 };
/* 717 */       byte[] ba3 = { 38, 103, 116, 59 };
/* 718 */       while ((temp = (byte)bis.read()) != -1)
/*     */       {
/*     */         byte temp;
/* 719 */         switch (temp)
/*     */         {
/*     */         case 38:
/* 721 */           bos.write(ba1);
/* 722 */           break;
/*     */         case 60:
/* 724 */           bos.write(ba2);
/* 725 */           break;
/*     */         case 62:
/* 727 */           bos.write(ba3);
/* 728 */           break;
/*     */         }
/* 730 */         bos.write(temp);
/*     */       }
/*     */ 
/* 733 */       return bos.toString();
/*     */     }
/* 735 */     return str;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {
/* 742 */     Cfg c = new Cfg("testcfg.xml", true);
/* 743 */     c.put("a/b", "汉字");
/* 744 */     c.put("c", "");
/* 745 */     c.put("a", "avalusaaaaaaaaae");
/* 746 */     c.flush();
/* 747 */     c = new Cfg("testcfg.xml", true);
/* 748 */     System.out.println("Config file content:");
/* 749 */     BufferedReader in = new BufferedReader(new FileReader("testcfg.xml"));
/*     */ 
/* 751 */     while ((line = in.readLine()) != null)
/*     */     {
/*     */       String line;
/* 752 */       System.out.println(line);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.util.Cfg
 * JD-Core Version:    0.5.3
 */