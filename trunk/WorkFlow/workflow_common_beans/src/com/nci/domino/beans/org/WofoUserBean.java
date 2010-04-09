package com.nci.domino.beans.org;

import com.nci.domino.beans.WofoBaseBean;

public class WofoUserBean extends WofoBaseBean {

    private String userId;
    private String userCode;
    private String userName;
    private String userUnitId;

    public WofoUserBean() {
    }

    public WofoUserBean(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUnitId() {
        return userUnitId;
    }

    public void setUserUnitId(String userUnitId) {
        this.userUnitId = userUnitId;
    }
    
    public String toString(){
    	return userName;
    }
    
	public String getID() {
		return userId;
	}

	public void setID(String id) {
		userId = id;
	}
}
