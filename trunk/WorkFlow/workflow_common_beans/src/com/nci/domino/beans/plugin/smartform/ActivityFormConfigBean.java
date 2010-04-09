package com.nci.domino.beans.plugin.smartform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动&表单属性配置
 * 
 * @author denvelope
 *
 */
public class ActivityFormConfigBean implements SmartObject, Serializable {

    private String pcId; // 配置ID，和流程&表单配置的pcId一致
	private String activityId; // 活动定义ID
	private String activityCode; // 活动定义英文名
	private String activityName; // 活动定义中文名
	
	private List businessObjects = new ArrayList(); // 拥有的表单业务对象

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
