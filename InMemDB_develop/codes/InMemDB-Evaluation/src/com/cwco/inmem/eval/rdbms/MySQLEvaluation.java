package com.cwco.inmem.eval.rdbms;

import java.sql.SQLException;
import java.sql.Statement;

public class MySQLEvaluation extends RDBMSEvaluation {

	public MySQLEvaluation(int concurrencySize) {
		super(concurrencySize);
		db_type = "MySQL 5";
	}

	@Override
	public String[] getDriverInfo() {
		String[] info = { "com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost:3306/test", "root", "password" };
		return info;
	}

	@Override
	protected void createTestTable() throws SQLException {
		Statement st = conns[0].createStatement();
		st.addBatch("DROP TABLE IF EXISTS " + TEST_TABLE_NAME);
		st.addBatch("CREATE TABLE " + TEST_TABLE_NAME + " (c_id int "
				+ generateCreateTableValueColumnSQL()
				+ " ,primary key (c_id)) type=heap");
		st.executeBatch();
		st.close();
	}
	
	protected void createTestTable_NoIndex() throws SQLException {
		Statement st = conns[0].createStatement();
		st.addBatch("DROP TABLE IF EXISTS " + TEST_TABLE_NAME);
		st.addBatch("CREATE TABLE " + TEST_TABLE_NAME + " (c_id int "
				+ generateCreateTableValueColumnSQL() + ") type=heap");
		st.executeBatch();
		st.close();
	}

}
