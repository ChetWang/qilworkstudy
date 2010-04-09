package com.nci.domino.beans;

import java.io.Serializable;

/**
 * <p>
 * ���⣺WofoCodeBean.java
 * </p>
 * <p>
 * ������ ������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2010-3-30
 * @version 1.0
 */
public class WofoCodeBean implements Serializable {

    /**
     * ֵ
     */
    private String value;
    /**
     * ����
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
