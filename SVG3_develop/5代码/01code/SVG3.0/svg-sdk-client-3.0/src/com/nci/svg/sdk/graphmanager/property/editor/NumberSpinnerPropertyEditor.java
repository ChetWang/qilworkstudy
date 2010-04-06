/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-2-3
 * @功能：数字带上下切换
 *
 */
package com.nci.svg.sdk.graphmanager.property.editor;

import java.util.Set;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.w3c.dom.Element;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.nci.svg.sdk.graphmanager.property.NciGraphProperty;

/**
 * @author yx.nci
 * 
 */
public class NumberSpinnerPropertyEditor extends AbstractPropertyEditor {

	public NumberSpinnerPropertyEditor(boolean bPositive,
			final NciGraphProperty property, final Set<Element> elements) {
		super();
		property.setPropertyEditor(this);
		// 构建符合要求的model
		SpinnerNumberModel spinnerModel = null;

		double val = 0;

		if (bPositive) {

			spinnerModel = new SpinnerNumberModel(val, 0, 10000000000000D, 1);

		} else {

			spinnerModel = new SpinnerNumberModel(val, -10000000000000D,
					10000000000000D, 1);
		}
		editor = new JSpinner(spinnerModel);
		final ChangeListener changeListener = new ChangeListener() {

			public void stateChanged(ChangeEvent evt) {
				
				if (property != null) {
					property.writeToObject(elements);
				}
			}
		};
//		((JSpinner) editor).addChangeListener(changeListener);
		((JSpinner) editor).setOpaque(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.l2fprod.common.beans.editor.AbstractPropertyEditor#getValue()
	 */
	@Override
	public Object getValue() {

		JSpinner spinner = (JSpinner) editor;
		Double value = (Double) spinner.getValue();
		return value.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.l2fprod.common.beans.editor.AbstractPropertyEditor#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		JSpinner spinner = (JSpinner) editor;

		spinner.setValue(Double.valueOf((String) value));
	}

}
