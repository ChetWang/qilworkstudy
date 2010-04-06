package com.nci.svg.client.mode;

import javax.swing.JToggleButton;

import org.w3c.dom.Document;

import com.nci.svg.client.module.NCIViewEditModule;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.mode.EditorModeAdapter;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * ���ֲ��������ģʽ����ģʽ�¿��������û�������ͱ༭����ģʽ֮���л���Ĭ�������ģʽ
 * 
 * @author Qil.Wong
 * 
 */
public class PartViewMode extends EditorModeAdapter {

	public PartViewMode(EditorAdapter editor) {
		super(editor, SVGTOOL_MODE_VIEW_PARTVIEW);
		createOutlookPane = false;
		showPopupMenu = true;
		createJFileChooser = false;
		showRuler = false;
		showGrid = false;
		showMenubar = false;
		showToolbar = true;
		internalFrameCloseable = false;
		createPropertyPane = false;
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
//		((EquipmentPropertyModule) editor
//				.getModule(EquipmentPropertyModule.equipmentPropertyID))
//				.getPropertyDialog().getToolBarButton().setVisible(false);
	}
}
