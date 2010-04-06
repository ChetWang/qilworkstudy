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
 * ���⣺ReadData.java
 * </p>
 * <p>
 * ��������ԭʼ���ݶ��������ڴ�ṹ��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-03-03
 * @version 1.0
 */
public class ReadData {
	/**
	 * ���յ���ԭʼ�����ַ���
	 */
	private String dataString;
	/**
	 * ̨������
	 */
	private ArrayList areaList;

	/**
	 * ���캯��
	 * 
	 * @param dataString:String:ԭʼ�����ַ���(XML)
	 */
	public ReadData(String dataString) {
		this.dataString = dataString;
		this.areaList = new ArrayList();

		preTreateData();
	}

	/**
	 * ����ԭʼ���ݣ��������豸����
	 */
	private void preTreateData() {
		Document document = XmlUtil.getXMLDocumentByString(this.dataString);
		document.getDocumentElement().normalize();

		Node root = null; // ���ڵ�

		// *********
		// ��ȡ���ڵ�
		// *********
		NodeList rootNodeList = document.getElementsByTagName("NCI");
		if (rootNodeList.getLength() <= 0)
			return;
		root = rootNodeList.item(0);

		NodeList areaNodeList = root.getChildNodes(); // ��ȡ����ڵ�
		Area area = null;
		for (int i = 0, areaSize = areaNodeList.getLength(); i < areaSize; i++) {
			Node areaNode = areaNodeList.item(i);
			if (!areaNode.getNodeName().equals("Area"))
				continue;

			// ��ȡ̨��������Ϣ
			area = readAreaProperties(areaNode);
			// area = new Area();
			// readProperty(areaNode, area);

			NodeList devideNodeList = areaNode.getChildNodes();
			for (int j = 0, devSize = devideNodeList.getLength(); j < devSize; j++) {
				Node devNode = devideNodeList.item(j);
				if (devNode.getNodeName().equals("DsTransformer")) {
					// ��ѹ��
					area.setTransformer(readTransformerProperties(devNode));
				}
				if (devNode.getNodeName().equals("Lines")) {
					// ��·����
					area.setLinesList(preTreateLines(devNode));
				}
			}
			areaList.add(area);
		}
	}

	/**
	 * ����·�ڵ��л�ȡ��·�������
	 * 
	 * @param devNode:Node:��·�ڵ�
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
			// ��ȡ��·������Ϣ
			readProperty(lineNode, line);

			// ��ȡ��·�¸�����Ϣ
			NodeList polelist = lineNode.getChildNodes();
			for (int j = 0, pSize = polelist.getLength(); j < pSize; j++) {
				Node poleNode = polelist.item(j);
				if (!poleNode.getNodeName().equals("Poles"))
					continue;
				line.setPoles(preTreatePoles(poleNode));
			}

			// �����·����
			String code = (String) line.getProperty("code");
			if (code != null && code.length() > 0)
				lineHashMap.put(code, line);
		}
		return lineHashMap;
	}

	/**
	 * ����������ڵ��л�ȡ��������������
	 * 
	 * @param devNode:Node:�����ڵ�
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
			// ��ȡ����������Ϣ
			readProperty(poleNode, pole);
			// ��Ӹ������󵽸�������
			polelist.add(pole);
		}
		polelist.trimToSize();
		return polelist;
	}

	/**
	 * �ӱ�ѹ���ڵ��л�ȡ��ѹ�����������Ϣ
	 * 
	 * @param devNode:Node:��ѹ���ڵ�
	 * @return
	 */
	private AreaTransformer readTransformerProperties(Node devNode) {
		AreaTransformer transformer = new AreaTransformer();
		readProperty(devNode, transformer);
		return transformer;
	}

	/**
	 * ��̨���ڵ��л�ȡ̨��������Ϣ
	 * 
	 * @param areaNode:Node:����̨��������Ϣ��Node�ڵ�
	 * @return
	 */
	private Area readAreaProperties(Node areaNode) {
		Area area = new Area();
		readProperty(areaNode, area);
		return area;
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
	 * ��ȡԭʼ����
	 * 
	 * @return String
	 */
	public String getDataString() {
		return dataString;
	}

	/**
	 * ��ȡ̨������
	 * 
	 * @return ArrayList
	 */
	public ArrayList getAreaList() {
		areaList.trimToSize();
		return areaList;
	}
}
