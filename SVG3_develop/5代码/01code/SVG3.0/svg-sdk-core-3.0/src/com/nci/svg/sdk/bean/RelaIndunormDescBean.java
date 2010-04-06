package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>
 * ���⣺RelaIndunormDesc.java
 * </p>
 * <p>
 * ������ �淶��ҵ��ͼ������Ϣ��
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
public class RelaIndunormDescBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8258311428949066103L;

	/**
	 * ҵ��ϵͳ���
	 */
	private SimpleCodeBean bussBean = new SimpleCodeBean();;
	/**
	 * ͼҵ�����ʹ���ֵ
	 */
	private SimpleCodeBean graphTypeBean = new SimpleCodeBean();;
	
	private IndunormDescBean descBean = new IndunormDescBean();
	
	private IndunormTypeBean typeBean = new IndunormTypeBean();

	

	/**
	 * ����
	 * @return the bussBean
	 */
	public SimpleCodeBean getBussBean() {
		return bussBean;
	}

	/**
	 * ����
	 * @param bussBean the bussBean to set
	 */
	public void setBussBean(SimpleCodeBean bussBean) {
		this.bussBean = bussBean;
	}

	/**
	 * ����
	 * @return the graphTypeBean
	 */
	public SimpleCodeBean getGraphTypeBean() {
		return graphTypeBean;
	}

	/**
	 * ����
	 * @param graphTypeBean the graphTypeBean to set
	 */
	public void setGraphTypeBean(SimpleCodeBean graphTypeBean) {
		this.graphTypeBean = graphTypeBean;
	}

	/**
	 * ����
	 * @return the descBean
	 */
	public IndunormDescBean getDescBean() {
		return descBean;
	}

	/**
	 * ����
	 * @param descBean the descBean to set
	 */
	public void setDescBean(IndunormDescBean descBean) {
		this.descBean = descBean;
	}

	/**
	 * ����
	 * @return the typeBean
	 */
	public IndunormTypeBean getTypeBean() {
		return typeBean;
	}

	/**
	 * ����
	 * @param typeBean the typeBean to set
	 */
	public void setTypeBean(IndunormTypeBean typeBean) {
		this.typeBean = typeBean;
	}

}
