
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-18
 * @功能：模型类型实体类
 *
 */
public class ModelTypeBean implements Serializable {
	private static final long serialVersionUID = -4926083134711495199L;
	/**
	 * add by yux,2009-1-18
	 * 业务系统模型
	 */
	public static final String BUSINESS_MODEL = "business";
	/**
	 * add by yux,2009-1-18
	 * 自定义模型
	 */
	public static final String CUSTOM_MODEL = "custom";
	/**
	 * add by yux,2009-1-18
	 * 模型编号
	 */
	private String id = null;
    /**
     * add by yux,2009-1-18
     * 模型名称
     */
    private String name = null;
    
    /**
     * add by yux,2009-1-18
     * 模型类型
     */
    private String type = null;
    
    /**
     * add by yux,2009-1-18
     * 模型短名
     */
    private String shortName = null;
    

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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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


    
}
