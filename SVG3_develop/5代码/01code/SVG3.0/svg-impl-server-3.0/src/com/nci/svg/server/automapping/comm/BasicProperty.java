package com.nci.svg.server.automapping.comm;

import java.util.LinkedHashMap;

/**
 * <p>
 * 标题：BasicPorperty.java
 * </p>
 * <p>
 * 描述：基础属性类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-03-05
 * @version 1.0
 */
public class BasicProperty {
	/**
	 * 设备基础属性队列
	 */
	private LinkedHashMap properties;
	/**
	 * 设备基础属性名称队列
	 */
	private String[] propertNames;

	/**
	 * 构造函数
	 */
	public BasicProperty() {
		properties = new LinkedHashMap();
		propertNames = new String[0];
	}

	/**
	 * 构造函数
	 * 
	 * @param propertName
	 */
	public BasicProperty(String[] propertName) {
		if (propertName != null && propertName.length > 0)
			this.propertNames = propertName;
		else
			this.propertNames = new String[0];
		properties = new LinkedHashMap();
	}

	/**
	 * 获取指定属性名称的属性值
	 * 
	 * @param propertName:Object:属性值
	 * @return
	 */
	public Object getProperty(String proName) {
		if (checkNames(proName))
			return properties.get(proName);
		else
			return null;
	}

	/**
	 * 设置指定属性名称的属性值
	 * 
	 * @param proName
	 * @param proValue
	 */
	public void setProperty(String proName, Object proValue) {
		if (checkNames(proName))
			properties.put(proName, proValue);
	}

	/**
	 * 判断指定属性名称是否包含在属性队列中
	 * 
	 * @param proName:String:属性名
	 * @return 是否存在
	 */
	private boolean checkNames(String proName) {
		for (int i = 0, size = propertNames.length; i < size; i++) {
			if (proName.equals(propertNames[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取设备基础属性名称队列
	 * 
	 * @return String
	 */
	public String[] getPropertNames() {
		return propertNames;
	}

	/**
	 * 设置设备基础属性名称队列
	 * 
	 * @param propertName:String[]:设备基础属性名称队列
	 */
	public void setPropertNames(String[] propertNames) {
		this.propertNames = propertNames;
	}

	/**
	 * 获取基础属性值
	 * 
	 * @return LinkedHashMap
	 */
	protected LinkedHashMap getProperties() {
		return properties;
	}

	/**
	 * 设置基础属性值
	 * 
	 * @param properties:LinkedHashMap:基础属性值
	 */
	protected void setProperties(LinkedHashMap properties) {
		this.properties = properties;
	}
}
