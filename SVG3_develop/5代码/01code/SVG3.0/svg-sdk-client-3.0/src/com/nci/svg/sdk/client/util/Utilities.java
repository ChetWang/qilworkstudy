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
	// * 线程池
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
	 * 通过document的字符串得到Document对象
	 * 
	 * @param documentString
	 * @return
	 */
	public static Document getXMLDocumentByString(String documentString) {
		try {
			// svg的编码必须是UTF-8
			return getXMLDocumentByStream(new ByteArrayInputStream(
					documentString.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过给定的InputStream对象获取XML Document对象
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
	 * 通过url来获取指定文档的Document对象
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
	 * 转换XML Node对象，要在JSvgCanvas对象上显示，必须是SVG相关的Element对象
	 * 
	 * @param doc
	 * @param generalNode
	 * @return
	 */
	public static Node parseNodeToSVGNode(Document doc, Node generalNode) {
		// 这里的namespace值必须是svgNS

		Element svgElement = doc.createElementNS(
				SVGDOMImplementation.SVG_NAMESPACE_URI, generalNode
						.getNodeName());
		NamedNodeMap namedNodeMap = generalNode.getAttributes();
		if (namedNodeMap != null) {
			for (int i = 0; i < namedNodeMap.getLength(); i++) {
				/** servlet中传递过来的图元不一定有style属性，即使有属性，其stroke也未必定义，所以这里全部加上* */
				if (namedNodeMap.item(i).getNodeName().equals("style")) {
					svgElement.setAttribute(namedNodeMap.item(i).getNodeName(),
							"fill:none;stroke:#101000;");
				} else
					svgElement.setAttribute(namedNodeMap.item(i).getNodeName(),
							namedNodeMap.item(i).getNodeValue());

			}
			// 没有style属性，则添加
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
	 * 从已知的svg文档url，获取该svg文档的SVGDocument对象
	 * 
	 * @param url
	 *            svg文档的url
	 * @return SVGDocument对象
	 */
	@Deprecated
	// 能使用SVG String内容的时候尽量使用getSVGDocumentByContent(String content)
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
	 * 根据svg文件内容和相对路径生成SVGDocument对象
	 * 
	 * @param content
	 *            文件内容
	 * @param uri
	 *            指定的相对路径
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
	 * 根据NCIEquipSymbolBean中的svg文件内容和相对路径生成SVGDocument对象
	 * 
	 * @param symbolBean
	 *            NCIEquipSymbolBean对象
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
	 * 将servlet参数与基本URL整合成一个servlet请求，其中param是一个二元数组，第一个元素是参数名，第二个元素是参数值
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
	// * 与Servlet一次通信过程，得到一个返回对象
	// *
	// * @param baseURL
	// * 基本URL
	// * @param param
	// * 传递给servlet的参数
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
	 * 查询指定Node中需要的属性的值，这个值在Node对象中必须唯一
	 * 
	 * @param attribute
	 *            查询的属性名称
	 * @param xpathExpr
	 *            xpath表达式
	 * @param doc
	 *            查询所在的Document对象
	 * @return 指定查询的属性名称对应的值
	 * @throws XPathExpressionException
	 */
	public static synchronized String findOneAttributeValue(String attribute,
			String xpathExpr, Node node) throws XPathExpressionException {
		XPathExpression expr = getXPath().compile(xpathExpr);
		Object result = expr.evaluate(node, XPathConstants.NODESET);
		for (int i = 0; i < ((NodeList) result).getLength(); i++) {
			Node child = ((NodeList) result).item(i);
			if (child instanceof Element)// 由于是唯一的属性，查询到值就返回
				return ((Element) child).getAttribute(attribute);
		}
		return null;
	}

	/**
	 * 查询指定Node中需要的子节点集合
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
	 * 查询指定Node中需要的属性元素，这个元素在Node对象中必须唯一
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
			// 由于是唯一的属性，查询到值就返回
			return child;
		}
		return null;
	}

	/**
	 * 获取带冒号的属性的值，这是一个无奈的方法，如果找到原因（无法直接从e中获取属性值），则不要用该方法。
	 * 
	 * @param e
	 *            属性所在的节点
	 * @param attrName
	 *            属性名称（带冒号）
	 * @return 属性值
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
	 * 打印xml节点字符串
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
	 * 打印Set中的元素字符串
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
	 * 清理缓存
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
	 * 获取选中的单个元素的metadata子元素
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
	 * 将选择的节点继续往下层，仅当如果仅有一个子元素并且这个子元素是use节点时停止。
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
	 * 将指定的图形节点标识为“无”关联设备
	 * 
	 * @param shapeElement
	 */
	public static void markShapeAsNoEquipRelation(Element shapeElement) {
		EditorToolkit.setStyleProperty(shapeElement,
				Constants.NCI_SVG_DOCUMENT_DASHARRAY_ATTR,
				Constants.NCI_SVG_DOCUMENT_DASHARRAY_DEFAULT_VALUE);
	}

	/**
	 * 将指定的图形节点标识为“有”关联设备
	 * 
	 * @param shapeElement
	 */
	public static void markShapeAsEquipRelated(Element shapeElement) {
		EditorToolkit.setStyleProperty(shapeElement,
				Constants.NCI_SVG_DOCUMENT_DASHARRAY_ATTR,
				Constants.NCI_SVG_DOCUMENT_DASHARRAY_NONE_VALUE);
	}

	/**
	 * 为指定的图形节点分配AppCode（PSMS 设备编号）
	 * 
	 * @param appCode
	 *            PSMS 设备编号
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
	 * 更新设备关联数据
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
		// if (!shapeEle.getNodeName().equals("text")) // 不应包含text
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
		sb.append("编号：" + bean.getId() + "\n");
		sb.append(
				ResourcesManager.bundle
						.getString("nci_graphunit_property_name")).append(
				bean.getName()).append("\n");
		sb.append(
				ResourcesManager.bundle
						.getString("nci_graphunit_property_type")).append(
				bean.getVariety().getName()).append("\n");
		sb.append("操作人：" + bean.getOperator() + "\n");
		sb.append("创建时间：" + bean.getCreateTime() + "\n");
		sb.append("修改时间：" + bean.getModifyTime() + "\n");

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
		Document xmlDoc = Utilities.getXMLDocumentByURL(new File("c:/嘉兴.svg")
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
	 * 获取Symbol大类的中文名字，是图元还是模板
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
	 * 获取图元或模板的代号（这里是英文，graphunit，template）
	 * 
	 * @param handleType
	 * @return 图元或模板的代号；graphunit，template
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
			System.err.println("不存在这种类型的Handle:" + handleType);
			break;
		}
		return type;
	}

	/**
	 * 初始化外观
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
	 * 执行线程池线程
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
