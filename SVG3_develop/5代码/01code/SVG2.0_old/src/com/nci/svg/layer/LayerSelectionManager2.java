package com.nci.svg.layer;

import java.util.ArrayList;
import java.util.HashMap;

import fr.itris.glips.svgeditor.Editor;

public class LayerSelectionManager2 {

	public final static String VISIBILITY = "visibility";
	public final static String VISIBLE = "visible";
	public final static String HIDDEN = "hidden";
	public final static String LAYER_EFFECT = "layerEffect";
	
	private Editor editor;

	public LayerSelectionManager2(Editor editor) {
		this.editor = editor;
	}

	public void showLayers(
			final HashMap<String, ArrayList<String>> allSelectedLayersMap,
			final boolean keepDescription) {

	}

}
