
package com.nci.svg.sdk.bean;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-1-18
 * @���ܣ�ģ��ʵ����
 *
 */
public class ModelBean implements Serializable {
	private static final long serialVersionUID = -8025897398657939384L;
	/**
     * add by yux,2009-1-18
     * ģ������
     */
    private ModelTypeBean typeBean = new ModelTypeBean();
    /**
     * add by yux,2009-1-18
     * ģ�������嵥
     * key���������ͣ�Ψһ��ʶ��״̬���������ԣ���չ���ԣ�
     * value:HashMap<String,ModelPropertyBean>,keyΪ�������ƣ�valueΪ����bean
     */
    private HashMap properties = new HashMap();
    /**
     * add by yux,2009-1-18
     * ģ�Ͷ����嵥
     * key:��������
     * value:HashMap<String,ModelActionBean>,keyΪ�������ƣ�valueΪ����bean
     */
    private HashMap actions = new HashMap();
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return typeBean.getName();
	}
	/**
	 * ����
	 * @return the typeBean
	 */
	public ModelTypeBean getTypeBean() {
		return typeBean;
	}
	/**
	 * ����
	 * @param typeBean the typeBean to set
	 */
	public void setTypeBean(ModelTypeBean typeBean) {
		this.typeBean = typeBean;
	}
	/**
	 * ����
	 * @return the properties
	 */
	public HashMap getProperties() {
		return properties;
	}
	/**
	 * ����
	 * @param properties the properties to set
	 */
	public void setProperties(HashMap properties) {
		this.properties = properties;
	}
	/**
	 * ����
	 * @return the actions
	 */
	public HashMap getActions() {
		return actions;
	}
	/**
	 * ����
	 * @param actions the actions to set
	 */
	public void setActions(HashMap actions) {
		this.actions = actions;
	}
	
}
