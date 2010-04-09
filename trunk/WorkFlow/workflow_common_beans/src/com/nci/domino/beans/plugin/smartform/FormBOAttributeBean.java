package com.nci.domino.beans.plugin.smartform;

import java.io.Serializable;

/**
 * ��ҵ���������
 * 
 * @author denvelope
 *
 */
public class FormBOAttributeBean implements SmartObject, Serializable {

    private String attributeCode; // ����Ӣ����
    private String attributeName; // ��������
    private boolean isDisplay; // �Ƿ���ʾ
    private boolean isReadonly; // �Ƿ�ֻ��
    private boolean isEditable; // �Ƿ������޸�
    private int orderNum; // ��ʾ˳��

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
