package com.nci.svg.sdk.module;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;

/**
 * 默认的模块适配器，将所有GeneralModuleIF的接口都提供默认实现，并将初始化init和开启组件start在构造函数中执行。
 * 
 * @author Qil.Wong
 * 
 */
public abstract class DefaultModuleAdapter implements GeneralModuleIF {

	/**
	 * 模块参数存放列表
	 */
	protected HashMap parameters = new HashMap();

	/**
	 * 参数类型，初始化所需参数
	 */
	public final static String PARAM_TYPE_INIT = "init";
	/**
	 * 参数类型，开启模块所需参数
	 */
	public final static String PARAM_TYPE_START = "start";
	/**
	 * 参数类型，停止模块所需参数
	 */
	public final static String PARAM_TYPE_STOP = "stop";
	/**
	 * 参数类型，重新初始化所需参数
	 */
	public final static String PARAM_TYPE_REINIT = "reinit";

	/**
	 * 模块启动与否标记
	 */
	protected boolean startFlag = false;

	protected String moduleUUID = null;

	/**
	 * 构造函数，默认构造出对象后就启动
	 */
	public DefaultModuleAdapter() {

	}


	public DefaultModuleAdapter(HashMap parameters) {
		this.parameters = parameters;
		System.out.println(this.toString());
		// int result = init();
		// if (result != MODULE_INITIALIZE_COMPLETE) {
		// new ModuleInitializeFailedException(this).printStackTrace();
		// }
	}

	public int init() {
		return MODULE_INITIALIZE_COMPLETE;
	}

	public int start() {
		startFlag = true;
		return MODULE_START_COMPLETE;
	}

	public int stop() {
		startFlag = false;
		return MODULE_STOP_COMPLETE;
	}

	public int reInit() {
		return MODULE_INITIALIZE_COMPLETE;
	}

	/**
	 * 设置所需类型参数
	 * 
	 * @param paramType
	 *            参数类型
	 * @param obj
	 *            参数对象
	 */
	public void putParameter(String paramType, Object obj) {
		parameters.put(paramType, obj);
	}

	/**
	 * 获取类型参数
	 * 
	 * @param paramType
	 *            参数类型
	 * @return 参数类型对应的对象
	 */
	public Object getParameter(String paramType) {
		return parameters.get(paramType);
	}

	public boolean isStoped() {
		return !startFlag;
	}

	public ResultBean handleOper(String action, Map params) {
		ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR, "业务处理有误",
				null, null);
		return bean;
	}

	public String getModuleID() {
		return moduleUUID;
	}

	public HashMap getParameters() {
		return parameters;
	}

	/**
	 * 设置操作失败返回对象
	 * 
	 * @param text:String:错误信息
	 * @return 返回值对象
	 */
	protected ResultBean returnErrMsg(String text) {
		ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR, text, null,
				null);
		return bean;
	}
	
	/**
	 * 设置操作成功返回对象
	 * @param type:String:返回对象类型
	 * @param obj:Object:返回对象值
	 * @return 返回值对象
	 */
	protected ResultBean returnSuccMsg(String type, Object obj){
		return new ResultBean(ResultBean.RETURN_SUCCESS, null, type, obj);
	}
}
