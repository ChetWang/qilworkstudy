package com.nci.domino.beans.org;

import com.nci.domino.beans.WofoBaseBean;

public class WofoVirtualRoleBean extends WofoBaseBean implements Comparable{

    private String roleId;
    private String roleCode;
    private String roleName;

    public WofoVirtualRoleBean() {
    }

    public WofoVirtualRoleBean(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String toString(){
    	return roleName;
    }

	public int compareTo(Object o) {
		if(o instanceof WofoVirtualRoleBean){
			return toString().compareTo(o.toString());
		}
		return 0;
	}
	
	public String getID() {
		return roleId;
	}

	public void setID(String id) {
		roleId = id;
	}
}
