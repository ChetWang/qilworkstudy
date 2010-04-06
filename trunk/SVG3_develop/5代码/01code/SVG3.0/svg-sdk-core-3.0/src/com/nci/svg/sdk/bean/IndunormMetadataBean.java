
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-12-26
 * @功能：业务规范数据域实体类
 *
 */
public class IndunormMetadataBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6878562439141071666L;
	/**
     * 编号
     */
    private String id;
    /**
     * 短名
     */
    private String shortName;
    /**
     * 名称
     */
    private String name;
    /**
     * 所属类型
     */
    private String variety;
    /**
     * 描述
     */
    private String desc;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return id + shortName;
	}
}
