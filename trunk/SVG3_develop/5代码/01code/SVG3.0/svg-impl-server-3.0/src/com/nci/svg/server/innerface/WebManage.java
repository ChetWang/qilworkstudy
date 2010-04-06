package com.nci.svg.server.innerface;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.cache.CacheManagerKeys;

/**
 * <p>
 * 标题：WebManage.java
 * </p>
 * <p>
 * 描述：Web管理页面响应服务
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-17
 * @version 1.0
 */
public class WebManage extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5514924255543862406L;

	public WebManage(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean rb = new ResultBean();
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("eb管理页面响应服务，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("eb管理页面响应服务，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "Web管理页面响应服务类，获取到‘" + actionName
				+ "’请求命令！");

		if (actionName.equalsIgnoreCase(ActionNames.CONFIG_CODE_DATA)) {
			// 代码管理响应
			String type = (String) getRequestParameter(requestParams, "type"); // 动作类型
			String codeName = (String) getRequestParameter(requestParams,
					"codename"); // 代码短名
			String value = (String) getRequestParameter(requestParams, "value"); // 代码值
			String desc = (String) getRequestParameter(requestParams, "desc"); // 描述
			rb = configCodeData(type, codeName, value, desc);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.CONFIG_SERVICE_MANAGER)) {
			// 组件升级管理
			String type = (String) getRequestParameter(requestParams, "type"); // 动作类型
			String managerType = (String) getRequestParameter(requestParams,
					"managertype");// 管理组件的短名
			String desc = (String) getRequestParameter(requestParams, "desc"); // 描述
			rb = configServiceManager(type, managerType, desc);
		} else if (actionName.equalsIgnoreCase(ActionNames.GET_SERVICE_STATUS)) {
			// 服务状态查询
			String type = (String) getRequestParameter(requestParams,
					"servicetype"); // 动作类型
			String serviceName = (String) getRequestParameter(requestParams,
					"servicename");// 服务名
			rb = getServiceStatus(type, serviceName);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.CONFIG_SERVICE_MODULE)) {
			// 服务管理
			String type = (String) getRequestParameter(requestParams, "type"); // 动作类型
			String serviceType = (String) getRequestParameter(requestParams,
					"servicetype");
			String serviceName = (String) getRequestParameter(requestParams,
					"servicename");// 服务名
			String desc = (String) getRequestParameter(requestParams, "desc"); // 描述
			rb = configServiceModule(type, serviceType, serviceName, desc);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.CONFIG_SYMBOL_MANAGER)) {
			// 图元管理
			String type = (String) getRequestParameter(requestParams, "type"); // 动作类型
			String symbolType = (String) getRequestParameter(requestParams,
					"symboltype");
			String symbolName = (String) getRequestParameter(requestParams,
					"symbolname");// 服务名
			String desc = (String) getRequestParameter(requestParams, "desc");// 描述
			rb = configSymbolManager(type, symbolType, symbolName, desc);
		} else if (actionName.equalsIgnoreCase(ActionNames.CONFIG_INDUNORM)) {
			// 业务规范管理
			String type = (String) getRequestParameter(requestParams, "type"); // 动作类型
			String indunormType = (String) getRequestParameter(requestParams,
					"indunormtype");// 规范操作种类
			String desc = (String) getRequestParameter(requestParams, "desc");// 描述
			rb = configIndunorm(type, indunormType, desc);
		} else {
			rb = returnErrMsg(actionName + "命令，目前该请求未实现!");
		}
		return rb;
	}

	/**
	 * Web管理 代码管理响应函数
	 * 
	 * @param type
	 *            String 动作类型，(add/delete/modify)
	 * @param codeName
	 *            String 代码短名
	 * @param value
	 *            String 代码值
	 * @param desc
	 *            String 代码描述
	 * @return
	 */
	private ResultBean configCodeData(String type, String codeName,
			String value, String desc) {
		ResultBean rb = new ResultBean();
		HashMap params = new HashMap();
		params.put("codename", changeStringToArray(codeName));
		params.put("codevalue", changeStringToArray(value));
		params.put("codedesc", changeStringToArray(desc));
		params.put(ActionNames.action, changeStringToArray("modifyCodeCache"));
		// params.put(ActionNames.action, type);
		rb = refersTo(CacheManagerKeys.CODE_MANAGER.toUpperCase(), params);
		return rb;
	}

	/**
	 * Web管理 组件升级管理响应函数
	 * 
	 * @param type
	 *            String 动作类型，(add/delete/modify)
	 * @param managerType
	 *            String 管理组件的短名
	 * @param desc
	 *            String 描述
	 * @return
	 */
	private ResultBean configServiceManager(String type, String managerType,
			String desc) {
		return controller.getServiceModuleManager().configModule(type,
				managerType);
	}

	/**
	 * 获取服务器提供服务的状态
	 * 
	 * @param serviceName
	 *            String 服务名
	 * @return
	 */
	private ResultBean getServiceStatus(String serviceType, String serviceName) {
		return controller.getServiceShunt().getServiceStatus(serviceType,
				serviceName);
	}

	/**
	 * 服务管理响应函数
	 * 
	 * @param type
	 *            String 动作类型(add/delete/modify/start/stop)
	 * @param managerType
	 *            String 组件短名
	 * @param serviceName
	 *            String 服务名
	 * @param desc
	 *            String 描述
	 * @return
	 */
	private ResultBean configServiceModule(String type, String managerType,
			String serviceName, String desc) {
		return controller.getServiceShunt().handleService(type, managerType,
				serviceName);
	}

	/**
	 * Web管理 图元管理响应函数
	 * 
	 * @param type
	 *            String
	 *            动作类型(validity/cancel_validity/publish/cancel_publish/delete)
	 * @param symbolType
	 *            String 图元大类(graphunit/template)
	 * @param symbolName
	 *            String 图元名称
	 * @param desc
	 *            String 描述
	 * @return
	 */
	private ResultBean configSymbolManager(String type, String symbolType,
			String symbolName, String desc) {
		ResultBean rb = new ResultBean();
		HashMap params = new HashMap();
		params.put("symboltype", changeStringToArray(symbolType));
		params.put("symbolname", changeStringToArray(symbolName));
		params.put("codedesc", changeStringToArray(desc));
		params
				.put(ActionNames.action,
						changeStringToArray("modifySymbolCache"));
		// params.put(ActionNames.action, type);
		rb = refersTo(CacheManagerKeys.SYMBOL_MANAGER.toUpperCase(), params);
		return rb;
	}

	/**
	 * Web管理 行业规范管理响应函数
	 * 
	 * @param type
	 *            String 动作类型，(add/delete/modify)
	 * @param indunormType
	 *            String 规范操作种类(indunorm/desc/metadata/fields)
	 * @param desc
	 *            String 描述
	 * @return
	 */
	private ResultBean configIndunorm(String type, String indunormType,
			String desc) {
		return null;
	}

}
