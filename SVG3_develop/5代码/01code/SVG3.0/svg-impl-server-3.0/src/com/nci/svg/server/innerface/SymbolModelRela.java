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
 * ���⣺SymbolModuleRela.java
 * </p>
 * <p>
 * ������ͼԪ��ҵ��ģ�͹�������
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
public class SymbolModelRela extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2044328103111466620L;

	public SymbolModelRela(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean rb = new ResultBean();
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("ͼԪ��ҵ��ģ�͹���������δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("ͼԪ��ҵ��ģ�͹���������δ�ܻ�ȡ��־��������!");
		}

		log.log(this, LoggerAdapter.DEBUG, "ͼԪ��ҵ��ģ�͹��������࣬��ȡ����" + actionName
				+ "���������");

		String bussinessID = (String) getRequestParameter(requestParams,
				ActionParams.BUSINESS_ID);
		String graphUnitID = (String) getRequestParameter(requestParams,
				ActionParams.SYMBOL_ID);
		String moduleID = (String) getRequestParameter(requestParams,
				ActionParams.MODULE_ID);
		HashMap params = new HashMap();
		params.put(ActionParams.BUSINESS_ID, bussinessID);
		params.put(ActionParams.SYMBOL_ID, graphUnitID);
		params.put(ActionParams.MODULE_ID, moduleID);

		if (actionName.equalsIgnoreCase(ActionNames.GET_GRAPH_UNIT_AND_MODULE_RELA)) {
			// ͼԪ��ģ�͹�ϵ��ȡ
			rb = refersToOperInterface(bussinessID,
					OperInterfaceDefines.GET_GRAPH_UNIT_AND_MODULE_RELA_NAME,
					params);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.MODIFY_GRAPH_UNIT_AND_MODULE_RELA)) {
			// ͼԪ��ģ�͹�ϵά��
			rb = refersToOperInterface(
					bussinessID,
					OperInterfaceDefines.MODIFY_GRAPH_UNIT_AND_MODULE_RELA_NAME,
					params);
		} else {
			rb = returnErrMsg(actionName + "���Ŀǰ������δʵ��!");
		}

		return rb;
	}

}
