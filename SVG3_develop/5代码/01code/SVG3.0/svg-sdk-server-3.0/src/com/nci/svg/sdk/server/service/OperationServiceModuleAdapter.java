package com.nci.svg.sdk.server.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.module.DefaultModuleAdapter;
import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2008-11-25
 * @功能：业务服务组件基类
 * 
 */
public abstract class OperationServiceModuleAdapter extends
		DefaultModuleAdapter {
	/**
	 * 管理组件对象
	 */
	protected ServerModuleControllerAdapter controller;
	/**
	 * 日志对象
	 */
	protected LoggerAdapter log;

	/**
	 * 模块参数存放列表
	 */
	protected HashMap parameters = new HashMap();

	public OperationServiceModuleAdapter(HashMap parameters) {
		this.parameters = parameters;
		// 获取管理组件对象
		controller = (ServerModuleControllerAdapter) parameters
				.get(ServerModuleControllerAdapter.class.toString());
		// 获取日志操作对象
		if (controller != null)
			log = controller.getLogger();
	}

	/**
	 * 服务请求，根据传入的请求信息，业务处理，返回
	 * 
	 * @param request:请求包,请求包的格式为MAP，业务服务组件根据标识获取参数
	 * @return：处理结果，
	 */
	public abstract ResultBean handleOper(String actionName, Map requestParams);

	/**
	 * 获取请求参数中指定参数名的值
	 * 
	 * @param requestParams:请求包参数MAP
	 * @param requestName:请求包中参数名
	 * @return
	 */
	protected Object getRequestParameter(Object requestParams, String paramName) {
		if (requestParams instanceof Map) {
			Map params = (Map) requestParams;
			try
			{
				
			Object[] values = (Object[]) params.get(paramName);
			if (values != null)
				return values[0];
			}
			catch(Exception ex){
				Object value = params.get(paramName);
				return value;
			}
		}
		return null;
	}

	/**
	 * 将iso8859_1编码的字符串转换为UTF-8的字符串
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 转换好的字符串
	 * @throws UnsupportedEncodingException
	 */
	protected String isoToUtf8(String str) throws UnsupportedEncodingException {
		String result = "";
		result = (str == null) ? "" : new String(str.getBytes("ISO-8859-1"),
				"UTF-8");
		return result;
	}
	
	/**
	 * 将字符串转变为字符串数组
	 * @param name
	 * @return
	 */
	protected String[] changeStringToArray(String name) {
		if (name != null && name.length() > 0) {
			String[] names = { name };
			return names;
		} else {
			return null;
		}
	}


	/**
	 * 组件初始化
	 * 
	 * @return：初始化结果
	 */
	public int init() {
		return OPER_SUCCESS;
	}

	/**
	 * 调用系统其他服务
	 * 
	 * @param action：服务名
	 * @param requestParams：请求列表
	 * @return：返回结果
	 */
	public ResultBean refersTo(String action, Map requestParams) {
		if (controller == null || controller.getServiceShunt() == null) {
			ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR, "调用组件有误", null, null);
			return bean;
		}
		return controller.getServiceShunt().serviceMultiplexing(action,
				requestParams);
	}

	/**
	 * 调用系统外部接口
	 * @param bussinessID:业务系统编号
	 * @param action：接口服务名
	 * @param requestParams：请求列表
	 * @return：返回结果
	 */
	public ResultBean refersToOperInterface(String bussinessID, String action,
			Map requestParams) {
		if (controller == null || controller.getOperInterfaceManager() == null) {
			ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR, "调用组件有误", null, null);
			return bean;
		}
		return controller.getOperInterfaceManager().OperInterfaceMultiplexing(
				bussinessID, action, requestParams);
	}

	public String getModuleType() {
		return ModuleDefines.OPERATION_SERVICE_MODULE;
	}
	
}
