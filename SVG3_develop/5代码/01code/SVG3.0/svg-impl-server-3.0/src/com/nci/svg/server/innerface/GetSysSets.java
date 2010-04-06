package com.nci.svg.server.innerface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.bean.SysSetBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-22
 * @���ܣ���ȡϵͳ���ò���
 * 
 */
public class GetSysSets extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3267394765192630291L;

	public GetSysSets(HashMap parameters) {
		super(parameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.service.OperationServiceModuleAdapter#handleOper(java.lang.String,
	 *      java.util.Map)
	 */
	public ResultBean handleOper(String actionName, Map requestParams) {
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("��ȡϵͳ���ò�����δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("��ȡϵͳ���ò�����δ�ܻ�ȡ��־��������!");
		}
		log.log(this, LoggerAdapter.DEBUG, "��ȡϵͳ���ò������ܣ���ȡ����" + actionName
				+ "���������");

		ResultBean rb = new ResultBean();
		// �ж����������Ƿ�ΪgetSvgCodes
		if (actionName.equalsIgnoreCase(ActionNames.GET_SYSSETS)) {
			String shortName = (String) getRequestParameter(requestParams,
					ActionParams.SHORT_NAME);
			rb = getSysSets(shortName);
		} else {
			rb = returnErrMsg(actionName + "���Ŀǰ������δʵ��!");
		}
		return rb;
	}

	/**
	 * ��ȡָ��ҵ��ϵͳ֧�ֵ�ͼ���ļ�����
	 * 
	 * @param bussID
	 *            String ҵ��ϵͳ���
	 * @return
	 */
	protected ResultBean getSysSets(String shortName) {
		// sql���
		StringBuffer sql = new StringBuffer();
		if (shortName != null && shortName.length() != 0) {
			sql.append("SELECT ss_id, ss_short_name,ss_name,").append(
					"ss_desc,ss_param1,ss_param2,ss_param3 FROM").append(
					" t_svg_sys_sets t ").append(" WHERE ss_short_name = '")
					.append(shortName).append("'");
		} else {
			return returnErrMsg("��ȡָ��ҵ��ϵͳ֧�ֵ�ͼ���ļ����ͣ�ҵ��ϵͳ����Ϊ�գ�");
		}

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���ȡϵͳ���ò�����");
			return returnErrMsg("��ȡָ��ҵ��ϵͳ֧�ֵ�ͼ���ļ����ͣ����ݿ����ӻ�ȡʧ�ܣ�");
		}
		SysSetBean bean = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			// String name = null;
			// String code = null;
			if (rs.next()) {
				bean = new SysSetBean();
				bean.setId(rs.getString("ss_id"));
				bean.setShortName(rs.getString("ss_short_name"));
				bean.setName(rs.getString("ss_name"));
				bean.setDesc(rs.getString("ss_desc"));
				bean.setParam1(rs.getString("ss_param1"));
				bean.setParam2(rs.getString("ss_param2"));
				bean.setParam3(rs.getString("ss_param3"));
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("��ȡָ��ҵ��ϵͳ֧�ֵ�ͼ���ļ�����,���ݿ����ʧ�ܣ�");
		} finally {
			controller.getDBManager().close(conn);
		}

		if (bean == null) {
			return returnErrMsg("��ȡָ��ҵ��ϵͳ֧�ֵ�ͼ���ļ����ͣ���ȡ���ݿ�����Ϊ�գ�");
		} else {
			return returnSuccMsg("SysSetBean", bean);
		}
	}

}
