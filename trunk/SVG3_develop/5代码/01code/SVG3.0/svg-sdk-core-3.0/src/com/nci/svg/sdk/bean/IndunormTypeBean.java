
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-12-26
 * @功能：业务规范类型实体类
 *
 */
public class IndunormTypeBean implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1121503833540868537L;
	/**
     * 规范种类短名
     */
    private String shortName = null;
    /**
     * 规范名称
     */
    private String name = null;
    /**
     * 规范引用
     */
    private String quote = null;
    /**
     * 规范注释
     */
    private String desc = null;
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
	 * @return the quote
	 */
	public String getQuote() {
		return quote;
	}
	/**
	 * 设置
	 * @param quote the quote to set
	 */
	public void setQuote(String quote) {
		this.quote = quote;
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
		return shortName;
	}
    
}
