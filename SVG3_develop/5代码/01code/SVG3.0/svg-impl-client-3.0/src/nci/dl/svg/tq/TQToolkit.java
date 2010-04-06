package nci.dl.svg.tq;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.UUID;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;

import fr.itris.glips.library.geom.path.Path;

public class TQToolkit {

	public static final String MODEL_ATTR = "model";

	public static final String MODEL_MAIN_LINE = "干线";

	public static final String MODEL_BRANCH_LINE = "支线";

	public static final String MODEL_JIEHU_LINE = "接户线";

	public static final String MODEL_TONGHUI_LINE = "同回";

	/**
	 * 基本的svg path
	 */
	public static final String NORMAL_SVG_PATH = "normal-path";

	/**
	 * 导线的中文称呼
	 */
	public static final String TQ_WIRE_PATH = "导线";

	/**
	 * 支线的英文标识
	 */
	public static final String TQ_METADATA_BRANCH = "branch";

	/**
	 * 接户线的英文标识
	 */
	public static final String TQ_METADATA_JIEHU = "jiehu";

	/**
	 * 线路ID的标识
	 */
	public static final String TQ_METADATA_LINE_ID = "lineID";

	public static final String TQ_METADATA_UUID = "id";

	/**
	 * 杆塔ID标识
	 */
	public static final String TQ_METADATA_POLE_ID = "poleID";

	/**
	 * 终端杆标识
	 */
	public static final String TQ_METADATA_TERMINAL_POLE = "terminal-pole";

	/**
	 * 终端计量表
	 */
	public static final String TQ_METADATA_TERMINAL_JILIANGBIAO = "terminal-jiliangbiao";

	/**
	 * 转角杆标识
	 */
	public static final String TQ_METADATA_CORNER_POLE = "corner-pole";
	/**
	 * 拉线标识
	 */
	public static final String TQ_METADATA_PULL_LINE = "pull-line";

	/**
	 * 基本线路段
	 */
	public static final String TQ_METADATA_BASE_LINE = "base-line";

	/**
	 * 同回线路段
	 */
	public static final String TQ_METADATA_TONGHUI_LINE = "tonghui-line";

	public static final String TQ_METADATA_NAME = "name";

	/**
	 * 仅可能有一个值的属性，如名称、类型等
	 */
	public static final String TQ_METADATA_PROPERTIES = "properties";

	public static final String TQ_METADATA_VALUE = "value";



	/**
	 * 线路类型，拉线、基本线路、等等
	 */
	public static final String TQ_METADATA_LINE_TYPE = "lineType";

	/**
	 * 杆塔称谓
	 */
	public static final String POLE_NAME = "杆塔";

	/**
	 * 计量表称谓
	 */
	public static final String JILIANGBIAO_NAME = "计量表";

