
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-12
 * @功能：简化版业务规范实体类
 *
 */
public class SimpleIndunormBean implements Serializable {
	private static final long serialVersionUID = -8753432125629589943L;
	/**
     * add by yux,2009-1-12
     * 规范种类
     */
    private String type;
    /**
     * add by yux,2009-1-12
     * 规范名称
     */
    private String typeName;
    /**
     * add by yux,2009-1-12
     * 规范子类型，为空时表示文件描述
     */
    private String metadata;
    /**
     * add by yux,2009-1-12
     * 规范子类型名称
     */
    private String metadataName;
    /**
     * add by yux,2009-1-12
     * 规范字段名
     */
    private String field;
    /**
     * add by yux,2009-1-12
     * 规范字段名称
     */
    private String fieldName;
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
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * 设置
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * 返回
	 * @return the metadata
	 */
	public String getMetadata() {
		return metadata;
	}
	/**
	 * 设置
	 * @param metadata the metadata to set
	 */
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
	/**
	 * 返回
	 * @return the metadataName
	 */
	public String getMetadataName() {
		return metadataName;
	}
	/**
	 * 设置
	 * @param metadataName the metadataName to set
	 */
	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}
	/**
	 * 返回
	 * @return the field
	 */
	public String getField() {
		return field;
	}
	/**
	 * 设置
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}
	/**
	 * 返回
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * 设置
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return type + ":" +metadata + ":" + field;
	}
	
}
