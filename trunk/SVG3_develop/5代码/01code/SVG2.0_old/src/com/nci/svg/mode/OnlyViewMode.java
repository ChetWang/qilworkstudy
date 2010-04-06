package com.nci.svg.mode;

import org.w3c.dom.Document;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class OnlyViewMode extends EditorModeAdapter {

	public OnlyViewMode(Editor editor) {
		super(editor, SVGTOOL_MODE_VIEW_ONLYVIEW);
		createOutlookPane = false;
		showPopupMenu = false;
		createJFileChooser = false;
		showGrid = false;
		showRuler = false;
		showMenubar = false;
		showToolbar = false;
		internalFrameCloseable = false;
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
