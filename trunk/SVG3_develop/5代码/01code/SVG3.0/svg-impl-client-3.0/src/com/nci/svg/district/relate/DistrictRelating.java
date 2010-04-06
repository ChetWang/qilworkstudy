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
 * ���⣺DistrictRelating.java
 * </p>
 * <p>
 * ������ ̨��ͼ�Զ�������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-4-14
 * @version 1.0
 */
public class DistrictRelating {
	/**
	 * ҵ��̨������
	 */
	private DistrictAreaBean areaBean;
	/**
	 * ͼ��̨������
	 */
	private DistrictTreeNodeBean areaNode;
	/**
	 * ���˹�ϵ����
	 */
	private DistrictTopology topology;
	/**
	 * ҵ���������߼���
	 */
	private ArrayList<DistrictLineBean> linesMainB;
	/**
	 * ͼ����doc�ڵ�
	 */
	private Document doc;
	/**
	 * �ѹ����豸��Ӧ��<ҵ����,ͼ�α��>
	 */
	private HashMap<String, String> relatedBusi;
	/**
	 * �ѹ����豸��Ӧ��<ͼ�α��,ҵ����>
	 */
	private HashMap<String, String> relatedMap;
	/**
	 * ҵ��ģ�͹�����־
	 */
	private boolean isRelated;

	/**
	 * ���캯��
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
	 * @���� ��ȡҵ��ģ�͹�����־
	 * @return
	 */
	public boolean isRelated() {
		return isRelated;
	}

	/**
	 * 2009-5-4 Add by ZHM
	 * 
	 * @���� ����ҵ��ģ�͹�����־
	 * @param isRelated
	 */
	public void setRelated(boolean isRelated) {
		this.isRelated = isRelated;
	}

	/**
	 * 2009-4-15 Add by ZHM
	 * 
	 * @���� ����������ȡ���ݺ�̨��ͼͼԪ���бȶ�
	 * @return
	 */
	public ResultBean relating() {
		ResultBean rb;
		// ����ͼ�κ�ҵ������·����
		rb = initLines();
		if (rb.getReturnFlag() == ResultBean.RETURN_ERROR)
			return rb;
		// ̨��������Ϣ���
		// rb = checkAreaBasic();
		if (rb != null && rb.getReturnFlag() == ResultBean.RETURN_ERROR)
			return rb;

		// *****************
		// ������������Ƿ����
		// *****************
		int size = linesMainB.size();
		// ҵ������·��ͼ��������ȱ�־
		boolean[] equalFlag = new boolean[size];
		// ��ʼ����ȱ�־
		for (int j = 0; j < size; j++) {
			equalFlag[j] = false;
		}
		// ��ȡͼ������·��
		LinkedList<DistrictTreeNodeBean> linesMainM = areaNode.getSubNode();
		for (int i = 0; i < size; i++) {
			// �ж������Ƿ����
			for (int j = 0; j < size && (!equalFlag[j]); j++) {
				rb = checkSubLineEqual(linesMainM.get(i), linesMainB.get(j));
				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
					// �������
					equalFlag[j] = true;
					break;
				}
			}
		}

