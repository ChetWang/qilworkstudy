package com.nci.domino.beans.plugin.smartform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * �&����������
 * 
 * @author denvelope
 *
 */
public class ActivityFormConfigBean implements SmartObject, Serializable {

    private String pcId; // ����ID��������&�����õ�pcIdһ��
	private String activityId; // �����ID
	private String activityCode; // �����Ӣ����
	private String activityName; // �����������
	
	private List businessObjects = new ArrayList(); // ӵ�еı�ҵ�����

	public ActivityFormConfigBean() {
	}

	public ActivityFormConfigBean(String pcId) {
        this.pcId = pcId;
    }

	public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

    public List getBusinessObjects() {
        return businessObjects;
    }

    public void setBusinessObjects(List businessObjects) {
        this.businessObjects = businessObjects;
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
    
}
