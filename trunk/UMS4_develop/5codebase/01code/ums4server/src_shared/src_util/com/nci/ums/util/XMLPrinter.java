package com.nci.ums.util;

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
import org.w3c.dom.Text;

/**
 * the class providing methods used to convert documents to XML content and to
 * save it into files
 * 
 * @author Jordi SUC
 */
public class XMLPrinter {

	/**
	 * prints the given document into the given file
	 * 
	 * @param doc
	 *            the document to save
	 * @param file
	 *            the file into which the document will be saved
	 * @param monitor
	 *            the monitor of the progress
	 */
	public static void printXML(Document doc, File file) {

		// whether the file extension is "svg" or "svgz"
		boolean isSVGFile = file.getName().endsWith(".svg")
				|| file.getName().endsWith(".xml");

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
				// Writer out = new OutputStreamWriter(fos, "UTF-8");
				// out.write(content);
				// out.close();
				fos.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * writes the string representation of the given node in the given string
	 * buffer
	 * 
	 * @param node
	 *            a node
	 * @param buffer
	 *            the string buffer
	 * @param indent
	 *            the indent
	 * @param format
	 *            whether the xml should be formatted
	 * @param monitor
	 *            the monitor for this action
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
						if (c instanceof Text == false)
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
	 * @param s
	 *            the string to be modified
	 * @return the given content value transformed to replace invalid characters
	 *         with entities.
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

	/**
	 * 打印xml节点字符串
	 * 
	 * @param node
	 */
	public static String printNode(Node node, boolean isPrint) {
		if (node instanceof Document)
			node = ((Document) node).getDocumentElement();
		StringBuffer sb = new StringBuffer();
		XMLPrinter.writeNode(node, sb, 0, true);
		if (isPrint)
			System.err.println(sb.toString());
		return sb.toString();
	}
}
