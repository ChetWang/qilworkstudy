package com.nci.svg.sdk.module;

/**
 * @author yx.nci
 * 模块停止异常类
 */
public class ModuleStopedException extends Exception {
	private static final long serialVersionUID = -6477577477923899791L;

	public ModuleStopedException(GeneralModuleIF module) {
		super("指定的模块已停止运行！Module ID:" + module.getModuleID() + "; Module Type:"
				+ module.getModuleType() + "; Module class:"
				+ module.getClass().getName());
	}

}
