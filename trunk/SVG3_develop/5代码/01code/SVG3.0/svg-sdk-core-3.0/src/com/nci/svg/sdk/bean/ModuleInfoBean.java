
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：模块信息实体类
 *
 */
public class ModuleInfoBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -37817942385437624L;
	/**
     * 模块短名简称
     */
    private String moduleShortName;
    /**
     * 模块全名描述
     */
    private String name;
    /**
     * 模块部署位置
     */
    private String location;
    /**
     * 组件大类
     */
    private String kind;
    /**
     * 组件类型
     */
    private String type;
    /**
     * 日志等级
     */
    private String logLevel;
    /**
     * 日志类型
     */
    private String logType;
    /**
     * 组件加载类
     */
    private String className;
    
    /**
     * 组件版本号
     */
    private String edition;
    
    /**
     * 组件应用的业务系统编号
     */
    private String bussID;

	/**
	 * 获取组件模块短名
	 * @return：组件模块短名
	 */
	public String getModuleShortName() {
		return moduleShortName;
	}

	/**
	 * 设置组件模块短名
	 * @param moduleShortName：组件模块短名
	 */
	public void setModuleShortName(String moduleShortName) {
		this.moduleShortName = moduleShortName;
	}

	/**
	 * 获取组件模块名
	 * @return：组件模块名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置组件模块名
	 * @param name：组件模块名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取部署位置
	 * @return：部署位置
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * 设置部署位置
	 * @param location：部署位置
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 获取组件模块大类
	 * @return：组件模块大类
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * 设置组件模块大类
	 * @param kind：组件模块大类
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * 获取组件模块类型
	 * @return：组件模块类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置组件模块类型
	 * @param type：组件模块类型
	 */
	public void setType(String type) {
		this.type = type;
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
	 * 获取组件调用类名
	 * @return：组件调用类名
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 设置组件调用类名
	 * @param className：组件调用类名
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 获取组件最新版本号
	 * @return：组件最新版本号
	 */
	public String getEdition() {
		return edition;
	}

	/**
	 * 设置组件最新版本号
	 * @param edition：组件最新版本号
	 */
	public void setEdition(String edition) {
		this.edition = edition;
	}

	/**
	 * 获取业务系统编号
	 * @return
	 */
	public String getBussID() {
		return bussID;
	}
	/**
	 * 设置业务系统编号
	 * @param bussID
	 */
	public void setBussID(String bussID) {
		this.bussID = bussID;
	}
	
}
