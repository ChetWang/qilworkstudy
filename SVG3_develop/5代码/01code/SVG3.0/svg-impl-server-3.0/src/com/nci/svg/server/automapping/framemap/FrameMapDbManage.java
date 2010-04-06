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
 * ���⣺FrameMapDbManage.java
 * </p>
 * <p>
 * ������ �ṹͼ�����ݿ����
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-7-1
 * @version 1.0
 */
public class FrameMapDbManage {
	/**
	 * ������Ŀ��Ʋ�������
	 */
	private ServerModuleControllerAdapter controller;
	/**
	 * ��־��������
	 */
	private LoggerAdapter log;

	public FrameMapDbManage(ServerModuleControllerAdapter controller) {
		this.controller = controller;
		this.log = controller.getLogger();
	}

	/**
	 * 2009-7-1 Add by ZHM
	 * 
	 * @���� �����ݿ��л�ȡ�ṹͼ
	 * @param businessID:String:ҵ��ϵͳ���
	 * @param obj:GraphFileBean:�ļ�����
	 * @return
	 */
	public ResultBean loadGraph(String businessID, GraphFileBean obj) {
		if (businessID == null || businessID.length() <= 0)
			return returnErrMsg("ȡ�ṹͼ���������ܻ�ȡҵ��ϵͳ��ţ�");
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(null, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ȡͼ���ʶ��");
			return null;
		}
		// ��ȡͼ���ʶ
		String rela = getRelateString(businessID, conn);
		if (rela == null || rela.length() <= 0) {
			return returnErrMsg("ȡ�ṹͼ��������ȡͼ���ʶʧ�ܣ�");
		}

		// ��ȡ����ת����
		String type = controller.getDBManager().getDBType("SVG");
		DBSqlIdiomAdapter sqlIdiom = null;
		if (type != null) {
			// ͨ������ת�����ȡblob�ֶ�
			sqlIdiom = controller.getDBManager().getDBSqlIdiom(type);
			if (sqlIdiom == null) {
				return returnErrMsg("ȡ�ṹͼ����ʱ����ȡ��������ת������");
			}
		}

		// ���ݴ���Ĳ���ȡͼ
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
			log.log(null, LoggerAdapter.DEBUG, "��ȡ�ļ���" + sql.toString());
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
			return returnErrMsg("ȡ�ṹͼ���������ݿ����ʧ�ܣ�");
		} finally {
			controller.getDBManager().close(conn);
		}

