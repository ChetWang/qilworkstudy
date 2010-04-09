package com.nci.domino.beans.plugin.smartform;

import java.io.Serializable;

/**
 * 表单业务对象属性
 * 
 * @author denvelope
 *
 */
public class FormBOAttributeBean implements SmartObject, Serializable {

    private String attributeCode; // 属性英文名
    private String attributeName; // 属性描述
    private boolean isDisplay; // 是否显示
    private boolean isReadonly; // 是否只读
    private boolean isEditable; // 是否允许修改
    private int orderNum; // 显示顺序

    public FormBOAttributeBean() {

    }

    public FormBOAttributeBean(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    public String toString(){
    	return attributeName;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    public boolean isReadonly() {
        return isReadonly;
    }

    public void setReadonly(boolean isReadonly) {
        this.isReadonly = isReadonly;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getObjID() {
        return getAttributeCode();
    }

    public String getObjName() {
        return getAttributeName();
    }

    public SmartObject getObjParent() {
        return null;
    }

    public String getObjType() {
        return null;
    }
}
