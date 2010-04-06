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
 * ���⣺SystemReadData.java
 * </p>
 * <p>
 * ������ ϵͳͼ�Զ���ͼ����ȡ��ͼ����
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-7-13
 * @version 1.0
 */
public class SystemReadData {
	/**
	 * �����ַ���
	 */
	private String dataXml;
	/**
	 * ��վ��Ŷ���
	 */
	private ArrayList stationIds;
	/**
	 * ��վ��
	 */
	private HashMap stationMap;
	/**
	 * ��·��
	 */
	private ArrayList lineList;

	/**
	 * ���캯��
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
	 * @���� ��ʼ������
	 */
	private void init() {
		Document doc = XmlUtil.getXMLDocumentByString(dataXml);
		doc.getDocumentElement().normalize();

		Node root = null; // ���ڵ�

		// *********
		// ��ȡ���ڵ�
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
				// ��ȡ���վ����
				ArrayList subList = readSubstationData(dataNode);
				if (subList != null)
					stationIds.addAll(subList);
			} else if ("Power_plants".equals(nodeName)) {
				// ��ȡ����վ����
				ArrayList powerList = readPowerplantData(dataNode);
				if (powerList != null)
					stationIds.addAll(powerList);
			} else if ("lines".equals(nodeName)) {
				// ��ȡ��·����
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
	 * @���� �������ַ����л�ȡ��վ����
	 * @param substationNode:Node:��վԪ�ض���
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

			// ���ӳ�վ���
			String stationId = (String) substation.getProperty("Id");
			list.add(stationId);

			// ���ӳ�վ
			stationMap.put(stationId, substation);
		}
		return list;
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @���� �������ַ����л�ȡ����վ����
	 * @param powerNode:Node:����վԪ�ض���
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
			// ���ӳ�վ���
			String stationId = (String) substation.getProperty("Id");
			list.add(stationId);

			// ���ӳ�վ
			stationMap.put(stationId, substation);
		}
		return list;
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @���� �������ַ����л�ȡ��·����
	 * @param lineNode:Node:��·Ԫ�ض���
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
	 * ��ָ���ڵ�����Զ��뵽ָ������Ļ������Զ�����
	 * 
	 * @param areaNode:Node:Node�ڵ�
	 * @param bp:BasicProperty:�������Զ���
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
	 * @���� ��ȡ��վ������
	 * @return
	 */
	public ArrayList getStationIdList() {
		return stationIds;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @���� ��ȡ��վ��
	 * @return
	 */
	public HashMap getStationMap() {
		return stationMap;
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @���� ��ȡ��·��
	 * @return
	 */
	public ArrayList getLineList() {
		return lineList;
	}

}
