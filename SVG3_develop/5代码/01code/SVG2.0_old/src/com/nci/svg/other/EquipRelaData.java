/**
 * 类名：com.nci.svg.other.EquipRelaData
 * 创建人:yx
 * 创建日期：2008-5-12
 * 类作用:设备关联信息结构体
 * 修改日志：
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
