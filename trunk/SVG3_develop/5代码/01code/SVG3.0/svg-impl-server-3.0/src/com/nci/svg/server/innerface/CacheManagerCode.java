package com.nci.svg.server.innerface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.cache.CacheManagerAdapter;
import com.nci.svg.sdk.server.cache.CacheObject;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.cache.CacheManagerKeys;

/**
 * <p>
 * ���⣺CacheManagerCode.java
 * </p>
 * <p>
 * ������ ���뻺����������
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
public class CacheManagerCode extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9080935259363105467L;

	/**
	 * ��ʼ������
	 */
	private final String INIT = "init";
	/**
	 * ��ѯָ������
	 */
	private final String SEARCH = "searchCode";
	/**
	 * ά��ָ������
	 */
	private final String MODIFY_CODE_CACHE = "modifyCodeCache";

	public CacheManagerCode(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("���뻺�����δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("���뻺�����δ�ܻ�ȡ��־��������!");
		}

		log.log(this, LoggerAdapter.DEBUG, "���뻺������࣬��ȡ����" + actionName + "���������");

		String subAction = (String) getRequestParameter(requestParams,
		          ActionParams.SUB_ACTION);
		ResultBean rb = new ResultBean();
		if (subAction.equalsIgnoreCase(INIT)) {
			// *************
			// ��ʼ�����뻺��
			// *************
			int result = init();
			if (result == OPER_ERROR) {
				rb.setErrorText("δ�ܳɹ����д��뻺�����");
				rb.setReturnFlag(ResultBean.RETURN_ERROR);
			} else {
				rb.setReturnFlag(ResultBean.RETURN_SUCCESS);
				rb.setReturnObj("�ɹ����д��뻺�����");
				rb.setReturnType("String");
			}
		} else if (subAction.equalsIgnoreCase(SEARCH)) {
			// **************
			// ��ѯָ��������Ϣ
			// **************
		} else if (subAction.equalsIgnoreCase(MODIFY_CODE_CACHE)) {
			// **************
			// ά��ָ��������Ϣ
			// **************
			String codeName = (String) getRequestParameter(requestParams,
					"codename");
			int result = manageCodeCache(codeName);
			if (result == OPER_ERROR) {
				rb.setErrorText("δ�ܳɹ����д��뻺�����");
				rb.setReturnFlag(ResultBean.RETURN_ERROR);
			} else {
				rb.setReturnFlag(ResultBean.RETURN_SUCCESS);
				rb.setReturnObj("�ɹ����д��뻺�����");
				rb.setReturnType("String");
			}
		} else {
			rb.setReturnFlag(ResultBean.RETURN_ERROR);
			rb.setErrorText(actionName + "����:������" + subAction + "��Ŀǰ������δʵ��!");
		}
		return rb;
	}

	/**
	 * ����ָ��������Ϣ�����������
	 * 
	 * @param codeName
	 *            String �������
	 * @return
	 */
	private int manageCodeCache(String codeName) {
		if (codeName == null || codeName.length() <= 0) {
			// �ж������������Ƿ�Ϸ�
			return OPER_ERROR;
		}
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			// �ж��Ƿ��û��������
			return OPER_ERROR;
		}

		// ��ȡ���뻺��
		Map cacheHash = cma.getCacheObj(CacheManagerKeys.CODE_MANAGER)
				.getCacheMap();
		if (cacheHash == null) {
			return OPER_ERROR;
		}
		// ��ȡָ�������Ĵ��뻺��
		Map moduleHash = (Map) cacheHash.get(codeName);
		// ����
		// Untilities.displayCodeHash(cacheHash);
		// �����ݿ��л�ȡָ�������Ĵ�����Ϣ
		Map dbHash = (Map) getCodeMessage(codeName).get(codeName);

		if (moduleHash != null && dbHash != null) {
			// ԭ�������ݴ��ڣ���ѯ������Ϣ���ڣ��޸ģ�
			cacheHash.put(codeName, dbHash);
		} else if (moduleHash != null && dbHash == null) {
			// ԭ�������ݴ��ڣ���ѯ������Ϣ�����ڣ�ɾ����
			cacheHash.remove(codeName);
		} else if (moduleHash == null && dbHash != null) {
			// ԭ�������ݲ����ڣ���ѯ������Ϣ���ڣ�������
			cacheHash.put(codeName, dbHash);
		}
		// ��ӡ�����ϣ������
		// Untilities.displayCodeHash(cacheHash);
		cma.setCacheObj(CacheObject.MAP_FLAG, CacheManagerKeys.CODE_MANAGER,
				cacheHash);
		return OPER_SUCCESS;
	}

	public int init() {
		if (controller == null)
			return OPER_ERROR;
		// ****************
		// ��������Ϣ���뻺��
		// ****************
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return OPER_ERROR;
		}

		Map codeHash = getCodeMessage(null);
		// ��ӡ�����ϣ������
//		Untilities.displayCodeHash(codeHash);
		cma.addCacheInnerData(CacheObject.MAP_FLAG,
				CacheManagerKeys.CODE_MANAGER, codeHash);

		return OPER_SUCCESS;
	}

	/**
	 * ����ģ�����ƻ�ȡ�����б�
	 * 
	 * @param codeName
	 *            String ģ������
	 * @return ������Ϣ�б�
	 */
	private Map getCodeMessage(String codeName) {
		Map codeHash = new HashMap();
		// sql���
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.*, cm_parentshortname").append(
				" FROM t_svg_code_commsets a, t_svg_code_manager b").append(
				" WHERE a.cc_shortname = b.cm_shortname");
		if (codeName != null && codeName.length() > 0) {
			sql.append(" AND cc_shortname = '").append(codeName).append("'");
		}
		sql.append(" ORDER BY cc_shortname");

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���ȡ�����б�");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡ�����б�" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			Map subHash = null;
			CodeInfoBean bean;
			String shortname = "";
			while (rs.next()) {
				String temp = rs.getString("cc_shortname");
				if (!temp.equals(shortname)) {
					if (subHash != null)
						codeHash.put(shortname, subHash);
					subHash = new HashMap();
					shortname = temp;
				}
				bean = new CodeInfoBean();
				bean.setShortName(temp);
				bean.setValue(rs.getString("cc_code"));
				bean.setName(rs.getString("cc_name"));
				bean.setSpell(rs.getString("cc_spell"));
				bean.setDesc(rs.getString("cc_desc"));
				bean.setParentShortName(rs.getString("cm_parentshortname"));
				bean.setParentValue(rs.getString("cc_parent_code"));
				bean.setParam1(rs.getString("cc_param1"));
				bean.setParam2(rs.getString("cc_param2"));
				bean.setParam3(rs.getString("cc_param3"));
				bean.setParam4(rs.getString("cc_param4"));
				bean.setParam5(rs.getString("cc_param5"));
				bean.setCc_id(rs.getString("cc_id"));
				subHash.put(bean.getValue(), bean);
			}
			if (subHash != null)
				codeHash.put(shortname, subHash);
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		} finally {
			controller.getDBManager().close(conn);
		}

		return codeHash;
	}
}
