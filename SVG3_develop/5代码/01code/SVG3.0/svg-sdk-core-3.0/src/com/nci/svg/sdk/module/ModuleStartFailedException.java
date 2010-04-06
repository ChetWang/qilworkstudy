package com.nci.svg.sdk.module;

/**
 * @author yx.nci
 * 模块启动异常类
 */
public class ModuleStartFailedException extends Exception {
	private static final long serialVersionUID = -5498595205303416470L;

	public ModuleStartFailedException(GeneralModuleIF module) {
		super("开启指定的模块失败！Module ID:" + module.getModuleID()
				+"; Module Type:"+module.getModuleType()+ "; Module class:" + module.getClass().getName());
	}

}
