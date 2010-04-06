package com.nci.svg.server.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * <p>
 * ���⣺XmlUtil.java
 * </p>
 * <p>
 * ������ xml��ش���̬����
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-6-15
 * @version 1.0
 */
public class XmlUtil {

	/**
	 * ȫ�ֱ���
	 */
	static DocumentBuilder documentBuild = null;
	/**
	 * ��ʼ��ȫ�ֱ���
	 */
	static {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			documentBuild = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 2009-6-15 Add by ZHM
	 * 
	 * @���� ����document����
	 * @return
	 */
	public synchronized static Document createDoc() {
		return documentBuild.newDocument();
	}
	
	/**
	 * 2009-7-13
	 * Add by ZHM
	 * @���� ���ַ���ת���Document����
	 * @param documentString:String:XML��ʽ�ַ���
	 * @return
	 */
	public static Document getXMLDocumentByString(String documentString) {
		try {
			// svg�ı��������UTF-8
			return getXMLDocumentByStream(new ByteArrayInputStream(
					documentString.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 2009-7-13
	 * Add by ZHM
	 * @���� ��������������Document����
	 * @param is:InputStream:������
	 * @return
	 */
	public static Document getXMLDocumentByStream(InputStream is) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder bulider = null;
		try {
			bulider = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = bulider.parse(is);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		doc.normalize();
		return doc;
	}

	/**
	 * 2009-6-15 Add by ZHM
	 * 
	 * @���� ��document����ת����ַ���
	 * @param doc
	 * @return
	 */
	public static String nodeToString(Node doc) {
		try {
			DOMSource source = new DOMSource(doc);
			StringWriter writer = new StringWriter();

			Result result = new StreamResult(writer);
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.transform(source, result);
			return writer.getBuffer().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @���� ��Node����ת����ַ���
	 * @param node:Node:Ҫת���Ķ���
	 * @param isPrint:boolean:�Ƿ�ת�õ��ַ�����ӡ������̨
	 * @return String:ת��õ��ַ���
	 */
	public static String printNode(Node node, boolean isPrint) {
		if (node instanceof Document)
			node = ((Document) node).getDocumentElement();
		StringBuffer sb = new StringBuffer();
		writeNode(node, sb, 0, true);
		if (isPrint)
			System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * 2009-7-13
	 * Add by ZHM
	 * @���� ��Node����ת����ַ���
	 * @param node
	 * @param buffer
	 * @param indent
	 * @param format
	 */
	public static void writeNode(Node node, StringBuffer buffer, int indent,
			boolean format) {

		if (node != null) {
			switch (node.getNodeType()) {
			case Node.ELEMENT_NODE:
				buffer.append("<");
				buffer.append(node.getNodeName());
				if (node.hasAttributes()) {
					NamedNodeMap attr = node.getAttributes();
					int len = attr.getLength();
					for (int i = 0; i < len; i++) {
						Attr a = (Attr) attr.item(i);
						buffer.append(" ");
						buffer.append(a.getNodeName());
						buffer.append("=\"");
						buffer.append(contentToString(a.getNodeValue()));
						buffer.append("\"");
					}
				}
				Node c = node.getFirstChild();
				if (c != null) {
					buffer.append(">");
					if (format) {
						buffer.append("\n");
					}
					for (; c != null; c = c.getNextSibling()) {
						writeNode(c, buffer, indent + 1, format);
					}
					buffer.append("</");
					buffer.append(node.getNodeName());
					buffer.append(">");
				} else {
					buffer.append("/>");
				}
				if (format) {
					buffer.append("\n");
				}
				break;
			case Node.TEXT_NODE:
				buffer.append(contentToString(node.getNodeValue()));
				break;
			case Node.CDATA_SECTION_NODE:
				buffer.append("<![CDATA[");
				buffer.append(node.getNodeValue());
				buffer.append("]]>");
				break;
			case Node.ENTITY_REFERENCE_NODE:
				buffer.append("&");
				buffer.append(node.getNodeName());
				buffer.append(";");
				break;
			case Node.PROCESSING_INSTRUCTION_NODE:
				buffer.append("<?");
				buffer.append(node.getNodeName());
				buffer.append(" ");
				buffer.append(node.getNodeValue());
				buffer.append("?>");
				break;
			case Node.COMMENT_NODE:
				buffer.append("<!--");
				buffer.append(node.getNodeValue());
				buffer.append("-->");
				break;
			case Node.DOCUMENT_TYPE_NODE:
				DocumentType dt = (DocumentType) node;
				buffer.append("<!DOCTYPE ");
				buffer.append(node.getOwnerDocument().getDocumentElement()
						.getNodeName());
				String pubID = dt.getPublicId();
				if (pubID != null) {
					buffer.append(" PUBLIC \"" + dt.getNodeName() + "\" \""
							+ pubID + "\">");
				} else {
					String sysID = dt.getSystemId();
					if (sysID != null) {
						buffer.append(" SYSTEM \"" + sysID + "\">");
					}
				}
				break;
			}
		}
	}

	public static String contentToString(String s) {
		StringBuffer result = new StringBuffer();
		s = s.replaceAll("\\n+", "");
		s = s.replaceAll("\\r+", "");
		s = s.replaceAll("\\t+", "");

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '"':
				result.append("&quot;");
				break;
			case '\'':
				result.append("&apos;");
				break;
			default:
				result.append(c);
			}
		}
		return result.toString();
	}
	
	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @���� ����ָ��Ԫ�ص�����ֵ
	 * @param e
	 * @param attrs
	 */
	public static void setElementAttributes(Element e, String[][] attrs) {
		for (int i = 0, size = attrs.length; i < size; i++) {
			if (attrs[i].length == 2) {
				e.setAttribute(attrs[i][0], attrs[i][1]);
			}
		}
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @���� ����ָ��Ԫ�ش������ռ������ֵ
	 * @param e
	 * @param attrs
	 */
	public static void setElementAttributesNS(Element e, String[][] attrs) {
		for (int i = 0, size = attrs.length; i < size; i++) {
			if (attrs[i].length == 3) {
				e.setAttributeNS(attrs[i][0], attrs[i][1], attrs[i][2]);
			}
		}
	}

}
