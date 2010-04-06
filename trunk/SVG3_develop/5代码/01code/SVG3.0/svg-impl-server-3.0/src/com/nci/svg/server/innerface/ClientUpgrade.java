package com.nci.svg.server.innerface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ModuleInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;

/**
 * <p>
 * ���⣺ClientUpgrade.java
 * </p>
 * <p>
 * ������ �ͻ�������
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
public class ClientUpgrade extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7905991472780450975L;

	public ClientUpgrade(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("�ͻ����������ܣ�δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("�ͻ����������ܣ�δ�ܻ�ȡ��־��������!");
		}

		log.log(this, LoggerAdapter.DEBUG, "�ͻ����������ܣ���ȡ����" + actionName + "���������");

		ResultBean rb = new ResultBean();
		if (actionName.equalsIgnoreCase(ActionNames.GET_UPGRADE_MODULE)) {
			// *******************
			// ��ȡ�ͻ�������汾��Ϣ
			// *******************
			String moduleType = (String) getRequestParameter(requestParams,
					ActionParams.MODULE_TYPE);

			rb = getUpgradeModule(moduleType);
			// ArrayList list = getUpgradeModule(moduleType);
			// if (list != null && list.size() > 0) {
			// rb.setReturnFlag(ResultBean.RETURN_SUCCESS);
			// rb.setReturnType("ArrayList");
			// rb.setReturnObj(list);
			// } else {
			// rb.setReturnFlag(ResultBean.RETURN_ERROR);
			// rb.setErrorText("�ͻ����������ܣ��޷���ȡָ�����ݣ�");
			// }
		} else if (actionName.equalsIgnoreCase(ActionNames.DOWN_UPGRADE_MODULE)) {
			// ****************
			// ��ȡ�ͻ����������
			// ****************
			String moduleShortName = (String) getRequestParameter(
					requestParams, ActionParams.MODULE_SHORT_NAME);

			rb = downloadUpgradeModule(moduleShortName);
//			byte[] content = downloadUpgradeModule(moduleShortName);
//			if (content != null && content.length > 0) {
//				rb.setReturnFlag(ResultBean.RETURN_SUCCESS);
//				rb.setReturnType("byte[]");
//				rb.setReturnObj(content);
//			} else {
//				rb.setReturnFlag(ResultBean.RETURN_ERROR);
//				rb.setErrorText("��ȡ�ͻ���������ݣ��������ݿ�ʧ�ܣ�");
//			}
		} else {
			rb = returnErrMsg(actionName + "���Ŀǰ������δʵ��!");
		}

		return rb;
	}

	/**
	 * ��ȡָ�����͵Ŀͻ�������嵥
	 * 
	 * @param moduleType
	 *            String ������ͣ���Ϊƽ̨�����ҵ������� ���������Ϊ��ʱ����ȫ���ͻ�������İ汾��Ϣ
	 * @return ����嵥
	 */
	private ResultBean getUpgradeModule(String moduleType) {
		// sql���
		StringBuffer sql = new StringBuffer();
		if (moduleType != null && moduleType.length() != 0) {
			sql.append("SELECT a.*").append(
					" FROM t_svg_module_list a, t_svg_code_commsets b").append(
					" WHERE a.ml_location = '1' AND a.ml_kind = b.cc_code")
					.append(" AND b.cc_shortname = 'SVG_MODULE_KIND'").append(
							"AND b.cc_name = '").append(moduleType).append("'");
		} else {
			sql.append("SELECT a.*").append(
					" FROM t_svg_module_list a, t_svg_code_commsets b").append(
					" WHERE a.ml_location = '1' AND a.ml_kind = b.cc_code")
					.append(" AND b.cc_shortname = 'SVG_MODULE_KIND'");
		}

		ArrayList moduleList = new ArrayList();
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ÿͻ�������嵥��");
			return returnErrMsg("��ȡ�ͻ�������嵥���޷���ȡ��Ч���ݿ����ӣ�");
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡ�ͻ�������嵥��" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			ModuleInfoBean bean;
			while (rs.next()) {
				bean = new ModuleInfoBean();
				bean.setModuleShortName(rs.getString("ml_shortname"));
				bean.setName(rs.getString("ml_name"));
				bean.setEdition(rs.getString("ml_edition"));
				bean.setLogLevel(rs.getString("ml_log_level"));
				bean.setLogType(rs.getString("ml_log_type"));
				moduleList.add(bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("��ȡ�ͻ�������嵥�����ݿ����ʧ�ܣ�");
		} finally {
			controller.getDBManager().close(conn);
		}

		moduleList.trimToSize();

		if (moduleList.size() > 0)
			return returnSuccMsg("ArrayList", moduleList);
		else
			return returnErrMsg("��ȡ�ͻ�������嵥����ȡ����Ϊ�գ�");
	}

	/**
	 * ��ȡָ�����
	 * 
	 * @param moduleShortName
	 *            String �������
	 * @return �������
	 */
	private ResultBean downloadUpgradeModule(String moduleShortName) {
		byte[] content = null;
		// ��ȡ���ݿ�����
		String type = controller.getDBManager().getDBType("SVG");
		// sql���
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ml_content FROM t_svg_module_list").append(
				" WHERE ml_shortname = '").append(moduleShortName).append("'");

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ÿͻ���������ݣ�");
			return returnErrMsg("��ȡָ��������ݣ��޷���ȡ��Ч���ݿ����ӣ�");
		}
		if (type != null) {
			// ͨ������ת�����ȡblob�ֶ�
			DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
					.getDBSqlIdiom(type);
			// ��ȡ�������
			if (sqlIdiom != null) {
				content = sqlIdiom.getBlob(conn, sql.toString(), "ml_content");
			}
			// �ر����ݿ�����
			controller.getDBManager().close(conn);
		}
		
		if(content!=null && content.length>0)
			return returnSuccMsg("byte[]", content);
		else
			return returnErrMsg("��ȡָ��������ݣ���ȡ����Ϊ�գ�");
	}

}
