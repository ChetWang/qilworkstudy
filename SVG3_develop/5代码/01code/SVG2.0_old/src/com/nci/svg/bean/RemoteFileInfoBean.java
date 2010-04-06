/**
 * 类名：com.nci.svg.bean
 * 创建人:yx.nci
 * 创建日期：2008-8-20
 * 类作用:TODO
 * 修改日志：
 */
package com.nci.svg.bean;

import java.io.Serializable;

/**
 * @author yx.nci
 *
 */
public class RemoteFileInfoBean implements Serializable{
    
    private static final long serialVersionUID = -3307779170805877762L;
    private String strID="";
    /**
     * 文件名
     */
    private String strFileName="";
    /**
     * 文件描述
     */
    private String strFileDesc="";
    /**
     * 文件类型
     */
    private String strFileType="";
    /**
     * 地区描述
     */
    private String strArea="";
    /**
     * 电压等级
     */
    private String strVoltage="";
    /**
     * @return the strFileName
     */
    public String getStrFileName() {
        return strFileName;
    }
    /**
     * @param strFileName the strFileName to set
     */
    public void setStrFileName(String strFileName) {
        this.strFileName = strFileName;
    }
    /**
     * @return the strFileDesc
     */
    public String getStrFileDesc() {
        return strFileDesc;
    }
    /**
     * @param strFileDesc the strFileDesc to set
     */
    public void setStrFileDesc(String strFileDesc) {
        this.strFileDesc = strFileDesc;
    }
    /**
     * @return the strFileType
     */
    public String getStrFileType() {
        return strFileType;
    }
    /**
     * @param strFileType the strFileType to set
     */
    public void setStrFileType(String strFileType) {
        this.strFileType = strFileType;
    }
    public String getStrID() {
        return strID;
    }
    public void setStrID(String strID) {
        this.strID = strID;
    }
    public String getStrArea() {
        return strArea;
    }
    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }
    public String toString() {
        // TODO Auto-generated method stub
        return strFileDesc;
    }
    public String getStrVoltage() {
        return strVoltage;
    }
    public void setStrVoltage(String strVoltage) {
        this.strVoltage = strVoltage;
    }

}
