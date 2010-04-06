package com.nci.svg.sdk.server.operationinterface;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-11-24
 * @功能：服务器端业务接口管理组件抽象类
 *
 */
public abstract class OperInterfaceManagerAdapter extends ServerModuleAdapter {

	public OperInterfaceManagerAdapter(HashMap parameters)
	{
		super(parameters);
	}
	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#getModuleID()
	 */
	public String getModuleID() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#getModuleType()
	 */
	public String getModuleType() {
		// TODO Auto-generated method stub
		return ModuleDefines.OPER_INTERFACE_MANAGER;
	}
	
	/**
	 * 根据传入的业务接口名称获取业务接口对象
	 * @param bussinessID：业务系统编号
	 * @param actionName:业务接口名称
	 * @return：业务接口组件对象
	 */
	public abstract OperInterfaceAdapter getOperationModule(String bussinessID, String actionName);

	/**
	 * 业务接口流转
	 * @param bussinessID:系统编号
	 * @param action:服务名
	 * @param requestParams：请求参数集
	 * @return：返回结果包
	 */
	public abstract ResultBean OperInterfaceMultiplexing(String bussinessID,String action,Map requestParams);
}
