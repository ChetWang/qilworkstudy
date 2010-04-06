
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-26
 * @���ܣ�ҵ��淶����ʵ����
 *
 */
public class IndunormTypeBean implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1121503833540868537L;
	/**
     * �淶�������
     */
    private String shortName = null;
    /**
     * �淶����
     */
    private String name = null;
    /**
     * �淶����
     */
    private String quote = null;
    /**
     * �淶ע��
     */
    private String desc = null;
	/**
	 * ����
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * ����
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * ����
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * ����
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * ����
	 * @return the quote
	 */
	public String getQuote() {
		return quote;
	}
	/**
	 * ����
	 * @param quote the quote to set
	 */
	public void setQuote(String quote) {
		this.quote = quote;
	}
	/**
	 * ����
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * ����
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
