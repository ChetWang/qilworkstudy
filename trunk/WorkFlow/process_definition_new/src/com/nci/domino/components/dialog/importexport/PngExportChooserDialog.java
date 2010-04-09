package com.nci.domino.components.dialog.importexport;

import javax.swing.JComponent;

import com.nci.domino.WfEditor;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.help.Functions;
import com.nci.domino.importexport.PngExport;
import com.nci.domino.importexport.WfFileExport;

/**
 * png图形导出
 * 
 * @author Qil.Wong
 * 
 */
public class PngExportChooserDialog extends WfSaveDialog {

	private static final long serialVersionUID = -4318727585025494987L;

	public PngExportChooserDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("提取",
				"将流程图形以PNG格式提取到本地.", Functions.getImageIcon("export_edit.gif"));
		return headerPanel1.getGlassBanner();
	}

	@Override
	public WfFileExport[] getFileFilters() {
		WfFileExport[] filters = new WfFileExport[] { new PngExport(editor,
				".png", "PNG格式文件 (*.png)") };
		return filters;
	}

}
