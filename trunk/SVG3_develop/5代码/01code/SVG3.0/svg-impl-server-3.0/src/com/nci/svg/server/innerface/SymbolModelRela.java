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
 * 标题：SymbolModuleRela.java
 * </p>
 * <p>
 * 描述：图元与业务模型关联操作
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
public class SymbolModelRela extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2044328103111466620L;

	public SymbolModelRela(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean rb = new ResultBean();
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("图元与业务模型关联操作，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("图元与业务模型关联操作，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "图元与业务模型关联操作类，获取到‘" + actionName
				+ "’请求命令！");

		String bussinessID = (String) getRequestParameter(requestParams,
				ActionParams.BUSINESS_ID);
		String graphUnitID = (String) getRequestParameter(requestParams,
				ActionParams.SYMBOL_ID);
		String moduleID = (String) getRequestParameter(requestParams,
				ActionParams.MODULE_ID);
		HashMap params = new HashMap();
		params.put(ActionParams.BUSINESS_ID, bussinessID);
		params.put(ActionParams.SYMBOL_ID, graphUnitID);
		params.put(ActionParams.MODULE_ID, moduleID);

		if (actionName.equalsIgnoreCase(ActionNames.GET_GRAPH_UNIT_AND_MODULE_RELA)) {
			// 图元与模型关系获取
			rb = refersToOperInterface(bussinessID,
					OperInterfaceDefines.GET_GRAPH_UNIT_AND_MODULE_RELA_NAME,
					params);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.MODIFY_GRAPH_UNIT_AND_MODULE_RELA)) {
			// 图元与模型关系维护
			rb = refersToOperInterface(
					bussinessID,
					OperInterfaceDefines.MODIFY_GRAPH_UNIT_AND_MODULE_RELA_NAME,
					params);
		} else {
			rb = returnErrMsg(actionName + "命令，目前该请求未实现!");
		}

		return rb;
	}

}
