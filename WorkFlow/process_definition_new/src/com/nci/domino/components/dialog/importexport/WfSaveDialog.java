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
 * 抽象的本地保存、导入导出基类
 * 
 * @author Qil.Wong
 * 
 */
public abstract class WfSaveDialog extends WfDialog {

	protected SolidFileChooser fileChooser = new SolidFileChooser(this);

	protected WfFileExport currentFilter;
	
	/**
	 * 完整流程格式
	 */
	protected static final String WOML_EXT = ".woml";

	/**
	 * 单个流程格式
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
			return "必须指定文件名";
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
	 * 获取文件输入的TextField
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
	 * 获取当前的过滤器
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
	 * 获取文件选择器对象
	 * @return
	 */
	public SolidFileChooser getFileChooser() {
		return fileChooser;
	}

}
