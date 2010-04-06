/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：Oracle数据库方言实现类
 *
 */
package com.nci.svg.server.database;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import oracle.sql.BLOB;

import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;

/**
 * @author yx.nci
 * 
 */
public class DBSqlIdiomOfOracleImpl extends DBSqlIdiomAdapter {

	public String getSupportDBType() {
		// TODO Auto-generated method stub
		return "Oracle";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.database.DBSqlIdiomIF#getBlob(java.sql.Connection,
	 *      java.lang.String, java.lang.String)
	 */
	public byte[] getBlob(Connection conn, String sql, String field) {
		byte[] buff = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			// 从数据集获取blob字段
			if (rs.next()) {
				BLOB blob = (BLOB) rs.getBlob(field);
				// 将blob对象转换成byte[]
				if (blob != null) {
					InputStream is = blob.getBinaryStream();
					buff = new byte[(int) blob.length()];
					is.read(buff);
				} else {
					System.out.println("blob字段有误");
				}
			}
			// 关闭数据集
			if (rs != null)
				rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return buff;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.database.DBSqlIdiomIF#getPartResultSet(java.lang.String,
	 *      int, int)
	 */
	public String getPartResultSet(String sql, int min, int max) {
		// 传入参数的合法性校验
		if (sql == null || sql.length() == 0) {
			return null;
		}

		if (sql.toUpperCase().indexOf("SELECT ") != 0)
			return null;

		if (min > max)
			return null;

		if (min < 0)
			min = 0;

		// 查询行数大于min，小于等于max的记录
		StringBuffer returnSql = new StringBuffer();
		returnSql.append("SELECT * FROM (SELECT ROWNUM NCI_R,svg_t.* FROM (")
				.append(sql).append(") svg_t WHERE ROWNUM <= ").append(max)
				.append(") WHERE NCI_R > ").append(min);

		return returnSql.toString();
	}

	public int emptyBlob(Connection conn, String sql) {
		int ret = -1;
		try {
			PreparedStatement presm = conn.prepareStatement(sql);
			ret = presm.executeUpdate();
			presm.clearParameters();
			if (presm != null)
				presm.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			ret = -1;
		}
		return ret;
	}

	public int setBlob(Connection conn, String sql, String field, byte[] content) {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			// 首先获取blob对象
			while (rs.next()) {
				BLOB blob = (BLOB) rs.getBlob(field);
				OutputStream out = blob.getBinaryOutputStream(); // 建立输出流
				out.write(content);
				out.close();
			}
			// 关闭数据集以提交
			if (rs != null)
				rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
		return 1;
	}

	public String getEmptyBlobString() {
		// TODO Auto-generated method stub
		return "empty_blob()";
	}

	public String getSysDate(Connection conn) {
		// TODO Auto-generated method stub
		String sql = "SELECT to_char(sysdate,'YYYYMMDDHH24MISS') curdate FROM dual";
		String sysdate = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				sysdate = rs.getString("curdate");
			}
			if (rs != null)
				rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			sysdate = null;
		}
		return sysdate;
	}

}
