package com.nci.domino.help;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.utils.XmlUtils;

/**
 * 这里定义了一系列的通用方法
 * 
 * @author Qil.Wong
 */
public class Functions {
	/**
	 * 把一个字符串保存成一个文件
	 * 
	 * @param content
	 *            要保存的字符串
	 * @param filename
	 *            文件名
	 * @param encoding
	 *            编码类型 如果为null或者空字符串则取默认
	 * @return 0 表示成功 -1 表示失败
	 */
	public static int saveStringToFile(String content, String filename,
			String encoding) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			byte[] bstr = (encoding == null || encoding.equals("")) ? content
					.getBytes() : content.getBytes(encoding);
			fos.write(bstr);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	/**
	 * 在二维数组中查找某个值
	 * 
	 * @param strArr
	 *            输入二维数组
	 * @param value
	 *            待查找的值
	 * @param index
	 *            索引值
	 * @param ret
	 *            返回索引
	 * @param defaultRet
	 *            缺省返回值
	 * @return 查到的字符串
	 */
	public static String getValueByIndex(String[][] strArr, String value,
			int index, int ret, String defaultRet) {
		for (int i = 0; i < strArr.length; i++)
			if (strArr[i][index].equals(value))
				return strArr[i][ret];
		return defaultRet;
	}

	/**
	 * 从一个url得到Document对象
	 * 
	 * @param url
	 *            一个url路径
	 * @return Document对象
	 */
	public static Document getXMLDocument(URL url) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder bulider = factory.newDocumentBuilder();
			Document doc = bulider.parse(url.toString());
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 得到时间的标准字符串形式 形式是 yyyy-mm-dd hh:mm:ss 注意没有毫秒
	 * 
	 * @param d
	 *            Calendar实例
	 * @return 时间的标准字符串形式
	 */
	public static String getStrFromDateTime(Calendar d) {
		return d.get(Calendar.YEAR) + "-" + (d.get(Calendar.MONTH) + 1) + "-"
				+ d.get(Calendar.DATE) + " " + d.get(Calendar.HOUR_OF_DAY)
				+ ":" + d.get(Calendar.MINUTE) + ":" + d.get(Calendar.SECOND);
	}

	/**
	 * 得到包容一个Component的Frame
	 * 
	 * @param compOnApplet
	 *            一个Component
	 * @return 包容一个Component的Frame
	 */
	public static Frame getParentWindow(Component compOnApplet) {
		Container c = compOnApplet.getParent();
		while (c != null) {
			if (c instanceof Frame)
				return (Frame) c;
			c = c.getParent();
		}
		return null;
	}

