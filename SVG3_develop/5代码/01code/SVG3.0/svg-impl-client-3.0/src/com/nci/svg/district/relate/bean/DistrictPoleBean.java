package com.nci.svg.district.relate.bean;

import com.nci.svg.district.relate.BasicProperty;

public class DistrictPoleBean extends BasicProperty {
	/**
	 * @apram 杆塔基础属性，依次为：
	 * @param 杆塔编号，杆塔名称
	 */
	private static String[] basicPropertyName = { "p_objid", "p_name" };

	/**
	 * 构造函数
	 */
	public DistrictPoleBean() {
		super(basicPropertyName);
	}
}
