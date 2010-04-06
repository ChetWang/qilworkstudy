package com.nci.svg.bean;

import java.io.Serializable;

public class NestOperBean implements Serializable {
    private static final long serialVersionUID = -3307779170805877762L;
    /**
     * 业务编号
     */
    private String OperID = "";
    /**
     * 业务名称
     */
    private String OperName = "";
    /**
     * @return the operID
     */
    public String getOperID() {
        return OperID;
    }
    /**
     * @param operID the operID to set
     */
    public void setOperID(String operID) {
        OperID = operID;
    }
    /**
     * @return the operName
     */
    public String getOperName() {
        return OperName;
    }
    /**
     * @param operName the operName to set
     */
    public void setOperName(String operName) {
        OperName = operName;
    }
}
