
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-1-12
 * @���ܣ��򻯰�ҵ��淶ʵ����
 *
 */
public class SimpleIndunormBean implements Serializable {
	private static final long serialVersionUID = -8753432125629589943L;
	/**
     * add by yux,2009-1-12
     * �淶����
     */
    private String type;
    /**
     * add by yux,2009-1-12
     * �淶����
     */
    private String typeName;
    /**
     * add by yux,2009-1-12
     * �淶�����ͣ�Ϊ��ʱ��ʾ�ļ�����
     */
    private String metadata;
    /**
     * add by yux,2009-1-12
     * �淶����������
     */
    private String metadataName;
    /**
     * add by yux,2009-1-12
     * �淶�ֶ���
     */
    private String field;
    /**
     * add by yux,2009-1-12
     * �淶�ֶ�����
     */
    private String fieldName;
	/**
	 * ����
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * ����
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * ����
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * ����
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * ����
	 * @return the metadata
	 */
	public String getMetadata() {
		return metadata;
	}
	/**
	 * ����
	 * @param metadata the metadata to set
	 */
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
	/**
	 * ����
	 * @return the metadataName
	 */
	public String getMetadataName() {
		return metadataName;
	}
	/**
	 * ����
	 * @param metadataName the metadataName to set
	 */
	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}
	/**
	 * ����
	 * @return the field
	 */
	public String getField() {
		return field;
	}
	/**
	 * ����
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}
	/**
	 * ����
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * ����
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
