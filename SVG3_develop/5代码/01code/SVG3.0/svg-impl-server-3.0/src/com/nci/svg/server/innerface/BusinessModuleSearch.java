package com.nci.svg.server.innerface;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.operinterface.OperInterfaceDefines;

/**
 * <p>
 * 标题：BussinessModuleSearch.java
 * </p>
 * <p>
 * 描述： 业务系统查询类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-04
 * @version 1.0
 */
public class BusinessModuleSearch extends OperationServiceModuleAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7502385969946301310L;

	public BusinessModuleSearch(HashMap parameters) {
		super(parameters);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.service.OperationServiceModuleAdapter#handleOper(java.lang.Object)
	 */
	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean rb = new ResultBean();
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("业务系统查询，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("业务系统查询，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "业务系统查询类，获取到‘" + actionName
				+ "’请求命令！");

		// 准备业务请求数据
		String businessID = (String) getRequestParameter(requestParams,
				ActionParams.BUSINESS_ID);
		String moduleID = (String) getRequestParameter(requestParams,
				ActionParams.MODULE_ID);
		String type = (String) getRequestParameter(requestParams, "type");
		HashMap params = new HashMap();
		params.put(ActionParams.BUSINESS_ID, businessID);
		params.put(ActionParams.MODULE_ID, moduleID);
		params.put(ActionParams.TYPE, type);

		// 业务请求判定分配
		if (actionName.equalsIgnoreCase(ActionNames.GET_MODEL_LIST)) {
			// 获取业务模型列表
			rb = refersToOperInterface(businessID,
					OperInterfaceDefines.GET_MODEL_LIST_NAME, params);
		} else if (actionName.equalsIgnoreCase(ActionNames.GET_MODEL_PARAMS)) {
			// 获取业务模型属性
			rb = refersToOperInterface(businessID,
					OperInterfaceDefines.GET_MODEL_PARAMS_NAME, params);
		} else if (actionName.equalsIgnoreCase(ActionNames.GET_MODEL_ACTIONS)) {
			// 获取业务模型动作列表
			rb = refersToOperInterface(businessID,
					OperInterfaceDefines.GET_MODEL_ACTIONLIST_NAME, params);
		} else {
			rb = new ResultBean(ResultBean.RETURN_ERROR, actionName + "命令，目前该请求未实现", null,
					null);
		}

		return rb;
	}
}
