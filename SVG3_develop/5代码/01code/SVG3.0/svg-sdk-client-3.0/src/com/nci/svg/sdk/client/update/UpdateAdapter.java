package com.nci.svg.sdk.client.update;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.module.DefaultModuleAdapter;
import com.nci.svg.sdk.module.ModuleInitializeFailedException;
import com.nci.svg.sdk.module.ModuleStartFailedException;

/**
 * 客户端升级组件
 * @author Qil.Wong
 *
 */
public abstract class UpdateAdapter extends DefaultModuleAdapter {
	
	protected EditorAdapter editor = null;
	
	public UpdateAdapter(EditorAdapter editor){
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
		return "update";
	}

}
