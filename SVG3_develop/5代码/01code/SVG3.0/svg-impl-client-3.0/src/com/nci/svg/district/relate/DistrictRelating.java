package com.nci.svg.district.relate;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.district.DistrictTopology;
import com.nci.svg.district.DistrictTopology.LineGraphInfo;
import com.nci.svg.district.relate.bean.DistrictAreaBean;
import com.nci.svg.district.relate.bean.DistrictLineBean;
import com.nci.svg.district.relate.bean.DistrictPoleBean;
import com.nci.svg.district.relate.bean.DistrictTreeNodeBean;
import com.nci.svg.district.relate.tree.DynamicTreeNode;
import com.nci.svg.district.relate.tree.TreeData;
import com.nci.svg.sdk.bean.ResultBean;

/**
 * <p>
 * 标题：DistrictRelating.java
 * </p>
 * <p>
 * 描述： 台区图自动关联类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-4-14
 * @version 1.0
 */
public class DistrictRelating {
	/**
	 * 业务台区对象
	 */
	private DistrictAreaBean areaBean;
	/**
	 * 图形台区对象
	 */
	private DistrictTreeNodeBean areaNode;
	/**
	 * 拓扑关系对象
	 */
	private DistrictTopology topology;
	/**
	 * 业务数据主线集合
	 */
	private ArrayList<DistrictLineBean> linesMainB;
	/**
	 * 图形上doc节点
	 */
	private Document doc;
	/**
	 * 已关联设备对应表<业务编号,图形编号>
	 */
	private HashMap<String, String> relatedBusi;
	/**
	 * 已关联设备对应表<图形编号,业务编号>
	 */
	private HashMap<String, String> relatedMap;
	/**
	 * 业务模型关联标志
	 */
	private boolean isRelated;

	/**
	 * 构造函数
	 * 
	 * @param areaBean
	 * @param topology
	 * @param doc
	 */
	public DistrictRelating(DistrictAreaBean areaBean,
			DistrictTopology topology, Document doc) {
		this.areaBean = areaBean;
		this.topology = topology;
		this.doc = doc;
		this.relatedBusi = new HashMap<String, String>();
		this.relatedMap = new HashMap<String, String>();
	}

	/**
	 * 2009-5-4 Add by ZHM
	 * 
	 * @功能 获取业务模型关联标志
	 * @return
	 */
	public boolean isRelated() {
		return isRelated;
	}

	/**
	 * 2009-5-4 Add by ZHM
	 * 
	 * @功能 设置业务模型关联标志
	 * @param isRelated
	 */
	public void setRelated(boolean isRelated) {
		this.isRelated = isRelated;
	}

	/**
	 * 2009-4-15 Add by ZHM
	 * 
	 * @功能 检查服务器获取数据和台区图图元进行比对
	 * @return
	 */
	public ResultBean relating() {
		ResultBean rb;
		// 分离图形和业务上线路数据
		rb = initLines();
		if (rb.getReturnFlag() == ResultBean.RETURN_ERROR)
			return rb;
		// 台区基本信息检核
		// rb = checkAreaBasic();
		if (rb != null && rb.getReturnFlag() == ResultBean.RETURN_ERROR)
			return rb;

		// *****************
		// 检查所有主线是否相等
		// *****************
		int size = linesMainB.size();
		// 业务主线路与图形主线相等标志
		boolean[] equalFlag = new boolean[size];
		// 初始化相等标志
		for (int j = 0; j < size; j++) {
			equalFlag[j] = false;
		}
		// 获取图形主线路集
		LinkedList<DistrictTreeNodeBean> linesMainM = areaNode.getSubNode();
		for (int i = 0; i < size; i++) {
			// 判断主线是否相等
			for (int j = 0; j < size && (!equalFlag[j]); j++) {
				rb = checkSubLineEqual(linesMainM.get(i), linesMainB.get(j));
				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
					// 主线相等
					equalFlag[j] = true;
					break;
				}
			}
		}

