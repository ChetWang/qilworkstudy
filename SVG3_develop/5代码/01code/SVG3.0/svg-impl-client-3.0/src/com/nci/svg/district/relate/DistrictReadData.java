package com.nci.svg.district.relate;

import java.util.HashMap;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.district.relate.bean.DistrictAreaBean;
import com.nci.svg.district.relate.bean.DistrictBoxBean;
import com.nci.svg.district.relate.bean.DistrictLineBean;
import com.nci.svg.district.relate.bean.DistrictPoleBean;
import com.nci.svg.district.relate.bean.DistrictTransformerBean;
import com.nci.svg.sdk.client.util.Utilities;

public class DistrictReadData {
	private HashMap<String, String> lopeHash = new HashMap<String, String>();

	/**
	 * 2009-4-10 Add by ZHM
	 * 
	 * @���� ��ȡ̨������
	 * @param data
	 */
	public DistrictAreaBean readData(String data) {
		Document doc = Utilities.getXMLDocumentByString(data);
		doc.getDocumentElement().normalize();

		Node root = null; // ���ڵ�

		// *********
		// ��ȡ���ڵ�
		// *********
		NodeList rootNodeList = doc.getElementsByTagName("Area");
		if (rootNodeList.getLength() <= 0)
			return null;
		root = rootNodeList.item(0);

		DistrictAreaBean area = new DistrictAreaBean();
		// ��ȡ̨��������Ϣ
		readAreaParam(root, area);

		NodeList deviceList = root.getChildNodes();
		for (int i = 0, size = deviceList.getLength(); i < size; i++) {
			Node device = deviceList.item(i);
			if (device.getNodeName().equals("DsTransformers")) {
				// ��ȡ̨���������Ϣ
				HashMap<String, DistrictTransformerBean> transformers = readTransformer(device);
				area.setTransformers(transformers);
			} else if (device.getNodeName().equals("EquiBoxs")) {
				// ��ȡ�������Ϣ
				HashMap<String, DistrictBoxBean> boxes = readBox(device);
				area.setBoxs(boxes);
			} else if (device.getNodeName().equals("Poles")) {
				// ��ȡ������Ϣ
				HashMap<String, DistrictPoleBean> poles = readPole(device);
				area.setPoles(poles);
			} else if (device.getNodeName().equals("Lines")) {
				// ��ȡ��·��Ϣ
				HashMap<String, DistrictLineBean> lines = readLine(device);
				area.setLine(lines);
			}
		}

		// ��鲢Ԥ����̨������
		if (checkLines(area)) {
			return area;
		} else {
			return null;
		}
	}

