package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>
 * 标题：CodeInfoBean.java
 * </p>
 * <p>
 * 描述： 代码表消息类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-04
 * @version 1.0
 */
public class CodeInfoBean implements Serializable {

	/**
	 * Serial编号
	 */
	private static final long serialVersionUID = -2271805018967381696L;

	/**
	 * 代码短名（代码标识）
	 */
	private String shortName;
	/**
	 * 代码值
	 */
	private String value;
	/**
	 * 代码名称
	 */
	private String name;
	/**
	 * 代码简称
	 */
	private String spell;
	/**
	 * 代码描述
	 */
	private String desc;
	/**
	 * 父节点代码短名
	 */
	private String parentShortName;
	/**
	 * 父节点代码值
	 */
	private String parentValue;
	/**
	 * 代码参数1
	 */
	private String param1;
	/**
	 * 代码参数2
	 */
	private String param2;
	/**
	 * 代码参数3
	 */
	private String param3;
	/**
	 * 代码参数4
	 */
	private String param4;
	/**
	 * 代码参数5
	 */
	private String param5;
	/**
	 * 唯一标识
	 */
	private String cc_id;
	
	
	
	
	/**
	 * 获取代码短名
	 * @return
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * 设置代码短名
	 * @param shortName
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	/**
	 * 获取代码名称
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置代码名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取代码值
	 * @return value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 设置代码值
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 获取父节点代码短名
	 * @return parentShortName
	 */
	public String getParentShortName() {
		return parentShortName;
	}
	/**
	 * 设置父节点代码短名
	 * @param parentShortName
	 */
	public void setParentShortName(String parentShortName) {
		this.parentShortName = parentShortName;
	}
	
	/**
	 * 获取父节点代码值
	 * @return parentValue
	 */
	public String getParentValue() {
		return parentValue;
	}
	/**
	 * 设置父节点代码值
	 * @param parentValue
	 */
	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}
	/**
	 * 获取代码拼音
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
