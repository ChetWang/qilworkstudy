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
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ�ͼ��洢��
 * 
 */
public class GraphStorageManagerImpl extends GraphStorageManagerAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5729203748520689029L;
	/**
	 * �����������
	 */
	private ServerModuleControllerAdapter controller;
	/**
	 * ��־����
	 */
	private LoggerAdapter log;

	/**
	 * �ް汾����
	 */
	private final String VERSION_NULL = "0";
	/**
	 * ���ʽ�汾����
	 */
	private final String VERSION_SUBS = "1";
	/**
	 * ����ʽ�汾����
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
			return returnErrMsg("ȡͼ���������ܻ�ȡҵ��ϵͳ��ţ�");
		// ��ȡ�洢����
		String tableName = getTableName(businessID);
		if (tableName == null || tableName.length() <= 0) {
			return returnErrMsg("ȡͼ������δ�ܻ�ȡ�洢������");
		}

		// ���ݴ���Ĳ���ȡͼ
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(tableName).append(" WHERE '1'='1'");
		if (obj.getID() != null && obj.getID().length() > 0) {
			sql.append(" AND rg_id = '").append(obj.getID()).append("'");
		}
		if (obj.getFileName() != null && obj.getFileName().length() > 0) {
			sql.append(" AND rg_graph_name = '").append(obj.getFileName())
					.append("' ");
		}

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷�����ļ��б�");
			return returnErrMsg("ȡͼ�������޷���ȡ��Ч���ݿ����ӣ�");
		}
		GraphFileBean fileBean = null;
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡ�ļ���" + sql.toString());
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
			return returnErrMsg("ȡͼ���������ݿ����ʧ�ܣ�");
		} finally {
			controller.getDBManager().close(conn);
		}

		if (fileBean != null)
			return returnSuccMsg("GraphFileBean", fileBean);
		else
			return returnErrMsg("ȡͼ��������ȡ����Ϊ�գ�");
	}

	/**
	 * �����ļ���Ż�ȡ�ļ�����
	 * 
	 * @param conn:Connection:���ݿ�����
	 * @param tableName:String:�洢����
	 * @param id:String:�ļ����
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
				// ͨ������ת�����ȡblob�ֶ�
				DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
						.getDBSqlIdiom(type);
				if (sqlIdiom == null)
					return graphContent;

				// ��ȡͼԪ����
				log.log(this, LoggerAdapter.DEBUG, "��ȡͼ�����ݣ�" + sql.toString());
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
			return returnErrMsg("ȡͼԪ������ͼԪ����Ϊ�գ�");
		}
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���ȡͼԪ��");
			return returnErrMsg("ȡͼԪ���޷���ȡ��Ч���ݿ����ӣ�");
		}
		String symbolContent = null;
		String type = controller.getDBManager().getDBType("SVG");
		if (type != null) {
			// ͨ������ת�����ȡblob�ֶ�
			DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
					.getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return returnErrMsg("ȡͼԪ���޷���ȡ����ת������");

			// ��ȡͼԪ����
			log.log(this, LoggerAdapter.DEBUG, "��ȡͼԪ���ݣ�" + sql.toString());
			byte[] content = sqlIdiom.getBlob(conn, sql.toString(), "content");

			if (content != null && content.length > 0)
				symbolContent = new String(content);
		}
		// �ر����ݿ�����
		controller.getDBManager().close(conn);

		if (symbolContent != null && symbolContent.length() > 0)
			return returnSuccMsg("String", symbolContent);
		else
			return returnErrMsg("ȡͼԪ����ȡ����Ϊ�գ�");
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
			return returnErrMsg("����ͼ���ļ���û�н��յ�ҵ��ϵͳ��ţ�");
		}
		// ��ȡͼ���ʶ
		String rela = getRelateString(businessID);
		if (rela == null || rela.length() <= 0) {
			return returnErrMsg("�洢ͼ���ļ�����ȡͼ���ʶʧ�ܣ�");
		}

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷�����ͼ�����ݣ�");
			return returnErrMsg("����ͼ���ļ���������Ч��ȡ���ݿ����ӣ�");
		}
		// ��ȡ����ת����
		String type = controller.getDBManager().getDBType("SVG");
		DBSqlIdiomAdapter sqlIdiom = null;
		if (type != null) {
			// ͨ������ת�����ȡblob�ֶ�
			sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
			if (sqlIdiom == null) {
				return returnErrMsg("����ͼ���ļ�ʱ����ȡ��������ת������");
			}
		}

		ResultBean rb = new ResultBean();
		try {
			conn.setAutoCommit(false);
			if (fileBean.getID() == null || fileBean.getID().length() <= 0) {
				// ����ͼ��Ϣ
				rb = AddNewGraph(conn, sqlIdiom, rela, fileBean);
			} else {
				// �汾����
				rb = saveGraphLog(conn, rela, logs, fileBean);
				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
					// ����ͼ��Ϣ
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
			return returnErrMsg("����ͼ���ļ����������ݿ�ʧ�ܣ�");
		} finally {
			controller.getDBManager().close(conn);
		}

		return rb;
	}

	/**
	 * �޸�ָ��ͼ���ļ���δ�ύ���ݣ�
	 * 
	 * @param conn:Connection:���ݿ�����
	 * @param sqlIdim:DBSqlIdiomAdapter:����ת����
	 * @param rela:String:���ݿ��ʶ
	 * @param fileBean:GraphFileBean:ͼ�ζ���
	 * @param logs:String:ͼ�β�����¼
	 * @return
	 */
	private ResultBean ModifyGraph(Connection conn, DBSqlIdiomAdapter sqlIdiom,
			String rela, GraphFileBean fileBean, String logs) {
		ResultBean rb = null;
		int ret = -1;
		try {
			// **************
			// �޸�ͼ�λ�����Ϣ
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

			// ��õ�ǰ������ʱ��
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
			// ����ͼ���ļ������ͼ���޸�ʱ��
			fileBean.setModifyTime(sysdate);

			// ***********
			// ����ͼ������
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
				rb = returnSuccMsg("String", "�ɹ��޸�ͼ���ļ������ݿ��У�");
			} else {
				rb = returnErrMsg("�޸�ָ��ͼ���ļ������ݿ����ʧ�ܣ�");
			}
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, "�޸�ָ��ͼ���ļ������ݿ����ʧ�ܣ�");
			return returnErrMsg("�޸�ָ��ͼ���ļ������ݿ����ʧ�ܣ�");
		}
		return rb;
	}

	/**
	 * ����ͼ���ļ������ݿ��У�δ�ύ���ݣ�
	 * 
	 * @param conn:Connection:���ݿ�����
	 * @param sqlIdim:DBSqlIdiomAdapter:����ת����
	 * @param rela:String:���ݿ��ʶ
	 * @param fileBean:GraphFileBean:ͼ�ζ���
	 * @return
	 */
	private ResultBean AddNewGraph(Connection conn, DBSqlIdiomAdapter sqlIdiom,
			String rela, GraphFileBean fileBean) {
		ResultBean rb = null;
		int ret = -1;
		try {
			// **************
			// ����ͼ�λ�����Ϣ
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
			// ���ɱ��
			String id = ServerUtilities.getUUIDString();

			// ��õ�ǰ������ʱ��
			String sysdate = sqlIdiom.getSysDate(conn);
			// ��õ�ǰ���ӵĿ�blob����
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
			// �����ļ�������ļ����
			fileBean.setID(id);
			// ����ͼ���ļ������ͼ���޸�ʱ��
			fileBean.setModifyTime(sysdate);

			// ***********
			// ����ͼ������
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
				rb = returnSuccMsg("String", "�ɹ�����ͼ���ļ������ݿ��У�");
			} else {
				rb = returnErrMsg("����ͼ���ļ������ݿ⣬���ݿ����ʧ�ܣ�");
			}
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, "����ͼ���ļ������ݿ⣬���ݿ����ʧ�ܣ�");
			return returnErrMsg("����ͼ���ļ������ݿ⣬���ݿ����ʧ�ܣ�");
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
		String type = getSameSymbol(obj.getName()); // ��ȡָ�����Ƶ�ͼԪ��ģ���Ƿ����
		// String id = str[0];
		String id = obj.getId();

		if (businessID == null || businessID.length() <= 0) {
			return returnErrMsg("����ͼԪʧ�ܣ�δ��ȡҵ��ϵͳ���!");
		}
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.DEBUG, "�޷���ȡ���ݿ����ӣ��޷�����ͼԪ��");
			return returnErrMsg("�޷���ȡ���ݿ����ӣ��޷�����ͼԪ��");
		}

		try {
			conn.setAutoCommit(false);
			if (strSymbolType.toLowerCase().equals(graphUnitType)) {
				// ********
				// ͼԪ����
				// ********
				if (id != null && id.length() == 36
						&& type.equals(graphUnitType)) {
					rb = updateSymbol(conn, id, obj);
				} else if (type == null) { // ָ��ͼԪ���Ʋ��������½�
					// ���ɱ��
					id = ServerUtilities.getUUIDString();
					rb = insertSymbol(conn, id, obj);
				} else { // Ҫ�����ͼԪ�����Ѵ���
					rb = returnErrMsg("����ͼԪʧ�ܣ�����ͬ��ͼԪ��ģ�壡");
				}

				String holdFlag = controller.getServiceSets("symbolSets",
						"holdModelFlag");
				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS
						&& holdFlag.equalsIgnoreCase("true")) {
					// ����ͼԪ��ҵ��ģ�͹�����ϵ
					rb = saveRelaBusiSymbol(conn, id, businessID, obj
							.getModelID());
				}

				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS
						&& content != null && content.length() > 0) {
					// ����ͼԪ����
					rb = updateSymbolContent(conn, id, content);
				}
			} else if (strSymbolType.toLowerCase().equals(templateType)) {
				// ********
				// ģ�屣��
				// ********
				// conn.setAutoCommit(false);
				if (id != null && id.length() == 36
						&& type.equals(templateType)) {
					rb = updateTemplate(conn, id, obj);
				} else if (type == null) { // ָ��ģ�����Ʋ��������½�
					// ���ɱ��
					id = ServerUtilities.getUUIDString();
					rb = insertTemplate(conn, id, obj);
				} else { // Ҫ�����ͼԪ�����Ѵ���
					rb = returnErrMsg("����ͼԪʧ�ܣ�����ͬ��ͼԪ��ģ�壡");
				}

				String holdFlag = controller.getServiceSets("symbolSets",
						"holdModelFlag");
				if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS
						&& holdFlag.equalsIgnoreCase("true")) {
					// ����ͼԪ��ҵ��ģ�͹�����ϵ
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
			return returnErrMsg("����ͼԪʧ�ܣ����ݿ����ʧ��!");
		} finally {
			controller.getDBManager().close(conn);
		}

		return rb;
	}

	/**
	 * ��ȡ���ݿ��е�ͼԪ���ģ�ͱ�����ָ�����Ƶ�ͼԪ��ģ��
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
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		String ret = null;
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ÿͻ�������嵥��");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡָ�����Ƶ�ͼԪ��ţ�" + sql.toString());
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
	 * ���ݴ����ҵ��ϵͳ��ź����ݿ����ӻ�ȡ��Ӧ��ͼ���ʶ
	 * 
	 * @param strBussID��ҵ��ϵͳ���
	 * @return��ͼ���ʶ���������򷵻�null
	 */
	public String getRelateString(String strBussID) {
		StringBuffer sql = new StringBuffer();
		String relate = null;
		// ���Ȼ�ȡ��ҵ��ϵͳͼ����
		sql
				.append(
						"SELECT rm_table_relate FROM t_svg_rep_manager WHERE rm_businesssys_id = '")
				.append(strBussID).append("'");

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ȡͼ���ʶ��");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡͼ���ʶ��" + sql.toString());
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
			// ����ͼԪ��ģ���б�
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
			// ����ͼԪ�б�
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
			// ����ģ���б�
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
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷����ͼԪ�嵥��");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡͼԪ�б�" + sql.toString());
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
				String symbolID = rs.getString("id"); // ��ȡͼԪ���
				symbolBean.setId(symbolID); // ����ͼԪ���
				symbolBean.setType(type); // ��ȡ��ͼԪ����ģ��
				symbolBean.setName(rs.getString("name")); // ��ȡͼԪ����
				// ��ȡͼԪ����
				symbolBean.setVariety(new SimpleCodeBean(tempVariety, rs
						.getString("varietyname")));
				symbolBean.setCreateTime(rs.getString("create_time")); // ��ȡͼԪ����ʱ��
				symbolBean.setModifyTime(rs.getString("modify_time")); // ��ȡͼԪ�޸�ʱ��
				symbolBean.setOperator(rs.getString("modify_person")); // ��ȡͼԪ�޸���
				symbolBean.setValidity(rs.getBoolean("validity")); // ��ȡͼԪ�Ƿ���Ч
				symbolBean.setReleased(rs.getBoolean("releaseflag")); // ��ȡͼԪ�Ƿ񱻷���
				symbolBean.setContent(getSymbolContent(symbolID)); // ��ȡͼԪ����
				symbolBean.setModelID(rs.getString("model_id")); // ��ȡͼԪ������ҵ��ģ�ͱ��

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
		// sql��ѯ���
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT sf_modify_person AS person").append(
				" FROM t_svg_symbol_fileinfo").append(" UNION").append(
				" SELECT st_modify_person AS person").append(
				" FROM t_svg_symbol_template");
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷����ͼԪ�嵥��");
			return null;
		}
		ArrayList list = new ArrayList();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡͼԪ����" + sql.toString());
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
	 * ����ͼԪ���ƻ�ȡ��ͼԪ�Ķ���
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

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷����ͼԪ�嵥��");
			return null;
		}
		NCIEquipSymbolBean symbolBean = null;
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡͼԪ����" + sql.toString());
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
				String symbolID = rs.getString("id"); // ��ȡͼԪ���
				symbolBean.setId(symbolID); // ����ͼԪ���
				symbolBean.setType(type); // ��ȡ��ͼԪ����ģ��
				symbolBean.setName(rs.getString("name")); // ��ȡͼԪ����
				// ��ȡͼԪ����
				symbolBean.setVariety(new SimpleCodeBean(tempVariety, rs
						.getString("varietyname")));
				symbolBean.setCreateTime(rs.getString("create_time")); // ��ȡͼԪ����ʱ��
				symbolBean.setModifyTime(rs.getString("modify_time")); // ��ȡͼԪ�޸�ʱ��
				symbolBean.setOperator(rs.getString("modify_person")); // ��ȡͼԪ�޸���
				symbolBean.setValidity(rs.getBoolean("validity")); // ��ȡͼԪ�Ƿ���Ч
				symbolBean.setReleased(rs.getBoolean("releaseflag")); // ��ȡͼԪ�Ƿ񱻷���
				symbolBean.setContent(getSymbolContent(symbolID)); // ��ȡͼԪ����
				symbolBean.setModelID(rs.getString("model_id")); // ��ȡͼԪ������ҵ��ģ�ͱ��
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
	 * ����ͼԪ��Ż�ȡͼԪ����
	 * 
	 * @param symbolID
	 * @return
	 */
	private String getSymbolContent(String symbolID) {
		byte[] content = null;
		String type = controller.getDBManager().getDBType("SVG");
		if (type != null) {
			// ͨ������ת�����ȡblob�ֶ�
			DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
					.getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return null;
			// ��ȡ���ݿ�����
			Connection conn = controller.getDBManager().getConnection("svg");

			StringBuffer sql = new StringBuffer();
			sql.append("SELECT sf_content FROM ").append(
					"(SELECT sf_content, sf_id ").append(
					"FROM t_svg_symbol_fileinfo ").append(" UNION ALL ")
					.append("SELECT st_content, st_id ").append(
							"FROM t_svg_symbol_template) a ").append(
							"WHERE a.sf_id = '").append(symbolID).append("'");
			// ��ȡ���������
			log.log(this, LoggerAdapter.DEBUG, "��ȡͼԪ���ݣ�" + sql.toString());
			content = sqlIdiom.getBlob(conn, sql.toString(), "sf_content");
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					log.log(this, LoggerAdapter.ERROR, "ȡͼԪ���ݣ��ر����ݿ����ӳ���");
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
	 * ����id��ͼԪ��Ϣ������ͼԪ��¼
	 * 
	 * @param conn:���ݿ�����
	 * @param id��ͼԪ���
	 * @param obj��ͼԪ��Ϣ
	 * @return���������
	 */
	protected ResultBean insertSymbol(Connection conn, String id,
			NCIEquipSymbolBean obj) {
		StringBuffer sql = new StringBuffer();
		String type = controller.getDBManager().getDBType("SVG");
		DBSqlIdiomAdapter sqlIdiom = null;
		int ret = 0;
		if (type != null) {
			// ͨ������ת�����ȡblob�ֶ�
			sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return returnErrMsg("����ͼԪ��������ȡ����ת����ʧ�ܣ�");
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
			// ��õ�ǰ������ʱ��
			String sysdate = sqlIdiom.getSysDate(conn);
			// // ��õ�ǰ���ӵĿ�blob����
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

			log.log(this, LoggerAdapter.DEBUG, "����ͼԪ��" + sql.toString());
			ret = presm.executeUpdate();
			presm.clearParameters();
			if (presm != null)
				presm.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, "����ͼԪ���������ݿ����ʧ�ܣ�");
			return returnErrMsg("����ͼԪ���������ݿ����ʧ�ܣ�");
		}
		if (ret > 0) {
			return returnSuccMsg("String", id);
		}
		return returnErrMsg("����ͼԪ���������ݿ����ʧ�ܣ�");
	}

	/**
	 * ����id��ͼԪ��Ϣ������ͼԪ��¼
	 * 
	 * @param conn:���ݿ�����
	 * @param id��ͼԪ���
	 * @param obj��ͼԪ��Ϣ
	 * @return�����½��
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
				// ͨ������ת�����ȡblob�ֶ�
				sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
				if (sqlIdiom == null) {
					return returnErrMsg("����ͼԪ��������ȡ����ת����ʧ�ܣ�");
				}
			}
			sql.append("UPDATE t_svg_symbol_fileinfo").append(
					" SET sf_name = ?, sf_variety = ?, ").append(
					"sf_modify_time = ?, sf_modify_person = ?, ").append(
					"sf_validity = ?, sf_content=EMPTY_BLOB() ").append(
					"WHERE sf_id = ?");
			presm = conn.prepareStatement(sql.toString());

			// ��õ�ǰ������ʱ��
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
			log.log(this, LoggerAdapter.ERROR, "����ͼԪ���������ݿ����ʧ�ܣ�");
			return returnErrMsg("����ͼԪ���������ݿ����ʧ�ܣ�");
		}

		if (ret > 0) {
			return returnSuccMsg("String", id);
		}
		return returnErrMsg("����ͼԪ���������ݿ����ʧ�ܣ�");
	}

	/**
	 * ����id��ͼԪ���ݣ�����ͼԪ����
	 * 
	 * @param conn:���ݿ�����
	 * @param id��ͼԪ���
	 * @param content��ͼԪ����
	 * @return�����½��
	 */
	protected ResultBean updateSymbolContent(Connection conn, String id,
			String content) {
		int ret = 0;
		try {
			String type = controller.getDBManager().getDBType("SVG");
			if (type != null) {
				// ͨ������ת�����ȡblob�ֶ�
				DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
						.getDBSqlIdiom(type);
				if (sqlIdiom == null)
					return returnErrMsg("����ͼԪ���ݣ���ȡ����ת����ʧ�ܣ�");

				StringBuffer sql = new StringBuffer();
				sql.append("SELECT sf_content FROM t_svg_symbol_fileinfo")
						.append(" WHERE sf_id = '").append(id).append("'");
				// ����ͼԪ����
				log.log(this, LoggerAdapter.DEBUG, "����ͼԪ���ݣ�" + sql.toString());
				ret = sqlIdiom.setBlob(conn, sql.toString(), "sf_content",
						content.getBytes());
			}
		} catch (Exception ex) {
			log.log(this, LoggerAdapter.ERROR, "����ͼԪ���ݣ��������ݿ�ʧ�ܣ�");
			return returnErrMsg("����ͼԪ���ݣ��������ݿ�ʧ�ܣ�");
		}
		if (ret > 0)
			return returnSuccMsg("String", id);
		return returnErrMsg("����ͼԪ���ݣ��������ݿ�ʧ�ܣ�");
	}

	/**
	 * ����id��ģ����Ϣ������ģ���¼
	 * 
	 * @param conn:���ݿ�����
	 * @param id��ͼԪ���
	 * @param obj��ģ����Ϣ
	 * @return���������
	 */
	protected ResultBean insertTemplate(Connection conn, String id,
			NCIEquipSymbolBean obj) {
		StringBuffer sql = new StringBuffer();
		String type = controller.getDBManager().getDBType("SVG");
		DBSqlIdiomAdapter sqlIdiom = null;
		if (type != null) {
			// ͨ������ת�����ȡblob�ֶ�
			sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return returnErrMsg("����ģ���������ȡ����ת����ʧ�ܣ�");
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
			// ��õ�ǰ������ʱ��
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
			log.log(this, LoggerAdapter.ERROR, "����ģ����������ݿ����ʧ�ܣ�");
			return returnErrMsg("����ģ����������ݿ����ʧ�ܣ�");
		}
		if (ret > 0) {
			return returnSuccMsg("String", id);
		}
		return returnErrMsg("����ģ����������ݿ����ʧ�ܣ�");
	}

	/**
	 * ����id��ģ����Ϣ������ģ���¼
	 * 
	 * @param conn:���ݿ�����
	 * @param id��ͼԪ���
	 * @param obj��ģ����Ϣ
	 * @return�����½��
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
				// ͨ������ת�����ȡblob�ֶ�
				sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
				if (sqlIdiom == null)
					return returnErrMsg("����ģ���������ȡ����ת����ʧ�ܣ�");
			}
			sql.append("UPDATE t_svg_symbol_template").append(
					" SET st_name = ?, st_variety = ?, ").append(
					"st_modify_time = ?, st_modify_person = ?, ").append(
					"st_validity = ?, st_param1 = ?, ").append(
					"st_param2 = ?, st_param3 = ?, ").append(
					"st_content=EMPTY_BLOB() WHERE st_id = ?");
			presm = conn.prepareStatement(sql.toString());

			// ��õ�ǰ������ʱ��
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
			log.log(this, LoggerAdapter.ERROR, "����ģ����������ݿ����ʧ�ܣ�");
			return returnErrMsg("����ģ����������ݿ����ʧ�ܣ�");
		}
		if (ret > 0) {
			return returnSuccMsg("String", id);
		} else {
			return returnErrMsg("����ģ����������ݿ����ʧ�ܣ�");
		}
	}

	/**
	 * ����id��ģ�����ݣ�����ģ������
	 * 
	 * @param conn:���ݿ�����
	 * @param id��ͼԪ���
	 * @param content��ģ������
	 * @return�����½��
	 */
	protected ResultBean updateTemplateContent(Connection conn, String id,
			String content) {
		int ret = -1;
		try {
			String type = controller.getDBManager().getDBType("SVG");
			if (type != null) {
				// ͨ������ת�����ȡblob�ֶ�
				DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
						.getDBSqlIdiom(type);
				if (sqlIdiom == null)
					return returnErrMsg("����ģ�����ݣ���ȡ����ת����ʧ�ܣ�");
				// ���ÿ�BLOB
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT st_content FROM t_svg_symbol_template")
						.append(" WHERE st_id = '").append(id).append("'");
				log.log(this, LoggerAdapter.DEBUG, "����ģ�����ݣ�" + sql.toString());
				ret = sqlIdiom.setBlob(conn, sql.toString(), "st_content",
						content.getBytes());
			}
		} catch (Exception ex) {
			log.log(this, LoggerAdapter.ERROR, "����ģ�����ݣ��������ݿ�ʧ�ܣ�");
			return returnErrMsg("����ģ�����ݣ��������ݿ�ʧ�ܣ�");
		}
		if (ret > 0)
			return returnSuccMsg("String", id);
		else
			return returnErrMsg("����ģ�����ݣ��������ݿ�ʧ�ܣ�");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.graphstorage.GraphStorageManagerAdapter#getSupportFileType(java.lang.String)
	 */
	public ResultBean getSupportFileType(String bussID) {
		// sql���
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
			return returnErrMsg("��ȡҵ��ϵͳͼ���ļ����Ͳ�����δ�ܻ�ȡҵ��ϵͳ��ţ�");
		}

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���ȡ�����б�");
			// return null;
			return returnErrMsg("��ȡҵ��ϵͳͼ���ļ����Ͳ������޷���ȡ���ݿ����ӣ�");
		}
		LinkedHashMap mapType = new LinkedHashMap();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡҵ��ϵͳ�ļ����ͣ�" + sql.toString());
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
			return returnErrMsg("��ȡҵ��ϵͳͼ���ļ����Ͳ������������ݿ�ʧ�ܣ�");
		} finally {
			controller.getDBManager().close(conn);
		}

		if (mapType.size() > 0)
			return returnSuccMsg("LinkedHashMap", mapType);
		else
			return returnErrMsg("��ȡҵ��ϵͳͼ���ļ����Ͳ�������ȡ����Ϊ�գ�");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.graphstorage.GraphStorageManagerAdapter#getFilesInformation(java.lang.String,
	 *      java.lang.String)
	 */
	public ResultBean getFilesInformation(String businessID,
			GraphFileBean fileBean) {
		// ��ȡ�洢����
		String tableName = getTableName(businessID);
		if (tableName == null || tableName.length() <= 0 || fileBean == null) {
			return returnErrMsg("��ȡָ��ҵ��ϵͳ�ļ��б���ȡ�洢����ʧ�ܣ�");
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
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷�����ļ��б�");
			return returnErrMsg("��ȡָ��ҵ��ϵͳ�ļ��б��޷���ȡ��Ч���ݿ����ӣ�");
		}

		ArrayList list = new ArrayList();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡ�ļ��б�" + sql.toString());
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
			return returnErrMsg("��ȡָ��ҵ��ϵͳ�ļ��б��������ݿ�ʧ�ܣ�");
		} finally {
			controller.getDBManager().close(conn);
		}
		list.trimToSize();

		if (list.size() > 0)
			return returnSuccMsg("ArrayList", list);
		else
			return returnErrMsg("��ȡָ��ҵ��ϵͳ�ļ��б���ȡ����Ϊ�գ�");
	}

	/**
	 * ����ҵ��ϵͳ��Ż�ȡ��ϵͳ��ͼ�α�������
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
			// ��ȡ���ݿ�����
			Connection conn = controller.getDBManager().getConnection("svg");
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ȡͼ�α���������");
				return null;
			}

			try {
				Statement st = conn.createStatement();
				log.log(this, LoggerAdapter.DEBUG, "��ȡͼ�α���������"
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
	 * ���ݴ�������ʹ���ֵ����ȡ��������
	 * 
	 * @param codeType:String:�������
	 * @param codeValue:String:����ֵ
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

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ȡ�������ƣ�");
			return null;
		}

		String name = null;
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡ�������ƣ�" + sql.toString());
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
	 * ����ͼԪ��ҵ��ģ�͹�����ϵ
	 * 
	 * @param conn:Connection:���ݿ�����
	 * @param symbolID:String:ͼԪ���
	 * @param busiID:String:ҵ��ϵͳ���
	 * @param modelID:String:ҵ��ģ�ͱ��
	 * @return
	 */
	private ResultBean saveRelaBusiSymbol(Connection conn, String symbolID,
			String busiID, String modelID) {
		if (symbolID == null || symbolID.length() <= 0 || busiID == null
				|| busiID.length() <= 0 || modelID == null
				|| modelID.length() <= 0)
			return returnErrMsg("����ͼԪ��ҵ��ģ�͹�����ϵ��δ�ܻ�ȡͼԪ��Ż�ҵ��ϵͳ��Ż�ҵ��ģ�ͱ�ţ�");

		int ret = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) FROM t_svg_symbol_modelrela").append(
				" WHERE sm_symbol_id='").append(symbolID).append("'");
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡͼԪ��ҵ��ģ�͹�ϵ��" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			if (rs.next()) {
				ret = rs.getInt(1);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("����ͼԪ��ҵ��ģ�͹�����ϵ������¼�Ƿ���ڣ����ݿ����ʧ�ܣ�");
		}

		sql = new StringBuffer();
		if (ret > 0) {
			// ָ��ͼԪ�Ĺ��������Ѵ���
			// UPDATE t_svg_symbol_modelrela SET sm_business_id='nest',
			// sm_module_id='sss' WHERE sm_symbol_id='111'
			sql.append("UPDATE t_svg_symbol_modelrela").append(
					" SET sm_business_id='").append(busiID).append(
					"', sm_module_id='").append(modelID).append(
					"' WHERE sm_symbol_id='").append(symbolID).append("'");
		} else {
			// ָ��ͼԪ�Ĺ������ݲ�����
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
			log.log(this, LoggerAdapter.DEBUG, "����ͼԪ��ҵ��ģ�͹�ϵ��" + sql.toString());
			ret = st.executeUpdate(sql.toString());
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("����ͼԪ��ҵ��ģ�͹�����ϵ�������¼���ݿ����ʧ�ܣ�");
		}

		if (ret > 0)
			return returnSuccMsg("String", null);
		return returnErrMsg("����ͼԪ��ҵ��ģ�͹�����ϵ�������¼���ݿ����ʧ�ܣ�");
	}

	/**
	 * ����ͼ�β�����־
	 * 
	 * @param conn:Connection:���ݿ�����
	 * @param rela:String:���ݿ��ʶ
	 * @param logs:String:��־����
	 * @param obj:GraphFileBean:ͼ�ζ���
	 * @return
	 */
	private ResultBean saveGraphLog(Connection conn, String rela, String logs,
			GraphFileBean fileBean) {
		// // ��ȡͼ���ʶ
		// String rela = getRelateString(businessID);
		// if (rela == null || rela.length() <= 0)
		// return null;
		// *********************
		// �ж�ָ��ͼ�ΰ汾�����ʽ
		// ��\����\���
		// *********************
		String tableName = "t_svg_rep_verset_" + rela;
		String editionType = getGraphVersionType(conn, tableName, fileBean
				.getBusiType());

		if (VERSION_NULL.equals(editionType)) {
			// *************
			// �ް汾�������
			// *************
			return returnSuccMsg("String", "Ҫ�����ͼ��û�н��а汾���ƣ�");
		} else if (VERSION_INCR.equals(editionType)) {
			// ****************
			// ����ʽ�汾�������
			// ****************
			tableName = "t_svg_rep_verlog_" + rela;
			return SaveGraphLogIncr(conn, tableName, logs, fileBean);
		} else if (VERSION_SUBS.equals(editionType)) {
			// ****************
			// ���ʽ�汾�������
			// ****************
			// tableName = "t_svg_rep_verhis_" + rela;
			return SaveGraphLogSubs(conn, rela, fileBean);
		} else {
			// **********************
			// ��ȡ���İ汾�����ʽ������
			// **********************
			return returnErrMsg("����ͼ�β�����־����ȡͼ�ΰ汾����������Ϣ����");
		}
	}

	/**
	 * ����ʽ�汾���������־�洢
	 * 
	 * @param conn:Connection:���ݿ�����
	 * @param tableName:String:�洢����
	 * @param logs:String:������־
	 * @param fileBean:GraphFileBean:ͼ���ļ�����
	 * @return
	 */
	private ResultBean SaveGraphLogIncr(Connection conn, String tableName,
			String logs, GraphFileBean fileBean) {
		// ���Ҫ����Ĳ�����־�Ƿ�Ϊ��
		if (logs == null || logs.length() <= 0) {
			// logs = "û�н��յ��ͻ������ݣ�ʹ�ò������ݣ�";
			return returnSuccMsg("String", "û����Ҫ����Ĳ�����־��");
		}
		// ��ȡ����ת����
		String type = controller.getDBManager().getDBType("SVG");
		DBSqlIdiomAdapter sqlIdiom = null;
		if (type != null) {
			// ͨ������ת�����ȡblob�ֶ�
			sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
			if (sqlIdiom == null) {
				return returnErrMsg("����ʽ�汾�����������ȡ��������ת������");
			}
		}
		// ���ɼ�¼���
		String recoderID = ServerUtilities.getUUIDString();
		// ��ȡ�ļ����
		String fileID = fileBean.getID();
		if (fileID == null || fileID.length() <= 0) {
			return returnErrMsg("����ʽ�汾����δ�ܻ�ȡ��ͼ��ϵͳ��ţ�");
		}
		// ��ȡ�ļ�����
		String fileName = fileBean.getFileName();
		// ��ȡ������
		String operator = fileBean.getOperator();
		// ��ȡ����ʱ��
		String time = fileBean.getModifyTime();
		// ��ȡ��������
		String changeType = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ").append(tableName).append(
				" (rv_id,rv_graph_id,rv_graph_name,").append(
				"rv_change_type,rv_change_person,").append(
				"rv_change_time,rv_change_conent) ").append(
				"VALUES (?,?,?,?,?,?,EMPTY_BLOB())");

		PreparedStatement presm = null;
		// **************
		// �����¼������Ϣ
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
			return returnErrMsg("����ʽ�汾������Ӳ�����־������Ϣ���������ݿ�ʧ�ܣ�");
		}

		// **************
		// �����¼��������
		// **************
		sql = new StringBuffer();
		sql.append("SELECT rv_change_conent FROM ").append(tableName).append(
				" WHERE rv_id='").append(recoderID).append("'");
		int ret = sqlIdiom.setBlob(conn, sql.toString(), "rv_change_conent",
				logs.getBytes());

		if (ret > 0)
			return returnSuccMsg("String", "����ɹ���");
		else
			return returnErrMsg("����ʽ�汾���������¼�������ݣ����ݿ����ʧ�ܣ�");
	}

	/**
	 * ���ʽ�汾���������־�洢
	 * 
	 * @param conn:Connection:���ݿ�����
	 * @param rela:String:��ȡͼ���ʶ
	 * @param fileBean:GraphFileBean:ͼ���ļ�����
	 * @return
	 */
	private ResultBean SaveGraphLogSubs(Connection conn, String rela,
			GraphFileBean fileBean) {
		// ���ɼ�¼���
		String recoderID = ServerUtilities.getUUIDString();
		// ��ȡ�ļ����
		String fileID = fileBean.getID();
		if (fileID == null || fileID.length() <= 0) {
			return returnErrMsg("���ʽ�汾���������δ�ܻ�ȡ��ͼ��ϵͳ��ţ�");
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
			log.log(this, LoggerAdapter.DEBUG, "���ʽ�汾���������" + sql.toString());
			PreparedStatement presm = conn.prepareStatement(sql.toString());
			ret = presm.executeUpdate();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("���ʽ�汾�����������ͼ��������ʷ����������ݿ����ʧ�ܣ�");
		}

		if (ret > 0) {
			// ���滻ʽ�汾����ָ��ͼ�εĶ���汾ɾ��
			// ÿ��ͼ����ౣ��10���汾
			sql = new StringBuffer();
			sql.append("delete from t_svg_rep_verhis_").append(rela).append(
					" where rv_id in (select rv_id").append(
					" from (select row_.*, rownum rownum_").append(
					" from (select * from t_svg_rep_verhis_").append(rela)
					.append(" where rv_graph_id = '").append(fileBean.getID())
					.append("' order by rv_modify_time desc) row_)").append(
							" where rownum_ > 10)");
			try {
				log.log(this, LoggerAdapter.DEBUG, "���ʽ�汾���������"
						+ sql.toString());
				PreparedStatement presm = conn.prepareStatement(sql.toString());
				ret = presm.executeUpdate();
			} catch (SQLException e) {
				log.log(this, LoggerAdapter.ERROR, e);
				return returnErrMsg("���ʽ�汾�����������ͼ��������ʷ����������ݿ����ʧ�ܣ�");
			}
		}

		return returnSuccMsg("String", "���ʽ�汾��������ɹ���");
	}

	/**
	 * ��ȡָ��ҵ��ͼ���Ͱ汾������ʽ
	 * 
	 * @param conn:Connection:���ݿ�����
	 * @param tableName:String:�汾�������ñ����
	 * @param busiType:String:ҵ��ͼ����ֵ
	 * @return String:�汾������ʽ
	 */
	private String getGraphVersionType(Connection conn, String tableName,
			String busiType) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT rv_store_policy FROM ").append(tableName).append(
				" WHERE rv_graph_business_type='").append(busiType).append("'");
		String versionType = null;
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡָ��ҵ��ͼ���Ͱ汾������ʽ��"
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
	 * ������symbol
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
			log.log(null, LoggerAdapter.ERROR, "symbol������ʱ�޷���������" + symbolType
					+ "�ҵ�ָ�����͵Ĵ洢��!");
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
		// ��ȡ�����������
		controller = (ServerModuleControllerAdapter) parameters
				.get(ServerModuleControllerAdapter.class.toString());
		// ��ȡ��־��������
		log = controller.getLogger();
		return super.init();
	}
}
