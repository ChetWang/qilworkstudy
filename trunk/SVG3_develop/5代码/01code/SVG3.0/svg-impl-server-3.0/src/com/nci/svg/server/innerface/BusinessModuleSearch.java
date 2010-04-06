package com.nci.svg.server.innerface;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.operinterface.OperInterfaceDefines;

/**
 * <p>
 * ���⣺BussinessModuleSearch.java
 * </p>
 * <p>
 * ������ ҵ��ϵͳ��ѯ��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-04
 * @version 1.0
 */
public class BusinessModuleSearch extends OperationServiceModuleAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7502385969946301310L;

	public BusinessModuleSearch(HashMap parameters) {
		super(parameters);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.service.OperationServiceModuleAdapter#handleOper(java.lang.Object)
	 */
	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean rb = new ResultBean();
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("ҵ��ϵͳ��ѯ��δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("ҵ��ϵͳ��ѯ��δ�ܻ�ȡ��־��������!");
		}

		log.log(this, LoggerAdapter.DEBUG, "ҵ��ϵͳ��ѯ�࣬��ȡ����" + actionName
				+ "���������");

		// ׼��ҵ����������
		String businessID = (String) getRequestParameter(requestParams,
				ActionParams.BUSINESS_ID);
		String moduleID = (String) getRequestParameter(requestParams,
				ActionParams.MODULE_ID);
		String type = (String) getRequestParameter(requestParams, "type");
		HashMap params = new HashMap();
		params.put(ActionParams.BUSINESS_ID, businessID);
		params.put(ActionParams.MODULE_ID, moduleID);
		params.put(ActionParams.TYPE, type);

		// ҵ�������ж�����
		if (actionName.equalsIgnoreCase(ActionNames.GET_MODEL_LIST)) {
			// ��ȡҵ��ģ���б�
			rb = refersToOperInterface(businessID,
					OperInterfaceDefines.GET_MODEL_LIST_NAME, params);
		} else if (actionName.equalsIgnoreCase(ActionNames.GET_MODEL_PARAMS)) {
			// ��ȡҵ��ģ������
			rb = refersToOperInterface(businessID,
					OperInterfaceDefines.GET_MODEL_PARAMS_NAME, params);
		} else if (actionName.equalsIgnoreCase(ActionNames.GET_MODEL_ACTIONS)) {
			// ��ȡҵ��ģ�Ͷ����б�
			rb = refersToOperInterface(businessID,
					OperInterfaceDefines.GET_MODEL_ACTIONLIST_NAME, params);
		} else {
			rb = new ResultBean(ResultBean.RETURN_ERROR, actionName + "���Ŀǰ������δʵ��", null,
					null);
		}

		return rb;
	}
}
