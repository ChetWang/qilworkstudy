package com.nci.domino.components.dialog.importexport;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.nci.domino.components.dialog.WfDialog;

/**
 * 自定义的FileChooser，可以更改某些组件
 * 
 * @author Qil.Wong
 * 
 */
public class SolidFileChooser extends JFileChooser {

	WfDialog chooserParent;

	public SolidFileChooser(WfDialog parent) {
		this.chooserParent = parent;
	}

	@Override
	public void approveSelection() {
		super.approveSelection();
		chooserParent.setDialogResult(LocalExportChooserDialog.RESULT_AFFIRMED);
		chooserParent.setVisible(false);

	}

	/**
	 * 获取输入地址的TextField
	 * 
	 * @return
	 */
	public JTextField getInputField() {
		return getTextField(this);
	}

	public JComboBox getFilterSelector() {
		return getComboBox(this);
	}

	private JTextField getTextField(Container c) {
		JTextField textField = null;
		for (int i = 0; i < c.getComponentCount(); i++) {
			Component cnt = c.getComponent(i);
			if (cnt instanceof JTextField) {
				return (JTextField) cnt;
			}
			if (cnt instanceof Container) {

				textField = getTextField((Container) cnt);

				if (textField != null) {

					return textField;
				}
			}
		}
		return null;
	}

	private JComboBox getComboBox(Container c) {
		JComboBox textField = null;
		for (int i = 0; i < c.getComponentCount(); i++) {
			Component cnt = c.getComponent(i);
			if (cnt instanceof JComboBox) {
				if (((JComboBox) cnt).getModel().getElementAt(0) instanceof FileFilter) {
					return (JComboBox) cnt;
				}

			}
			if (cnt instanceof Container) {

				textField = getComboBox((Container) cnt);

				if (textField != null) {

					if (textField.getModel().getElementAt(0) instanceof FileFilter) {
						return (JComboBox) textField;
					}
				}
			}
		}
		return null;
	}

}