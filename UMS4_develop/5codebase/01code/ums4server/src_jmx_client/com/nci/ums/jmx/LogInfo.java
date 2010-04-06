/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.jmx;

import java.io.Serializable;

/**
 * JMX logger ∂‘œÛ
 * @author Qil.Wong
 */
public class LogInfo implements Serializable{

    private int level;

    private String info;

    public LogInfo(){}

    public LogInfo(int level,String info){
        this.level = level;
        this.info = info;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }
}
