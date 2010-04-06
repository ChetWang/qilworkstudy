
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-12-26
 * @功能：业务规范描述实体类
 *
 */
public class IndunormDescBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5696829147790229111L;
	/**
     * 描述编号
     */
    private String id = null;
    /**
     * 描述短名
     */
    private String shortName = null;
    /**
     * 描述名称
     */
    private String name = null;
    /**
     * 规范种类短名
     */
    private String variety = null;
    /**
     * 描述注释
     */
    private String desc = null;
	/**
	 * 返回
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 返回
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * 设置
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * 返回
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 返回
	 * @return the variety
	 */
	public String getVariety() {
		return variety;
	}
	/**
	 * 设置
	 * @param variety the variety to set
	 */
	public void setVariety(String variety) {
		this.variety = variety;
	}
	/**
	 * 返回
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * 设置
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
