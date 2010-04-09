package com.nci.domino.domain;

public class WofoBaseDomain {

    // 可以/不可以
    public static final String CAN = "Y"; // 可以
    public static final String CAN_NOT = "N"; // 不可以

    // 是/否
    public static final String YES = "Y"; // 是
    public static final String NO = "N"; // 否

    // 模块类型
    public static final String APP_TYPE_JFW = "JFW"; // 功能模型
    public static final String APP_TYPE_URL = "URL"; // URL
    
    // 流程定义对象类型（代码表wofoc_object_type）
    public static final String OBJ_TYPE_PACKAGE = "PACKAGE";
    public static final String OBJ_TYPE_PROCESS_MASTER = "PROCESS_MASTER";
    public static final String OBJ_TYPE_PROCESS = "PROCESS";
    public static final String OBJ_TYPE_ACTIVITY = "ACTIVITY";
    public static final String OBJ_TYPE_ARGUMENT = "ARGUMENT";
    public static final String OBJ_TYPE_MESSAGE = "MESSAGE"; // 消息
    public static final String OBJ_TYPE_EVENT = "EVENT"; // 事件
    public static final String OBJ_TYPE_CONDITION = "CONDITION";
    public static final String OBJ_TYPE_CONDITION_MEMBER = "CONDITION_MEMBER";
    public static final String OBJ_TYPE_PARTICIPANT_SCOPE = "PARTICIPANT_SCOPE"; // 参与者
    public static final String OBJ_TYPE_WORKITEM_RULE = "WORKITEM_RULE";
    public static final String OBJ_TYPE_TRANSITION = "TRANSITION";
    public static final String OBJ_TYPE_FUNCTION_TRACK = "FUNCTION_TRACK"; // 泳道职能
}
