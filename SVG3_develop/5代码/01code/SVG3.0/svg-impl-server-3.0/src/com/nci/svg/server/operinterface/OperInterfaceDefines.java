/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-12-15
 * @功能：TODO
 *
 */
package com.nci.svg.server.operinterface;

/**
 * @author yx.nci
 * 
 */
public class OperInterfaceDefines {
	/**
	 * 获取业务系统模型列表 String moduleID:模型编号
	 */
	public static final int GET_MODEL_LIST = 1;
	/**
	 * 获取业务系统模型列表外部接口名
	 */
	public static final String GET_MODEL_LIST_NAME = "getModelListOutterFace";

	/**
	 * 获取业务系统模型属性 String moduleID:模型编号
	 */
	public static final int GET_MODEL_PARAMS = 2;
	/**
	 * 获取业务系统模型属性外部接口名
	 */
	public static final String GET_MODEL_PARAMS_NAME = "getModelParamsOutterFace";

	/**
	 * 获取业务系统模型动作列表 String moduleID:模型编号
	 */
	public static final int GET_MODEL_ACTIONLIST = 3;
	/**
	 * 获取业务系统模型动作列表外部接口名
	 */
	public static final String GET_MODEL_ACTIONLIST_NAME = "getModelActionsOutterFace";

	/**
	 * 获取业务模型状态 String moduleID:模型编号
	 */
	public static final int GET_MODEL_STATUS = 4;
	/**
	 * 获取业务模型状态外部接口名
	 */
	public static final String GET_MODEL_STATUS_NAME = "getModelStatusOutterFace";

	/**
	 * 图元与模型关系获取 String graphUnitID:图元编号 String moduleID:模型编号
	 */
	public static final int GET_GRAPH_UNIT_AND_MODULE_RELA = 5;
	/**
	 * 图元与模型关系获取外部接口名
	 */
	public static final String GET_GRAPH_UNIT_AND_MODULE_RELA_NAME = "getSymbolModuleRelaOutterFace";

	/**
	 * 图元与模型关系维护 String graphUnitID:图元编号 String moduleID:模型编号
	 */
	public static final int MODIFY_GRAPH_UNIT_AND_MODULE_RELA = 6;
	/**
	 * 图元与模型关系维护外部接口名
	 */
	public static final String MODIFY_GRAPH_UNIT_AND_MODULE_RELA_NAME = "modifySymbolModuleRelaOutterFace";

}
