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
	 * @功能 读取台区数据
	 * @param data
	 */
	public DistrictAreaBean readData(String data) {
		Document doc = Utilities.getXMLDocumentByString(data);
		doc.getDocumentElement().normalize();

		Node root = null; // 根节点

		// *********
		// 获取根节点
		// *********
		NodeList rootNodeList = doc.getElementsByTagName("Area");
		if (rootNodeList.getLength() <= 0)
			return null;
		root = rootNodeList.item(0);

		DistrictAreaBean area = new DistrictAreaBean();
		// 获取台区基本信息
		readAreaParam(root, area);

		NodeList deviceList = root.getChildNodes();
		for (int i = 0, size = deviceList.getLength(); i < size; i++) {
			Node device = deviceList.item(i);
			if (device.getNodeName().equals("DsTransformers")) {
				// 获取台区下配变信息
				HashMap<String, DistrictTransformerBean> transformers = readTransformer(device);
				area.setTransformers(transformers);
			} else if (device.getNodeName().equals("EquiBoxs")) {
				// 获取配变箱信息
				HashMap<String, DistrictBoxBean> boxes = readBox(device);
				area.setBoxs(boxes);
			} else if (device.getNodeName().equals("Poles")) {
				// 获取杆塔信息
				HashMap<String, DistrictPoleBean> poles = readPole(device);
				area.setPoles(poles);
			} else if (device.getNodeName().equals("Lines")) {
				// 获取线路信息
				HashMap<String, DistrictLineBean> lines = readLine(device);
				area.setLine(lines);
			}
		}

		// 检查并预处理台区数据
		if (checkLines(area)) {
			return area;
		} else {
			return null;
		}
	}

	/**
	 * 2009-4-12 Add by ZHM
	 * 
	 * @功能 检查并预处理台区下线路数据
	 * @param area
	 */
	private boolean checkLines(DistrictAreaBean area) {
		HashMap<String, DistrictLineBean> lines = area.getLine();
		HashMap<String, DistrictPoleBean> poles = area.getPoles();
		// 获取台区编号
		String areaID = (String) area.getProperties().get("sd_objid");
		for (String lineID : lines.keySet()) {
			DistrictLineBean line = lines.get(lineID);
			// 获取线路类型
			String lineType = line.getLineType();
			// 获取上级线路编号
			String perLineID = (String) line.getProperties().get("bl_sjxl");
			// 检查线路是否为支线
			DistrictLineBean perLine = lines.get(perLineID);
			if (perLine != null) {
				if (lineType.equals(DistrictLineBean.MAIN_LINE)) {
					line.setLineType(DistrictLineBean.LATERAL_LINE);
				}
				perLine.setSubLines(lineID);
			} else if (!perLineID.equals(areaID)) {
				return false;
			}

			// 获取接入杆编号（支线上连接主线的杆塔）
			String contactID = (String) line.getProperties().get("bl_contact");
			// 检查接入杆是否存在
			DistrictPoleBean pole = poles.get(contactID);
			if (pole != null) {
				line.setMiddle(true);
			} else if (contactID != null && contactID.length() > 0) {
				return false;
			}

			// 获取支接杆编号（主线上连接支线的杆塔）
			String supportID = (String) line.getProperties().get("bl_zjgid");
			// 将线路的支接杆回来编号转换成杆塔编号
			line.getProperties().put("bl_zjgid", lopeHash.get(supportID));

			// 获取线路杆塔组
			LinkedList<String> polesID = line.getPoles();
			// 检查线路下回路关联的杆塔是否存在
			for (String poleID : polesID) {
				pole = poles.get(poleID);
				if (pole == null)
					return false;
			}
		}
		// 将所有子线路的支接杆顺序号计算并写入子线路对象中
		for (String lineID : lines.keySet()) {
			DistrictLineBean line = lines.get(lineID);
			checkLinesPosition(lines, line);
		}
		return true;
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @功能 递归处理指定线路的支接杆顺序号
	 * @param lines
	 * @param line
	 */
	private void checkLinesPosition(HashMap<String, DistrictLineBean> lines,
			DistrictLineBean line) {
		String position = line.getPosition();
		if (position == null || position.length() <= 0) {
			// 检查线路是否有支线
			LinkedList<String> subLineIDs = line.getSubLines();
			for (int i = 0, size = subLineIDs.size(); i < size; i++) {
				DistrictLineBean subLine = lines.get(subLineIDs.get(i));
				String positionID = (String) subLine.getProperties().get(
						"bl_zjgid");
				// 获取支接杆顺序号
				position = getSubLinePosition(line, positionID);
				subLine.setPosition(position);
				// 递归处理子线路的支接杆
				checkLinesPosition(lines, subLine);
			}
		}
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @功能 获取指定线路上指定杆塔的顺序号
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
	 * @功能 获取线路信息
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
				// 读取支线数据
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
						// 将接入杆回路编号转换成杆塔编号
						lineBean.getProperties().put("bl_contact",
								lopeHash.get(contactId));
					}
				}
				lineBean.setPoles(lopes);

				String id = (String) lineBean.getProperties().get("bl_objid");
				lineHash.put(id, lineBean);
			} else if (lineNodeName.equals("ServiceLine")) {
				// ************************************
				// 读取接户线数据,并将接户线数据转换成支线数据
				// 业务关联比对算法，只支持支线
				// ************************************
			}
		}
		return lineHash;
	}

	/**
	 * 2009-4-12 Add by ZHM
	 * 
	 * @功能 读取线路下杆塔信息
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
	 * @功能 获取杆塔信息
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
	 * @功能 获取配变箱信息
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
	 * @功能 获取配变信息
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
	 * @功能 获取台区基本信息
	 * @param root
	 */
	private void readAreaParam(Node root, DistrictAreaBean area) {
		readProperty(root, area);
	}

	/**
	 * 2009-4-10 Add by ZHM
	 * 
	 * @功能 将指定节点的属性读入到指定对象的基础属性对象内
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

}
