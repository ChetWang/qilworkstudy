package com.nci.svg.util.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import oracle.sql.BLOB;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nci.svg.shape.GraphUnitImageShape;
import com.nci.svg.util.Constants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.EditorToolkit;

/**
 * 将单个xml的图元文件转换成多个svg文件
 * 
 * @author Qil.Wong
 * 
 */
public class SymbolXMLConvertor {
	
	private static String singleSymbolXML = null;
	
	private static String hnSymbolXML = null;

	private static String newSymbolSVG = null;


	private String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;


	private static String convertedSymbolDir = null;


	private HashMap<String, String> code_data = new HashMap<String, String>();
	
	private HashMap<String, Element> layerEleMap = new HashMap<String, Element>();

	private Connection conn = null;

	private PreparedStatement prep = null;

	private PreparedStatement prep2 = null;

	private HashMap<String, String> groupIDs = new HashMap<String, String>();

	// private

	private SymbolXMLConvertor() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		String destServerAddr = null;
		String destServerPort = null;
		String destServerSID = null;
		String destServerUser = null;
		String destServerPass = null;
		Properties p = new Properties();
        try {
            p.load(new FileInputStream(new File("conf/importer.p")));
            destServerAddr = p.getProperty("destinationDBServerIP");
            destServerPort = p.getProperty("destinationDBServerPort");
            destServerSID = p.getProperty("destinationDBSID");
            destServerUser = p.getProperty("destinationDBUserName");
            destServerPass = p.getProperty("destinationDBUserPass");
            singleSymbolXML = p.getProperty("singleSymbolXML");
            hnSymbolXML= p.getProperty("hnSymbolXML");
            newSymbolSVG = p.getProperty("newSymbolSVG");
            convertedSymbolDir = p.getProperty("convertedSymbolDir");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		conn = DriverManager.getConnection(

				"jdbc:oracle:thin:@"+destServerAddr+":"+destServerPort+":"+destServerSID, destServerUser,
//				"jdbc:oracle:thin:@127.0.0.1:1521:qil", "svgtool",
				destServerPass);

		conn.setAutoCommit(false);
		prep = conn
				.prepareStatement("insert into svg_symbol_fileinfo (SYMBOLID,SYMBOLNAME,VARIETYID,SYMBOLCONTENT,STATUS,GROUPID,VALID) values (?,?,?,?,?,?,?)");
		prep2 = conn
				.prepareStatement("SELECT * FROM svg_symbol_fileinfo WHERE SYMBOLID=?");
	}

