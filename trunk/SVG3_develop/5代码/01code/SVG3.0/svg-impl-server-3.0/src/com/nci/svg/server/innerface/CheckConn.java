package com.nci.svg.server.innerface;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;

/**
 * <p>
 * 标题：CheckConn.java
 * </p>
 * <p>
 * 描述： 客户端与服务器端通信检查
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-09
 * @version 1.0
 */
public class CheckConn extends OperationServiceModuleAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -854156213877061459L;

	public CheckConn(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean rb = new ResultBean();
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("通信检查，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("通信检查，未能获取日志操作对象!");
		}


		if (actionName.equalsIgnoreCase(ActionNames.CHECK_URL_CONN)) {
			rb = returnSuccMsg("String", "success");
		} else {
			rb = returnErrMsg(actionName + "命令，目前该请求未实现!");
		}
		return rb;
	}

}
