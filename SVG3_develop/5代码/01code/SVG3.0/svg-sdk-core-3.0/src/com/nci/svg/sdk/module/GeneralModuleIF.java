package com.nci.svg.sdk.module;

import java.io.Serializable;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;

/**
 * 所有组件模块的公共接口，提供初始化、重新初始化、开启、停止各类组件及相应的业务逻辑处理接口等功能。
 * @author Qil.Wong
 * @since 3.0
 *
 */
public interface GeneralModuleIF extends Serializable{

	/**
	 * 模块初始化成功标记
	 */
	public final static int MODULE_INITIALIZE_COMPLETE = 0;

	/**
	 * 模块初始化失败标记
	 */
	public final static int MODULE_INITIALIZE_FAILED = -1;

	/**
	 * 模块开启成功标记
	 */
	public final static int MODULE_START_COMPLETE = 0;

	/**
	 * 模块开启失败标记
	 */
	public final static int MODULE_START_FAILED = -1;

	/**
	 * 模块关闭成功标记
	 */
	public final static int MODULE_STOP_COMPLETE = 0;

	/**
	 * 模块关闭失败标记
	 */
	public final static int MODULE_STOP_FAILED = -1;
	
	/**
	 * 操作失败
	 */
	public final static int OPER_ERROR = -1;
	/**
	 * 操作成功
	 */
	public final static int OPER_SUCCESS = 0;

	/**
	 * 初始化组件

	 * @return 初始化结果。
	 * @see GeneralModuleIF#MODULE_INITIALIZE_COMPLETE
	 * @see GeneralModuleIF#MODULE_INITIALIZE_FAILED
	 */
	public int init();

	/**
	 * 重新初始化组件
	 * @param obj 重新初始化组件所需参数
	 * @return 重新初始化的结果。
	 * @see GeneralModuleIF#MODULE_INITIALIZE_COMPLETE
	 * @see GeneralModuleIF#MODULE_INITIALIZE_FAILED
	 */
	public int reInit();

	/**
	 * 开启组件
	 * @param obj 开启组件所需参数
	 * @return 开启组件的结果
	 * @see GeneralModuleIF#MODULE_START_COMPLETE
	 * @see GeneralModuleIF#MODULE_START_FAILED
	 */
	public int start();

	/**
	 * 关闭组件
	 * @param obj 关闭组件所需参数
	 * @return 关闭组件的结果
	 * @see GeneralModuleIF#MODULE_STOP_COMPLETE
	 * @see GeneralModuleIF#MODULE_STOP_FAILED
	 */
	public int stop();

	/**
	 * 组件处理逻辑业务处理
	 * @param action 逻辑业务处理名
	 * @param params 处理所需参数map
	 * @return 处理后的结果
	 */
	public ResultBean handleOper(String action, Map params);

	/**
	 * 获取组件模块的类型
	 * @return 组件模块的类型
	 */
	public String getModuleType();

	/**
	 * 获取组件模块编号
	 * @return 组件模块编号
	 */
	public String getModuleID();
	
	/**
	 * 判断组件是否在运行
	 * @return true为已经停止，false正在运行
	 */
	public boolean isStoped();

}
