package com.nci.svg.server.database;

import junit.framework.TestCase;

public class DBSqlIdiomOfOracleImplTest extends TestCase {

	public void testGetBlob() {
		
	}

	public void testGetPartResultSet() {
		DBSqlIdiomOfOracleImpl db = new DBSqlIdiomOfOracleImpl();
		String sql = "SELECT * FROM epms2.JFW_OWAY_DATA";
		sql = db.getPartResultSet(sql, 50, 200);
		System.out.println(sql);
	}

	public void testSetBlob() {
		
	}

}
