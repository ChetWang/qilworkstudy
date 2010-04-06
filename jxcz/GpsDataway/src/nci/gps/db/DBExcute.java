/**
 * @author ZHOUHM
 * @功能:数据库语句执行
 * @时间:2008-06-03
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
	 * Field conn. 数据库连接
	 */
	private Connection conn = null;

	/**
	 * Field transactionFlag. 事务操作标志
	 */
	private boolean transactionFlag = false;

	/**
	 * Field statements. sql语句声明列表
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
			MsgLogger.logExceptionTrace("数据库操作类，获取数据库连接失败", e);
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
	 * 获得数据库连接
	 * 
	 * @return 返回数据库连接
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
	 * Method executeQuery. 执行查询
	 * @param sqlSelect String	查询sql语句
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
	 * Method executeUpdate. 执行更新
	 * @param sql	更新sql语句
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
	 * Method getConnection. 获取数据库连接
	 * @return Connection
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * Method close. 关闭连接
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
	 * 开始事务.
	 * <p>仅在事务还未开始时将提交状态设置为手动提交,<br>
	 * 同时将事务操作标志设置为true.
	 * @throws SQLException 
	 * 
	 * @throws DataException 当有数据异常发生时
	 */
	public void beginTransaction() throws SQLException {
		if (transactionFlag == false) {
			conn.setAutoCommit(false);
			transactionFlag = true;
		}
	}

	/**
	 * Method commit. 事务提交处理
	 * @throws SQLException 
	 * @throws DataException
	 */
	public void commit() throws SQLException {
		conn.commit();
		transactionFlag = false;
		conn.setAutoCommit(true);
	}

	/**
	 * Method rollback. 事务回滚处理
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
