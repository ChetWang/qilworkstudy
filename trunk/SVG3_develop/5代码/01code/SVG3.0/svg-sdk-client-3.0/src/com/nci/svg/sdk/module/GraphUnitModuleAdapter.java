package com.nci.svg.sdk.module;

import javax.swing.tree.TreeModel;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;

import fr.itris.glips.library.monitor.Monitor;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;

public abstract class GraphUnitModuleAdapter extends ModuleAdapter {

	public static final String idGraphUnitManage = "nci_menu_graphunit_manage";
	
	public static final String MODULE_ID = "d866e001-69c8-4713-a484-be2d821cf524";

	public GraphUnitModuleAdapter(EditorAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
	}

	public abstract boolean saveSymbol(final SVGHandle handle, Monitor monitor);
	
	public abstract TreeModel getSymbolModelTree(String moduleID);
	
	public String getModuleID(){
		return MODULE_ID;
	}
}
