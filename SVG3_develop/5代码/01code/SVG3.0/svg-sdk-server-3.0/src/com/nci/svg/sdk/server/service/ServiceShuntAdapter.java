
package com.nci.svg.sdk.server.service;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ���������ҵ�����·�����������
 *
 */
public abstract class ServiceShuntAdapter extends ServerModuleAdapter {
	
	/**
	 * ģ������
	 */
	public static final String SERVICE_MODULE= "ServiceModule";
	/**
	 * ����������
	 */
	public static final String SERVICE_ACTION = "ServiceAction";
	
	/**
	 * ���з���,���ڻ�ȡ����״̬�����������ֹͣ����ʱ��Ч
	 */
	public static final String SERVICE_ALL = "ServiceAll";
	
	/**
	 * ���ӣ���ע��
	 */
	public static final String HANDLE_ADD = "add";
	/**
	 * �޸�
	 */
	public static final String HANDLE_MODIFY = "modify";
	/**
	 * ɾ��
	 */
	public static final String HANDLE_DELETE = "delete";
	/**
	 * ��������
	 */
	public static final String HANDLE_START = "start";
	/**
	 * ֹͣ����
	 */
	public static final String HANDLE_STOP = "stop";
	/**
	 * ����·�ɣ������ʶ��request������
	 * @param request�������
	 * @return�����ؽ����
	 */
	public abstract ResultBean shuntService(Object request);
	
	/**
	 * ����·�ɣ����������֮�����
	 * @param action:������
	 * @param requestParams�����������
	 * @return�����ؽ����
	 */
	public abstract ResultBean serviceMultiplexing(String action,Map requestParams);

	public String getModuleType() {
		// TODO Auto-generated method stub
		return ModuleDefines.SERVICE_SHUNT_MANAGER;
	}
	public ServiceShuntAdapter(HashMap parameters)
	{
		super(parameters);
	}
	
	/**
	 * ���ݴ���Ĳ�������ȡ����action״̬
	 * @param serviceType���������ͣ�SERVICE_MODULE��SERVICE_ACTION��SERVICE_ALL
	 * @param name����Ӧ������
	 * @return����ѯ�����
	 */
	public abstract ResultBean getServiceStatus(String serviceType,String name);
	
	/**
	 * ���ݴ���Ĳ������Է������ά��
	 * @param handleType������ʽ
	 * HANDLE_ADD��HANDLE_MODIFY��HANDLE_DELETE��HANDLE_START��HANDLE_STOP
	 * @param serviceType���������ͣ�SERVICE_MODULE��SERVICE_ACTION��SERVICE_ALL
	 * @param name����Ӧ������
	 * @return��ά�������
	 */
	public abstract ResultBean handleService(String handleType,String serviceType,String name);
	
	
	
}
