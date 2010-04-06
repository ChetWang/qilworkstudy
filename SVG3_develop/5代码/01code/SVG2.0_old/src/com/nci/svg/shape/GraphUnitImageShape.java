package com.nci.svg.shape;

import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.dom.svg.SVGOMDocument;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.other.ComputeID;
import com.nci.svg.util.Constants;
import com.nci.svg.util.EquipPool;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.Toolkit;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.shape.RectangularShape;

/**
 * NCI 设备图元对象类
 * 
 * @author Qil.Wong
 * 
 */
public class GraphUnitImageShape extends RectangularShape {

	/**
	 * the element attributes names
	 */
	protected static String hrefAtt = "xlink:href",
			preserveAtt = "preserveAspectRatio";
	//
	// /**
	// * 设备符合路径，相对形式
	// */
	// // public static String NCI_GraphUnit_EquipType = "equipType";
	// public static String NCI_GraphUnit_SymbolName = "symbolName";

	public static String NCI_GraphUnit_IDENTIFIER_NAME = "graphUnitIdentifier";

	private String equipTypeName = null;

	private String symbolName = null;

	// private String equipSymbolPath = null;

	public GraphUnitImageShape(Editor editor) {
		super(editor);
		// Rectangle2D r2d = new Rectangle2D();
		handledElementTagName = "image";
	}

	@Override
	public int getLevelCount() {

		return 2;
	}

