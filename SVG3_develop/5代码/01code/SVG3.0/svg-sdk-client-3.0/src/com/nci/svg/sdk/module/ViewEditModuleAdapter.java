package com.nci.svg.sdk.module;

import java.awt.Component;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;

public abstract class ViewEditModuleAdapter extends ModuleAdapter {

	public static final String NCI_View_Edit_ModuleID = "view_edit";

	public static final String MODULE_ID = "bf0e1c82-4a7e-4167-a39a-3bf31a2cac82";
	
	

	public static final String VIEW_MODE = "view";

	public static final String EDIT_MODE = "edit";

	public static final String BOTH_VIEW_EDIT_MODE = "view-edit";
	
	protected String ve_mode = EDIT_MODE;

	public ViewEditModuleAdapter(EditorAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
	}

	/**
	 * 处理菜单中的分隔符
	 * 
	 * @param menuComps
	 *            指定菜单中的所有子组件集合
	 */
	public abstract void handleSeparators(Component[] menuComps);
	
	/**
	 * 获取平台模式
	 * @return 平台模式,SVG平台默认提供三种模式，但用户也同时扩展其他模式
	 * @see ViewEditModuleAdapter#VIEW_MODE
	 * @see ViewEditModuleAdapter#EDIT_MODE
	 * @see ViewEditModuleAdapter#BOTH_VIEW_EDIT_MODE
	 */
	public String getViewEdit_mode() {
		return ve_mode;
	}

}
