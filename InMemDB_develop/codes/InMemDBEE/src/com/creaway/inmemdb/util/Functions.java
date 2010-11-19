/*
 * @(#)Functions.java	1.0  08/27/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 常用功能工具类
 * 
 * @author Qil.Wong
 * 
 */
public class Functions {

	/**
	 * 从一个url得到Document对象
	 * 
	 * @param url
	 *            一个url路径
	 * @return Document对象
	 */
	public static Document getXMLDocument(URL url) {
		return getXMLDocument(url.toString());
	}

	public static Document getXMLDocument(String url) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder bulider = factory.newDocumentBuilder();
			Document doc = bulider.parse(url);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将XML字符串转化成Document对象
	 * 
	 * @param xml
	 * @return
	 */
	public static Document parseXML(String xml) {
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringReader sr = new StringReader(xml);
			InputSource iSrc = new InputSource(sr);
			doc = builder.parse(iSrc);
			doc.normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 将Document/Element/Node对象转化成字符串
	 * 
	 * @param doc
	 * @return
	 */
	public static String printXML(Node doc) {
		try {
			Source source = new DOMSource(doc);
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(baos);
			Result result = new StreamResult(out);
			xformer.transform(source, result);
			out.close();
			return new String(baos.toByteArray(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据xpath查找单个Element元素，如果有多个条件符合，返回第一个
	 * 
	 * @param xpathExpr
	 * @param sourceNode
	 * @return
	 */
	public static synchronized Element findElement(String xpathExpr,
			Node sourceNode) {
		try {
			NodeList result = (NodeList) XPathFactory.newInstance().newXPath()
					.evaluate(xpathExpr, sourceNode, XPathConstants.NODESET);
			return (Element) result.item(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据xpath查找所有符合条件的Element元素集合
	 * 
	 * @param xpathExpr
	 * @param sourceNode
	 * @return
	 */
	public static synchronized NodeList findNodes(String xpathExpr,
			Node sourceNode) {
		try {
			Object result = XPathFactory.newInstance().newXPath()
					.evaluate(xpathExpr, sourceNode, XPathConstants.NODESET);
			return (NodeList) result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取MD5加密字符串，大写，32位
	 * 
	 * @param bytes
	 * @return
	 */
	public static synchronized String md5(String str) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException(
					"String to encript cannot be null or zero length");
		}
		try {
			MessageDigest digester = MessageDigest.getInstance("MD5");
			digester.update(str.getBytes());
			byte[] hash = digester.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0"
							+ Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
			return hexString.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 通过流的方式来完整克隆一个对象
	 * 
	 * @param oldObj
	 * @return
	 */
	public static synchronized Object deepClone(Object oldObj) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(oldObj);
			out.flush();
			out.close();
			ByteArrayInputStream bis = new ByteArrayInputStream(
					bos.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bis);
			return in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字节数组转成带符号整数,摘自BigInt
	 * 
	 * @param b
	 * @return
	 */
	public static int bytes2Int(byte[] b) {
		if (b.length > 4)
			throw new NumberFormatException("BigInt.toLong, too big");
		int i = 0;
		for (int j = 0; j < b.length; j++)
			i = (i << 8) + (b[j] & 255);
		return i;
	}

	/**
	 * 将int型转成字节，摘自BigInt
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] int2Bytes(int i) {
		byte[] places = null;
		if (i < 256) {
			places = new byte[1];
			places[0] = (byte) i;
		} else if (i < 65536) {
			places = new byte[2];
			places[0] = (byte) (i >> 8);
			places[1] = (byte) i;
		} else if (i < 16777216) {
			places = new byte[3];
			places[0] = (byte) (i >> 16);
			places[1] = (byte) (i >> 8);
			places[2] = (byte) i;
		} else {
			places = new byte[4];
			places[0] = (byte) (i >> 24);
			places[1] = (byte) (i >> 16);
			places[2] = (byte) (i >> 8);
			places[3] = (byte) i;
		}
		return places;
	}

	/**
	 * @功能 二进制字符串转换成十六进制值字符串
	 * @param bin
	 *            String 二进制字符串
	 * @return String 十六进制字符串
	 */
	public static String bin2hex(String bin) {
		char[] digital = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer("");
		byte[] bs = bin.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(digital[bit]);
			bit = bs[i] & 0x0f;
			sb.append(digital[bit]);
		}
		return sb.toString();
	}

	/**
	 * @功能 十六进制字符串转换成二进制字符串
	 * @param hex
	 *            String 十六进制字符串
	 * @return String 二进制字符串
	 */
	public static String hex2bin(String hex) {
		String digital = "0123456789ABCDEF";
		char[] hex2char = hex.toCharArray();
		byte[] bytes = new byte[hex.length() / 2];
		int temp;
		for (int i = 0; i < bytes.length; i++) {
			temp = digital.indexOf(hex2char[2 * i]) * 16;
			temp += digital.indexOf(hex2char[2 * i + 1]);
			bytes[i] = (byte) (temp & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * @功能 将字节流转换成16进制字符串
	 * @param bytes
	 *            byte[] 字节数组
	 * @return String 十六进制字符串
	 */
	public static String byte2hex(byte[] bytes) {
		StringBuffer hs = new StringBuffer();
		String tmp = "";
		for (int n = 0; n < bytes.length; n++) {
			// 整数转成十六进制表示
			tmp = (java.lang.Integer.toHexString(bytes[n] & 0XFF));
			if (tmp.length() == 1) {
				hs.append("0").append(tmp);
			} else {
				hs.append(tmp);
			}
		}
		return hs.toString().toUpperCase(); // 转成大写
	}

	/**
	 * @功能 将十六进制字节数组转换成正常字节数组
	 * @param hexBytes
	 *            byte[] 十六进制数组
	 * @return 正常字节数组
	 */
	public static byte[] hex2byte(byte[] hexBytes) {
		if ((hexBytes.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[hexBytes.length / 2];
		for (int n = 0; n < hexBytes.length; n += 2) {
			String item = new String(hexBytes, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		hexBytes = null;
		return b2;
	}

	public static void main(String... strings) {
		System.out.println(md5("minerchen1981"));
	}
}
