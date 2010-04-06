package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>
 * ���⣺CodeInfoBean.java
 * </p>
 * <p>
 * ������ �������Ϣ��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-04
 * @version 1.0
 */
public class CodeInfoBean implements Serializable {

	/**
	 * Serial���
	 */
	private static final long serialVersionUID = -2271805018967381696L;

	/**
	 * ��������������ʶ��
	 */
	private String shortName;
	/**
	 * ����ֵ
	 */
	private String value;
	/**
	 * ��������
	 */
	private String name;
	/**
	 * ������
	 */
	private String spell;
	/**
	 * ��������
	 */
	private String desc;
	/**
	 * ���ڵ�������
	 */
	private String parentShortName;
	/**
	 * ���ڵ����ֵ
	 */
	private String parentValue;
	/**
	 * �������1
	 */
	private String param1;
	/**
	 * �������2
	 */
	private String param2;
	/**
	 * �������3
	 */
	private String param3;
	/**
	 * �������4
	 */
	private String param4;
	/**
	 * �������5
	 */
	private String param5;
	/**
	 * Ψһ��ʶ
	 */
	private String cc_id;
	
	
	
	
	/**
	 * ��ȡ�������
	 * @return
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * ���ô������
	 * @param shortName
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	/**
	 * ��ȡ��������
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * ���ô�������
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * ��ȡ����ֵ
	 * @return value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * ���ô���ֵ
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * ��ȡ���ڵ�������
	 * @return parentShortName
	 */
	public String getParentShortName() {
		return parentShortName;
	}
	/**
	 * ���ø��ڵ�������
	 * @param parentShortName
	 */
	public void setParentShortName(String parentShortName) {
		this.parentShortName = parentShortName;
	}
	
	/**
	 * ��ȡ���ڵ����ֵ
	 * @return parentValue
	 */
	public String getParentValue() {
		return parentValue;
	}
	/**
	 * ���ø��ڵ����ֵ
	 * @param parentValue
	 */
	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}
	/**
	 * ��ȡ����ƴ��
	 * @return
	 */
	public String getSpell() {
		return spell;
	}
	public void setSpell(String spell) {
		this.spell = spell;
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
	public String getParam4() {
		return param4;
	}
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	public String getParam5() {
		return param5;
	}
	public void setParam5(String param5) {
		this.param5 = param5;
	}
	public String getCc_id() {
		return cc_id;
	}
	public void setCc_id(String cc_id) {
		this.cc_id = cc_id;
	}
}
