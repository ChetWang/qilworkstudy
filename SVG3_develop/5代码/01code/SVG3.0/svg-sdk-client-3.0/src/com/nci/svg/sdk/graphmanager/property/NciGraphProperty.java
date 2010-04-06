/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-2-2
 * @功能：图形属性框
 *
 */
package com.nci.svg.sdk.graphmanager.property;

import java.util.Set;
import java.util.UUID;

import org.apache.batik.dom.svg.SVGOMTextElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.nci.svg.sdk.bean.SimpleCodeBean;
import com.nci.svg.sdk.client.EditorAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * @author yx.nci
 * 
 */
public class NciGraphProperty extends DefaultProperty {
	/**
	 * add by yux,2009-2-4
	 * 当前
	 */
	protected SVGHandle handle = null;
	protected String propertyType = null;
	protected String propertyName = null;
	protected String defaultValue = null;
	protected String constraint = null;
	protected String oldValue = null;
	protected EditorAdapter nciEditor = null;

	public NciGraphProperty(EditorAdapter editor, SVGHandle handle, String id,
			String name, String parentID, String parentName, Class classType,
			String propertyType, String defaultValue, String constraint) {

		this(editor, handle, id, name, parentID, parentName, propertyType,
				defaultValue, constraint);
		if (classType != null)
			setType(classType);
	}

	public NciGraphProperty(EditorAdapter editor, SVGHandle handle, String id,
			String name, String parentID, String parentName,
			String propertyType, String defaultValue, String constraint) {
		super();
		this.handle = handle;
		this.propertyType = propertyType;
		if(id != null)
	    	this.propertyName = id
				.substring(id.indexOf("_") + 1, id.length());
		this.defaultValue = defaultValue;
		this.constraint = constraint;
		this.nciEditor = editor;
		setName(UUID.randomUUID().toString());
		if (parentName == null || parentName.length() == 0) {
			setCategory(ResourcesManager.bundle.getString(parentID));
		} else
			setCategory(parentName);
		if (name == null || name.length() == 0) {
			setDisplayName(ResourcesManager.bundle.getString(id));
		} else
			setDisplayName(name);
	}

	private AbstractPropertyEditor propertyEditor = null;

	public NciGraphProperty(EditorAdapter editor, SVGHandle handle, String id,
			String name, String parentID, String parentName,
			AbstractPropertyEditor propertyEditor, String propertyType,
			String defaultValue, String constraint) {
		this(editor, handle, id, name, parentID, parentName, propertyType,
				defaultValue, constraint);
		this.propertyType = propertyType;
		this.propertyEditor = propertyEditor;
	}

