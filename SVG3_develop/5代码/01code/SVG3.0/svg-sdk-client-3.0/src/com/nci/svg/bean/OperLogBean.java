package com.nci.svg.bean;

import java.io.Serializable;

import org.w3c.dom.Element;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-1-9
 * @���ܣ�������־ʵ��
 *
 */
public class OperLogBean implements Serializable {
	public static final int APPEND_OPER = 1;
	public static final int MODIFY_OPER = 0;
	public static final int DELETE_OPER = -1;
    /**
     * add by yux,2009-1-9
     * ͼԪ���
     */
    private String id;
    /**
     * add by yux,2009-1-9
     * �ڵ�
     */
    private Element element;
    /**
     * add by yux,2009-1-9
     * ������ʽ
     */
    private int operType = 0;
    
    public OperLogBean()
    {
    	
    }
    
    public OperLogBean(String id,Element element,int operType)
    {
    	this.id = id;
    	this.element = element;
    	this.operType = operType;
    }
	/**
	 * ����ͼԪ���
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * ����ͼԪ���
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * ���ؽڵ�
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}
	/**
	 * ���ýڵ�
	 * @param element the element to set
	 */
	public void setElement(Element element) {
		this.element = element;
	}
	/**
	 * ���ز�����ʽ
	 * @return the operType
	 */
	public int getOperType() {
		return operType;
	}
	/**
	 * ���ò�����ʽ
	 * @param operType the operType to set
	 */
	public void setOperType(int operType) {
		this.operType = operType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id;
	}
}
