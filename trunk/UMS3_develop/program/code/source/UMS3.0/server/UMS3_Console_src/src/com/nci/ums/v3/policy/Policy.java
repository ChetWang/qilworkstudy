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
     * ��һ���ȼ�����ʱ���������ǰ�ĸ����ȼ����������Ѿ�ʧ��
     */
    public static final boolean POLICY_NEXT_PRIORITY_YES = true;
    /**
     * ���ʼ�����ȼ���Ҳ���ǲ������ȼ���ߵ�����
     */
    public static final boolean POLICY_NEXT_PRIORITY_NO = false;
}
