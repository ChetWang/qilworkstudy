package com.nci.svg.server.automapping.area;

import java.util.LinkedHashMap;

import com.nci.svg.server.automapping.comm.BasicProperty;

/**
 * <p>
 * 标题：Area.java
 * </p>
 * <p>
 * 描述：台区类
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
public class Area extends BasicProperty {
	/**
	 * @param 台区基础属性，依次为：
	 * @param 台区编号、台区名称、电压等级、
	 * @param 公司代码、所属营业所代码、小区代码
	 */
	private static String[] basicPropertyName = { "code", "name", "basevol",
			"company", "serviceStation", "village" };
	/**
	 * 台区变压器
	 */
	private AreaTransformer transformer;
	/**
	 * 物理杆塔队列
	 */
	private LinkedHashMap electricPoleList;
	/**
	 * 线路队列
	 */
	private LinkedHashMap linesList;

	public Area() {
		super(basicPropertyName);
		electricPoleList = new LinkedHashMap();
	}

	/**
	 * 获取变压器对象
	 * 
	 * @return Transformer
	 */
	public AreaTransformer getTransformer() {
		return transformer;
	}

	/**
	 * 设置变压器对象
	 * 
	 * @param transformer:Transformer:变压器
	 */
	public void setTransformer(AreaTransformer transformer) {
		this.transformer = transformer;
	}

	/**
	 * 获取物理杆塔队列
	 * 
	 * @return ArrayList
	 */
	public LinkedHashMap getElectricPoleList() {
		return electricPoleList;
	}

	/**
	 * 设置物理杆塔队列
	 * 
	 * @param electricPoleList:ArrayList:物理杆塔队列
	 */
	public void setElectricPoleList(LinkedHashMap electricPoleList) {
		this.electricPoleList = electricPoleList;
	}

	/**
	 * 获取线路队列
	 * 
	 * @return ArrayList
	 */
	public LinkedHashMap getLinesList() {
		return linesList;
	}

	/**
	 * 设置线路队列
	 * 
	 * @param linesList:ArrayList:线路队列
	 */
	public void setLinesList(LinkedHashMap linesList) {
		this.linesList = linesList;
	}

}
