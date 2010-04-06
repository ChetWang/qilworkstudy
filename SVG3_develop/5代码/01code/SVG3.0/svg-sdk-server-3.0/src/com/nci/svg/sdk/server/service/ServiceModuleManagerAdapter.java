package com.nci.svg.sdk.server.service;

import java.util.HashMap;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ���������ҵ�����������������
 *
 */
public abstract class ServiceModuleManagerAdapter extends ServerModuleAdapter {
	/**
	 * ���ӣ���ע��
	 */
	public static final String OPER_ADD = "add";
	/**
	 * �޸�
	 */
	public static final String OPER_MODIFY = "modify";
	/**
	 * ɾ��
	 */
	public static final String OPER_DELETE = "delete";
	/**
	 * �����캯�������ȹ�����ͨ�õķ�������
	 */
	public ServiceModuleManagerAdapter(HashMap parameters)
	{
		super(parameters);
	}

	public String getModuleType() {
		return ModuleDefines.SERVICE_MODULE_MANAGER;
	}
	
	/**
	 * ���ù������
	 * @param operType:��������
	 * @param shortName���������
	 * @return�����ý�����ɹ�����OPER_SUCCESS,ʧ�ܷ���OPER_ERROR
	 */
	public abstract ResultBean configModule(String operType,String shortName);
	
	

}
