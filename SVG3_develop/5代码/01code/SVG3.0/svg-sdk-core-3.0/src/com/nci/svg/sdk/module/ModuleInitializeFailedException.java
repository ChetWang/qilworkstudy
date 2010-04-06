package com.nci.svg.sdk.module;

/**
 * @author yx.nci
 * 模块初始化异常类
 */
public class ModuleInitializeFailedException extends Exception {
	private static final long serialVersionUID = -4707369904469944646L;

	public ModuleInitializeFailedException(GeneralModuleIF module) {
		super("初始化指定的模块失败！Module ID:" + module.getModuleID()
				+"; Module Type:"+module.getModuleType()+ "; Module class:" + module.getClass().getName());
	}

}
