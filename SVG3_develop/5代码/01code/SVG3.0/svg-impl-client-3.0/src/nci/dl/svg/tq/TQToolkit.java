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

	public static final String MODEL_MAIN_LINE = "����";

	public static final String MODEL_BRANCH_LINE = "֧��";

	public static final String MODEL_JIEHU_LINE = "�ӻ���";

	public static final String MODEL_TONGHUI_LINE = "ͬ��";

	/**
	 * ������svg path
	 */
	public static final String NORMAL_SVG_PATH = "normal-path";

	/**
	 * ���ߵ����ĳƺ�
	 */
	public static final String TQ_WIRE_PATH = "����";

	/**
	 * ֧�ߵ�Ӣ�ı�ʶ
	 */
	public static final String TQ_METADATA_BRANCH = "branch";

	/**
	 * �ӻ��ߵ�Ӣ�ı�ʶ
	 */
	public static final String TQ_METADATA_JIEHU = "jiehu";

	/**
	 * ��·ID�ı�ʶ
	 */
	public static final String TQ_METADATA_LINE_ID = "lineID";

	public static final String TQ_METADATA_UUID = "id";

	/**
	 * ����ID��ʶ
	 */
	public static final String TQ_METADATA_POLE_ID = "poleID";

	/**
	 * �ն˸˱�ʶ
	 */
	public static final String TQ_METADATA_TERMINAL_POLE = "terminal-pole";

	/**
	 * �ն˼�����
	 */
	public static final String TQ_METADATA_TERMINAL_JILIANGBIAO = "terminal-jiliangbiao";

	/**
	 * ת�Ǹ˱�ʶ
	 */
	public static final String TQ_METADATA_CORNER_POLE = "corner-pole";
	/**
	 * ���߱�ʶ
	 */
	public static final String TQ_METADATA_PULL_LINE = "pull-line";

	/**
	 * ������·��
	 */
	public static final String TQ_METADATA_BASE_LINE = "base-line";

	/**
	 * ͬ����·��
	 */
	public static final String TQ_METADATA_TONGHUI_LINE = "tonghui-line";

	public static final String TQ_METADATA_NAME = "name";

	/**
	 * ��������һ��ֵ�����ԣ������ơ����͵�
	 */
	public static final String TQ_METADATA_PROPERTIES = "properties";

	public static final String TQ_METADATA_VALUE = "value";



	/**
	 * ��·���ͣ����ߡ�������·���ȵ�
	 */
	public static final String TQ_METADATA_LINE_TYPE = "lineType";

	/**
	 * ������ν
	 */
	public static final String POLE_NAME = "����";

	/**
	 * �������ν
	 */
	public static final String JILIANGBIAO_NAME = "������";

	/**
	 * ��ѡ��Ľڵ��ܷ����֧��
	 * 
	 * @param ele
	 * @return
	 */
	public static boolean isSelectedCanDrawBranch(Element ele) {
		// ���ڸ��߻�֧���ϵĸ��������������䣩���ܻ��Ƹ���
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
	 * �ж�����Ƿ�����·��
	 * 
	 * @param compoElement
	 *            ���Element
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
	 * �жϸýڵ��Ƿ��Ǹ���
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
	 * �жϸýڵ��ǲ��ǽӻ���
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
	 * �жϸýڵ��Ƿ���֧��
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
	 * �жϸýڵ��Ƿ��Ǹ���
	 * 
	 * @param node
	 *            ָ���Ľڵ�
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
	 * ����ѡ��Ľڵ��ȡ��·���ͣ��������ߡ�֧���Լ����ߣ�
	 * 
	 * @param ele
	 *            ���ߡ�֧�ߡ������Լ�������svg path
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
	 * �ж����λ���µĽڵ��Ƿ��ʺϻ�֧��
	 * 
	 * @param nodeAtMousePoint
	 *            ���λ���µĽڵ�
	 * @return
	 */
	public static boolean isBranchLineDrawable(Node nodeAtMousePoint) {
		if (nodeAtMousePoint instanceof Element) {
			return isSelectedCanDrawBranch((Element) nodeAtMousePoint);
		}
		return false;
	}

	/**
	 * �Ƿ��ʺϻ��ӻ���
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
	 * Ϊͼ���������Ԫ���ݣ������һ����ӣ��򴴽�metadata�ڵ㣬�������ݶ�����metadata�ڵ���
	 * 
	 * @param parentEle
	 *            ͼ�����ݽڵ�
	 * @param metadataName
	 *            Ԫ��������
	 * @param attributeName
	 *            Ԫ���ݵ���������Ĭ��Ϊname
	 * @param attributeValue
	 *            Ԫ��������ֵ��Ĭ��Ϊ""
	 * @param appended
	 *            �Ƿ�׷�ӵ�����Ԫ���ݽڵ㣬trueΪ׷�ӣ�falseΪ�½���һ��Ԫ���ݽڵ�
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
				new Exception("�޷��ҵ���" + metadataName + "���Ӧ��Ԫ����")
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
	 * ����Ϊ�ն˸�
	 * 
	 * @param poleEle
	 */
	public static void setToTerminalPole(Element poleEle, String lineID) {
		addMetadata(poleEle, TQ_METADATA_TERMINAL_POLE, TQ_METADATA_LINE_ID,
				lineID, false);
	}

	/**
	 * ����Ϊת�Ǹ�
	 * 
	 * @param poleEle
	 * @param lineID
	 */
	public static void setToCornerPole(Element poleEle, String lineID) {
		addMetadata(poleEle, TQ_METADATA_CORNER_POLE, TQ_METADATA_LINE_ID,
				lineID, false);
	}

	/**
	 * ��ȡָ��Ԫ�ص�metadata���Ƶ�ֵ
	 * 
	 * @param ele
	 *            ָ��Ԫ��
	 * @param metadataName
	 *            ָ��Ԫ�ص�metadata
	 * @param attributeName
	 *            ָ��Ԫ�ص�metadata����
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
			new Exception("ָ��Ԫ�ص�metadata�ڵ���������ȷ�����ȣ�" + metadatas.getLength());
		}
		return null;
	}

	/**
	 * ��ȡָ���ڵ������
	 * 
	 * @param ele
	 *            ָ���Ľڵ�
	 * @return ָ���ڵ�����ƣ����Ϊ�գ��򷵻�uuid
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
	 * ��ȡָ���ڵ����ӵ�֧��
	 * 
	 * @param ele
	 * @return �ڵ�����֧�ߵ�id
	 */
	public static String[] getBranchlines(Element ele) {
		return getMetadata(ele, TQ_METADATA_BRANCH, TQ_METADATA_LINE_ID);
	}

	/**
	 * ����ID��ȡ�齨����
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
	 * �Ƿ����ն˸����ڵ�
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
	 * �Ƿ���ת�Ǹ˽ڵ�
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
	 * �����豸��Ҫ�Ľڵ�Element
	 * 
	 * @param ownerDoc
	 * @param href
	 *            href������Ҫ�����ӵ�����
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
	 * ����path�ڵ�
	 * 
	 * @param ownerDoc
	 * @param startPoint
	 *            ��ʼ��
	 * @param endPoint
	 *            �յ�
	 * @return
	 */
	public static Element createPathElement(Document ownerDoc,
			Point2D startPoint, Point2D endPoint) {
		// �����߽ڵ�
		Element element = ownerDoc.createElementNS(ownerDoc
				.getDocumentElement().getNamespaceURI(), "path");

		// ����������
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
	 * �жϴ�����·���Ƿ�������豸
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
	 * �ж��Ƿ��ǽӻ����ϵ����һ��������
	 * 
	 * @param e
	 *            ָ����useԪ��
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
	 * �Ƴ�metadata�ڵ�
	 * 
	 * @param e
	 *            ָ�����豸�ڵ�
	 * @param metadataName
	 *            ָ����metadata����
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
