package com.nci.svg.server.innerface;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;

/**
 * <p>
 * ���⣺CheckConn.java
 * </p>
 * <p>
 * ������ �ͻ������������ͨ�ż��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-09
 * @version 1.0
 */
public class CheckConn extends OperationServiceModuleAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -854156213877061459L;

	public CheckConn(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean rb = new ResultBean();
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("ͨ�ż�飬δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("ͨ�ż�飬δ�ܻ�ȡ��־��������!");
		}


		if (actionName.equalsIgnoreCase(ActionNames.CHECK_URL_CONN)) {
			rb = returnSuccMsg("String", "success");
		} else {
			rb = returnErrMsg(actionName + "���Ŀǰ������δʵ��!");
		}
		return rb;
	}

}
