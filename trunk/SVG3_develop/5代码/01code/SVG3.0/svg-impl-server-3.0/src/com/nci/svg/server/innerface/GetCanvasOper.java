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
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-3-5
 * @功能：
 *
 */
public class GetCanvasOper extends OperationServiceModuleAdapter {
	private static final long serialVersionUID = 4883724557298568928L;

	public GetCanvasOper(HashMap parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("Canvas接口查询功能，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("Canvas接口查询功能，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "Canvas接口查询功能，获取到‘" + actionName
				+ "’请求命令！");
		log.log(this, LoggerAdapter.DEBUG, "Canvas接口查询功能，获取到‘" + actionName
				+ "’请求命令！", true);
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
		// sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.mg_canvasoper").append(
					" FROM t_svg_module_graphrela a").append(
					" WHERE a.mg_businessid = '")
					.append(businessID).append("' AND a.mg_graph_business_type = '").append(businessType).append("' ")
					.append("AND a.mg_validity = '1'");


		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法Canvas接口查询！");
			return returnErrMsg("Canvas接口查询，无法获取有效数据库连接！");
		}
		String canvasOper = null;
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "Canvas接口查询：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			if (rs.next()) {
				canvasOper = rs.getString("MG_CANVASOPER");
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("Canvas接口查询，数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}


		if (canvasOper != null && canvasOper.length() > 0)
			return returnSuccMsg("String", canvasOper);
		else
			return returnErrMsg("Canvas接口查询，获取数据为空！");

	}



}
