package com.nci.svg.server.innerface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-3-5
 * @���ܣ�
 *
 */
public class GetCanvasOper extends OperationServiceModuleAdapter {
	private static final long serialVersionUID = 4883724557298568928L;

	public GetCanvasOper(HashMap parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("Canvas�ӿڲ�ѯ���ܣ�δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("Canvas�ӿڲ�ѯ���ܣ�δ�ܻ�ȡ��־��������!");
		}

		log.log(this, LoggerAdapter.DEBUG, "Canvas�ӿڲ�ѯ���ܣ���ȡ����" + actionName
				+ "���������");
		log.log(this, LoggerAdapter.DEBUG, "Canvas�ӿڲ�ѯ���ܣ���ȡ����" + actionName
				+ "���������", true);
		ResultBean rb = new ResultBean();
		if (actionName.equalsIgnoreCase(ActionNames.GET_GRAPHBUSINESSTYPE_CANVASOPER)) {
		    String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
		    String businessType = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_TYPE);
		    rb = queryCanvasOper(businessID,businessType);
		}
		return rb;
	}
	
	private ResultBean queryCanvasOper(String businessID,String businessType)
	{
		// sql���
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.mg_canvasoper").append(
					" FROM t_svg_module_graphrela a").append(
					" WHERE a.mg_businessid = '")
					.append(businessID).append("' AND a.mg_graph_business_type = '").append(businessType).append("' ")
					.append("AND a.mg_validity = '1'");


		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷�Canvas�ӿڲ�ѯ��");
			return returnErrMsg("Canvas�ӿڲ�ѯ���޷���ȡ��Ч���ݿ����ӣ�");
		}
		String canvasOper = null;
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "Canvas�ӿڲ�ѯ��" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			if (rs.next()) {
				canvasOper = rs.getString("MG_CANVASOPER");
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("Canvas�ӿڲ�ѯ�����ݿ����ʧ�ܣ�");
		} finally {
			controller.getDBManager().close(conn);
		}


		if (canvasOper != null && canvasOper.length() > 0)
			return returnSuccMsg("String", canvasOper);
		else
			return returnErrMsg("Canvas�ӿڲ�ѯ����ȡ����Ϊ�գ�");

	}



}
