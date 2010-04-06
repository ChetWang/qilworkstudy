package com.nci.svg.district;

import java.awt.Cursor;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.dom.svg.SVGOMGElement;
import org.apache.batik.dom.svg.SVGOMUseElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.bean.TreeNodeBean;
import com.nci.svg.district.DistrictTopology.LineGraphInfo;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.shape.GraphUnitImageShape;
import com.nci.svg.sdk.topology.TopologyManagerAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-3-27
 * @���ܣ�̨��ͼ���˹�����
 * 
 */
public class DistrictTopologyManager extends TopologyManagerAdapter {

	/**
	 * add by yux,2009-4-9 ̨�����˶���
	 */
	protected DistrictTopology topology = new DistrictTopology();
	/**
	 * add by yux,2009-4-9 ���������ڵ�
	 */
	private DefaultMutableTreeNode root = null;
	/**
	 * add by yux,2009-4-9 ������model
	 */
	private DefaultTreeModel treeModel = null;
	/**
	 * ���ڵ�
	 */
	private Element transformerElement = null;
	/**
	 * �����ڵ�
	 */
	private Element distributionBoxElement = null;

	public DistrictTopologyManager(EditorAdapter editor, SVGHandle handle) {
		super(editor, handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.topology.TopologyManagerAdapter#getTreeModel()
	 */
	@Override
	public TreeModel getTreeModel() {
		if (createTopology()) {
			HashMap<String, DefaultMutableTreeNode> mapNode = new HashMap<String, DefaultMutableTreeNode>();
			// ͼ�����˹����ɹ�������ģ����
			TreeNodeBean nodeBean = new TreeNodeBean("����", handle.getCanvas()
					.getDocument().getDocumentElement());
			root = new DefaultMutableTreeNode(nodeBean);
			// // �������ڵ�
			// {
			// TreeNodeBean bean = new TreeNodeBean(TRANSFORMER,
			// transformerElement);
			// DefaultMutableTreeNode node = new DefaultMutableTreeNode(bean);
			// root.add(node);
			// }
			//
			// // ��������ڵ�
			// {
			// TreeNodeBean bean = new TreeNodeBean(DISTRIBUTIONBOX,
			// distributionBoxElement);
			// DefaultMutableTreeNode node = new DefaultMutableTreeNode(bean);
			// root.add(node);
			// }

			LinkedList<LineGraphInfo> lateralLines = new LinkedList<LineGraphInfo>();
			// ���Ӹ���
			for (LineGraphInfo line : topology.getLineList()) {
				if (line.getLineType().equals(DistrictTopology.MAIN_LINE)) {
					TreeNodeBean bean = null;
					if (topology.isRelated()) {
						bean = new TreeNodeBean(line.getLineName(), line
								.getLineElement());
					} else {
						bean = new TreeNodeBean(line.getLineID(), line
								.getLineElement());
					}
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(
							bean);
					root.add(node);
					mapNode.put(line.getLineID(), node);
					insertEquipIntoTree(node, line, mapNode, null);
				} else if (line.getLineType().equals(
						DistrictTopology.LATERAL_LINE)) {
					lateralLines.add(line);
				}
			}

			// ����֧�ߣ���Ҫ���ϱ�����������·���ڵ�����·����
			int count = 0;
			while (!lateralLines.isEmpty() && count < 50) {
				LineGraphInfo line = lateralLines.poll();
				String parentID = getParentEquipID(line);
				DefaultMutableTreeNode parentNode = mapNode.get(parentID);
				if (parentNode == null) {
					lateralLines.push(line);
					count++;
					continue;
				}

				TreeNodeBean bean = null;
				if (topology.isRelated()) {
					bean = new TreeNodeBean(line.getLineName(), line
							.getLineElement());
				} else {
					bean = new TreeNodeBean(line.getLineID(), line
							.getLineElement());
				}
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(bean);
				parentNode.add(node);
				mapNode.put(line.getLineID(), node);
				insertEquipIntoTree(node, line, mapNode, parentID);
			}
			// for (LineGraphInfo line : topology.getLineList()) {
			// if (line.getLineType().equals(DistrictTopology.LATERAL_LINE)) {
			// String parentID = getParentEquipID(line);
			// DefaultMutableTreeNode parentNode = mapNode.get(parentID);
			// TreeNodeBean bean = new TreeNodeBean(line.getLineID(), line
			// .getLineElement());
			// DefaultMutableTreeNode node = new DefaultMutableTreeNode(
			// bean);
			// parentNode.add(node);
			// mapNode.put(line.getLineID(), node);
			// insertEquipIntoTree(node, line, mapNode, parentID);
			// }
			// }

			// ���ӽӻ���
			for (LineGraphInfo line : topology.getLineList()) {
				if (line.getLineType().equals(DistrictTopology.SERVICE_LINE)) {
					String parentID = getParentEquipID(line);
					DefaultMutableTreeNode parentNode = mapNode.get(parentID);
					TreeNodeBean bean = null;
					if (topology.isRelated()) {
						bean = new TreeNodeBean(line.getLineName(), line
								.getLineElement());
					} else {
						bean = new TreeNodeBean(line.getLineID(), line
								.getLineElement());
					}
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(
							bean);

					parentNode.add(node);
					insertEquipIntoTree(node, line, mapNode, parentID);
				}
			}
			treeModel = new DefaultTreeModel(root);

			// ��ҵ��ģ�͹�����־��λ
			topology.setRelated(false);
			return treeModel;

		}
		return null;
	}

	/**
	 * ����·�������漰���豸ͼԪ�������ڵ�
	 * 
	 * @param parentnode:�����ڵ�
	 * @param line:��·����
	 * @param mapNode:���ڵ�ӳ���
	 * @param beginID:��ʼ����
	 */
	private void insertEquipIntoTree(DefaultMutableTreeNode parentnode,
			LineGraphInfo line,
			HashMap<String, DefaultMutableTreeNode> mapNode, String beginID) {
		boolean listWay = false;
		if (beginID != null) {
			if (line.getEquipList().getLast().equals(beginID))
				listWay = true;
		}
		if (!listWay) {
			for (String equipID : line.getEquipList()) {
				if (beginID != null && equipID.equals(beginID))
					continue;
				TreeNodeBean bean = null;
				if (topology.isRelated()) {
					String name = line.getEquipName().get(equipID);
					bean = new TreeNodeBean(name, handle.getCanvas()
							.getDocument().getElementById(equipID));
				} else {
					bean = new TreeNodeBean(equipID, handle.getCanvas()
							.getDocument().getElementById(equipID));
				}
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(bean);
				parentnode.add(node);
				mapNode.put(equipID, node);
			}
		} else {
			int len = line.getEquipList().size();
			for (int i = len - 1; i >= 0; i--) {
				String equipID = line.getEquipList().get(i);
				if (beginID != null && equipID.equals(beginID))
					continue;
				TreeNodeBean bean = null;
				if (topology.isRelated()) {
					String name = line.getEquipName().get(equipID);
					bean = new TreeNodeBean(name, handle.getCanvas()
							.getDocument().getElementById(equipID));
				} else {
					bean = new TreeNodeBean(equipID, handle.getCanvas()
							.getDocument().getElementById(equipID));
				}
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(bean);
				parentnode.add(node);
				mapNode.put(equipID, node);

			}
		}
	}

	/**
	 * Ѱ����·�ĸ��豸���
	 * 
	 * @param line:��·����
	 * @return ������򷵻ظ��豸ͼԪ���
	 */
	private String getParentEquipID(LineGraphInfo line) {
		String beginID = line.getEquipList().getFirst();
		String endID = line.getEquipList().getLast();
		Element beginEl = handle.getCanvas().getDocument().getElementById(
				beginID);
		if (!beginEl.getParentNode().isSameNode(line.getLineElement())) {
			return beginID;
		}
		return endID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.topology.TopologyManagerAdapter#nodifyMouseEvent(int,
	 *      fr.itris.glips.svgeditor.display.handle.SVGHandle,
	 *      java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
	@Override
	public boolean nodifyMouseEvent(int mouseEventType, Point2D initPoint,
			Point2D curPoint) {
		// TODO Auto-generated method stub
		// add by yux,2009-3-27
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.topology.TopologyManagerIF#createTopology()
	 */
	@Override
	public boolean createTopology() {
		// ����ͼ������
		Document doc = handle.getCanvas().getDocument();
		// Ѱ�ҵ�һ�����
		if (transformerElement == null
				|| !transformerElement.getOwnerDocument().equals(doc)) {
			transformerElement = findTransformer(doc, false);
			if (transformerElement != null) {
				topology.setTransformerElement(transformerElement);
			}
		}
		// // Ѱ�ҵ�һ������
		if (distributionBoxElement == null
				|| !distributionBoxElement.getOwnerDocument().equals(doc)) {
			distributionBoxElement = findDistributionBox(doc, false);
			if (distributionBoxElement != null) {
				topology.setDistributionBoxElement(distributionBoxElement);
			}
		}
		// У����������������ϵ
		//
		// boolean relaFlag = isTransRelaBox(doc, false);
		// if (!relaFlag)
		// return false;
		// �������еĸ��ߡ�֧�ߡ��ӻ���
		findLine(doc);

		topology.setTopologyCreated(true);
		return true;
	}

	/**
	 * Ѱ�����ڵ�
	 * 
	 * @return ������򷵻����ڵ㣬�������򷵻�null
	 */
	protected Element findTransformer(Document doc, boolean showFlag) {
		// Utilities.printNode(doc.getDocumentElement(), true);
		String xpathExpr = "//*[contains(@xlink:href,'"
				+ DistrictTopology.TRANSFORMER + "')]";
		NodeList list = null;
		try {
			list = Utilities.findNodes(xpathExpr, doc.getDocumentElement());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			list = null;
		}
		if (list == null) {
			if (showFlag)
				editor.getSvgSession().showMessageBox("�������", "�����");
			return null;
		}

		if (list.getLength() > 1) {
			if (showFlag)
				editor.getSvgSession().showMessageBox("�������", "�����������1");
			return null;
		}
		return (Element) list.item(0);
	}

	/**
	 * Ѱ������ڵ�
	 * 
	 * @return ������򷵻�����ڵ㣬�������򷵻�null
	 */
	protected Element findDistributionBox(Document doc, boolean showFlag) {
		String xpathExpr = "//*[contains(@xlink:href,'"
				+ DistrictTopology.DISTRIBUTIONBOX + "')]";
		NodeList list = null;
		try {
			list = Utilities.findNodes(xpathExpr, doc.getDocumentElement());
		} catch (XPathExpressionException e) {
			list = null;
		}
		if (list == null) {
			if (showFlag)
				editor.getSvgSession().showMessageBox("��������", "������");
			return null;
		}

		if (list.getLength() > 1) {
			if (showFlag)
				editor.getSvgSession().showMessageBox("��������", "������������1");
			return null;
		}
		return (Element) list.item(0);
	}

	/**
	 * У������������Ƿ�����
	 * 
	 * @return �����򷵻�true���������򷵻�false
	 */
	protected boolean isTransRelaBox(Document doc, boolean showFlag) {
		if (topology.getTransformerElement() == null
				|| topology.getDistributionBoxElement() == null)
			return false;
		String transformerID = topology.getTransformerElement().getAttribute(
				"id");
		String distributionBoxID = topology.getDistributionBoxElement()
				.getAttribute("id");
		if (transformerID == null || distributionBoxID == null
				|| transformerID.length() == 0
				|| distributionBoxID.length() == 0)
			return false;
		StringBuffer xpathExpr = new StringBuffer();
		xpathExpr.append("//*[").append("(p0='").append(transformerID).append(
				"' and p1 = '").append(distributionBoxID)
				.append("') or ( p1='").append(transformerID).append(
						"' and p0 = '").append(distributionBoxID).append("')]");
		NodeList list = null;
		try {
			list = Utilities.findNodes(xpathExpr.toString(), doc
					.getDocumentElement());
		} catch (XPathExpressionException e) {
			list = null;
		}
		if (list == null) {
			if (showFlag)
				editor.getSvgSession().showMessageBox("��������", "������");
			return false;
		}

		return true;
	}

	/**
	 * ����docѰ�ҷ���ҵ��ĸ��ߡ�֧�ߺͽӻ���
	 * 
	 * @param doc
	 *            ��������doc����
	 */
	protected void findLine(Document doc) {
		// ����·�е���·������ʱ����
		HashMap<String, String> lineNames = new LinkedHashMap<String, String>();
		// ����·�µ��豸���ֱ���<��·���,<�豸���,�豸����>>
		HashMap<String, HashMap<String, String>> equipNames = new LinkedHashMap<String, HashMap<String,String>>();
		for (LineGraphInfo line : topology.getLineList()) {
			String lineID = line.getLineID();
			String lineName = line.getLineName();
			lineNames.put(lineID, lineName);
			
			HashMap<String, String> subEquipNames = line.getEquipName();
			equipNames.put(lineID, subEquipNames);
		}

		topology.getLineList().clear();
		String xpathExpr = "//*[@model = '" + DistrictTopology.MAIN_LINE
				+ "' or @model = '" + DistrictTopology.LATERAL_LINE
				+ "' or @model = '" + DistrictTopology.SERVICE_LINE + "']";
		NodeList list = null;
		try {
			list = Utilities.findNodes(xpathExpr.toString(), doc
					.getDocumentElement());
		} catch (XPathExpressionException e) {
			list = null;
		}
		if (list == null)
			return;
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof SVGOMGElement) {
				Element gElement = (Element) list.item(i);
				HashMap<String, String> mapTemp = new HashMap<String, String>();
				String id = gElement.getAttribute("id");
				LineGraphInfo lineInfo = new DistrictTopology.LineGraphInfo();
				lineInfo.setLineElement(gElement);
				lineInfo.setLineID(id);
				String name = lineNames.get(id);
				lineInfo.setLineName(name);
				HashMap<String, String> subEquipNames = equipNames.get(id);
				lineInfo.setEquipName(subEquipNames);
				lineInfo.setLineType(gElement.getAttribute("model"));

				NodeList pathList = gElement.getElementsByTagName("path");
				lineInfo.setEquipNum(pathList.getLength() + 1);
				for (int j = 0; j < pathList.getLength(); j++) {
					Element pathElement = (Element) pathList.item(j);
					String p0 = pathElement.getAttribute("p0");
					String p1 = pathElement.getAttribute("p1");
					calEquipElement(lineInfo, mapTemp, p0, p1);
				}

				trimEquipElement(lineInfo, mapTemp);
				mapTemp.clear();
				topology.getLineList().add(lineInfo);
			}
		}
		return;
	}

	/**
	 * ���ݴ���Ľڵ��Ӧ��ϵ�����������豸˳��
	 * 
	 * @param lineInfo
	 *            �洢
	 * @param mapTemp
	 *            �ڵ��Ӧ��ϵ
	 */
	protected void trimEquipElement(LineGraphInfo lineInfo,
			HashMap<String, String> mapTemp) {
		String beginEquipID = null, endEquipID = null;
		boolean type = false;
		// Ѱ����·���˵�
		Iterator<String> keyIterator = mapTemp.keySet().iterator();
		while (keyIterator.hasNext()) {
			String equipID = keyIterator.next();
			if (!mapTemp.containsValue(equipID)) {
				if (beginEquipID == null)
					beginEquipID = equipID;
				else {
					endEquipID = equipID;
					type = true;
				}
			}
		}

		if (endEquipID == null) {
			// ֻ��һ���˵㱻����
			Iterator<String> valueIterator = mapTemp.values().iterator();
			while (valueIterator.hasNext()) {
				String equipID = valueIterator.next();
				if (!mapTemp.containsKey(equipID)) {
					endEquipID = equipID;
					break;
				}
			}
		}
		// ��˳����������
		String equipID = "";
		while (beginEquipID != null) {
			lineInfo.getEquipList().add(beginEquipID);
			beginEquipID = mapTemp.get(beginEquipID);
			// beginEquipID = equipID;
		}
		if (type) {
			lineInfo.getEquipList().add(endEquipID);
			equipID = mapTemp.get(endEquipID);
			lineInfo.getEquipList().add(equipID);
			endEquipID = equipID;
		}

	}

	/**
	 * У��������֮��� ���ӹ�ϵ
	 * 
	 * @param lineInfo
	 *            ��·����
	 * @param mapTemp
	 * @param p0
	 *            �ڵ�0���
	 * @param p1
	 *            �ڵ�1���
	 */
	protected void calEquipElement(LineGraphInfo lineInfo,
			HashMap<String, String> mapTemp, String p0, String p1) {
		Element el0 = handle.getCanvas().getDocument().getElementById(p0);
		Element el1 = handle.getCanvas().getDocument().getElementById(p1);
		if (el0 == null || el1 == null)
			return;
		// ��ͼԪ�ڵ�ֱ�ӷ���
		if (!(el0 instanceof SVGOMUseElement)
				|| !(el1 instanceof SVGOMUseElement))
			return;

		SVGOMUseElement useEl0 = (SVGOMUseElement) el0;
		SVGOMUseElement useEl1 = (SVGOMUseElement) el1;
		if (!isEquipOfLine(useEl0) && isEquipOfLine(useEl1)) {
			// p0Ϊ�������豸,p1Ϊ�����豸
			lineInfo.appendRelaEquip(p1, p0);
		} else if (!isEquipOfLine(useEl1) && isEquipOfLine(useEl0)) {
			// p1Ϊ�������豸,p0Ϊ�����豸
			lineInfo.appendRelaEquip(p0, p1);
		} else if (isEquipOfLine(useEl1) && isEquipOfLine(useEl0)) {
			if (!isEquipOnLine(lineInfo, useEl0)
					&& !isEquipOnLine(lineInfo, useEl1)) {
				// ����ͬ����·,��ͬ���漰���豸һ�����ͬ����·��
				LineGraphInfo otherLine = topology
						.getLineInfoByLineElement((Element) useEl0
								.getParentNode());
				int begin = otherLine.getEquipList().indexOf(p0);
				int end = otherLine.getEquipList().indexOf(p1);
				if (begin < end) {
					for (int i = begin; i <= end - 1; i++) {
						String e0 = otherLine.getEquipList().get(i);
						String e1 = otherLine.getEquipList().get(i + 1);
						if (mapTemp.get(e0) != null) {
							if (!mapTemp.get(e0).equals(11))
								mapTemp.put(e1, e0);
						} else {
							mapTemp.put(e0, e1);
						}
					}
				} else {
					for (int i = begin; i >= end + 1; i--) {
						String e0 = otherLine.getEquipList().get(i);
						String e1 = otherLine.getEquipList().get(i - 1);
						if (mapTemp.get(e0) != null) {
							if (!mapTemp.get(e0).equals(11))
								mapTemp.put(e1, e0);
						} else {
							mapTemp.put(e0, e1);
						}
					}
				}
			} else {
				if (mapTemp.get(p0) != null) {
					if (!mapTemp.get(p0).equals(p1))
						mapTemp.put(p1, p0);
				} else {
					mapTemp.put(p0, p1);
				}
			}
		}
	}

	/**
	 * �����豸�ж�
	 * 
	 * @param el
	 *            ���жϵ�ͼԪ
	 * @return ��Ϊ�����豸�򷵻�true�����򷵻�false
	 */
	protected boolean isEquipOfLine(SVGOMUseElement el) {
		String href = el.getHref().getBaseVal();
		if (href.indexOf("#" + DistrictTopology.TOWER
				+ Constants.SYMBOL_STATUS_SEP) == 0
				|| href.indexOf("#" + DistrictTopology.STRAPPING_TABLE
						+ Constants.SYMBOL_STATUS_SEP) == 0)
			return true;
		return false;
	}

	/**
	 * ����ͼԪ��ź���·���ݣ��ж��豸�Ƿ�����·��
	 * 
	 * @param lineInfo
	 *            ��·����
	 * @param equipID
	 *            �豸ͼԪ���
	 * @return �����Ϸ���true�������򷵻�false
	 */
	protected boolean isEquipOnLine(LineGraphInfo lineInfo, String equipID) {
		Element el = handle.getCanvas().getDocument().getElementById(equipID);
		return isEquipOnLine(lineInfo, el);
	}

	/**
	 * ����ͼԪ�ڵ����·���ݣ��ж��豸�Ƿ�����·��
	 * 
	 * @param lineInfo
	 *            ��·����
	 * @param el
	 *            �豸ͼԪ���
	 * @return �������򷵻�true�������򷵻�false
	 */
	protected boolean isEquipOnLine(LineGraphInfo lineInfo, Element el) {
		if (lineInfo == null || el == null || lineInfo.getLineElement() == null)
			return false;

		if (el.getParentNode().equals(lineInfo.getLineElement()))
			return true;
		return false;
	}

	@Override
	public MouseListener getTreeMouseListener() {
		// TODO Auto-generated method stub
		// add by yux,2009-4-7
		return null;
	}

	@Override
	public DefaultTreeCellRenderer getTreeCellRenderer() {
		// TODO Auto-generated method stub
		// add by yux,2009-4-7
		return new DistrcitTreeCellRenderer();
	}

	@Override
	public Object getTopologyObject() {
		// ���˹�ϵδ����,��ֱ�ӷ���null
		if (!topology.isTopologyCreated())
			return null;
		return topology;
	}

	@Override
	public Element createElementAsGraphUnit(Rectangle2D bounds, Document doc,
			Document symboldoc, String name, String symbolID,
			GraphUnitImageShape shape) {
		// TODO Auto-generated method stub
		// add by yux,2009-4-9
		return null;
	}

	@Override
	public Element createElementAsTemplate(Rectangle2D bounds, Document doc,
			Document symboldoc, String name, String symbolID,
			GraphUnitImageShape shape) {
		// TODO Auto-generated method stub
		// add by yux,2009-4-9
		return null;
	}

	@Override
	public Cursor getCursor(Point2D point) {
		// TODO Auto-generated method stub
		// add by yux,2009-4-9
		return null;
	}
}
