package com.nci.svg.sdk.layer;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.module.DefaultModuleAdapter;

public abstract class LayerSelectionManager extends DefaultModuleAdapter {
	
	protected EditorAdapter editor;
	
	public LayerSelectionManager(EditorAdapter editor){
		this.editor = editor;
	}

	public abstract void showLayers(
			final HashMap<String, ArrayList<String>> allSelectedLayersMap);
	
	
	public abstract HashMap<String,Element> getLayerElementMap();
	
	public abstract void removeEffectAttrib(Document svgDoc);
	
	public abstract void restore(Document svgDoc);
	
	public String getModuleType(){
		return "layerSelection";
	}

}
