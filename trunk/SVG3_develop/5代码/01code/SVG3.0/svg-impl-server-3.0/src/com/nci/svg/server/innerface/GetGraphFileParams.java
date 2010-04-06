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
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2008-12-26
 * @功能：获取对应的业务系统中对应的业务图类型的参数信息
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
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("获取业务图类型参数，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("获取业务图类型参数，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "获取业务图类型参数，获取到‘" + actionName + "’请求命令！");

		ResultBean rb = new ResultBean();
		if (actionName.equalsIgnoreCase(ActionNames.GET_GRAPHFILE_PARAMS)) {
			String bussID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			String graphType = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_TYPE);
			
			rb = getGraphFileParams(bussID, graphType);
		} else {
			returnErrMsg(actionName + "命令，目前该请求未实现!");
		}
		return rb;
	}

	/**
	 * 获取指定图形文件参数信息
	 * 
	 * @param bussID:String:业务系统编号
	 * @param graphType:String:图形类型
	 * @return
	 */
	private ResultBean getGraphFileParams(String bussID, String graphType) {
		String rela = controller.getGraphStorageManager().getRelateString(
				bussID);
		if (rela == null || rela.length() == 0)
			return returnErrMsg("获取指定图形文件参数信息，无法获取图库标识！");

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM t_svg_rep_fieldnote_").append(rela);
		if (graphType != null && graphType.length() > 0)
			sql.append(" WHERE RF_GRAPH_BUSINESS_TYPE = '").append(graphType)
					.append("'");
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法获取代码列表！");
			return returnErrMsg("获取指定图形文件参数信息，无法获取有效数据库连接！");
		}
		GraphFileParamsBean beans = null;
		ArrayList list = new ArrayList();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取图类型参数：" + sql.toString());
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
			return returnErrMsg("获取指定图形文件参数信息，数据库操作失败！");
			// list = null;
		} finally {
			controller.getDBManager().close(conn);
		}

		list.trimToSize();
		if (list.size() > 0)
			return returnSuccMsg("ArrayList", list);
		else
			return returnErrMsg("获取指定图形文件参数信息，获取数据为空！");
	}

}
