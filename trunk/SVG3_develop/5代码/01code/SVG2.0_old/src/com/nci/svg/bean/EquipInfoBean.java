package com.nci.svg.bean;

import java.io.Serializable;

public class EquipInfoBean implements Serializable {
    private static final long serialVersionUID = -3307779170805877762L;
    /**
     * psms�豸���
     */
    private String strObjectID="";
    /**
     * �豸����
     */
    private String strName="";
    /**
     * �豸psms��������
     */
    private String strTypeName="";
    /**
     * �豸��ѹ�ȼ�
     */
    private String strVoltage="";
    /**
     * ������������
     */
    private String strAreaName="";
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        // TODO Auto-generated method stub
        return strName + "-" + strObjectID;
    }
    /**
     * @return the strObjectID
     */
    public String getStrObjectID() {
        return strObjectID;
    }
    /**
     * @param strObjectID the strObjectID to set
     */
    public void setStrObjectID(String strObjectID) {
        this.strObjectID = strObjectID;
    }
    /**
     * @return the strName
     */
    public String getStrName() {
        return strName;
    }
    /**
     * @param strName the strName to set
     */
    public void setStrName(String strName) {
        this.strName = strName;
    }
    /**
     * @return the strTypeName
     */
    public String getStrTypeName() {
        return strTypeName;
    }
    /**
     * @param strTypeName the strTypeName to set
     */
    public void setStrTypeName(String strTypeName) {
        this.strTypeName = strTypeName;
    }
    /**
     * @return the strVoltage
     */
    public String getStrVoltage() {
        return strVoltage;
    }
    /**
     * @param strVoltage the strVoltage to set
     */
    public void setStrVoltage(String strVoltage) {
        this.strVoltage = strVoltage;
    }
    /**
     * @return the strAreaName
     */
    public String getStrAreaName() {
        return strAreaName;
    }
    /**
     * @param strAreaName the strAreaName to set
     */
    public void setStrAreaName(String strAreaName) {
        this.strAreaName = strAreaName;
    }
}
