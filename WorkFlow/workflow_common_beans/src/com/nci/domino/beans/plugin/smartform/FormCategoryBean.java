package com.nci.domino.beans.plugin.smartform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表单分类对象
 * 
 * @author denvelope
 * 
 */
public class FormCategoryBean implements SmartObject, Serializable {

    protected String catId;
    protected String catName;
    protected String catDescription;

    protected List forms = new ArrayList(); // 拥有的表单对象

    public FormCategoryBean() {
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatDescription() {
        return catDescription;
    }

    public void setCatDescription(String catDescription) {
        this.catDescription = catDescription;
    }

    public String getObjID() {
        return getCatId();
    }

    public String getObjName() {
        return getCatName();
    }

    public SmartObject getObjParent() {
        return null;
    }

    public String getObjType() {
        return TYPE_CATEGORY;
    }
    
    public String toString(){
    	return catName;
    }

}
