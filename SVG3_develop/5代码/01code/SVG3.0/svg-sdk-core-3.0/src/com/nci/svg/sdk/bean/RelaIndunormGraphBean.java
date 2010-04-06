package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>
 * ���⣺RelaIndunormGraph.java
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
public class RelaIndunormGraphBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -333316961001065460L;
	/**
	 * ҵ��ϵͳ���
	 */
	private SimpleCodeBean bussBean = new SimpleCodeBean();
	/**
	 * ͼҵ�����ʹ���ֵ
	 */
	private SimpleCodeBean graphTypeBean = new SimpleCodeBean();
	
	/**
	 * �淶����
	 */
	private IndunormTypeBean typeBean = new IndunormTypeBean();
	
	/**
	 * �淶��Ӧ������
	 */
	private String defs ;



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

	/**
	 * ����
	 * @return the defs
	 */
	public String getDefs() {
		return defs;
	}

	/**
	 * ����
	 * @param defs the defs to set
	 */
	public void setDefs(String defs) {
		this.defs = defs;
	}


}
