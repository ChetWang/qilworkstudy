package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-12-22
 * @功能：系统设置实体类
 *
 */
public class SysSetBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6497886101124484566L;
	/**
     * 系统参数设置编号，无意义序号
     */
    private String id = null;
    /**
     * 短名，唯一标识
     */
    private String shortName = null;
    /**
     * 名称
     */
    private String name = null;
    /**
     * 描述
     */
    private String desc = null;
    /**
     * 系统配置参数1
     */
    private String param1 = null;
    /**
     * 系统配置参数2
     */
    private String param2 = null;
    /**
     * 系统配置参数3
     */
    private String param3 = null;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
}
