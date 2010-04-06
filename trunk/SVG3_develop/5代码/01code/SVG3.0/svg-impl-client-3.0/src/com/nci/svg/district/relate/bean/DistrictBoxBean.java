package com.nci.svg.district.relate.bean;

import com.nci.svg.district.relate.BasicProperty;

public class DistrictBoxBean extends BasicProperty {
	/**
	 * @apram 台区基础属性，依次为：
	 * @param 台区编号,
	 *            台区名称
	 */
	private static String[] basicPropertyName = { "sl_objid", "sl_name" };
	/**
	 * 构造函数
	 */
	public DistrictBoxBean() {
		super(basicPropertyName);
	}

}
