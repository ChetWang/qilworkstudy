package com.nci.svg.sdk.bean;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-1-18
 * @���ܣ�ҵ��淶ʵ����
 * 
 */
public class IndunormBean implements Serializable {
	private static final long serialVersionUID = -7947447564465263511L;
	/**
	 * add by yux,2009-1-18 ҵ��淶����
	 */
	private IndunormTypeBean typeBean = new IndunormTypeBean();
	/**
	 * add by yux,2009-1-18 ҵ��淶��������ӳ���� keyΪ�������ƣ�valueΪ��������IndunormDescBean
	 */
	private HashMap descMap = new HashMap();
	/**
	 * add by yux,2009-1-18 ҵ��淶�����򲿷�ӳ����
	 * keyΪ���������ƣ�valueΪ��������IndunormMetadataBean
	 */
	private HashMap metadataMap = new HashMap();
	/**
	 * add by yux,2009-1-18 ҵ��淶�����ֶβ���ӳ����
	 * keyΪ����������+":"+�ֶ����ƣ�valueΪ��������IndunormFieldBean
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
	 * ����
	 * 
	 * @return the typeBean
	 */
	public IndunormTypeBean getTypeBean() {
		return typeBean;
	}

	/**
	 * ����
	 * 
	 * @param typeBean
	 *            the typeBean to set
	 */
	public void setTypeBean(IndunormTypeBean typeBean) {
		this.typeBean = typeBean;
	}

	/**
	 * ����
	 * 
	 * @return the descMap
	 */
	public HashMap getDescMap() {
		return descMap;
	}

	/**
	 * ����
	 * 
	 * @param descMap
	 *            the descMap to set
	 */
	public void setDescMap(HashMap descMap) {
		this.descMap = descMap;
	}

	/**
	 * ����
	 * 
	 * @return the metadataMap
	 */
	public HashMap getMetadataMap() {
		return metadataMap;
	}

	/**
	 * ����
	 * 
	 * @param metadataMap
	 *            the metadataMap to set
	 */
	public void setMetadataMap(HashMap metadataMap) {
		this.metadataMap = metadataMap;
	}

	/**
	 * ����
	 * 
	 * @return the fieldMap
	 */
	public HashMap getFieldMap() {
		return fieldMap;
	}

	/**
	 * ����
	 * 
	 * @param fieldMap
	 *            the fieldMap to set
	 */
	public void setFieldMap(HashMap fieldMap) {
		this.fieldMap = fieldMap;
	}

}
