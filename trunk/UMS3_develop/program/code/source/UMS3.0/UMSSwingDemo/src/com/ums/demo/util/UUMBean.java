/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ums.demo.util;

/**
 *
 * @author Qil.Wong
 */
public class UUMBean {

    private String name;
    private String mobile;
    private String email;
    private int showType;
    public static final int SHOW_TYPE_MOBLE = 1;
    public static final int SHOW_TYPE_EMAIL = 2;
    public static final int SHOW_TYPE_LCS = 3;
    

    public UUMBean() {
    }

    public UUMBean(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.showType = 0;
    }

    public UUMBean(String name, String email, String mobile, int showType) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.showType = showType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    @Override
    public String toString() {
        switch (showType) {
            case SHOW_TYPE_EMAIL:
                return email;
            case SHOW_TYPE_MOBLE:
                return mobile;
        }
        return "";
    }
}
