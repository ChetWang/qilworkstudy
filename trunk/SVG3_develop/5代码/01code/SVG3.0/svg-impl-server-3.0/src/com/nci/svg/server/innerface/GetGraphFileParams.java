package com.nci.svg.server.innerface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.GraphFileParamsBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.bean.GraphFileParamsBean.GraphFileParamBean;
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
 * @ʱ�䣺2008-12-26
 * @���ܣ���ȡ��Ӧ��ҵ��ϵͳ�ж�Ӧ��ҵ��ͼ���͵Ĳ�����Ϣ
 * 
 */
public class GetGraphFileParams extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5478684264059652910L;

	public GetGraphFileParams(HashMap parameters) {
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
			return returnErrMsg("��ȡҵ��ͼ���Ͳ�����δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("��ȡҵ��ͼ���Ͳ�����δ�ܻ�ȡ��־��������!");
		}

		log.log(this, LoggerAdapter.DEBUG, "��ȡҵ��ͼ���Ͳ�������ȡ����" + actionName + "���������");

		ResultBean rb = new ResultBean();
		if (actionName.equalsIgnoreCase(ActionNames.GET_GRAPHFILE_PARAMS)) {
			String bussID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			String graphType = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_TYPE);
			
			rb = getGraphFileParams(bussID, graphType);
		} else {
			returnErrMsg(actionName + "���Ŀǰ������δʵ��!");
		}
		return rb;
	}

	/**
	 * ��ȡָ��ͼ���ļ�������Ϣ
	 * 
	 * @param bussID:String:ҵ��ϵͳ���
	 * @param graphType:String:ͼ������
	 * @return
	 */
	private ResultBean getGraphFileParams(String bussID, String graphType) {
		String rela = controller.getGraphStorageManager().getRelateString(
				bussID);
		if (rela == null || rela.length() == 0)
			return returnErrMsg("��ȡָ��ͼ���ļ�������Ϣ���޷���ȡͼ���ʶ��");

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM t_svg_rep_fieldnote_").append(rela);
		if (graphType != null && graphType.length() > 0)
			sql.append(" WHERE RF_GRAPH_BUSINESS_TYPE = '").append(graphType)
					.append("'");
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���ȡ�����б�");
			return returnErrMsg("��ȡָ��ͼ���ļ�������Ϣ���޷���ȡ��Ч���ݿ����ӣ�");
		}
		GraphFileParamsBean beans = null;
		ArrayList list = new ArrayList();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡͼ���Ͳ�����" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());

			// String shortname = "";
			while (rs.next()) {
				beans = new GraphFileParamsBean();
				beans.setGraphType(rs.getString("RF_GRAPH_BUSINESS_TYPE"));
				String fieldName = null;
				for (int i = 0; i <= 9; i++) {
					GraphFileParamBean bean = beans.getBean(i);
					fieldName = "RF_RG_PARAM" + i + "_DESC";
					String str = rs.getString(fieldName);
					bean.setDesc(str);
					bean.setType(rs.getString("RF_RG_PARAM" + i + "_TYPE"));
					bean.setNullFlag(rs.getString("RF_RG_PARAM" + i
							+ "_NULLFLAG"));
					bean.setQueryOrder(rs.getInt("RF_RG_PARAM" + i
							+ "_QUERYORDER"));
				}
				list.add(beans);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("��ȡָ��ͼ���ļ�������Ϣ�����ݿ����ʧ�ܣ�");
			// list = null;
		} finally {
			controller.getDBManager().close(conn);
		}

		list.trimToSize();
		if (list.size() > 0)
			return returnSuccMsg("ArrayList", list);
		else
			return returnErrMsg("��ȡָ��ͼ���ļ�������Ϣ����ȡ����Ϊ�գ�");
	}

}
