package com.nci.svg.client.mode;

import org.w3c.dom.Document;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.mode.EditorModeAdapter;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class OnlyViewMode extends EditorModeAdapter {

	public OnlyViewMode(EditorAdapter editor) {
		super(editor, SVGTOOL_MODE_VIEW_ONLYVIEW);
		createOutlookPane = false;
		showPopupMenu = false;
		createJFileChooser = false;
		showGrid = false;
		showRuler = false;
		showMenubar = false;
		showToolbar = false;
		internalFrameCloseable = false;
		createPropertyPane = false;
		disabledProperties.add("createOutlookPane".toLowerCase());
		disabledProperties.add("showPopupMenu".toLowerCase());
		disabledProperties.add("createJFileChooser".toLowerCase());
		disabledProperties.add("showGrid".toLowerCase());
		disabledProperties.add("showRuler".toLowerCase());
		disabledProperties.add("showMenubar".toLowerCase());
		disabledProperties.add("showToolbar".toLowerCase());
		disabledProperties.add("internalFrameCloseable".toLowerCase());
	}

	@Override
	public void initMode() {
		editor.getMenuBar().setVisible(false);
		editor.getStatusBar().setVisible(false);
		editor.getToolBarManager().getToolsBar().setVisible(false);
	}

	@Override
	public Document getModuleDocument() {
		return ResourcesManager.getXMLDocument("modules_onlyview.xml");
	}

}
