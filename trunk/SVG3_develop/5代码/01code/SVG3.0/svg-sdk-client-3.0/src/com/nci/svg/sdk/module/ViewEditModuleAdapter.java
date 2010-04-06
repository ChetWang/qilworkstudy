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
	 * ����˵��еķָ���
	 * 
	 * @param menuComps
	 *            ָ���˵��е��������������
	 */
	public abstract void handleSeparators(Component[] menuComps);
	
	/**
	 * ��ȡƽ̨ģʽ
	 * @return ƽ̨ģʽ,SVGƽ̨Ĭ���ṩ����ģʽ�����û�Ҳͬʱ��չ����ģʽ
	 * @see ViewEditModuleAdapter#VIEW_MODE
	 * @see ViewEditModuleAdapter#EDIT_MODE
	 * @see ViewEditModuleAdapter#BOTH_VIEW_EDIT_MODE
	 */
	public String getViewEdit_mode() {
		return ve_mode;
	}

}
