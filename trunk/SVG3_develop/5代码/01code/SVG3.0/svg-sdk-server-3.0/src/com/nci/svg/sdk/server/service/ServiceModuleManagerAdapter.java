package com.nci.svg.sdk.server.service;

import java.util.HashMap;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：服务器端业务服务管理组件抽象类
 *
 */
public abstract class ServiceModuleManagerAdapter extends ServerModuleAdapter {
	/**
	 * 增加，新注册
	 */
	public static final String OPER_ADD = "add";
	/**
	 * 修改
	 */
	public static final String OPER_MODIFY = "modify";
	/**
	 * 删除
	 */
	public static final String OPER_DELETE = "delete";
	/**
	 * 抽象构造函数，首先构造最通用的服务端组件
	 */
	public ServiceModuleManagerAdapter(HashMap parameters)
	{
		super(parameters);
	}

	public String getModuleType() {
		return ModuleDefines.SERVICE_MODULE_MANAGER;
	}
	
	/**
	 * 配置管理组件
	 * @param operType:配置类型
	 * @param shortName：组件短名
	 * @return：配置结果，成功返回OPER_SUCCESS,失败返回OPER_ERROR
	 */
	public abstract ResultBean configModule(String operType,String shortName);
	
	

}
