package com.nci.svg.district.relate.bean;

import com.nci.svg.district.relate.BasicProperty;

public class DistrictLopeBean extends BasicProperty {
	/**
	 * @apram 回路基础属性，依次为：
	 * @param 回路编号
	 */
	private static String[] basicPropertyName = { "sp_id", "sp_name", "poleid" };

	/**
	 * 构造函数
	 */
	public DistrictLopeBean() {
		super(basicPropertyName);
	}
}