	private String getGraphUnitImageFile(SVGHandle handle) {
		return new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + equipTypeName
				+ "/" + symbolName + Constants.NCI_SVG_EXTENDSION).toURI()
				.toString();
	}

	private void initEquipSymbolPath() {
		equipTypeName = editor.getOutlookPane().getSelectedButtonTitle();
		symbolName = editor.getSymbolSession().getSelectedThumbnail().getText();

	}

	/**
	 * 
	 * @param doc
	 * @param defs
	 * @param sElement
	 * @param viewBox
	 * @param strID
	 * @deprecated
	 * @return
	 */
	public Element addSymbol(Document doc, Element defs, Element sElement,
			String viewBox, String strID) {
		Element element = null;
		element = doc.createElementNS(doc.getDocumentElement()
				.getNamespaceURI(), "symbol");
		element.setAttribute("viewBox", viewBox);
		element.setAttribute("id", strID);
		element.setAttribute("preserveAspectRatio", "xMidYMid meet");
		defs.appendChild(element);
		NodeList list = null;
		if (!sElement.getOwnerDocument().equals(doc)) {
			Element dElement = (Element) doc.importNode(sElement, true);
			list = dElement.getChildNodes();
		} else {
			list = sElement.getChildNodes();
		}
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element
					&& !list.item(i).getNodeName().equals("metadata")) {
				element.appendChild(list.item(i));
			}
		}

		return element;
	}

	public Element addSymbolElement(Document doc, Element defs,
			Element sElement, String viewBox, String strID) {
		Element element = null;
		// element = doc.createElementNS(doc.getDocumentElement()
		// .getNamespaceURI(), "symbol");
		// element.setAttribute("viewBox", viewBox);
		// element.setAttribute("id", strID);
		// element.setAttribute("preserveAspectRatio", "xMidYMid meet");
		element = (Element) doc.importNode(sElement.getElementsByTagName(
				"symbol").item(0), true);
		editor.getSvgSession().iteratorUseElementInserting(doc, sElement);
//		Utilities.printNode(element, true);
//		Utilities.printNode(doc.getDocumentElement(), true);
		
		// Utilities.printNode(doc.getDocumentElement(), true);
		// NodeList list = null;
		// if (!sElement.getOwnerDocument().equals(doc)) {
		// Element dElement = (Element) doc.importNode(sElement, true);
		// list = dElement.getChildNodes();
		// } else {
		// list = sElement.getChildNodes();
		// }
		// for (int i = 0; i < list.getLength(); i++) {
		// if (list.item(i) instanceof Element
		// && !list.item(i).getNodeName().equals("metadata")) {
		// element.appendChild(list.item(i));
		// }
		// }
//		Utilities.printNode(doc.getDocumentElement(), true);
		return element;
	}

	public Element createElementAsUse(SVGHandle handle, Rectangle2D bounds,
			Document doc, Document symboldoc) {
		String classStyle = "";
		String layerName = "";
		try {
			symboldoc.getDocumentElement().getChildNodes();
			classStyle = Utilities.findOneAttributeValue("class",
					"//*[@class]", symboldoc.getDocumentElement());
			layerName = Utilities.findOneAttributeValue("content",
					"//*[@content]", symboldoc.getDocumentElement());
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Element gelement = null;
		Element element = null;
		Element title = (Element) symboldoc.getElementsByTagName("title").item(
				0);
		String symbolname = title.getAttribute("name");

		String strSymbolViewBox = symboldoc.getDocumentElement().getAttribute(
				"viewBox");
//		Utilities.printNode(doc.getDocumentElement(), true);
//		Utilities.printNode(symboldoc.getDocumentElement(), true);
		if (symbolname != null && symbolname.length() > 0) {
			// 执行复制
			Element defs = null;
			if (doc.getElementsByTagName("defs").getLength() == 0) {
				defs = doc.createElementNS(doc.getDocumentElement()
						.getNamespaceURI(), "defs");
				doc.getDocumentElement().appendChild(defs);
			} else
				defs = (Element) doc.getElementsByTagName("defs").item(0);

			Element solidifiedSymbol = null;
//			Utilities.printNode(doc.getDocumentElement(), true);
			try {
				solidifiedSymbol = (Element) Utilities.findNode(
						"*/symbol[@id='" + symbolname + "']", defs);
				if (solidifiedSymbol == null) {
					solidifiedSymbol = addSymbolElement(doc, defs, symboldoc
							.getDocumentElement(), strSymbolViewBox, symbolname);
				}
//				Utilities.printNode(doc.getDocumentElement(), true);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				solidifiedSymbol = null;
			}

			Element sroot = (Element) symboldoc.getElementsByTagName("symbol")
					.item(0);
			// Element terminal = (Element)
			// sroot.getElementsByTagName("terminal")
			// .item(0);
			String strViewBox = sroot.getAttribute("viewBox");
			String[] strTemp = strViewBox.split(" ");
			// if ((terminal != null))
			// terminal = (Element) doc.importNode(terminal, true);

			// normalizing the bounds of the element
			if (bounds.getWidth() < 1) {

				bounds.setRect(bounds.getX(), bounds.getY(), 1, bounds
						.getHeight());
			}

			if (bounds.getHeight() < 1) {

				bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth(),
						1);
			}

			// creating the rectangle
			gelement = ((SVGOMDocument) doc).createElementNS(doc
					.getDocumentElement().getNamespaceURI(), "g");
			// gelement.setAttribute("type", "solidified");
			gelement.setAttribute("id", ComputeID.getSymbolID());
			element = ((SVGOMDocument) doc).createElementNS(doc
					.getDocumentElement().getNamespaceURI(), "use");
			element.setAttribute("class", classStyle);
			// element = doc.createElementNS(doc.getDocumentElement()
			// .getNamespaceURI(), "use");
			gelement.appendChild(element);

			// EditorToolkit.setAttributeValue(element, xAtt, bounds.getX());
			// EditorToolkit.setAttributeValue(element, yAtt, bounds.getY());
			// EditorToolkit.setAttributeValue(element, wAtt,
			// bounds.getWidth());
			// EditorToolkit.setAttributeValue(element, hAtt,
			// bounds.getHeight());

			element.setAttribute("filterUnits", "userSpaceOnUse");
			EditorToolkit.setAttributeValue(element, xAtt, bounds.getX());
			EditorToolkit.setAttributeValue(element, yAtt, bounds.getY());
			EditorToolkit.setAttributeValue(element, wAtt, bounds.getWidth());
			EditorToolkit.setAttributeValue(element, hAtt, bounds.getHeight());
			element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href",
					"#" + symbolname);
			// element.setAttributeNS("", "type", "solidified");
			element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:show",
					"embed");
			element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:type",
					"simple");
			element.setAttributeNS(null, preserveAtt, "none meet");
			element.setAttributeNS(null, "sw", strTemp[2]);
			element.setAttributeNS(null, "sh", strTemp[3]);
			// metadata

			Element metadataEle = doc.createElement("metadata");
			element.appendChild(metadataEle);
			Element psr_cimclassEle = doc.createElementNS("http://www.cim.com",
					"PSR:CimClass");
			psr_cimclassEle.setAttribute("CimType", layerName);
			metadataEle.appendChild(psr_cimclassEle);
			Element psr_objref = doc.createElementNS("http://www.cim.com",
					"PSR:ObjRef");
			psr_objref.setAttribute("Code", "");
			psr_objref.setAttribute("FieldNo", "");
			psr_objref.setAttribute("ObjectID", "");

			metadataEle.appendChild(psr_objref);

			metadataEle.appendChild(psr_objref);

