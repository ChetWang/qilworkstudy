
package com.nci.svg.sdk.logger;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-11-25
 * @功能：日志策略信息实体类
 *
 */
public class LogPolicyBean implements Serializable {
	private static final long serialVersionUID = 3379568818370365243L;

	/**
	 * 部署位置
	 */
	private String moduleLocation;
	
	/**
	 * 组件大类
	 */
	private String moduleKind;
	/**
	 * 组件类型
	 */
	private String moduleType;
	/**
	 * 日志级别
	 */
	private String logLevel;
	/**
	 * 日志类型
	 */
	private String logType;
	/**
	 * 获取组件类型
	 * @return：返回组件类型
	 */
	public String getModuleType() {
		return moduleType;
	}
	/**
	 * 设置组件类型
	 * @param moduleType：组件类型
	 */
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	/**
	 * 获取日志等级
	 * @return：日志等级
	 */
	public String getLogLevel() {
		return logLevel;
	}
	/**
	 * 设置日志等级
	 * @param logLevel：日志等级
	 */
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	/**
	 * 获取日志类型
	 * @return：日志类型
	 */
	public String getLogType() {
		return logType;
	}
	/**
	 * 设置日志类型
	 * @param logType：日志类型
	 */
	public void setLogType(String logType) {
		this.logType = logType;
	}
	/**
	 * 获取组件位置
	 * @return：组件部署位置
	 */
	public String getModuleLocation() {
		return moduleLocation;
	}
	/**
	 * 设置组件部署位置
	 * @param moduleLocation：组件部署位置
	 */
	public void setModuleLocation(String moduleLocation) {
		this.moduleLocation = moduleLocation;
	}
	/**
	 * 获取组件大类
	 * @return：组件大类
	 */
	public String getModuleKind() {
		return moduleKind;
	}
	/**
	 * 设置组件大类
	 * @param moduleKind：组件大类
	 */
	public void setModuleKind(String moduleKind) {
		this.moduleKind = moduleKind;
	}

}
