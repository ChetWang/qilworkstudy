/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-2-4
 * @功能：公司用代码
 *
 */
package com.nci.svg.sdk.graphmanager.property.editor;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.nci.svg.sdk.bean.SimpleCodeBean;

/**
 * @author yx.nci
 * 
 */
public class NciComboBoxPropertyEditor extends ComboBoxPropertyEditor {
	public NciComboBoxPropertyEditor() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.l2fprod.common.beans.editor.ComboBoxPropertyEditor#getValue()
	 */
	@Override
	public Object getValue() {
		Object selected = ((JComboBox) editor).getSelectedItem();
		if (selected instanceof SimpleCodeBean) {
			return (SimpleCodeBean) selected;
		} else {
			return (String) selected;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.l2fprod.common.beans.editor.ComboBoxPropertyEditor#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object arg0) {
		JComboBox combo = (JComboBox) editor;
		String current = null;
		int index = -1;
		String value = null;
		if(arg0 instanceof String)
			value = (String) arg0;
		else if(arg0 instanceof SimpleCodeBean)
			value = ((SimpleCodeBean)arg0).getCode();
		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
			Object obj = combo.getModel().getElementAt(i);
			if(obj instanceof String)
			    current = (String)obj ;
			else if(obj instanceof SimpleCodeBean)
				current = ((SimpleCodeBean)obj).getCode();
			if (current != null && current.equals(value)) {
				index = i;
				break;
			}
		}
		((JComboBox) editor).setSelectedIndex(index);

	}
}