		if (fileBean != null)
			return returnSuccMsg("GraphFileBean", fileBean);
		else
			return returnErrMsg("ȡ�ṹͼ��������ȡ����Ϊ�գ�");
	}

	/**
	 * 2009-7-1 Add by ZHM
	 * 
	 * @���� ɾ��ָ���ṹͼ
	 * @param businessID:String:ҵ��ϵͳ���
	 * @param obj:GraphFileBean:�ļ�����
	 * @return
	 */
	public ResultBean delGraph(String businessID, GraphFileBean obj) {
		if (businessID == null || businessID.length() <= 0) {
			return returnErrMsg("ɾ���ṹͼ���ļ���û�н��յ�ҵ��ϵͳ��ţ�");
		}
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(null, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ȡͼ���ʶ��");
			return null;
		}
		// ��ȡͼ���ʶ
		String rela = getRelateString(businessID, conn);
		if (rela == null || rela.length() <= 0) {
			return returnErrMsg("ɾ���ṹͼ���ļ�����ȡͼ���ʶʧ�ܣ�");
		}
		String filename = obj.getFileName();
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM t_svg_rep_graph_").append(rela).append(
				" WHERE rg_graph_name='").append(filename).append("'");
		try {
			log.log(null, LoggerAdapter.DEBUG, "ɾ���ṹͼ���ļ���"
					+ sql.toString());
			PreparedStatement presm = conn.prepareStatement(sql.toString());
			presm.executeUpdate();
		} catch (SQLException e) {
			log.log(null, LoggerAdapter.ERROR, e);
			return returnErrMsg("����ṹͼ���ļ�����ͼ��������ʷ����������ݿ����ʧ�ܣ�");
		}
		return returnSuccMsg("String", "�ɹ�ɾ��:"+filename);
	}

	/**
	 * 2009-7-1 Add by ZHM
	 * 
	 * @���� ����ṹͼ�����ݿ���
	 * @param businessID:String:ҵ��ϵͳ���
	 * @param obj:GraphFileBean:�ļ�����
	 * @return
	 */
	public ResultBean saveGraph(String businessID, GraphFileBean fileBean) {
		if (businessID == null || businessID.length() <= 0) {
			return returnErrMsg("����ͼ���ļ���û�н��յ�ҵ��ϵͳ��ţ�");
		}
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(null, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ȡͼ���ʶ��");
			return null;
		}
		// ��ȡͼ���ʶ
		String rela = getRelateString(businessID, conn);
		if (rela == null || rela.length() <= 0) {
			return returnErrMsg("�洢ͼ���ļ�����ȡͼ���ʶʧ�ܣ�");
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

		// �ж����ݿ��е��ļ��Ƿ����
		String tablename = "t_svg_rep_graph_" + rela;
		String filename = fileBean.getFileName();
		String fileID = graphExist(tablename, filename, conn);
		ResultBean rb = null;
		try {
			conn.setAutoCommit(false);
			if (fileID != null && fileID.length() == 36) {
				// �޸��ļ�
				fileBean.setID(fileID);
				rb = ModifyGraph(conn, sqlIdiom, rela, fileBean);
			} else {
				// �����ļ�
				rb = AddNewGraph(conn, sqlIdiom, rela, fileBean);
			}
			conn.setAutoCommit(true);
			if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
				conn.commit();
				// rb = returnSuccMsg("String", "�ɹ���ͼ���ļ������ݿ��У�");
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			log.log(null, LoggerAdapter.ERROR, e);
			return returnErrMsg("����ͼ���ļ����������ݿ�ʧ�ܣ�");
		} finally {
			controller.getDBManager().close(conn);
		}

		return rb;
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
				log.log(null, LoggerAdapter.DEBUG, "��ȡͼ�����ݣ�" + sql.toString());
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
	 * �޸�ָ��ͼ���ļ���δ�ύ���ݣ�
	 * 
	 * @param conn:Connection:���ݿ�����
	 * @param sqlIdim:DBSqlIdiomAdapter:����ת����
	 * @param rela:String:���ݿ��ʶ
	 * @param fileBean:GraphFileBean:ͼ�ζ���
	 * @return
	 */
	private ResultBean ModifyGraph(Connection conn, DBSqlIdiomAdapter sqlIdiom,
			String rela, GraphFileBean fileBean) {
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
			log.log(null, LoggerAdapter.ERROR, "�޸�ָ��ͼ���ļ������ݿ����ʧ�ܣ�");
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
			log.log(null, LoggerAdapter.ERROR, "����ͼ���ļ������ݿ⣬���ݿ����ʧ�ܣ�");
			return returnErrMsg("����ͼ���ļ������ݿ⣬���ݿ����ʧ�ܣ�");
		}
		return rb;
	}

	/**
	 * 2009-7-1 Add by ZHM
	 * 
	 * @���� ָ���ļ��Ƿ��Ѵ��������ݿ���
	 * @param tablename:String:����
	 * @param filename:String:�ļ���
	 * @param conn:Connection:���ݿ�����
	 * @return
	 */
	private String graphExist(String tablename, String filename, Connection conn) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT rg_id FROM ").append(tablename).append(
				" WHERE rg_graph_name='").append(filename).append("'");
		String fileID = null;
		try {
			Statement st = conn.createStatement();
			log.log(null, LoggerAdapter.DEBUG, "��ȡָ���ļ��Ƿ��Ѵ��ڣ�" + sql.toString());
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
	 * ���ݴ����ҵ��ϵͳ��ź����ݿ����ӻ�ȡ��Ӧ��ͼ���ʶ
	 * 
	 * @param strBussID��ҵ��ϵͳ���
	 * @return��ͼ���ʶ���������򷵻�null
	 */
	private String getRelateString(String strBussID, Connection conn) {
		StringBuffer sql = new StringBuffer();
		String relate = null;
		// ���Ȼ�ȡ��ҵ��ϵͳͼ����
		sql
				.append(
						"SELECT rm_table_relate FROM t_svg_rep_manager WHERE rm_businesssys_id = '")
				.append(strBussID).append("'");

		// ��ȡ���ݿ�����
		if (conn == null) {
			log.log(null, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ȡͼ���ʶ��");
			return null;
		}
		try {
			Statement st = conn.createStatement();
			log.log(null, LoggerAdapter.DEBUG, "��ȡͼ���ʶ��" + sql.toString());
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
	 * ���ò���ʧ�ܷ��ض���
	 * 
	 * @param text:String:������Ϣ
	 * @return ����ֵ����
	 */
	private ResultBean returnErrMsg(String text) {
		ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR, text, null,
				null);
		return bean;
	}

	/**
	 * ���ò����ɹ����ض���
	 * 
	 * @param type:String:���ض�������
	 * @param obj:Object:���ض���ֵ
	 * @return ����ֵ����
	 */
	private ResultBean returnSuccMsg(String type, Object obj) {
		return new ResultBean(ResultBean.RETURN_SUCCESS, null, type, obj);
	}
}
