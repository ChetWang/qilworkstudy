package com.nci.svg.sdk.communication;

/**
 * <p>
 * 标题：BussinessModuleSearch.java
 * </p>
 * <p>
 * 描述： 客户端请求命令名
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-12
 * @version 1.0 描述中的参数名称均保存在ActionParams中
 */
public class ActionNames {
	/**
	 * 系统请求
	 */
	public static final String action = "action";

	/**
	 * 获取客户端升级模块
	 * 
	 * @param MODULE_TYPE:
	 *            String 组件类型，分为平台组件和业务组件， 当组件类型为空时返回全部客户端组件的版本信息
	 */
	public static final String GET_UPGRADE_MODULE = "getUpgradeModule";

	/**
	 * 下载客户端升级模块
	 * 
	 * @param MODULE_SHORT_NAME
	 *            String 组件名称
	 */
	public static final String DOWN_UPGRADE_MODULE = "downloadUpgradeModule";

	/**
	 * 获取系统代码列表
	 * 
	 * @param MODULE_NAME
	 *            String 代码模型名称
	 * @param EXPRSTRING
	 *            String 过滤条件字符串
	 */
	public static final String GET_SVG_CODES = "getSvgCodes";

	/**
	 * 获取系统支持图形文件类型列表
	 * 
	 * @param BUSINESS_ID
	 *            String 业务系统编号
	 */
	public static final String GET_SUPPORT_FILE_TYPE = "getSupportFileType";

	/**
	 * 保存图元
	 * 
	 * @param ID
	 *            String 图元编号
	 * @param SYMBOL_TYPE
	 *            String 获取图元大类（graphunit、template）
	 * @param OPERATOR
	 *            String 获取图元操作人员
	 * @param NAME
	 *            String 获取图元名称
	 * @param CONTENT
	 *            String 获取图元内容
	 * @param VARIETY
	 *            String 获取图元类型
	 * @param PARAM1
	 *            String 获取param1参数
	 * @param PARAM2
	 *            String 获取param2参数
	 * @param PARAM3
	 *            String 获取param3参数
	 */
	public static final String SAVE_SYMBOL = "saveSymbol";

	/**
	 * 获取图元
	 * 
	 * @param NAME
	 *            String 图元或模板名称
	 */
	public static final String GET_SYMBOL = "getSymbol";

	/**
	 * 获取图元类型
	 * 
	 * @param SYMBOL_TYPE
	 *            String图元大类(图元graphunit、模板template)
	 */
	public static final String GET_SYMBOL_TYPE_LIST = "getSymbolTypeList";

	/**
	 * 获取图元列表
	 * 
	 * @param SYMBOL_TYPE
	 *            String 图元大类(图元graphunit、模板template)
	 * @param OPERATOR
	 *            String操作人，如果操作人为空则取消操作人过滤
	 */
	public static final String GET_SYMBOLS_LIST = "getSymbolsList";

	/**
	 * 获取服务器端图元版本信息
	 * 
	 * @param OPERATOR
	 *            String 操作人，如果操作人为空则取消操作人过滤
	 * @param OWNERS
	 *            String 所有人
	 */
	public static final String GET_SYMBOLS_VERSION = "getSymbolsVersion";

	/**
	 * 客户端与服务器端通信检查
	 */
	public static final String CHECK_URL_CONN = "checkURLConn";

	/**
	 * 获取业务模型列表
	 * 
	 * @param BUSINESS_ID
	 *            业务系统编号
	 * @param MODULE_ID:
	 *            父模型编号,如为空,则查询所有模型
	 */
	public static final String GET_MODEL_LIST = "getModelList";

	/**
	 * 获取业务模型属性
	 * 
	 * @param BUSINESS_ID
	 *            业务系统编号
	 * @param MODULE_ID:
	 *            模型编号
	 */
	public static final String GET_MODEL_PARAMS = "getModelParams";

	/**
	 * 获取业务模型动作
	 */
	public static final String GET_MODEL_ACTIONS = "getModelActions";

	/**
	 * 获取业务模型状态
	 * 
	 * @param BUSINESS_ID
	 *            业务系统编号
	 * @param MODULE_ID:
	 *            模型编号
	 */
	public static final String GET_MODEL_STATUSES = "getModelStatuses";

