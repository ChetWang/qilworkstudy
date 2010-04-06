package com.nci.svg.server.automapping.area;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.server.automapping.comm.BasicProperty;
import com.nci.svg.server.util.XmlUtil;

/**
 * <p>
 * 标题：ReadData.java
 * </p>
 * <p>
 * 描述：将原始数据读出放入内存结构中
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-03-03
 * @version 1.0
 */
public class ReadData {
	/**
	 * 接收到的原始数据字符串
	 */
	private String dataString;
	/**
	 * 台区队列
	 */
	private ArrayList areaList;

	/**
	 * 构造函数
	 * 
	 * @param dataString:String:原始数据字符串(XML)
	 */
	public ReadData(String dataString) {
		this.dataString = dataString;
		this.areaList = new ArrayList();

		preTreateData();
	}

	/**
	 * 分析原始数据，并生成设备队列
	 */
	private void preTreateData() {
		Document document = XmlUtil.getXMLDocumentByString(this.dataString);
		document.getDocumentElement().normalize();

		Node root = null; // 根节点

		// *********
		// 获取根节点
		// *********
		NodeList rootNodeList = document.getElementsByTagName("NCI");
		if (rootNodeList.getLength() <= 0)
			return;
		root = rootNodeList.item(0);

		NodeList areaNodeList = root.getChildNodes(); // 获取区域节点
		Area area = null;
		for (int i = 0, areaSize = areaNodeList.getLength(); i < areaSize; i++) {
			Node areaNode = areaNodeList.item(i);
			if (!areaNode.getNodeName().equals("Area"))
				continue;

			// 获取台区基本信息
			area = readAreaProperties(areaNode);
			// area = new Area();
			// readProperty(areaNode, area);

			NodeList devideNodeList = areaNode.getChildNodes();
			for (int j = 0, devSize = devideNodeList.getLength(); j < devSize; j++) {
				Node devNode = devideNodeList.item(j);
				if (devNode.getNodeName().equals("DsTransformer")) {
					// 变压器
					area.setTransformer(readTransformerProperties(devNode));
				}
				if (devNode.getNodeName().equals("Lines")) {
					// 线路队列
					area.setLinesList(preTreateLines(devNode));
				}
			}
			areaList.add(area);
		}
	}

	/**
	 * 从线路节点中获取线路对象队列
	 * 
	 * @param devNode:Node:线路节点
	 * @return
	 */
	private LinkedHashMap preTreateLines(Node devNode) {
		LinkedHashMap lineHashMap = new LinkedHashMap();
		NodeList nodelist = devNode.getChildNodes();
		AreaLine line = null;
		for (int i = 0, size = nodelist.getLength(); i < size; i++) {
			Node lineNode = nodelist.item(i);
			if (!lineNode.getNodeName().equals("Line"))
				continue;
			line = new AreaLine();
			// 读取线路属性信息
			readProperty(lineNode, line);

			// 读取线路下杆塔信息
			NodeList polelist = lineNode.getChildNodes();
			for (int j = 0, pSize = polelist.getLength(); j < pSize; j++) {
				Node poleNode = polelist.item(j);
				if (!poleNode.getNodeName().equals("Poles"))
					continue;
				line.setPoles(preTreatePoles(poleNode));
			}

			// 添加线路对象
			String code = (String) line.getProperty("code");
			if (code != null && code.length() > 0)
				lineHashMap.put(code, line);
		}
		return lineHashMap;
	}

	/**
	 * 从物理杆塔节点中获取物理杆塔对象队列
	 * 
	 * @param devNode:Node:杆塔节点
	 * @return
	 */
	private ArrayList preTreatePoles(Node devNode) {
		ArrayList polelist = new ArrayList();
		NodeList nodelist = devNode.getChildNodes();
		AreaPole pole = null;
		for (int i = 0, size = nodelist.getLength(); i < size; i++) {
			Node poleNode = nodelist.item(i);
			if (!poleNode.getNodeName().equals("Pole"))
				continue;
			pole = new AreaPole();
			// 读取杆塔属性信息
			readProperty(poleNode, pole);
			// 添加杆塔对象到杆塔组中
			polelist.add(pole);
		}
		polelist.trimToSize();
		return polelist;
	}

	/**
	 * 从变压器节点中获取变压器对象基本信息
	 * 
	 * @param devNode:Node:变压器节点
	 * @return
	 */
	private AreaTransformer readTransformerProperties(Node devNode) {
		AreaTransformer transformer = new AreaTransformer();
		readProperty(devNode, transformer);
		return transformer;
	}

	/**
	 * 从台区节点中获取台区基本信息
	 * 
	 * @param areaNode:Node:包含台区基本信息的Node节点
	 * @return
	 */
	private Area readAreaProperties(Node areaNode) {
		Area area = new Area();
		readProperty(areaNode, area);
		return area;
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
	 * 获取原始数据
	 * 
	 * @return String
	 */
	public String getDataString() {
		return dataString;
	}

	/**
	 * 获取台区队列
	 * 
	 * @return ArrayList
	 */
	public ArrayList getAreaList() {
		areaList.trimToSize();
		return areaList;
	}
}
