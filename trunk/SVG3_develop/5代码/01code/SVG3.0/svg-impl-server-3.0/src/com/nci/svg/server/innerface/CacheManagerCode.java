package com.nci.svg.server.innerface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.cache.CacheManagerAdapter;
import com.nci.svg.sdk.server.cache.CacheObject;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.cache.CacheManagerKeys;

/**
 * <p>
 * 标题：CacheManagerCode.java
 * </p>
 * <p>
 * 描述： 代码缓存管理服务类
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
public class CacheManagerCode extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9080935259363105467L;

	/**
	 * 初始化命令
	 */
	private final String INIT = "init";
	/**
	 * 查询指定代码
	 */
	private final String SEARCH = "searchCode";
	/**
	 * 维护指定代码
	 */
	private final String MODIFY_CODE_CACHE = "modifyCodeCache";

	public CacheManagerCode(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("代码缓存管理，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("代码缓存管理，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "代码缓存管理类，获取到‘" + actionName + "’请求命令！");

		String subAction = (String) getRequestParameter(requestParams,
		          ActionParams.SUB_ACTION);
		ResultBean rb = new ResultBean();
		if (subAction.equalsIgnoreCase(INIT)) {
			// *************
			// 初始化代码缓存
			// *************
			int result = init();
			if (result == OPER_ERROR) {
				rb.setErrorText("未能成功运行代码缓存管理！");
				rb.setReturnFlag(ResultBean.RETURN_ERROR);
			} else {
				rb.setReturnFlag(ResultBean.RETURN_SUCCESS);
				rb.setReturnObj("成功运行代码缓存管理！");
				rb.setReturnType("String");
			}
		} else if (subAction.equalsIgnoreCase(SEARCH)) {
			// **************
			// 查询指定代码信息
			// **************
		} else if (subAction.equalsIgnoreCase(MODIFY_CODE_CACHE)) {
			// **************
			// 维护指定代码信息
			// **************
			String codeName = (String) getRequestParameter(requestParams,
					"codename");
			int result = manageCodeCache(codeName);
			if (result == OPER_ERROR) {
				rb.setErrorText("未能成功运行代码缓存管理！");
				rb.setReturnFlag(ResultBean.RETURN_ERROR);
			} else {
				rb.setReturnFlag(ResultBean.RETURN_SUCCESS);
				rb.setReturnObj("成功运行代码缓存管理！");
				rb.setReturnType("String");
			}
		} else {
			rb.setReturnFlag(ResultBean.RETURN_ERROR);
			rb.setErrorText(actionName + "命令:子命令" + subAction + "，目前该请求未实现!");
		}
		return rb;
	}

	/**
	 * 增加指定代码信息缓存操作函数
	 * 
	 * @param codeName
	 *            String 代码短名
	 * @return
	 */
	private int manageCodeCache(String codeName) {
		if (codeName == null || codeName.length() <= 0) {
			// 判断输入代码短名是否合法
			return OPER_ERROR;
		}
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			// 判断是否获得缓存管理器
			return OPER_ERROR;
		}

		// 获取代码缓存
		Map cacheHash = cma.getCacheObj(CacheManagerKeys.CODE_MANAGER)
				.getCacheMap();
		if (cacheHash == null) {
			return OPER_ERROR;
		}
		// 获取指定短名的代码缓存
		Map moduleHash = (Map) cacheHash.get(codeName);
		// 测试
		// Untilities.displayCodeHash(cacheHash);
		// 从数据库中获取指定短名的代码信息
		Map dbHash = (Map) getCodeMessage(codeName).get(codeName);

		if (moduleHash != null && dbHash != null) {
			// 原缓存数据存在，查询代码信息存在（修改）
			cacheHash.put(codeName, dbHash);
		} else if (moduleHash != null && dbHash == null) {
			// 原缓存数据存在，查询代码信息不存在（删除）
			cacheHash.remove(codeName);
		} else if (moduleHash == null && dbHash != null) {
			// 原缓存数据不存在，查询代码信息存在（新增）
			cacheHash.put(codeName, dbHash);
		}
		// 打印代码哈希表数据
		// Untilities.displayCodeHash(cacheHash);
		cma.setCacheObj(CacheObject.MAP_FLAG, CacheManagerKeys.CODE_MANAGER,
				cacheHash);
		return OPER_SUCCESS;
	}

	public int init() {
		if (controller == null)
			return OPER_ERROR;
		// ****************
		// 将代码信息载入缓存
		// ****************
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return OPER_ERROR;
		}

		Map codeHash = getCodeMessage(null);
		// 打印代码哈希表数据
//		Untilities.displayCodeHash(codeHash);
		cma.addCacheInnerData(CacheObject.MAP_FLAG,
				CacheManagerKeys.CODE_MANAGER, codeHash);

		return OPER_SUCCESS;
	}

	/**
	 * 根据模型名称获取代码列表
	 * 
	 * @param codeName
	 *            String 模型名称
	 * @return 代码信息列表
	 */
	private Map getCodeMessage(String codeName) {
		Map codeHash = new HashMap();
		// sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.*, cm_parentshortname").append(
				" FROM t_svg_code_commsets a, t_svg_code_manager b").append(
				" WHERE a.cc_shortname = b.cm_shortname");
		if (codeName != null && codeName.length() > 0) {
			sql.append(" AND cc_shortname = '").append(codeName).append("'");
		}
		sql.append(" ORDER BY cc_shortname");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法获取代码列表！");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取代码列表：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			Map subHash = null;
			CodeInfoBean bean;
			String shortname = "";
			while (rs.next()) {
				String temp = rs.getString("cc_shortname");
				if (!temp.equals(shortname)) {
					if (subHash != null)
						codeHash.put(shortname, subHash);
					subHash = new HashMap();
					shortname = temp;
				}
				bean = new CodeInfoBean();
				bean.setShortName(temp);
				bean.setValue(rs.getString("cc_code"));
				bean.setName(rs.getString("cc_name"));
				bean.setSpell(rs.getString("cc_spell"));
				bean.setDesc(rs.getString("cc_desc"));
				bean.setParentShortName(rs.getString("cm_parentshortname"));
				bean.setParentValue(rs.getString("cc_parent_code"));
				bean.setParam1(rs.getString("cc_param1"));
				bean.setParam2(rs.getString("cc_param2"));
				bean.setParam3(rs.getString("cc_param3"));
				bean.setParam4(rs.getString("cc_param4"));
				bean.setParam5(rs.getString("cc_param5"));
				bean.setCc_id(rs.getString("cc_id"));
				subHash.put(bean.getValue(), bean);
			}
			if (subHash != null)
				codeHash.put(shortname, subHash);
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		} finally {
			controller.getDBManager().close(conn);
		}

		return codeHash;
	}
}
