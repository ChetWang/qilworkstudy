/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-19
 * @功能：TODO
 *
 */
package com.nci.svg.bean;

import java.io.Serializable;

import org.w3c.dom.Element;

/**
 * @author yx.nci
 *
 */
public class TreeNodeBean implements Serializable {
	private static final long serialVersionUID = 4793452649605556233L;
	private String text;
	private Element element;
	
	public TreeNodeBean(String text,Element element)
	{
		this.text = text;
		this.element = element;
	}
	/**
	 * 返回
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * 设置
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * 返回
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}
	/**
	 * 设置
	 * @param element the element to set
	 */
	public void setElement(Element element) {
		this.element = element;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return text;
	}
}
