
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-1-18
 * @���ܣ�ģ������bean
 *
 */
public class ModelPropertyBean implements Serializable {
	private static final long serialVersionUID = 4296813370466295423L;
	/**
	 * add by yux,2009-1-18
	 * Ψһ��ʶ
	 */
	public static final String TYPE_ID = "id";
	/**
	 * add by yux,2009-1-18
	 * ��������
	 */
	public static final String TYPE_BASEPROPERTY = "base";
	/**
	 * add by yux,2009-1-18
	 * ��չ����
	 */
	public static final String TYPE_EXTRAPROPERATY = "extra";
	/**
	 * add by yux,2009-1-18
	 * ״̬
	 */
	public static final String TYPE_STATUS = "4status" ;
    /**
     * add by yux,2009-1-18
     * ���Ա��
     */
    private String id = null;
    
    /**
     * add by yux,2009-1-19
     * ����ģ�ͱ��
     */
    private String modelID = null;
    /**
     * add by yux,2009-1-18
     * ���Զ���
     */
    private String shortName = null;
    /**
     * add by yux,2009-1-18
     * ��������
     */
    private String name = null;
    /**
     * add by yux,2009-1-18
     * ��������
     */
    private String type = null;
    /**
     * add by yux,2009-1-18
     * ���Զ�Ӧ��������
     */
    private String code = null;
    /**
     * add by yux,2009-1-18
     * ���ӱ�ʶ
     */
    private String visible = null;
	/**
	 * ����
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * ����
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * ����
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * ����
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * ����
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * ����
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * ����
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * ����
	 * @return the visible
	 */
	public String getVisible() {
		return visible;
	}
	/**
	 * ����
	 * @param visible the visible to set
	 */
	public void setVisible(String visible) {
		this.visible = visible;
	}
	/**
	 * ����
	 * @return the modelID
	 */
	public String getModelID() {
		return modelID;
	}
	/**
	 * ����
	 * @param modelID the modelID to set
	 */
	public void setModelID(String modelID) {
		this.modelID = modelID;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
}
