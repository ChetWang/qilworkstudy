/**
 * ������com.nci.svg.bean
 * ������:yx.nci
 * �������ڣ�2008-8-20
 * ������:TODO
 * �޸���־��
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
     * �ļ���
     */
    private String strFileName="";
    /**
     * �ļ�����
     */
    private String strFileDesc="";
    /**
     * �ļ�����
     */
    private String strFileType="";
    /**
     * ��������
     */
    private String strArea="";
    /**
     * ��ѹ�ȼ�
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