	/**
	 * 图元与模型关系获取
	 * 
	 * @param BUSINESS_ID
	 *            业务系统编号
	 * @param MODULE_ID:
	 *            模型编号
	 */
	public static final String GET_GRAPH_UNIT_AND_MODULE_RELA = "getGraphUnitAndModuleRela";

	/**
	 * 图元与模型关系维护
	 */
	public static final String MODIFY_GRAPH_UNIT_AND_MODULE_RELA = "modifyGraphUnitAndModuleRela";

	/**
	 * 转换图元至jpg格式
	 * 
	 * @param NAME
	 *            图元名称
	 */
	public static final String TRANSFORM_SYMBOL_TO_JPG = "transformSymbolToJpg";

	/**
	 * 转换svg图至jpg格式
	 * 
	 * @param BUSINESS_ID
	 *            业务系统编号
	 * @param FILE_NAME:
	 *            文件名称
	 */
	public static final String TRANSFORM_SVGFILE_TO_JPG = "transformSvgFileToJpg";

	/**
	 * Web管理 代码管理响应名
	 */
	public static final String CONFIG_CODE_DATA = "configCodeData";

	/**
	 * Web管理 组件升级管理响应名
	 */
	public static final String CONFIG_SERVICE_MANAGER = "configServiceManager";

	/**
	 * Web管理 服务状态变更响应名
	 */
	public static final String GET_SERVICE_STATUS = "getServiceStatus";

	/**
	 * Web管理 服务管理响应名
	 */
	public static final String CONFIG_SERVICE_MODULE = "configServiceModule";

	/**
	 * Web管理 图元管理响应名
	 */
	public static final String CONFIG_SYMBOL_MANAGER = "configSymbolManager";

	/**
	 * Web管理 业务规范响应名
	 */
	public static final String CONFIG_INDUNORM = "configIndunorm";

	/**
	 * 获取系统配置参数
	 * 
	 * @param SHORT_NAME:配置项短名
	 */
	public static final String GET_SYSSETS = "getSysSets";

	/**
	 * 将图形文件保存到指定业务系统图形表中
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 * @param ID:图编号
	 * @param FILE_NAME：图名称
	 * @param GRAPH_TYPE：图类型，标准图，个性图
	 * @param BUSINESS_TYPE:业务图类型（如，系统图、一次接线图等）
	 * @param FILE_FORMAT：图格式，svg，jpg
	 * @param PARAM+0..9:对应的属性
	 * @param OPERATOR:修改人
	 * @param CONTENT:图形文件内容
	 * @param LOGS:图形修改记录
	 * @return ResultBean,obj=GraphFileBean
	 */
	public static final String SAVE_GRAPH_FILE = "saveGraphFile";

	/**
	 * 从指定业务系统图形表中获取图形
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 * @param ID:图编号
	 * @param FILE_NAME:图名称
	 * @return ResultBean,obj=GraphFileBean
	 */
	public static final String GET_GRAPH_FILE = "getGraphFile";

	/**
	 * 从指定业务系统图形表中获取图形列表
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 * @param GRAPH_TYPE：图类型，标准图，个性图
	 * @param BUSINESS_TYPE:业务图类型（如，系统图、一次接线图等）
	 * @param FILE_FORMAT：图格式，svg，jpg
	 * @param PARAM+0..9:对应的属性
	 * @param OPERATOR:修改人
	 * @return ResultBean,obj=ArrayList<GraphFileBean>
	 */
	public static final String GET_GRAPHFILE_LIST = "getGraphFileList";

