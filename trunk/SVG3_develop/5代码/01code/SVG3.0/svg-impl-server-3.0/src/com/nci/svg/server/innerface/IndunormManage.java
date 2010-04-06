package com.nci.svg.server.innerface;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.RelaIndunormDescBean;
import com.nci.svg.sdk.bean.RelaIndunormGraphBean;
import com.nci.svg.sdk.bean.RelaIndunormModelBean;
import com.nci.svg.sdk.bean.RelaIndunormSymbolBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.bean.SimpleIndunormBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;

/**
 * <p>
 * 标题：IndunormManage.java
 * </p>
 * <p>
 * 描述：行业规范管理类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-24
 * @version 1.0
 */
public class IndunormManage extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8440037527533284260L;

	public IndunormManage(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("行业规范管理，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("行业规范管理，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "行业规范管理类，获取到‘" + actionName
				+ "’请求命令！");

		ResultBean rb = new ResultBean();
		if (actionName
				.equalsIgnoreCase(ActionNames.GET_INDUNORM_GRAPHTYPE_RELA)) {
			// ********************
			// 获取规范与图类型关联信息
			// ********************
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			String businessType = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_TYPE);

			rb = getIndunormGraphTypeRela(businessID, businessType);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.GET_INDUNORM_DESC_RELA)) {
			// ***********************
			// 获取图类型与规范描述关联信息
			// ***********************
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			String businessType = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_TYPE);

			rb = getIndunormDescRela(businessID, businessType);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.GET_INDUNORM_FIELD_SYMBOL_RELA)) {
			// ************************
			// 获取规范数据域与图元关联信息
			// ************************
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			String businessType = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_TYPE);
			String symbolType = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_TYPE);
			String symbolID = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_ID);

			rb = getIndunormFieldSymbolRela(businessID, businessType,
					symbolType, symbolID);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.GET_INDUNORM_MODEL_RELA)) {
			// **********************
			// 获取规范与业务模型关联信息
			// **********************
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			String modelID = (String) getRequestParameter(requestParams,
					ActionParams.MODEL_ID);
			String normID = (String) getRequestParameter(requestParams,
					ActionParams.INDUNORM_ID);

			rb = getIndunormModelRela(businessID, modelID, normID);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.GET_SIMPLE_INDUNORM_LIST)) {
			// **********************
			// 获取当前业务系统下的业务规范简易列表
			// **********************
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			rb = getSimpleIndunormList(businessID);
		} else {
			rb = returnErrMsg(actionName + "命令，目前该请求未实现!");
		}
		return rb;
	}

	/**
	 * 获取规范与图类型关联信息
	 * 
	 * @param businessID:String:系统编号
	 * @param graphType:String:图类型
	 * @return 返回指定图类型与规范关联信息 系统编号和图类型为过滤条件，当其为空时则不过滤
	 */
	private ResultBean getIndunormGraphTypeRela(String businessID,
			String graphType) {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT a.*,b.IG_ID,b.ig_businesssys_id,").append(
				"b.ig_graph_business_type ").append(
				"FROM t_svg_indunorm_type a,").append(
				"t_svg_indunorm_graphtyperela b ").append(
				"WHERE a.it_shortname = b.ig_norm_type");
		if (businessID != null && businessID.length() > 0) {
			sql.append(" AND b.ig_businesssys_id = '").append(businessID)
					.append("'");
		}
		if (graphType != null && graphType.length() > 0) {
			sql.append(" AND b.ig_graph_business_type = '").append(graphType)
					.append("'");
		}
		sql.append(" ORDER BY b.ig_graph_business_type");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取规范与图类型关联信息！");
			return returnErrMsg("获取规范与图类型关联信息，无法获得有效数据库连接！");
		}

		ArrayList relaList = new ArrayList();
		try {
			Statement st = conn.createStatement();
			log
					.log(this, LoggerAdapter.DEBUG, "获取规范与图类型关联信息："
							+ sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			RelaIndunormGraphBean relaBean;
			String code, id;
			while (rs.next()) {
				relaBean = new RelaIndunormGraphBean();
				code = rs.getString("ig_businesssys_id");
				relaBean.getBussBean().setCode(code);
				relaBean.getBussBean().setName(
						getCodeName("SVG_BUSINESS_ID", code));
				code = rs.getString("ig_graph_business_type");
				relaBean.getGraphTypeBean().setCode(code);
				relaBean.getGraphTypeBean().setName(
						getCodeName("SVG_BUSINESS_GRAPH_TYPE", code));
				relaBean.getTypeBean().setShortName(
						rs.getString("IT_SHORTNAME"));
				relaBean.getTypeBean().setName(rs.getString("IT_NAME"));
				relaBean.getTypeBean().setQuote(rs.getString("IT_QUOTE"));
				relaBean.getTypeBean().setDesc(rs.getString("IT_DESC"));
				id = rs.getString("IG_ID");
				DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
						.getDBSqlIdiom(
								controller.getDBManager().getDBType("SVG"));
				sql = new StringBuffer();
				sql
						.append(
								"SELECT ig_defs FROM t_svg_indunorm_graphtyperela WHERE ig_id = '")
						.append(id).append("'");
				byte[] content = sqlIdiom.getBlob(conn, sql.toString(),
						"IG_DEFS");
				if (content != null && content.length > 0)
					relaBean.setDefs(new String(content, "UTF-8"));
				relaList.add(relaBean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取规范与图类型关联信息，数据库操作失败！");
		} catch (UnsupportedEncodingException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取规范与图类型关联信息，字符集转换成字符串失败！");
		} finally {
			controller.getDBManager().close(conn);
		}
		relaList.trimToSize();
		if (relaList.size() > 0)
			return returnSuccMsg("ArrayList", relaList);
		else
			return returnErrMsg("获取规范与图类型关联信息，获取数据为空！");
	}

	/**
	 * 获取图类型与规范描述关联信息
	 * 
	 * @param businessID:String:系统编号
	 * @param graphType:String:图类型
	 * @return 返回指定图类型与规范描述关联信息 系统编号和图类型为过滤条件，当其为空时则不过滤
	 */
	private ResultBean getIndunormDescRela(String businessID, String graphType) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.id_businesssys_id,").append(
				"a.id_graph_business_type,b.*,c.*").append(
				"FROM t_svg_indunorm_descrela a,").append(
				"t_svg_indunorm_desc b,t_svg_indunorm_type c").append(
				" WHERE a.id_norm_desc_id = b.id_id").append(
				" and b.id_variety = c.it_shortname");
		if (businessID != null && businessID.length() > 0) {
			sql.append(" AND a.id_businesssys_id = '").append(businessID)
					.append("'");
		}
		if (graphType != null && graphType.length() > 0) {
			sql.append(" AND a.id_graph_business_type = '").append(graphType)
					.append("'");
		}
		sql.append(" ORDER BY a.id_graph_business_type");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取图类型与规范描述关联信息！");
			return returnErrMsg("获取图类型与规范描述关联信息，无法获取有效数据库连接！");
		}

		ArrayList relaList = new ArrayList();
		try {
			String code;
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取图类型与规范描述关联信息："
					+ sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			RelaIndunormDescBean relaBean;
			while (rs.next()) {
				relaBean = new RelaIndunormDescBean();
				code = rs.getString("id_businesssys_id");
				relaBean.getBussBean().setCode(code);
				relaBean.getBussBean().setName(
						getCodeName("SVG_BUSINESS_ID", code));
				code = rs.getString("id_graph_business_type");
				relaBean.getGraphTypeBean().setCode(code);
				relaBean.getGraphTypeBean().setName(
						getCodeName("SVG_BUSINESS_GRAPH_TYPE", code));
				relaBean.getTypeBean().setShortName(
						rs.getString("IT_SHORTNAME"));
				relaBean.getTypeBean().setName(rs.getString("IT_NAME"));
				relaBean.getTypeBean().setQuote(rs.getString("IT_QUOTE"));
				relaBean.getTypeBean().setDesc(rs.getString("IT_DESC"));

				relaBean.getDescBean().setId(rs.getString("ID_ID"));
				relaBean.getDescBean().setShortName(
						rs.getString("ID_SHORT_NAME"));
				relaBean.getDescBean().setName(rs.getString("ID_NAME"));
				relaBean.getDescBean().setVariety(rs.getString("ID_VARIETY"));
				relaBean.getDescBean().setDesc(rs.getString("ID_DESC"));

				relaList.add(relaBean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取图类型与规范描述关联信息，数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}
		relaList.trimToSize();

		if (relaList.size() > 0)
			return returnSuccMsg("ArrayList", relaList);
		else
			return returnErrMsg("获取图类型与规范描述关联信息，获取数据为空！");
	}

	/**
	 * 获取规范数据域与图元关联信息
	 * 
	 * @param symbolID:String:图元编号（包含模板编号）
	 * @param dataFieldID:String:数据域编号
	 * @return 返回指定图元与规范关联信息，图元编号与数据域编号为过滤条件，如果为空则不过滤
	 */
	private ResultBean getIndunormFieldSymbolRela(String bussID,
			String graphFileType, String symbolType, String symbolID) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT d.IT_SHORTNAME,c.IM_SHORT_NAME,").append(
				"b.IF_SHORT_NAME FROM ").append(
				"T_SVG_INDUNORM_SYMBOLFIELDRE a,").append(
				"T_SVG_INDUNORM_FIELDS b,").append(
				"T_SVG_INDUNORM_METADATAS c,").append("T_SVG_INDUNORM_TYPE d,")
				.append("T_SVG_INDUNORM_GRAPHTYPERELA e ").append(
						"WHERE a.IS_DATAFIELD_ID = b.IF_ID ").append(
						"AND b.IF_VARIETY = c.IM_ID ").append(
						"AND c.IM_VARIETY = d.IT_SHORTNAME ").append(
						"AND d.IT_SHORTNAME = e.IG_NORM_TYPE ").append(
						"AND e.IG_BUSINESSSYS_ID = '").append(bussID).append(
						"' AND e.IG_GRAPH_BUSINESS_TYPE = '").append(
						graphFileType).append("' AND a.IS_SYMBOL_TYPE = '")
				.append(symbolType).append("' AND a.IS_SYMBOL_ID = '").append(
						symbolID).append("' ORDER BY d.IT_SHORTNAME,").append(
						"c.IM_SHORT_NAME,b.IF_SHORT_NAME");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取图类型与规范描述关联信息！");
			return returnErrMsg("获取规范数据域与图元关联信息，无法获取有效数据库连接！");
		}

		ArrayList relaList = new ArrayList();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取图类型与规范描述关联信息："
					+ sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			RelaIndunormSymbolBean relaBean;
			while (rs.next()) {
				relaBean = new RelaIndunormSymbolBean();
				relaBean.getTypeBean().setShortName(
						rs.getString("IT_SHORTNAME"));
				relaBean.getMetadataBean().setShortName(
						rs.getString("IM_SHORT_NAME"));
				relaBean.getFieldBean().setShortName(
						rs.getString("IF_SHORT_NAME"));
				relaList.add(relaBean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取规范数据域与图元关联信息，数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}
		relaList.trimToSize();

		if (relaList.size() > 0)
			return returnSuccMsg("ArrayList", relaList);
		else
			return returnErrMsg("获取规范数据域与图元关联信息，获取数据为空！");
	}

	/**
	 * 获取规范与业务模型关联信息
	 * 
	 * @param modelID:String:业务模型编号
	 * @param normID:String:规范编号
	 * @return 返回指定业务模型与规范关联信息，如果业务模型参数为空则返回所有关联信息
	 */
	private ResultBean getIndunormModelRela(String businessID, String modelID,
			String normID) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.im_buss_id, a.im_modle_id, ").append(
				"a.im_module_name, a.im_modle_attrid, ").append(
				"a.im_modle_attrname, a.im_norm_type,").append(
				"a.im_norm_id, b.id_name, a.im_validity").append(
				" FROM t_svg_indunorm_modelrela a, ").append(
				"(SELECT d.id_id, d.id_name, 1 AS norm_type ").append(
				"FROM t_svg_indunorm_desc d UNION ").append(
				"SELECT e.if_id, e.if_name, 2 AS norm_type ").append(
				"FROM t_svg_indunorm_fields e) b ").append(
				"WHERE a.im_norm_id = b.id_id ").append(
				"AND a.im_norm_type = b.norm_type ");
		if (businessID != null && businessID.length() > 0) {
			sql.append(" AND a.im_buss_id = '").append(businessID).append("'");
		}
		if (modelID != null && modelID.length() > 0) {
			sql.append(" AND a.im_modle_id = '").append(modelID).append("'");
		}
		if (normID != null && normID.length() > 0) {
			sql.append(" AND a.im_norm_id = '").append(normID).append("'");
		}
		sql.append(" ORDER BY a.im_buss_id, a.im_modle_id, a.im_norm_id");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取规范与业务模型关联信息！");
			return returnErrMsg("获取规范与业务模型关联信息，无法获取有效数据库连接！");
		}

		ArrayList relaList = new ArrayList();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取规范与业务模型关联信息："
					+ sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			RelaIndunormModelBean relaBean;
			while (rs.next()) {
				relaBean = new RelaIndunormModelBean();
				String busiID = rs.getString("im_buss_id");
				String busiName = getCodeName("SVG_BUSINESS_ID", busiID);
				modelID = rs.getString("im_modle_id");
				String modelName = rs.getString("im_module_name");
				String attrID = rs.getString("im_modle_attrid");
				String attrName = rs.getString("im_modle_attrname");
				String normType = rs.getString("im_norm_type");
				String normTypeName = getCodeName("SVG_NORM_TYPE", normType);
				normID = rs.getString("im_norm_id");
				String normName = rs.getString("id_name");
				String validity = rs.getString("im_validity");
				String validityName = getCodeName("SVG_VALID_FLAG", validity);

				relaBean.setBusiID(busiID);
				relaBean.setBusiName(busiName);
				relaBean.setModelID(modelID);
				relaBean.setModelName(modelName);
				relaBean.setModelAttrID(attrID);
				relaBean.setModelAttrName(attrName);
				relaBean.setNormID(normID);
				relaBean.setNormName(normName);
				relaBean.setNormType(normType);
				relaBean.setNormTypeName(normTypeName);
				relaBean.setValidity(validity);
				relaBean.setValidityName(validityName);

				relaList.add(relaBean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			// relaList = null;
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取规范与业务模型关联信息，数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}
		relaList.trimToSize();
		if (relaList.size() > 0)
			return returnSuccMsg("ArrayList", relaList);
		else
			return returnErrMsg("获取规范与业务模型关联信息，获取数据为空！");
	}

	/**
	 * 根据代码短名和代码值，获取代码名称
	 * 
	 * @param codeType:String:代码短名
	 * @param codeValue:String:代码值
	 * @return
	 */
	private String getCodeName(String codeType, String codeValue) {
		// SELECT cc_name FROM t_svg_code_commsets WHERE cc_code='1' AND
		// cc_shortname = 'SVG_BUSINESS_ID'
		if (codeType == null || codeType.length() <= 0 || codeValue == null
				|| codeValue.length() <= 0)
			return null;

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT cc_name FROM t_svg_code_commsets ").append(
				"WHERE cc_code='").append(codeValue).append(
				"' AND cc_shortname = '").append(codeType).append("'");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取代码名称！");
			return null;
		}

		String name = null;
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取代码名称：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			if (rs.next()) {
				name = rs.getString("cc_name");
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		} catch (Exception e) {
			log.log(this, LoggerAdapter.ERROR, e);
		} finally {
			controller.getDBManager().close(conn);
		}
		return name;
	}

	private ResultBean getSimpleIndunormList(String businessID) {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT a.it_shortname type,a.it_name typename,'' metadata,")
				.append(
						"'文件描述' metadataname,b.id_short_name field,b.id_name fieldname ")
				.append(
						"FROM t_svg_indunorm_type a,t_svg_indunorm_desc b,t_svg_indunorm_graphtyperela e ")
				.append(
						"WHERE a.it_shortname = b.id_variety AND a.it_shortname = e.ig_norm_type AND e.ig_businesssys_id = '")
				.append(businessID)
				.append("' ")
				.append(
						"UNION SELECT a.it_shortname type,a.it_name typename,c.im_short_name metadata,")
				.append(
						"c.im_name metadataname,d.if_short_name field,d.if_name fieldname ")
				.append(
						"FROM t_svg_indunorm_type a,t_svg_indunorm_metadatas c,t_svg_indunorm_fields d ,t_svg_indunorm_graphtyperela e ")
				.append(
						"WHERE a.it_shortname = c.im_variety AND c.im_id = d.if_variety ")
				.append(
						"AND a.it_shortname = e.ig_norm_type AND e.ig_businesssys_id = '")
				.append(businessID).append("'");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取规范列表信息！");
			return returnErrMsg("获取规范列表信息，无法获取有效数据库连接！");
		}

		HashMap map = new HashMap();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取规范列表信息：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			SimpleIndunormBean simpleBean;
			while (rs.next()) {
				simpleBean = new SimpleIndunormBean();
				simpleBean.setType(rs.getString("type"));
				simpleBean.setTypeName(rs.getString("typename"));
				simpleBean.setMetadata(rs.getString("metadata"));
				simpleBean.setMetadataName(rs.getString("metadataName"));
				simpleBean.setField(rs.getString("field"));
				simpleBean.setFieldName(rs.getString("fieldName"));
				map.put(simpleBean.toString(), simpleBean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			// relaList = null;
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取规范列表，数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}
		if (map.size() > 0)
			return returnSuccMsg("HashMap", map);
		else
			return returnErrMsg("获取规范列表，获取数据为空！");
	}
}
