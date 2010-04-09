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
 * 本地XML导出对话框
 * 
 * @author Qil.Wong
 * 
 */
public class LocalExportChooserDialog extends WfSaveDialog {

	private static final long serialVersionUID = -4318727585025494987L;

	String[] bannerSubTitles = new String[] { "将所有流程数据导出为特定格式的XML(.woml).",
			"将选中的流程数据导出为特定格式的XML(.processml)." };

	

	public LocalExportChooserDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("保存为本地文件",
				bannerSubTitles[0], Functions.getImageIcon("export_edit.gif"));
		return headerPanel1.getGlassBanner();
	}

	@Override
	public WfFileExport[] getFileFilters() {
		WfFileExport[] filters = new WfFileExport[] {
				new WofoProcessImportExport(editor, ".processml",
						"NCI 单个Workflow格式文件 (*" + PROCESSML_EXT + ")"),
				new WofoImportExport(editor, WOML_EXT, "NCI 完整Workflow格式文件 (*"
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