		// ������ȱ�־ȫ��Ϊ���ʾ����̨��������ͬ
		for (int i = 0; i < size; i++) {
			if (!equalFlag[i]) {
				return new ResultBean(ResultBean.RETURN_ERROR, "̨���д��ڲ���ȵ�����·��",
						null, null);
			}
		}
		return new ResultBean(ResultBean.RETURN_SUCCESS, null, null, null);
	}

	/**
	 * 2009-4-16 Add by ZHM
	 * 
	 * @���� ��ͼ�κ�ҵ����������·�ֳ����ߺ�֧�ߣ��������·�����Ƿ�һ��
	 */
	private ResultBean initLines() {
		// ����ͼ��ҵ�����
		areaNode = madeTreeFromMap();
		// ��ȡͼ����·
		ArrayList<LineGraphInfo> linesM = topology.getLineList();
		// ��ȡҵ����·
		HashMap<String, DistrictLineBean> linesD = areaBean.getLine();

		// ҵ������·
		linesMainB = new ArrayList<DistrictLineBean>();
		for (String key : linesD.keySet()) {
			DistrictLineBean lineBean = linesD.get(key);
			String lineType = lineBean.getLineType();
			if (lineType.equals(DistrictLineBean.MAIN_LINE)) {
				linesMainB.add(lineBean);
			}
		}

		// ���ͼ�κ�ҵ������������·���Ƿ�һ��
		if (linesM.size() != linesD.size()) {
			return new ResultBean(ResultBean.RETURN_ERROR, "", null, null);
		}
		return new ResultBean(ResultBean.RETURN_SUCCESS, null, null, null);
	}

	/**
	 * 2009-4-16 Add by ZHM
	 * 
	 * @���� ���̨��������Ϣ�Ƿ�һ��
	 * @����� ��������������������������
	 * @return
	 */
	private ResultBean checkAreaBasic() {
		StringBuffer strErr = new StringBuffer();
		// ������������
		int EXIST = 1;
		// ��������䲻����
		int NO_EXIST = 0;
		// *****************
		// �ж��������Ƿ�Ϸ�
		// *****************
		Element transformer = topology.getTransformerElement();
		if (transformer == null) {
			strErr.append("ͼ����ȱ����䣡");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		int size = areaBean.getTransformers().size();
		if (size != EXIST) {
			strErr.append("ҵ������������Ϊ��").append(size);
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// ********************
		// �ж����������Ƿ�Ϸ�
		// ********************
		Element transformerBox = topology.getDistributionBoxElement();
		size = areaBean.getBoxs().size();
		if ((transformerBox == null && size != NO_EXIST)
				|| (transformerBox != null && size != EXIST)) {
			// ͼ�����������ҵ���������Ŀ����
			strErr.append("�������Ŀ����ͬ��");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// ******************
		// �ж�����·�����Ƿ����
		// ******************
		int sizeM = areaNode.getSubNode().size();
		int sizeB = linesMainB.size();
		if (sizeM != sizeB) {
			strErr.append("����·��Ŀ����ȣ�ͼ������·��").append(sizeM).append("����ҵ����������·��")
					.append(sizeB).append("��");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}

		return new ResultBean(ResultBean.RETURN_SUCCESS, null, null, null);
	}

	/**
	 * 2009-4-16 Add by ZHM
	 * 
	 * @���� �������ָ����·������Ϣ�Ƿ�һ��
	 * @����� ��·���豸������·��֧������֧������λ��
	 * @param lineG
	 * @param lineB
	 * @return
	 */
	private ResultBean checkLineBasic(DistrictTreeNodeBean lineG,
			DistrictLineBean lineB) {
		StringBuffer strErr = new StringBuffer();
		// ********************
		// �ж�������·�����Ƿ�һ��
		// ********************
		String typeM = lineG.getType();
		String typeB = lineB.getLineType();
		if (!typeM.equals(typeB)) {
			strErr.append("ͼ����·����Ϊ��").append(typeM).append(",ҵ����·����Ϊ��").append(
					typeB);
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// ***********************
		// �ж�������·���豸���Ƿ����
		// ***********************
		int size = lineG.getEquips().size();
		if (lineB.getPoles().size() != size) {
			strErr.append("ͼ����·֧���豸��ҵ����·֧���豸������");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// ***********************
		// �ж�������·��֧�����Ƿ����
		// ***********************
		size = lineG.getSubNode().size();
		if (lineB.getSubLines().size() != size) {
			strErr.append("ͼ����·֧������ҵ����·֧�������ȡ�");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// *************************
		// �ж�������·��֧��λ���Ƿ����
		// *************************
		// ͼ����·������·��
		LinkedList<DistrictTreeNodeBean> subLineGs = lineG.getSubNode();
		// ҵ��ģ����·��
		HashMap<String, DistrictLineBean> lineBs = areaBean.getLine();
		// ҵ����·������·��
		LinkedList<String> subLineBs = lineB.getSubLines();

		// ˳����֧��λ���Ƿ���ͬ
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
			// ������֧��λ���Ƿ���ͬ
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
			strErr.append("ͼ����·֧������ҵ����·֧��֧�Ӹ�λ�ò�ͬ��");
			return new ResultBean(ResultBean.RETURN_ERROR, strErr.toString(),
					null, null);
		}
		// ****************************
		// ���ж�Ϊ��ȵ�������·�����豸����
		// ****************************
		relatingLine(lineG, lineB);

		return new ResultBean(ResultBean.RETURN_SUCCESS, null, null, null);
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� �ж�������·�Ƿ����
	 * @param lineG:ͼ����·����
	 * @param lineB:ҵ����·����
	 * @return
	 */
	private ResultBean checkSubLineEqual(DistrictTreeNodeBean lineG,
			DistrictLineBean lineB) {
		ResultBean rb;
		// ********************
		// �����·������Ϣ�Ƿ����
		// ********************
		rb = checkLineBasic(lineG, lineB);
		if (rb.getReturnFlag() == ResultBean.RETURN_ERROR) {
			return rb;
		}

		// ***********************
		// ������·������֧���Ƿ����
		// ***********************
		LinkedList<String> subLineBs = lineB.getSubLines();
		LinkedList<DistrictTreeNodeBean> subLineGs = lineG.getSubNode();
		int size = subLineBs.size();
		if (subLineGs.size() != size) {
			// �ж�֧����Ŀ�Ƿ����
			return new ResultBean(ResultBean.RETURN_ERROR, "", null, null);
		}
		// ҵ��֧��·��ͼ��֧����ȱ�־
		boolean[] equalFlag = new boolean[size];
		// ��ʼ����ȱ�־
		for (int j = 0; j < size; j++) {
			equalFlag[j] = false;
		}
		for (int i = 0; i < size; i++) {
			// �ж������Ƿ����
			for (int j = 0; j < size && (!equalFlag[j]); j++) {
				rb = checkSubLineEqual(subLineGs.get(i), areaBean.getLine()
						.get(subLineBs.get(j)));
				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
					// �������
					equalFlag[j] = true;
					break;
				}
			}
		}

		// ������ȱ�־ȫ��Ϊ���ʾ����̨��������ͬ
		for (int i = 0; i < size; i++) {
			if (!equalFlag[i]) {
				return new ResultBean(ResultBean.RETURN_ERROR, "̨���д��ڲ���ȵ�����·��",
						null, null);
			}
		}
		return new ResultBean(ResultBean.RETURN_SUCCESS, null, null, null);
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @���� ��ͼ���ϻ�ȡ�豸��
	 * @return
	 */
	private DistrictTreeNodeBean madeTreeFromMap() {
		// ̨���ڵ�
		DistrictTreeNodeBean areaNode = new DistrictTreeNodeBean();
		for (LineGraphInfo line : topology.getLineList()) {
			String lineType = line.getLineType();
			if (lineType.equals(DistrictTopology.MAIN_LINE)) {
				// ���߽ڵ�
				areaNode.setSubNode(getNodeFromLine(line));
			}
		}
		areaNode.setId("sss");
		areaNode.setType("̨��");
		System.out.println(displayArea(areaNode, 0));
		return areaNode;
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @���� ��LineGraphInfo����ת����DistrictTreeNodeBean����
	 * @param line
	 * @return
	 */
	private DistrictTreeNodeBean getNodeFromLine(LineGraphInfo line) {
		DistrictTreeNodeBean node = new DistrictTreeNodeBean();
		// ��·���
		node.setId(line.getLineID());
		// ��·����
		node.setType(line.getLineType());
		// ��·���豸
		LinkedList<String> equipList = line.getEquipList();
		for (int i = 0, size = equipList.size(); i < size; i++) {
			String[] equip = new String[2];
			equip[0] = equipList.get(i);
			node.setEquips(equip);
		}
		// ����·
		for (LineGraphInfo subLine : topology.getLineList()) {
			if (!subLine.getLineType().equals(DistrictLineBean.MAIN_LINE)) {
				// ��ȡ��·�ĸ���·���
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
	 * @���� ��ȡָ����·����·���
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
	 * @���� ��ȡָ����·��ָ���豸����λ��
	 * @param lineID:String:��·���
	 * @param equipID:String:�豸���
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
	 * @���� ���жϳ���ȵ�������·�����豸����
	 * @param lineG:ͼ����·����
	 * @param lineB:ҵ����·����
	 * @return
	 */
	private void relatingLine(DistrictTreeNodeBean lineG, DistrictLineBean lineB) {
		// *************
		// ������·����
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
		// ������·���豸
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
	 * @���� ���������ϵ
	 * @param busiID:ҵ����
	 * @param mapID:ͼ�α��
	 */
	private void saveRelating(String busiID, String mapID) {
		this.relatedBusi.put(busiID, mapID);
		this.relatedMap.put(mapID, busiID);
	}

	/**
	 * 2009-4-29 Add by ZHM
	 * 
	 * @���� ��ҵ�����ֵд��ָ��Ԫ�ص�metadataԪ����
	 * @param ele
	 * @param metaName:ҵ������������
	 * @param name:ҵ�������
	 * @param value:ҵ�����ֵ
	 */
	private void addBusinessMess(Element ele, String metaName, String name,
			String value) {
		// ��ȡmetadataԪ��
		Element metaEle = hasOrCreateMetaDataField(ele, "metadata");
		if (metaEle != null) {
			// ���metadataԪ�����Ƿ����
			Element subMetaEle = hasOrCreateMetaDataField(metaEle, metaName);
			subMetaEle.setAttribute(name, value);
		}
	}

	/**
	 * 2009-4-29 Add by ZHM
	 * 
	 * @���� ���metadataԪ�����Ƿ����metaName�ӽڵ����û�����½�һ��
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
	// * @���� ��ָ��Ԫ�����ж��Ƿ����metadataԪ�أ�������ڷ��ظ�Ԫ��
	// * @param ele ָ��Ԫ��
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
	 * @���� ��ӡͼ���豸��
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
	 * @���� ��ȡ�ѹ�����Ϣ��ҵ����Ϊ����
	 * @return
	 */
	public HashMap<String, String> getRelatedBusi() {
		return relatedBusi;
	}

	/**
	 * 2009-4-30 Add by ZHM
	 * 
	 * @���� ��ȡ�ѹ�����Ϣ��ͼ�α��Ϊ����
	 * @return
	 */
	public HashMap<String, String> getRelatedMap() {
		return relatedMap;
	}

	/**
	 * 2009-4-30 Add by ZHM
	 * 
	 * @���� ����ҵ��ģ����ʾ��
	 * @return
	 */
	public DefaultMutableTreeNode createBusiTree(boolean isSucess) {
		// �ڵ���Ϣ����
		TreeData nodeBean;
		// �ڵ���ɫ
		Color color;
		if (isSucess) {
			color = Color.BLACK;
		} else {
			color = Color.RED;
		}
		nodeBean = new TreeData(null, color, "����");
		DefaultMutableTreeNode root = new DynamicTreeNode(nodeBean);

		DefaultMutableTreeNode treeNode;
		// ���ɸ��߽ڵ�
		for (int i = 0, size = linesMainB.size(); i < size; i++) {
			DistrictLineBean line = linesMainB.get(i);
			// ��ȡ��·����
			String name = (String) line.getProperties().get("bl_name");
			// ��ȡ��·���
			String id = (String) line.getProperties().get("bl_objid");
			// ��ȡ������·���
			String mapid = relatedBusi.get(id);
			if (mapid != null) {
				color = Color.BLACK;
			} else {
				color = Color.RED;
				name += "(*)";
			}
			nodeBean = new TreeData(null, color, name);
			treeNode = new DynamicTreeNode(nodeBean);

			// ����֧�߽ڵ�
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
	 * @���� ��������·���ڵ�
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
			// ��ȡ��·����
			String name = (String) subLine.getProperties().get("bl_name");
			// ��ȡ��·���
			String id = (String) subLine.getProperties().get("bl_objid");
			// ��ȡ������·���
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

			// ����Ƿ��������·
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
	 * @���� ��������Ϣд��svg�ļ���
	 */
	public void modifySvgDom() {
		for (String mapID : relatedMap.keySet()) {
			String busID = relatedMap.get(mapID);
			String busName = getNameFromBusiness(busID);

			// ��������Ϣд��svg�ļ���
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
	 * @���� ��ҵ��ģ�������л�ȡ�豸����
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
	 * @���� ��������Ϣд��༭����ģ����
	 */
	public void modifyMapTree() {
		for (LineGraphInfo line : topology.getLineList()) {
			// ��д��·����
			String mapID = line.getLineID();
			String busID = relatedMap.get(mapID);
			String busName = getNameFromBusiness(busID);
			line.setLineName(busName);
			
			// ��д��·���豸����
			for (String equipID : line.getEquipList()){
				busID = relatedMap.get(equipID);
				busName = getNameFromBusiness(busID);
				line.getEquipName().put(equipID,busName);
			}
		}
		
		topology.setRelated(true);
	}
}
