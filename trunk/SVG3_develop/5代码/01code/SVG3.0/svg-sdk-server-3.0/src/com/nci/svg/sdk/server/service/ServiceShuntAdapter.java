
package com.nci.svg.sdk.server.service;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：服务器端业务服务路由组件抽象类
 *
 */
public abstract class ServiceShuntAdapter extends ServerModuleAdapter {
	
	/**
	 * 模块类型
	 */
	public static final String SERVICE_MODULE= "ServiceModule";
	/**
	 * 单服务类型
	 */
	public static final String SERVICE_ACTION = "ServiceAction";
	
	/**
	 * 所有服务,仅在获取服务状态、启动服务和停止服务时有效
	 */
	public static final String SERVICE_ALL = "ServiceAll";
	
	/**
	 * 增加，新注册
	 */
	public static final String HANDLE_ADD = "add";
	/**
	 * 修改
	 */
	public static final String HANDLE_MODIFY = "modify";
	/**
	 * 删除
	 */
	public static final String HANDLE_DELETE = "delete";
	/**
	 * 启动服务
	 */
	public static final String HANDLE_START = "start";
	/**
	 * 停止服务
	 */
	public static final String HANDLE_STOP = "stop";
	/**
	 * 服务路由，服务标识在request中体现
	 * @param request：请求包
	 * @return：返回结果包
	 */
	public abstract ResultBean shuntService(Object request);
	
	/**
	 * 服务路由，服务于组件之间访问
	 * @param action:服务名
	 * @param requestParams：请求参数集
	 * @return：返回结果包
	 */
	public abstract ResultBean serviceMultiplexing(String action,Map requestParams);

	public String getModuleType() {
		// TODO Auto-generated method stub
		return ModuleDefines.SERVICE_SHUNT_MANAGER;
	}
	public ServiceShuntAdapter(HashMap parameters)
	{
		super(parameters);
	}
	
	/**
	 * 根据传入的参数，获取服务action状态
	 * @param serviceType：服务类型，SERVICE_MODULE，SERVICE_ACTION，SERVICE_ALL
	 * @param name：对应的名称
	 * @return：查询结果集
	 */
	public abstract ResultBean getServiceStatus(String serviceType,String name);
	
	/**
	 * 根据传入的参数，对服务进行维护
	 * @param handleType：处理方式
	 * HANDLE_ADD，HANDLE_MODIFY，HANDLE_DELETE，HANDLE_START，HANDLE_STOP
	 * @param serviceType：服务类型，SERVICE_MODULE，SERVICE_ACTION，SERVICE_ALL
	 * @param name：对应的名称
	 * @return：维护结果集
	 */
	public abstract ResultBean handleService(String handleType,String serviceType,String name);
	
	
	
}
