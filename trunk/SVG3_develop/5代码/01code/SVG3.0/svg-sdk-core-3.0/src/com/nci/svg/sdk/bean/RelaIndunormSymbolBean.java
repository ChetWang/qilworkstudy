package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>
 * 标题：RelaIndunormSymbolBean.java
 * </p>
 * <p>
 * 描述： 图元与规范数据域关联信息类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-24
 * @version 1.0
 */
public class RelaIndunormSymbolBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -57758509410196894L;
	/**
	 * add by yux,2009-1-2
	 * 规范类型bean
	 */
	private IndunormTypeBean typeBean = new IndunormTypeBean();
	
	/**
	 * add by yux,2009-1-2
	 * 规范数据域种类bean
	 */
	private IndunormMetadataBean metadataBean = new IndunormMetadataBean();
	
	/**
	 * add by yux,2009-1-2
	 * 规范数据域字段bean
	 */
	private IndunormFieldBean fieldBean = new IndunormFieldBean();

	/**
	 * 返回规范类型bean
	 * @return the typeBean
	 */
	public IndunormTypeBean getTypeBean() {
		return typeBean;
	}

	/**
	 * 设置规范类型bean
	 * @param typeBean the typeBean to set
	 */
	public void setTypeBean(IndunormTypeBean typeBean) {
		this.typeBean = typeBean;
	}

	/**
	 * 返回规范数据域种类bean
	 * @return the metadataBean
	 */
	public IndunormMetadataBean getMetadataBean() {
		return metadataBean;
	}

	/**
	 * 设置规范数据域种类bean
	 * @param metadataBean the metadataBean to set
	 */
	public void setMetadataBean(IndunormMetadataBean metadataBean) {
		this.metadataBean = metadataBean;
	}

	/**
	 * 返回规范数据域字段bean
	 * @return the fieldBean
	 */
	public IndunormFieldBean getFieldBean() {
		return fieldBean;
	}

	/**
	 * 设置规范数据域字段bean
	 * @param fieldBean the fieldBean to set
	 */
	public void setFieldBean(IndunormFieldBean fieldBean) {
		this.fieldBean = fieldBean;
	}

}
