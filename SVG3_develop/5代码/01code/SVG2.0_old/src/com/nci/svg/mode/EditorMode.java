package com.nci.svg.mode;

import org.w3c.dom.Document;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class EditorMode extends EditorModeAdapter {

	public EditorMode(Editor editor) {
		super(editor, SVGTOOL_MODE_EDIT);
		createOutlookPane = true;
		showPopupMenu = true;
		createJFileChooser = true;
		showRuler = true;
		showGrid = true;
		showMenubar = true;
		showToolbar = true;
		internalFrameCloseable = true;
	}

	@Override
	public Document getModuleDocument() {
		return ResourcesManager.getXMLDocument("modules.xml");
	}

	@Override
	public void initMode() {

	}

}