	/* (non-Javadoc)
	 * @see com.l2fprod.common.propertysheet.AbstractProperty#initializeValue(java.lang.Object)
	 */
	@Override
	protected void initializeValue(Object value) {
		if(propertyEditor != null)
		{
			propertyEditor.setValue(value);
		}
		else
		super.initializeValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.l2fprod.common.propertysheet.DefaultProperty#readFromObject(java.lang.Object)
	 */
	@Override
	public void readFromObject(Object arg0) {
		if (arg0 instanceof Element) {
			Element element = (Element) arg0;
			String value = null;
			if (propertyType.equals("child")) {
				value = getChildValue(element);
			} else if (propertyType.equals("attribute")) {
				value = getAttributeValue(element);
			} else if (propertyType.equals("style")) {
				value = getStylePropertyValue(element);
			}
			
			if(value == null || value.length() == 0)
			{
				value = defaultValue;
			}
			initializeValue(value);
		}
	}

	/* (non-Javadoc)
	 * @see com.l2fprod.common.propertysheet.AbstractProperty#getValue()
	 */
	@Override
	public Object getValue() {
		String value = null;
		if(propertyEditor != null)
		{
			Object obj = propertyEditor.getValue();
			if(obj instanceof SimpleCodeBean)
			{
				value = ((SimpleCodeBean)obj).getCode();
			}
			else if(obj instanceof String)
			    value = (String)obj;
		}
		else
		{
			Object obj = super.getValue();
			if(obj instanceof SimpleCodeBean)
			{
				value = ((SimpleCodeBean)obj).getName();
			}
			else if(obj instanceof String)
			    value = (String)obj;
		}
		return value;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.l2fprod.common.propertysheet.DefaultProperty#writeToObject(java.lang.Object)
	 */
	@Override
	public void writeToObject(Object arg0) {
		if(propertyName == null || propertyName.length() == 0)
			return;
		if (arg0 instanceof Element) {
			final Element oldElement = (Element)arg0;
            Element element = (Element)arg0;
			String value = null;
			value = (String)getValue();
			if (propertyType.equals("child")) {
				setChildValue(element,value);
			} else if (propertyType.equals("attribute")) {
				setAttributeValue(element,value);
			} else if (propertyType.equals("style")) {
				setStylePropertyValue(element,value);
			}
			nciEditor.getSvgSession().refreshElement(oldElement, element);
			nciEditor.getSvgSession().refreshCurrentHandleImediately();
			
//			handle.getSelection().handleSelection(element, false, true);
		}
		else if(arg0 instanceof Set)
		{
			Set<Element> sets = (Set<Element>)arg0;
			for(Element element :sets)
			{
				final Element oldElement = element;
				String value = null;
				value = (String)getValue();
				if (propertyType.equals("child")) {
					setChildValue(element,value);
				} else if (propertyType.equals("attribute")) {
					setAttributeValue(element,value);
				} else if (propertyType.equals("style")) {
					setStylePropertyValue(element,value);
				}
				nciEditor.getSvgSession().refreshElement(oldElement, element);
			}
			nciEditor.getSvgSession().refreshCurrentHandleImediately();
		}
		
	}

	private void setChildValue(Element element, String value) {
		if (element != null && propertyName != null && !propertyName.equals("")
				&& value != null && !value.equals(oldValue)) {

			// checks all the child nodes of the element to find the
			// text node, if it is found, sets its value
			for (Node cur = element.getFirstChild(); cur != null; cur = cur
					.getNextSibling()) {

				if (cur.getNodeName().equals(propertyName)) {

					// sets the value of the node
					cur.setNodeValue(value);
					break;
				}
			}
		}
	}


	private String getChildValue(Element element) {
		String value = "";
		Node  cur = null;

		if (element != null && propertyName != null && !propertyName.equals("")) {

			// for each child of the given element, tests if the name of
			// these children is equals to the parameter string
			StringBuffer sb = new StringBuffer();
			for (cur = element.getFirstChild(); cur != null; cur = cur
					.getNextSibling()) {

				if (cur.getNodeName().equals(propertyName)) {

					sb.append(cur.getNodeValue());
					break;
				} else { // modified by wangql
					if (element instanceof SVGOMTextElement
							&& cur.getNodeName().equals("tspan")) {
						sb.append(cur.getChildNodes().item(0).getNodeValue())
								.append("\n");
					}
				}
			}
			value = sb.toString();
		}

		if (value == null || (value != null && value.equals(""))) {

			value = defaultValue;
		}

		return value;
	}

	private void setAttributeValue(Element element, String value) {
       if(element != null)
    	   element.setAttribute(propertyName, value);
	}

	private String getAttributeValue(Element element) {
		String value = "";
		if (element != null) {
			value = element.getAttribute(propertyName);
		}
		return value;
	}

	private void setStylePropertyValue(Element element, String value) {
		handle.getEditor().getSVGToolkit().setStyleProperty(
				element, propertyName, value);
	}

	private String getStylePropertyValue(Element element) {
		String value = "";
		if (element != null) {
			value = handle.getEditor().getSVGToolkit().getStyleProperty(
					element, propertyName);
		}
		return value;
	}

	/**
	 * 设置
	 * @param propertyEditor the propertyEditor to set
	 */
	public void setPropertyEditor(AbstractPropertyEditor propertyEditor) {
		this.propertyEditor = propertyEditor;
	}

}
