package com.nci.svg.sdk.server.operationinterface;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-11-24
 * @���ܣ���������ҵ��ӿڹ������������
 *
 */
public abstract class OperInterfaceManagerAdapter extends ServerModuleAdapter {

	public OperInterfaceManagerAdapter(HashMap parameters)
	{
		super(parameters);
	}
	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#getModuleID()
	 */
	public String getModuleID() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#getModuleType()
	 */
	public String getModuleType() {
		// TODO Auto-generated method stub
		return ModuleDefines.OPER_INTERFACE_MANAGER;
	}
	
	/**
	 * ���ݴ����ҵ��ӿ����ƻ�ȡҵ��ӿڶ���
	 * @param bussinessID��ҵ��ϵͳ���
	 * @param actionName:ҵ��ӿ�����
	 * @return��ҵ��ӿ��������
	 */
	public abstract OperInterfaceAdapter getOperationModule(String bussinessID, String actionName);

	/**
	 * ҵ��ӿ���ת
	 * @param bussinessID:ϵͳ���
	 * @param action:������
	 * @param requestParams�����������
	 * @return�����ؽ����
	 */
	public abstract ResultBean OperInterfaceMultiplexing(String bussinessID,String action,Map requestParams);
}
