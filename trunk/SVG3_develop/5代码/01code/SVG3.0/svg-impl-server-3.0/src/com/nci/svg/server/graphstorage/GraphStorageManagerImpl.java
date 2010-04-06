package com.nci.svg.server.graphstorage;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.nci.svg.sdk.bean.GraphFileBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.bean.SimpleCodeBean;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;
import com.nci.svg.sdk.server.graphstorage.GraphStorageManagerAdapter;
import com.nci.svg.sdk.server.util.ServerUtilities;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：图库存储类
 * 
 */
public class GraphStorageManagerImpl extends GraphStorageManagerAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5729203748520689029L;
	/**
	 * 管理组件对象
	 */
	private ServerModuleControllerAdapter controller;
	/**
	 * 日志对象
	 */
	private LoggerAdapter log;

	/**
	 * 无版本管理
	 */
	private final String VERSION_NULL = "0";
	/**
	 * 替代式版本管理
	 */
	private final String VERSION_SUBS = "1";
	/**
	 * 增量式版本管理
	 */
	private final String VERSION_INCR = "2";

	public GraphStorageManagerImpl(HashMap parameters) {
		super(parameters);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.GraphStorage.GraphStorageManagerAdapter#loadGraph(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.Object)
	 */
	public ResultBean loadGraph(String businessID, GraphFileBean obj) {
		if (businessID == null || businessID.length() <= 0)
			return returnErrMsg("取图操作，不能获取业务系统编号！");
		// 获取存储表名
		String tableName = getTableName(businessID);
		if (tableName == null || tableName.length() <= 0) {
			return returnErrMsg("取图操作，未能获取存储表名！");
		}

		// 根据传入的参数取图
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(tableName).append(" WHERE '1'='1'");
		if (obj.getID() != null && obj.getID().length() > 0) {
			sql.append(" AND rg_id = '").append(obj.getID()).append("'");
		}
		if (obj.getFileName() != null && obj.getFileName().length() > 0) {
			sql.append(" AND rg_graph_name = '").append(obj.getFileName())
					.append("' ");
		}

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获得文件列表！");
			return returnErrMsg("取图操作，无法获取有效数据库连接！");
		}
		GraphFileBean fileBean = null;
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取文件：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());

			if (rs.next()) {
				String id = rs.getString("rg_id");
				String name = rs.getString("rg_graph_name");
				String graphType = rs.getString("rg_graph_type");
				String graphTypeName = getCodeName("SVG_GRAPH_TYPE", graphType);
				String busiType = rs.getString("rg_graph_busi_type");
				String busiTypeName = getCodeName("SVG_BUSINESS_GRAPH_TYPE",
						busiType);
				String graphFormat = rs.getString("rg_graph_file_type");
				String graphFormatName = getCodeName("SVG_GRAPH_FILE_TYPE",
						graphFormat);
				String operator = rs.getString("rg_modify_person");
				String time = rs.getString("rg_modify_time");
				String[] param = new String[10];
				for (int i = 0, size = param.length; i < size; i++) {
					param[i] = rs.getString("rg_param" + i);
				}

				fileBean = new GraphFileBean();
				fileBean.setID(id);
				fileBean.setBusiType(busiType);
				fileBean.setBusiTypeName(busiTypeName);
				fileBean.setFileFormat(graphFormat);
				fileBean.setFileFormatName(graphFormatName);
				fileBean.setFileName(name);
				fileBean.setFileType(graphType);
				fileBean.setFileTypeName(graphTypeName);
				fileBean.setModifyTime(time);
				fileBean.setOperator(operator);
				fileBean.setParams(param);
				String content = loadGraphContent(conn, tableName, id);
				fileBean.setContent(content);
			}

			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("取图操作，数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}

		if (fileBean != null)
			return returnSuccMsg("GraphFileBean", fileBean);
		else
			return returnErrMsg("取图操作，获取数据为空！");
	}

	/**
	 * 更加文件编号获取文件内容
	 * 
	 * @param conn:Connection:数据库连接
	 * @param tableName:String:存储表名
	 * @param id:String:文件编号
	 * @return String
	 */
	private String loadGraphContent(Connection conn, String tableName, String id) {
		StringBuffer sql = new StringBuffer();
		if (id != null && id.length() > 0 && tableName != null
				&& tableName.length() > 0) {
			sql.append("SELECT rg_content FROM ").append(tableName).append(
					" WHERE rg_id = '").append(id).append("'");
		} else {
			return null;
		}

		String graphContent = null;
		try {
			String type = controller.getDBManager().getDBType("SVG");
			if (type != null) {
				// 通过方言转换类获取blob字段
				DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
						.getDBSqlIdiom(type);
				if (sqlIdiom == null)
					return graphContent;

				// 获取图元内容
				log.log(this, LoggerAdapter.DEBUG, "获取图形内容：" + sql.toString());
				byte[] content = sqlIdiom.getBlob(conn, sql.toString(),
						"rg_content");

				if (content != null && content.length > 0)
					graphContent = new String(content);
			}
		} catch (Exception ex) {
			log.log(this, LoggerAdapter.ERROR, ex);
			graphContent = null;
		}
		return graphContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.GraphStorage.GraphStorageManagerAdapter#loadSymbol(java.lang.String,
	 *      java.lang.Object)
	 */
	public ResultBean loadSymbol(String name) {
		StringBuffer sql = new StringBuffer();
		if (name != null && name.length() > 0) {
			sql.append("SELECT sf_content AS content").append(
					" FROM (SELECT sf_content, sf_name").append(
					" FROM t_svg_symbol_fileinfo").append(" UNION ALL").append(
					" SELECT st_content, st_name FROM ").append(
					"t_svg_symbol_template) a").append(" WHERE a.sf_name = '")
					.append(name).append("'");
		} else {
			return returnErrMsg("取图元，输入图元名称为空！");
		}
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法获取图元！");
			return returnErrMsg("取图元，无法获取有效数据库连接！");
		}
		String symbolContent = null;
		String type = controller.getDBManager().getDBType("SVG");
		if (type != null) {
			// 通过方言转换类获取blob字段
			DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
					.getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return returnErrMsg("取图元，无法获取方言转换器！");

			// 获取图元内容
			log.log(this, LoggerAdapter.DEBUG, "获取图元内容：" + sql.toString());
			byte[] content = sqlIdiom.getBlob(conn, sql.toString(), "content");

			if (content != null && content.length > 0)
				symbolContent = new String(content);
		}
		// 关闭数据库连接
		controller.getDBManager().close(conn);

		if (symbolContent != null && symbolContent.length() > 0)
			return returnSuccMsg("String", symbolContent);
		else
			return returnErrMsg("取图元，获取数据为空！");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.GraphStorage.GraphStorageManagerAdapter#saveGraph(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.Object)
	 */
	public ResultBean saveGraph(String businessID, String logs,
			GraphFileBean fileBean) {
		if (businessID == null || businessID.length() <= 0) {
			return returnErrMsg("保存图形文件，没有接收到业务系统编号！");
		}
		// 获取图库标识
		String rela = getRelateString(businessID);
		if (rela == null || rela.length() <= 0) {
			return returnErrMsg("存储图形文件，获取图标标识失败！");
		}

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法保持图形内容！");
			return returnErrMsg("保存图形文件，不能有效获取数据库连接！");
		}
		// 获取方言转换器
		String type = controller.getDBManager().getDBType("SVG");
		DBSqlIdiomAdapter sqlIdiom = null;
		if (type != null) {
			// 通过方言转换类获取blob字段
			sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
			if (sqlIdiom == null) {
				return returnErrMsg("保存图形文件时，获取不到方言转换对象！");
			}
		}

		ResultBean rb = new ResultBean();
		try {
			conn.setAutoCommit(false);
			if (fileBean.getID() == null || fileBean.getID().length() <= 0) {
				// 新增图信息
				rb = AddNewGraph(conn, sqlIdiom, rela, fileBean);
			} else {
				// 版本控制
				rb = saveGraphLog(conn, rela, logs, fileBean);
				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
					// 更新图信息
					rb = ModifyGraph(conn, sqlIdiom, rela, fileBean, logs);
				}
			}

			conn.setAutoCommit(true);
			if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
				conn.commit();
				rb = returnSuccMsg("GraphFileBean", loadGraph(businessID,
						fileBean).getReturnObj());
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("保存图形文件，操作数据库失败！");
		} finally {
			controller.getDBManager().close(conn);
		}

		return rb;
	}

	/**
	 * 修改指定图形文件（未提交数据）
	 * 
	 * @param conn:Connection:数据库连接
	 * @param sqlIdim:DBSqlIdiomAdapter:方言转换器
	 * @param rela:String:数据库标识
	 * @param fileBean:GraphFileBean:图形对象
	 * @param logs:String:图形操作记录
	 * @return
	 */
	private ResultBean ModifyGraph(Connection conn, DBSqlIdiomAdapter sqlIdiom,
			String rela, GraphFileBean fileBean, String logs) {
		ResultBean rb = null;
		int ret = -1;
		try {
			// **************
			// 修改图形基本信息
			// **************
			String id = fileBean.getID();
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE t_svg_rep_graph_").append(rela).append(
					" SET rg_graph_name=?, rg_graph_type=?,").append(
					" rg_graph_busi_type=?,").append(
					" rg_graph_file_type=?, rg_param0 = ?,").append(
					" rg_param1 = ?, rg_param2 = ?,").append(
					" rg_param3 = ?, rg_param4 = ?,").append(
					" rg_param5 = ?, rg_param6 = ?,").append(
					" rg_param7 = ?, rg_param8 = ?,").append(
					" rg_param9 = ?, rg_modify_person = ?,").append(
					" rg_modify_time = ?, rg_content = EMPTY_BLOB()").append(
					" WHERE rg_id = ?");
			PreparedStatement presm = conn.prepareStatement(sql.toString());

			// 获得当前服务器时间
			String sysdate = sqlIdiom.getSysDate(conn);
			presm.setString(1, fileBean.getFileName());
			presm.setString(2, fileBean.getFileType());
			presm.setString(3, fileBean.getBusiType());
			presm.setString(4, fileBean.getFileFormat());
			for (int i = 0, size = fileBean.getParams().length; i < size; i++) {
				presm.setString(5 + i, fileBean.getParams(i));
			}
			presm.setString(15, fileBean.getOperator());
			presm.setString(16, sysdate);
			presm.setString(17, id);

			ret = presm.executeUpdate();
			presm.clearParameters();
			// 设置图形文件对象的图形修改时间
			fileBean.setModifyTime(sysdate);

			// ***********
			// 保存图形内容
			// ***********
			String content = fileBean.getContent();
			if (content != null && content.length() > 0 && ret > 0) {
				sql = new StringBuffer();
				sql.append("SELECT rg_content FROM t_svg_rep_graph_").append(
						rela).append(" WHERE rg_id = '").append(id).append("'");

				ret = sqlIdiom.setBlob(conn, sql.toString(), "rg_content",
						content.getBytes());
			}
			if (ret > 0) {
				rb = returnSuccMsg("String", "成功修改图形文件到数据库中！");
			} else {
				rb = returnErrMsg("修改指定图形文件，数据库操作失败！");
			}
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, "修改指定图形文件，数据库操作失败！");
			return returnErrMsg("修改指定图形文件，数据库操作失败！");
		}
		return rb;
	}

	/**
	 * 新增图形文件到数据库中（未提交数据）
	 * 
	 * @param conn:Connection:数据库连接
	 * @param sqlIdim:DBSqlIdiomAdapter:方言转换器
	 * @param rela:String:数据库标识
	 * @param fileBean:GraphFileBean:图形对象
	 * @return
	 */
	private ResultBean AddNewGraph(Connection conn, DBSqlIdiomAdapter sqlIdiom,
			String rela, GraphFileBean fileBean) {
		ResultBean rb = null;
		int ret = -1;
		try {
			// **************
			// 新增图形基本信息
			// **************
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO t_svg_rep_graph_").append(rela).append(
					" (rg_id, rg_graph_name,").append(
					" rg_graph_type, rg_graph_busi_type,").append(
					" rg_graph_file_type, rg_param0,").append(
					" rg_param1, rg_param2, rg_param3,").append(
					" rg_param4, rg_param5,").append(" rg_param6, rg_param7,")
					.append(" rg_param8, rg_param9, rg_modify_person,").append(
							" rg_modify_time, rg_content) ").append(
							"VALUES (?,?,?,?,?,?,?,?,?,?,?,").append(
							"?,?,?,?,?,?,EMPTY_BLOB())");

			PreparedStatement presm = conn.prepareStatement(sql.toString());
			// 生成编号
			String id = ServerUtilities.getUUIDString();

			// 获得当前服务器时间
			String sysdate = sqlIdiom.getSysDate(conn);
			// 获得当前连接的空blob描述
			presm.setString(1, id);
			presm.setString(2, fileBean.getFileName());
			presm.setString(3, fileBean.getFileType());
			presm.setString(4, fileBean.getBusiType());
			presm.setString(5, fileBean.getFileFormat());
			for (int i = 0, size = fileBean.getParams().length; i < size; i++) {
				presm.setString(6 + i, fileBean.getParams(i));
			}
			presm.setString(16, fileBean.getOperator());
			presm.setString(17, sysdate);

			ret = presm.executeUpdate();
			presm.clearParameters();
			// 设置文件对象的文件编号
			fileBean.setID(id);
			// 设置图形文件对象的图形修改时间
			fileBean.setModifyTime(sysdate);

			// ***********
			// 保存图形内容
			// ***********
			String content = fileBean.getContent();
			if (content != null && content.length() > 0 && ret > 0) {
				sql = new StringBuffer();
				sql.append("SELECT rg_content FROM t_svg_rep_graph_").append(
						rela).append(" WHERE rg_id = '").append(id).append("'");

				ret = sqlIdiom.setBlob(conn, sql.toString(), "rg_content",
						content.getBytes());
			}

			if (ret > 0) {
				rb = returnSuccMsg("String", "成功新增图形文件到数据库中！");
			} else {
				rb = returnErrMsg("新增图形文件到数据库，数据库操作失败！");
			}
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, "新增图形文件到数据库，数据库操作失败！");
			return returnErrMsg("新增图形文件到数据库，数据库操作失败！");
		}
		return rb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.GraphStorage.GraphStorageManagerAdapter#saveSymbol(java.lang.String,
	 *      java.lang.String,
	 *      com.nci.svg.sdk.server.GraphStorage.SymbolInfoBean)
	 */
	public ResultBean saveSymbol(String businessID, String strSymbolType,
			String content, NCIEquipSymbolBean obj) {
		ResultBean rb = new ResultBean();
		String type = getSameSymbol(obj.getName()); // 获取指定名称的图元或模板是否存在
		// String id = str[0];
		String id = obj.getId();

		if (businessID == null || businessID.length() <= 0) {
			return returnErrMsg("保存图元失败，未获取业务系统编号!");
		}
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.DEBUG, "无法获取数据库连接，无法新增图元！");
			return returnErrMsg("无法获取数据库连接，无法新增图元！");
		}

		try {
			conn.setAutoCommit(false);
			if (strSymbolType.toLowerCase().equals(graphUnitType)) {
				// ********
				// 图元保存
				// ********
				if (id != null && id.length() == 36
						&& type.equals(graphUnitType)) {
					rb = updateSymbol(conn, id, obj);
				} else if (type == null) { // 指定图元名称不存在则新建
					// 生成编号
					id = ServerUtilities.getUUIDString();
					rb = insertSymbol(conn, id, obj);
				} else { // 要保存的图元名称已存在
					rb = returnErrMsg("保存图元失败，存在同名图元或模板！");
				}

				String holdFlag = controller.getServiceSets("symbolSets",
						"holdModelFlag");
				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS
						&& holdFlag.equalsIgnoreCase("true")) {
					// 保存图元与业务模型关联关系
					rb = saveRelaBusiSymbol(conn, id, businessID, obj
							.getModelID());
				}

				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS
						&& content != null && content.length() > 0) {
					// 保存图元内容
					rb = updateSymbolContent(conn, id, content);
				}
			} else if (strSymbolType.toLowerCase().equals(templateType)) {
				// ********
				// 模板保存
				// ********
				// conn.setAutoCommit(false);
				if (id != null && id.length() == 36
						&& type.equals(templateType)) {
					rb = updateTemplate(conn, id, obj);
				} else if (type == null) { // 指定模板名称不存在则新建
					// 生成编号
					id = ServerUtilities.getUUIDString();
					rb = insertTemplate(conn, id, obj);
				} else { // 要保存的图元名称已存在
					rb = returnErrMsg("保存图元失败，存在同名图元或模板！");
				}

				String holdFlag = controller.getServiceSets("symbolSets",
						"holdModelFlag");
				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS
						&& holdFlag.equalsIgnoreCase("true")) {
					// 保存图元与业务模型关联关系
					rb = saveRelaBusiSymbol(conn, id, businessID, obj
							.getModelID());
				}
				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS
						&& content != null && content.length() > 0) {
					rb = updateTemplateContent(conn, id, content);
				}
			}
			conn.setAutoCommit(true);
			if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("保存图元失败，数据库操作失败!");
		} finally {
			controller.getDBManager().close(conn);
		}

		return rb;
	}

	/**
	 * 获取数据库中的图元表和模型表中有指定名称的图元或模板
	 * 
	 * @param symbolName
	 * @return
	 */
	public String getSameSymbol(String symbolName) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT sf_id AS id, '").append(graphUnitType).append(
				"' AS type").append(
				" FROM t_svg_symbol_fileinfo WHERE sf_name = '").append(
				symbolName).append("' UNION SELECT st_id AS id, '").append(
				templateType).append("' AS type").append(
				" FROM t_svg_symbol_template WHERE st_name = '").append(
				symbolName).append("'");
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		String ret = null;
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获得客户端组件清单！");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取指定名称的图元编号：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				ret = rs.getString("type");
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		} finally {
			controller.getDBManager().close(conn);
		}
		return ret;
	}

	/**
	 * 根据传入的业务系统编号和数据库连接获取对应的图库标识
	 * 
	 * @param strBussID：业务系统编号
	 * @return：图库标识，不存在则返回null
	 */
	public String getRelateString(String strBussID) {
		StringBuffer sql = new StringBuffer();
		String relate = null;
		// 首先获取该业务系统图库标记
		sql
				.append(
						"SELECT rm_table_relate FROM t_svg_rep_manager WHERE rm_businesssys_id = '")
				.append(strBussID).append("'");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取图库标识！");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取图库标识：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());

			if (rs.next()) {
				relate = rs.getString("rm_table_relate");
			}
		} catch (Exception ex) {
			log.log(this, LoggerAdapter.ERROR, ex);
			relate = null;
		} finally {
			controller.getDBManager().close(conn);
		}

		return relate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.GraphStorage.GraphStorageManagerAdapter#getSymbolList(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public ArrayList getSymbolList(String symbolType, String releaseFlag,
			String person) {
		ArrayList symbolList = new ArrayList();
		StringBuffer sql = new StringBuffer();
		if (symbolType == null || symbolType.length() == 0) {
			// 请求图元与模板列表
			sql.append("SELECT sf_name AS name, ").append(
					"sf_variety AS variety, ").append(
					"b.cc_name AS varietyname, ").append(
					"sf_create_time AS create_time, ").append(
					"sf_modify_time AS modify_time, ").append(
					"sf_modify_person AS modify_person, ").append(
					"sf_validity AS validity, ").append(
					"sf_releaseflag AS releaseflag, ").append(
					"'graphunit' AS symbol_type, ").append("sf_id AS id, ")
					.append("c.sm_module_id AS model_id ").append(
							"FROM t_svg_symbol_fileinfo a, ").append(
							"t_svg_code_commsets b, ").append(
							"t_svg_symbol_modelrela c ").append(
							"WHERE a.sf_variety = b.cc_code AND").append(
							" b.cc_shortname = 'SVG_GRAPHUNIT_VARIETY'")
					.append(" AND a.sf_id = c.sm_symbol_id");
			sql.append(" UNION ").append("SELECT st_name AS name, ").append(
					"st_variety AS variety, ").append(
					"p.cc_name AS varietyname, ").append(
					"st_create_time AS create_time, ").append(
					"st_modify_time AS modify_time, ").append(
					"st_modify_person AS modify_person, ").append(
					"st_validity AS validity, ").append(
					"st_releaseflag   AS releaseflag, ").append(
					"'template' AS symbol_type, ").append("st_id AS id, ")
					.append("q.sm_module_id AS model_id ").append(
							"FROM t_svg_symbol_template o, ").append(
							"t_svg_code_commsets p, ").append(
							"t_svg_symbol_modelrela q ").append(
							"WHERE o.st_variety = p.cc_code AND ").append(
							"p.cc_shortname = 'SVG_TEMPLATE_VARIETY'").append(
							" AND o.st_id = q.sm_symbol_id");
			sql.append(" ORDER BY symbol_type, variety");
		} else if (symbolType
				.equalsIgnoreCase(NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT)) {
			// 请求图元列表
			sql.append("SELECT sf_name AS name, ").append(
					"sf_variety AS variety, ").append(
					"b.cc_name AS varietyname, ").append(
					"sf_create_time AS create_time, ").append(
					"sf_modify_time AS modify_time, ").append(
					"sf_modify_person AS modify_person, ").append(
					"sf_validity AS validity, ").append(
					"sf_releaseflag AS releaseflag, ").append(
					"'graphunit' AS symbol_type, ").append("sf_id AS id, ")
					.append("c.sm_module_id AS model_id ").append(
							"FROM t_svg_symbol_fileinfo a, ").append(
							"t_svg_code_commsets b, ").append(
							"t_svg_symbol_modelrela c ").append(
							"WHERE a.sf_variety = b.cc_code AND").append(
							" b.cc_shortname = 'SVG_GRAPHUNIT_VARIETY'")
					.append(" AND a.sf_id = c.sm_symbol_id");
			sql.append(" ORDER BY symbol_type, variety");
		} else if (symbolType
				.equalsIgnoreCase(NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			// 请求模板列表
			sql.append("SELECT st_name AS name, ").append(
					"st_variety AS variety, ").append(
					"d.cc_name AS varietyname, ").append(
					"st_create_time AS create_time, ").append(
					"st_modify_time AS modify_time, ").append(
					"st_modify_person AS modify_person, ").append(
					"st_validity AS validity, ").append(
					"st_releaseflag   AS releaseflag, ").append(
					"'template' AS symbol_type, ").append("st_id AS id, ")
					.append("q.sm_module_id AS model_id ").append(
							"FROM t_svg_symbol_template c, ").append(
							"t_svg_code_commsets d, ").append(
							"t_svg_symbol_modelrela q ").append(
							" WHERE c.st_variety = d.cc_code AND ").append(
							"d.cc_shortname = 'SVG_TEMPLATE_VARIETY'").append(
							" AND c.st_id = q.sm_symbol_id");
			sql.append(" ORDER BY symbol_type, variety");
		}
		if (sql.toString().equals("")) {
			return null;
		}
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获得图元清单！");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取图元列表：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			NCIEquipSymbolBean symbolBean;
			while (rs.next()) {
				String type = null;
				String tempVariety = rs.getString("variety");
				String tempSymbolType = rs.getString("symbol_type");
				if (tempSymbolType.equals("graphunit")) {
					type = NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT;
				} else {
					type = NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE;
				}

				symbolBean = new NCIEquipSymbolBean();
				String symbolID = rs.getString("id"); // 获取图元编号
				symbolBean.setId(symbolID); // 设置图元编号
				symbolBean.setType(type); // 获取是图元还是模板
				symbolBean.setName(rs.getString("name")); // 获取图元名称
				// 获取图元类型
				symbolBean.setVariety(new SimpleCodeBean(tempVariety, rs
						.getString("varietyname")));
				symbolBean.setCreateTime(rs.getString("create_time")); // 获取图元创建时间
				symbolBean.setModifyTime(rs.getString("modify_time")); // 获取图元修改时间
				symbolBean.setOperator(rs.getString("modify_person")); // 获取图元修改人
				symbolBean.setValidity(rs.getBoolean("validity")); // 获取图元是否有效
				symbolBean.setReleased(rs.getBoolean("releaseflag")); // 获取图元是否被发布
				symbolBean.setContent(getSymbolContent(symbolID)); // 获取图元内容
				symbolBean.setModelID(rs.getString("model_id")); // 获取图元关联的业务模型编号

				symbolList.add(symbolBean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		} catch (Exception e) {
			e.printStackTrace();
			log.log(this, LoggerAdapter.ERROR, e);
		} finally {
			controller.getDBManager().close(conn);
		}

		symbolList.trimToSize();
		return symbolList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.graphstorage.GraphStorageManagerAdapter#getSymbolOperators()
	 */
	public ArrayList getSymbolOperators() {
		// sql查询语句
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT sf_modify_person AS person").append(
				" FROM t_svg_symbol_fileinfo").append(" UNION").append(
				" SELECT st_modify_person AS person").append(
				" FROM t_svg_symbol_template");
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获得图元清单！");
			return null;
		}
		ArrayList list = new ArrayList();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取图元对象：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());

			while (rs.next()) {
				String operator = rs.getString("person");
				list.add(operator);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		} finally {
			controller.getDBManager().close(conn);
		}

		list.trimToSize();
		return list;
	}

	/**
	 * 根据图元名称获取该图元的对象
	 * 
	 * @param symbolName
	 * @return
	 */
	public NCIEquipSymbolBean getSymbolIDFromName(String symbolName) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT name, variety, varietyname, create_time,").append(
				" modify_time, modify_person, validity,").append(
				" releaseflag, symbol_type, id,model_id FROM (");
		sql.append("SELECT sf_name AS name, ")
				.append("sf_variety AS variety, ").append(
						"b.cc_name AS varietyname, ").append(
						"sf_create_time AS create_time, ").append(
						"sf_modify_time AS modify_time, ").append(
						"sf_modify_person AS modify_person, ").append(
						"sf_validity AS validity, ").append(
						"sf_releaseflag AS releaseflag, ").append(
						"'graphunit' AS symbol_type, ").append("sf_id AS id, ")
				.append("c.sm_module_id AS model_id ").append(
						"FROM t_svg_symbol_fileinfo a, ").append(
						"t_svg_code_commsets b ,t_svg_symbol_modelrela c ")
				.append("WHERE a.sf_variety = b.cc_code AND").append(
						" b.cc_shortname = 'SVG_GRAPHUNIT_VARIETY'").append(
						" AND a.sf_id = c.sm_symbol_id");
		sql.append(" UNION ").append("SELECT st_name AS name, ").append(
				"st_variety AS variety, ").append("d.cc_name AS varietyname, ")
				.append("st_create_time AS create_time, ").append(
						"st_modify_time AS modify_time, ").append(
						"st_modify_person AS modify_person, ").append(
						"st_validity AS validity, ").append(
						"st_releaseflag   AS releaseflag, ").append(
						"'template' AS symbol_type, ").append("st_id AS id, ")
				.append("q.sm_module_id AS model_id ").append(
						"FROM t_svg_symbol_template c, ").append(
						"t_svg_code_commsets d, ").append(
						"t_svg_symbol_modelrela q ").append(
						"WHERE c.st_variety = d.cc_code AND ").append(
						"d.cc_shortname = 'SVG_TEMPLATE_VARIETY'").append(
						" AND c.st_id = q.sm_symbol_id");
		sql.append(" ORDER BY symbol_type, variety");
		sql.append(") WHERE name='").append(symbolName).append("'");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获得图元清单！");
			return null;
		}
		NCIEquipSymbolBean symbolBean = null;
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取图元对象：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());

			if (rs.next()) {
				String type = null;
				String tempVariety = rs.getString("variety");
				String tempSymbolType = rs.getString("symbol_type");
				if (tempSymbolType.equals("graphunit")) {
					type = NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT;
				} else {
					type = NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE;
				}

				symbolBean = new NCIEquipSymbolBean();
				String symbolID = rs.getString("id"); // 获取图元编号
				symbolBean.setId(symbolID); // 设置图元编号
				symbolBean.setType(type); // 获取是图元还是模板
				symbolBean.setName(rs.getString("name")); // 获取图元名称
				// 获取图元类型
				symbolBean.setVariety(new SimpleCodeBean(tempVariety, rs
						.getString("varietyname")));
				symbolBean.setCreateTime(rs.getString("create_time")); // 获取图元创建时间
				symbolBean.setModifyTime(rs.getString("modify_time")); // 获取图元修改时间
				symbolBean.setOperator(rs.getString("modify_person")); // 获取图元修改人
				symbolBean.setValidity(rs.getBoolean("validity")); // 获取图元是否有效
				symbolBean.setReleased(rs.getBoolean("releaseflag")); // 获取图元是否被发布
				symbolBean.setContent(getSymbolContent(symbolID)); // 获取图元内容
				symbolBean.setModelID(rs.getString("model_id")); // 获取图元关联的业务模型编号
			}

			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		} finally {
			controller.getDBManager().close(conn);
		}
		return symbolBean;
	}

	/**
	 * 根据图元编号获取图元内容
	 * 
	 * @param symbolID
	 * @return
	 */
	private String getSymbolContent(String symbolID) {
		byte[] content = null;
		String type = controller.getDBManager().getDBType("SVG");
		if (type != null) {
			// 通过方言转换类获取blob字段
			DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
					.getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return null;
			// 获取数据库连接
			Connection conn = controller.getDBManager().getConnection("svg");

			StringBuffer sql = new StringBuffer();
			sql.append("SELECT sf_content FROM ").append(
					"(SELECT sf_content, sf_id ").append(
					"FROM t_svg_symbol_fileinfo ").append(" UNION ALL ")
					.append("SELECT st_content, st_id ").append(
							"FROM t_svg_symbol_template) a ").append(
							"WHERE a.sf_id = '").append(symbolID).append("'");
			// 获取组件包内容
			log.log(this, LoggerAdapter.DEBUG, "获取图元内容：" + sql.toString());
			content = sqlIdiom.getBlob(conn, sql.toString(), "sf_content");
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					log.log(this, LoggerAdapter.ERROR, "取图元内容，关闭数据库连接出错！");
					log.log(this, LoggerAdapter.ERROR, e1);
				}
			}
		}

		String reContent = null;
		try {
			reContent = new String(content, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return reContent;

	}

	/**
	 * 根据id和图元信息，新增图元记录
	 * 
	 * @param conn:数据库连接
	 * @param id：图元编号
	 * @param obj：图元信息
	 * @return：新增结果
	 */
	protected ResultBean insertSymbol(Connection conn, String id,
			NCIEquipSymbolBean obj) {
		StringBuffer sql = new StringBuffer();
		String type = controller.getDBManager().getDBType("SVG");
		DBSqlIdiomAdapter sqlIdiom = null;
		int ret = 0;
		if (type != null) {
			// 通过方言转换类获取blob字段
			sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return returnErrMsg("新增图元操作，获取方言转换器失败！");
		}
		sql.append("INSERT INTO t_svg_symbol_fileinfo ").append(
				"(sf_id, sf_name, sf_variety, ").append(
				"sf_create_time, sf_modify_time, ").append(
				"sf_modify_person, sf_validity, ").append(
				"sf_releaseflag, sf_content) ").append(
				"VALUES (?,?,?,?,?,?,?,?,EMPTY_BLOB())");
		PreparedStatement presm = null;
		try {
			presm = conn.prepareStatement(sql.toString());
			// 获得当前服务器时间
			String sysdate = sqlIdiom.getSysDate(conn);
			// // 获得当前连接的空blob描述
			// String emptyBlob = sqlIdiom.getEmptyBlobString();

			presm.setString(1, id);
			presm.setString(2, obj.getName());
			presm.setString(3, obj.getVariety().getCode());
			presm.setString(4, sysdate);
			presm.setString(5, sysdate);
			presm.setString(6, obj.getOperator());
			// presm.setString(6, obj.getModifyPerson());
			// presm.setString(7, "EMPTY_BLOB()");
			presm.setString(7, "1");
			presm.setString(8, "0");

			log.log(this, LoggerAdapter.DEBUG, "新增图元：" + sql.toString());
			ret = presm.executeUpdate();
			presm.clearParameters();
			if (presm != null)
				presm.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, "新增图元操作，数据库操作失败！");
			return returnErrMsg("新增图元操作，数据库操作失败！");
		}
		if (ret > 0) {
			return returnSuccMsg("String", id);
		}
		return returnErrMsg("新增图元操作，数据库操作失败！");
	}

	/**
	 * 根据id和图元信息，更新图元记录
	 * 
	 * @param conn:数据库连接
	 * @param id：图元编号
	 * @param obj：图元信息
	 * @return：更新结果
	 */
	protected ResultBean updateSymbol(Connection conn, String id,
			NCIEquipSymbolBean obj) {
		StringBuffer sql = new StringBuffer();
		int ret = 0;

		PreparedStatement presm = null;
		try {
			String type = controller.getDBManager().getDBType("SVG");
			DBSqlIdiomAdapter sqlIdiom = null;
			if (type != null) {
				// 通过方言转换类获取blob字段
				sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
				if (sqlIdiom == null) {
					return returnErrMsg("更新图元操作，获取方言转换器失败！");
				}
			}
			sql.append("UPDATE t_svg_symbol_fileinfo").append(
					" SET sf_name = ?, sf_variety = ?, ").append(
					"sf_modify_time = ?, sf_modify_person = ?, ").append(
					"sf_validity = ?, sf_content=EMPTY_BLOB() ").append(
					"WHERE sf_id = ?");
			presm = conn.prepareStatement(sql.toString());

			// 获得当前服务器时间
			String sysdate = sqlIdiom.getSysDate(conn);

			presm.setString(1, obj.getName());
			presm.setString(2, obj.getVariety().getCode());
			presm.setString(3, sysdate);
			presm.setString(4, obj.getOperator());
			// presm.setString(4, obj.getModifyPerson());
			presm.setString(5, "1");
			// presm.setBlob(6, oracle.sql.BLOB.empty_lob());
			presm.setString(6, id);

			ret = presm.executeUpdate();
			presm.clearParameters();
			if (presm != null)
				presm.close();
		} catch (Exception ex) {
			log.log(this, LoggerAdapter.ERROR, "更新图元操作，数据库操作失败！");
			return returnErrMsg("更新图元操作，数据库操作失败！");
		}

		if (ret > 0) {
			return returnSuccMsg("String", id);
		}
		return returnErrMsg("更新图元操作，数据库操作失败！");
	}

	/**
	 * 根据id和图元内容，更新图元内容
	 * 
	 * @param conn:数据库连接
	 * @param id：图元编号
	 * @param content：图元内容
	 * @return：更新结果
	 */
	protected ResultBean updateSymbolContent(Connection conn, String id,
			String content) {
		int ret = 0;
		try {
			String type = controller.getDBManager().getDBType("SVG");
			if (type != null) {
				// 通过方言转换类获取blob字段
				DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
						.getDBSqlIdiom(type);
				if (sqlIdiom == null)
					return returnErrMsg("更新图元内容，获取方言转换器失败！");

				StringBuffer sql = new StringBuffer();
				sql.append("SELECT sf_content FROM t_svg_symbol_fileinfo")
						.append(" WHERE sf_id = '").append(id).append("'");
				// 更新图元内容
				log.log(this, LoggerAdapter.DEBUG, "更新图元内容：" + sql.toString());
				ret = sqlIdiom.setBlob(conn, sql.toString(), "sf_content",
						content.getBytes());
			}
		} catch (Exception ex) {
			log.log(this, LoggerAdapter.ERROR, "更新图元内容，操作数据库失败！");
			return returnErrMsg("更新图元内容，操作数据库失败！");
		}
		if (ret > 0)
			return returnSuccMsg("String", id);
		return returnErrMsg("更新图元内容，操作数据库失败！");
	}

	/**
	 * 根据id和模板信息，新增模板记录
	 * 
	 * @param conn:数据库连接
	 * @param id：图元编号
	 * @param obj：模板信息
	 * @return：新增结果
	 */
	protected ResultBean insertTemplate(Connection conn, String id,
			NCIEquipSymbolBean obj) {
		StringBuffer sql = new StringBuffer();
		String type = controller.getDBManager().getDBType("SVG");
		DBSqlIdiomAdapter sqlIdiom = null;
		if (type != null) {
			// 通过方言转换类获取blob字段
			sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return returnErrMsg("新增模板操作，获取方言转换器失败！");
		}
		sql.append("INSERT INTO t_svg_symbol_template ").append(
				"(st_id, st_name, st_variety, ").append(
				"st_create_time, st_modify_time, ").append(
				"st_modify_person, st_validity, ").append(
				"st_releaseflag, st_param1, st_param2, ").append(
				"st_param3, st_content) ").append(
				"VALUES (?,?,?,?,?,?,?,?,?,?,?,EMPTY_BLOB())");

		PreparedStatement presm = null;
		int ret = -1;
		try {
			presm = conn.prepareStatement(sql.toString());
			// 获得当前服务器时间
			String sysdate = sqlIdiom.getSysDate(conn);

			presm.setString(1, id);
			presm.setString(2, obj.getName());
			presm.setString(3, obj.getVariety().getCode());
			presm.setString(4, sysdate);
			presm.setString(5, sysdate);
			presm.setString(6, obj.getOperator());
			// presm.setString(6, obj.getModifyPerson());
			// presm.setString(7, emptyBlob);
			presm.setString(7, "1");
			presm.setString(8, "0");
			presm.setString(9, obj.getParam1());
			presm.setString(10, obj.getParam2());
			presm.setString(11, obj.getParam3());

			ret = presm.executeUpdate();
			presm.clearParameters();
			if (presm != null)
				presm.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, "新增模板操作，数据库操作失败！");
			return returnErrMsg("新增模板操作，数据库操作失败！");
		}
		if (ret > 0) {
			return returnSuccMsg("String", id);
		}
		return returnErrMsg("新增模板操作，数据库操作失败！");
	}

	/**
	 * 根据id和模板信息，更新模板记录
	 * 
	 * @param conn:数据库连接
	 * @param id：图元编号
	 * @param obj：模板信息
	 * @return：更新结果
	 */
	protected ResultBean updateTemplate(Connection conn, String id,
			NCIEquipSymbolBean obj) {
		StringBuffer sql = new StringBuffer();
		PreparedStatement presm = null;
		int ret = -1;
		try {
			String type = controller.getDBManager().getDBType("SVG");
			DBSqlIdiomAdapter sqlIdiom = null;
			if (type != null) {
				// 通过方言转换类获取blob字段
				sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
				if (sqlIdiom == null)
					return returnErrMsg("更新模板操作，获取方言转换器失败！");
			}
			sql.append("UPDATE t_svg_symbol_template").append(
					" SET st_name = ?, st_variety = ?, ").append(
					"st_modify_time = ?, st_modify_person = ?, ").append(
					"st_validity = ?, st_param1 = ?, ").append(
					"st_param2 = ?, st_param3 = ?, ").append(
					"st_content=EMPTY_BLOB() WHERE st_id = ?");
			presm = conn.prepareStatement(sql.toString());

			// 获得当前服务器时间
			String sysdate = sqlIdiom.getSysDate(conn);

			presm.setString(1, obj.getName());
			presm.setString(2, obj.getVariety().getCode());
			presm.setString(3, sysdate);
			presm.setString(4, obj.getOperator());
			// presm.setString(4, obj.getModifyPerson());
			presm.setString(5, "1");
			presm.setString(6, obj.getParam1());
			presm.setString(7, obj.getParam2());
			presm.setString(8, obj.getParam3());
			presm.setString(9, id);

			ret = presm.executeUpdate();
			presm.clearParameters();
			if (presm != null)
				presm.close();
		} catch (Exception ex) {
			log.log(this, LoggerAdapter.ERROR, "更新模板操作，数据库操作失败！");
			return returnErrMsg("更新模板操作，数据库操作失败！");
		}
		if (ret > 0) {
			return returnSuccMsg("String", id);
		} else {
			return returnErrMsg("更新模板操作，数据库操作失败！");
		}
	}

	/**
	 * 根据id和模板内容，更新模板内容
	 * 
	 * @param conn:数据库连接
	 * @param id：图元编号
	 * @param content：模板内容
	 * @return：更新结果
	 */
	protected ResultBean updateTemplateContent(Connection conn, String id,
			String content) {
		int ret = -1;
		try {
			String type = controller.getDBManager().getDBType("SVG");
			if (type != null) {
				// 通过方言转换类获取blob字段
				DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
						.getDBSqlIdiom(type);
				if (sqlIdiom == null)
					return returnErrMsg("更新模板内容，获取方言转换器失败！");
				// 设置空BLOB
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT st_content FROM t_svg_symbol_template")
						.append(" WHERE st_id = '").append(id).append("'");
				log.log(this, LoggerAdapter.DEBUG, "更新模板内容：" + sql.toString());
				ret = sqlIdiom.setBlob(conn, sql.toString(), "st_content",
						content.getBytes());
			}
		} catch (Exception ex) {
			log.log(this, LoggerAdapter.ERROR, "更新模板内容，操作数据库失败！");
			return returnErrMsg("更新模板内容，操作数据库失败！");
		}
		if (ret > 0)
			return returnSuccMsg("String", id);
		else
			return returnErrMsg("更新模板内容，操作数据库失败！");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.graphstorage.GraphStorageManagerAdapter#getSupportFileType(java.lang.String)
	 */
	public ResultBean getSupportFileType(String bussID) {
		// sql语句
		StringBuffer sql = new StringBuffer();
		if (bussID != null && bussID.length() != 0) {
			sql.append("SELECT cc_name, cc_code FROM").append(
					" t_svg_indunorm_graphtyperela t,").append(
					" t_svg_code_commsets c").append(
					" WHERE t.ig_graph_business_type =  c.cc_code").append(
					" AND upper(c.cc_shortname) = 'SVG_BUSINESS_GRAPH_TYPE'")
					.append(" AND t.ig_validity = '1'").append(
							" AND t.ig_businesssys_id = '").append(bussID)
					.append("'");
		} else {
			// return mapType;
			return returnErrMsg("获取业务系统图形文件类型操作，未能获取业务系统编号！");
		}

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法获取代码列表！");
			// return null;
			return returnErrMsg("获取业务系统图形文件类型操作，无法获取数据库连接！");
		}
		LinkedHashMap mapType = new LinkedHashMap();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取业务系统文件类型：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			String name = null;
			String code = null;
			while (rs.next()) {
				name = rs.getString("cc_name");
				code = rs.getString("cc_code");
				mapType.put(name, code);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取业务系统图形文件类型操作，操作数据库失败！");
		} finally {
			controller.getDBManager().close(conn);
		}

		if (mapType.size() > 0)
			return returnSuccMsg("LinkedHashMap", mapType);
		else
			return returnErrMsg("获取业务系统图形文件类型操作，获取数据为空！");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.graphstorage.GraphStorageManagerAdapter#getFilesInformation(java.lang.String,
	 *      java.lang.String)
	 */
	public ResultBean getFilesInformation(String businessID,
			GraphFileBean fileBean) {
		// 获取存储表名
		String tableName = getTableName(businessID);
		if (tableName == null || tableName.length() <= 0 || fileBean == null) {
			return returnErrMsg("获取指定业务系统文件列表，获取存储表名失败！");
		}

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(tableName).append(" WHERE 1 = 1 ");
		if (fileBean.getFileType() != null
				&& fileBean.getFileType().length() > 0) {
			sql.append(" AND ").append("RG_GRAPH_TYPE = '").append(
					fileBean.getFileType()).append("' ");
		}
		if (fileBean.getBusiType() != null
				&& fileBean.getBusiType().length() > 0) {
			sql.append(" AND ").append("RG_GRAPH_BUSI_TYPE = '").append(
					fileBean.getBusiType()).append("' ");
		}
		if (fileBean.getFileFormat() != null
				&& fileBean.getFileFormat().length() > 0) {
			sql.append(" AND ").append("RG_GRAPH_FILE_TYPE = '").append(
					fileBean.getFileFormat()).append("' ");
		}
		if (fileBean.getOperator() != null
				&& fileBean.getOperator().length() > 0) {
			sql.append(" AND ").append("RG_MODIFY_PERSON = '").append(
					fileBean.getOperator()).append("' ");
		}
		for (int i = 0; i < 10; i++) {
			if (fileBean.getParams(i) != null
					&& fileBean.getParams(i).length() > 0) {
				sql.append(" AND ").append("RG_PARAM").append(i).append(" = '")
						.append(fileBean.getParams(i)).append("' ");
			}
		}
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获得文件列表！");
			return returnErrMsg("获取指定业务系统文件列表，无法获取有效数据库连接！");
		}

		ArrayList list = new ArrayList();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取文件列表：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			GraphFileBean bean = null;
			while (rs.next()) {
				String id = rs.getString("rg_id");
				String name = rs.getString("rg_graph_name");
				String graphType = rs.getString("rg_graph_type");
				String busiType = rs.getString("rg_graph_busi_type");
				String graphFormat = rs.getString("rg_graph_file_type");
				String operator = rs.getString("rg_modify_person");
				String time = rs.getString("rg_modify_time");
				String[] param = new String[10];
				for (int i = 0, size = param.length; i < size; i++) {
					param[i] = rs.getString("rg_param" + i);
				}

				bean = new GraphFileBean();
				bean.setID(id);
				bean.setBusiType(busiType);
				bean.setFileFormat(graphFormat);
				bean.setFileName(name);
				bean.setFileType(graphType);
				bean.setModifyTime(time);
				bean.setOperator(operator);
				bean.setParams(param);

				list.add(bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取指定业务系统文件列表，操作数据库失败！");
		} finally {
			controller.getDBManager().close(conn);
		}
		list.trimToSize();

		if (list.size() > 0)
			return returnSuccMsg("ArrayList", list);
		else
			return returnErrMsg("获取指定业务系统文件列表，获取数据为空！");
	}

	/**
	 * 根据业务系统编号获取该系统的图形保存库表名
	 * 
	 * @param businessID
	 * @return
	 */
	private String getTableName(String businessID) {
		String result = null;
		if (businessID != null && businessID.length() > 0) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM user_tables c, ").append(
					"(SELECT CONCAT('t_svg_rep_graph_', a.rm_table_relate)")
					.append(" AS table_name FROM t_svg_rep_manager a ").append(
							"WHERE a.rm_businesssys_id = '").append(businessID)
					.append("') b ").append(
							"WHERE UPPER(c.table_name) = UPPER(b.table_name)");
			// 获取数据库连接
			Connection conn = controller.getDBManager().getConnection("svg");
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取图形保存库表名！");
				return null;
			}

			try {
				Statement st = conn.createStatement();
				log.log(this, LoggerAdapter.DEBUG, "获取图形保存库表名："
						+ sql.toString());
				ResultSet rs = st.executeQuery(sql.toString());
				if (rs.next()) {
					result = rs.getString("table_name");
				}
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				result = null;
				log.log(this, LoggerAdapter.ERROR, e);
			} catch (Exception e) {
				result = null;
				log.log(this, LoggerAdapter.ERROR, e);
			} finally {
				controller.getDBManager().close(conn);
			}
		}

		return result;
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

	/**
	 * 保存图元与业务模型关联关系
	 * 
	 * @param conn:Connection:数据库连接
	 * @param symbolID:String:图元编号
	 * @param busiID:String:业务系统编号
	 * @param modelID:String:业务模型编号
	 * @return
	 */
	private ResultBean saveRelaBusiSymbol(Connection conn, String symbolID,
			String busiID, String modelID) {
		if (symbolID == null || symbolID.length() <= 0 || busiID == null
				|| busiID.length() <= 0 || modelID == null
				|| modelID.length() <= 0)
			return returnErrMsg("保存图元与业务模型关联关系，未能获取图元编号或业务系统编号或业务模型编号！");

		int ret = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) FROM t_svg_symbol_modelrela").append(
				" WHERE sm_symbol_id='").append(symbolID).append("'");
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取图元与业务模型关系：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			if (rs.next()) {
				ret = rs.getInt(1);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("保存图元与业务模型关联关系，检查记录是否存在，数据库操作失败！");
		}

		sql = new StringBuffer();
		if (ret > 0) {
			// 指定图元的关联数据已存在
			// UPDATE t_svg_symbol_modelrela SET sm_business_id='nest',
			// sm_module_id='sss' WHERE sm_symbol_id='111'
			sql.append("UPDATE t_svg_symbol_modelrela").append(
					" SET sm_business_id='").append(busiID).append(
					"', sm_module_id='").append(modelID).append(
					"' WHERE sm_symbol_id='").append(symbolID).append("'");
		} else {
			// 指定图元的关联数据不存在
			// INSERT INTO
			// t_svg_symbol_modelrela(sm_symbol_id,sm_business_id,sm_module_id)
			// VALUES('ddd','sss','yy')
			sql.append("INSERT INTO t_svg_symbol_modelrela ").append(
					"(sm_symbol_id,sm_business_id,sm_module_id)").append(
					" VALUES('").append(symbolID).append("', '").append(busiID)
					.append("', '").append(modelID).append("')");
		}

		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "保存图元与业务模型关系：" + sql.toString());
			ret = st.executeUpdate(sql.toString());
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("保存图元与业务模型关联关系，保存记录数据库操作失败！");
		}

		if (ret > 0)
			return returnSuccMsg("String", null);
		return returnErrMsg("保存图元与业务模型关联关系，保存记录数据库操作失败！");
	}

	/**
	 * 保存图形操作日志
	 * 
	 * @param conn:Connection:数据库连接
	 * @param rela:String:数据库标识
	 * @param logs:String:日志内容
	 * @param obj:GraphFileBean:图形对象
	 * @return
	 */
	private ResultBean saveGraphLog(Connection conn, String rela, String logs,
			GraphFileBean fileBean) {
		// // 获取图库标识
		// String rela = getRelateString(businessID);
		// if (rela == null || rela.length() <= 0)
		// return null;
		// *********************
		// 判断指定图形版本管理格式
		// 无\增量\替代
		// *********************
		String tableName = "t_svg_rep_verset_" + rela;
		String editionType = getGraphVersionType(conn, tableName, fileBean
				.getBusiType());

		if (VERSION_NULL.equals(editionType)) {
			// *************
			// 无版本管理操作
			// *************
			return returnSuccMsg("String", "要保存的图形没有进行版本控制！");
		} else if (VERSION_INCR.equals(editionType)) {
			// ****************
			// 增量式版本管理操作
			// ****************
			tableName = "t_svg_rep_verlog_" + rela;
			return SaveGraphLogIncr(conn, tableName, logs, fileBean);
		} else if (VERSION_SUBS.equals(editionType)) {
			// ****************
			// 替代式版本管理操作
			// ****************
			// tableName = "t_svg_rep_verhis_" + rela;
			return SaveGraphLogSubs(conn, rela, fileBean);
		} else {
			// **********************
			// 获取到的版本管理格式有问题
			// **********************
			return returnErrMsg("保存图形操作日志，获取图形版本管理设置信息出错！");
		}
	}

	/**
	 * 增量式版本管理操作日志存储
	 * 
	 * @param conn:Connection:数据库连接
	 * @param tableName:String:存储表名
	 * @param logs:String:操作日志
	 * @param fileBean:GraphFileBean:图形文件对象
	 * @return
	 */
	private ResultBean SaveGraphLogIncr(Connection conn, String tableName,
			String logs, GraphFileBean fileBean) {
		// 检查要保存的操作日志是否为空
		if (logs == null || logs.length() <= 0) {
			// logs = "没有接收到客户端数据，使用测试数据！";
			return returnSuccMsg("String", "没有需要保存的操作日志！");
		}
		// 获取方言转换器
		String type = controller.getDBManager().getDBType("SVG");
		DBSqlIdiomAdapter sqlIdiom = null;
		if (type != null) {
			// 通过方言转换类获取blob字段
			sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
			if (sqlIdiom == null) {
				return returnErrMsg("增量式版本管理操作，获取不到方言转换对象！");
			}
		}
		// 生成记录编号
		String recoderID = ServerUtilities.getUUIDString();
		// 获取文件编号
		String fileID = fileBean.getID();
		if (fileID == null || fileID.length() <= 0) {
			return returnErrMsg("增量式版本管理，未能获取到图形系统编号！");
		}
		// 获取文件名称
		String fileName = fileBean.getFileName();
		// 获取操作人
		String operator = fileBean.getOperator();
		// 获取操作时间
		String time = fileBean.getModifyTime();
		// 获取操作类型
		String changeType = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ").append(tableName).append(
				" (rv_id,rv_graph_id,rv_graph_name,").append(
				"rv_change_type,rv_change_person,").append(
				"rv_change_time,rv_change_conent) ").append(
				"VALUES (?,?,?,?,?,?,EMPTY_BLOB())");

		PreparedStatement presm = null;
		// **************
		// 保存记录基本信息
		// **************
		try {
			presm = conn.prepareStatement(sql.toString());
			presm.setString(1, recoderID);
			presm.setString(2, fileID);
			presm.setString(3, fileName);
			presm.setString(4, changeType);
			presm.setString(5, operator);
			presm.setString(6, time);

			presm.executeUpdate();
			presm.clearParameters();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("增量式版本管理，添加操作日志基本信息，操作数据库失败！");
		}

		// **************
		// 保存记录操作内容
		// **************
		sql = new StringBuffer();
		sql.append("SELECT rv_change_conent FROM ").append(tableName).append(
				" WHERE rv_id='").append(recoderID).append("'");
		int ret = sqlIdiom.setBlob(conn, sql.toString(), "rv_change_conent",
				logs.getBytes());

		if (ret > 0)
			return returnSuccMsg("String", "保存成功！");
		else
			return returnErrMsg("增量式版本管理，保存记录操作内容，数据库操作失败！");
	}

	/**
	 * 替代式版本管理操作日志存储
	 * 
	 * @param conn:Connection:数据库连接
	 * @param rela:String:获取图库标识
	 * @param fileBean:GraphFileBean:图形文件对象
	 * @return
	 */
	private ResultBean SaveGraphLogSubs(Connection conn, String rela,
			GraphFileBean fileBean) {
		// 生成记录编号
		String recoderID = ServerUtilities.getUUIDString();
		// 获取文件编号
		String fileID = fileBean.getID();
		if (fileID == null || fileID.length() <= 0) {
			return returnErrMsg("替代式版本管理操作，未能获取到图形系统编号！");
		}

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO t_svg_rep_verhis_").append(rela).append(
				" (rv_id,rv_graph_id,rv_graph_name,rv_graph_content,").append(
				"rv_modify_person,rv_modify_time) SELECT '").append(recoderID)
				.append("',a.rg_id,a.rg_graph_name,a.rg_content,").append(
						"a.rg_modify_person,a.rg_modify_time FROM").append(
						" t_svg_rep_graph_").append(rela).append(
						" a WHERE a.rg_id = '").append(fileBean.getID())
				.append("'");
		int ret = -1;
		try {
			log.log(this, LoggerAdapter.DEBUG, "替代式版本管理操作：" + sql.toString());
			PreparedStatement presm = conn.prepareStatement(sql.toString());
			ret = presm.executeUpdate();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("替代式版本管理操作，将图形移入历史表过程中数据库操作失败！");
		}

		if (ret > 0) {
			// 将替换式版本表中指定图形的多余版本删除
			// 每个图形最多保留10个版本
			sql = new StringBuffer();
			sql.append("delete from t_svg_rep_verhis_").append(rela).append(
					" where rv_id in (select rv_id").append(
					" from (select row_.*, rownum rownum_").append(
					" from (select * from t_svg_rep_verhis_").append(rela)
					.append(" where rv_graph_id = '").append(fileBean.getID())
					.append("' order by rv_modify_time desc) row_)").append(
							" where rownum_ > 10)");
			try {
				log.log(this, LoggerAdapter.DEBUG, "替代式版本管理操作："
						+ sql.toString());
				PreparedStatement presm = conn.prepareStatement(sql.toString());
				ret = presm.executeUpdate();
			} catch (SQLException e) {
				log.log(this, LoggerAdapter.ERROR, e);
				return returnErrMsg("替代式版本管理操作，将图形移入历史表过程中数据库操作失败！");
			}
		}

		return returnSuccMsg("String", "替代式版本管理操作成功！");
	}

	/**
	 * 获取指定业务图类型版本管理形式
	 * 
	 * @param conn:Connection:数据库连接
	 * @param tableName:String:版本管理配置表表名
	 * @param busiType:String:业务图类型值
	 * @return String:版本管理形式
	 */
	private String getGraphVersionType(Connection conn, String tableName,
			String busiType) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT rv_store_policy FROM ").append(tableName).append(
				" WHERE rv_graph_business_type='").append(busiType).append("'");
		String versionType = null;
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取指定业务图类型版本管理形式："
					+ sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			if (rs.next()) {
				versionType = rs.getString("rv_store_policy");
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			versionType = null;
		}

		return versionType;
	}

	/**
	 * 重命名symbol
	 */
	public String renameSymbol(String oldName, String newName,
			String symbolType, String operator) {
		String tableName = null;
		String columnName = null;
		String modifyTime = null;
		String modifyPerson = null;
		if (symbolType.equals(NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT)) {
			tableName = "t_svg_symbol_fileinfo";
			columnName = "sf_name";
			modifyTime = "sf_modify_time";
			modifyPerson = "sf_modify_person";
		} else if (symbolType.equals(NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			tableName = "t_svg_symbol_template";
			columnName = "st_name";
			modifyTime = "st_modify_time";
			modifyPerson = "st_modify_person";
		}
		if (tableName == null) {
			log.log(null, LoggerAdapter.ERROR, "symbol重命名时无法根据类型" + symbolType
					+ "找到指定类型的存储表!");
			return null;
		}
		StringBuffer sql = new StringBuffer("UPDATE ");
		sql.append(tableName);
		sql.append(" SET ");
		sql.append(columnName);
		sql.append("=?,");
		sql.append(modifyPerson);
		sql.append("=?,");
		sql.append(modifyTime);
		sql.append("=? ");
		sql.append("WHERE ");
		sql.append(columnName);
		sql.append("=?");
		Connection conn = controller.getDBManager().getConnection("svg");
		String type = controller.getDBManager().getDBType("SVG");
		DBSqlIdiomAdapter sqlIdiom = controller.getDBManager().getDBSqlIdiom(
				type);
		String sysDate = sqlIdiom.getSysDate(conn);
		try {
			PreparedStatement p = conn.prepareStatement(sql.toString());
			p.setString(1, newName);
			p.setString(2, operator);
			p.setString(3, sysDate);
			p.setString(4, oldName);

			int result = p.executeUpdate();
			p.close();
			if (result == 0) {
				return null;
			}
			return sysDate;
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
		} finally {
			controller.getDBManager().close(conn);
		}
		return null;
	}

	public int init() {
		// 获取管理组件对象
		controller = (ServerModuleControllerAdapter) parameters
				.get(ServerModuleControllerAdapter.class.toString());
		// 获取日志操作对象
		log = controller.getLogger();
		return super.init();
	}
}
