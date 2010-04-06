
package com.nci.svg.sdk.server.service;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-11-25
 * @功能：业务服务实体类
 *
 */
public class ServiceInfoBean implements Serializable {
	private static final long serialVersionUID = -4733200897836819585L;
	/**
     * 组件短名
     */
    private String shortName;
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 服务类名
     */
    private String className;
    
    /**
     * 服务启动标记
     */
    private boolean startFlag;
    
    /**
     * 服务当前被调用的数量
     */
    private int count = 0;
	/**
	 * 获取组件短名
	 * @return：组件短名
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * 设置组件短名
	 * @param shortName：组件短名
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * 获取服务名
	 * @return：服务名
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * 设置服务名
	 * @param serviceName：服务名
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * 获取类名
	 * @return：类名
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * 设置类名
	 * @param className：类名
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * 获取服务启动标记
	 * @return:启动标记
	 */
	public boolean isStartFlag() {
		return startFlag;
	}
	/**
	 * 设置服务启动标记
	 * @param startFlag：启动状态
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
	 * 增加调用个数1
	 */
	public void increaseCount()
	{
		count++;
	}
	
	/**
	 * 减少调用个数1
	 */
	public void decreaseCount()
	{
		count--;
	}
	public int getCount() {
		return count;
	}
}
