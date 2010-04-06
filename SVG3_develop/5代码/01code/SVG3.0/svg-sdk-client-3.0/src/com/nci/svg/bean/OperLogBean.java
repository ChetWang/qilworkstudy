package com.nci.svg.bean;

import java.io.Serializable;

import org.w3c.dom.Element;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-9
 * @功能：操作日志实体
 *
 */
public class OperLogBean implements Serializable {
	public static final int APPEND_OPER = 1;
	public static final int MODIFY_OPER = 0;
	public static final int DELETE_OPER = -1;
    /**
     * add by yux,2009-1-9
     * 图元编号
     */
    private String id;
    /**
     * add by yux,2009-1-9
     * 节点
     */
    private Element element;
    /**
     * add by yux,2009-1-9
     * 操作方式
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
	 * 返回图元编号
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置图元编号
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 返回节点
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}
	/**
	 * 设置节点
	 * @param element the element to set
	 */
	public void setElement(Element element) {
		this.element = element;
	}
	/**
	 * 返回操作方式
	 * @return the operType
	 */
	public int getOperType() {
		return operType;
	}
	/**
	 * 设置操作方式
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
