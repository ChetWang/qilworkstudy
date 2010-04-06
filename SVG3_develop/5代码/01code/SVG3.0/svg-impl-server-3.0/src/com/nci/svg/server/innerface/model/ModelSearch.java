package com.nci.svg.server.innerface.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ModelActionBean;
import com.nci.svg.sdk.bean.ModelBean;
import com.nci.svg.sdk.bean.ModelPropertyBean;
import com.nci.svg.sdk.bean.ModelTypeBean;
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
 * 标题：ModelSearch.java
 * </p>
 * <p>
 * 描述： 业务模型查询类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-03-20
 * @version 1.0
 */
public class ModelSearch extends OperationServiceModuleAdapter {
	private static final long serialVersionUID = -2684072849016326447L;

	public ModelSearch(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("业务模型，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("业务模型，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "业务模型管理类，获取到‘" + actionName
				+ "’请求命令！");
		ResultBean rb = null;

		if (actionName.equalsIgnoreCase(ActionNames.GET_BUSINESS_MODEL2)) {
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			rb = getModelMaps(businessID);
		}

		return rb;
	}

	/**
	 * 2009-3-23 Add by ZHM
	 * 
	 * @param businessID:String:业务系统编号
	 * @return
	 */
	private ResultBean getModelMaps(String businessID) {
		ResultBean resultBean = null;
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取模型信息！");
			return returnErrMsg("获取模型信息，无法获得有效数据库连接！");
		}

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT *").append(" FROM t_view_svg_datamodel").append(
				" WHERE modelname in (SELECT mb_modelid").append(
				" FROM t_svg_model_businessrela").append(
				" WHERE mb_businessid = '").append(businessID).append(
				"' AND mb_validity = '1')");

		HashMap map = new HashMap();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取业务模型基础信息：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			ModelBean bean = null;
			while (rs.next()) {
				bean = new ModelBean();
				String id = rs.getString("modelname");
				String type = ModelTypeBean.CUSTOM_MODEL;
				String shortname = rs.getString("cls_uri");
				String name = rs.getString("description");
				String behavior = rs.getString("behavior");
				bean.getTypeBean().setId(id);
				bean.getTypeBean().setType(type);
				bean.getTypeBean().setShortName(shortname);
				bean.getTypeBean().setName(name);

				// 获取业务模型动作定义信息
				if (behavior != null && behavior.length() > 0) {
					resultBean = loadModelActions(conn, bean, behavior);
					if (resultBean.getReturnFlag() == ResultBean.RETURN_ERROR)
						return resultBean;
					else
						bean.setActions((HashMap) resultBean.getReturnObj());
				}

				// 添加业务模型对象
				map.put(id, bean);
			}
			if (rs != null)
				rs.close();

			// 获取业务模型属性定义信息
			resultBean = loadModelProperties(conn, map, businessID);

		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取模型信息，数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}
		return resultBean;
	}

	/**
	 * 2009-3-29 Add by ZHM 获取业务模型的动作定义信息
	 * 
	 * @param conn:Connection:数据库连接
	 * @param map:HashMap:业务模型表
	 * @param businessID:String:业务系统编号
	 * @return
	 */
	private ResultBean loadModelActions(Connection conn, ModelBean modelBean,
			String behavior) {
		ResultBean resultBean = null;
		HashMap map = new HashMap();
		String modelname = modelBean.getTypeBean().getId(); // 业务模型短名
		// 获取业务模型关联的动作
		String[] behaviorID = behavior.split(",");

		// 获取业务模型动作缓存池
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return returnErrMsg("获取模型动作信息，获取业务模型动作缓存池失败！");
		}
		CacheObject co = cma
				.getCacheObj(CacheManagerKeys.MODEL_ACTIONS_MANAGER);
		Map actionMap = co.getCacheMap();
		ModelActionBean bean = null;
		for (int i = 0, size = behaviorID.length; i < size; i++) {
			Object obj = actionMap.get(behaviorID[i]);
			if (obj instanceof ModelActionBean) {
				ModelActionBean tempBean = (ModelActionBean) obj;
				bean = new ModelActionBean();
				bean.setId(tempBean.getId());
				bean.setContent(tempBean.getContent());
				bean.setModelID(modelname);
				bean.setName(tempBean.getName());
				bean.setShortName(tempBean.getShortName());
				bean.setType(tempBean.getType());
				map.put(bean.getId(), bean);
			}
		}

		resultBean = new ResultBean(ResultBean.RETURN_SUCCESS, null, "HashMap",
				map);
		return resultBean;
	}

	/**
	 * 2009-3-24 Add by ZHM 获取业务模型的属性定义信息
	 * 
	 * @param conn:Connection:数据库连接
	 * @param map:HashMap:业务模型表
	 * @param businessID:String:业务系统编号
	 * @return
	 */
	private ResultBean loadModelProperties(Connection conn, HashMap map,
			String businessID) {
		ResultBean resultBean = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM t_view_svg_datamodel_field").append(
				" ORDER BY modelname");
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取业务模型基础信息：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			ModelBean bean = null;
			ModelPropertyBean propertyBean = null;
			while (rs.next()) {
				String modelname = rs.getString("modelname");
				bean = (ModelBean) map.get(modelname);
				if (bean != null) {
					String fieldname = rs.getString("fieldname");
					String id = modelname + "_" + fieldname;
					String name = rs.getString("displayname");
					String type = rs.getString("cat_id");
					String code = rs.getString("map_propname");
					String shortName = rs.getString("fieldname");
					String visible = "visible";
					HashMap propertyMap = (HashMap) bean.getProperties().get(
							type);
					if (propertyMap == null) {
						propertyMap = new HashMap();
						bean.getProperties().put(type, propertyMap);
					}
					propertyBean = new ModelPropertyBean();
					propertyBean.setId(id);
					propertyBean.setModelID(modelname);
					propertyBean.setName(name);
					propertyBean.setCode(code);
					propertyBean.setType(type);
					propertyBean.setShortName(shortName);
					propertyBean.setVisible(visible);
					propertyMap.put(id, propertyBean);
				}
			}

			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取模型属性信息，数据库操作失败！");
		}
		resultBean = new ResultBean(ResultBean.RETURN_SUCCESS, null, "HashMap",
				map);
		return resultBean;
	}

}
