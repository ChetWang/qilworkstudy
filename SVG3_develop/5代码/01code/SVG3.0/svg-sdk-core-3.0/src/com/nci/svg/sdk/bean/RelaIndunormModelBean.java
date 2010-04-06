package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * @author yx.nci
 *
 */
public class RelaIndunormModelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9029210616031555528L;
	
	/**
	 * 业务系统编号
	 */
	private String busiID;
	/**
	 * 业务系统名称
	 */
	private String busiName;
	/**
	 * 模型编号
	 */
	private String modelID;
	/**
	 * 模型名称
	 */
	private String modelName;
	/**
	 * 模型属性编号
	 */
	private String modelAttrID;
	/**
	 * 模型属性名
	 */
	private String modelAttrName;
	/**
	 * 规范类型代码值
	 */
	private String normType;
	/**
	 * 规范类型代码名称
	 */
	private String normTypeName;
	/**
	 * 规范编号
	 */
	private String normID;
	/**
	 * 规范名称
	 */
	private String normName;
	/**
	 * 有效标记代码值
	 */
	private String validity;
	/**
	 * 有效标记代码名称
	 */
	private String validityName;
	
	/**
	 * 获取模型编号
	 * @return
	 */
	public String getModelID() {
		return modelID;
	}
	/**
	 * 设置模型编号
	 * @param modelID
	 */
	public void setModelID(String modelID) {
		this.modelID = modelID;
	}
	/**
	 * 获取模型属性名
	 * @return
	 */
	public String getModelAttrName() {
		return modelAttrName;
	}
	/**
	 * 设置模型属性名
	 * @param modelAttrName
	 */
	public void setModelAttrName(String modelAttrName) {
		this.modelAttrName = modelAttrName;
	}
	/**
	 * 获取规范代码值
	 * @return
	 */
	public String getNormType() {
		return normType;
	}
	/**
	 * 设置规范类型代码值
	 * @param normType
	 */
	public void setNormType(String normType) {
		this.normType = normType;
	}
	/**
	 * 获取规范类型代码名称
	 * @return
	 */
	public String getNormTypeName() {
		return normTypeName;
	}
	/**
	 * 设置规范类型代码名称
	 * @param normTypeName
	 */
	public void setNormTypeName(String normTypeName) {
		this.normTypeName = normTypeName;
	}
	/**获取规范编号
	 * @return
	 */
	public String getNormID() {
		return normID;
	}
	/**
	 * 设置规范编号
	 * @param normID
	 */
	public void setNormID(String normID) {
		this.normID = normID;
	}
	/**
	 * 获取规范名称
	 * @return
	 */
	public String getNormName() {
		return normName;
	}
	/**
	 * 设置规范名词
	 * @param normName
	 */
	public void setNormName(String normName) {
		this.normName = normName;
	}
	/**
	 * 获取游戏标志代码值
	 * @return
	 */
	public String getValidity() {
		return validity;
	}
	/**
	 * 设置有效标志代码值
	 * @param validity
	 */
	public void setValidity(String validity) {
		this.validity = validity;
	}
	/**
	 * 获取有效标志代码名称
	 * @return
	 */
	public String getValidityName() {
		return validityName;
	}
	/**
	 * 设置有效标记代码名称
	 * @param validityName
	 */
	public void setValidityName(String validityName) {
		this.validityName = validityName;
	}
	/**
	 * 获取业务系统编号
	 * @return
	 */
	public String getBusiID() {
		return busiID;
	}
	/**
	 * 设置业务系统编号
	 * @param busiID
	 */
	public void setBusiID(String busiID) {
		this.busiID = busiID;
	}
	/**
	 * 获取业务系统名称
	 * 
	 * @return
	 */
	public String getBusiName() {
		return busiName;
	}
	/**
	 * 设置业务系统名称
	 * 
	 * @param busiName
	 */
	public void setBusiName(String busiName) {
		this.busiName = busiName;
	}
	/**
	 * 获取模型名称
	 * @return
	 */
	public String getModelName() {
		return modelName;
	}
	/**
	 * 设置模型名称
	 * @param modelName
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	/**
	 * 获取模型属性编号
	 * @return
	 */
	public String getModelAttrID() {
		return modelAttrID;
	}
	/**
	 * 设置模型属性编号
	 * @param modelAttrID
	 */
	public void setModelAttrID(String modelAttrID) {
		this.modelAttrID = modelAttrID;
	}

}
