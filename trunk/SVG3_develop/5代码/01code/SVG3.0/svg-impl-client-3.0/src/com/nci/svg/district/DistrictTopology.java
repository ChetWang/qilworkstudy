package com.nci.svg.district;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.w3c.dom.Element;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-4-9
 * @���ܣ�
 * 
 */
public class DistrictTopology {
	public static final String MAIN_LINE = "����";
	public static final String LATERAL_LINE = "֧��";
	public static final String SERVICE_LINE = "�ӻ���";
	public static final String TOWER = "����";
	public static final String STRAPPING_TABLE = "������";
	public static final String TRANSFORMER = "���";
	public static final String DISTRIBUTIONBOX = "�����";

	/**
	 * <p>
	 * ��˾��Hangzhou NCI System Engineering, Ltd.
	 * </p>
	 * 
	 * @author yx.nci
	 * @ʱ�䣺2009-4-9
	 * @���ܣ���·ͼ������
	 * 
	 */
	/**
	 * <p>
	 * ���⣺DistrictTopology.java
	 * </p>
	 * <p>
	 * ������
	 * </p>
	 * <p>
	 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
	 * </p>
	 * <p>
	 * ��˾��Hangzhou NCI System Engineering, Ltd.
	 * </p>
	 * 
	 * @author ZHM
	 * @ʱ��: 2009-5-4
	 * @version 1.0
	 */
	public static class LineGraphInfo {
		/**
		 * add by yux,2009-3-27 ��·���
		 */
		private String lineID = "";
		/**
		 * ��·���
		 */
		private String lineName = "";
		/**
		 * add by yux,2009-3-27 ��·����
		 */
		private String lineType = "";

		/**
		 * add by yux,2009-3-27 �����豸����
		 */
		private int equipNum = 0;

		/**
		 * add by yux,2009-3-27 ��·�ڵ�
		 */
		private Element lineElement = null;

		/**
		 * add by yux,2009-3-27 �����豸�ڵ�
		 */
		private LinkedList<String> equipList = new LinkedList<String>();
		/**
		 * ��·�ϵ��豸����
		 */
		private HashMap<String, String> equipNames = new LinkedHashMap<String, String>();

		/**
		 * add by yux,2009-4-14 �����豸����ӳ��
		 */
		private HashMap<String, ArrayList<String>> relaEquipMap = new HashMap<String, ArrayList<String>>();

		/**
		 * 2009-5-4 Add by ZHM
		 * 
		 * @���� ��ȡ��·����
		 * @return
		 */
		public String getLineName() {
			return lineName;
		}

		/**
		 * 2009-5-4 Add by ZHM
		 * 
		 * @���� ������·����
		 * @param lineName
		 */
		public void setLineName(String lineName) {
			this.lineName = lineName;
		}

		/**
		 * 2009-5-4 Add by ZHM
		 * 
		 * @���� ��ȡ��·�ϵ��豸����
		 * @return
		 */
		public HashMap<String, String> getEquipName() {
			if(equipNames == null)
				equipNames = new HashMap<String, String>();
			return equipNames;
		}

		/**
		 * 2009-5-4 Add by ZHM
		 * 
		 * @���� ������·�ϵ��豸����
		 * @param equipName
		 */
		public void setEquipName(HashMap<String, String> equipName) {
			this.equipNames = equipName;
		}

		public LineGraphInfo() {

		}

		/**
		 * ����
		 * 
		 * @return the lineID
		 */
		public String getLineID() {
			return lineID;
		}

		/**
		 * ����
		 * 
		 * @param lineID
		 *            the lineID to set
		 */
		public void setLineID(String lineID) {
			this.lineID = lineID;
		}

		/**
		 * ����
		 * 
		 * @return the lineType
		 */
		public String getLineType() {
			return lineType;
		}

		/**
		 * ����
		 * 
		 * @param lineType
		 *            the lineType to set
		 */
		public void setLineType(String lineType) {
			this.lineType = lineType;
		}

		/**
		 * ����
		 * 
		 * @return the equipNum
		 */
		public int getEquipNum() {
			return equipNum;
		}

		/**
		 * ����
		 * 
		 * @param equipNum
		 *            the equipNum to set
		 */
		public void setEquipNum(int equipNum) {
			this.equipNum = equipNum;
		}

		/**
		 * ����
		 * 
		 * @return the lineElement
		 */
		public Element getLineElement() {
			return lineElement;
		}

		/**
		 * ����
		 * 
		 * @param lineElement
		 *            the lineElement to set
		 */
		public void setLineElement(Element lineElement) {
			this.lineElement = lineElement;
		}

		/**
		 * ����
		 * 
		 * @return the equipList
		 */
		public LinkedList<String> getEquipList() {
			return equipList;
		}

		/**
		 * ����
		 * 
		 * @param equipList
		 *            the equipList to set
		 */
		public void setEquipList(LinkedList<String> equipList) {
			this.equipList = equipList;
		}

		/**
		 * ����
		 * 
		 * @return the relaEquipMap
		 */
		public HashMap<String, ArrayList<String>> getRelaEquipMap() {
			return relaEquipMap;
		}

