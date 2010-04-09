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
 * ����XML�����Ի���
 * 
 * @author Qil.Wong
 * 
 */
public class LocalExportChooserDialog extends WfSaveDialog {

	private static final long serialVersionUID = -4318727585025494987L;

	String[] bannerSubTitles = new String[] { "�������������ݵ���Ϊ�ض���ʽ��XML(.woml).",
			"��ѡ�е��������ݵ���Ϊ�ض���ʽ��XML(.processml)." };

	

	public LocalExportChooserDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("����Ϊ�����ļ�",
				bannerSubTitles[0], Functions.getImageIcon("export_edit.gif"));
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
