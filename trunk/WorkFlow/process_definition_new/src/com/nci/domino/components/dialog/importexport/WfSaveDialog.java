package com.nci.domino.components.dialog.importexport;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import com.nci.domino.GlobalConstants;
import com.nci.domino.WfEditor;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.importexport.WfFileExport;

/**
 * ����ı��ر��桢���뵼������
 * 
 * @author Qil.Wong
 * 
 */
public abstract class WfSaveDialog extends WfDialog {

	protected SolidFileChooser fileChooser = new SolidFileChooser(this);

	protected WfFileExport currentFilter;
	
	/**
	 * �������̸�ʽ
	 */
	protected static final String WOML_EXT = ".woml";

	/**
	 * �������̸�ʽ
	 */
	protected static final String PROCESSML_EXT = ".processml";

	static {
		File f = new File(GlobalConstants.tempFile);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}
	}

	public WfSaveDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
		defaultWidth = 650;
		defaultHeight = 500;
	}

	@Override
	protected String checkInput() {
		JTextField textField = getTextField(fileChooser);
		if (textField.getText().trim().equals("")) {
			return "����ָ���ļ���";
		}
		return super.checkInput();
	}

	@Override
	public JComponent createContentPanel() {
		WfFileExport[] filters = getFileFilters();
		for (WfFileExport e : filters) {
			fileChooser.addChoosableFileFilter(e);
		}
		try {
			fileChooser.setCurrentDirectory(new File(GlobalConstants.tempFile)
					.getParentFile());
		} catch (Exception e) {

		}

		currentFilter = (WfFileExport) fileChooser.getChoosableFileFilters()[fileChooser
				.getChoosableFileFilters().length - 1];
		fileChooser.removeChoosableFileFilter(fileChooser
				.getAcceptAllFileFilter());
		fileChooser.setControlButtonsAreShown(false);
		fileChooser.addPropertyChangeListener(
				JFileChooser.FILE_FILTER_CHANGED_PROPERTY,
				new PropertyChangeListener() {

					public void propertyChange(PropertyChangeEvent evt) {

						filterChanged(evt);
					}
				});
		return fileChooser;
	}

	public abstract WfFileExport[] getFileFilters();

	protected void filterChanged(PropertyChangeEvent evt) {
		currentFilter = (WfFileExport) evt.getNewValue();
	}

	/**
	 * ��ȡ�ļ������TextField
	 * 
	 * @param c
	 * @return
	 */
	protected JTextField getTextField(Container c) {
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

	@Override
	protected void clearContents() {
		fileChooser.setSelectedFile(null);
		fileChooser.getInputMap().clear();
	}

	@Override
	public Serializable getInputValues() {
		final JTextField textField = getTextField(fileChooser);
		String name = textField.getText().trim();
		String ext = currentFilter.getAcceptExtension();
		if (!name.endsWith(ext)) {
			name = name + ext;
		}
		return fileChooser.getCurrentDirectory().toString() + "/" + name;
	}

	/**
	 * ��ȡ��ǰ�Ĺ�����
	 * @return
	 */
	public WfFileExport getCurrentFilter() {
		return currentFilter;
	}

	@Override
	public void setInputValues(Serializable value) {
		//DO NOTHING
	}

	/**
	 * ��ȡ�ļ�ѡ��������
	 * @return
	 */
	public SolidFileChooser getFileChooser() {
		return fileChooser;
	}

}
