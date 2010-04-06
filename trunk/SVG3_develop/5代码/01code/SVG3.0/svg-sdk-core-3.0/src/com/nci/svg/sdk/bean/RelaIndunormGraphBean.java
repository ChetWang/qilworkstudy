package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>
 * 标题：RelaIndunormGraph.java
 * </p>
 * <p>
 * 描述： 规范与业务图关联信息类
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
public class RelaIndunormGraphBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -333316961001065460L;
	/**
	 * 业务系统编号
	 */
	private SimpleCodeBean bussBean = new SimpleCodeBean();
	/**
	 * 图业务类型代码值
	 */
	private SimpleCodeBean graphTypeBean = new SimpleCodeBean();
	
	/**
	 * 规范种类
	 */
	private IndunormTypeBean typeBean = new IndunormTypeBean();
	
	/**
	 * 规范对应的配置
	 */
	private String defs ;



	/**
	 * 返回
	 * @return the bussBean
	 */
	public SimpleCodeBean getBussBean() {
		return bussBean;
	}

	/**
	 * 设置
	 * @param bussBean the bussBean to set
	 */
	public void setBussBean(SimpleCodeBean bussBean) {
		this.bussBean = bussBean;
	}

	/**
	 * 返回
	 * @return the graphTypeBean
	 */
	public SimpleCodeBean getGraphTypeBean() {
		return graphTypeBean;
	}

	/**
	 * 设置
	 * @param graphTypeBean the graphTypeBean to set
	 */
	public void setGraphTypeBean(SimpleCodeBean graphTypeBean) {
		this.graphTypeBean = graphTypeBean;
	}

	/**
	 * 返回
	 * @return the typeBean
	 */
	public IndunormTypeBean getTypeBean() {
		return typeBean;
	}

	/**
	 * 设置
	 * @param typeBean the typeBean to set
	 */
	public void setTypeBean(IndunormTypeBean typeBean) {
		this.typeBean = typeBean;
	}

	/**
	 * 返回
	 * @return the defs
	 */
	public String getDefs() {
		return defs;
	}

	/**
	 * 设置
	 * @param defs the defs to set
	 */
	public void setDefs(String defs) {
		this.defs = defs;
	}


}
