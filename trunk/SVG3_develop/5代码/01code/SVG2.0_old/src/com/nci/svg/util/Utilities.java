package com.nci.svg.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
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
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.SAXException;

import chrriis.dj.nativeswing.ui.JWebBrowser;
import chrriis.dj.nativeswing.ui.JWebBrowserWindow;

import com.nci.svg.graphunit.NCIEquipSymbolBean;
import com.nci.svg.shape.GraphUnitImageShape;

import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class Utilities {

	private static XPath xpath = XPathFactory.newInstance().newXPath();
	private static String user = "";
	private static boolean netWorkOK = true;
	private static Robot robot;
	private static JWebBrowserWindow browserFrame;
	private static JWebBrowser browser;

	static {

		// NativeInterfaceHandler.init();
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = bulider.parse(is);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	 * ����֪��xml�ַ�������SVGDocument����
	 * 
	 * @param xmlContent
	 * @return
	 */
	public static Document createSVGDocument(String xmlContent) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		// �����namespaceֵ������svgNS�����򴴽�������Document���󲻻���SVGDocument
		Document doc = impl.createDocument(Constants.svgNS, "svg", null);
		Element root = doc.getDocumentElement();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder bulider;
		try {
			bulider = factory.newDocumentBuilder();
			Document generaldoc = bulider.parse(new ByteArrayInputStream(
					xmlContent.getBytes("utf-8")));
			NodeList children = generaldoc.getDocumentElement().getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node node = doc.importNode(children.item(i), true);
				if (!node.getNodeName().equals("#text")) {
					root.appendChild(parseNodeToSVGNode(doc, node));
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
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
	public static Document getSVGDocument(String url) {
		Document doc = null;
		int count = 0;
		try {
			// SAXSVGDocumentFactory svgDocumentFactory = new
			// SAXSVGDocumentFactory(
			// XMLResourceDescriptor.getXMLParserClassName());
			doc = svgDocumentFactory.createDocument(url);
			return doc;
		} catch (Exception e) {
			if (count < 3) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
				}
				count++;
				doc = getSVGDocument(url);
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

	/**
	 * ��Servletһ��ͨ�Ź��̣��õ�һ�����ض���
	 * 
	 * @param baseURL
	 *            ����URL
	 * @param param
	 *            ���ݸ�servlet�Ĳ���
	 * @return
	 * @throws IOException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static synchronized Object communicateWithURL(String baseURL, String[][] param)
			throws IOException {
		// if (netWorkOK) {
		try {
			URL url = new URL(baseURL);
			URLConnection rConn = url.openConnection();
			rConn.setDoOutput(true);
			rConn.setDoInput(true);
			rConn.setReadTimeout(Constants.SERVLET_TIME_OUT);
			PrintStream output = new PrintStream(rConn.getOutputStream());
			StringBuffer rs = new StringBuffer();
			if (param != null && param.length > 0) {
				for (int i = 0; i < param.length; i++) {
					if (param[i][0] == null || param[i][1] == null)
						continue;
					if (i != 0) {
						rs.append("&");
					}
					rs.append(URLEncoder.encode(param[i][0], "UTF-8")).append(
							'=')
							.append(URLEncoder.encode(param[i][1], "UTF-8"));
				}
			}
			output.print(rs.toString());
			output.flush();
			ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream(rConn.getInputStream()));
			return ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			netWorkOK = false;
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					JOptionPane.showConfirmDialog(null, ResourcesManager.bundle
							.getString("nci_network_err"),
							ResourcesManager.bundle
									.getString("nci_optionpane_error_title"),
							JOptionPane.CLOSED_OPTION,
							JOptionPane.ERROR_MESSAGE);
				}
			});

			throw e;
			// }
		}
		return "";
	}

	public static XPath getXPath() {

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
		XPathExpression expr = xpath.compile(xpathExpr);
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
		XPathExpression expr = xpath.compile(xpathExpr);
		Object result = expr.evaluate(sourceNode, XPathConstants.NODESET);
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
		XPathExpression expr = xpath.compile(xpathExpr);
		Object result = expr.evaluate(sourceNode, XPathConstants.NODESET);
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
		StringBuffer sb = new StringBuffer();
		XMLPrinter.writeNode(node, sb, 0, true, null);
		if (isPrint)
			System.out.println(sb.toString());
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
		if(child == null)
		{
		    child = getSingleSibingElement(tagName,selectedElement);
		}
		return child;
	}
	
	public static Element getSingleSibingElement(String tagName,
            Element selectedElement) {
	    Element parentElement = (Element)selectedElement.getParentNode();
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

	public static void updateNCIGraphUnitXLink(Document svgDoc, Editor editor) {
		try {
			String baseXlink = Constants.NCI_SVG_SYMBOL_CACHE_DIR;
			NodeList nciGraphUnits = Utilities.findNodes(
					"//*[name()='image'][@" + Constants.NCI_SVG_Type_Attr
							+ "='" + Constants.NCI_SVG_Type_GraphUnit_Value
							+ "']", svgDoc.getDocumentElement());
			Element graphUnit = null;
			String relaPath = null;
			NCIEquipSymbolBean bean = null;
			for (int i = 0; i < nciGraphUnits.getLength(); i++) {
				graphUnit = (Element) nciGraphUnits.item(i);
				bean = editor
						.getGraphUnitManager()
						.getOriginalSymbolMap()
						.get(
								graphUnit
										.getAttribute(GraphUnitImageShape.NCI_GraphUnit_IDENTIFIER_NAME));
				relaPath = bean.getGraphUnitType() + "/"
						+ bean.getGraphUnitName();
				graphUnit.setAttributeNS("http://www.w3.org/1999/xlink",
						"xlink:href", "file:/" + baseXlink + relaPath
								+ Constants.NCI_SVG_EXTENDSION);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getGraphUnitPropertyDesc(NCIEquipSymbolBean bean) {
		StringBuffer sb = new StringBuffer();
		sb.append(
				ResourcesManager.bundle
						.getString("nci_graphunit_property_name")).append(
				bean.getGraphUnitName()).append("\n");
		sb.append(
				ResourcesManager.bundle
						.getString("nci_graphunit_property_type")).append(
				bean.getGraphUnitType()).append("\n");
		sb.append(
				ResourcesManager.bundle
						.getString("nci_graphunit_property_status")).append(
				bean.getGraphUnitStatus()).append("\n");
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
		SVGDocument doc = (SVGDocument) Utilities.createSVGDocument(xml);
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

		canvas.setDocument(Utilities.getSVGDocument(new File("C:/temp.svg")
				.toURI().toString()));
		// frame.setSize(800, 600);
		frame.pack();
		frame.setVisible(true);
	}

	public static Robot getRobot() {
		return robot;
	}

	public static void gotoWebSite(String url, String title) {
		// String osName = System.getProperty("os.name");
		// if (osName.indexOf("indows") >= 0) {
		// try {
		// Runtime.getRuntime().exec(
		// "rundll32 url.dll,FileProtocolHandler " + url);
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// } else {
		//			
		// }
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.indexOf("windows") > -1) {
			try {
//				Runtime.getRuntime().exec("cmd.exe /c start " + url);
			    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (browser == null)
				browser = new JWebBrowser();
			if (browserFrame == null) {
				browserFrame = new JWebBrowserWindow();
				browserFrame.setTitle(title);
				// browserFrame.setIconImage(ResourcesManager.getIcon("Editor",
				// false).getImage());
				browserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				browserFrame.getContentPane().add(browser);
				browser.setMenuBarVisible(false);
				browserFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			}
			browser.setURL(url);
			browserFrame.setVisible(true);
		}
	}

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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
