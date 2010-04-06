package com.nci.svg.sdk.module;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;

/**
 * 截屏模块适配器
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
	 * 显示提示显示
	 */
	public abstract void showPromptInfo();
	
	

}