	/**
	 * 该选择的节点能否绘制支线
	 * 
	 * @param ele
	 * @return
	 */
	public static boolean isSelectedCanDrawBranch(Element ele) {
		// 属于干线或支线上的杆塔或其它（补充）才能绘制干线
		boolean flag = false;
		if (ele.getParentNode() != null
				&& (isMainLine(ele.getParentNode()) || isBranchLine(ele
						.getParentNode()))) {
			String xlink = ele.getAttributeNS(EditorToolkit.xmlnsXLinkNS,
					"href");
			if (xlink.indexOf(POLE_NAME) >= 0) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 判断组件是否在线路上
	 * 
	 * @param compoElement
	 *            组件Element
	 * @return
	 */
	public static boolean isComponnetOnLine(Element compoElement) {
		Node compoElementParent = compoElement.getParentNode();
		if (compoElementParent != null) {
			if (isMainLine(compoElementParent)
					|| isBranchLine(compoElementParent)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断该节点是否是干线
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isMainLine(Node node) {
		if (node instanceof Element) {
			if (((Element) node).getAttribute(MODEL_ATTR).equals(
					MODEL_MAIN_LINE)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断该节点是不是接户线
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isJieHuLine(Node node) {
		if (node instanceof Element) {
			if (((Element) node).getAttribute(MODEL_ATTR).equals(
					MODEL_JIEHU_LINE)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断该节点是否是支线
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isBranchLine(Node node) {
		if (node instanceof Element) {
			if (((Element) node).getAttribute(MODEL_ATTR).equals(
					MODEL_BRANCH_LINE)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断该节点是否是杆塔
	 * 
	 * @param node
	 *            指定的节点
	 * @return
	 */
	public static boolean isPole(Element ele) {
		if (ele.getNodeName().equals("use")) {
			String xlink = ele.getAttributeNS(EditorToolkit.xmlnsXLinkNS,
					"href");
			if (xlink.indexOf(POLE_NAME) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据选择的节点获取线路类型，包括干线、支线以及导线，
	 * 
	 * @param ele
	 *            干线、支线、导线以及正常的svg path
	 * @return
	 */
	public static String getLineType(Element ele) {
		if (isBranchLine(ele)) {
			return MODEL_BRANCH_LINE;
		}
		if (isMainLine(ele)) {
			return MODEL_MAIN_LINE;
		}
		Node parentNode = ele.getParentNode();
		if (!isBranchLine(parentNode) && !isMainLine(parentNode)) {
			if (ele.getNodeName().equals("path")) {
				return NORMAL_SVG_PATH;
			}
		} else if (isBranchLine(parentNode) || isMainLine(parentNode)) {
			if (ele.getNodeName().equals("path")) {
				return TQ_WIRE_PATH;
			}
		}
		return null;
	}

	/**
	 * 判断鼠标位置下的节点是否适合画支线
	 * 
	 * @param nodeAtMousePoint
	 *            鼠标位置下的节点
	 * @return
	 */
	public static boolean isBranchLineDrawable(Node nodeAtMousePoint) {
		if (nodeAtMousePoint instanceof Element) {
			return isSelectedCanDrawBranch((Element) nodeAtMousePoint);
		}
		return false;
	}

	/**
	 * 是否适合画接户线
	 * 
	 * @param nodeAtMousePoint
	 * @return
	 */
	public static boolean isJieHuLineDrawable(Node nodeAtMousePoint) {
		if (nodeAtMousePoint instanceof Element) {
			return isSelectedCanDrawBranch((Element) nodeAtMousePoint);
		}
		return false;
	}



	/**
	 * 为图形数据添加元数据，如果第一次添加，则创建metadata节点，所有数据都存在metadata节点下
	 * 
	 * @param parentEle
	 *            图形数据节点
	 * @param metadataName
	 *            元数据名称
	 * @param attributeName
	 *            元数据的属性名，默认为name
	 * @param attributeValue
	 *            元数据属性值，默认为""
	 * @param appended
	 *            是否追加到已有元数据节点，true为追加，false为新建另一个元数据节点
	 */
	public static void addMetadata(Element parentEle, String metadataName,
			String attributeName, String attributeValue, boolean appended) {
		NodeList l = parentEle.getElementsByTagName(Constants.NCI_SVG_METADATA);
		Element metadataEle = null;
		if (l.getLength() == 0) {
			metadataEle = parentEle.getOwnerDocument().createElement(
					Constants.NCI_SVG_METADATA);
			parentEle.appendChild(metadataEle);
		} else {
			metadataEle = (Element) l.item(0);
		}
		Element childMetadata = null;
		if (appended) {
			NodeList metadatas = metadataEle.getElementsByTagName(metadataName);
			if (metadatas.getLength() > 0) {
				childMetadata = (Element) metadatas.item(0);
			} else {
				new Exception("无法找到与" + metadataName + "相对应的元数据")
						.printStackTrace();
				return;
			}
		} else {
			childMetadata = parentEle.getOwnerDocument().createElement(
					metadataName);
			boolean existSameNode = false;
			NodeList metadatas = metadataEle.getElementsByTagName(metadataName);
			for (int i = 0; i < metadatas.getLength(); i++) {
				Element e = (Element) metadatas.item(i);
				if (e.getAttribute(attributeName).equals(attributeValue)) {
					existSameNode = true;
					break;
				}
			}
			if (!existSameNode)
				metadataEle.appendChild(childMetadata);
		}
		childMetadata.setAttribute(attributeName, attributeValue);
	}

	/**
	 * 设置为终端杆
	 * 
	 * @param poleEle
	 */
	public static void setToTerminalPole(Element poleEle, String lineID) {
		addMetadata(poleEle, TQ_METADATA_TERMINAL_POLE, TQ_METADATA_LINE_ID,
				lineID, false);
	}

	/**
	 * 设置为转角杆
	 * 
	 * @param poleEle
	 * @param lineID
	 */
	public static void setToCornerPole(Element poleEle, String lineID) {
		addMetadata(poleEle, TQ_METADATA_CORNER_POLE, TQ_METADATA_LINE_ID,
				lineID, false);
	}

	/**
	 * 获取指定元素的metadata名称的值
	 * 
	 * @param ele
	 *            指定元素
	 * @param metadataName
	 *            指定元素的metadata
	 * @param attributeName
	 *            指定元素的metadata名称
	 * @return
	 */
	public static String[] getMetadata(Element ele, String metadataName,
			String attributeName) {
		NodeList metadatas = ele
				.getElementsByTagName(Constants.NCI_SVG_METADATA);
		if (metadatas.getLength() == 1) {
			Element metadata = (Element) metadatas.item(0);
			NodeList values = metadata.getElementsByTagName(metadataName);
			int valueLen = values.getLength();
			if (valueLen > 0) {
				String[] metadataArr = new String[valueLen];
				for (int i = 0; i < valueLen; i++) {
					Element e = (Element) values.item(i);
					metadataArr[i] = e.getAttribute(attributeName);
				}
				return metadataArr;
			}
		} else {
			new Exception("指定元素的metadata节点数量不正确。长度：" + metadatas.getLength());
		}
		return null;
	}

	/**
	 * 获取指定节点的名称
	 * 
	 * @param ele
	 *            指定的节点
	 * @return 指定节点的名称，如果为空，则返回uuid
	 */
	public static TQName getComponentName(Element ele) {
		String[] names = getMetadata(ele, TQ_METADATA_PROPERTIES,
				TQ_METADATA_NAME);
		String name = null;
		if (names != null && !names[0].equals("")) {
			name = names[0];
		}
		return new TQName(name, ele.getAttribute(TQ_METADATA_UUID));
	}

	/**
	 * 获取指定节点连接的支线
	 * 
	 * @param ele
	 * @return 节点所在支线的id
	 */
	public static String[] getBranchlines(Element ele) {
		return getMetadata(ele, TQ_METADATA_BRANCH, TQ_METADATA_LINE_ID);
	}

	/**
	 * 根据ID获取组建名称
	 * 
	 * @param id
	 * @return
	 */
	public static TQName getComonentNameByID(String id, Node docNode) {
		try {
			Element ele = (Element) Utilities.findNode("//*[@id='" + id + "']",
					docNode);
			return getComponentName(ele);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 是否是终端杆塔节点
	 * 
	 * @param poleEle
	 * @return
	 */
	public static boolean isTerminalPole(Element poleEle) {
		String[] meta = getMetadata(poleEle, TQ_METADATA_TERMINAL_POLE,
				TQ_METADATA_LINE_ID);
		if (meta != null && meta.length > 0)
			return true;
		return false;
	}

	/**
	 * 是否是转角杆节点
	 * 
	 * @param poleEle
	 * @return
	 */
	public static boolean isCornerPole(Element poleEle) {
		String[] meta = getMetadata(poleEle, TQ_METADATA_CORNER_POLE,
				TQ_METADATA_LINE_ID);
		if (meta != null && meta.length > 0)
			return true;
		return false;
	}
	
	public static final double TQ_GRAPH_DEFAULT_WIDTH = 30d;
	
	public static final double TQ_GRAPH_DEFAULT_HEIGHT = 30d;

	/**
	 * 创建设备需要的节点Element
	 * 
	 * @param ownerDoc
	 * @param href
	 *            href属性需要的连接的名字
	 * @param point
	 * @return
	 */
	public static Element createEquipElement(Document ownerDoc, String href,
			Point2D point) {
		Element useEle = ownerDoc.createElementNS(ownerDoc.getDocumentElement()
				.getNamespaceURI(), "use");
		useEle.setAttributeNS(EditorToolkit.xmlnsXLinkNS,
				EditorToolkit.hrefAtt, "#" + href);
		useEle.setAttribute("id", UUID.randomUUID().toString());
		useEle.setAttribute("filterUnits", "userSpaceOnUse");
		EditorToolkit.setAttributeValue(useEle, EditorToolkit.xAtt, point
				.getX()
				- TQ_GRAPH_DEFAULT_WIDTH / 2);
		EditorToolkit.setAttributeValue(useEle, EditorToolkit.yAtt, point
				.getY()
				- TQ_GRAPH_DEFAULT_HEIGHT / 2);
		EditorToolkit.setAttributeValue(useEle, EditorToolkit.wAtt,
				TQ_GRAPH_DEFAULT_WIDTH);
		EditorToolkit.setAttributeValue(useEle, EditorToolkit.hAtt,
				TQ_GRAPH_DEFAULT_HEIGHT);
		useEle
				.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:show",
						"embed");
		useEle.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:type",
				"simple");
		useEle.setAttributeNS(null, EditorToolkit.preserveAtt, "none meet");
		return useEle;
	}

	/**
	 * 生成path节点
	 * 
	 * @param ownerDoc
	 * @param startPoint
	 *            起始点
	 * @param endPoint
	 *            终点
	 * @return
	 */
	public static Element createPathElement(Document ownerDoc,
			Point2D startPoint, Point2D endPoint) {
		// 构建线节点
		Element element = ownerDoc.createElementNS(ownerDoc
				.getDocumentElement().getNamespaceURI(), "path");

		// 设置线属性
		StringBuffer pathD = new StringBuffer("M");
		pathD.append(startPoint.getX()).append(" ").append(startPoint.getY());
		pathD.append("L");
		pathD.append(endPoint.getX()).append(" ").append(endPoint.getY());

		element.setAttribute("d", pathD.toString());
		element.setAttributeNS(null, "style", "fill:none;stroke:"
				+ Constants.NCI_DEFAULT_STROKE_COLOR + ";");

		return element;
	}

	

	/**
	 * 判断此类线路上是否能添加设备
	 * 
	 * @param pathEle
	 * @return
	 */
	public static boolean isEquipCanAdded(Element pathEle) {
		String[] lineType = getMetadata(pathEle, TQ_METADATA_PROPERTIES,
				TQ_METADATA_LINE_TYPE);
		if (lineType != null && lineType.length == 1) {
			if (lineType[0].equals(TQ_METADATA_BASE_LINE)) {
				return true;
			}
		}
		return false;
	}

	public static void setToTerminalJiliangbiao(Element e, String lineID) {
		addMetadata(e, TQ_METADATA_TERMINAL_JILIANGBIAO, TQ_METADATA_LINE_ID,
				lineID, false);
	}

	/**
	 * 判断是否是接户线上的最后一个计量表
	 * 
	 * @param e
	 *            指定的use元素
	 * @return
	 */
	public static boolean isTerminalJiliangbiao(Element e) {
		String[] meta = getMetadata(e, TQ_METADATA_TERMINAL_JILIANGBIAO,
				TQ_METADATA_LINE_ID);
		if (meta != null && meta.length > 0)
			return true;
		return false;
	}

	/**
	 * 移出metadata节点
	 * 
	 * @param e
	 *            指定的设备节点
	 * @param metadataName
	 *            指定的metadata名称
	 */
	public static void removeMetadata(Element e, String metadataName) {
		NodeList metadata = e.getElementsByTagName(Constants.NCI_SVG_METADATA);
		if (metadata != null && metadata.getLength() == 1) {
			Node metadataNode = metadata.item(0);
			NodeList nodes = metadataNode.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				if (metadataName.equals(n.getNodeName())) {
					n.getParentNode().removeChild(n);
				}
			}
		} else {
			new Exception("No such metadata or metadata element size > 1:"
					+ metadataName).printStackTrace();
		}
	}

	public static void removeMetadata(Element e, String metadataName,
			String attributeName, String attributeValue) {
		NodeList metadata = e.getElementsByTagName(Constants.NCI_SVG_METADATA);
		if (metadata != null && metadata.getLength() == 1) {
			Node metadataNode = metadata.item(0);
			NodeList nodes = metadataNode.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element n = (Element) nodes.item(i);
				if (metadataName.equals(n.getNodeName())
						&& attributeValue.equals(n.getAttribute(attributeName))) {
					n.getParentNode().removeChild(n);
				}
			}
		} else {
			new Exception("No such metadata or metadata element size > 1:"
					+ metadataName).printStackTrace();
		}
	}
}
