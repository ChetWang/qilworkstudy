package com.nci.svg.district.relate.bean;

import com.nci.svg.district.relate.BasicProperty;

public class DistrictTransformerBean extends BasicProperty {
	/**
	 * @apram ���������ԣ�����Ϊ��
	 * @param �����, �������
	 */
	private static String[] basicPropertyName = { "sd_objid", "sd_name" };

	/**
	 * ���캯��
	 */
	public DistrictTransformerBean() {
		super(basicPropertyName);
	}

}
