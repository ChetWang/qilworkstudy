package com.nci.svg.sdk.module;

import java.util.HashMap;

/**
 * @author yx.nci
 * 模块管理器基类
 */
public abstract class ModuleControllerAdapter extends DefaultModuleAdapter{
	
	public ModuleControllerAdapter(){
		super();
	}
	
	public ModuleControllerAdapter(HashMap parameters)
	{
		super(parameters);
	}

}
