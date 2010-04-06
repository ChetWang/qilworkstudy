package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>
 * ���⣺RelaIndunormSymbolBean.java
 * </p>
 * <p>
 * ������ ͼԪ��淶�����������Ϣ��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-24
 * @version 1.0
 */
public class RelaIndunormSymbolBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -57758509410196894L;
	/**
	 * add by yux,2009-1-2
	 * �淶����bean
	 */
	private IndunormTypeBean typeBean = new IndunormTypeBean();
	
	/**
	 * add by yux,2009-1-2
	 * �淶����������bean
	 */
	private IndunormMetadataBean metadataBean = new IndunormMetadataBean();
	
	/**
	 * add by yux,2009-1-2
	 * �淶�������ֶ�bean
	 */
	private IndunormFieldBean fieldBean = new IndunormFieldBean();

	/**
	 * ���ع淶����bean
	 * @return the typeBean
	 */
	public IndunormTypeBean getTypeBean() {
		return typeBean;
	}

	/**
	 * ���ù淶����bean
	 * @param typeBean the typeBean to set
	 */
	public void setTypeBean(IndunormTypeBean typeBean) {
		this.typeBean = typeBean;
	}

	/**
	 * ���ع淶����������bean
	 * @return the metadataBean
	 */
	public IndunormMetadataBean getMetadataBean() {
		return metadataBean;
	}

	/**
	 * ���ù淶����������bean
	 * @param metadataBean the metadataBean to set
	 */
	public void setMetadataBean(IndunormMetadataBean metadataBean) {
		this.metadataBean = metadataBean;
	}

	/**
	 * ���ع淶�������ֶ�bean
	 * @return the fieldBean
	 */
	public IndunormFieldBean getFieldBean() {
		return fieldBean;
	}

	/**
	 * ���ù淶�������ֶ�bean
	 * @param fieldBean the fieldBean to set
	 */
	public void setFieldBean(IndunormFieldBean fieldBean) {
		this.fieldBean = fieldBean;
	}

}
