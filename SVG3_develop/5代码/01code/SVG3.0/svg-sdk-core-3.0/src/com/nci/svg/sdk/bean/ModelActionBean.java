
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-1-18
 * @���ܣ�ģ�Ͷ���ʵ����
 *
 */
public class ModelActionBean implements Serializable {
	private static final long serialVersionUID = 7828556456435941646L;
	/**
	 * add by yux,2009-1-21
	 * ����ƶ�
	 */
	public static final String TYPE_MOUSE_MOVE = "mMove";
	/**
	 * add by yux,2009-1-18
	 * �������
	 */
	public static final String TYPE_MOUSE_LDOWN = "mLDowm";
	/**
	 * add by yux,2009-1-18
	 * �Ҽ�����
	 */
	public static final String TYPE_MOUSE_RDOWM = "mRDowm";
	/**
	 * add by yux,2009-1-18
	 * ���˫��
	 */
	public static final String TYPE_MOUSE_LDOUBLE = "mLDouble";

    /**
     * add by yux,2009-1-21
     * ��ʼ������
     */
    public static final String TYPE_INITLOAD = "initLoad";
    
    /**
     * add by yux,2009-1-21
     * ����ʵ��
     */
    public static final String TYPE_NEWINSTANCE = "newInst";
    
    /**
     * add by yux,2009-1-21
     * ��ʵ��
     */
    public static final String TYPE_RELAINSTANCE = "relaInst";
    
    /**
     * add by yux,2009-1-21
     * �˳��¼�
     */
    public static final String TYPE_CLOSE = "close";
    
    
    /**
     * add by yux,2009-1-18
     * �������
     */
    private String id = null;
    /**
     * add by yux,2009-1-19
     * ģ�ͱ��
     */
    private String modelID = null;
    /**
     * add by yux,2009-1-18
     * ��������
     */
    private String type = null;
    /**
     * add by yux,2009-1-18
     * ��������
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
    private String content = null;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * ����
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
}
