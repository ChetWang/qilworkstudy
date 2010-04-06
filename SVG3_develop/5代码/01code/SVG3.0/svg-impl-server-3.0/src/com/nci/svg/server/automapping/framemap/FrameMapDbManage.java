package com.nci.svg.server.automapping.framemap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.nci.svg.sdk.bean.GraphFileBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;
import com.nci.svg.sdk.server.util.ServerUtilities;

/**
 * <p>
 * 标题：FrameMapDbManage.java
 * </p>
 * <p>
 * 描述： 结构图的数据库操作
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-7-1
 * @version 1.0
 */
public class FrameMapDbManage {
	/**
	 * 调用类的控制操作对象
	 */
	private ServerModuleControllerAdapter controller;
	/**
	 * 日志操作对象
	 */
	private LoggerAdapter log;

	public FrameMapDbManage(ServerModuleControllerAdapter controller) {
		this.controller = controller;
		this.log = controller.getLogger();
	}

	/**
	 * 2009-7-1 Add by ZHM
	 * 
	 * @功能 从数据库中获取结构图
	 * @param businessID:String:业务系统编号
	 * @param obj:GraphFileBean:文件属性
	 * @return
	 */
	public ResultBean loadGraph(String businessID, GraphFileBean obj) {
		if (businessID == null || businessID.length() <= 0)
			return returnErrMsg("取结构图操作，不能获取业务系统编号！");
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(null, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取图库标识！");
			return null;
		}
		// 获取图库标识
		String rela = getRelateString(businessID, conn);
		if (rela == null || rela.length() <= 0) {
			return returnErrMsg("取结构图操作，获取图标标识失败！");
		}

		// 获取方言转换器
		String type = controller.getDBManager().getDBType("SVG");
		DBSqlIdiomAdapter sqlIdiom = null;
		if (type != null) {
			// 通过方言转换类获取blob字段
			sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
			if (sqlIdiom == null) {
				return returnErrMsg("取结构图操作时，获取不到方言转换对象！");
			}
		}

		// 根据传入的参数取图
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM t_svg_rep_graph_").append(rela).append(
				" WHERE '1'='1'");
		if (obj.getFileName() != null && obj.getFileName().length() > 0) {
			sql.append(" AND rg_graph_name = '").append(obj.getFileName())
					.append("' ");
		}

		GraphFileBean fileBean = null;
		try {
			Statement st = conn.createStatement();
			log.log(null, LoggerAdapter.DEBUG, "获取文件：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());

			if (rs.next()) {
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

				fileBean = new GraphFileBean();
				fileBean.setID(id);
				fileBean.setBusiType(busiType);
				fileBean.setFileFormat(graphFormat);
				fileBean.setFileName(name);
				fileBean.setFileType(graphType);
				fileBean.setModifyTime(time);
				fileBean.setOperator(operator);
				fileBean.setParams(param);
				String content = loadGraphContent(conn, "t_svg_rep_graph_"
						+ rela, id);
				fileBean.setContent(content);
			}

			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(null, LoggerAdapter.ERROR, e);
			return returnErrMsg("取结构图操作，数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}

		if (fileBean != null)
			return returnSuccMsg("GraphFileBean", fileBean);
		else
			return returnErrMsg("取结构图操作，获取数据为空！");
	}

	/**
	 * 2009-7-1 Add by ZHM
	 * 
	 * @功能 删除指定结构图
	 * @param businessID:String:业务系统编号
	 * @param obj:GraphFileBean:文件属性
	 * @return
	 */
	public ResultBean delGraph(String businessID, GraphFileBean obj) {
		if (businessID == null || businessID.length() <= 0) {
			return returnErrMsg("删除结构图形文件，没有接收到业务系统编号！");
		}
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(null, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取图库标识！");
			return null;
		}
		// 获取图库标识
		String rela = getRelateString(businessID, conn);
		if (rela == null || rela.length() <= 0) {
			return returnErrMsg("删除结构图形文件，获取图标标识失败！");
		}
		String filename = obj.getFileName();
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM t_svg_rep_graph_").append(rela).append(
				" WHERE rg_graph_name='").append(filename).append("'");
		try {
			log.log(null, LoggerAdapter.DEBUG, "删除结构图形文件："
					+ sql.toString());
			PreparedStatement presm = conn.prepareStatement(sql.toString());
			presm.executeUpdate();
		} catch (SQLException e) {
			log.log(null, LoggerAdapter.ERROR, e);
			return returnErrMsg("保存结构图形文件，将图形移入历史表过程中数据库操作失败！");
		}
		return returnSuccMsg("String", "成功删除:"+filename);
	}

	/**
	 * 2009-7-1 Add by ZHM
	 * 
	 * @功能 保存结构图到数据库中
	 * @param businessID:String:业务系统编号
	 * @param obj:GraphFileBean:文件属性
	 * @return
	 */
	public ResultBean saveGraph(String businessID, GraphFileBean fileBean) {
		if (businessID == null || businessID.length() <= 0) {
			return returnErrMsg("保存图形文件，没有接收到业务系统编号！");
		}
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(null, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取图库标识！");
			return null;
		}
		// 获取图库标识
		String rela = getRelateString(businessID, conn);
		if (rela == null || rela.length() <= 0) {
			return returnErrMsg("存储图形文件，获取图标标识失败！");
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

		// 判断数据库中的文件是否存在
		String tablename = "t_svg_rep_graph_" + rela;
		String filename = fileBean.getFileName();
		String fileID = graphExist(tablename, filename, conn);
		ResultBean rb = null;
		try {
			conn.setAutoCommit(false);
			if (fileID != null && fileID.length() == 36) {
				// 修改文件
				fileBean.setID(fileID);
				rb = ModifyGraph(conn, sqlIdiom, rela, fileBean);
			} else {
				// 新增文件
				rb = AddNewGraph(conn, sqlIdiom, rela, fileBean);
			}
			conn.setAutoCommit(true);
			if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
				conn.commit();
				// rb = returnSuccMsg("String", "成功将图形文件到数据库中！");
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			log.log(null, LoggerAdapter.ERROR, e);
			return returnErrMsg("保存图形文件，操作数据库失败！");
		} finally {
			controller.getDBManager().close(conn);
		}

		return rb;
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
				log.log(null, LoggerAdapter.DEBUG, "获取图形内容：" + sql.toString());
				byte[] content = sqlIdiom.getBlob(conn, sql.toString(),
						"rg_content");

				if (content != null && content.length > 0)
					graphContent = new String(content);
			}
		} catch (Exception ex) {
			log.log(null, LoggerAdapter.ERROR, ex);
			graphContent = null;
		}
		return graphContent;
	}

