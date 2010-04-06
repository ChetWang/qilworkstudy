
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-19
 * @功能：模型与业务规范的关联关系实体类
 *
 */
public class ModelRelaIndunormBean implements Serializable {
	private static final long serialVersionUID = -1075199296598663058L;
	/**
     * add by yux,2009-1-20
     * 模型编号
     */
    private String modelID = null;
    /**
     * add by yux,2009-1-20
     * 模型属性编号
     */
    private String modelPropertyID = null;
    
    /**
     * add by yux,2009-1-20
     * 模型属性名称
     */
    private String modelPropertyName = null;
    /**
     * add by yux,2009-1-20
     * 模型属性类型
     */
    private String modelPropertyType = null;
    
    /**
     * add by yux,2009-1-20
     * 模型属性类型名称
     */
    private String modelPropertyTypeName = null;
    /**
     * add by yux,2009-1-20
     * 属性对应的代码编号
     */
    private String modelPropertyCode = null;
    /**
     * add by yux,2009-1-20
     * 规范短名
     */
    private String indunormShortName = null;
    /**
     * add by yux,2009-1-20
     * 规范描述短名
     */
    private String descShortName = null;
    /**
     * add by yux,2009-1-20
     * 规范数据域短名
     */
    private String metadataShortName = null;
    /**
     * add by yux,2009-1-20
     * 规范数据字段短名
     */
    private String fieldShortName = null;
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
	/**
	 * 返回
	 * @return the modelPropertyID
	 */
	public String getModelPropertyID() {
		return modelPropertyID;
	}
	/**
	 * 设置
	 * @param modelPropertyID the modelPropertyID to set
	 */
	public void setModelPropertyID(String modelPropertyID) {
		this.modelPropertyID = modelPropertyID;
	}
	/**
	 * 返回
	 * @return the modelPropertyName
	 */
	public String getModelPropertyName() {
		return modelPropertyName;
	}
	/**
	 * 设置
	 * @param modelPropertyName the modelPropertyName to set
	 */
	public void setModelPropertyName(String modelPropertyName) {
		this.modelPropertyName = modelPropertyName;
	}
	/**
	 * 返回
	 * @return the modelPropertyType
	 */
	public String getModelPropertyType() {
		return modelPropertyType;
	}
	/**
	 * 设置
	 * @param modelPropertyType the modelPropertyType to set
	 */
	public void setModelPropertyType(String modelPropertyType) {
		this.modelPropertyType = modelPropertyType;
	}
	/**
	 * 返回
	 * @return the modelPropertyTypeName
	 */
	public String getModelPropertyTypeName() {
		return modelPropertyTypeName;
	}
	/**
	 * 设置
	 * @param modelPropertyTypeName the modelPropertyTypeName to set
	 */
	public void setModelPropertyTypeName(String modelPropertyTypeName) {
		this.modelPropertyTypeName = modelPropertyTypeName;
	}
	/**
	 * 返回
	 * @return the indunormShortName
	 */
	public String getIndunormShortName() {
		return indunormShortName;
	}
	/**
	 * 设置
	 * @param indunormShortName the indunormShortName to set
	 */
	public void setIndunormShortName(String indunormShortName) {
		this.indunormShortName = indunormShortName;
	}
	/**
	 * 返回
	 * @return the descShortName
	 */
	public String getDescShortName() {
		return descShortName;
	}
	/**
	 * 设置
	 * @param descShortName the descShortName to set
	 */
	public void setDescShortName(String descShortName) {
		this.descShortName = descShortName;
	}
	/**
	 * 返回
	 * @return the metadataShortName
	 */
	public String getMetadataShortName() {
		return metadataShortName;
	}
	/**
	 * 设置
	 * @param metadataShortName the metadataShortName to set
	 */
	public void setMetadataShortName(String metadataShortName) {
		this.metadataShortName = metadataShortName;
	}
	/**
	 * 返回
	 * @return the fieldShortName
	 */
	public String getFieldShortName() {
		return fieldShortName;
	}
	/**
	 * 设置
	 * @param fieldShortName the fieldShortName to set
	 */
	public void setFieldShortName(String fieldShortName) {
		this.fieldShortName = fieldShortName;
	}
	/**
	 * 返回
	 * @return the modelPropertyCode
	 */
	public String getModelPropertyCode() {
		return modelPropertyCode;
	}
	/**
	 * 设置
	 * @param modelPropertyCode the modelPropertyCode to set
	 */
	public void setModelPropertyCode(String modelPropertyCode) {
		this.modelPropertyCode = modelPropertyCode;
	}

}
