package com.nci.svg.server.innerface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.cache.CacheManagerAdapter;
import com.nci.svg.sdk.server.cache.CacheObject;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.cache.CacheManagerKeys;

/**
 * <p>
 * ���⣺CodeSearch.java
 * </p>
 * <p>
 * ������ �����ѯ
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-01
 * @version 1.0
 */
public class CodeSearch extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4422712116862921079L;

	public CodeSearch(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("�����ѯ���ܣ�δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("�����ѯ���ܣ�δ�ܻ�ȡ��־��������!");
		}

		log.log(this, LoggerAdapter.DEBUG, "�����ѯ���ܣ���ȡ����" + actionName
				+ "���������");
		log.log(this, LoggerAdapter.DEBUG, "�����ѯ���ܣ���ȡ����" + actionName
				+ "���������", true);
		ResultBean rb = new ResultBean();
		if (actionName.equalsIgnoreCase(ActionNames.GET_SVG_CODES)) {
			// ***********
			// ��ȡ�����б�
			// ***********
			String moduleName = (String) getRequestParameter(requestParams,
					ActionParams.MODULE_NAME);
			String expr = (String) getRequestParameter(requestParams,
					ActionParams.EXPRSTRING);
			rb = getCodeMessage(moduleName, expr);
		} else {
			rb = returnErrMsg(actionName + "���Ŀǰ������δʵ��!");
		}

		return rb;
	}

	/**
	 * ����ģ�����ƻ�ȡ�����б����ģ����Ϊ���򷵻�ȫ��������Ϣ
	 * 
	 * @param moduleName
	 *            String ģ������
	 * @return ������Ϣ�б�
	 */
	private ResultBean getCodeMessage(String moduleName, String expr) {
		Map codeMap = null;
		if (expr == null || expr.length() == 0) {
			// �޹�������
			CacheManagerAdapter cma = controller.getCacheManager();
			if (cma == null) {
				return returnErrMsg("��ȡ�����б��޷���ȡ�����������");
			}

			CacheObject co = cma.getCacheObj(CacheManagerKeys.CODE_MANAGER);
			codeMap = co.getCacheMap();
			if (moduleName != null && moduleName.length() > 0) {
				Map subMap = (Map) codeMap.get(moduleName);
				Map reMap = new HashMap();
				reMap.put(moduleName, subMap);
				return returnSuccMsg("Map", reMap);
			} else {
				return returnSuccMsg("Map", codeMap);
			}
		} else {
			if (moduleName != null) {
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT cc_code,cc_name ").append(
						"FROM t_svg_code_commsets ").append("WHERE upper(cc_shortname) = '").append(moduleName.toUpperCase())
						.append("' AND ")
						.append(expr);

				// ��ȡ���ݿ�����
				Connection conn = controller.getDBManager()
						.getConnection("svg");
				if (conn == null) {
					log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ȡģ����Ϣ��");
					return returnErrMsg("��ȡģ����Ϣ���޷������Ч���ݿ����ӣ�");
				}
				codeMap = new HashMap();
				try {
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql.toString());
					CodeInfoBean bean = null;
					while (rs.next()) {
						bean = new CodeInfoBean();
						bean.setName(rs.getString("CC_NAME"));
						bean.setValue(rs.getString("CC_CODE"));
						codeMap.put(rs.getString("CC_CODE"), bean);
					}
					if (rs != null)
						rs.close();

				} catch (SQLException e) {

				} finally {
					controller.getDBManager().close(conn);
				}

			}
		}
		if (codeMap != null)
			return returnSuccMsg("Map", codeMap);
		return returnErrMsg("�޷��������Ĵ��뼯");
	}
}
