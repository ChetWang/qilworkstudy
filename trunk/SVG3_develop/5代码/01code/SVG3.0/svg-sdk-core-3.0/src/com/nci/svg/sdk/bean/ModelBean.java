
package com.nci.svg.sdk.bean;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-18
 * @功能：模型实体类
 *
 */
public class ModelBean implements Serializable {
	private static final long serialVersionUID = -8025897398657939384L;
	/**
     * add by yux,2009-1-18
     * 模型种类
     */
    private ModelTypeBean typeBean = new ModelTypeBean();
    /**
     * add by yux,2009-1-18
     * 模型属性清单
     * key：属性类型（唯一标识，状态，基础属性，扩展属性）
     * value:HashMap<String,ModelPropertyBean>,key为属性名称，value为属性bean
     */
    private HashMap properties = new HashMap();
    /**
     * add by yux,2009-1-18
     * 模型动作清单
     * key:动作类型
     * value:HashMap<String,ModelActionBean>,key为动作名称，value为动作bean
     */
    private HashMap actions = new HashMap();
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return typeBean.getName();
	}
	/**
	 * 返回
	 * @return the typeBean
	 */
	public ModelTypeBean getTypeBean() {
		return typeBean;
	}
	/**
	 * 设置
	 * @param typeBean the typeBean to set
	 */
	public void setTypeBean(ModelTypeBean typeBean) {
		this.typeBean = typeBean;
	}
	/**
	 * 返回
	 * @return the properties
	 */
	public HashMap getProperties() {
		return properties;
	}
	/**
	 * 设置
	 * @param properties the properties to set
	 */
	public void setProperties(HashMap properties) {
		this.properties = properties;
	}
	/**
	 * 返回
	 * @return the actions
	 */
	public HashMap getActions() {
		return actions;
	}
	/**
	 * 设置
	 * @param actions the actions to set
	 */
	public void setActions(HashMap actions) {
		this.actions = actions;
	}
	
}