	/**
	 * 2009-4-12 Add by ZHM
	 * 
	 * @���� ��鲢Ԥ����̨������·����
	 * @param area
	 */
	private boolean checkLines(DistrictAreaBean area) {
		HashMap<String, DistrictLineBean> lines = area.getLine();
		HashMap<String, DistrictPoleBean> poles = area.getPoles();
		// ��ȡ̨�����
		String areaID = (String) area.getProperties().get("sd_objid");
		for (String lineID : lines.keySet()) {
			DistrictLineBean line = lines.get(lineID);
			// ��ȡ��·����
			String lineType = line.getLineType();
			// ��ȡ�ϼ���·���
			String perLineID = (String) line.getProperties().get("bl_sjxl");
			// �����·�Ƿ�Ϊ֧��
			DistrictLineBean perLine = lines.get(perLineID);
			if (perLine != null) {
				if (lineType.equals(DistrictLineBean.MAIN_LINE)) {
					line.setLineType(DistrictLineBean.LATERAL_LINE);
				}
				perLine.setSubLines(lineID);
			} else if (!perLineID.equals(areaID)) {
				return false;
			}

			// ��ȡ����˱�ţ�֧�����������ߵĸ�����
			String contactID = (String) line.getProperties().get("bl_contact");
			// ��������Ƿ����
			DistrictPoleBean pole = poles.get(contactID);
			if (pole != null) {
				line.setMiddle(true);
			} else if (contactID != null && contactID.length() > 0) {
				return false;
			}

			// ��ȡ֧�Ӹ˱�ţ�����������֧�ߵĸ�����
			String supportID = (String) line.getProperties().get("bl_zjgid");
			// ����·��֧�Ӹ˻������ת���ɸ������
			line.getProperties().put("bl_zjgid", lopeHash.get(supportID));

			// ��ȡ��·������
			LinkedList<String> polesID = line.getPoles();
			// �����·�»�·�����ĸ����Ƿ����
			for (String poleID : polesID) {
				pole = poles.get(poleID);
				if (pole == null)
					return false;
			}
		}
		// ����������·��֧�Ӹ�˳��ż��㲢д������·������
		for (String lineID : lines.keySet()) {
			DistrictLineBean line = lines.get(lineID);
			checkLinesPosition(lines, line);
		}
		return true;
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @���� �ݹ鴦��ָ����·��֧�Ӹ�˳���
	 * @param lines
	 * @param line
	 */
	private void checkLinesPosition(HashMap<String, DistrictLineBean> lines,
			DistrictLineBean line) {
		String position = line.getPosition();
		if (position == null || position.length() <= 0) {
			// �����·�Ƿ���֧��
			LinkedList<String> subLineIDs = line.getSubLines();
			for (int i = 0, size = subLineIDs.size(); i < size; i++) {
				DistrictLineBean subLine = lines.get(subLineIDs.get(i));
				String positionID = (String) subLine.getProperties().get(
						"bl_zjgid");
				// ��ȡ֧�Ӹ�˳���
				position = getSubLinePosition(line, positionID);
				subLine.setPosition(position);
				// �ݹ鴦������·��֧�Ӹ�
				checkLinesPosition(lines, subLine);
			}
		}
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @���� ��ȡָ����·��ָ��������˳���
	 * @param line
	 * @param positionID
	 * @return
	 */
	private String getSubLinePosition(DistrictLineBean line, String positionID) {
		LinkedList<String> poles = line.getPoles();
		for (int i = 0, size = poles.size(); i < size; i++) {
			String poleID = poles.get(i);
			if (poleID.equals(positionID)) {
				return Integer.toString(i);
			}
		}
		return null;
	}

	/**
	 * 2009-4-10 Add by ZHM
	 * 
	 * @���� ��ȡ��·��Ϣ
	 * @param lineNode
	 * @return
	 */
	private HashMap<String, DistrictLineBean> readLine(Node lineNode) {
		HashMap<String, DistrictLineBean> lineHash = new HashMap<String, DistrictLineBean>();
		NodeList lineNodes = lineNode.getChildNodes();
		DistrictLineBean lineBean = null;
		LinkedList<String> lopes = null;
		for (int i = 0, size = lineNodes.getLength(); i < size; i++) {
			Node line = lineNodes.item(i);
			String lineNodeName = line.getNodeName();
			if (lineNodeName.equals("line")) {
				// *************
				// ��ȡ֧������
				// *************
				lineBean = new DistrictLineBean();
//				if (!lineNodeName.equals("line")) {
//					lineBean.setLineType(DistrictLineBean.SERVICE_LINE);
//					// lineBean.setLineType(DistrictLineBean.LATERAL_LINE);
//				}
				readProperty(line, lineBean);

				NodeList lopeList = line.getChildNodes();
				for (int j = 0, jSize = lopeList.getLength(); j < jSize; j++) {
					Node lope = lopeList.item(j);
					if (lope.getNodeName().equals("Lopes")) {
						lopes = readLope(lope);
						String contactId = (String) lineBean.getProperties()
								.get("bl_contact");
						// ������˻�·���ת���ɸ������
						lineBean.getProperties().put("bl_contact",
								lopeHash.get(contactId));
					}
				}
				lineBean.setPoles(lopes);

				String id = (String) lineBean.getProperties().get("bl_objid");
				lineHash.put(id, lineBean);
			} else if (lineNodeName.equals("ServiceLine")) {
				// ************************************
				// ��ȡ�ӻ�������,�����ӻ�������ת����֧������
				// ҵ������ȶ��㷨��ֻ֧��֧��
				// ************************************
			}
		}
		return lineHash;
	}

	/**
	 * 2009-4-12 Add by ZHM
	 * 
	 * @���� ��ȡ��·�¸�����Ϣ
	 * @param lope
	 * @return
	 */
	private LinkedList<String> readLope(Node lope) {
		LinkedList<String> poleList = new LinkedList<String>();
		NodeList lopeNodes = lope.getChildNodes();
		for (int i = 0, size = lopeNodes.getLength(); i < size; i++) {
			Node lopes = lopeNodes.item(i);
			if (lopes.getNodeName().equals("lope")) {
				NamedNodeMap attr = lopes.getAttributes();
				String[] lopeTemp = new String[2];
				for (int j = 0, jsize = attr.getLength(); j < jsize; j++) {
					String name = attr.item(j).getNodeName();
					String value = attr.item(j).getNodeValue();
					if (name.equals("sp_id")) {
						lopeTemp[0] = value;
					} else if (name.equals("poleid")) {
						lopeTemp[1] = value;
						poleList.add(value);
					}
				}
				lopeHash.put(lopeTemp[0], lopeTemp[1]);
			}
		}
		return poleList;
	}

	/**
	 * 2009-4-10 Add by ZHM
	 * 
	 * @���� ��ȡ������Ϣ
	 * @param pole
	 * @return
	 */
	private HashMap<String, DistrictPoleBean> readPole(Node pole) {
		NodeList poleNodes = pole.getChildNodes();
		HashMap<String, DistrictPoleBean> poleHash = new HashMap<String, DistrictPoleBean>();
		DistrictPoleBean poleBean = null;
		for (int i = 0, size = poleNodes.getLength(); i < size; i++) {
			Node poles = poleNodes.item(i);
			if (poles.getNodeName().equals("pole")) {
				poleBean = new DistrictPoleBean();
				readProperty(poles, poleBean);
				String id = (String) poleBean.getProperties().get("p_objid");
				poleHash.put(id, poleBean);
			}
		}
		return poleHash;
	}

	/**
	 * 2009-4-10 Add by ZHM
	 * 
	 * @���� ��ȡ�������Ϣ
	 * @param box
	 * @return
	 */
	private HashMap<String, DistrictBoxBean> readBox(Node box) {
		NodeList boxNodes = box.getChildNodes();
		HashMap<String, DistrictBoxBean> boxHash = new HashMap<String, DistrictBoxBean>();
		DistrictBoxBean boxBean = null;
		for (int i = 0, size = boxNodes.getLength(); i < size; i++) {
			Node boxes = boxNodes.item(i);
			if (boxes.getNodeName().equals("EquiBox")) {
				boxBean = new DistrictBoxBean();
				readProperty(boxes, boxBean);
				String id = (String) boxBean.getProperties().get("sl_objid");
				boxHash.put(id, boxBean);
			}
		}
		return boxHash;
	}

	/**
	 * 2009-4-10 Add by ZHM
	 * 
	 * @���� ��ȡ�����Ϣ
	 * @param root
	 * @param area
	 */
	private HashMap<String, DistrictTransformerBean> readTransformer(Node tran) {
		NodeList transNodes = tran.getChildNodes();
		HashMap<String, DistrictTransformerBean> tranHash = new HashMap<String, DistrictTransformerBean>();
		DistrictTransformerBean tranBean = null;
		for (int i = 0, size = transNodes.getLength(); i < size; i++) {
			Node trans = transNodes.item(i);
			if (trans.getNodeName().equals("DsTransformer")) {
				tranBean = new DistrictTransformerBean();
				readProperty(trans, tranBean);
				String id = (String) tranBean.getProperties().get("sd_objid");
				tranHash.put(id, tranBean);
			}
		}
		return tranHash;
	}

	/**
	 * 2009-4-10 Add by ZHM
	 * 
	 * @���� ��ȡ̨��������Ϣ
	 * @param root
	 */
	private void readAreaParam(Node root, DistrictAreaBean area) {
		readProperty(root, area);
	}

	/**
	 * 2009-4-10 Add by ZHM
	 * 
	 * @���� ��ָ���ڵ�����Զ��뵽ָ������Ļ������Զ�����
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

}
