package com.nci.domino.components.dialog.importexport;

import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;

import com.nci.domino.WfEditor;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.help.Functions;
import com.nci.domino.importexport.WfFileExport;
import com.nci.domino.importexport.WofoImportExport;
import com.nci.domino.importexport.WofoProcessImportExport;

/**
 * ����XML����Ի���
 * 
 * @author Qil.Wong
 * 
 */
public class LocalImportChooserDialog extends WfSaveDialog {

	String[] bannerSubTitles = new String[] { "�������������ݴ��ض���ʽ��XML(.woml)�ļ��е���.",
			"�������������ݴ��ض���ʽ��XML(.processml)�ļ��е���." };

	public LocalImportChooserDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("�򿪻��뱾���ļ�",
				"���������ݴ��ض���ʽ��XML(.woml)�ļ��е���.", Functions
						.getImageIcon("import_edit.gif"));
		return headerPanel1.getGlassBanner();
	}

	@Override
	public WfFileExport[] getFileFilters() {
		WfFileExport[] filters = new WfFileExport[] {
				new WofoProcessImportExport(editor, ".processml",
						"NCI ����Workflow��ʽ�ļ� (*" + PROCESSML_EXT + ")"),
				new WofoImportExport(editor, WOML_EXT, "NCI ����Workflow��ʽ�ļ� (*"
						+ WOML_EXT + ")") };
		return filters;
	}

	protected void filterChanged(PropertyChangeEvent evt) {
		currentFilter = (WfFileExport) evt.getNewValue();
		if (currentFilter.getAcceptExtension().equals(WOML_EXT)) {
			getBannerPanel().setSubtitle(bannerSubTitles[0]);
		} else {
			getBannerPanel().setSubtitle(bannerSubTitles[1]);
		}
	}

}
