/**
 * ������com.nci.svg.other.EquipRelaData
 * ������:yx
 * �������ڣ�2008-5-12
 * ������:�豸������Ϣ�ṹ��
 * �޸���־��
 */
package com.nci.svg.other;

import org.w3c.dom.Element;

/**
 * @author yx
 *
 */
public class EquipRelaData {
    public String firstEquipID,secondEquipID,betweenEquipID;
    public int nSvgLevel = 0;
    
    /**
     * @return the firstEquipID
     */
    public String getFirstEquipID() {
        return firstEquipID;
    }
    /**
     * @param firstEquipID the firstEquipID to set
     */
    public void setFirstEquipID(String firstEquipID) {
        this.firstEquipID = firstEquipID;
    }
    /**
     * @return the secondEquipID
     */
    public String getSecondEquipID() {
        return secondEquipID;
    }
    /**
     * @param secondEquipID the secondEquipID to set
     */
    public void setSecondEquipID(String secondEquipID) {
        this.secondEquipID = secondEquipID;
    }
    /**
     * @return the betweenEquipID
     */
    public String getBetweenEquipID() {
        return betweenEquipID;
    }
    /**
     * @param betweenEquipID the betweenEquipID to set
     */
    public void setBetweenEquipID(String betweenEquipID) {
        this.betweenEquipID = betweenEquipID;
    }
    /**
     * @return the nSvgLevel
     */
    public int getNSvgLevel() {
        return nSvgLevel;
    }
    /**
     * @param svgLevel the nSvgLevel to set
     */
    public void setNSvgLevel(int svgLevel) {
        nSvgLevel = svgLevel;
    }
    
    public void print()
    {
        System.out.println("FirstEquip:" + firstEquipID);
        System.out.println("secondEquipID:" + secondEquipID);
        
        return;
    }
    
    
    

}
