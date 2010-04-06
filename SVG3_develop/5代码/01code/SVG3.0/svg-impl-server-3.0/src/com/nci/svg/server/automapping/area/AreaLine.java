package com.nci.svg.server.automapping.area;

import java.util.ArrayList;

import com.nci.svg.server.automapping.comm.BasicProperty;

/**
 * <p>
 * 标题：Line.java
 * </p>
 * <p>
 * 描述：台区图自动成图用的线路类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-03-03
 * @version 1.0
 */
public class AreaLine extends BasicProperty {
	/**
	 * @apram 线路基础属性，依次为：
	 * @param 线路编号、线路名称、线路材料类型、线路起始杆塔名称、
	 * @param 线路终止杆塔名称、线路总长度、线路起始角、配变挂接中间杆塔标志、
	 * @param 父线路编号、父线路的接入杆塔编号
	 */
	private static String[] basicPropertyName = { "code", "name", "leadModel",
			"from", "to", "distance", "startAngle", "middleTag", "pline",
			"startPole" };
	/**
	 * 线路下杆塔队列，仅保存杆塔编号信息
	 */
	private ArrayList poles;
	/**
	 * 支线标志
	 */
	private boolean isBranch;
	/**
	 * 中间接入标志
	 */
	private boolean isMiddle;

	/**
	 * 构造函数
	 */
	public AreaLine() {
		super(basicPropertyName);
		isBranch = false;
		isMiddle = false;
	}

	/**
	 * 获取杆塔队列
	 * 
	 * @return ArrayList
	 */
	public ArrayList getPoles() {
		return poles;
	}

	/**
	 * 设置线路下杆塔队列
	 * 
	 * @param poles:ArrayList:杆塔队列
	 */
	public void setPoles(ArrayList poles) {
		this.poles = poles;
	}

	/**
	 * 获取支线标志
	 * 
	 * @return boolean
	 */
	public boolean isBranch() {
		return isBranch;
	}

	/**
	 * 设置支线标志
	 * 
	 * @param isBranch:boolean:支线标志
	 */
	public void setBranch(boolean isBranch) {
		this.isBranch = isBranch;
	}

	/**
	 * 获取中间接入标志
	 * 
	 * @return boolean
	 */
	public boolean isMiddle() {
		return isMiddle;
	}

	/**
	 * 设置中间接入标志
	 * 
	 * @param isMiddle:boolean:中间接入标志
	 */
	public void setMiddle(boolean isMiddle) {
		this.isMiddle = isMiddle;
	}

}
