package com.nci.svg.district.relate.bean;

import java.util.LinkedList;

import com.nci.svg.district.relate.BasicProperty;

public class DistrictLineBean extends BasicProperty {
	public static final String MAIN_LINE = "干线";
	public static final String LATERAL_LINE = "支线";
	public static final String SERVICE_LINE = "接户线";
	/**
	 * @apram 线路基础属性，依次为：
	 * @param 线路编号、线路调度码、线路名称、线路上级线路编号、
	 * @param 上级线路支接杆回路编号、接入回路编号、
	 */
	private static String[] basicPropertyName = { "bl_objid", "bl_ddm",
			"bl_name", "bl_sjxl", "bl_zjgid", "bl_contact" };
	/**
	 * 线路下回路队列，仅保存杆塔编号信息
	 */
	private LinkedList<String> poles;
	/**
	 * 中间接入标志
	 */
	private boolean isMiddle;
	/**
	 * 子线路编号
	 */
	private LinkedList<String> subLines;
	/**
	 * 线路类型
	 */
	private String lineType;
	/**
	 * 父线路连接杆顺序号
	 */
	private String position;

	/**
	 * 构造函数
	 */
	public DistrictLineBean() {
		super(basicPropertyName);
		isMiddle = false;
		subLines = new LinkedList<String>();
		lineType = MAIN_LINE;
		position = "";
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @功能 获取父线路连接杆顺序号
	 * @return
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @功能 设置父线路连接杆顺序号
	 * @param position
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 获取线路类型
	 * @return
	 */
	public String getLineType() {
		return lineType;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @功能 设置线路类型
	 * @param lineType
	 */
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public boolean isMiddle() {
		return isMiddle;
	}

	public void setMiddle(boolean isMiddle) {
		this.isMiddle = isMiddle;
	}

	/**
	 * 2009-4-16 Add by ZHM 获取子线路编号
	 * 
	 * @return
	 */
	public LinkedList<String> getSubLines() {
		return subLines;
	}

	/**
	 * 2009-4-16 Add by ZHM 新增一条子线路
	 * 
	 * @param subLineID
	 */
	public void setSubLines(String subLineID) {
		this.subLines.add(subLineID);
	}

	/**
	 * 2009-4-16 Add by ZHM 设置子线路编号
	 * 
	 * @param subLines
	 */
	public void setSubLines(LinkedList<String> subLines) {
		this.subLines = subLines;
	}

	/**
	 * 返回
	 * 
	 * @return the poles
	 */
	public LinkedList<String> getPoles() {
		return poles;
	}

	/**
	 * 设置
	 * 
	 * @param poles
	 */
	public void setPoles(LinkedList<String> poles) {
		this.poles = poles;
	}

}
