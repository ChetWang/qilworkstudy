package com.nci.svg.sdk.module;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;

/**
 * Ĭ�ϵ�ģ����������������GeneralModuleIF�Ľӿڶ��ṩĬ��ʵ�֣�������ʼ��init�Ϳ������start�ڹ��캯����ִ�С�
 * 
 * @author Qil.Wong
 * 
 */
public abstract class DefaultModuleAdapter implements GeneralModuleIF {

	/**
	 * ģ���������б�
	 */
	protected HashMap parameters = new HashMap();

	/**
	 * �������ͣ���ʼ���������
	 */
	public final static String PARAM_TYPE_INIT = "init";
	/**
	 * �������ͣ�����ģ���������
	 */
	public final static String PARAM_TYPE_START = "start";
	/**
	 * �������ͣ�ֹͣģ���������
	 */
	public final static String PARAM_TYPE_STOP = "stop";
	/**
	 * �������ͣ����³�ʼ���������
	 */
	public final static String PARAM_TYPE_REINIT = "reinit";

	/**
	 * ģ�����������
	 */
	protected boolean startFlag = false;

	protected String moduleUUID = null;

	/**
	 * ���캯����Ĭ�Ϲ��������������
	 */
	public DefaultModuleAdapter() {

	}


	public DefaultModuleAdapter(HashMap parameters) {
		this.parameters = parameters;
		System.out.println(this.toString());
		// int result = init();
		// if (result != MODULE_INITIALIZE_COMPLETE) {
		// new ModuleInitializeFailedException(this).printStackTrace();
		// }
	}

	public int init() {
		return MODULE_INITIALIZE_COMPLETE;
	}

	public int start() {
		startFlag = true;
		return MODULE_START_COMPLETE;
	}

	public int stop() {
		startFlag = false;
		return MODULE_STOP_COMPLETE;
	}

	public int reInit() {
		return MODULE_INITIALIZE_COMPLETE;
	}

	/**
	 * �����������Ͳ���
	 * 
	 * @param paramType
	 *            ��������
	 * @param obj
	 *            ��������
	 */
	public void putParameter(String paramType, Object obj) {
		parameters.put(paramType, obj);
	}

	/**
	 * ��ȡ���Ͳ���
	 * 
	 * @param paramType
	 *            ��������
	 * @return �������Ͷ�Ӧ�Ķ���
	 */
	public Object getParameter(String paramType) {
		return parameters.get(paramType);
	}

	public boolean isStoped() {
		return !startFlag;
	}

	public ResultBean handleOper(String action, Map params) {
		ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR, "ҵ��������",
				null, null);
		return bean;
	}

	public String getModuleID() {
		return moduleUUID;
	}

	public HashMap getParameters() {
		return parameters;
	}

	/**
	 * ���ò���ʧ�ܷ��ض���
	 * 
	 * @param text:String:������Ϣ
	 * @return ����ֵ����
	 */
	protected ResultBean returnErrMsg(String text) {
		ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR, text, null,
				null);
		return bean;
	}
	
	/**
	 * ���ò����ɹ����ض���
	 * @param type:String:���ض�������
	 * @param obj:Object:���ض���ֵ
	 * @return ����ֵ����
	 */
	protected ResultBean returnSuccMsg(String type, Object obj){
		return new ResultBean(ResultBean.RETURN_SUCCESS, null, type, obj);
	}
}
