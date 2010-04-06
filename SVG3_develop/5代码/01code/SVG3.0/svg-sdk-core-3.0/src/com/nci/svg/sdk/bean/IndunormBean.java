package com.nci.svg.sdk.bean;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2009-1-18
 * @功能：业务规范实体类
 * 
 */
public class IndunormBean implements Serializable {
	private static final long serialVersionUID = -7947447564465263511L;
	/**
	 * add by yux,2009-1-18 业务规范描述
	 */
	private IndunormTypeBean typeBean = new IndunormTypeBean();
	/**
	 * add by yux,2009-1-18 业务规范描述部分映射类 key为描述名称，value为该描述的IndunormDescBean
	 */
	private HashMap descMap = new HashMap();
	/**
	 * add by yux,2009-1-18 业务规范数据域部分映射类
	 * key为数据域名称，value为该描述的IndunormMetadataBean
	 */
	private HashMap metadataMap = new HashMap();
	/**
	 * add by yux,2009-1-18 业务规范数据字段部分映射类
	 * key为数据域名称+":"+字段名称，value为该描述的IndunormFieldBean
	 */
	private HashMap fieldMap = new HashMap();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return typeBean.getShortName();
	}

	/**
	 * 返回
	 * 
	 * @return the typeBean
	 */
	public IndunormTypeBean getTypeBean() {
		return typeBean;
	}

	/**
	 * 设置
	 * 
	 * @param typeBean
	 *            the typeBean to set
	 */
	public void setTypeBean(IndunormTypeBean typeBean) {
		this.typeBean = typeBean;
	}

	/**
	 * 返回
	 * 
	 * @return the descMap
	 */
	public HashMap getDescMap() {
		return descMap;
	}

	/**
	 * 设置
	 * 
	 * @param descMap
	 *            the descMap to set
	 */
	public void setDescMap(HashMap descMap) {
		this.descMap = descMap;
	}

	/**
	 * 返回
	 * 
	 * @return the metadataMap
	 */
	public HashMap getMetadataMap() {
		return metadataMap;
	}

	/**
	 * 设置
	 * 
	 * @param metadataMap
	 *            the metadataMap to set
	 */
	public void setMetadataMap(HashMap metadataMap) {
		this.metadataMap = metadataMap;
	}

	/**
	 * 返回
	 * 
	 * @return the fieldMap
	 */
	public HashMap getFieldMap() {
		return fieldMap;
	}

	/**
	 * 设置
	 * 
	 * @param fieldMap
	 *            the fieldMap to set
	 */
	public void setFieldMap(HashMap fieldMap) {
		this.fieldMap = fieldMap;
	}

}
