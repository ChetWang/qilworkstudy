package com.nci.svg.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class StationInfoBean implements Serializable {
    private static final long serialVersionUID = -3307779170805877762L;
    /**
     * 场站编号
     */
    private String strStationID = "";
    /**
     * 场站名称
     */
    private String strStationName = "";
    /**
     * 场站一次接线图内容
     */
    private String strContent = "";
    /**
     * 设备映射列表
     */
    private ArrayList equipList = new ArrayList();
    /**
     * @return the strStationID
     */
    public String getStrStationID() {
        return strStationID;
    }
    /**
     * @param strStationID the strStationID to set
     */
    public void setStrStationID(String strStationID) {
        this.strStationID = strStationID;
    }
    /**
     * @return the strStationName
     */
    public String getStrStationName() {
        return strStationName;
    }
    /**
     * @param strStationName the strStationName to set
     */
    public void setStrStationName(String strStationName) {
        this.strStationName = strStationName;
    }
    /**
     * @return the strContent
     */
    public String getStrContent() {
        return strContent;
    }
    /**
     * @param strContent the strContent to set
     */
    public void setStrContent(String strContent) {
        this.strContent = strContent;
    }
    /**
     * @return the equipList
     */
    public ArrayList getEquipList() {
        return equipList;
    }
    /**
     * @param equipList the equipList to set
     */
    public void setEquipList(ArrayList equipList) {
        this.equipList = equipList;
    }
}
