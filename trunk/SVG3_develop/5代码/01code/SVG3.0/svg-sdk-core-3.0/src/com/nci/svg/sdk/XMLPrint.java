package com.nci.svg.sdk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.zip.GZIPOutputStream;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-11-24
 * @功能：XML文件打印类
 *
 */
public class XMLPrint {

	/**
	 * 将指定的文档对象打印至指定的文件中
	 * @param doc 文档对象
	 * @param file 文件对象
	 */
	public static void printXML(Document doc, File file) {

		// whether the file extension is "svg" or "svgz"
		boolean isSVGFile = file.getName().endsWith("svg");

		// converts the svg document into xml strings
		StringBuffer buffer = new StringBuffer("");

		for (Node node = doc.getFirstChild(); node != null; node = node
				.getNextSibling()) {

			writeNode(node, buffer, 0, isSVGFile);
		}

		try {

			ByteBuffer byteBuffer = ByteBuffer.wrap(buffer.toString().getBytes(
					"UTF-8"));
			FileOutputStream out = new FileOutputStream(file);

			if (!isSVGFile) {

				// compressing the svg content
				GZIPOutputStream zout = new GZIPOutputStream(out);
				WritableByteChannel channel = Channels.newChannel(zout);

				channel.write(byteBuffer);
				zout.flush();
				zout.close();

			} else {

				// writing the svg content without compression
				WritableByteChannel channel = Channels.newChannel(out);
				channel.write(byteBuffer);
				channel.close();
			}

			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 将字符串作为文件的内容写入指定的文件（覆盖掉原文件内容），适合于xml、svg等文本文件。
	 * 
	 * @param content
	 *            字符串信息
	 * @param file
	 *            待写入的文件
	 */
	public synchronized static void printStringToFile(String content, File file) {
		try {
			if (file.exists())
				file.delete();
			while (!file.exists()) {
				File parentDir = file.getParentFile();
				if (!parentDir.exists())
					parentDir.mkdirs();
				FileOutputStream fos = new FileOutputStream(file);
				ByteBuffer byteBuffer = ByteBuffer.wrap(content
						.getBytes("UTF-8"));
				WritableByteChannel channel = Channels.newChannel(fos);
				channel.write(byteBuffer);
				channel.close();
				fos.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将文档节点转换成String格式
	 * @param node 文档节点
	 * @param isPrint 是否在控制台打印，true表示打印
	 * @return：节点的String型
	 */
	public static String printNode(Node node, boolean isPrint) {
		StringBuffer sb = new StringBuffer();
		if (node instanceof Document)
			node = ((Document) node).getDocumentElement();
		writeNode(node, sb, 0, true);
		if (isPrint)
			System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * 将文档节点node写入字符串缓存buffer中
	 * @param node 文档节点
	 * @param buffer 字符串缓存
	 * @param indent 层次
	 * @param format 换行格式控制
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

	/**
	 * 将内容转换成String型
	 * @param s 
	 * @return
	 */
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

}
