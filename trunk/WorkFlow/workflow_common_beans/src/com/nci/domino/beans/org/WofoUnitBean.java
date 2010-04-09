package com.nci.domino.beans.org;

import com.nci.domino.beans.WofoBaseBean;

public class WofoUnitBean extends WofoBaseBean {
    
    // ��������
    public static final String TYPE_GROUP = "GROUP"; // ����
    public static final String TYPE_REGION = "REGION"; // ���򡢴���
    public static final String TYPE_COMPANY = "COMPANY"; // ��˾
    public static final String TYPE_AREA = "AREA"; // ����
    public static final String TYPE_DEPARTMENT = "DEPARTMENT"; // ����
    public static final String TYPE_OFFICE = "OFFICE"; // ����
    public static final String TYPE_TEAM = "TEAM"; // ���顢�Ŷ�

    private String unitId;
    private String unitCode;
    private String unitName;
    private String unitType;
    private String parentUnitId;

    public WofoUnitBean() {
    }

    public WofoUnitBean(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getParentUnitId() {
        return parentUnitId;
    }

    public void setParentUnitId(String parentUnitId) {
        this.parentUnitId = parentUnitId;
    }
    
    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String toString(){
    	return unitName;
    }
	public String getID() {
		return unitId;
	}

	public void setID(String id) {
		this.unitId = id;
	}
}
