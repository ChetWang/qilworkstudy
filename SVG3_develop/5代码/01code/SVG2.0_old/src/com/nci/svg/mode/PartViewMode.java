package com.nci.svg.mode;

import javax.swing.JToggleButton;

import org.w3c.dom.Document;

import com.nci.svg.module.EquipmentPropertyModule;
import com.nci.svg.module.NCIViewEditModule;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * ���ֲ��������ģʽ����ģʽ�¿��������û�������ͱ༭����ģʽ֮���л���Ĭ�������ģʽ
 * 
 * @author Qil.Wong
 * 
 */
public class PartViewMode extends EditorModeAdapter {

	public PartViewMode(Editor editor) {
		super(editor, SVGTOOL_MODE_VIEW_PARTVIEW);
		createOutlookPane = false;
		showPopupMenu = true;
		createJFileChooser = false;
		showRuler = false;
		showGrid = false;
		showMenubar = false;
		showToolbar = true;
		internalFrameCloseable = false;
		
		disabledProperties.add("createOutlookPane".toLowerCase());
		disabledProperties.add("createJFileChooser".toLowerCase());
		disabledProperties.add("showGrid".toLowerCase());
		disabledProperties.add("showRuler".toLowerCase());
		disabledProperties.add("showMenubar".toLowerCase());
		disabledProperties.add("internalFrameCloseable".toLowerCase());
	}

	@Override
	public Document getModuleDocument() {
		return ResourcesManager.getXMLDocument("modules_partview.xml");
	}

	@Override
	public void initMode() {
		JToggleButton btn = ((NCIViewEditModule) editor
				.getModule(NCIViewEditModule.NCI_View_Edit_ModuleID))
				.getToggleButton();
		btn.doClick();
		btn.setVisible(false);
		((EquipmentPropertyModule) editor
				.getModule(EquipmentPropertyModule.equipmentPropertyID))
				.getPropertyDialog().getToolBarButton().setVisible(false);
	}
}
