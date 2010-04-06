package com.nci.svg.district;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.w3c.dom.Element;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2009-4-9
 * @功能：
 * 
 */
public class DistrictTopology {
	public static final String MAIN_LINE = "干线";
	public static final String LATERAL_LINE = "支线";
	public static final String SERVICE_LINE = "接户线";
	public static final String TOWER = "杆塔";
	public static final String STRAPPING_TABLE = "计量表";
	public static final String TRANSFORMER = "配变";
	public static final String DISTRIBUTIONBOX = "配电箱";

	/**
	 * <p>
	 * 公司：Hangzhou NCI System Engineering, Ltd.
	 * </p>
	 * 
	 * @author yx.nci
	 * @时间：2009-4-9
	 * @功能：线路图形数据
	 * 
	 */
	/**
	 * <p>
	 * 标题：DistrictTopology.java
	 * </p>
	 * <p>
	 * 描述：
	 * </p>
	 * <p>
	 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
	 * </p>
	 * <p>
	 * 公司：Hangzhou NCI System Engineering, Ltd.
	 * </p>
	 * 
	 * @author ZHM
	 * @时间: 2009-5-4
	 * @version 1.0
	 */
	public static class LineGraphInfo {
		/**
		 * add by yux,2009-3-27 线路编号
		 */
		private String lineID = "";
		/**
		 * 线路编号
		 */
		private String lineName = "";
		/**
		 * add by yux,2009-3-27 线路性质
		 */
		private String lineType = "";

		/**
		 * add by yux,2009-3-27 线上设备数量
		 */
		private int equipNum = 0;

		/**
		 * add by yux,2009-3-27 线路节点
		 */
		private Element lineElement = null;

		/**
		 * add by yux,2009-3-27 线上设备节点
		 */
		private LinkedList<String> equipList = new LinkedList<String>();
		/**
		 * 线路上的设备名称
		 */
		private HashMap<String, String> equipNames = new LinkedHashMap<String, String>();

		/**
		 * add by yux,2009-4-14 线外设备关联映射
		 */
		private HashMap<String, ArrayList<String>> relaEquipMap = new HashMap<String, ArrayList<String>>();

		/**
		 * 2009-5-4 Add by ZHM
		 * 
		 * @功能 获取线路名称
		 * @return
		 */
		public String getLineName() {
			return lineName;
		}

		/**
		 * 2009-5-4 Add by ZHM
		 * 
		 * @功能 设置线路名称
		 * @param lineName
		 */
		public void setLineName(String lineName) {
			this.lineName = lineName;
		}

		/**
		 * 2009-5-4 Add by ZHM
		 * 
		 * @功能 获取线路上的设备名称
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
		 * @功能 设置线路上的设备名称
		 * @param equipName
		 */
		public void setEquipName(HashMap<String, String> equipName) {
			this.equipNames = equipName;
		}

		public LineGraphInfo() {

		}

		/**
		 * 返回
		 * 
		 * @return the lineID
		 */
		public String getLineID() {
			return lineID;
		}

		/**
		 * 设置
		 * 
		 * @param lineID
		 *            the lineID to set
		 */
		public void setLineID(String lineID) {
			this.lineID = lineID;
		}

		/**
		 * 返回
		 * 
		 * @return the lineType
		 */
		public String getLineType() {
			return lineType;
		}

		/**
		 * 设置
		 * 
		 * @param lineType
		 *            the lineType to set
		 */
		public void setLineType(String lineType) {
			this.lineType = lineType;
		}

		/**
		 * 返回
		 * 
		 * @return the equipNum
		 */
		public int getEquipNum() {
			return equipNum;
		}

		/**
		 * 设置
		 * 
		 * @param equipNum
		 *            the equipNum to set
		 */
		public void setEquipNum(int equipNum) {
			this.equipNum = equipNum;
		}

		/**
		 * 返回
		 * 
		 * @return the lineElement
		 */
		public Element getLineElement() {
			return lineElement;
		}

		/**
		 * 设置
		 * 
		 * @param lineElement
		 *            the lineElement to set
		 */
		public void setLineElement(Element lineElement) {
			this.lineElement = lineElement;
		}

