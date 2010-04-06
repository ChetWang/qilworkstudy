
package com.nci.svg.sdk.server.service;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-11-25
 * @���ܣ�ҵ�����ʵ����
 *
 */
public class ServiceInfoBean implements Serializable {
	private static final long serialVersionUID = -4733200897836819585L;
	/**
     * �������
     */
    private String shortName;
    /**
     * ������
     */
    private String serviceName;
    /**
     * ��������
     */
    private String className;
    
    /**
     * �����������
     */
    private boolean startFlag;
    
    /**
     * ����ǰ�����õ�����
     */
    private int count = 0;
	/**
	 * ��ȡ�������
	 * @return���������
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * �����������
	 * @param shortName���������
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * ��ȡ������
	 * @return��������
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * ���÷�����
	 * @param serviceName��������
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * ��ȡ����
	 * @return������
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * ��������
	 * @param className������
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * ��ȡ�����������
	 * @return:�������
	 */
	public boolean isStartFlag() {
		return startFlag;
	}
	/**
	 * ���÷����������
	 * @param startFlag������״̬
	 */
	public void setStartFlag(boolean startFlag) {
		this.startFlag = startFlag;
	}
	
	public String getStartFlag()
	{
		if(startFlag)
			return "RUNNING";
		
		return "STOPPING";
	}
	
	/**
	 * ���ӵ��ø���1
	 */
	public void increaseCount()
	{
		count++;
	}
	
	/**
	 * ���ٵ��ø���1
	 */
	public void decreaseCount()
	{
		count--;
	}
	public int getCount() {
		return count;
	}
}
