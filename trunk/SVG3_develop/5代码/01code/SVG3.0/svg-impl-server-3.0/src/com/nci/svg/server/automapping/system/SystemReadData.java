package com.nci.svg.server.automapping.system;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.server.automapping.comm.BasicProperty;
import com.nci.svg.server.util.XmlUtil;

/**
 * <p>
 * 标题：SystemReadData.java
 * </p>
 * <p>
 * 描述： 系统图自动成图，获取成图数据
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-7-13
 * @version 1.0
 */
public class SystemReadData {
	/**
	 * 数据字符串
	 */
	private String dataXml;
	/**
	 * 厂站编号队列
	 */
	private ArrayList stationIds;
	/**
	 * 厂站集
	 */
	private HashMap stationMap;
	/**
	 * 线路集
	 */
	private ArrayList lineList;

	/**
	 * 构造函数
	 * 
	 * @param dataXml
	 */
	public SystemReadData(String dataXml) {
		this.dataXml = dataXml;
		stationIds = new ArrayList();
		lineList = new ArrayList();
		stationMap = new HashMap();

		init();
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @功能 初始化函数
	 */
	private void init() {
		Document doc = XmlUtil.getXMLDocumentByString(dataXml);
		doc.getDocumentElement().normalize();

		Node root = null; // 根节点

		// *********
		// 获取根节点
		// *********
		NodeList rootNodeList = doc.getElementsByTagName("Datas");
		if (rootNodeList.getLength() <= 0)
			return;
		root = rootNodeList.item(0);

		NodeList dataNodeList = root.getChildNodes();
		for (int i = 0, size = dataNodeList.getLength(); i < size; i++) {
			Node dataNode = dataNodeList.item(i);
			String nodeName = dataNode.getNodeName();
			if ("Substations".equals(nodeName)) {
				// 获取变电站数据
				ArrayList subList = readSubstationData(dataNode);
				if (subList != null)
					stationIds.addAll(subList);
			} else if ("Power_plants".equals(nodeName)) {
				// 获取发电站数据
				ArrayList powerList = readPowerplantData(dataNode);
				if (powerList != null)
					stationIds.addAll(powerList);
			} else if ("lines".equals(nodeName)) {
				// 获取线路数据
				ArrayList lList = readLineData(dataNode);
				if (lList != null)
					lineList.addAll(lList);
			}
		}
		stationIds.trimToSize();
		lineList.trimToSize();
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @功能 从数据字符串中获取厂站数据
	 * @param substationNode:Node:厂站元素对象
	 */
	private ArrayList readSubstationData(Node substationNode) {
		ArrayList list = new ArrayList();
		NodeList subNodes = substationNode.getChildNodes();
		for (int i = 0, size = subNodes.getLength(); i < size; i++) {
			Node node = subNodes.item(i);
			String nodeName = node.getNodeName();
			if (!"substation".equals(nodeName)) {
				continue;
			}
			SystemSubstation substation = new SystemSubstation();
			NodeList sNodes = node.getChildNodes();
			readProperty(sNodes.item(1), substation);

			substation.setProperty("substationType", "Substation");

			// 增加厂站编号
			String stationId = (String) substation.getProperty("Id");
			list.add(stationId);

			// 增加厂站
			stationMap.put(stationId, substation);
		}
		return list;
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @功能 从数据字符串中获取发电站数据
	 * @param powerNode:Node:发电站元素对象
	 */
	private ArrayList readPowerplantData(Node powerNode) {
		ArrayList list = new ArrayList();
		NodeList subNodes = powerNode.getChildNodes();
		for (int i = 0, size = subNodes.getLength(); i < size; i++) {
			Node node = subNodes.item(i);
			String nodeName = node.getNodeName();
			if (!"power_plant".equals(nodeName)) {
				continue;
			}
			SystemSubstation substation = new SystemSubstation();
			NodeList sNodes = node.getChildNodes();
			readProperty(sNodes.item(1), substation);

			substation.setProperty("substationType", "PowerPlant");
			// 增加厂站编号
			String stationId = (String) substation.getProperty("Id");
			list.add(stationId);

			// 增加厂站
			stationMap.put(stationId, substation);
		}
		return list;
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @功能 从数据字符串中获取线路数据
	 * @param lineNode:Node:线路元素对象
	 */
	private ArrayList readLineData(Node lineNode) {
		ArrayList list = new ArrayList();
		NodeList subNodes = lineNode.getChildNodes();
		for (int i = 0, size = subNodes.getLength(); i < size; i++) {
			Node node = subNodes.item(i);
			String nodeName = node.getNodeName();
			if (!"line".equals(nodeName)) {
				continue;
			}
			SystemLine line = new SystemLine();
			NodeList sNodes = node.getChildNodes();
			readProperty(sNodes.item(1), line);

			list.add(line);
		}
		return list;
	}

	/**
	 * 将指定节点的属性读入到指定对象的基础属性对象内
	 * 
	 * @param areaNode:Node:Node节点
	 * @param bp:BasicProperty:基础属性对象
	 */
	private void readProperty(Node node, BasicProperty bp) {
		NamedNodeMap attr = node.getAttributes();
		for (int i = 0, size = attr.getLength(); i < size; i++) {
			String name = attr.item(i).getNodeName();
			String value = attr.item(i).getNodeValue();
			bp.setProperty(name, value);
		}
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @功能 获取厂站名队列
	 * @return
	 */
	public ArrayList getStationIdList() {
		return stationIds;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 获取厂站集
	 * @return
	 */
	public HashMap getStationMap() {
		return stationMap;
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @功能 获取线路集
	 * @return
	 */
	public ArrayList getLineList() {
		return lineList;
	}

}