	/**
	 * 修改指定图形文件（未提交数据）
	 * 
	 * @param conn:Connection:数据库连接
	 * @param sqlIdim:DBSqlIdiomAdapter:方言转换器
	 * @param rela:String:数据库标识
	 * @param fileBean:GraphFileBean:图形对象
	 * @return
	 */
	private ResultBean ModifyGraph(Connection conn, DBSqlIdiomAdapter sqlIdiom,
			String rela, GraphFileBean fileBean) {
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
			log.log(null, LoggerAdapter.ERROR, "修改指定图形文件，数据库操作失败！");
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
			log.log(null, LoggerAdapter.ERROR, "新增图形文件到数据库，数据库操作失败！");
			return returnErrMsg("新增图形文件到数据库，数据库操作失败！");
		}
		return rb;
	}

	/**
	 * 2009-7-1 Add by ZHM
	 * 
	 * @功能 指定文件是否已存在于数据库中
	 * @param tablename:String:表名
	 * @param filename:String:文件名
	 * @param conn:Connection:数据库连接
	 * @return
	 */
	private String graphExist(String tablename, String filename, Connection conn) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT rg_id FROM ").append(tablename).append(
				" WHERE rg_graph_name='").append(filename).append("'");
		String fileID = null;
		try {
			Statement st = conn.createStatement();
			log.log(null, LoggerAdapter.DEBUG, "获取指定文件是否已存在：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());

			if (rs.next()) {
				fileID = rs.getString(1);
			}

			if (rs != null)
				rs.close();
		} catch (Exception ex) {
			log.log(null, LoggerAdapter.ERROR, ex);
			fileID = null;
		}
		return fileID;
	}

	/**
	 * 根据传入的业务系统编号和数据库连接获取对应的图库标识
	 * 
	 * @param strBussID：业务系统编号
	 * @return：图库标识，不存在则返回null
	 */
	private String getRelateString(String strBussID, Connection conn) {
		StringBuffer sql = new StringBuffer();
		String relate = null;
		// 首先获取该业务系统图库标记
		sql
				.append(
						"SELECT rm_table_relate FROM t_svg_rep_manager WHERE rm_businesssys_id = '")
				.append(strBussID).append("'");

		// 获取数据库连接
		if (conn == null) {
			log.log(null, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获取图库标识！");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(null, LoggerAdapter.DEBUG, "获取图库标识：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());

			if (rs.next()) {
				relate = rs.getString("rm_table_relate");
			}
		} catch (Exception ex) {
			log.log(null, LoggerAdapter.ERROR, ex);
			relate = null;
		}

		return relate;
	}

	/**
	 * 设置操作失败返回对象
	 * 
	 * @param text:String:错误信息
	 * @return 返回值对象
	 */
	private ResultBean returnErrMsg(String text) {
		ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR, text, null,
				null);
		return bean;
	}

	/**
	 * 设置操作成功返回对象
	 * 
	 * @param type:String:返回对象类型
	 * @param obj:Object:返回对象值
	 * @return 返回值对象
	 */
	private ResultBean returnSuccMsg(String type, Object obj) {
		return new ResultBean(ResultBean.RETURN_SUCCESS, null, type, obj);
	}
}
