package com.nci.domino.beans.plugin.smartform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程&表单配置
 * 
 * @author denvelope
 *
 */
public class ProcessFormConfigBean implements SmartObject, Serializable {

    private String pcId; // 配置ID
	private String processId; // 业务流程定义ID
	private String processCode; // 业务流程定义编码
	private String formId; // 表单ID
	private String formName; // 表单名称

	private List businessObjects = new ArrayList(); // 拥有的表单业务对象
	private List roles = new ArrayList(); // 表单发起角色 WofoRoleBean
	
	public ProcessFormConfigBean() {
	}

	public ProcessFormConfigBean(String pcId) {
		this.pcId = pcId;
	}

	public String getPcId() {
		return pcId;
	}

	public void setPcId(String pcId) {
		this.pcId = pcId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
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
        return getPcId();
    }

    public String getObjName() {
        return getPcId();
    }

    public SmartObject getObjParent() {
        return null;
    }

    public String getObjType() {
        return null;
    }

	public List getBusinessObjects() {
        return businessObjects;
    }

    public void setBusinessObjects(List businessObjects) {
        this.businessObjects = businessObjects;
    }

    public List getRoles() {
		return roles;
	}

	public void setRoles(List roles) {
		this.roles = roles;
	}
}
