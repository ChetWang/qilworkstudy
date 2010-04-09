package com.nci.domino.help;

/**
 * @author 陈新中
 * 这里定义了一系列的代码表
 */
public class WorkFlowCode {
	public static String[][] eventTrigerType = new String[][] { { "PROCESS_START", "流程创建" }, { "PROCESS_End", "流程完成" },
			{ "ACTIVITY_CREATE", "活动创建" }, { "ACTIVITY_COMPLETE", "活动完成" } };//事件触发时机
	public static String[][] eventMode = new String[][] { { "SYNCHRONIZATION", "同步" }, { "ASYNCHRONISM", "异步" } };//事件模式
	public static String[][] participantType = new String[][] { { "2", "人员" }, { "3", "部门" }, { "1", "角色" }, { "9", "其它" } };//参与者类型
	public static String[][] activityJoinModeList = new String[][] { { "XOR", "或" }, { "AND", "与" } };//聚合模式
	public static String[][] activitySplitModeList = new String[][] { { "XOR", "或" }, { "AND", "与" } };//分支模式
	public static String[][] processScopeList = new String[][] { { "0", "公有" }, { "1", "私有" } };//通用范围
	public static String[][] processStatusList = new String[][] { { "1", "在用" }, { "2", "禁用" } };//流程状态
	public static String[][] activityParticipantTypeList = new String[][] { { "1", "单人" }, { "2", "多人" } };//参与类型
	public static String[][] activityStatusList = new String[][] { { "1", "在用" }, { "2", "禁用" } };//活动状态
	public static String[][] moduleTypeList = new String[][] { { "1", "功能模型" }, { "2", "URL" } };//module-type
	public static String[][] eventType = new String[][] { { "JAVA", "JAVA方法" }, { "SQL", "SQL语句" }, { "PROCEDURE", "PROCEDURE过程" },
			{ "WEBSERVICE", "WEBSERVICE服务" } };//事件类型
	public static String default_activityStatus = "1";//默认活动状态
	public static String default_displayOrder = "0";//默认显示顺序
	public static String default_processScope = "1";//默认processScope
	public static String[][] ACTTYPE_CODES = new String[][] { { "1", "31.gif", "开始活动" }, { "2", "51.gif", "人工活动" },
			{ "5", "21.gif", "分支路由活动" }, { "6", "81.gif", "聚合路由活动" }, { "4", "41.gif", "自动活动" }, { "9", "61.gif", "结束活动" } };//活动参数代码表
	public static String[][] variableType = new String[][] { { "0", "字符串变量" }, { "1", "整型变量" }, { "2", "逻辑型变量" }, { "3", "浮点型变量" },
			{ "4", "日期型变量" }, { "5", "日期时间变量" }, { "6", "对象变量" }, { "7", "时间型变量" }, };
	public static String default_variableType = "0";
	public static String[][] variableDirection = new String[][] { { "1", "输入" }, { "2", "输出" } };
	public static String default_variableDirection = "1";
}
