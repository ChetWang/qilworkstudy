/*
 * AppInfoV3.java
 * 
 * Created on 2007-9-27, 11:48:00
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.v3.service;

import com.nci.ums.periphery.application.*;

/**
 *
 * @author Qil.Wong
 */
public class AppInfoV3 extends AppInfo{

    private int appStatus;
    
    public static final int DISABLED_STATUS = 1;
    public static final int ENABLED_STATUS= 0;
    
    public AppInfoV3(){
        super();
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }
    
}
