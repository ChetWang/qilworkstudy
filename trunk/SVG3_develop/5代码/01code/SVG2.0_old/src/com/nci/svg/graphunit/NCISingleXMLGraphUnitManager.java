package com.nci.svg.graphunit;

import java.util.ArrayList;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.nci.svg.ui.graphunit.NCIThumbnailPanel;
import com.nci.svg.util.NCIGlobal;
import com.nci.svg.util.ServletActionConstants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;

/**
 * 单个XML存放所有图元时的图元管理器
 * 
 * @author Qil.Wong
 * 
 */
public class NCISingleXMLGraphUnitManager extends AbstractNCIGraphUnitManager {

	public NCISingleXMLGraphUnitManager(Editor e) {
		super(e);
	}

	/**
	 * 从servlet端获取Document对象
	 * 
	 * @return
	 */
	private Document getEquipSymboDoc() {
		if (equipDoc == null) {
			String url = new StringBuffer((String)editor.getGCParam("appRoot")).append(
			        (String)editor.getGCParam("servletPath")).append("?action=").append(ServletActionConstants.GET_SYMBOL_FILE)
					.toString();
			equipDoc = Utilities.getXMLDocumentByURL(url);
		}
		return equipDoc;
	}

	/**
	 * 从外部数据创建SVGDocument对象。为了能在JSvgCanvas对象上显示，document对象必须是SVGDocument
	 * 
	 * @param symbolNode
	 * @param viewBox
	 * @return
	 */
	private Document createSvgDocument(Node symbolNode, String viewBox, int type) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		// 这里的namespace值必须是svgNS，否则创建出来的Document对象不会是SVGDocument
		Document doc = impl.createDocument(svgNS, "svg", null);
		Element root = doc.getDocumentElement();
		root.setAttribute("viewBox",
				viewBox == null || viewBox.equals("") ? "0 0 35 35" : viewBox);
		NodeList childrenSymbol = symbolNode.getChildNodes();
		for (int n = 0; n < childrenSymbol.getLength(); n++) {
			try {
				Node generalNode = childrenSymbol.item(n);
				if (generalNode instanceof Element) {
					Node node = null;
					node = doc.importNode(generalNode, true);
					// 需要通过parseNodeToSVGNode方法加上ElementNS。否则无法显示，参见：http://xmlgraphics.apache.org/batik/using/dom-api.html
					if(!node.getNodeName().equals("#text")){
						
					node = Utilities.parseNodeToSVGNode(doc, node);
					root.appendChild(node);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("createSvgDocument exception happeded: "
						+ e.getMessage());
			}
		}
		return doc;
	}

	

	@Override
	public ArrayList<NCIGraphUnitTypeBean> getEquipSymbolTypeList() {
		ArrayList<NCIGraphUnitTypeBean> equipArray =new ArrayList<NCIGraphUnitTypeBean>();
		NodeList nl = getEquipSymboDoc().getElementsByTagName("g");
		for (int i = 0; i < nl.getLength(); i++) {
			Node my_node = nl.item(i);
			if (my_node != null) {
				NamedNodeMap map = my_node.getAttributes();
				String equipName;
				if (map != null)
					if (map.getNamedItem("content") != null) {
						equipName = map.getNamedItem("content").getNodeValue();
						equipArray.add(new NCIGraphUnitTypeBean(equipName,
								my_node));
					}
			}
		}
		return equipArray;
	}

	@Override
	public synchronized ArrayList<NCIThumbnailPanel> getThumnailList(
			NCIGraphUnitTypeBean bean, int type) {
		ArrayList<NCIThumbnailPanel> thumbnails = new ArrayList<NCIThumbnailPanel>();
		Node node = bean.getGraphUnitNode();
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node my_node = childNodes.item(i);
			if (my_node != null && my_node.getNodeName().equals("symbol")) {
				/** 取得symbol节点的所有属性* */
				NamedNodeMap map = my_node.getAttributes();
				String symbolName = "";
				if (map != null)
					if (map.getNamedItem("id") != null) {
						try {
							symbolName = map.getNamedItem("id").getNodeValue();
							NCIThumbnailPanel thumbnail = new NCIThumbnailPanel(
									type,editor);
							// 将symbol的id作为名字显示
							thumbnail.setText(symbolName);
							// 为了完整的显示图像，需要viewBox的属性值
							String viewBox = map.getNamedItem("viewBox")
									.getNodeValue();
							Document doc = createSvgDocument(my_node, viewBox,
									type);
							thumbnail.setDocument(doc);
							thumbnails.add(thumbnail);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
			}
		}
		return thumbnails;
	}

	/**
	 * 节点复制
	 * 
	 * @param doc_dup
	 * @param father
	 * @param son
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public boolean dupliate(Document doc_dup, Node father, Node son)
			throws Exception {
		boolean is_done = false;
		String son_name = son.getNodeName();
		Element subITEM = doc_dup.createElement(son_name);
		// 复制节点的属性
		if (son.hasAttributes()) {
			NamedNodeMap attributes = son.getAttributes();

			for (int i = 0; i < attributes.getLength(); i++) {
				String attribute_name = attributes.item(i).getNodeName();
				String attribute_value = attributes.item(i).getNodeValue();
				subITEM.setAttribute(attribute_name, attribute_value);
			}
		}
		father.appendChild(subITEM);
		// 复制节点的值
		Text value_son = (Text) son.getFirstChild();
		String nodevalue_root = "";
		if (value_son != null && value_son.getLength() > 0)
			nodevalue_root = (String) value_son.getNodeValue();
		Text valuenode_root = null;
		if ((nodevalue_root != null) && (nodevalue_root.length() > 0))
			valuenode_root = doc_dup.createTextNode(nodevalue_root);
		if (valuenode_root != null && valuenode_root.getLength() > 0)
			subITEM.appendChild(valuenode_root);
		// 复制子结点
		NodeList sub_messageItems = son.getChildNodes();
		int sub_item_number = sub_messageItems.getLength();
		if (sub_item_number < 2) {
			// 如果没有子节点,则返回
			is_done = true;
		} else {
			for (int j = 1; j < sub_item_number; j = j + 2) {
				// 如果有子节点,则递归调用本方法
				Element sub_messageItem = (Element) sub_messageItems.item(j);
				is_done = dupliate(doc_dup, subITEM, sub_messageItem);
			}
		}
		return is_done;
	}

	// /**
	// * 从已知URI创建SVGDocument对象
	// * @param uri
	// * @return
	// * @throws IOException
	// */
	// public Document createDocument(String uri) throws IOException{
	// String parser = XMLResourceDescriptor.getXMLParserClassName();
	// SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
	// Document doc = f.createDocument(uri);
	// return doc;
	//
	// }

}
