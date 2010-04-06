/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.svg.equip;

/**
 * 设备属性的Java Bean，用于在设备属性输入时的JComboBox的显示
 * @author qil
 */
public class NCIPropertyComboBean implements Comparable {
    
    private String comboBeanID = null;
    
    private String comboBeanValue = null;
    
    public NCIPropertyComboBean(){}
    
    public NCIPropertyComboBean(String comboBeanID, String comboBeanValue ){
        this.comboBeanID = comboBeanID;
        this.comboBeanValue = comboBeanValue;
    }

    public int compareTo(Object o) {
        return comboBeanID.compareTo(o.toString());
    }

    public String getComboBeanID() {
        return comboBeanID;
    }

    public void setComboBeanID(String comboBeanID) {
        this.comboBeanID = comboBeanID;
    }

    public String getComboBeanValue() {
        return comboBeanValue;
    }

    public void setComboBeanValue(String comboBeanValue) {
        this.comboBeanValue = comboBeanValue;
    }
    
    @Override
    public String toString(){
        return "["+comboBeanID+"]"+comboBeanValue;
    }

}