	/**
	 * 从返回信息中解析出成功失败以及详细内容来
	 * 
	 * @param str
	 *            返回信息字符串
	 * @return 解析出的信息
	 */
	public static String[] getSuccessInfo(String str) {
		/*
		 * 
		 */
		String[] ret = null;
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			InputSource is = new InputSource(new ByteArrayInputStream(str
					.getBytes("UTF-8")));
			Node s = (Node) xpath.evaluate("/root/result", is,
					XPathConstants.NODE);
			Node ErrorCode = (Node) xpath.evaluate("ErrorCode", s,
					XPathConstants.NODE);
			Node ErrorMsg = (Node) xpath.evaluate("ErrorMsg", s,
					XPathConstants.NODE);
			Node Param = (Node) xpath.evaluate("Param", s, XPathConstants.NODE);
			ret = new String[] { ErrorCode.getTextContent(),
					ErrorMsg.getTextContent(), Param.getTextContent() };
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	/**
	 * 把一个字符串转化为转义形式 采用utf8编码
	 * 
	 * @param str
	 *            输入字符串
	 * @return 经过转义的字符串
	 */
	public static String toUtf8Meaning(String str) {
		StringBuffer buf = new StringBuffer();
		int nc, nL = 0;
		for (int k = 0; k < str.length(); k++) {
			nc = str.charAt(k);
			if (nc > 256) {
				buf.append(str.substring(nL, k)).append("&#").append(nc)
						.append(";");
				nL = k + 1;
				continue;
			}
			if (k == str.length() - 1) {
				buf.append(str.substring(nL));
				break;
			}
		}
		return buf.toString();
	}

	/**
	 * 得到唯一的id
	 * 
	 * @return 唯一的id
	 */
	public synchronized static long genrateOnlyId() {
		return System.currentTimeMillis();
	}

	private static long orginidTime = System.nanoTime();

	/**
	 * 生成UUID的WofoID对象
	 * 
	 * @return
	 */
	public synchronized static String getUID() {
		String id = new UUID(orginidTime, ++orginidTime).toString()
				.toUpperCase();
		return id;
	}

	/**
	 * 生成UUID的String对象
	 * 
	 * @return
	 */
	public synchronized static String getUIDString() {
		return new UUID(orginidTime, ++orginidTime).toString().toUpperCase();
	}

	/**
	 * 判断字符串是否为空 包括为空字符串或者null
	 * 
	 * @param s
	 *            字符串
	 * @return 空返回true
	 */
	public static boolean stringIsEmpty(String s) {
		return s == null || Trim(s).equals("");
	}

	/**
	 * 判断是不是非负整数
	 * 
	 * @param s
	 *            字符串
	 * @return 是非负整数返回true
	 */
	public static boolean IsPInt(String s) {
		if (stringIsEmpty(s))
			return false;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) < '0' || s.charAt(i) > '9')
				return false;
		return true;
	}

	/**
	 * 重新定义了一个修剪字符串的方法 把全角空格换成半角空格再转
	 * 
	 * @param s
	 *            待修剪得字符串
	 * @return 修剪过的字符串
	 */
	public static String Trim(String s) {
		return s.replaceAll("　", " ").trim();
	}

	/**
	 * 把字符串中的几个特殊字符转成xml转义格式 包括& < > ' " 这五个字符
	 * 
	 * @param str
	 *            输入字符串
	 * @return 转义后的字符串
	 */
	public static String encodexml(String str) {
		StringBuffer buf = new StringBuffer();
		if (str == null)
			return "";
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			case '&':
				buf.append("&amp;");
				break;
			case '<':
				buf.append("&lt;");
				break;
			case '>':
				buf.append("&gt;");
				break;
			case '\'':
				buf.append("&apos;");
				break;
			case '\"':
				buf.append("&quot;");
				break;
			default:
				buf.append(c);
				break;
			}
		}
		return buf.toString();
	}

	/**
	 * 得到URL资源返回的内容字符串
	 * 
	 * @param url
	 *            URL资源的路径 如 "http://192.168.0.201/java/servlet"
	 * @param params
	 *            二维字符串数组，提供一系列，名/值对 String params[][]={ {"request","query"},
	 *            {"format","xml"},
	 *            {"query","select * from substation where name like '%硖北%'"} };
	 * @return 返回的内容字符串
	 */
	public static String getReturnText(String url, String[][] params) {
		try {
			String reqstring = "";
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					if (i != 0)
						reqstring += "&";
					reqstring += URLEncoder.encode(params[i][0], "UTF-8") + '='
							+ URLEncoder.encode(params[i][1], "UTF-8");
				}
			}
			StringBuffer bf = new StringBuffer();
			URL ur = new URL(url);
			URLConnection reverseConn = ur.openConnection();
			reverseConn.setDoOutput(true);
			reverseConn.setDoInput(true);
			PrintStream output = new PrintStream(reverseConn.getOutputStream());
			output.print(reqstring);
			output.flush();
			DataInputStream input = new DataInputStream(reverseConn
					.getInputStream());
			int b;
			while ((b = input.read()) != -1) {
				bf.append((char) b);
			}
			return new String(bf.toString().getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			System.out.print(e.toString());
			return e.toString();
		}
	}

	public static WofoNetBean getReturnNetBean(String url, Serializable param) {
		return (WofoNetBean) getReturnObj(url, param);
	}

	@SuppressWarnings("unchecked")
	public static Object getReturnObj(final String url, final Serializable param) {

		return AccessController.doPrivileged(new PrivilegedAction() {

			public Object run() {
				try {
					URL ur = new URL(url);
					URLConnection reverseConn = ur.openConnection();
					reverseConn.setDoOutput(true);
					reverseConn.setDoInput(true);
					reverseConn.setRequestProperty("Content-type",
							"application/octest-stream");
					ObjectOutputStream output = new ObjectOutputStream(
							reverseConn.getOutputStream());
					output.writeObject(param);
					output.flush();
					ObjectInputStream input = new ObjectInputStream(reverseConn
							.getInputStream());
					Object o = input.readObject();
					return o;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

		});

	}

	public static Element createElement(Document doc, String tagName,
			String[][] attrNV) {
		Element one = doc.createElement(tagName);
		if (attrNV != null && attrNV.length > 0) {
			for (int i = 0; i < attrNV.length; i++)
				one.setAttribute(attrNV[i][0], attrNV[i][1]);
		}
		return one;
	}

	public static String removePrefix(String str, String sufix) {
		int nLc = str.indexOf(sufix);
		if (nLc != -1)
			return str.substring(0, nLc) + str.substring(nLc + sufix.length());
		return str;
	}

	/**
	 * 存储的图标
	 */
	private static Map<String, ImageIcon> icons = new HashMap<String, ImageIcon>();

	public static ImageIcon getImageIcon(String imgfName) {
		ImageIcon icon = icons.get(imgfName);
		if (icon == null) {
			URL url = Functions.class.getResource("/resources/images/"
					+ imgfName);
			if (url != null) {
				icon = icons.get(imgfName);
				if (icon == null) {
					icon = new ImageIcon(url);
					icons.put(imgfName, icon);
				}
			}
		}
		return icon;
	}

	/**
	 * 根据操作模式获取鼠标
	 * 
	 * @param toolMode
	 * @return
	 */
	public static Cursor getCursor(int toolMode) {
		Cursor cursor = null;
		switch (toolMode) {
		case ToolMode.TOOL_DRAW_ACTIVITY:
			cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
			break;
		case ToolMode.TOOL_LINKLINE:
			// cursor =
			// createCursorFromImage(getImageIcon("cursor_transition.gif")
			// .getImage());
			break;
		case ToolMode.TOOL_SELECT_OR_DRAG:
			break;
		case ToolMode.TOOL_TRANSLATE:
			cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
			break;
		case ToolMode.Tool_DRAW_PIPE:
			cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
			break;
		case ToolMode.Tool_DRAW_NOTES:
			cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
			break;
		default:
			cursor = Cursor.getDefaultCursor();
			break;
		}
		return cursor;
	}

	public static Cursor createCursorFromImage(Image image) {
		Cursor cursor = null;
		if (image != null) {
			try {
				cursor = java.awt.Toolkit.getDefaultToolkit()
						.createCustomCursor(image, new Point(16, 16),
								"resource");
			} catch (Exception ex) {
			}
		}
		return cursor;
	}

	/**
	 * 将字符串转换成拐点列表,字符串格式是“x1,x1|x2,y2
	 * 
	 * @return
	 */
	public static List<Point2D> parseCornerPoints(String archors) {
		List<Point2D> cornerPoints = new ArrayList<Point2D>();
		if (archors != null && !archors.trim().equals("")) {
			String[] points = archors.split("\\|");
			for (String s : points) {
				String[] x = s.split(",");
				Point2D p = new Point2D.Double(Double.valueOf(x[0]), Double
						.valueOf(x[1]));
				cornerPoints.add(p);
			}
		}
		return cornerPoints;
	}

	public static void stopEditingCells(javax.swing.JTable table) {
		if (table.getCellEditor() != null) {
			int selectIndex = table.getSelectedRow();
			if (selectIndex != -1) {
				table.getCellEditor().stopCellEditing();
			}
		}
	}

	/**
	 * 根据xpath查找单个Element元素，如果有多个条件符合，返回第一个
	 * 
	 * @param xpathExpr
	 * @param sourceNode
	 * @return
	 */
	public static synchronized Element findNode(String xpathExpr,
			Node sourceNode) {
		try {
			Object result = XPathFactory.newInstance().newXPath().evaluate(
					xpathExpr, sourceNode, XPathConstants.NODESET);
			for (int i = 0; i < ((NodeList) result).getLength(); i++) {
				Node child = ((NodeList) result).item(i);
				// 由于是唯一的属性，查询到值就返回
				return (Element) child;
			}
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
			Object result = XPathFactory.newInstance().newXPath().evaluate(
					xpathExpr, sourceNode, XPathConstants.NODESET);
			return (NodeList) result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
			ByteArrayInputStream bis = new ByteArrayInputStream(bos
					.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bis);
			return in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对序列化后的xml进行新版本分配，这在复制粘贴、新版本生成时候适用
	 * 
	 * @param xmlEncodedXml
	 * @return
	 */
	public static synchronized String applyNewVersion(String xmlEncodedXml) {
		Document doc = XmlUtils.fromXML(xmlEncodedXml);
		if (doc == null)
			return null;
		NodeList idNodes = findNodes("//*[@property='ID']", doc
				.getDocumentElement());
		if (idNodes != null) {
			for (int i = 0; i < idNodes.getLength(); i++) {
				Element idEle = (Element) idNodes.item(i);
				// System.out.println("变更前的ID节点" + XmlUtils.toXML(idEle));
				Element idStringValueEle = (Element) idEle
						.getElementsByTagName("string").item(0);
				String oldIDValue = idStringValueEle.getTextContent();
				String newIDValue = getUID();
				idStringValueEle.setTextContent(newIDValue);
				// System.out.println("变更后的ID节点" + XmlUtils.toXML(idEle));
				// 找出其它关联id是oldIDValue的节点，该类节点的值要被newIDValue替换掉，这就解决了新版本id关联问题
				NodeList relatedIDNodes = findNodes("//void[string='"
						+ oldIDValue + "']", doc.getDocumentElement());
				for (int x = 0; x < relatedIDNodes.getLength(); x++) {
					Element relatedEle = (Element) relatedIDNodes.item(x);
					// System.err.println("与老ID" + oldIDValue + "关联的ID节点："
					// + XmlUtils.toXML(relatedEle));
					((Element) relatedEle.getElementsByTagName("string")
							.item(0)).setTextContent(newIDValue);
				}
			}
		}
		return XmlUtils.toXML(doc);
	}

	
	/**
	 * 隐藏表中的某列
	 * 
	 * @param index
	 */
	public static void hideColumn(JTable table ,int index) {
		TableColumn tc = table.getColumnModel().getColumn(index);
		tc.setMaxWidth(0);
		tc.setPreferredWidth(0);
		tc.setWidth(0);
		tc.setMinWidth(0);
		table.getTableHeader().getColumnModel().getColumn(index)
				.setMaxWidth(0);
		table.getTableHeader().getColumnModel().getColumn(index)
				.setMinWidth(0);
	}

}
