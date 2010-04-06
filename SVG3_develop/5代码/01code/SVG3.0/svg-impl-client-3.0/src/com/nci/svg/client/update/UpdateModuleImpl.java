package com.nci.svg.client.update;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.update.UpdateAdapter;

/**
 * ����ģ��������ڿͻ��˳����ļ���ͼ���ļ�������
 * @author Qil.Wong
 *
 */
public abstract class UpdateModuleImpl extends UpdateAdapter {
	
	public final static String MODULE_ID = "1130f3fb-b34a-4252-8f1d-979962cab1c1";
	
	public UpdateModuleImpl(EditorAdapter editor){
		super(editor);
		moduleUUID = MODULE_ID;
	}

	@Override
	public int init(){		
		return UpdateModuleImpl.MODULE_INITIALIZE_COMPLETE;
	}
	
	
	public String getModuleID() {
		return MODULE_ID;
	}

}