		// 主线相等标志全部为真表示两个台区数据相同
		for (int i = 0; i < size; i++) {
			if (!equalFlag[i]) {
				return new ResultBean(ResultBean.RETURN_ERROR, "台区中存在不相等的主线路！",
						null, null);
			}
		}
		return new ResultBean(ResultBean.RETURN_SUCCESS, null, null, null);
	}

	/**
	 * 2009-4-16 Add by ZHM
	 * 
	 * @功能 将图形和业务数据中线路分成主线和支线，并检查线路总数是否一致
	 */
	private ResultBean initLines() {
		// 生成图形业务对象
		areaNode = madeTreeFromMap();
		// 获取图形线路
		ArrayList<LineGraphInfo> linesM = topology.getLineList();
		// 获取业务线路
		HashMap<String, DistrictLineBean> linesD = areaBean.getLine();

		// 业务主线路
		linesMainB = new ArrayList<DistrictLineBean>();
		for (String key : linesD.keySet()) {
			DistrictLineBean lineBean = linesD.get(key);
			String lineType = lineBean.getLineType();
			if (lineType.equals(DistrictLineBean.MAIN_LINE)) {
				linesMainB.add(lineBean);
			}
		}

		// 检查图形和业务数据中总线路数是否一致
		if (linesM.size() != linesD.size()) {
			return new ResultBean(ResultBean.RETURN_ERROR, "", null, null);
		}
		return new ResultBean(ResultBean.RETURN_SUCCESS, null, null, null);
	}

	/**
	 * 2009-4-16 Add by ZHM
	 * 
	 * @功能 检查台区基本信息是否一致
	 * @检查项 配变个数、配变箱个数、主线条数
	 * @return
	 */
	private ResultBean checkAreaBasic() {
		StringBuffer strErr = new StringBuffer();
		// 配变或配变箱存在
		int EXIST = 1;
		// 配变或配变箱不存在
		int NO_EXIST = 0;
		// *****************
		// 判断配变个数是否合法
		// *****************
		Element transformer = topology.getTransformerElement();
		if (transformer == null) {
			strErr.append("图形上缺少配变！");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		int size = areaBean.getTransformers().size();
		if (size != EXIST) {
			strErr.append("业务数据配变个数为：").append(size);
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// ********************
		// 判断配变箱个数是否合法
		// ********************
		Element transformerBox = topology.getDistributionBoxElement();
		size = areaBean.getBoxs().size();
		if ((transformerBox == null && size != NO_EXIST)
				|| (transformerBox != null && size != EXIST)) {
			// 图形上配变箱与业务配变箱数目不等
			strErr.append("配变箱数目不相同！");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// ******************
		// 判断主线路条数是否相等
		// ******************
		int sizeM = areaNode.getSubNode().size();
		int sizeB = linesMainB.size();
		if (sizeM != sizeB) {
			strErr.append("主线路数目不相等，图形主线路有").append(sizeM).append("条，业务数据主线路有")
					.append(sizeB).append("条");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}

		return new ResultBean(ResultBean.RETURN_SUCCESS, null, null, null);
	}

	/**
	 * 2009-4-16 Add by ZHM
	 * 
	 * @功能 检查两个指定线路基本信息是否一致
	 * @检查项 线路下设备数、线路下支线数、支线连接位置
	 * @param lineG
	 * @param lineB
	 * @return
	 */
	private ResultBean checkLineBasic(DistrictTreeNodeBean lineG,
			DistrictLineBean lineB) {
		StringBuffer strErr = new StringBuffer();
		// ********************
		// 判断两条线路类型是否一致
		// ********************
		String typeM = lineG.getType();
		String typeB = lineB.getLineType();
		if (!typeM.equals(typeB)) {
			strErr.append("图形线路类型为：").append(typeM).append(",业务线路类型为：").append(
					typeB);
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// ***********************
		// 判断两条线路下设备数是否相等
		// ***********************
		int size = lineG.getEquips().size();
		if (lineB.getPoles().size() != size) {
			strErr.append("图形线路支线设备与业务线路支线设备数不等");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// ***********************
		// 判断两条线路下支线数是否相等
		// ***********************
		size = lineG.getSubNode().size();
		if (lineB.getSubLines().size() != size) {
			strErr.append("图形线路支线数与业务线路支线数不等。");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// *************************
		// 判断两条线路下支线位置是否相等
		// *************************
		// 图形线路的子线路集
		LinkedList<DistrictTreeNodeBean> subLineGs = lineG.getSubNode();
		// 业务模型线路集
		HashMap<String, DistrictLineBean> lineBs = areaBean.getLine();
		// 业务线路的子线路集
		LinkedList<String> subLineBs = lineB.getSubLines();

		// 顺序检查支线位置是否相同
		boolean isSame = true;
		for (int i = 0; i < size; i++) {
			DistrictTreeNodeBean subLineG = subLineGs.get(i);
			String positionG = subLineG.getSubNodePosition();
			DistrictLineBean subLineB = lineBs.get(subLineBs.get(i));
			String positionB = subLineB.getPosition();
			if (!positionG.equals(positionB)) {
				isSame = false;
				break;
			}
		}
		if (!isSame) {
			// 逆序检查支线位置是否相同
			for (int i = 0; i < size; i++) {
				DistrictTreeNodeBean subLineG = subLineGs.get(i);
				String positionG = subLineG.getSubNodePosition();
				int posG = Integer.parseInt(positionG);
				DistrictLineBean subLineB = lineBs.get(subLineBs.get(i));
				String positionB = subLineB.getPosition();
				int posB = Integer.parseInt(positionB);
				if (posG != (size - posB - 1)) {
					isSame = false;
					break;
				}
			}
		}
		if (!isSame) {
			strErr.append("图形线路支线数与业务线路支线支接杆位置不同。");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// ****************************
		// 将判断为相等的两条线路进行设备关联
		// ****************************
		relatingLine(lineG, lineB);

		return new ResultBean(ResultBean.RETURN_SUCCESS, null, null, null);
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 判断两条线路是否相等
	 * @param lineG:图形线路对象
	 * @param lineB:业务线路对象
	 * @return
	 */
	private ResultBean checkSubLineEqual(DistrictTreeNodeBean lineG,
			DistrictLineBean lineB) {
		ResultBean rb;
		// ********************
		// 检查线路基本信息是否相等
		// ********************
		rb = checkLineBasic(lineG, lineB);
		if (rb.getReturnFlag() == ResultBean.RETURN_ERROR) {
			return rb;
		}

		// ***********************
		// 检查该线路下所有支线是否相等
		// ***********************
		LinkedList<String> subLineBs = lineB.getSubLines();
		LinkedList<DistrictTreeNodeBean> subLineGs = lineG.getSubNode();
		int size = subLineBs.size();
		if (subLineGs.size() != size) {
			// 判断支线数目是否相等
			return new ResultBean(ResultBean.RETURN_ERROR, "", null, null);
		}
		// 业务支线路与图形支线相等标志
		boolean[] equalFlag = new boolean[size];
		// 初始化相等标志
		for (int j = 0; j < size; j++) {
			equalFlag[j] = false;
		}
		for (int i = 0; i < size; i++) {
			// 判断主线是否相等
			for (int j = 0; j < size && (!equalFlag[j]); j++) {
				rb = checkSubLineEqual(subLineGs.get(i), areaBean.getLine()
						.get(subLineBs.get(j)));
				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
					// 主线相等
					equalFlag[j] = true;
					break;
				}
			}
		}

		// 主线相等标志全部为真表示两个台区数据相同
		for (int i = 0; i < size; i++) {
			if (!equalFlag[i]) {
				return new ResultBean(ResultBean.RETURN_ERROR, "台区中存在不相等的主线路！",
						null, null);
			}
		}
		return new ResultBean(ResultBean.RETURN_SUCCESS, null, null, null);
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @功能 从图形上获取设备树
	 * @return
	 */
	private DistrictTreeNodeBean madeTreeFromMap() {
		// 台区节点
		DistrictTreeNodeBean areaNode = new DistrictTreeNodeBean();
		for (LineGraphInfo line : topology.getLineList()) {
			String lineType = line.getLineType();
			if (lineType.equals(DistrictTopology.MAIN_LINE)) {
				// 主线节点
				areaNode.setSubNode(getNodeFromLine(line));
			}
		}
		areaNode.setId("sss");
		areaNode.setType("台区");
		System.out.println(displayArea(areaNode, 0));
		return areaNode;
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @功能 将LineGraphInfo对象转换成DistrictTreeNodeBean对象
	 * @param line
	 * @return
	 */
	private DistrictTreeNodeBean getNodeFromLine(LineGraphInfo line) {
		DistrictTreeNodeBean node = new DistrictTreeNodeBean();
		// 线路编号
		node.setId(line.getLineID());
		// 线路类型
		node.setType(line.getLineType());
		// 线路上设备
		LinkedList<String> equipList = line.getEquipList();
		for (int i = 0, size = equipList.size(); i < size; i++) {
			String[] equip = new String[2];
			equip[0] = equipList.get(i);
			node.setEquips(equip);
		}
		// 子线路
		for (LineGraphInfo subLine : topology.getLineList()) {
			if (!subLine.getLineType().equals(DistrictLineBean.MAIN_LINE)) {
				// 获取线路的父线路编号
				String[] parentMess = getParentEquipID(subLine);
				if (parentMess[0].equals(line.getLineID())) {
					DistrictTreeNodeBean treeNode = getNodeFromLine(subLine);
					treeNode.setSubNodePosition(parentMess[1]);
					node.setSubNode(treeNode);
				}
			}
		}
		return node;
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @功能 获取指定线路父线路编号
	 * @param line
	 * @return
	 */
	private String[] getParentEquipID(LineGraphInfo line) {
		String beginID = line.getEquipList().getFirst();
		String endID = line.getEquipList().getLast();
		String position = null;
		String[] re = new String[2];

		Element beginEl = doc.getElementById(beginID);
		Element el = null;
		if (!beginEl.getParentNode().equals(line.getLineElement())) {
			position = beginID;
		} else {
			position = endID;
		}
		el = (Element) doc.getElementById(position).getParentNode();
		re[0] = el.getAttribute("id");
		re[1] = getLinePosition(re[0], position);
		return re;
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @功能 获取指定线路上指定设备所处位置
	 * @param lineID:String:线路编号
	 * @param equipID:String:设备编号
	 * @return
	 */
	private String getLinePosition(String lineID, String equipID) {
		LineGraphInfo line = topology.getLineInfoByLineID(lineID);
		if (line != null) {
			LinkedList<String> equips = line.getEquipList();
			for (int i = 0, size = equips.size(); i < size; i++) {
				if (equipID.equals(equips.get(i))) {
					return Integer.toString(i);
				}
			}
		}
		return null;
	}

	/**
	 * 2009-4-21 Add by ZHM
	 * 
	 * @功能 将判断成相等的两条线路进行设备关联
	 * @param lineG:图形线路对象
	 * @param lineB:业务线路对象
	 * @return
	 */
	private void relatingLine(DistrictTreeNodeBean lineG, DistrictLineBean lineB) {
		// *************
		// 关联线路本身
		// *************
		Element lineE = doc.getElementById(lineG.getId());
		if (lineE != null) {
			String lineBID = (String) lineB.getProperties().get("bl_objid");
			// String lineBName = (String) lineB.getProperties().get("bl_name");
			// addBusinessMess(lineE, "ObjRef", "psmsID", lineBID);
			// addBusinessMess(lineE, "ObjRef", "psmsName",lineBName);
			saveRelating(lineBID, lineG.getId());
		}
		// *************
		// 关联线路下设备
		// *************
		LinkedList<String[]> equipsG = lineG.getEquips();
		LinkedList<String> equipsB = lineB.getPoles();
		HashMap<String, DistrictPoleBean> poles = areaBean.getPoles();
		for (int i = 0, size = equipsB.size(); i < size; i++) {
			String equipGID = equipsG.get(i)[0];
			Element equipE = doc.getElementById(equipGID);
			if (equipE != null) {
				String equipBID = equipsB.get(i);
				// DistrictPoleBean pole = poles.get(equipBID);
				// String poleName = (String)pole.getProperties().get("p_name");
				// addBusinessMess(equipE, "ObjRef", "psmsID", equipBID);
				// addBusinessMess(equipE, "ObjRef", "psmsName", poleName);
				saveRelating(equipBID, equipGID);
			}
		}
	}

	/**
	 * 2009-4-30 Add by ZHM
	 * 
	 * @功能 保存关联关系
	 * @param busiID:业务编号
	 * @param mapID:图形编号
	 */
	private void saveRelating(String busiID, String mapID) {
		this.relatedBusi.put(busiID, mapID);
		this.relatedMap.put(mapID, busiID);
	}

	/**
	 * 2009-4-29 Add by ZHM
	 * 
	 * @功能 将业务参数值写入指定元素的metadata元素内
	 * @param ele
	 * @param metaName:业务数据域名称
	 * @param name:业务参数名
	 * @param value:业务参数值
	 */
	private void addBusinessMess(Element ele, String metaName, String name,
			String value) {
		// 获取metadata元素
		Element metaEle = hasOrCreateMetaDataField(ele, "metadata");
		if (metaEle != null) {
			// 检查metadata元素下是否存在
			Element subMetaEle = hasOrCreateMetaDataField(metaEle, metaName);
			subMetaEle.setAttribute(name, value);
		}
	}

	/**
	 * 2009-4-29 Add by ZHM
	 * 
	 * @功能 检查metadata元素下是否存在metaName子节点如果没有则新建一个
	 * @param metaEle
	 * @param metaName
	 * @return
	 */
	private Element hasOrCreateMetaDataField(Element metaEle, String metaName) {
		NodeList nodes = metaEle.getChildNodes();
		for (int i = 0, size = nodes.getLength(); i < size; i++) {
			Node node = nodes.item(i);
			if (node != null && node.getNodeName().equals(metaName)) {
				return (Element) node;
			}
		}
		Element subMeta = doc.createElement(metaName);
		metaEle.appendChild(subMeta);
		return subMeta;
	}

	// /**
	// * 2009-4-29
	// * Add by ZHM
	// * @功能 在指定元素下判断是否存在metadata元素，如果存在返回该元素
	// * @param ele 指定元素
	// * @return
	// */
	// private Element hasMetaData(Element ele){
	// NodeList nodes = ele.getElementsByTagName("metadata");
	// if(nodes!=null && nodes.getLength()>0){
	// return (Element)nodes.item(0);
	// }
	// return null;
	// }

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @功能 打印图形设备树
	 * @param areaNode
	 * @return
	 */
	private String displayArea(DistrictTreeNodeBean node, int num) {
		StringBuffer str = new StringBuffer();
		str.append("\n").append(node.getId()).append(":").append(num).append(
				":").append(node.getSubNodePosition());
		str.append(";");
		LinkedList<DistrictTreeNodeBean> subNodes = node.getSubNode();
		for (DistrictTreeNodeBean sub : subNodes) {
			str.append(displayArea(sub, num + 1));
		}
		return str.toString();
	}

	/**
	 * 2009-4-30 Add by ZHM
	 * 
	 * @功能 获取已关联信息，业务编号为主键
	 * @return
	 */
	public HashMap<String, String> getRelatedBusi() {
		return relatedBusi;
	}

	/**
	 * 2009-4-30 Add by ZHM
	 * 
	 * @功能 获取已关联信息，图形编号为主键
	 * @return
	 */
	public HashMap<String, String> getRelatedMap() {
		return relatedMap;
	}

	/**
	 * 2009-4-30 Add by ZHM
	 * 
	 * @功能 生成业务模型显示树
	 * @return
	 */
	public DefaultMutableTreeNode createBusiTree(boolean isSucess) {
		// 节点信息对象
		TreeData nodeBean;
		// 节点颜色
		Color color;
		if (isSucess) {
			color = Color.BLACK;
		} else {
			color = Color.RED;
		}
		nodeBean = new TreeData(null, color, "总览");
		DefaultMutableTreeNode root = new DynamicTreeNode(nodeBean);

		DefaultMutableTreeNode treeNode;
		// 生成干线节点
		for (int i = 0, size = linesMainB.size(); i < size; i++) {
			DistrictLineBean line = linesMainB.get(i);
			// 获取线路名称
			String name = (String) line.getProperties().get("bl_name");
			// 获取线路编号
			String id = (String) line.getProperties().get("bl_objid");
			// 获取关联线路编号
			String mapid = relatedBusi.get(id);
			if (mapid != null) {
				color = Color.BLACK;
			} else {
				color = Color.RED;
				name += "(*)";
			}
			nodeBean = new TreeData(null, color, name);
			treeNode = new DynamicTreeNode(nodeBean);

			// 生成支线节点
			if (line.getSubLines().size() > 0) {
				DefaultMutableTreeNode subTreeNode = createSubBusiTree(
						treeNode, line);
				treeNode.insert(subTreeNode, 0);
			}

			root.insert(treeNode, 0);
		}

		this.isRelated = isSucess;

		return root;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @功能 生成子线路树节点
	 * @param parNode
	 * @param line
	 * @return
	 */
	private DefaultMutableTreeNode createSubBusiTree(
			DefaultMutableTreeNode parNode, DistrictLineBean line) {
		HashMap<String, DistrictLineBean> lines = areaBean.getLine();
		LinkedList<String> subLines = line.getSubLines();
		DefaultMutableTreeNode treeNode = null;
		for (int i = 0, size = subLines.size(); i < size; i++) {
			DistrictLineBean subLine = lines.get(subLines.get(i));
			// 获取线路名称
			String name = (String) subLine.getProperties().get("bl_name");
			// 获取线路编号
			String id = (String) subLine.getProperties().get("bl_objid");
			// 获取关联线路编号
			String mapid = relatedBusi.get(id);

			Color color;
			if (mapid != null) {
				color = Color.BLACK;
			} else {
				color = Color.RED;
				name += "(*)";
			}
			TreeData nodeBean = new TreeData(null, color, name);
			treeNode = new DynamicTreeNode(nodeBean);

			// 检查是否存在子线路
			if (subLine.getSubLines().size() > 0) {
				DefaultMutableTreeNode subTreeNode = createSubBusiTree(
						treeNode, subLine);
				treeNode.insert(subTreeNode, 0);
			}

			parNode.insert(treeNode, 0);
		}
		return treeNode;
	}

	/**
	 * 2009-5-4 Add by ZHM
	 * 
	 * @功能 将关联信息写入svg文件中
	 */
	public void modifySvgDom() {
		for (String mapID : relatedMap.keySet()) {
			String busID = relatedMap.get(mapID);
			String busName = getNameFromBusiness(busID);

			// 将关联信息写入svg文件中
			Element equipE = doc.getElementById(mapID);
			if (equipE != null) {
				addBusinessMess(equipE, "ObjRef", "psmsID", busID);
				addBusinessMess(equipE, "ObjRef", "psmsName", busName);
			}

		}
	}

	/**
	 * 2009-5-4 Add by ZHM
	 * 
	 * @功能 从业务模型数据中获取设备名称
	 * @param busID
	 * @return
	 */
	private String getNameFromBusiness(String busID) {
		String busName = null;
		DistrictLineBean line = areaBean.getLine().get(busID);
		if (line != null) {
			busName = (String) line.getProperties().get("bl_name");
		} else {
			DistrictPoleBean pole = areaBean.getPoles().get(busID);
			if (pole != null) {
				busName = (String) pole.getProperties().get("p_name");
			}
		}

		return busName;
	}

	/**
	 * 2009-5-4 Add by ZHM
	 * 
	 * @功能 将关联信息写入编辑器上模型树
	 */
	public void modifyMapTree() {
		for (LineGraphInfo line : topology.getLineList()) {
			// 填写线路名称
			String mapID = line.getLineID();
			String busID = relatedMap.get(mapID);
			String busName = getNameFromBusiness(busID);
			line.setLineName(busName);
			
			// 填写线路下设备名称
			for (String equipID : line.getEquipList()){
				busID = relatedMap.get(equipID);
				busName = getNameFromBusiness(busID);
				line.getEquipName().put(equipID,busName);
			}
		}
		
		topology.setRelated(true);
	}
}
