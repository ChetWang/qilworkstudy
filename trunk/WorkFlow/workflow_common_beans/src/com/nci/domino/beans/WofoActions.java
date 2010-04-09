package com.nci.domino.beans;

public class WofoActions {

    /**
     * 保存所有流程定义（包括流程包）
     */
    public static final String SAVE_ALL = "save_all";

    /**
     * 保存当前（单个）流程定义
     */
    public static final String SAVE_CURRENT = "save_current";

    /**
     * 载入所有流程定义（包括流程包）
     */
    public static final String LOAD_ALL = "load_all";

    /**
     * 载入单个业务流程定义
     */
    public static final String LOAD_PROCESS = "load_process";
    
    /**
     * 取所有同版本的流程定义
     */
    public static final String LOAD_VERSION = "load_version";
    
    /**
     * 校验流程定义属性，在流程定义属性面板"确定"按钮点下后，窗口关闭前
     */
    public static final String VERIFY_PROCESS_PREFERENCES = "verify_process_preferences";
    
    /**
     * 校验流程包属性，在流程包属性面板"确定"按钮点下后，窗口关闭前
     */
    public static final String VERIFY_PACKAGE_PREFERENCES = "verify_package_preferences";

    /**
     * 校验活动环节属性，在活动环节属性面板"确定"按钮点下后，窗口关闭前
     */
    public static final String VERIFY_ACTIVITY_PREFERENCES = "verify_activity_preferences";
    
    /**
     * 取机构树
     */
    public static final String GET_UNIT_TREE = "get_unit_tree";

    /**
     * 取部门树
     */
    public static final String GET_DEPT_TREE = "get_dept_tree";

    /**
     * 取组织树（包括人员）
     */
    public static final String GET_ORG_TREE = "get_org_tree";

    /**
     * 取角色
     */
    public static final String GET_ROLES = "get_roles";
    
    /**
     * 取虚拟角色
     */
    public static final String GET_VIRTUAL_ROLES = "get_virtual_roles";
    
    /**
     * 取用户信息
     */
    public static final String GET_USER_INFO = "get_user_info";
    
    /**
     * 取系统参数
     */
    public static final String GET_SYSTEM_PROPERTY = "get_system_property";
    
    /**
     * 取监听类型
     */
    public static final String GET_LISTENER_TYPE = "get_listener_type";
    
    /**
     * 取表单对象树
     */
    public static final String GET_SMART_FORM_TREE = "get_smart_form_tree";

    /**
     * 根据表单ID取业务对象及属性
     */
    public static final String GET_BO_ATTRIBUTES = "get_bo_attributes";

    /**
     * 获取流程&表单配置
     */
    public static final String GET_PROCESS_FORM = "get_process_form";

    /**
     * 获取活动&表单业务对象属性配置
     */
    public static final String GET_ACTIVITY_FORM = "get_activity_form";
}
