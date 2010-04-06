package com.nci.svg.district.relate.bean;

import java.util.HashMap;

import com.nci.svg.district.relate.BasicProperty;

public class DistrictAreaBean extends BasicProperty {
	/**
	 * @apram 台区基础属性，依次为：
	 * @param 台区编号,
	 *            台区名称
	 */
	private static String[] basicPropertyName = { "sd_objid", "sd_name" };

	/**
	 * 配变列表
	 */
	private HashMap<String, DistrictTransformerBean> transformers;
	/**
	 * 配变箱列表
	 */
	private HashMap<String, DistrictBoxBean> boxes;
	/**
	 * 线路列表
	 */
	private HashMap<String, DistrictLineBean> lines;
	/**
	 * 杆塔列表
	 */
	private HashMap<String, DistrictPoleBean> poles;

	/**
	 * 构造函数
	 */
	public DistrictAreaBean() {
		super(basicPropertyName);
		transformers = new HashMap<String, DistrictTransformerBean>();
		boxes = new HashMap<String, DistrictBoxBean>();
		lines = new HashMap<String, DistrictLineBean>();
		poles = new HashMap<String, DistrictPoleBean>();
	}

	/**
	 * 2009-4-10 Add by ZHM 获取配变对象组
	 * 
	 * @return
	 */
	public HashMap<String, DistrictTransformerBean> getTransformers() {
		return transformers;
	}

	/**
	 * 2009-4-10 Add by ZHM 设置配变对象组
	 * 
	 * @param transformers
	 */
	public void setTransformers(
			HashMap<String, DistrictTransformerBean> transformers) {
		this.transformers = transformers;
	}

	/**
	 * 2009-4-10 Add by ZHM 获取配变箱对象组
	 * 
	 * @return
	 */
	public HashMap<String, DistrictBoxBean> getBoxs() {
		return boxes;
	}

	/**
	 * 2009-4-10 Add by ZHM 设置配变箱对象组
	 * 
	 * @param boxs
	 */
	public void setBoxs(HashMap<String, DistrictBoxBean> boxs) {
		this.boxes = boxs;
	}

	/**
	 * 2009-4-10 Add by ZHM 获取线路对象组
	 * 
	 * @return
	 */
	public HashMap<String, DistrictLineBean> getLine() {
		return lines;
	}

	/**
	 * 2009-4-10 Add by ZHM 设置线路对象组
	 * 
	 * @param line
	 */
	public void setLine(HashMap<String, DistrictLineBean> lines) {
		this.lines = lines;
	}

	/**
	 * 返回
	 * 
	 * @return poles
	 */
	public HashMap<String, DistrictPoleBean> getPoles() {
		return poles;
	}

	/**
	 * 设置
	 * 
	 * @param poles
	 */
	public void setPoles(HashMap<String, DistrictPoleBean> poles) {
		this.poles = poles;
	}
}
