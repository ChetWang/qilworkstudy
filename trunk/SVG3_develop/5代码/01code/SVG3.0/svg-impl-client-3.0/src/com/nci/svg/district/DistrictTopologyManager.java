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
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2009-3-27
 * @功能：台区图拓扑管理类
 * 
 */
public class DistrictTopologyManager extends TopologyManagerAdapter {

	/**
	 * add by yux,2009-4-9 台区拓扑对象
	 */
	protected DistrictTopology topology = new DistrictTopology();
	/**
	 * add by yux,2009-4-9 拓扑树根节点
	 */
	private DefaultMutableTreeNode root = null;
	/**
	 * add by yux,2009-4-9 拓扑树model
	 */
	private DefaultTreeModel treeModel = null;
	/**
	 * 配变节点
	 */
	private Element transformerElement = null;
	/**
	 * 配变箱节点
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
			// 图形拓扑构建成功，创建模型树
			TreeNodeBean nodeBean = new TreeNodeBean("总览", handle.getCanvas()
					.getDocument().getDocumentElement());
			root = new DefaultMutableTreeNode(nodeBean);
			// // 增加配变节点
			// {
			// TreeNodeBean bean = new TreeNodeBean(TRANSFORMER,
			// transformerElement);
			// DefaultMutableTreeNode node = new DefaultMutableTreeNode(bean);
			// root.add(node);
			// }
			//
			// // 增加配电柜节点
			// {
			// TreeNodeBean bean = new TreeNodeBean(DISTRIBUTIONBOX,
			// distributionBoxElement);
			// DefaultMutableTreeNode node = new DefaultMutableTreeNode(bean);
			// root.add(node);
			// }

			LinkedList<LineGraphInfo> lateralLines = new LinkedList<LineGraphInfo>();
			// 增加干线
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

			// 增加支线，需要不断遍历，将父线路存在的子线路加入
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

			// 增加接户线
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

			// 将业务模型关联标志复位
			topology.setRelated(false);
			return treeModel;

		}
		return null;
	}

	/**
	 * 将线路数据中涉及的设备图元插入树节点
	 * 
	 * @param parentnode:父树节点
	 * @param line:线路数据
	 * @param mapNode:树节点映射表
	 * @param beginID:起始点编号
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
	 * 寻找线路的父设备编号
	 * 
	 * @param line:线路数据
	 * @return 如存在则返回父设备图元编号
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
		// 创建图形拓扑
		Document doc = handle.getCanvas().getDocument();
		// 寻找单一的配变
		if (transformerElement == null
				|| !transformerElement.getOwnerDocument().equals(doc)) {
			transformerElement = findTransformer(doc, false);
			if (transformerElement != null) {
				topology.setTransformerElement(transformerElement);
			}
		}
		// // 寻找单一的配电柜
		if (distributionBoxElement == null
				|| !distributionBoxElement.getOwnerDocument().equals(doc)) {
			distributionBoxElement = findDistributionBox(doc, false);
			if (distributionBoxElement != null) {
				topology.setDistributionBoxElement(distributionBoxElement);
			}
		}
		// 校验配变与配电柜关联关系
		//
		// boolean relaFlag = isTransRelaBox(doc, false);
		// if (!relaFlag)
		// return false;
		// 遍历所有的干线、支线、接户线
		findLine(doc);

		topology.setTopologyCreated(true);
		return true;
	}

	/**
	 * 寻找配变节点
	 * 
	 * @return 如存在则返回配变节点，不存在则返回null
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
				editor.getSvgSession().showMessageBox("配变适配", "无配变");
			return null;
		}

		if (list.getLength() > 1) {
			if (showFlag)
				editor.getSvgSession().showMessageBox("配变适配", "配变数量大于1");
			return null;
		}
		return (Element) list.item(0);
	}

	/**
	 * 寻找配电柜节点
	 * 
	 * @return 如存在则返回配电柜节点，不存在则返回null
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
				editor.getSvgSession().showMessageBox("配电柜适配", "无配电柜");
			return null;
		}

		if (list.getLength() > 1) {
			if (showFlag)
				editor.getSvgSession().showMessageBox("配电柜适配", "配电柜数量大于1");
			return null;
		}
		return (Element) list.item(0);
	}

	/**
	 * 校验配变与配电柜是否相连
	 * 
	 * @return 相连则返回true，不相连则返回false
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
				editor.getSvgSession().showMessageBox("配电柜适配", "无配电柜");
			return false;
		}

		return true;
	}

	/**
	 * 根据doc寻找符合业务的干线、支线和接户线
	 * 
	 * @param doc
	 *            待分析的doc对象
	 */
	protected void findLine(Document doc) {
		// 将线路中的线路名称暂时保留
		HashMap<String, String> lineNames = new LinkedHashMap<String, String>();
		// 将线路下的设备名字保留<线路编号,<设备编号,设备名称>>
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
	 * 根据传入的节点对应关系，梳理线上设备顺序
	 * 
	 * @param lineInfo
	 *            存储
	 * @param mapTemp
	 *            节点对应关系
	 */
	protected void trimEquipElement(LineGraphInfo lineInfo,
			HashMap<String, String> mapTemp) {
		String beginEquipID = null, endEquipID = null;
		boolean type = false;
		// 寻找线路两端点
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
			// 只有一个端点被捕获
			Iterator<String> valueIterator = mapTemp.values().iterator();
			while (valueIterator.hasNext()) {
				String equipID = valueIterator.next();
				if (!mapTemp.containsKey(equipID)) {
					endEquipID = equipID;
					break;
				}
			}
		}
		// 按顺序放入队列中
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
	 * 校验两连接之间的 连接关系
	 * 
	 * @param lineInfo
	 *            线路数据
	 * @param mapTemp
	 * @param p0
	 *            节点0编号
	 * @param p1
	 *            节点1编号
	 */
	protected void calEquipElement(LineGraphInfo lineInfo,
			HashMap<String, String> mapTemp, String p0, String p1) {
		Element el0 = handle.getCanvas().getDocument().getElementById(p0);
		Element el1 = handle.getCanvas().getDocument().getElementById(p1);
		if (el0 == null || el1 == null)
			return;
		// 非图元节点直接返回
		if (!(el0 instanceof SVGOMUseElement)
				|| !(el1 instanceof SVGOMUseElement))
			return;

		SVGOMUseElement useEl0 = (SVGOMUseElement) el0;
		SVGOMUseElement useEl1 = (SVGOMUseElement) el1;
		if (!isEquipOfLine(useEl0) && isEquipOfLine(useEl1)) {
			// p0为非线上设备,p1为线上设备
			lineInfo.appendRelaEquip(p1, p0);
		} else if (!isEquipOfLine(useEl1) && isEquipOfLine(useEl0)) {
			// p1为非线上设备,p0为线上设备
			lineInfo.appendRelaEquip(p0, p1);
		} else if (isEquipOfLine(useEl1) && isEquipOfLine(useEl0)) {
			if (!isEquipOnLine(lineInfo, useEl0)
					&& !isEquipOnLine(lineInfo, useEl1)) {
				// 处理同回线路,将同回涉及的设备一起加入同回线路中
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
	 * 线上设备判断
	 * 
	 * @param el
	 *            待判断的图元
	 * @return 如为线上设备则返回true，否则返回false
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
	 * 根据图元编号和线路数据，判断设备是否在线路上
	 * 
	 * @param lineInfo
	 *            线路数据
	 * @param equipID
	 *            设备图元编号
	 * @return 在线上返回true，不在则返回false
	 */
	protected boolean isEquipOnLine(LineGraphInfo lineInfo, String equipID) {
		Element el = handle.getCanvas().getDocument().getElementById(equipID);
		return isEquipOnLine(lineInfo, el);
	}

	/**
	 * 根据图元节点和线路数据，判断设备是否在线路上
	 * 
	 * @param lineInfo
	 *            线路数据
	 * @param el
	 *            设备图元编号
	 * @return 在线上则返回true，不在则返回false
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
		// 拓扑关系未建立,则直接返回null
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
