package com.nci.svg.server.automapping.area;

import com.nci.svg.server.automapping.comm.BasicProperty;

/**
 * <p>
 * 标题：Transformer.java
 * </p>
 * <p>
 * 描述：台区图自动成图用的变压器类
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
public class AreaTransformer extends BasicProperty {
	
	/**
	 * @param 变压器基础属性，依次为：
	 * @param 变压器编号、变压器名称、电压等级、变压器容量
	 */
	private static String[] basicPropertyName = { "code", "name", "voltage",
			"capacity" };
	
	/**
	 * 构造函数
	 */
	public AreaTransformer(){
		super(basicPropertyName);
	}
}
