package com.nci.svg.server.innerface.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ModelActionBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.cache.CacheManagerAdapter;
import com.nci.svg.sdk.server.cache.CacheObject;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.cache.CacheManagerKeys;

/**
 * <p>
 * 标题：CacheManagerModelActions.java
 * </p>
 * <p>
 * 描述： 业务模型动作缓存管理服务类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-18
 * @version 1.0
 */
public class CacheManagerModelActions extends OperationServiceModuleAdapter {
	private static final long serialVersionUID = -5341715339507227058L;
	/**
	 * 初始化命令
	 */
	private final String INIT = "init";
	/**
	 * 查询业务模型动作
	 */
	private final String SEARCH_MODEL_ACTIONS = "searchModelActions";

	public CacheManagerModelActions(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("业务模型动作缓存管理，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("业务模型动作缓存管理，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "业务模型动作缓存管理类，获取到‘" + actionName
				+ "’请求命令！");

		String subAction = (String) getRequestParameter(requestParams,
				ActionParams.SUB_ACTION);
		ResultBean rb = new ResultBean();
		if (subAction.equalsIgnoreCase(INIT)) {
			// *************
			// 初始化代码缓存
			// *************
			int result = init();
			if (result == OPER_ERROR) {
				rb.setErrorText("未能成功运行业务模型动作缓存管理！");
				rb.setReturnFlag(ResultBean.RETURN_ERROR);
			} else {
				rb.setReturnFlag(ResultBean.RETURN_SUCCESS);
				rb.setReturnObj("成功运行业务模型动作缓存管理！");
				rb.setReturnType("String");
			}
		} else if (subAction.equalsIgnoreCase(SEARCH_MODEL_ACTIONS)) {
			// *************
			// 
			// *************
		}

		return rb;
	}

	public int init() {
		if (controller == null)
			return OPER_ERROR;
		// ****************
		// 将业务模型动作信息载入缓存
		// ****************
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return OPER_ERROR;
		}

		Map codeHash = getActionsMessage();
		cma.addCacheInnerData(CacheObject.MAP_FLAG,
				CacheManagerKeys.MODEL_ACTIONS_MANAGER, codeHash);

		return OPER_SUCCESS;
	}

	/**
	 * 2009-3-30 Add by ZHM 获取所有的业务模型动作信息到缓存中
	 * 
	 * @return
	 */
	private Map getActionsMessage() {
		Map actionMap = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from t_view_svg_datamodel_behavior");
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法获取业务模型动作列表！");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取业务模型动作列表：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			ModelActionBean bean = null;
			while (rs.next()) {
				String id = rs.getString("seqkey");
				String description = rs.getString("description");
				String name = rs.getString("name");
				String type = rs.getString("type");
				String content = rs.getString("url");

				bean = new ModelActionBean();
				bean.setId(id);
				bean.setShortName(description);
				bean.setName(name);
				bean.setType(type);
				bean.setContent(content);
				actionMap.put(id, bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		} finally {
			controller.getDBManager().close(conn);
		}
		return actionMap;
	}
}
