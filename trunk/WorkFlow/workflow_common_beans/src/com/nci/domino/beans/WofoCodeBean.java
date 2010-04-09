package com.nci.domino.beans;

import java.io.Serializable;

/**
 * <p>
 * 标题：WofoCodeBean.java
 * </p>
 * <p>
 * 描述： 代码类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2010-3-30
 * @version 1.0
 */
public class WofoCodeBean implements Serializable {

    /**
     * 值
     */
    private String value;
    /**
     * 名称
     */
    private String name;
    
    public WofoCodeBean() {
    }

    public WofoCodeBean(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
