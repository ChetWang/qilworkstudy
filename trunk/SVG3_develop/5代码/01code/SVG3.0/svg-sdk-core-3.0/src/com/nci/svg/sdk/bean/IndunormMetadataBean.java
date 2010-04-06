
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-26
 * @���ܣ�ҵ��淶������ʵ����
 *
 */
public class IndunormMetadataBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6878562439141071666L;
	/**
     * ���
     */
    private String id;
    /**
     * ����
     */
    private String shortName;
    /**
     * ����
     */
    private String name;
    /**
     * ��������
     */
    private String variety;
    /**
     * ����
     */
    private String desc;
	/**
	 * ����
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * ����
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
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
	 * @return the variety
	 */
	public String getVariety() {
		return variety;
	}
	/**
	 * ����
	 * @param variety the variety to set
	 */
	public void setVariety(String variety) {
		this.variety = variety;
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
		return id + shortName;
	}
}
