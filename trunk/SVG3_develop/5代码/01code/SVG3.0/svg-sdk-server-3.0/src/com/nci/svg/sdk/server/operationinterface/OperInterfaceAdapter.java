package com.nci.svg.sdk.server.operationinterface;

import java.util.HashMap;

import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;


/**
 * <p>
 * 标题：OperInterfaceAdapter.java
 * </p>
 * <p>
 * 描述： 业务系统信息获取接口
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-05
 * @version 1.0
 */
public abstract class OperInterfaceAdapter extends OperationServiceModuleAdapter{
	/*	
	 * 接口参数用的结构
	 *	<?xml version="1.0" encoding="UTF-8"?>
	 *	<params>
	 *		<param>
	 *			<!-- 参数名 -->
	 *			<name></name>
	 *			<!-- 参数值 -->
	 *			<value></value>
	 *		</param>
	 *	</params>
	 *	返回数据的结构
	 *	<?xml version="1.0" encoding="UTF-8"?>
	 *	<results>
	 *		<result>
	 *			<!-- 返回值名称 -->
	 *			<name></name>
	 *			<!-- 返回值 -->
	 *			<value></value>
	 *		</result>
	 *	</results>
	*/	
	
	public OperInterfaceAdapter(HashMap parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	public String getModuleType() {
		// TODO Auto-generated method stub
		return ModuleDefines.OPER_INTERFACE_MODULE;
	}

	


}
