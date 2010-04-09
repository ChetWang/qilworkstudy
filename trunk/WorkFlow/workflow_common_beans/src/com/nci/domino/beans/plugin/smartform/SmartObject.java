package com.nci.domino.beans.plugin.smartform;


public interface SmartObject {

    public static final String TYPE_CATEGORY = "CATEGORY"; // 分类(主题)
    public static final String TYPE_FORM = "FORM"; // 表单
    
    public final static String SMARTFORM_PROCESS = "SMARTFORM_PROCESS";
    public final static String FORM_ROLES = "FORM_ROLES";
    public final static String FORM_ID = "FORM_ID";
    
    public String getObjID();

    public String getObjName();

    public String getObjType();

    public SmartObject getObjParent();
}
