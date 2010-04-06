package com.nci.svg.sdk.client.util;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.SAXException;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.office2003.BasicOffice2003Theme;
import com.jidesoft.plaf.office2003.Office2003Painter;
import com.jidesoft.utils.SystemInfo;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;

import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class Utilities {

	private static XPath xpath = null;

	private static boolean netWorkOK = true;
	private static Robot robot;
	// /**
	// * �̳߳�
	// */
	private static ThreadPoolExecutor pool = new ThreadPoolExecutor(20, 25, 5,
			TimeUnit.SECONDS, new LinkedBlockingQueue(),
			new ThreadPoolExecutor.DiscardOldestPolicy());

	static {

		try {
			robot = new Robot();
			NamespaceContext ctx = new NamespaceContext() {
				public String getNamespaceURI(String prefix) {
					// System.err.println(prefix);
					if (prefix.equals("svg")
							|| prefix.equals("defs")
							|| EditorToolkit.svgShapeElementNames
									.contains(prefix)) {
						return EditorToolkit.svgNS;
					}
					if (prefix.equals("xlink")) {
						return EditorToolkit.xmlnsXLinkNS;
					}
					return null;
				}

				public Iterator getPrefixes(String val) {
					return null;
				}

				public String getPrefix(String uri) {
					return null;
				}
			};
			getXPath().setNamespaceContext(ctx);
		} catch (AWTException e) {

			e.printStackTrace();
		}
	}

	public static void deleteFile(File f) {
		if (f.isDirectory()) {
			File[] childF = f.listFiles();
			for (File child : childF) {
				deleteFile(child);
			}
			f.delete();
		} else {
			f.delete();
		}
	}

	/**
	 * ͨ��document���ַ����õ�Document����
	 * 
	 * @param documentString
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
	 * ͨ��������InputStream�����ȡXML Document����
	 * 
	 * @param is
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
	 * ͨ��url����ȡָ���ĵ���Document����
	 * 
	 * @param url
	 * @return
	 */
	public static Document getXMLDocumentByURL(String url) {
		try {
			// System.out.println("url=" + url);
			URL u = new URL(url);
			InputStream in = null;
			try {
				in = u.openStream();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Document doc = getXMLDocumentByStream(in);
			return doc;
		} catch (Exception e) {
			System.err.println("*********************************************");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ת��XML Node����Ҫ��JSvgCanvas��������ʾ��������SVG��ص�Element����
	 * 
	 * @param doc
	 * @param generalNode
	 * @return
	 */
	public static Node parseNodeToSVGNode(Document doc, Node generalNode) {
		// �����namespaceֵ������svgNS

		Element svgElement = doc.createElementNS(
				SVGDOMImplementation.SVG_NAMESPACE_URI, generalNode
						.getNodeName());
		NamedNodeMap namedNodeMap = generalNode.getAttributes();
		if (namedNodeMap != null) {
			for (int i = 0; i < namedNodeMap.getLength(); i++) {
				/** servlet�д��ݹ�����ͼԪ��һ����style���ԣ���ʹ�����ԣ���strokeҲδ�ض��壬��������ȫ������* */
				if (namedNodeMap.item(i).getNodeName().equals("style")) {
					svgElement.setAttribute(namedNodeMap.item(i).getNodeName(),
							"fill:none;stroke:#101000;");
				} else
					svgElement.setAttribute(namedNodeMap.item(i).getNodeName(),
							namedNodeMap.item(i).getNodeValue());

			}
			// û��style���ԣ������
			if (svgElement.getAttribute("style") == null
					|| svgElement.getAttribute("style").equals("")) {
				svgElement.setAttribute("style", "fill:none;stroke:#101000;");
			}
			if (generalNode.hasChildNodes()) {
				NodeList children = generalNode.getChildNodes();
				for (int n = 0; n < children.getLength(); n++) {
					if (children.item(n) instanceof Element) {
						Node node = children.item(n);
						if (!node.getNodeName().equals("#text")) {
							Node child = parseNodeToSVGNode(doc, node);
							svgElement.appendChild(child);
						}
					}
				}
			}
		}

		return svgElement;
	}

	private static SAXSVGDocumentFactory svgDocumentFactory = new SAXSVGDocumentFactory(
			XMLResourceDescriptor.getXMLParserClassName());

	/**
	 * ����֪��svg�ĵ�url����ȡ��svg�ĵ���SVGDocument����
	 * 
	 * @param url
	 *            svg�ĵ���url
	 * @return SVGDocument����
	 */
	@Deprecated
	// ��ʹ��SVG String���ݵ�ʱ����ʹ��getSVGDocumentByContent(String content)
	public static Document getSVGDocumentByURL(String url) {
		Document doc = null;
		int count = 0;
		try {
			doc = svgDocumentFactory.createDocument(url);
			return doc;
		} catch (Exception e) {
			if (count < 3) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
				}
				count++;
				doc = getSVGDocumentByURL(url);
			} else {
				System.out
						.println("*********************************************"
								+ url);
				e.printStackTrace();
			}
		}
		return doc;
	}

	/**
	 * ����svg�ļ����ݺ����·������SVGDocument����
	 * 
	 * @param content
	 *            �ļ�����
	 * @param uri
	 *            ָ�������·��
	 * @return
	 */
	public synchronized static Document getSVGDocumentByContent(String content,
			String uri) {
		StringReader reader = new StringReader(content);
		Document doc = null;
		try {
			doc = svgDocumentFactory.createSVGDocument(uri, reader);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			reader.close();
		}
		return doc;
	}

	/**
	 * ����NCIEquipSymbolBean�е�svg�ļ����ݺ����·������SVGDocument����
	 * 
	 * @param symbolBean
	 *            NCIEquipSymbolBean����
	 * @return
	 */
	public synchronized static Document getSVGDocumentByContent(
			NCIEquipSymbolBean symbolBean) {
		StringReader reader = new StringReader(symbolBean.getContent());
		Document doc = null;
		try {
			doc = svgDocumentFactory.createSVGDocument("file://"
					+ symbolBean.getName(), reader);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			reader.close();
		}
		return doc;
	}

	/**
	 * ��servlet���������URL���ϳ�һ��servlet��������param��һ����Ԫ���飬��һ��Ԫ���ǲ��������ڶ���Ԫ���ǲ���ֵ
	 * 
	 * @param baseURL
	 * @param param
	 * @return
	 */
	public synchronized static StringBuffer parseURL(String baseURL,
			String[][] param) {
		StringBuffer sb = new StringBuffer(baseURL);
		try {
			for (int i = 0; i < param.length; i++) {
				sb.append("&");
				sb.append(URLEncoder.encode(param[i][0], "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(param[i][1], "UTF-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	// /**
	// * ��Servletһ��ͨ�Ź��̣��õ�һ�����ض���
	// *
	// * @param baseURL
	// * ����URL
	// * @param param
	// * ���ݸ�servlet�Ĳ���
	// * @return
	// * @throws IOException
	// * @throws IOException
	// * @throws ClassNotFoundException
	// */
	// public static synchronized Object communicateWithURL(String baseURL,
	// String[][] param)
	// throws IOException {
	// // if (netWorkOK) {
	// try {
	// URL url = new URL(baseURL);
	// URLConnection rConn = url.openConnection();
	// rConn.setDoOutput(true);
	// rConn.setDoInput(true);
	// rConn.setReadTimeout(Constants.SERVLET_TIME_OUT);
	// PrintStream output = new PrintStream(rConn.getOutputStream());
	// StringBuffer rs = new StringBuffer();
	// if (param != null && param.length > 0) {
	// for (int i = 0; i < param.length; i++) {
	// if (param[i][0] == null || param[i][1] == null)
	// continue;
	// if (i != 0) {
	// rs.append("&");
	// }
	// rs.append(URLEncoder.encode(param[i][0], "UTF-8")).append(
	// '=')
	// .append(URLEncoder.encode(param[i][1], "UTF-8"));
	// }
	// }
	// output.print(rs.toString());
	// output.flush();
	// ObjectInputStream ois = new ObjectInputStream(
	// new BufferedInputStream(rConn.getInputStream()));
	// return ois.readObject();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// netWorkOK = false;
	// SwingUtilities.invokeLater(new Runnable() {
	//
	// public void run() {
	// JOptionPane.showConfirmDialog(null, ResourcesManager.bundle
	// .getString("nci_network_err"),
	// ResourcesManager.bundle
	// .getString("nci_optionpane_error_title"),
	// JOptionPane.CLOSED_OPTION,
	// JOptionPane.ERROR_MESSAGE);
	// }
	// });
	//
	// throw e;
	// // }
	// }
	// return "";
	// }

	public static XPath getXPath() {
		if (xpath == null) {
			xpath = XPathFactory.newInstance().newXPath();
		}
		return xpath;
	}

	/**
	 * ��ѯָ��Node����Ҫ�����Ե�ֵ�����ֵ��Node�����б���Ψһ
	 * 
	 * @param attribute
	 *            ��ѯ����������
	 * @param xpathExpr
	 *            xpath���ʽ
	 * @param doc
	 *            ��ѯ���ڵ�Document����
	 * @return ָ����ѯ���������ƶ�Ӧ��ֵ
	 * @throws XPathExpressionException
	 */
	public static synchronized String findOneAttributeValue(String attribute,
			String xpathExpr, Node node) throws XPathExpressionException {
		XPathExpression expr = getXPath().compile(xpathExpr);
		Object result = expr.evaluate(node, XPathConstants.NODESET);
		for (int i = 0; i < ((NodeList) result).getLength(); i++) {
			Node child = ((NodeList) result).item(i);
			if (child instanceof Element)// ������Ψһ�����ԣ���ѯ��ֵ�ͷ���
				return ((Element) child).getAttribute(attribute);
		}
		return null;
	}

	/**
	 * ��ѯָ��Node����Ҫ���ӽڵ㼯��
	 * 
	 * @param xpathExpr
	 * @param sourceNode
	 * @return
	 * @throws XPathExpressionException
	 */
	public static synchronized NodeList findNodes(String xpathExpr,
			Node sourceNode) throws XPathExpressionException {
		// final PrefixResolver resolver = new
		// PrefixResolverDefault(sourceNode.getOwnerDocument().getDocumentElement());

		Object result = getXPath().evaluate(xpathExpr, sourceNode,
				XPathConstants.NODESET);
		return (NodeList) result;
	}

	/**
	 * ��ѯָ��Node����Ҫ������Ԫ�أ����Ԫ����Node�����б���Ψһ
	 * 
	 * @param attribute
	 * @param attrValue
	 * @param xpathExpr
	 * @param sourceNode
	 * @return
	 * @throws javax.xml.xpath.XPathExpressionException
	 */
	public static synchronized Node findNode(String xpathExpr, Node sourceNode)
			throws XPathExpressionException {
		Object result = getXPath().evaluate(xpathExpr, sourceNode,
				XPathConstants.NODESET);
		for (int i = 0; i < ((NodeList) result).getLength(); i++) {
			Node child = ((NodeList) result).item(i);
			// ������Ψһ�����ԣ���ѯ��ֵ�ͷ���
			return child;
		}
		return null;
	}

	/**
	 * ��ȡ��ð�ŵ����Ե�ֵ������һ�����εķ���������ҵ�ԭ���޷�ֱ�Ӵ�e�л�ȡ����ֵ������Ҫ�ø÷�����
	 * 
	 * @param e
	 *            �������ڵĽڵ�
	 * @param attrName
	 *            �������ƣ���ð�ţ�
	 * @return ����ֵ
	 * @deprecated
	 */
	public static synchronized String getOneAttributeValueWithColone(Element e,
			String attrName) {
		StringBuffer sb = new StringBuffer();
		XMLPrinter.writeNode(e, sb, 0, true, null);
		Document doc = Utilities.getXMLDocumentByString(sb.toString());
		Element newEle = doc.getDocumentElement();
		String linkedFile = newEle.getAttribute(attrName);
		sb = null;
		doc = null;
		newEle = null;
		return linkedFile;
	}

	/**
	 * ��ӡxml�ڵ��ַ���
	 * 
	 * @param node
	 */
	public static String printNode(Node node, boolean isPrint) {
		if (node instanceof Document)
			node = ((Document) node).getDocumentElement();
		StringBuffer sb = new StringBuffer();
		XMLPrinter.writeNode(node, sb, 0, true, null);
		if (isPrint)
			System.err.println(sb.toString());
		return sb.toString();
	}

	/**
	 * ��ӡSet�е�Ԫ���ַ���
	 * 
	 * @param s
	 */
	public static void printSet(Set s) {
		Iterator it = s.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	/**
	 * ������
	 * 
	 * @param folder
	 */
	public synchronized static void clearCacheFolder(File folder) {
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File[] clipCacheFiles = folder.listFiles();
		for (int i = 0; i < clipCacheFiles.length; i++) {
			clipCacheFiles[i].delete();
		}
	}

	/**
	 * ��ȡѡ�еĵ���Ԫ�ص�metadata��Ԫ��
	 * 
	 * @param selectedElement
	 * @return
	 */
	public static Element getSingleChildElement(String tagName,
			Element selectedElement) {
		Element child = null;
		NodeList children = selectedElement.getElementsByTagName(tagName);
		if (children.getLength() > 0)
			child = (Element) children.item(0);
		if (child == null) {
			child = getSingleSibingElement(tagName, selectedElement);
		}
		return child;
	}

	public static Element getSingleSibingElement(String tagName,
			Element selectedElement) {
		Element parentElement = (Element) selectedElement.getParentNode();
		Element child = null;
		NodeList children = parentElement.getElementsByTagName(tagName);
		if (children.getLength() > 0)
			child = (Element) children.item(0);
		return child;
	}

	/**
	 * ��ѡ��Ľڵ�������²㣬�����������һ����Ԫ�ز��������Ԫ����use�ڵ�ʱֹͣ��
	 * 
	 * @param selectedElement
	 * @return
	 */
	public static Element parseSelectedElement(Element selectedElement) {
		if (selectedElement.getNodeName().equals("g")
				&& selectedElement.getChildNodes().getLength() == 1) {
			if (selectedElement.getChildNodes().item(0).getNodeName().equals(
					"use")) {
				return (Element) selectedElement.getChildNodes().item(0);
			}
			// else
			// if(selectedElement.getChildNodes().item(0).getNodeName().equals("g"
			// )){
			// selectedElement =
			// (Element)selectedElement.getChildNodes().item(0);
			// return parseSelectedElement(selectedElement);
			// }
		}
		return selectedElement;
	}

	/**
	 * ��ָ����ͼ�νڵ��ʶΪ���ޡ������豸
	 * 
	 * @param shapeElement
	 */
	public static void markShapeAsNoEquipRelation(Element shapeElement) {
		EditorToolkit.setStyleProperty(shapeElement,
				Constants.NCI_SVG_DOCUMENT_DASHARRAY_ATTR,
				Constants.NCI_SVG_DOCUMENT_DASHARRAY_DEFAULT_VALUE);
	}

	/**
	 * ��ָ����ͼ�νڵ��ʶΪ���С������豸
	 * 
	 * @param shapeElement
	 */
	public static void markShapeAsEquipRelated(Element shapeElement) {
		EditorToolkit.setStyleProperty(shapeElement,
				Constants.NCI_SVG_DOCUMENT_DASHARRAY_ATTR,
				Constants.NCI_SVG_DOCUMENT_DASHARRAY_NONE_VALUE);
	}

	/**
	 * Ϊָ����ͼ�νڵ����AppCode��PSMS �豸��ţ�
	 * 
	 * @param appCode
	 *            PSMS �豸���
	 * @param shapeElement
	 */
	public static void assighAppCode(String appCode, Element shapeElement) {
		NodeList children = shapeElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals(Constants.NCI_SVG_METADATA)) {
				NodeList children2 = child.getChildNodes();
				for (int k = 0; k < children2.getLength(); k++) {
					Node child2 = children2.item(k);
					if (child2.getNodeName().equals(
							Constants.NCI_SVG_PSR_OBJREF)) {
						((Element) child2).setAttribute(
								Constants.NCI_SVG_APPCODE_ATTR, appCode);
					}
				}
			}
		}
	}

	/**
	 * �����豸��������
	 * 
	 * @param handle
	 */
	public static void updateRelationUI(SVGDocument svgDoc) {
		// try {
		// NodeList PSR_ObjRefs = Utilities.findNodes("//*[name()='"
		// + Constants.NCI_SVG_PSR_OBJREF + "'][@"
		// + Constants.NCI_SVG_APPCODE_ATTR + "='']", svgDoc
		// .getDocumentElement());
		// for (int i = 0; i < PSR_ObjRefs.getLength(); i++) {
		// Element shapeEle = (Element) PSR_ObjRefs.item(i)
		// .getParentNode().getParentNode();
		// if (!shapeEle.getNodeName().equals("text")) // ��Ӧ����text
		// Utilities.markShapeAsNoEquipRelation(shapeEle);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	// public static void updateNCIGraphUnitXLink(Document svgDoc, EditorAdapter
	// editor) {
	// try {
	// String baseXlink = Constants.NCI_SVG_SYMBOL_CACHE_DIR;
	// NodeList nciGraphUnits = Utilities.findNodes(
	// "//*[name()='image'][@" + Constants.NCI_SVG_Type_Attr
	// + "='" + Constants.NCI_SVG_Type_GraphUnit_Value
	// + "']", svgDoc.getDocumentElement());
	// Element graphUnit = null;
	// String relaPath = null;
	// NCIEquipSymbolBean bean = null;
	// for (int i = 0; i < nciGraphUnits.getLength(); i++) {
	// graphUnit = (Element) nciGraphUnits.item(i);
	// bean = editor
	// .getGraphUnitManager()
	// .getOriginalSymbolMap()
	// .get(
	// graphUnit
	// .getAttribute(GraphUnitImageShape.NCI_GraphUnit_IDENTIFIER_NAME));
	// relaPath = bean.getGraphUnitType() + "/"
	// + bean.getGraphUnitName();
	// graphUnit.setAttributeNS("http://www.w3.org/1999/xlink",
	// "xlink:href", "file:/" + baseXlink + relaPath
	// + Constants.NCI_SVG_EXTENDSION);
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public static String getGraphUnitPropertyDesc(NCIEquipSymbolBean bean) {
		StringBuffer sb = new StringBuffer();
		sb.append("��ţ�" + bean.getId() + "\n");
		sb.append(
				ResourcesManager.bundle
						.getString("nci_graphunit_property_name")).append(
				bean.getName()).append("\n");
		sb.append(
				ResourcesManager.bundle
						.getString("nci_graphunit_property_type")).append(
				bean.getVariety().getName()).append("\n");
		sb.append("�����ˣ�" + bean.getOperator() + "\n");
		sb.append("����ʱ�䣺" + bean.getCreateTime() + "\n");
		sb.append("�޸�ʱ�䣺" + bean.getModifyTime() + "\n");

		return sb.toString();
	}

	public static boolean isNetworkOK() {
		return netWorkOK;
	}

	public static String getTempFileName() {
		return Constants.NCI_SVG_CACHE_CLIP_TEMP_CACHE_DIR
				+ System.currentTimeMillis() + "temp.svg";
	}

	public static void main(String[] xxx) {
		Document xmlDoc = Utilities.getXMLDocumentByURL(new File("c:/����.svg")
				.toURI().toString());
		String xml = Utilities.printNode(xmlDoc.getDocumentElement(), false);
		SVGDocument doc = (SVGDocument) Utilities.getSVGDocumentByContent(xml,
				"file://xx");
		XMLPrinter.printStringToFile(xml, new File(
				Constants.NCI_SVG_CACHE_CLIP_TEMP_CACHE_DIR
						+ System.currentTimeMillis() + "temp.svg"));
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JSVGCanvas canvas = new JSVGCanvas();
		canvas.setRecenterOnResize(true);
		JScrollPane p = new JScrollPane();
		p.setViewportView(canvas);
		frame.getContentPane().add(p);

		// canvas.setURI(new File("C:/temp.svg")
		// .toURI().toString());

		// canvas.setDocument(Utilities.getSVGDocumentByURL(new
		// File("C:/temp.svg")
		// .toURI().toString()));
		canvas.setDocument(doc);
		// frame.setSize(800, 600);
		frame.pack();
		frame.setVisible(true);
	}

	public static Robot getRobot() {
		return robot;
	}

	//
	// public static void gotoWebSite(String url, String title) {
	// // String osName = System.getProperty("os.name");
	// // if (osName.indexOf("indows") >= 0) {
	// // try {
	// // Runtime.getRuntime().exec(
	// // "rundll32 url.dll,FileProtocolHandler " + url);
	// // } catch (IOException e1) {
	// //
	// // e1.printStackTrace();
	// // }
	// // } else {
	// //
	// // }
	// String osName = System.getProperty("os.name").toLowerCase();
	// if (osName.indexOf("windows") > -1) {
	// try {
	// // Runtime.getRuntime().exec("cmd.exe /c start " + url);
	// Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
	// } catch (IOException e) {
	//				
	// e.printStackTrace();
	// }
	// } else {
	// if (browser == null)
	// browser = new JWebBrowser();
	// if (browserFrame == null) {
	// browserFrame = new JWebBrowserWindow();
	// browserFrame.setTitle(title);
	// // browserFrame.setIconImage(ResourcesManager.getIcon("Editor",
	// // false).getImage());
	// browserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	// browserFrame.getContentPane().add(browser);
	// browser.setMenuBarVisible(false);
	// browserFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	// }
	// browser.setURL(url);
	// browserFrame.setVisible(true);
	// }
	// }

	public static void openEditor() {
		String osName = System.getProperty("os.name");
		if (osName.indexOf("indows") >= 0) {
			try {
				String[] cmd = {
						"set JAVA_HOME=C:/Program Files/Java/jre1.6.0_05",
						"set EXE_JAVA=\"%JAVA_HOME%\"/bin/java",
						"%EXE_JAVA% fr.itris.glips.svgeditor.EditorMain" };
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		}
	}

	/**
	 * ��ȡSymbol������������֣���ͼԪ����ģ��
	 * 
	 * @param bean
	 * @return
	 */
	public static String getSinoSymbolType(SymbolTypeBean bean) {
		String type = bean.getSymbolType();
		if (type.equals(NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT)) {
			return Constants.SINO_NAMED_GRAPHUNIT;
		}
		if (type.equals(NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			return Constants.SINO_NAMED_TEMPLATE;
		}
		return null;
	}

	/**
	 * ��ȡͼԪ��ģ��Ĵ��ţ�������Ӣ�ģ�graphunit��template��
	 * 
	 * @param handleType
	 * @return ͼԪ��ģ��Ĵ��ţ�graphunit��template
	 */
	public static String getHandleSymbolType(int handleType) {
		String type = "";
		switch (handleType) {
		case SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL:
			type = NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT;
			break;
		case SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE:
			type = NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE;
			break;
		default:
			System.err.println("�������������͵�Handle:" + handleType);
			break;
		}
		return type;
	}

	/**
	 * ��ʼ�����
	 */
	public static void installLAF() {
		try {
			if (SystemInfo.isWindows()) {
				BasicOffice2003Theme theme = new BasicOffice2003Theme("Custom");
				theme.setBaseColor(new Color(150, 90, 150), true, "c1");
				((Office2003Painter) Office2003Painter.getInstance())
						.addTheme(theme);
				LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
			} else {
				try {
					UIManager
							.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");

					// UIManager.setLookAndFeel(new MetalLookAndFeel());
					// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (Exception e) {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					// UIManager
					// .setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
					// LookAndFeelFactory
					// .installJideExtension(LookAndFeelFactory.XERTO_STYLE);
				}

			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(new MetalLookAndFeel());
			} catch (UnsupportedLookAndFeelException e1) {
			}
		}
	}

	/**
	 * ִ���̳߳��߳�
	 * 
	 * @param runnable
	 */
	public static void executeRunnable(final Runnable runnable) {
		pool.execute(runnable);
		// runnable.run();
		// new SwingWorker(){
		//
		// @Override
		// protected Object doInBackground() throws Exception {
		// runnable.run();
		// return null;
		// }
		//			
		// }.execute();
	}

}
