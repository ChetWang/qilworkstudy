package com.nci.svg.sdk.module;

/**
 * @author yx.nci
 * ģ���ʼ���쳣��
 */
public class ModuleInitializeFailedException extends Exception {
	private static final long serialVersionUID = -4707369904469944646L;

	public ModuleInitializeFailedException(GeneralModuleIF module) {
		super("��ʼ��ָ����ģ��ʧ�ܣ�Module ID:" + module.getModuleID()
				+"; Module Type:"+module.getModuleType()+ "; Module class:" + module.getClass().getName());
	}

}
