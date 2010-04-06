/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-19
 * @功能：模型管理类
 *
 */
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
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;

/**
 * @author yx.nci
 * 
 */
public class ModelModule extends OperationServiceModuleAdapter {
	private static final long serialVersionUID = 5130170994370301632L;

	public ModelModule(HashMap parameters) {
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
			return returnErrMsg("业务模型，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("业务模型，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "业务模型管理类，获取到‘" + actionName
				+ "’请求命令！");

		ResultBean rb = null;
		if (actionName.equalsIgnoreCase(ActionNames.GET_BUSINESS_MODEL)) {
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			rb = getModelMaps(businessID);
		}
		return rb;
	}

	/**
	 * add by yux,2009-1-19 根据输入的业务系统编号，获取该业务系统所支持的所有业务模型
	 * 
	 * @param businessID：业务系统编号
	 * @return：结果
	 */
	private ResultBean getModelMaps(String businessID) {
		ResultBean resultBean = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT b.* ").append(
				"FROM t_svg_model_businessrela a,t_svg_model_summary b ")
				.append("WHERE a.mb_modelid = b.ms_id AND a.mb_businessid = '")
				.append(businessID).append("'");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取模型信息！");
			return returnErrMsg("获取模型信息，无法获得有效数据库连接！");
		}

		HashMap map = new HashMap();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取模型种类信息：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			ModelBean bean = null;
			String shortName = null;
			while (rs.next()) {
				bean = new ModelBean();
				shortName = rs.getString("MS_SHORTNAME");
				bean.getTypeBean().setId(rs.getString("MS_ID"));
				bean.getTypeBean().setType(rs.getString("MS_TYPE"));
				bean.getTypeBean().setShortName(shortName);
				bean.getTypeBean().setName(rs.getString("MS_NAME"));
				map.put(bean.getTypeBean().getId(), bean);
			}
			if (rs != null)
				rs.close();

			loadModelProperties(conn, map, businessID);
			loadModelActions(conn, map, businessID);
			resultBean = new ResultBean(ResultBean.RETURN_SUCCESS, null,
					"HashMap", map);

		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取模型信息，数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}
		return resultBean;
	}

	private void loadModelProperties(Connection conn, HashMap map,
			String businessID) {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT c.*,d.ms_id FROM t_svg_model_propertys c,t_svg_model_summary d  ")
				.append("WHERE c.mp_modelid = d.ms_id AND c.mp_modelid IN ")
				.append("( ")
				.append(
						"SELECT b.ms_id FROM t_svg_model_businessrela a,t_svg_model_summary b")
				.append(" WHERE a.mb_modelid = b.ms_id AND a.mb_businessid = '")
				.append(businessID)
				.append("') ORDER BY c.mp_modelid,c.mp_type");
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取模型属性信息：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			ModelBean bean = null;
			String id = null;
			String type = null;
			while (rs.next()) {

				id = rs.getString("MS_ID");
				bean = (ModelBean) map.get(id);
				if (bean != null) {
					type = rs.getString("MP_TYPE");
					HashMap propertyMap = (HashMap) bean.getProperties().get(
							type);
					if (propertyMap == null) {
						propertyMap = new HashMap();
						bean.getProperties().put(type, propertyMap);
					}
					ModelPropertyBean propertyBean = new ModelPropertyBean();
					propertyBean.setId(rs.getString("MP_ID"));
					propertyBean.setModelID(rs.getString("MP_MODELID"));
					propertyBean.setShortName(rs.getString("MP_SHORTNAME"));
					propertyBean.setName(rs.getString("MP_NAME"));
					propertyBean.setType(type);
					propertyBean.setCode(rs.getString("MP_CODE"));
					propertyBean.setVisible(rs.getString("MP_VISIBLE"));
					propertyMap.put(propertyBean.getId(), propertyBean);
				}
			}
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		}
		return;
	}

	private void loadModelActions(Connection conn, HashMap map,
			String businessID) {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT c.*,d.ms_id FROM t_svg_model_actions c,t_svg_model_summary d,t_svg_model_actionrelasys e ")
				.append(
						"WHERE c.ma_modelid = d.ms_id AND c.ma_id = e.as_actionid AND c.ma_modelid IN ")
				.append("( ")
				.append(
						"SELECT b.ms_id FROM t_svg_model_businessrela a,t_svg_model_summary b")
				.append(" WHERE a.mb_modelid = b.ms_id AND a.mb_businessid = '")
				.append(businessID).append("') ").append(
						"AND e.as_businessid='").append(businessID).append(
						"' ORDER BY c.ma_modelid,c.ma_type");
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取模型属性信息：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			ModelBean bean = null;
			String id = null;
			String type = null;
			while (rs.next()) {

				id = rs.getString("MS_ID");
				bean = (ModelBean) map.get(id);
				if (bean != null) {
					type = rs.getString("MA_TYPE");
					HashMap actionMap = (HashMap) bean.getActions().get(type);
					if (actionMap == null) {
						actionMap = new HashMap();
						bean.getActions().put(type, actionMap);
					}
					ModelActionBean actionBean = new ModelActionBean();
					actionBean.setId(rs.getString("MA_ID"));
					actionBean.setModelID(rs.getString("MA_MODELID"));
					actionBean.setShortName(rs.getString("MA_SHORTNAME"));
					actionBean.setName(rs.getString("MA_NAME"));
					actionBean.setType(type);
					actionBean.setContent(rs.getString("MA_CONTENT"));
					actionMap.put(actionBean.getId(), actionBean);
				}
			}
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		}
		return;
	}
}
