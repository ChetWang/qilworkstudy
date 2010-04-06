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
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2008-12-22
 * @功能：获取系统配置参数
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
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("获取系统配置参数，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("获取系统配置参数，未能获取日志操作对象!");
		}
		log.log(this, LoggerAdapter.DEBUG, "获取系统配置参数功能，获取到‘" + actionName
				+ "’请求命令！");

		ResultBean rb = new ResultBean();
		// 判断请求命令是否为getSvgCodes
		if (actionName.equalsIgnoreCase(ActionNames.GET_SYSSETS)) {
			String shortName = (String) getRequestParameter(requestParams,
					ActionParams.SHORT_NAME);
			rb = getSysSets(shortName);
		} else {
			rb = returnErrMsg(actionName + "命令，目前该请求未实现!");
		}
		return rb;
	}

	/**
	 * 获取指定业务系统支持的图形文件类型
	 * 
	 * @param bussID
	 *            String 业务系统编号
	 * @return
	 */
	protected ResultBean getSysSets(String shortName) {
		// sql语句
		StringBuffer sql = new StringBuffer();
		if (shortName != null && shortName.length() != 0) {
			sql.append("SELECT ss_id, ss_short_name,ss_name,").append(
					"ss_desc,ss_param1,ss_param2,ss_param3 FROM").append(
					" t_svg_sys_sets t ").append(" WHERE ss_short_name = '")
					.append(shortName).append("'");
		} else {
			return returnErrMsg("获取指定业务系统支持的图形文件类型，业务系统短名为空！");
		}

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法获取系统配置参数！");
			return returnErrMsg("获取指定业务系统支持的图形文件类型，数据库连接获取失败！");
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
			return returnErrMsg("获取指定业务系统支持的图形文件类型,数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}

		if (bean == null) {
			return returnErrMsg("获取指定业务系统支持的图形文件类型，获取数据库数据为空！");
		} else {
			return returnSuccMsg("SysSetBean", bean);
		}
	}

}
