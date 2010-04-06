/**
 * @author ZHOUHM
 * @����:���ݿ����ִ��
 * @ʱ��:2008-06-03
 */
package nci.gps.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import nci.gps.log.MsgLogger;


public class DBExcute {
	/**
	 * Field conn. ���ݿ�����
	 */
	private Connection conn = null;

	/**
	 * Field transactionFlag. ���������־
	 */
	private boolean transactionFlag = false;

	/**
	 * Field statements. sql��������б�
	 */
	private List statements = new ArrayList();

	/**
	 * Field statement.
	 */
	private Statement statement;

	public DBExcute(){
		try {
			init();
		} catch (SQLException e) {
			MsgLogger.logExceptionTrace("���ݿ�����࣬��ȡ���ݿ�����ʧ��", e);
			e.printStackTrace();
		}
	}

	private void init() throws SQLException {
		conn = DBConnHelper.getConn();
		conn.setAutoCommit(true);
		statement = conn.createStatement();
	}

	public Statement createStatement() throws SQLException {
		Statement result = null;
		result = conn.createStatement();
		return result;
	}
	

	/**
	 * Method getStatement.
	 * ������ݿ�����
	 * 
	 * @return �������ݿ�����
	 */
	public Statement getStatement() {
		Statement result = null;
		try {
			if (statement != null)
				return statement;
			result = conn.createStatement();
			statements.add(statement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Method executeQuery. ִ�в�ѯ
	 * @param sqlSelect String	��ѯsql���
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet ExecuteQuery(String sqlSelect) throws SQLException {
		Statement stmt = getStatement();
		if (stmt != null) {
			return stmt.executeQuery(sqlSelect);
		} else {
			return null;
		}
//		PreparedStatement presm = conn.prepareStatement(sqlSelect);
//		if(presm != null){
//			ResultSet rs = presm.executeQuery();
//			presm.close();
//			return rs;
//		}else{
//			return null;
//		}
	}
	
	/**
	 * Method executeUpdate. ִ�и���
	 * @param sql	����sql���
	 * @return
	 * @throws SQLException
	 */
	public int ExecuteUpdate(String sql) throws SQLException{
		Statement stmt = getStatement();
		if(stmt != null){
			return stmt.executeUpdate(sql);
		} else {
			return -1;
		}
	}

	/**
	 * Method getConnection. ��ȡ���ݿ�����
	 * @return Connection
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * Method close. �ر�����
	 */
	public void close() {

		try {
			if (statement != null)
				statement.close();
		} catch (Exception e) {
		}

		int length = statements.size();
		for (int i = 0; i < length; i++) {
			try {
				Statement stmt = (Statement) statements.get(i);
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}

		if (conn != null)
			try {
				DBConnHelper.realse(conn);
//				conn.close();
			} catch (Exception e) {
			}
	}

	/**
	 * Method beginTransaction.
	 * ��ʼ����.
	 * <p>��������δ��ʼʱ���ύ״̬����Ϊ�ֶ��ύ,<br>
	 * ͬʱ�����������־����Ϊtrue.
	 * @throws SQLException 
	 * 
	 * @throws DataException ���������쳣����ʱ
	 */
	public void beginTransaction() throws SQLException {
		if (transactionFlag == false) {
			conn.setAutoCommit(false);
			transactionFlag = true;
		}
	}

	/**
	 * Method commit. �����ύ����
	 * @throws SQLException 
	 * @throws DataException
	 */
	public void commit() throws SQLException {
		conn.commit();
		transactionFlag = false;
		conn.setAutoCommit(true);
	}

	/**
	 * Method rollback. ����ع�����
	 */
	public void rollback() {
		try {
			conn.rollback();
			transactionFlag = false;
			conn.setAutoCommit(true);
		} catch (Exception e) {
		}
	}
}
