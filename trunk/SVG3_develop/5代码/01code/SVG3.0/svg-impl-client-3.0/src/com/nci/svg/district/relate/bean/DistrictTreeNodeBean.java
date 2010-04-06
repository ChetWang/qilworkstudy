package com.nci.svg.district.relate.bean;

import java.util.LinkedList;

/**
 * <p>
 * 标题：DistrictTreeNodeBean.java
 * </p>
 * <p>
 * 描述：台区图形设备树节点类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-4-17
 * @version 1.0
 */
public class DistrictTreeNodeBean {
	/**
	 * 节点对应的设备编号
	 */
	private String id;
	/**
	 * 对应的设备类型
	 */
	private String type;
	/**
	 * 子节点编号
	 */
	private LinkedList<DistrictTreeNodeBean> subNode;
	/**
	 * 子线路的连接位置
	 */
	private String subNodePosition;
	/**
	 * 节点的设备集合 0设备编号、1设备类型
	 */
	private LinkedList<String[]> equips;

	/**
	 * 构造函数
	 */
	public DistrictTreeNodeBean() {
		this.id = "";
		this.type = "";
		this.subNode = new LinkedList<DistrictTreeNodeBean>();
		this.equips = new LinkedList<String[]>();
	}

	/**
	 * 2009-4-20
	 * Add by ZHM
	 * @功能 获取子线路连接位置
	 * @return
	 */
	public String getSubNodePosition() {
		return subNodePosition;
	}
	
	/**
	 * 2009-4-20
	 * Add by ZHM
	 * @功能 新增支线连接位置
	 * @param subNodePosition
	 */
	public void setSubNodePosition(String subNodePosition){
		this.subNodePosition = subNodePosition;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 获取节点设备编号
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 设置节点设备编号
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 获取节点设备类型
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 设置节点设备类型
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 获取子节点集
	 * @return
	 */
	public LinkedList<DistrictTreeNodeBean> getSubNode() {
		return subNode;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 新增子节点到节点集中
	 * @param subNode
	 */
	public void setSubNode(DistrictTreeNodeBean subNode) {
		if (subNode != null) {
			this.subNode.add(subNode);
		}
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 设置子节点集
	 * @param subNode
	 */
	public void setSubNode(LinkedList<DistrictTreeNodeBean> subNode) {
		this.subNode = subNode;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 获取节点的设备集
	 * @return
	 */
	public LinkedList<String[]> getEquips() {
		return equips;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 新增设备信息到节点设备集中
	 * @param equip
	 */
	public void setEquips(String[] equip) {
		if (equip.length == 2) {
			this.equips.add(equip);
		}
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 设置节点的设备集
	 * @param equips
	 */
	public void setEquips(LinkedList<String[]> equips) {
		this.equips = equips;
	}
}
