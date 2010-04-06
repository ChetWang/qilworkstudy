package com.nci.svg.server.automapping.system;

import com.nci.svg.server.automapping.comm.BasicProperty;

/**
 * <p>
 * 标题：SystemLine.java
 * </p>
 * <p>
 * 描述：系统图自动成图用的线路类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-03-10
 * @version 1.0
 */
public class SystemLine extends BasicProperty {
	/**
	 * @apram 线路基础属性，依次为：
	 * @param 名称、编号、起始厂站编号、起始厂站名称、终止厂站编号、
	 * @param 终止厂站名称、电压等级、应用编号、所属区域、
	 */
	private static String[] basicPropertyName = { "Name", "Id",
			"StartSubId", "startEquipmentName", "EndSubId",
			"endEquipmentName", "VoltageLevel", "AppCode", "Dwdm", "zcsx" };

	/**
	 * 构造函数
	 */
	public SystemLine() {
		super(basicPropertyName);
	}
}
