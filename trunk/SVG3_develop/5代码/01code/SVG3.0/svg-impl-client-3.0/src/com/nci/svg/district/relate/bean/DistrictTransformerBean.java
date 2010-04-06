package com.nci.svg.district.relate.bean;

import com.nci.svg.district.relate.BasicProperty;

public class DistrictTransformerBean extends BasicProperty {
	/**
	 * @apram 配变基础属性，依次为：
	 * @param 配变编号, 配变名称
	 */
	private static String[] basicPropertyName = { "sd_objid", "sd_name" };

	/**
	 * 构造函数
	 */
	public DistrictTransformerBean() {
		super(basicPropertyName);
	}

}
