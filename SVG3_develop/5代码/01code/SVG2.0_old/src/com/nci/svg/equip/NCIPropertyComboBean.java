/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.svg.equip;

/**
 * �豸���Ե�Java Bean���������豸��������ʱ��JComboBox����ʾ
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
