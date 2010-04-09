package com.nci.domino.beans.plugin.smartform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ����&������
 * 
 * @author denvelope
 *
 */
public class ProcessFormConfigBean implements SmartObject, Serializable {

    private String pcId; // ����ID
	private String processId; // ҵ�����̶���ID
	private String processCode; // ҵ�����̶������
	private String formId; // ��ID
	private String formName; // ������

	private List businessObjects = new ArrayList(); // ӵ�еı�ҵ�����
	private List roles = new ArrayList(); // �������ɫ WofoRoleBean
	
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
