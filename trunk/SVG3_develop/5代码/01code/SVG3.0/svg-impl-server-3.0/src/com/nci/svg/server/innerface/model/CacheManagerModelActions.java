package com.nci.svg.server.innerface.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ModelActionBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.cache.CacheManagerAdapter;
import com.nci.svg.sdk.server.cache.CacheObject;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.cache.CacheManagerKeys;

/**
 * <p>
 * ���⣺CacheManagerModelActions.java
 * </p>
 * <p>
 * ������ ҵ��ģ�Ͷ���������������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-18
 * @version 1.0
 */
public class CacheManagerModelActions extends OperationServiceModuleAdapter {
	private static final long serialVersionUID = -5341715339507227058L;
	/**
	 * ��ʼ������
	 */
	private final String INIT = "init";
	/**
	 * ��ѯҵ��ģ�Ͷ���
	 */
	private final String SEARCH_MODEL_ACTIONS = "searchModelActions";

	public CacheManagerModelActions(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("ҵ��ģ�Ͷ����������δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("ҵ��ģ�Ͷ����������δ�ܻ�ȡ��־��������!");
		}

		log.log(this, LoggerAdapter.DEBUG, "ҵ��ģ�Ͷ�����������࣬��ȡ����" + actionName
				+ "���������");

		String subAction = (String) getRequestParameter(requestParams,
				ActionParams.SUB_ACTION);
		ResultBean rb = new ResultBean();
		if (subAction.equalsIgnoreCase(INIT)) {
			// *************
			// ��ʼ�����뻺��
			// *************
			int result = init();
			if (result == OPER_ERROR) {
				rb.setErrorText("δ�ܳɹ�����ҵ��ģ�Ͷ����������");
				rb.setReturnFlag(ResultBean.RETURN_ERROR);
			} else {
				rb.setReturnFlag(ResultBean.RETURN_SUCCESS);
				rb.setReturnObj("�ɹ�����ҵ��ģ�Ͷ����������");
				rb.setReturnType("String");
			}
		} else if (subAction.equalsIgnoreCase(SEARCH_MODEL_ACTIONS)) {
			// *************
			// 
			// *************
		}

		return rb;
	}

	public int init() {
		if (controller == null)
			return OPER_ERROR;
		// ****************
		// ��ҵ��ģ�Ͷ�����Ϣ���뻺��
		// ****************
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return OPER_ERROR;
		}

		Map codeHash = getActionsMessage();
		cma.addCacheInnerData(CacheObject.MAP_FLAG,
				CacheManagerKeys.MODEL_ACTIONS_MANAGER, codeHash);

		return OPER_SUCCESS;
	}

	/**
	 * 2009-3-30 Add by ZHM ��ȡ���е�ҵ��ģ�Ͷ�����Ϣ��������
	 * 
	 * @return
	 */
	private Map getActionsMessage() {
		Map actionMap = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from t_view_svg_datamodel_behavior");
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���ȡҵ��ģ�Ͷ����б�");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡҵ��ģ�Ͷ����б�" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			ModelActionBean bean = null;
			while (rs.next()) {
				String id = rs.getString("seqkey");
				String description = rs.getString("description");
				String name = rs.getString("name");
				String type = rs.getString("type");
				String content = rs.getString("url");

				bean = new ModelActionBean();
				bean.setId(id);
				bean.setShortName(description);
				bean.setName(name);
				bean.setType(type);
				bean.setContent(content);
				actionMap.put(id, bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		} finally {
			controller.getDBManager().close(conn);
		}
		return actionMap;
	}
}
