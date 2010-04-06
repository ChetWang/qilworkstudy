
package com.nci.svg.sdk.server.database;

import java.sql.Connection;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-11-24
 * @功能：数据库方言解析抽象基类
 *
 */
public abstract class DBSqlIdiomAdapter implements DBSqlIdiomIF {

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.server.database.DBSqlIdiomIF#getBlob(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public byte[] getBlob(Connection conn, String sql, String field) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.server.database.DBSqlIdiomIF#getPartResultSet(java.lang.String, int, int)
	 */
	public String getPartResultSet(String sql, int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.server.database.DBSqlIdiomIF#getSupportDBType()
	 */
	public String getSupportDBType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.server.database.DBSqlIdiomIF#setBlob(java.sql.Connection, java.lang.String, java.lang.String, byte[])
	 */
	public int setBlob(Connection conn, String sql, String field, byte[] content) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.server.database.DBSqlIdiomIF#getEmptyBlobString()
	 */
	public String getEmptyBlobString() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.server.database.DBSqlIdiomIF#getSysDate(java.sql.Connection)
	 */
	public String getSysDate(Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.server.database.DBSqlIdiomIF#emptyBlob(java.sql.Connection, java.lang.String)
	 */
	public int emptyBlob(Connection conn, String sql){
		return 0;
	}

}
