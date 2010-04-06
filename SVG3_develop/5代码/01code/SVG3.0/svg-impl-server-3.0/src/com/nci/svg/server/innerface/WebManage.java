package com.nci.svg.server.innerface;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.cache.CacheManagerKeys;

/**
 * <p>
 * ���⣺WebManage.java
 * </p>
 * <p>
 * ������Web����ҳ����Ӧ����
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-17
 * @version 1.0
 */
public class WebManage extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5514924255543862406L;

	public WebManage(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean rb = new ResultBean();
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("eb����ҳ����Ӧ����δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("eb����ҳ����Ӧ����δ�ܻ�ȡ��־��������!");
		}

		log.log(this, LoggerAdapter.DEBUG, "Web����ҳ����Ӧ�����࣬��ȡ����" + actionName
				+ "���������");

		if (actionName.equalsIgnoreCase(ActionNames.CONFIG_CODE_DATA)) {
			// ���������Ӧ
			String type = (String) getRequestParameter(requestParams, "type"); // ��������
			String codeName = (String) getRequestParameter(requestParams,
					"codename"); // �������
			String value = (String) getRequestParameter(requestParams, "value"); // ����ֵ
			String desc = (String) getRequestParameter(requestParams, "desc"); // ����
			rb = configCodeData(type, codeName, value, desc);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.CONFIG_SERVICE_MANAGER)) {
			// �����������
			String type = (String) getRequestParameter(requestParams, "type"); // ��������
			String managerType = (String) getRequestParameter(requestParams,
					"managertype");// ��������Ķ���
			String desc = (String) getRequestParameter(requestParams, "desc"); // ����
			rb = configServiceManager(type, managerType, desc);
		} else if (actionName.equalsIgnoreCase(ActionNames.GET_SERVICE_STATUS)) {
			// ����״̬��ѯ
			String type = (String) getRequestParameter(requestParams,
					"servicetype"); // ��������
			String serviceName = (String) getRequestParameter(requestParams,
					"servicename");// ������
			rb = getServiceStatus(type, serviceName);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.CONFIG_SERVICE_MODULE)) {
			// �������
			String type = (String) getRequestParameter(requestParams, "type"); // ��������
			String serviceType = (String) getRequestParameter(requestParams,
					"servicetype");
			String serviceName = (String) getRequestParameter(requestParams,
					"servicename");// ������
			String desc = (String) getRequestParameter(requestParams, "desc"); // ����
			rb = configServiceModule(type, serviceType, serviceName, desc);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.CONFIG_SYMBOL_MANAGER)) {
			// ͼԪ����
			String type = (String) getRequestParameter(requestParams, "type"); // ��������
			String symbolType = (String) getRequestParameter(requestParams,
					"symboltype");
			String symbolName = (String) getRequestParameter(requestParams,
					"symbolname");// ������
			String desc = (String) getRequestParameter(requestParams, "desc");// ����
			rb = configSymbolManager(type, symbolType, symbolName, desc);
		} else if (actionName.equalsIgnoreCase(ActionNames.CONFIG_INDUNORM)) {
			// ҵ��淶����
			String type = (String) getRequestParameter(requestParams, "type"); // ��������
			String indunormType = (String) getRequestParameter(requestParams,
					"indunormtype");// �淶��������
			String desc = (String) getRequestParameter(requestParams, "desc");// ����
			rb = configIndunorm(type, indunormType, desc);
		} else {
			rb = returnErrMsg(actionName + "���Ŀǰ������δʵ��!");
		}
		return rb;
	}

	/**
	 * Web���� ���������Ӧ����
	 * 
	 * @param type
	 *            String �������ͣ�(add/delete/modify)
	 * @param codeName
	 *            String �������
	 * @param value
	 *            String ����ֵ
	 * @param desc
	 *            String ��������
	 * @return
	 */
	private ResultBean configCodeData(String type, String codeName,
			String value, String desc) {
		ResultBean rb = new ResultBean();
		HashMap params = new HashMap();
		params.put("codename", changeStringToArray(codeName));
		params.put("codevalue", changeStringToArray(value));
		params.put("codedesc", changeStringToArray(desc));
		params.put(ActionNames.action, changeStringToArray("modifyCodeCache"));
		// params.put(ActionNames.action, type);
		rb = refersTo(CacheManagerKeys.CODE_MANAGER.toUpperCase(), params);
		return rb;
	}

	/**
	 * Web���� �������������Ӧ����
	 * 
	 * @param type
	 *            String �������ͣ�(add/delete/modify)
	 * @param managerType
	 *            String ��������Ķ���
	 * @param desc
	 *            String ����
	 * @return
	 */
	private ResultBean configServiceManager(String type, String managerType,
			String desc) {
		return controller.getServiceModuleManager().configModule(type,
				managerType);
	}

	/**
	 * ��ȡ�������ṩ�����״̬
	 * 
	 * @param serviceName
	 *            String ������
	 * @return
	 */
	private ResultBean getServiceStatus(String serviceType, String serviceName) {
		return controller.getServiceShunt().getServiceStatus(serviceType,
				serviceName);
	}

	/**
	 * ���������Ӧ����
	 * 
	 * @param type
	 *            String ��������(add/delete/modify/start/stop)
	 * @param managerType
	 *            String �������
	 * @param serviceName
	 *            String ������
	 * @param desc
	 *            String ����
	 * @return
	 */
	private ResultBean configServiceModule(String type, String managerType,
			String serviceName, String desc) {
		return controller.getServiceShunt().handleService(type, managerType,
				serviceName);
	}

	/**
	 * Web���� ͼԪ������Ӧ����
	 * 
	 * @param type
	 *            String
	 *            ��������(validity/cancel_validity/publish/cancel_publish/delete)
	 * @param symbolType
	 *            String ͼԪ����(graphunit/template)
	 * @param symbolName
	 *            String ͼԪ����
	 * @param desc
	 *            String ����
	 * @return
	 */
	private ResultBean configSymbolManager(String type, String symbolType,
			String symbolName, String desc) {
		ResultBean rb = new ResultBean();
		HashMap params = new HashMap();
		params.put("symboltype", changeStringToArray(symbolType));
		params.put("symbolname", changeStringToArray(symbolName));
		params.put("codedesc", changeStringToArray(desc));
		params
				.put(ActionNames.action,
						changeStringToArray("modifySymbolCache"));
		// params.put(ActionNames.action, type);
		rb = refersTo(CacheManagerKeys.SYMBOL_MANAGER.toUpperCase(), params);
		return rb;
	}

	/**
	 * Web���� ��ҵ�淶������Ӧ����
	 * 
	 * @param type
	 *            String �������ͣ�(add/delete/modify)
	 * @param indunormType
	 *            String �淶��������(indunorm/desc/metadata/fields)
	 * @param desc
	 *            String ����
	 * @return
	 */
	private ResultBean configIndunorm(String type, String indunormType,
			String desc) {
		return null;
	}

}
