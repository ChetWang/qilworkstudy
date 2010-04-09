package com.nci.domino.beans.plugin.smartform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表单业务对象
 * 
 * @author denvelope
 * 
 */
public class FormBizObjectBean implements SmartObject, Serializable {

    private String boCode; // 业务对象英文名
    private String boName; // 业务对象描述
    private boolean isDisplay; // 是否显示
    
    private List attributes = new ArrayList(); // 拥有的属性(FormBOAttributeBean[])

    public FormBizObjectBean() {
        
    }
    
    public String getBoCode() {
        return boCode;
    }

    public void setBoCode(String boCode) {
        this.boCode = boCode;
    }

    public String getBoName() {
        return boName;
    }

    public void setBoName(String boName) {
        this.boName = boName;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public String getObjID() {
        return getBoCode();
    }

    public String getObjName() {
        return getBoName();
    }

    public SmartObject getObjParent() {
        return null;
    }

    public String getObjType() {
        return null;
    }
}
