package com.nci.svg.sdk.server.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.module.DefaultModuleAdapter;
import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-11-25
 * @���ܣ�ҵ������������
 * 
 */
public abstract class OperationServiceModuleAdapter extends
		DefaultModuleAdapter {
	/**
	 * �����������
	 */
	protected ServerModuleControllerAdapter controller;
	/**
	 * ��־����
	 */
	protected LoggerAdapter log;

	/**
	 * ģ���������б�
	 */
	protected HashMap parameters = new HashMap();

	public OperationServiceModuleAdapter(HashMap parameters) {
		this.parameters = parameters;
		// ��ȡ�����������
		controller = (ServerModuleControllerAdapter) parameters
				.get(ServerModuleControllerAdapter.class.toString());
		// ��ȡ��־��������
		if (controller != null)
			log = controller.getLogger();
	}

	/**
	 * �������󣬸��ݴ����������Ϣ��ҵ��������
	 * 
	 * @param request:�����,������ĸ�ʽΪMAP��ҵ�����������ݱ�ʶ��ȡ����
	 * @return����������
	 */
	public abstract ResultBean handleOper(String actionName, Map requestParams);

	/**
	 * ��ȡ���������ָ����������ֵ
	 * 
	 * @param requestParams:���������MAP
	 * @param requestName:������в�����
	 * @return
	 */
	protected Object getRequestParameter(Object requestParams, String paramName) {
		if (requestParams instanceof Map) {
			Map params = (Map) requestParams;
			try
			{
				
			Object[] values = (Object[]) params.get(paramName);
			if (values != null)
				return values[0];
			}
			catch(Exception ex){
				Object value = params.get(paramName);
				return value;
			}
		}
		return null;
	}

	/**
	 * ��iso8859_1������ַ���ת��ΪUTF-8���ַ���
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return ת���õ��ַ���
	 * @throws UnsupportedEncodingException
	 */
	protected String isoToUtf8(String str) throws UnsupportedEncodingException {
		String result = "";
		result = (str == null) ? "" : new String(str.getBytes("ISO-8859-1"),
				"UTF-8");
		return result;
	}
	
	/**
	 * ���ַ���ת��Ϊ�ַ�������
	 * @param name
	 * @return
	 */
	protected String[] changeStringToArray(String name) {
		if (name != null && name.length() > 0) {
			String[] names = { name };
			return names;
		} else {
			return null;
		}
	}


	/**
	 * �����ʼ��
	 * 
	 * @return����ʼ�����
	 */
	public int init() {
		return OPER_SUCCESS;
	}

	/**
	 * ����ϵͳ��������
	 * 
	 * @param action��������
	 * @param requestParams�������б�
	 * @return�����ؽ��
	 */
	public ResultBean refersTo(String action, Map requestParams) {
		if (controller == null || controller.getServiceShunt() == null) {
			ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR, "�����������", null, null);
			return bean;
		}
		return controller.getServiceShunt().serviceMultiplexing(action,
				requestParams);
	}

	/**
	 * ����ϵͳ�ⲿ�ӿ�
	 * @param bussinessID:ҵ��ϵͳ���
	 * @param action���ӿڷ�����
	 * @param requestParams�������б�
	 * @return�����ؽ��
	 */
	public ResultBean refersToOperInterface(String bussinessID, String action,
			Map requestParams) {
		if (controller == null || controller.getOperInterfaceManager() == null) {
			ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR, "�����������", null, null);
			return bean;
		}
		return controller.getOperInterfaceManager().OperInterfaceMultiplexing(
				bussinessID, action, requestParams);
	}

	public String getModuleType() {
		return ModuleDefines.OPERATION_SERVICE_MODULE;
	}
	
}
