package com.nci.domino.beans.org;

import com.nci.domino.beans.WofoBaseBean;

public class WofoUnitBean extends WofoBaseBean {
    
    // 机构类型
    public static final String TYPE_GROUP = "GROUP"; // 集团
    public static final String TYPE_REGION = "REGION"; // 区域、大区
    public static final String TYPE_COMPANY = "COMPANY"; // 公司
    public static final String TYPE_AREA = "AREA"; // 工区
    public static final String TYPE_DEPARTMENT = "DEPARTMENT"; // 部门
    public static final String TYPE_OFFICE = "OFFICE"; // 科室
    public static final String TYPE_TEAM = "TEAM"; // 班组、团队

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
