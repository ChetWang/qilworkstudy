package com.nci.svg.sdk.module;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;

/**
 * ����ģ��������
 * @author Qil.Wong
 *
 */
public abstract class ScreenCastModuleAdapter extends ModuleAdapter {
	
	public final static String MODULE_ID = "0b3064e3-ed36-43e6-b941-92cf300dd205";
	
	public final static String ScreenCastModuleID = "nciScreenCast";

	public ScreenCastModuleAdapter(EditorAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
	}
	
	/**
	 * ��ʾ��ʾ��ʾ
	 */
	public abstract void showPromptInfo();
	
	

}