		/**
		 * ����
		 * 
		 * @param relaEquipMap
		 *            the relaEquipMap to set
		 */
		public void setRelaEquipMap(
				HashMap<String, ArrayList<String>> relaEquipMap) {
			this.relaEquipMap = relaEquipMap;
		}

		/**
		 * ���������豸�������豸������ϵ
		 * 
		 * @param onlineEquipID:�����豸ͼԪ���
		 * @param outlineEquipID:�����豸ͼԪ���
		 */
		public void appendRelaEquip(String onlineEquipID, String outlineEquipID) {
			ArrayList<String> list = relaEquipMap.get(onlineEquipID);
			if (list == null) {
				list = new ArrayList<String>();
				relaEquipMap.put(onlineEquipID, list);
			}
			list.add(outlineEquipID);
		}

		/**
		 * ���������豸ͼԪ��Ż�ȡ�������豸�������豸�Ĺ�����ϵ
		 * 
		 * @param onlineEquipID:�����豸���
		 * @return ������򷵻�ArrayList<String>���������򷵻�null
		 */
		public ArrayList<String> getRelaEquipsByID(String onlineEquipID) {
			return relaEquipMap.get(onlineEquipID);
		}

	};

	/**
	 * add by yux,2009-3-27 ���ڵ�
	 */
	protected Element transformerElement = null;

	/**
	 * add by yux,2009-3-27 ����
	 */
	protected Element distributionBoxElement = null;

	/**
	 * add by yux,2009-4-9 ��·��Ϣ
	 */
	protected ArrayList<LineGraphInfo> lineList = new ArrayList<LineGraphInfo>();

	/**
	 * add by yux,2009-4-9 ���˹������
	 */
	protected boolean topologyCreated = false;

	/**
	 * ҵ��ģ�͹�����־
	 */
	protected boolean isRelated = false;

	/**
	 * ����
	 * 
	 * @return the transformerElement
	 */
	public Element getTransformerElement() {
		return transformerElement;
	}

	/**
	 * ����
	 * 
	 * @param transformerElement
	 *            the transformerElement to set
	 */
	public void setTransformerElement(Element transformerElement) {
		this.transformerElement = transformerElement;
	}

	/**
	 * ����
	 * 
	 * @return the distributionBoxElement
	 */
	public Element getDistributionBoxElement() {
		return distributionBoxElement;
	}

	/**
	 * ����
	 * 
	 * @param distributionBoxElement
	 *            the distributionBoxElement to set
	 */
	public void setDistributionBoxElement(Element distributionBoxElement) {
		this.distributionBoxElement = distributionBoxElement;
	}

	/**
	 * ����
	 * 
	 * @return the lineList
	 */
	public ArrayList<LineGraphInfo> getLineList() {
		return lineList;
	}

	/**
	 * ����
	 * 
	 * @param lineList
	 *            the lineList to set
	 */
	public void setLineList(ArrayList<LineGraphInfo> lineList) {
		this.lineList = lineList;
	}

	/**
	 * �����߱�Ż�ȡ������
	 * 
	 * @param lineID:�߱��
	 * @return ����·��Ϣ���粻�����򷵻�null
	 */
	public LineGraphInfo getLineInfoByLineID(String lineID) {
		if (lineID == null || lineID.length() == 0)
			return null;

		for (LineGraphInfo line : lineList) {
			if (line.getLineID().equals(lineID))
				return line;
		}
		return null;
	}

	/**
	 * �����߽ڵ��ȡ������
	 * 
	 * @param el:�߽ڵ�
	 * @return �粻�����򷵻�null
	 */
	public LineGraphInfo getLineInfoByLineElement(Element el) {
		if (el == null)
			return null;
		String lineID = el.getAttribute("id");
		return getLineInfoByLineID(lineID);
	}

	/**
	 * ����������߱�ţ���ʼ�豸��ţ���ֹ�豸��Ż�ȡ����·�豸�б�
	 * 
	 * @param lineID:�߱��
	 * @param beginEquipID:��ʼ�豸���
	 * @param endEquipID:��ֹ�豸���
	 * @return ����·�豸�б��粻�����򷵻�null
	 */
	public LinkedList<String> getSubList(String lineID, String beginEquipID,
			String endEquipID) {
		for (LineGraphInfo line : lineList) {
			if (line.getLineID().equals(lineID)) {
				int begin = line.getEquipList().indexOf(beginEquipID);
				int end = line.getEquipList().indexOf(endEquipID);
				if (begin == -1 || end == -1)
					return null;

				LinkedList<String> subList = new LinkedList<String>();
				if (begin < end) {
					for (int i = begin; i <= end; i++) {
						subList.add(line.getEquipList().get(i));
					}
				} else {
					for (int i = begin; i >= end; i--) {
						subList.add(line.getEquipList().get(i));
					}
				}
				return subList;
			}
		}
		return null;
	}

	/**
	 * ����
	 * 
	 * @return the topologyCreated
	 */
	public boolean isTopologyCreated() {
		return topologyCreated;
	}

	/**
	 * ����
	 * 
	 * @param topologyCreated
	 *            the topologyCreated to set
	 */
	public void setTopologyCreated(boolean topologyCreated) {
		this.topologyCreated = topologyCreated;
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

}
