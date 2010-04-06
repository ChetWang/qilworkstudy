package com.nci.svg.sdk.graphmanager.property.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.w3c.dom.Element;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.PercentLayout;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.communication.ActionNames;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2009-2-19
 * @功能：
 * 
 */
public class NciCustomPropertyEditor extends AbstractPropertyEditor {
	/**
	 * add by yux,2009-2-4
	 * 
	 */
	private EditorAdapter nciEditor = null;

	/**
	 * add by yux,2009-2-19 输入框
	 */
	private JTextField textField = null;

	/**
	 * add by yux,2009-2-19 按钮
	 */
	private JButton button = null;

	private String className = null;
	private Set<Element> elements = new HashSet<Element>();

	public NciCustomPropertyEditor(final EditorAdapter nciEditor,
			final Set<Element> elements, String className, String name) {
		super();
		this.nciEditor = nciEditor;
		this.className = className;
		this.elements.addAll(elements);
		editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0)) {
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
			}

		};
		textField = new JTextField();

		((JPanel) editor).add("*", textField);
		button = new JButton();
		button.setName(UUID.randomUUID().toString());
		button.setText(name);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAction();
			}

		});
		((JPanel) editor).add(button);
	}

	private void doAction() {
		ModuleAdapter module = null;
		try {
			module = (ModuleAdapter) startModule();
		} catch (Exception ex) {
			module = null;
		}
		if (module == null)
			return;

		ResultBean bean = module.handleOper(
				ActionNames.GET_BUSINESS_PROPERTYVALUE, null);
		if (bean.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
			String value = (String) bean.getReturnObj();
			textField.setText(value);
		}
	}

	public synchronized Object startModule() {
		Object obj = null;
		try {
			Class[] classargs = { EditorAdapter.class };
			Object[] args = { nciEditor };

			Class cl = Thread.currentThread().getContextClassLoader()
					.loadClass(className);
			Constructor constructor = cl.getConstructor(classargs);
			obj = constructor.newInstance(args);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return obj;
	}
}