	public Document readSingleSymbolXMLDoc()
			throws ParserConfigurationException, SAXException, IOException {
		code_data.put("刀闸", "5");
		code_data.put("电压互感器", "6");
		code_data.put("断路器", "7");
		code_data.put("发电机", "8");
		code_data.put("两圈变压器", "9");
		code_data.put("容抗", "10");
		code_data.put("三圈变压器", "11");
		code_data.put("自定义图元", "12");
		code_data.put("站所", "13");
		code_data.put("线路", "14");
		code_data.put("电抗", "15");
		code_data.put("Disconnector", "刀闸");
		code_data.put("Breaker", "断路器");
		code_data.put("Terminal", "自定义图元");
		code_data.put("Transformer2", "两圈变压器");
		code_data.put("Transformer3", "三圈变压器");
		code_data.put("PT", "电压互感器");
		code_data.put("Compensator", "电抗");
		code_data.put("TXJMachine", "调相机");
		code_data.put("SynchronousMachine", "发电机");
		code_data.put("GroundDisconnector", "刀闸");
		//嘉兴版本的symbol
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(singleSymbolXML));
		try {
			NodeList layers = Utilities.findNodes("//*[@layer]", doc.getDocumentElement());
			for(int i=0;i<layers.getLength();i++){
				Element e = (Element)layers.item(i);
				String content = e.getAttribute("content");
//				String layer = e.getAttribute("layer");
				layerEleMap.put(content, e);
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.addHMSymbols(doc);
		//将转成的symbol（包括嘉兴和海宁的)打印到文件
		XMLPrinter.printXML(doc, new File(newSymbolSVG), null);
		return doc;

	}
	
	/**
	 * 增加海宁图元
	 * @param oriSymboldoc 增加海宁symbol前的symbol.xml转换过来的document对象
	 */
	private void addHMSymbols(Document oriSymboldoc){
		try {
			Document hndoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(hnSymbolXML));
			
			NodeList hnSymbols = hndoc.getElementsByTagName("symbol");
			for(int i=0;i<hnSymbols.getLength();i++){
				Element symbolEle = (Element)hnSymbols.item(i);
				Element descEle = (Element) symbolEle.getElementsByTagName("desc").item(0);
				if(descEle==null){
					System.err.println("null descEle");
					Utilities.printNode(symbolEle, true);
					continue;
				}
				String content =descEle.getAttribute("content");
				String type = code_data.get(content.substring(0,content.indexOf(":")));
				System.out.println("添加到"+type);
				Element contentLayerEle = this.layerEleMap.get(type);
				Node n = oriSymboldoc.importNode(symbolEle, true);
				contentLayerEle.appendChild(n);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void convert(Document singleXMLDoc) throws Exception {
		System.out.println("删除已有图元...");
		conn.createStatement().executeUpdate("delete from SVG_SYMBOL_FILEINFO");
		Element root = singleXMLDoc.getDocumentElement();
		NodeList equipList = root.getChildNodes();
		for (int i = 0; i < equipList.getLength(); i++) {
			Node child = equipList.item(i);
			NamedNodeMap map = child.getAttributes();
			if (map != null)
				if (map.getNamedItem("content") != null) {
					String equipName = map.getNamedItem("content")
							.getNodeValue();
					String layerName = map.getNamedItem("layer").getNodeValue();
					File equipDir = new File(convertedSymbolDir + equipName);
					equipDir.mkdirs();
					convertSymbol(child, convertedSymbolDir + equipName,
							layerName, equipName);
				}
		}
		conn.commit();
		prep.close();
		conn.close();
		prep2.close();
	}

	private void convertSymbol(Node equipNode, String parentDir,
			String layerName, String equipName) throws Exception {
		NodeList symbols = equipNode.getChildNodes();
		for (int i = 0; i < symbols.getLength(); i++) {
			Node symbol = symbols.item(i);
			if (symbol != null && symbol.getNodeName().equals("symbol")) {
				String viewBox = symbol.getAttributes()
				.getNamedItem("viewBox")==null?"0 0 64 64":symbol.getAttributes()
						.getNamedItem("viewBox").getNodeValue();			
				Document doc = createSvgDocument(equipName, layerName, symbol,
						symbol.getAttributes().getNamedItem("id")
								.getNodeValue(), viewBox, 1);

				File symboFile = new File(parentDir
						+ "/"
						+ symbol.getAttributes().getNamedItem("id")
								.getNodeValue() + ".svg");

				// String s = guManager.printNode(doc, null);
				// symboFile.createNewFile();
				// FileOutputStream fos = new FileOutputStream(symboFile);
				// fos.write(s.getBytes());
				// fos.close();

				XMLPrinter.printXML(doc, symboFile, null);
			}
		}
	}

	private Document createSvgDocument(String equipName, String layerName,
			Node symbolNode, String titleName, String viewBox, int type) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		// 这里的namespace值必须是svgNS，否则创建出来的Document对象不会是SVGDocument
		Document doc = impl.createDocument(svgNS, "svg", null);

		Element root = doc.getDocumentElement();
		root.setAttribute("xmlns:PSR", "http://www.cim.com");
		if (viewBox == null) {
			viewBox = "0 0 35 35";
		}
		String[] viewBoxes = viewBox.split(" ");
		ArrayList<String> realViewBox = new ArrayList<String>();
		for (int i = 0; i < viewBoxes.length; i++) {
			if (!viewBoxes[i].trim().equals("")) {
				realViewBox.add(viewBoxes[i].trim());
			}
		}

		root.setAttribute("viewBox", viewBox);
		root.setAttribute("width", realViewBox.get(2));
		root.setAttribute("height", realViewBox.get(3));
		Element title = doc.createElement("title");
		title.setAttribute("name", titleName);
		root.appendChild(title);
		Element styleEle = doc.createElement("style");
		styleEle.setAttribute("type", "text/css");
		styleEle.setAttribute("xml:space", "preserve");
		CDATASection CDATA = doc.createCDATASection(".fillnone{stroke:"+Constants.NCI_DEFAULT_STROKE_COLOR+";fill:none}\n.fill{stroke:"+Constants.NCI_DEFAULT_STROKE_COLOR+";fill:"+Constants.NCI_DEFAULT_GRAPHUNIT_FILL+"}");
		styleEle.appendChild(CDATA);
		Element defsEle = doc.createElement("defs");
		defsEle.appendChild(styleEle);
		root.appendChild(defsEle);
		
		symbolNode = doc.importNode(symbolNode, true);
		new GraphUnitImageShape(null).addSymbol(doc, defsEle, (Element)symbolNode, viewBox, titleName);
		
//		NodeList childrenSymbol = symbolNode.getChildNodes();
//		for (int n = 0; n < childrenSymbol.getLength(); n++) {
//			try {
//				Node generalNode = childrenSymbol.item(n);
//				if (generalNode instanceof Element) {
//					Node node = null;
//					node = doc.importNode(generalNode, true);
//					// 需要通过parseNodeToSVGNode方法加上ElementNS。否则无法显示，参见：http://xmlgraphics.apache.org/batik/using/dom-api.html
////					node = parseNodeToSVGNode(doc, node);
//					
//					root.appendChild(node);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.out.println("createSvgDocument exception happeded: "
//						+ e.getMessage());
//			}
//		}
		NodeList shapes = null;
		symbolNode  = doc.getElementsByTagName("symbol").item(0);
		if (((Element)symbolNode).getElementsByTagName("g").item(0) != null) {
			shapes = ((Element)symbolNode).getElementsByTagName("g").item(0).getChildNodes();
		} else {
			shapes = ((Element)symbolNode).getChildNodes();
		}
		Element useEle = doc.createElement("use");
		root.appendChild(useEle);
		useEle.setAttribute("xlink:href", "#"+titleName);
//		Utilities.printNode(root, true);
		for (int i = 0; i < shapes.getLength(); i++) {
//			Utilities.printNode(shapes.item(i), true);
			if(shapes.item(i) instanceof Element == false){
				continue;
			}
			Element shape = (Element) shapes.item(i);
			
			if(!EditorToolkit.isElementAnActualShape(shape)){
				continue;
			}
			if(shape.getAttribute("style").indexOf("fill:#")>=0 || shape.getAttribute("style").indexOf("fill")==-1){
				useEle.setAttribute("class", "fill");
			}else{
				useEle.setAttribute("class", "fillnone");
			}
//			shape.removeAttributeNS(null,"style");
			shape.removeAttributeNS(null,"stroke");
//			shape.removeAttributeNS(null,"fill");
//			Utilities.printNode(shape, true);
//			String fill = shape.getAttribute("fill");
//			if ((fill != null && !fill.equals(""))
//					|| shape.getAttribute("style").indexOf("fill") >= 0) {
//				Utilities.printNode(shape, true);
//			} else {
//				shape.setAttribute("fill", Constants.NCI_DEFAULT_GRAPHUNIT_FILL);
////				EditorToolkit.setStyleProperty(shape, "fill",
////						Constants.NCI_DEFAULT_GRAPHUNIT_FILL);
//			}
		}
		
//		Utilities.printNode(root, true);
		Element desc = doc.createElement("desc");
		desc.setAttribute("content", layerName);
		symbolNode.appendChild(desc);
//		Element metadataEle = doc.createElement("metadata");
//		symbolNode.appendChild(metadataEle);
//		Element psr_cimclassEle = doc.createElementNS("http://www.cim.com",
//				"PSR:CimClass");
//		psr_cimclassEle.setAttribute("CimType", layerName);
//		metadataEle.appendChild(psr_cimclassEle);
//		Element psr_objref = doc.createElementNS("http://www.cim.com",
//				"PSR:ObjRef");
//		psr_objref.setAttribute("Code", "");
//		psr_objref.setAttribute("FieldNo", "");
//		psr_objref.setAttribute("ObjectID", String.valueOf(symbolid));
//
//		metadataEle.appendChild(psr_objref);
		try {
			storeToDB(doc, String.valueOf(symbolid), titleName, equipName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		symbolid++;
		return doc;
	}

	private Node parseNodeToSVGNode(Document doc, Node generalNode) {
		// 这里的namespace值必须是svgNS
		Element svgElement = doc.createElementNS(svgNS, generalNode
				.getNodeName());
		NamedNodeMap namedNodeMap = generalNode.getAttributes();
		if (namedNodeMap != null) {
			for (int i = 0; i < namedNodeMap.getLength(); i++) {
				/** servlet中传递过来的图元不一定有style属性，即使有属性，其stroke也未必定义，所以这里全部加上* */
				if (namedNodeMap.item(i).getNodeName().equals("style")) {
					// svgElement.setAttribute(namedNodeMap.item(i).getNodeName(),
					// "stroke:#101000;");
//					if (namedNodeMap.item(i) instanceof Element)
//						EditorToolkit.setStyleProperty((Element) namedNodeMap
//								.item(i), "stroke", "#000000");
				} else {
					svgElement.setAttribute(namedNodeMap.item(i).getNodeName(),
							namedNodeMap.item(i).getNodeValue());
				}

			}
			// 没有style属性，则添加
			if (EditorToolkit.isElementAnActualShape(svgElement)) {
//				Utilities.printNode(svgElement, true);
				if (svgElement.getAttribute("style") == null
						|| svgElement.getAttribute("style").equals("")) {
//					EditorToolkit.setStyleProperty(svgElement, "stroke",
//							"#000000");
				}
			}
			if (generalNode.hasChildNodes()) {
				NodeList children = generalNode.getChildNodes();
				for (int n = 0; n < children.getLength(); n++) {
					if (children.item(n) instanceof Element) {
						Node child = parseNodeToSVGNode(doc, children.item(n));
						svgElement.appendChild(child);
					}
				}
			}
		}
		return svgElement;
	}

	int groupID_index = 200;

	public void storeToDB(Document doc, String symbolID, String symbolName,
			String equipName) throws Exception {
		prep.setString(1, symbolID);
		prep.setString(2, symbolName);
		// prep.setString(3,symbolName+".svg");
		String status = "0";
		if (symbolName.lastIndexOf("_0") == symbolName.length() - 2
				|| symbolName.lastIndexOf("_1") == symbolName.length() - 2) {
			if (symbolName.lastIndexOf("_0") == symbolName.length() - 2) {
				status = "1";
			} else {
				status = "2";
				// symbolName = symbolName.substring(0,
				// symbolName.length()-2)+"(关)";
			}
			symbolName = symbolName.substring(0, symbolName.length() - 2);
			groupIDs.put(symbolName, String.valueOf(groupID_index));
			groupID_index++;
		}
		prep.setString(3, code_data.get(equipName));
		prep.setBlob(4, oracle.sql.BLOB.empty_lob());
		prep.setString(5, status);// status
		prep.setString(6, groupIDs.get(symbolName) == null ? "0" : groupIDs
				.get(symbolName));// groupid
		prep.setString(7, "1");// valid
		prep.executeUpdate();// 先insert 再update blob

		// Statement stmt = conn2.createStatement();
		prep2.setString(1, symbolID);
		ResultSet rs = prep2.executeQuery();
		String docStr = "";
		while (rs.next()) {
			BLOB blob = (BLOB) rs.getBlob("SYMBOLCONTENT");
			OutputStream out = blob.getBinaryOutputStream(); // 建立输出流
			docStr = Utilities.printNode(doc.getDocumentElement(), false);
			out.write(docStr.getBytes("utf-8"));
			out.close();
		}
		rs.close();
		System.out.println("完成symbol入库：" + symbolName);
	}

	long symbolid = 1219119233598l;

	public static void main(String[] args) throws Exception {
		int i = JOptionPane.showConfirmDialog(null, "不是svg图元相关操作人员不要运行此程序，点击\"取消\"退出!\r\n该操作将删除库中的已有图元，开始操作前请检查conf/importer.p中配置信息是否正确.\r\n确认继续？",
				"危险", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if(i!=0)
			return;
		SymbolXMLConvertor convertor = new SymbolXMLConvertor();
		Document xmlDoc = convertor.readSingleSymbolXMLDoc();
		convertor.convert(xmlDoc);
//		String s = "xxx:ttt";
//		System.out.println(s.substring(0, s.indexOf(":")));
	}
	

}
