
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-18
 * @功能：模型属性bean
 *
 */
public class ModelPropertyBean implements Serializable {
	private static final long serialVersionUID = 4296813370466295423L;
	/**
	 * add by yux,2009-1-18
	 * 唯一标识
	 */
	public static final String TYPE_ID = "id";
	/**
	 * add by yux,2009-1-18
	 * 基本属性
	 */
	public static final String TYPE_BASEPROPERTY = "base";
	/**
	 * add by yux,2009-1-18
	 * 扩展属性
	 */
	public static final String TYPE_EXTRAPROPERATY = "extra";
	/**
	 * add by yux,2009-1-18
	 * 状态
	 */
	public static final String TYPE_STATUS = "4status" ;
    /**
     * add by yux,2009-1-18
     * 属性编号
     */
    private String id = null;
    
    /**
     * add by yux,2009-1-19
     * 所属模型编号
     */
    private String modelID = null;
    /**
     * add by yux,2009-1-18
     * 属性短名
     */
    private String shortName = null;
    /**
     * add by yux,2009-1-18
     * 属性名称
     */
    private String name = null;
    /**
     * add by yux,2009-1-18
     * 属性类型
     */
    private String type = null;
    /**
     * add by yux,2009-1-18
     * 属性对应代码名称
     */
    private String code = null;
    /**
     * add by yux,2009-1-18
     * 可视标识
     */
    private String visible = null;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 返回
	 * @return the visible
	 */
	public String getVisible() {
		return visible;
	}
	/**
	 * 设置
	 * @param visible the visible to set
	 */
	public void setVisible(String visible) {
		this.visible = visible;
	}
	/**
	 * 返回
	 * @return the modelID
	 */
	public String getModelID() {
		return modelID;
	}
	/**
	 * 设置
	 * @param modelID the modelID to set
	 */
	public void setModelID(String modelID) {
		this.modelID = modelID;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
}
