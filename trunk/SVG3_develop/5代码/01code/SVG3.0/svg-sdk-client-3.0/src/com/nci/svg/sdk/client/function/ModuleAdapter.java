package com.nci.svg.sdk.client.function;

import java.util.Collection;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.module.DefaultModuleAdapter;
import com.nci.svg.sdk.module.ModuleInitializeFailedException;
import com.nci.svg.sdk.module.ModuleStartFailedException;

import fr.itris.glips.svgeditor.actions.popup.PopupItem;

/**
 * @author yx.nci
 * 组件抽象基类
 */
public abstract class ModuleAdapter extends DefaultModuleAdapter {

	protected EditorAdapter editor;
	

	public ModuleAdapter(EditorAdapter editor) {
		super();
		this.editor = editor;
		int result = init();
		if (result != MODULE_INITIALIZE_COMPLETE) {
			new ModuleInitializeFailedException(this).printStackTrace();
		}
		result = start();
		if (result != MODULE_START_COMPLETE) {
			new ModuleStartFailedException(this).printStackTrace();
		}
	}
	
	public String getModuleType() {
		return "function";
	}


	/**
	 * @return a map associating a menu item id to its menu item object
	 */
	public HashMap<String, JMenuItem> getMenuItems() {

		return null;
	}

	/**
	 * Returns the list of the popup items
	 * 
	 * @return the list of the popup items
	 */
	public Collection<PopupItem> getPopupItems() {

		return null;
	}

	/**
	 * @return a map associating a tool item id to its tool item object
	 */
	public HashMap<String, AbstractButton> getToolItems() {

		return null;
	}

	public EditorAdapter getEditor() {
		return editor;
	}

	/**
	 * initializes the module
	 */
	public void initialize() {
	}
}