//			Node metadata = symboldoc.getElementsByTagName("metadata").item(0);
//			Node metadataNew = doc.importNode(metadata, true);
//			gelement.appendChild(metadataNew);
//			// 这个一定要加上，否则metadata无法解析
//			doc.getDocumentElement().setAttributeNS(null,"xmlns:PSR",
//			"http://www.cim.com");
			Toolkit.clearBatikImageCache();
			handle.getCanvas().clearCache();

			// inserting the element in the document and handling the undo/redo
			// support
			insertShapeElement(handle, gelement);

		}
//		Utilities.printNode(doc.getDocumentElement(), true);
		editor.getSvgSession().refreshCurrentHandleLater();
		return gelement;
	}

	@Override
	public Element createElement(SVGHandle handle, Rectangle2D bounds) {

		// the element
		Element element = null;

		initEquipSymbolPath();

		// getting the image file path for the element
		String imagePath = getGraphUnitImageFile(handle);
		// String imagePath = "";

		if (imagePath != null && !imagePath.equals("")) {

			// the edited document
			Document doc = handle.getScrollPane().getSVGCanvas().getDocument();

			/*
			 * add by yuxiang 读取图元原尺寸及metadata连接点数据，保存至设备图中
			 */
			Document symboldoc = editor.getSymbolSession()
					.getSelectedThumbnail().getDocument();

			// add by yuxiang
			// 为了支持部分符合修改
			// if (symboldoc.getElementsByTagName("desc").getLength() == 1) {
			// Element desc = (Element) symboldoc.getElementsByTagName("desc")
			// .item(0);
			// String useflag = desc.getAttribute("useflag");
			// if (useflag != null && useflag.equals("1")) {
			// return createElementAsUse(handle, bounds, doc, symboldoc);
			// }
			// }
			// end add

			// added by wangql,为将拖进去的图元全部转成use
			element = createElementAsUse(handle, bounds, doc, symboldoc);
			// wangql adding ended

			// 以下全部注释掉，是为了直接转成use，如果要换回image，则取消注释，并注释上一行“return
			// createElementAsUse(handle, bounds, doc, symboldoc);”
			// NCIEquipSymbolBean bean = editor.getSymbolSession()
			// .getSelectedThumbnail().getSymbolBean();
			// Element sroot = (Element) symboldoc.getElementsByTagName("svg")
			// .item(0);
			// // Element terminal = (Element)
			// // sroot.getElementsByTagName("terminal")
			// // .item(0);
			// String strViewBox = sroot.getAttribute("viewBox");
			// String[] strTemp = strViewBox.split(" ");
			// // if ((terminal != null))
			// // terminal = (Element) doc.importNode(terminal, true);
			//
			// // normalizing the bounds of the element
			// if (bounds.getWidth() < 1) {
			//
			// bounds.setRect(bounds.getX(), bounds.getY(), 1, bounds
			// .getHeight());
			// }
			//
			// if (bounds.getHeight() < 1) {
			//
			// bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth(),
			// 1);
			// }
			//
			// // creating the rectangle
			//
			// element = doc.createElementNS(doc.getDocumentElement()
			// .getNamespaceURI(), handledElementTagName);
			//
			// EditorToolkit.setAttributeValue(element, xAtt, bounds.getX());
			// EditorToolkit.setAttributeValue(element, yAtt, bounds.getY());
			// EditorToolkit.setAttributeValue(element, wAtt,
			// bounds.getWidth());
			// EditorToolkit.setAttributeValue(element, hAtt,
			// bounds.getHeight());
			//
			// element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href",
			// imagePath);
			// element.setAttributeNS(null, preserveAtt, "none meet");
			// // 为image增加设备类型、namespace等属性
			// element.setAttributeNS(null, Constants.NCI_SVG_Type_Attr,
			// Constants.NCI_SVG_Type_GraphUnit_Value);
			// // 表明这个设备是新建的设备
			// element.setAttributeNS(null, Constants.NCI_SVG_Status,
			// Constants.NCI_SVG_Status_New);
			// try {
			// element.setAttributeNS(null, Constants.NCI_SVG_EquipType_Name,
			// Utilities.findOneAttributeValue("value",
			// "/EquipProperties/Properties[@equipType='"
			// + symbolName + "']", EquipPool
			// .getEquipPropertiesDoc()));
			// } catch (DOMException e) {
			// e.printStackTrace();
			// } catch (XPathExpressionException e) {
			// e.printStackTrace();
			// }
			// element.setAttributeNS(null, NCI_GraphUnit_IDENTIFIER_NAME, bean
			// .getGraphUnitID());
			// element.setAttributeNS(null, "sw", strTemp[2]);
			// element.setAttributeNS(null, "sh", strTemp[3]);
			// Toolkit.clearBatikImageCache();
			// handle.getCanvas().clearCache();
			// // if (terminal != null)
			// // element.appendChild(terminal);
			// ComputeID cID = new ComputeID();
			// String strSymbolID = cID.getSymbolID();
			// element.setAttribute("id", strSymbolID);
			//
			// // inserting the element in the document and handling the
			// undo/redo
			// // support
			// insertShapeElement(handle, element);
		}

		return element;

	}

	// demo用，需要删除，
	public Element createDangerousPoint(SVGHandle handle, Rectangle2D bounds) {

		// the element
		Element element = null;

		equipTypeName = "系统图元";
		symbolName = "缺陷点";

		// getting the image file path for the element
		String imagePath = getGraphUnitImageFile(handle);
		// String imagePath = "";

		if (imagePath != null && !imagePath.equals("")) {

			// the edited document
			Document doc = handle.getScrollPane().getSVGCanvas().getDocument();

			/*
			 * add by yuxiang 读取图元原尺寸及metadata连接点数据，保存至设备图中
			 */

			// creating the rectangle
			element = doc.createElementNS(doc.getDocumentElement()
					.getNamespaceURI(), handledElementTagName);

			EditorToolkit.setAttributeValue(element, xAtt, bounds.getX());
			EditorToolkit.setAttributeValue(element, yAtt, bounds.getY());
			EditorToolkit.setAttributeValue(element, wAtt, bounds.getWidth());
			EditorToolkit.setAttributeValue(element, hAtt, bounds.getHeight());

			element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href",
					imagePath);
			element.setAttributeNS(null, preserveAtt, "none meet");
			// 为image增加设备类型、namespace等属性
			element.setAttributeNS(null, Constants.NCI_SVG_Type_Attr,
					Constants.NCI_SVG_Type_GraphUnit_Value);
			// 表明这个设备是新建的设备
			element.setAttributeNS(null, Constants.NCI_SVG_Status,
					Constants.NCI_SVG_Status_New);
			try {
				element.setAttributeNS(null, Constants.NCI_SVG_EquipType_Name,
						Utilities.findOneAttributeValue("value",
								"/EquipProperties/Properties[@equipType='"
										+ symbolName + "']", EquipPool
										.getEquipPropertiesDoc()));
			} catch (DOMException e) {
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			element.setAttributeNS(null, NCI_GraphUnit_IDENTIFIER_NAME,
					"系统图元/缺陷点");
			element.setAttributeNS(null, "sw", "64");
			element.setAttributeNS(null, "sh", "64");
			Toolkit.clearBatikImageCache();
			handle.getCanvas().clearCache();
			// if (terminal != null)
			// element.appendChild(terminal);
			ComputeID cID = new ComputeID();
			String strSymbolID = cID.getSymbolID();
			element.setAttribute("id", strSymbolID);

			// inserting the element in the document and handling the undo/redo
			// support
			insertShapeElement(handle, element);
		}

		return element;

	}

}