		/**
		 * 返回
		 * 
		 * @return the equipList
		 */
		public LinkedList<String> getEquipList() {
			return equipList;
		}

		/**
		 * 设置
		 * 
		 * @param equipList
		 *            the equipList to set
		 */
		public void setEquipList(LinkedList<String> equipList) {
			this.equipList = equipList;
		}

		/**
		 * 返回
		 * 
		 * @return the relaEquipMap
		 */
		public HashMap<String, ArrayList<String>> getRelaEquipMap() {
			return relaEquipMap;
		}

		/**
		 * 设置
		 * 
		 * @param relaEquipMap
		 *            the relaEquipMap to set
		 */
		public void setRelaEquipMap(
				HashMap<String, ArrayList<String>> relaEquipMap) {
			this.relaEquipMap = relaEquipMap;
		}

		/**
		 * 增加线上设备与线外设备关联关系
		 * 
		 * @param onlineEquipID:线上设备图元编号
		 * @param outlineEquipID:线外设备图元编号
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
		 * 根据线上设备图元编号获取该线上设备与线外设备的关联关系
		 * 
		 * @param onlineEquipID:线上设备编号
		 * @return 如存在则返回ArrayList<String>，不存在则返回null
		 */
		public ArrayList<String> getRelaEquipsByID(String onlineEquipID) {
			return relaEquipMap.get(onlineEquipID);
		}

	};

	/**
	 * add by yux,2009-3-27 配变节点
	 */
	protected Element transformerElement = null;

	/**
	 * add by yux,2009-3-27 配电柜
	 */
	protected Element distributionBoxElement = null;

	/**
	 * add by yux,2009-4-9 线路信息
	 */
	protected ArrayList<LineGraphInfo> lineList = new ArrayList<LineGraphInfo>();

	/**
	 * add by yux,2009-4-9 拓扑构建标记
	 */
	protected boolean topologyCreated = false;

	/**
	 * 业务模型关联标志
	 */
	protected boolean isRelated = false;

	/**
	 * 返回
	 * 
	 * @return the transformerElement
	 */
	public Element getTransformerElement() {
		return transformerElement;
	}

	/**
	 * 设置
	 * 
	 * @param transformerElement
	 *            the transformerElement to set
	 */
	public void setTransformerElement(Element transformerElement) {
		this.transformerElement = transformerElement;
	}

	/**
	 * 返回
	 * 
	 * @return the distributionBoxElement
	 */
	public Element getDistributionBoxElement() {
		return distributionBoxElement;
	}

	/**
	 * 设置
	 * 
	 * @param distributionBoxElement
	 *            the distributionBoxElement to set
	 */
	public void setDistributionBoxElement(Element distributionBoxElement) {
		this.distributionBoxElement = distributionBoxElement;
	}

	/**
	 * 返回
	 * 
	 * @return the lineList
	 */
	public ArrayList<LineGraphInfo> getLineList() {
		return lineList;
	}

	/**
	 * 设置
	 * 
	 * @param lineList
	 *            the lineList to set
	 */
	public void setLineList(ArrayList<LineGraphInfo> lineList) {
		this.lineList = lineList;
	}

	/**
	 * 根据线编号获取线数据
	 * 
	 * @param lineID:线编号
	 * @return 该线路信息，如不存在则返回null
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
	 * 根据线节点获取线数据
	 * 
	 * @param el:线节点
	 * @return 如不存在则返回null
	 */
	public LineGraphInfo getLineInfoByLineElement(Element el) {
		if (el == null)
			return null;
		String lineID = el.getAttribute("id");
		return getLineInfoByLineID(lineID);
	}

	/**
	 * 根据输入的线编号，起始设备编号，终止设备编号获取子线路设备列表
	 * 
	 * @param lineID:线编号
	 * @param beginEquipID:起始设备编号
	 * @param endEquipID:终止设备编号
	 * @return 子线路设备列表，如不存在则返回null
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
	 * 返回
	 * 
	 * @return the topologyCreated
	 */
	public boolean isTopologyCreated() {
		return topologyCreated;
	}

	/**
	 * 设置
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

}
