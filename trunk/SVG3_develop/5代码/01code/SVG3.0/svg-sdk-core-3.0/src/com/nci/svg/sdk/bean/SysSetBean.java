package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-22
 * @���ܣ�ϵͳ����ʵ����
 *
 */
public class SysSetBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6497886101124484566L;
	/**
     * ϵͳ�������ñ�ţ����������
     */
    private String id = null;
    /**
     * ������Ψһ��ʶ
     */
    private String shortName = null;
    /**
     * ����
     */
    private String name = null;
    /**
     * ����
     */
    private String desc = null;
    /**
     * ϵͳ���ò���1
     */
    private String param1 = null;
    /**
     * ϵͳ���ò���2
     */
    private String param2 = null;
    /**
     * ϵͳ���ò���3
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
