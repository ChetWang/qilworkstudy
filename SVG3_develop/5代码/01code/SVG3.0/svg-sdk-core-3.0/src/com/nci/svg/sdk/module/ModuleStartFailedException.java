package com.nci.svg.sdk.module;

/**
 * @author yx.nci
 * ģ�������쳣��
 */
public class ModuleStartFailedException extends Exception {
	private static final long serialVersionUID = -5498595205303416470L;

	public ModuleStartFailedException(GeneralModuleIF module) {
		super("����ָ����ģ��ʧ�ܣ�Module ID:" + module.getModuleID()
				+"; Module Type:"+module.getModuleType()+ "; Module class:" + module.getClass().getName());
	}

}
