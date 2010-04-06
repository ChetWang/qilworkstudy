/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-21
 * @功能：TODO
 *
 */
package com.nci.svg.sdk.graphmanager.property;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Element;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.ModelPropertyBean;
import com.nci.svg.sdk.bean.ModelRelaIndunormBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.DataManageAdapter;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * @author yx.nci
 * 
 */
public class NciBussComboProperty extends NciBussProperty {

	private HashMap<String, CodeInfoBean> mapCode = null;
	private ComboBoxPropertyEditor propertyEditor = null;

	public NciBussComboProperty(EditorAdapter editor,
			ModelRelaIndunormBean bean, SVGHandle handle,
			ComboBoxPropertyEditor propertyEditor) {
		super(editor, bean, handle);
		this.propertyEditor = propertyEditor;
		setType(NciBussComboProperty.class);
		// modify by yux,2009.2.19
		// 增加过滤条件的判定
		String code = bean.getModelPropertyCode();
		String[] strTmp = code.split(":");
		if (strTmp.length == 3
				|| (strTmp.length > 3 && (strTmp[3] == null || strTmp[3]
						.length() == 0))) {
			// 无过滤条件
			ResultBean result = editor.getDataManage().getData(
					DataManageAdapter.KIND_CODES, strTmp[1], null);
			if (result.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
				mapCode = (HashMap<String, CodeInfoBean>) result.getReturnObj();
				String[] values = new String[mapCode.size()];
				Iterator<CodeInfoBean> iterator = mapCode.values().iterator();
				int index = 0;
				while (iterator.hasNext()) {
					CodeInfoBean codeBean = iterator.next();
					values[index] = codeBean.getName();
					index++;
				}
				propertyEditor.setAvailableValues(values);

			}
			if (strTmp[2] != null && strTmp[2].equals("1")) {
				// 可编辑
				this.setEditable(true);
			} else
				this.setEditable(false);
		} else {
			// 带过滤条件，需要去服务器获取
			String[][] params = new String[2][2];
			params[0][0] = ActionParams.MODULE_NAME;
			params[0][1] = strTmp[1];
			params[1][0] = ActionParams.EXPRSTRING;
			params[1][1] = strTmp[3];
			ResultBean resultBean = editor.getCommunicator().communicate(
					new CommunicationBean(ActionNames.GET_SVG_CODES, params));
			if (resultBean.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
				mapCode = (HashMap<String, CodeInfoBean>) resultBean
						.getReturnObj();
				String[] values = new String[mapCode.size()];
				Iterator<CodeInfoBean> iterator = mapCode.values().iterator();
				int index = 0;
				while (iterator.hasNext()) {
					CodeInfoBean codeBean = iterator.next();
					values[index] = codeBean.getName();
					index++;
				}
				propertyEditor.setAvailableValues(values);

			}
			if (strTmp[2] != null && strTmp.equals("1")) {
				// 可编辑
				this.setEditable(true);
			} else
				this.setEditable(false);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.client.graphmanager.property.NciBussProperty#readFromObject(java.lang.Object)
	 */
	@Override
	public void readFromObject(Object arg0) {
		if (arg0 instanceof Element) {
			Element element = getElement((Element) arg0);
			if (element == null)
				return;

			String value = element.getAttribute(bean.getFieldShortName());
			String name = null;
			Iterator<CodeInfoBean> iterator = mapCode.values().iterator();
			while (iterator.hasNext()) {
				CodeInfoBean codeBean = iterator.next();
				if (codeBean.getValue().equals(value)) {
					name = codeBean.getName();
				}
			}
			initializeValue(name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.client.graphmanager.property.NciBussProperty#writeToObject(java.lang.Object)
	 */
	@Override
	public void writeToObject(Object arg0) {
		if (arg0 instanceof Element) {
			Element element = getElement((Element) arg0);
			if (element == null)
				return;

			String name = (String) getValue();
			String value = null;
			Iterator<CodeInfoBean> iterator = mapCode.values().iterator();
			while (iterator.hasNext()) {
				CodeInfoBean codeBean = iterator.next();
				if (codeBean.getName().equals(name)) {
					value = codeBean.getValue();
				}
			}
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
				String name = (String) getValue();
				String value = null;
				Iterator<CodeInfoBean> iterator = mapCode.values().iterator();
				while (iterator.hasNext()) {
					CodeInfoBean codeBean = iterator.next();
					if (codeBean.getName().equals(name)) {
						value = codeBean.getValue();
					}
				}
				element.setAttribute(bean.getFieldShortName(), value);
				if (bean.getModelPropertyType().equals(ModelPropertyBean.TYPE_ID)) {
					handle.getEditor().getPropertyModelInteractor().getGraphModel()
							.refreshTreeNode(element);
				}
			}
		}
	}

}