	/**
	 * 获取规范与图类型关联信息
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 * @param BUSINESS_TYPE:业务图类型（如，系统图、一次接线图等）
	 */
	public static final String GET_INDUNORM_GRAPHTYPE_RELA = "getIndunormGraphTypeRela";
	/**
	 * 获取图类型与规范描述关联信息
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 * @param BUSINESS_TYPE:业务图类型（如，系统图、一次接线图等）
	 */
	public static final String GET_INDUNORM_DESC_RELA = "getIndunormDescRela";
	/**
	 * 获取规范数据域与图元关联信息
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 * @param BUSINESS_TYPE:业务图类型（如，系统图、一次接线图等）
	 * @param SYMBOL_TYPE:图元类型(图元/模板)
	 * @param SYMBOL_ID:图元编号(包含了模板编号)
	 */
	public static final String GET_INDUNORM_FIELD_SYMBOL_RELA = "getIndunormFieldSymbolRela";
	/**
	 * 获取规范与业务模型关联信息
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 * @param MODEL_ID:业务模型编号
	 * @param INDUNORM_ID:规范编号
	 */
	public static final String GET_INDUNORM_MODEL_RELA = "getIndunormModelRela";

	/**
	 * add by yux,2009-1-12 获取当前业务系统所有规范的简易列表
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 */
	public static final String GET_SIMPLE_INDUNORM_LIST = "getSimpleIndunormList";

	/**
	 * 获取指定业务系统和文件类型的保存参数信息
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 * @param BUSINESS_TYPE:业务图类型（如，系统图、一次接线图等）
	 * @return:存在则返回GraphFileParamsBean
	 */
	public static final String GET_GRAPHFILE_PARAMS = "getGraphFileParams";

	/**
	 * 判断图元或模板是否在其他模板中被引用
	 * 
	 * @param SYMBOL_NAME
	 *            symbol名称
	 * @return List 有哪些模板应用到，元素为String，都是模板名称
	 */
	public static final String CHECK_SYMBOL_TEMPLATE_RELATION = "checkSymbolTemplateRelation";

	/**
	 * 判断symbol名称的有效性，是否重复
	 * 
	 * @param SYMBOL_NAME
	 *            新symbol名称——symbolName
	 * @return Boolean true为有效，false为无效
	 */
	public static final String CHECK_SYMBOL_NAME_REPEAT = "checkSymbolNameRepeat";

	/**
	 * 重命名,更改缓存中的名称及数据库中的记录名称
	 * 
	 * @param OLDNAME
	 *            原symbol名称
	 * @param NEWNAME
	 *            新symbol名称
	 * 
	 */
	public static final String RENAME_SYMBOL = "renameSymbol";

	/**
	 * add by yux,2009-1-18 获取本业务系统支持的所有业务规范清单列表
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 */
	public static final String GET_BUSINESS_INDUNORM = "getBusinessIndunorm";

	/**
	 * add by yux,2009-1-18 获取本业务系统支持的所有模型列表
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 */
	public static final String GET_BUSINESS_MODEL = "getBusinessModel";
	public static final String GET_BUSINESS_MODEL2 = "getBusinessModel2";

	/**
	 * add by yux,2009-1-18 获取本业务系统支持的所有模型与业务规范关联关系列表
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 */
	public static final String GET_BUSINESS_MOELRELAINDUNORM = "getModelRelaIndunorm";

	/**
	 * add by yux,2009-2-19 获取业务属性值 仅限于客户端内部属性框部分使用
	 */
	public static final String GET_BUSINESS_PROPERTYVALUE = "getBusinessPropertyValue";

	/**
	 * add by yux,2009-3-5 获取本业务系统当前业务图类型的操作接口类名
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 * @param BUSINESS_TYPE:业务图类型
	 */
	public static final String GET_GRAPHBUSINESSTYPE_CANVASOPER = "getGraphCanvasOper";

	/**
	 * add by zhangsf,2009-3-26 获取业务系统台区图列表
	 * 
	 * @param BUSINESS_ID:业务系统编号
	 */
	public static final String GET_NESTDISTRICTLIST = "getNestDistrictList";
	/**
	 * add by zhangsf,2009-3-27 获取指定台区下所有台区图所需的设备信息
	 * 
	 * @param DISTRICTID:台区编号
	 */
	public static final String GETNESTDISTRICTDEVICES = "getNestDistrictDevices";

	public static final String GET_MODEL_DEMO_DATA = "getModelDemoData";
	/**
	 * 获取业务模型模拟数据
	 */
	public static final String GET_AREA_DEMO_DATA = "getAreaDemoData";

	// FIXME:待补充说明
	// public static final String GET_NESTDISTRICTLIST = "getNestDistrictList";
}
