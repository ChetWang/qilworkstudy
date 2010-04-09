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
 * ���ﶨ����һϵ�е�ͨ�÷���
 * 
 * @author Qil.Wong
 */
public class Functions {
	/**
	 * ��һ���ַ��������һ���ļ�
	 * 
	 * @param content
	 *            Ҫ������ַ���
	 * @param filename
	 *            �ļ���
	 * @param encoding
	 *            �������� ���Ϊnull���߿��ַ�����ȡĬ��
	 * @return 0 ��ʾ�ɹ� -1 ��ʾʧ��
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
	 * �ڶ�ά�����в���ĳ��ֵ
	 * 
	 * @param strArr
	 *            �����ά����
	 * @param value
	 *            �����ҵ�ֵ
	 * @param index
	 *            ����ֵ
	 * @param ret
	 *            ��������
	 * @param defaultRet
	 *            ȱʡ����ֵ
	 * @return �鵽���ַ���
	 */
	public static String getValueByIndex(String[][] strArr, String value,
			int index, int ret, String defaultRet) {
		for (int i = 0; i < strArr.length; i++)
			if (strArr[i][index].equals(value))
				return strArr[i][ret];
		return defaultRet;
	}

	/**
	 * ��һ��url�õ�Document����
	 * 
	 * @param url
	 *            һ��url·��
	 * @return Document����
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
	 * �õ�ʱ��ı�׼�ַ�����ʽ ��ʽ�� yyyy-mm-dd hh:mm:ss ע��û�к���
	 * 
	 * @param d
	 *            Calendarʵ��
	 * @return ʱ��ı�׼�ַ�����ʽ
	 */
	public static String getStrFromDateTime(Calendar d) {
		return d.get(Calendar.YEAR) + "-" + (d.get(Calendar.MONTH) + 1) + "-"
				+ d.get(Calendar.DATE) + " " + d.get(Calendar.HOUR_OF_DAY)
				+ ":" + d.get(Calendar.MINUTE) + ":" + d.get(Calendar.SECOND);
	}

	/**
	 * �õ�����һ��Component��Frame
	 * 
	 * @param compOnApplet
	 *            һ��Component
	 * @return ����һ��Component��Frame
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
	 * �ӷ�����Ϣ�н������ɹ�ʧ���Լ���ϸ������
	 * 
	 * @param str
	 *            ������Ϣ�ַ���
	 * @return ����������Ϣ
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
	 * ��һ���ַ���ת��Ϊת����ʽ ����utf8����
	 * 
	 * @param str
	 *            �����ַ���
	 * @return ����ת����ַ���
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
	 * �õ�Ψһ��id
	 * 
	 * @return Ψһ��id
	 */
	public synchronized static long genrateOnlyId() {
		return System.currentTimeMillis();
	}

	private static long orginidTime = System.nanoTime();

	/**
	 * ����UUID��WofoID����
	 * 
	 * @return
	 */
	public synchronized static String getUID() {
		String id = new UUID(orginidTime, ++orginidTime).toString()
				.toUpperCase();
		return id;
	}

	/**
	 * ����UUID��String����
	 * 
	 * @return
	 */
	public synchronized static String getUIDString() {
		return new UUID(orginidTime, ++orginidTime).toString().toUpperCase();
	}

	/**
	 * �ж��ַ����Ƿ�Ϊ�� ����Ϊ���ַ�������null
	 * 
	 * @param s
	 *            �ַ���
	 * @return �շ���true
	 */
	public static boolean stringIsEmpty(String s) {
		return s == null || Trim(s).equals("");
	}

	/**
	 * �ж��ǲ��ǷǸ�����
	 * 
	 * @param s
	 *            �ַ���
	 * @return �ǷǸ���������true
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
	 * ���¶�����һ���޼��ַ����ķ��� ��ȫ�ǿո񻻳ɰ�ǿո���ת
	 * 
	 * @param s
	 *            ���޼����ַ���
	 * @return �޼������ַ���
	 */
	public static String Trim(String s) {
		return s.replaceAll("��", " ").trim();
	}

	/**
	 * ���ַ����еļ��������ַ�ת��xmlת���ʽ ����& < > ' " ������ַ�
	 * 
	 * @param str
	 *            �����ַ���
	 * @return ת�����ַ���
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
	 * �õ�URL��Դ���ص������ַ���
	 * 
	 * @param url
	 *            URL��Դ��·�� �� "http://192.168.0.201/java/servlet"
	 * @param params
	 *            ��ά�ַ������飬�ṩһϵ�У���/ֵ�� String params[][]={ {"request","query"},
	 *            {"format","xml"},
	 *            {"query","select * from substation where name like '%�̱�%'"} };
	 * @return ���ص������ַ���
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
	 * �洢��ͼ��
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
	 * ���ݲ���ģʽ��ȡ���
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
	 * ���ַ���ת���ɹյ��б�,�ַ�����ʽ�ǡ�x1,x1|x2,y2
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
	 * ����xpath���ҵ���ElementԪ�أ�����ж���������ϣ����ص�һ��
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
				// ������Ψһ�����ԣ���ѯ��ֵ�ͷ���
				return (Element) child;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ����xpath�������з���������ElementԪ�ؼ���
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
	 * ͨ�����ķ�ʽ��������¡һ������
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
	 * �����л����xml�����°汾���䣬���ڸ���ճ�����°汾����ʱ������
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
				// System.out.println("���ǰ��ID�ڵ�" + XmlUtils.toXML(idEle));
				Element idStringValueEle = (Element) idEle
						.getElementsByTagName("string").item(0);
				String oldIDValue = idStringValueEle.getTextContent();
				String newIDValue = getUID();
				idStringValueEle.setTextContent(newIDValue);
				// System.out.println("������ID�ڵ�" + XmlUtils.toXML(idEle));
				// �ҳ���������id��oldIDValue�Ľڵ㣬����ڵ��ֵҪ��newIDValue�滻������ͽ�����°汾id��������
				NodeList relatedIDNodes = findNodes("//void[string='"
						+ oldIDValue + "']", doc.getDocumentElement());
				for (int x = 0; x < relatedIDNodes.getLength(); x++) {
					Element relatedEle = (Element) relatedIDNodes.item(x);
					// System.err.println("����ID" + oldIDValue + "������ID�ڵ㣺"
					// + XmlUtils.toXML(relatedEle));
					((Element) relatedEle.getElementsByTagName("string")
							.item(0)).setTextContent(newIDValue);
				}
			}
		}
		return XmlUtils.toXML(doc);
	}

	
	/**
	 * ���ر��е�ĳ��
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
