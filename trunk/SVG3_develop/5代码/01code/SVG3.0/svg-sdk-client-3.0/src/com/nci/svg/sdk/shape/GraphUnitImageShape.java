package com.nci.svg.sdk.shape;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.svg.SVGOMEllipseElement;
import org.apache.batik.dom.svg.SVGOMPathElement;
import org.apache.batik.dom.svg.SVGOMTextElement;
import org.apache.batik.dom.svg.SVGOMUseElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.bean.SimpleCodeBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.logger.LoggerAdapter;

import fr.itris.glips.library.Toolkit;
import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.shape.RectangularShape;

/**
 * NCI �豸ͼԪ������
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

	public static String NCI_GraphUnit_IDENTIFIER_NAME = "graphUnitIdentifier";

	// private String equipTypeName = null;

	private String symbolName = null;

	public static final String MODULE_ID = "2cdbf693-cb01-4195-a021-d094f1f20649";

	// private String equipSymbolPath = null;

	public GraphUnitImageShape(EditorAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
		handledElementTagName = "image";
	}

	@Override
	public int getLevelCount() {

		return 2;
	}

	// private String getGraphUnitImageFile(SVGHandle handle) {
	// return new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + equipTypeName
	// + "/" + symbolName + Constants.NCI_SVG_EXTENDSION).toURI()
	// .toString();
	// }

	private void initEquipSymbolPath() {
		symbolName = editor.getSymbolSession().getSelectedThumbnail().getText();
		// equipTypeName = editor.getSymbolSession().getSelectedThumbnail()
		// .getSymbolBean().getVariety().getCode();
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

	public void addSymbolElement(Document doc, Element defs, Element sElement,
			String viewBox, String strID) {
		// Element element = null;
		// element = (Element) doc.importNode(sElement.getElementsByTagName(
		// "symbol").item(0), true);
		editor.getSvgSession().iteratorUseElementInserting(doc, sElement);
		// Utilities.printNode(sElement, true);
		// return element;
	}

	/**
	 * ͼԪ��������£������������ͼԪ, ��ģ�����������
	 * 
	 * @param handle
	 * @return
	 */
	private boolean checkGraphUnit(SVGHandle handle) {
		if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL) {
			JOptionPane.showConfirmDialog(handle.getCanvas(),
					ResourcesManager.bundle
							.getString("nci_graphunit_no_graphunit_inside"),
					ResourcesManager.bundle
							.getString("nci_optionpane_infomation_title"),
					JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			editor.getSelectionManager().setToRegularMode();
			return false;
		}
		return true;
	}

	/**
	 * ���ģ��Ƕ��
	 * 
	 * @param handle
	 * @return
	 */
	private boolean checkTemplateNesting(SVGHandle handle) {
		if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE) {
			// �жϵ�ǰ���õ�ģ����û�а�������template�Լ���UUID
			String currentTemplateID = "";
			String usedTemplateID = "";
			try {
				Node currentTemplateNode = Utilities.findNode("//*[@"
						+ Constants.SYMBOL_TYPE + "]", handle.getCanvas()
						.getDocument().getDocumentElement());
				if (currentTemplateNode != null) {
					currentTemplateID = ((Element) currentTemplateNode)
							.getAttributeNS(null, "id");
				}
				Node usedTemplateNode = Utilities.findNode("//*[@"
						+ Constants.SYMBOL_TYPE + "]", editor
						.getSymbolSession().getSelectedThumbnail()
						.getDocument().getDocumentElement());
				if (usedTemplateNode != null) {
					usedTemplateID = ((Element) usedTemplateNode)
							.getAttributeNS(null, "id");
				}
				if (currentTemplateID.equals(usedTemplateID)) {
					JOptionPane
							.showConfirmDialog(
									handle.getCanvas(),
									"ģ��䲻���໥Ƕ�ף�",
									ResourcesManager.bundle
											.getString("nci_optionpane_infomation_title"),
									JOptionPane.CLOSED_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
					editor.getSelectionManager().setToRegularMode();
					return false;
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public void switchStatus(Element groupEle, String statusSelected) {
		// ��ͨͼԪ������ֻ��ͼԪ�ſ�����״̬��

	}

	@Override
	public Element createElement(SVGHandle handle, Rectangle2D bounds) {

		// the element
		Element element = null;

		if (!checkGraphUnit(handle)) {
			return element;
		}
		if (!checkTemplateNesting(handle)) {
			return element;
		}

		initEquipSymbolPath();

		// getting the image file path for the element
		// String imagePath = getGraphUnitImageFile(handle);

		// if (imagePath != null && !imagePath.equals("")) {

		Document doc = handle.getScrollPane().getSVGCanvas().getDocument();

		Document symboldoc = editor.getSymbolSession().getSelectedThumbnail()
				.getDocument();

		Element desc = (Element) symboldoc.getElementsByTagName("desc").item(0);
		if (desc == null)
			return element;
		NCIEquipSymbolBean bean = editor.getSymbolSession()
				.getSelectedThumbnail().getSymbolBean();

		// ��ȡͼԪ���ͺ�����
		// String type = desc.getAttribute("type");
		String type = bean.getType();
		if (type == null)
			return element;
		// String name = desc.getAttribute("name");
		String name = bean.getName();
		String symbolID = bean.getId();
		SimpleCodeBean variety = bean.getVariety();
		if (type.equals(NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT)) {

			element = createElementAsGraphUnit(handle, bounds, doc, symboldoc,
					name, symbolID, variety.getCode(), bean.getModelID());
		} else if (type.equals(NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			// ģ��
			element = createElementAsTemplate(handle, bounds, doc, symboldoc,
					name, symbolID, variety.getCode(), bean.getModelID());
		}
		if (handle.getAutoAjuster() != null)
			handle.getAutoAjuster().autoAjustWhenCreated(element);
		return element;

	}

	// private double getWHRate(Element hrefEle) {
	// Utilities.printNode(hrefEle, true);
	// double width = 0;
	// double height = 0;
	// NodeList nodes = hrefEle.getChildNodes();
	// for (int i = 0; i < nodes.getLength(); i++) {
	// Element e = (Element) nodes.item(i);
	// if (e.getNodeName().equals("g")) {
	// width = width + getGroupSymbolValue(0, e);
	// height = height + getGroupSymbolValue(1, e);
	// } else {
	// width = width + Double.valueOf(e.getAttribute("width"));
	// height = height + Double.valueOf(e.getAttribute("height"));
	// }
	// }
	// return width / height;
	// }
	//
	// private double getGroupSymbolValue(int flag, Element g) {
	// String cor = flag == 0 ? "x" : "y";
	// String dire = flag == 0 ? "width" : "height";
	// double length = 0;
	// NodeList nodes = g.getChildNodes();
	// for (int i = 0; i < nodes.getLength(); i++) {
	// Element e = (Element) nodes.item(i);
	// double l1 = 0;
	// if (e.getNodeName().equals("g")) {
	// l1 = getGroupSymbolValue(flag, e);
	//
	// } else {
	// l1 = Double.valueOf(e.getAttribute(cor))
	// + Double.valueOf(e.getAttribute(dire));
	// }
	// if (l1 > length)
	// length = l1;
	// }
	// return length;
	// }

	/**
	 * add by yux��2008.12.25 ������ͨͼԪ��Ϣ
	 * 
	 * @param handle
	 *            ��ǰ��svg������
	 * @param bounds
	 *            ͼԪ��Χ
	 * @param doc
	 *            ����SVGDocumentͼ�ζ���
	 * @param symboldoc
	 *            �����Ƶ�ͼԪdocument����
	 * @param name
	 *            :ͼԪ����
	 * @param symbolID
	 *            :ͼԪ��ţ���ȡͼԪҵ��淶��
	 * @param layer
	 *            :ͼ������
	 * @param modelID
	 *            :��Ӧ��ģ�ͱ��
	 * @return ���ɵ�ͼԪElementԪ��
	 */
	private Element createElementAsGraphUnit(SVGHandle handle,
			Rectangle2D bounds, Document doc, Document symboldoc, String name,
			String symbolID, String layer, String modelID) {

		String href = EditorToolkit.addGraphUnitElements(handle, symboldoc,
				name);
		if (href == null)
			return null;
		double rate = EditorToolkit.getUseShapeRate(doc, href);
		Element sroot = symboldoc.getDocumentElement();

		String strViewBox = sroot.getAttribute("viewBox");
		String[] strTemp = strViewBox.split(" ");

		Element element = ((SVGOMDocument) doc).createElementNS(doc
				.getDocumentElement().getNamespaceURI(), "use");

		String id = UUID.randomUUID().toString();
		element.setAttribute("id", id);

		element.setAttribute("filterUnits", "userSpaceOnUse");
		double actualBoundsRate = bounds.getWidth() / bounds.getHeight();
		EditorToolkit.setAttributeValue(element, xAtt, bounds.getX());
		EditorToolkit.setAttributeValue(element, yAtt, bounds.getY());
		if (rate > actualBoundsRate) {
			EditorToolkit.setAttributeValue(element, wAtt, bounds.getWidth());
			EditorToolkit.setAttributeValue(element, hAtt, bounds.getWidth()
					/ rate);
		} else {
			EditorToolkit.setAttributeValue(element, wAtt, bounds.getHeight()
					* rate);
			EditorToolkit.setAttributeValue(element, hAtt, bounds.getHeight());
		}
		element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href", "#"
				+ href);
		element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:show",
				"embed");
		element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:type",
				"simple");
		element.setAttributeNS(null, preserveAtt, "none meet");
		element.setAttributeNS(null, "sw", strTemp[2]);
		element.setAttributeNS(null, "sh", strTemp[3]);

		Toolkit.clearBatikImageCache();
		handle.getCanvas().clearCache();

		insertShapeElement(handle, element);

		// inserting the element in the document and handling the undo/redo
		// support
		// if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE)
		// {
		// insertShapeElement(handle, element);
		// } else if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG) {
		// Element layerElement = ((SVGOMDocument) doc).getElementById(layer
		// + Constants.LAYER_SUFFIX);
		// if (layerElement == null) {
		// layerElement = ((SVGOMDocument) doc).createElementNS(doc
		// .getDocumentElement().getNamespaceURI(), "g");
		// layerElement.setAttribute("id", layer + Constants.LAYER_SUFFIX);
		// doc.getDocumentElement().appendChild(layerElement);
		// handle.getCanvas().appendElement(
		// layer + Constants.LAYER_SUFFIX, layerElement);
		// String layerName = null;
		// ResultBean resultBean = editor.getDataManage().getData(
		// DataManageAdapter.KIND_CODES,
		// CodeConstants.SVG_GRAPHUNIT_VARIETY, layer);
		// if (resultBean != null
		// && resultBean.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
		// layerName = ((CodeInfoBean) resultBean.getReturnObj())
		// .getName();
		// } else {
		// resultBean = editor.getDataManage().getData(
		// DataManageAdapter.KIND_CODES,
		// CodeConstants.SVG_TEMPLATE_VARIETY, layer);
		// if (resultBean != null
		// && resultBean.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
		// layerName = ((CodeInfoBean) resultBean.getReturnObj())
		// .getName();
		// }
		// }
		// handle.getEditor().getPropertyModelInteractor().getGraphModel()
		// .addTreeNode(doc.getDocumentElement(), layerName,
		// layerElement);
		// }
		// insertShapeElement(handle, layerElement, element, name);
		// insertShapeElement(handle, element);
		// }

		// RemoteUtilities.appendMetadataElement(editor, handle.getCanvas()
		// .getFileType(), NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT_CODE,
		// symbolID, element);
		EditorToolkit.appendMetadataElement(handle, modelID, element);
		editor.getSvgSession().refreshHandle(element);
		editor.getSvgSession().refreshCurrentHandleImediately();
		return element;
	}

	/**
	 * add by yux��2008.12.25 ����ģ��ͼԪ
	 * 
	 * @param handle
	 *            ��ǰ��svg������
	 * @param bounds
	 *            ͼԪ��Χ
	 * @param doc
	 *            ����SVGDocumentͼ�ζ���
	 * @param symboldoc
	 *            �����Ƶ�ͼԪdocument����
	 * @param name
	 *            :ͼԪ����
	 * @param symbolID
	 *            :ͼԪ���
	 * @param layer
	 *            :ͼ������
	 * @param modelID
	 *            :��Ӧ��ģ�ͱ��
	 * @return ���ɵ�ͼԪElementԪ��
	 */
	private Element createElementAsTemplate(SVGHandle handle,
			Rectangle2D bounds, Document doc, Document symboldoc, String name,
			String symbolID, String layer, String modelID) {
		Element gElement = ((SVGOMDocument) doc).createElementNS(doc
				.getDocumentElement().getNamespaceURI(), "g");
		// gElement.setAttribute("id", UUID.randomUUID().toString());
		gElement.setAttribute(Constants.SYMBOL_TYPE,
				NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE);
		gElement.setAttribute(Constants.SYMBOL_ID, name);
		// doc.getDocumentElement().appendChild(gElement);
		// modified by yux @2009.01.05
		// ���ӵ���Ӧ��ͼ����
		if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE) {
			insertShapeElement(handle, gElement);
		} else if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG) {
			Element layerElement = ((SVGOMDocument) doc).getElementById(layer
					+ Constants.LAYER_SUFFIX);
			if (layerElement == null) {
				layerElement = ((SVGOMDocument) doc).createElementNS(doc
						.getDocumentElement().getNamespaceURI(), "g");
				layerElement.setAttribute("id", layer + Constants.LAYER_SUFFIX);
				doc.getDocumentElement().appendChild(layerElement);
				handle.getCanvas().appendElement(
						layer + Constants.LAYER_SUFFIX, layerElement);
			}
			insertShapeElement(handle, layerElement, gElement);
		}
		int x = (int) bounds.getMinX();
		int y = (int) bounds.getMinY();
		NodeList list = null;

		list = symboldoc.getDocumentElement().getElementsByTagName("use");
		if (list != null && list.getLength() > 0) {

			ArrayList<String> array = new ArrayList<String>();
			for (int i = 0; i < list.getLength(); i++) {
				SVGOMUseElement e = (SVGOMUseElement) (list.item(i));

				String href = e.getHref().getBaseVal();
				if (href.equals("#terminal"))
					continue;
				int index = href.indexOf(Constants.SYMBOL_STATUS_SEP);
				if (index > -1) {
					href = href.substring(1, index);
				} else
					href = href.substring(1);
				if (!array.contains(href)) {
					array.add(href);
				}
			}
			EditorToolkit.insertCurSymbolByName(handle, array);
		}
		NodeList childList = symboldoc.getDocumentElement().getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			if (childList.item(i) instanceof Element
					&& !childList.item(i).getNodeName().equals("desc")
					&& !childList.item(i).getNodeName().equals("defs")) {
				// �����������Ͷ�����Ľڵ�
				Element e = (Element) (childList.item(i));
				e = (Element) doc.importNode(e, true);
				gElement.appendChild(e);
				modifyBounds(handle, e, x, y);
			}
		}

		EditorToolkit.appendMetadataElement(handle, modelID, gElement);
		editor.getSvgSession().refreshHandle(gElement);
		editor.getSvgSession().refreshCurrentHandleImediately();
		handle.getSelection().handleSelection(gElement, false, true);
		return gElement;
	}

	private void modifyBounds(SVGHandle handle, Element el, int x, int y) {
		if (el instanceof SVGOMEllipseElement) {
			double dx = Double.parseDouble(el.getAttribute("cx"));
			double dy = Double.parseDouble(el.getAttribute("cy"));
			EditorToolkit.setAttributeValue(el, "cx", dx + x);
			EditorToolkit.setAttributeValue(el, "cy", dy + y);
		} else if (el instanceof SVGOMTextElement) {
			double dx = Double.parseDouble(el.getAttribute("x"));
			double dy = Double.parseDouble(el.getAttribute("y"));
			final AffineTransform initialTransform = handle
					.getSvgElementsManager().getTransform(el);

			// getting the new transform
			AffineTransform transform = AffineTransform.getTranslateInstance(x,
					y);
			AffineTransform newTransform = new AffineTransform(initialTransform);
			newTransform.preConcatenate(transform);
			handle.getSvgElementsManager().setTransform(el, newTransform);
		} else if (el instanceof SVGOMPathElement) {
			AffineTransform actionTransform = AffineTransform
					.getTranslateInstance(x, y);
			Path initialPath = new Path(el.getAttribute("d"));
			Path transformPath = new Path(initialPath);

			// getting the element's transform
			AffineTransform initialTransform = handle.getSvgElementsManager()
					.getTransform(el);

			// concatenating the transforms
			AffineTransform transform = new AffineTransform(initialTransform);
			transform.preConcatenate(actionTransform);

			// transforming the shape
			if (initialPath.canBeAppliedTransform()) {

				transformPath.applyTransform(transform);
				transform = null;
			}

			// getting the path attribute value
			String dValue = transformPath.toString();
			el.setAttribute("d", dValue);
			AffineTransform ftransform = transform;
			handle.getSvgElementsManager().setTransform(el, ftransform);
		} else {
			double dx = Double.parseDouble(el.getAttribute("x"));
			double dy = Double.parseDouble(el.getAttribute("y"));
			EditorToolkit.setAttributeValue(el, "x", dx + x);
			EditorToolkit.setAttributeValue(el, "y", dy + y);
		}
	}

	// public void addUseShapeCreatedListener(UseShapeCreatedListener lis) {
	// useShapeCreatedListeners.add(lis);
	// }
	//
	// public void removeUseShapeCreatedListener(UseShapeCreatedListener lis) {
	// useShapeCreatedListeners.remove(lis);
	// }
	//
	// public void fireUseShapeCreatedListener(Element useEle) {
	// for (UseShapeCreatedListener lis : useShapeCreatedListeners) {
	// if (useEle.getNodeName().equals("use")) {
	// lis.useShapeCreated(useEle);
	// } else {
	// editor.getLogger().log(this, LoggerAdapter.WARN,
	// "Warning:�����Ĳ���use�ڵ�");
	// }
	// }
	// }

}
