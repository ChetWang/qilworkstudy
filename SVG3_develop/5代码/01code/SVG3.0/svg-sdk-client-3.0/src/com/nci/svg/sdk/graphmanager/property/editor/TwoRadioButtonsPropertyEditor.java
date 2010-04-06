/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-2-3
 * @功能：双单选框
 *
 */
package com.nci.svg.sdk.graphmanager.property.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.w3c.dom.Element;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.swing.PercentLayout;
import com.nci.svg.sdk.graphmanager.property.NciGraphProperty;

public class TwoRadioButtonsPropertyEditor extends AbstractPropertyEditor {

	private ButtonGroup buttonGroup = null;
	private JRadioButton radioButton1 = null;
	private JRadioButton radioButton2 = null;
	private JPanel panel = null;
	private int index = -1;
    private DefaultProperty property1 = null;
    private DefaultProperty property2 = null;
    private PropertySheetPanel sheet = null;
	public TwoRadioButtonsPropertyEditor(final PropertySheetPanel sheet,final NciGraphProperty property,final Set<Element> elements,
			String radio1Text,String radio1Value, String radio2Text,String radio2Value,
			final DefaultProperty property1, final DefaultProperty property2) {
		super();
		this.sheet = sheet;
		this.property1 = property1;
		this.property2 = property2;
		if(property != null)
		    property.setPropertyEditor(this);
		editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0)) {
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
				radioButton1.setEnabled(enabled);
				radioButton2.setEnabled(enabled);
			}
		};
		panel = (JPanel) editor;
		buttonGroup = new ButtonGroup();
		radioButton1 = new JRadioButton();
		radioButton1.setText(radio1Text);
		radioButton1.setName(radio1Value);
		radioButton1.setOpaque(false);
		radioButton2 = new JRadioButton();
		radioButton2.setText(radio2Text);
		radioButton2.setName(radio2Value);
		radioButton2.setOpaque(false);
		buttonGroup.add(radioButton1);
		buttonGroup.add(radioButton2);
		panel.add(radioButton1);
		panel.add(radioButton2);
		radioButton1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				index = 0;
				if (property1 != null && property2 != null) {
					property1.setEditable(true);
					property2.setEditable(false);
					sheet.updateUI();
				}
				if(property != null)
				{
					property.writeToObject(elements);
				}
			}

		});

		radioButton2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				index = 1;
				if (property1 != null && property2 != null) {
					property1.setEditable(false);
					property2.setEditable(true);
					sheet.updateUI();
				}
				if(property != null)
				{
					property.writeToObject(elements);
				}
			}

		});
		panel.setOpaque(false);
	}

	/* (non-Javadoc)
	 * @see com.l2fprod.common.beans.editor.AbstractPropertyEditor#getValue()
	 */
	@Override
	public Object getValue() {
		if(index == 0)
		{
			return radioButton1.getName();
		}
		return radioButton2.getName();
	}

	/* (non-Javadoc)
	 * @see com.l2fprod.common.beans.editor.AbstractPropertyEditor#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		if(radioButton1.getName().equals((String)value))
		{
			radioButton1.setSelected(true);
			radioButton2.setSelected(false);
			if (property1 != null && property2 != null) {
				property1.setEditable(true);
				property2.setEditable(false);
				sheet.updateUI();
			}
		}
		else if(radioButton2.getName().equals((String)value))
		{
			radioButton2.setSelected(true);
			radioButton1.setSelected(false);
			if (property1 != null && property2 != null) {
				property1.setEditable(false);
				property2.setEditable(true);
				sheet.updateUI();
			}
		}
		
		
	}

}
