package com.nci.domino.beans.plugin.smartform;

import java.io.Serializable;

/**
 * 表单对象
 * 
 * @author denvelope
 *
 */
public class SmartFormBean implements SmartObject, Serializable {

    protected String formId;
    protected String formName;
    
    public SmartFormBean() {
        // ..
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getObjID() {
        return getFormId();
    }

    public String getObjName() {
        return getFormName();
    }

    public SmartObject getObjParent() {
        return null;
    }

    public String getObjType() {
        return TYPE_FORM;
    }
    
    public String toString(){
    	return formName;
    }
}
