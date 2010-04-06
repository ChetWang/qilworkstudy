/*
 * Policy.java
 * 
 * Created on 2007-9-26, 16:35:35
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.v3.policy;

/**
 *
 * @author Qil.Wong
 */
public interface Policy {

    /**
     * Channel policy flag for application and services
     */
    public static final int POLICY_APPSERVICE = 1;
    /**
     * Channel policy flag for personal configuration
     */
    public static final int POLICY_PERSONAL = 2;
  
    
    /**
     * 下一优先级，此时的情况是先前的高优先级渠道发送已经失败
     */
    public static final boolean POLICY_NEXT_PRIORITY_YES = true;
    /**
     * 最初始的优先级，也就是采用优先级最高的渠道
     */
    public static final boolean POLICY_NEXT_PRIORITY_NO = false;
}
