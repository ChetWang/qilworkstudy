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
 * 标题：ClientUpgrade.java
 * </p>
 * <p>
 * 描述： 客户端升级
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-01
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
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("客户端升级功能，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("客户端升级功能，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "客户端升级功能，获取到‘" + actionName + "’请求命令！");

		ResultBean rb = new ResultBean();
		if (actionName.equalsIgnoreCase(ActionNames.GET_UPGRADE_MODULE)) {
			// *******************
			// 获取客户端组件版本信息
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
			// rb.setErrorText("客户端升级功能，无法获取指定数据！");
			// }
		} else if (actionName.equalsIgnoreCase(ActionNames.DOWN_UPGRADE_MODULE)) {
			// ****************
			// 获取客户端组件内容
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
//				rb.setErrorText("获取客户端组件内容，操作数据库失败！");
//			}
		} else {
			rb = returnErrMsg(actionName + "命令，目前该请求未实现!");
		}

		return rb;
	}

	/**
	 * 获取指定类型的客户端组件清单
	 * 
	 * @param moduleType
	 *            String 组件类型，分为平台组件和业务组件， 当组件类型为空时返回全部客户端组件的版本信息
	 * @return 组件清单
	 */
	private ResultBean getUpgradeModule(String moduleType) {
		// sql语句
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
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获得客户端组件清单！");
			return returnErrMsg("获取客户端组件清单，无法获取有效数据库连接！");
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取客户端组件清单：" + sql.toString());
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
			return returnErrMsg("获取客户端组件清单，数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}

		moduleList.trimToSize();

		if (moduleList.size() > 0)
			return returnSuccMsg("ArrayList", moduleList);
		else
			return returnErrMsg("获取客户端组件清单，获取数据为空！");
	}

	/**
	 * 获取指定组件
	 * 
	 * @param moduleShortName
	 *            String 组件名称
	 * @return 组件内容
	 */
	private ResultBean downloadUpgradeModule(String moduleShortName) {
		byte[] content = null;
		// 获取数据库类型
		String type = controller.getDBManager().getDBType("SVG");
		// sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ml_content FROM t_svg_module_list").append(
				" WHERE ml_shortname = '").append(moduleShortName).append("'");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获得客户端组件内容！");
			return returnErrMsg("获取指定组件内容，无法获取有效数据库连接！");
		}
		if (type != null) {
			// 通过方言转换类获取blob字段
			DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
					.getDBSqlIdiom(type);
			// 获取组件内容
			if (sqlIdiom != null) {
				content = sqlIdiom.getBlob(conn, sql.toString(), "ml_content");
			}
			// 关闭数据库连接
			controller.getDBManager().close(conn);
		}
		
		if(content!=null && content.length>0)
			return returnSuccMsg("byte[]", content);
		else
			return returnErrMsg("获取指定组件内容，获取内容为空！");
	}

}
