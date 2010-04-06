/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-20
 * @功能：TODO
 *
 */
package com.nci.svg.sdk.graphmanager.property;

import java.util.Set;

import org.w3c.dom.Element;

import com.l2fprod.common.propertysheet.DefaultProperty;
import com.nci.svg.sdk.bean.ModelPropertyBean;
import com.nci.svg.sdk.bean.ModelRelaIndunormBean;
import com.nci.svg.sdk.client.EditorAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * @author yx.nci
 * 
 */
public class NciBussProperty extends DefaultProperty {

	protected ModelRelaIndunormBean bean = null;
	protected SVGHandle handle = null;

	public NciBussProperty(EditorAdapter editor, ModelRelaIndunormBean bean,
			SVGHandle handle) {
		this.bean = bean;
		this.handle = handle;
		setName(bean.getModelPropertyID());
		setCategory(bean.getModelPropertyTypeName());
		setDisplayName(bean.getModelPropertyName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.l2fprod.common.propertysheet.DefaultProperty#toString()
	 */
	@Override
	public String toString() {
		return bean.getModelPropertyTypeName() + ":"
				+ bean.getModelPropertyName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.l2fprod.common.propertysheet.DefaultProperty#readFromObject(java.lang.Object)
	 */
	@Override
	public void readFromObject(Object arg0) {
		if (arg0 instanceof Element) {
			Element element = getElement((Element) arg0);
			if (element == null)
				return;

			String value = element.getAttribute(bean.getFieldShortName());
			initializeValue(value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.l2fprod.common.propertysheet.DefaultProperty#writeToObject(java.lang.Object)
	 */
	@Override
	public void writeToObject(Object arg0) {
		if (arg0 instanceof Element) {
			Element element = getElement((Element) arg0);
			if (element == null)
				return;

			String value = (String) getValue();
			element.setAttribute(bean.getFieldShortName(), value);
			if (bean.getModelPropertyType().equals(ModelPropertyBean.TYPE_ID)) {
				handle.getEditor().getPropertyModelInteractor().getGraphModel()
						.refreshTreeNode((Element) arg0);
			}
		}
		else if(arg0 instanceof Set)
		{
			Set<Element> elements = (Set<Element>)arg0;
			for(Element element:elements)
			{
				String value = (String) getValue();
				Element el = getElement(element);
				if(el == null)
					continue;
				el.setAttribute(bean.getFieldShortName(), value);
				if (bean.getModelPropertyType().equals(ModelPropertyBean.TYPE_ID)) {
					handle.getEditor().getPropertyModelInteractor().getGraphModel()
							.refreshTreeNode(element);
				}
			}
		}
	}

	public Element getElement(Element element) {
		Element metadata = (Element) element.getElementsByTagName("metadata")
				.item(0);
		if (metadata == null)
			return null;
		Element dest = (Element) metadata
				.getElementsByTagName(
						bean.getIndunormShortName() + ":"
								+ bean.getMetadataShortName()).item(0);
		return dest;
	}

}
