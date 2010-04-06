package com.nci.svg.sdk.module;

/**
 * @author yx.nci
 * ģ��ֹͣ�쳣��
 */
public class ModuleStopedException extends Exception {
	private static final long serialVersionUID = -6477577477923899791L;

	public ModuleStopedException(GeneralModuleIF module) {
		super("ָ����ģ����ֹͣ���У�Module ID:" + module.getModuleID() + "; Module Type:"
				+ module.getModuleType() + "; Module class:"
				+ module.getClass().getName());
	}

}
