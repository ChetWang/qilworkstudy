package com.nci.svg.client.mode;

import org.w3c.dom.Document;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.mode.EditorModeAdapter;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class EditorMode extends EditorModeAdapter {

	public EditorMode(EditorAdapter editor) {
		super(editor, SVGTOOL_MODE_EDIT);
		createOutlookPane = true;
		showPopupMenu = true;
		createJFileChooser = true;
		showRuler = true;
		showGrid = true;
		showMenubar = true;
		showToolbar = true;
		internalFrameCloseable = true;
		createPropertyPane = true;
	}

	@Override
	public Document getModuleDocument() {
		return ResourcesManager.getXMLDocument("modules.xml");
	}

	@Override
	public void initMode() {

	}

}
