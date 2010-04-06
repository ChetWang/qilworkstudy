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
 * 标题：CodeSearch.java
 * </p>
 * <p>
 * 描述： 代码查询
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
public class CodeSearch extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4422712116862921079L;

	public CodeSearch(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("代码查询功能，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("代码查询功能，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "代码查询功能，获取到‘" + actionName
				+ "’请求命令！");
		log.log(this, LoggerAdapter.DEBUG, "代码查询功能，获取到‘" + actionName
				+ "’请求命令！", true);
		ResultBean rb = new ResultBean();
		if (actionName.equalsIgnoreCase(ActionNames.GET_SVG_CODES)) {
			// ***********
			// 获取代码列表
			// ***********
			String moduleName = (String) getRequestParameter(requestParams,
					ActionParams.MODULE_NAME);
			String expr = (String) getRequestParameter(requestParams,
					ActionParams.EXPRSTRING);
			rb = getCodeMessage(moduleName, expr);
		} else {
			rb = returnErrMsg(actionName + "命令，目前该请求未实现!");
		}

		return rb;
	}

	/**
	 * 根据模型名称获取代码列表，如果模型名为空则返回全部代码信息
	 * 
	 * @param moduleName
	 *            String 模型名称
	 * @return 代码信息列表
	 */
	private ResultBean getCodeMessage(String moduleName, String expr) {
		Map codeMap = null;
		if (expr == null || expr.length() == 0) {
			// 无过滤条件
			CacheManagerAdapter cma = controller.getCacheManager();
			if (cma == null) {
				return returnErrMsg("获取代码列表，无法获取缓存管理器！");
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

				// 获取数据库连接
				Connection conn = controller.getDBManager()
						.getConnection("svg");
				if (conn == null) {
					log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取模型信息！");
					return returnErrMsg("获取模型信息，无法获得有效数据库连接！");
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
		return returnErrMsg("无符合条件的代码集");
	}
}
