/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：MySql数据库方言实现类
 *
 */
package com.nci.svg.server.database;

import java.sql.Connection;

import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;

/**
 * @author yx.nci
 *
 */
public class DBSqlIdiomOfMySqlImpl extends DBSqlIdiomAdapter {

	public String getSupportDBType() {
		// TODO Auto-generated method stub
		return "MySql";
	}

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
		//传入参数的合法性校验
		if(sql == null || sql.length() == 0 )
		{
			return null;
		}
		
		if(sql.toUpperCase().indexOf("SELECT ") != 0)
			return null;
		
		if(min > max)
			return null;
		
		if(min < 0)
			min = 0;
		
		//查询行数大于min，小于等于max的记录
    	StringBuffer  returnSql = new StringBuffer();
		returnSql.append(sql).append(" LIMIT ").append(min).append(",").append(max);
		
		return returnSql.toString();
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.server.database.DBSqlIdiomIF#setBlob(java.sql.Connection, java.lang.String, java.lang.String, byte[])
	 */
	public int setBlob(Connection conn, String sql, String field, byte[] content) {
		// TODO Auto-generated method stub
		return 0;
	}

}
