/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：目前支持的数据库类型描述
 *
 */
package com.nci.svg.server.database;

import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;

public class DBSupportType {
    /**
     * 支持的数据库类型标识
     */
	private static String[] supportType = {"Oracle","MSSqlServer","MySql"};
    /**
     * 支持的数据库类型描述，与标识一一对应
     */
    private static String[] supportTypeLabel = {"Oracle","MSSqlServer","MySql"};
    
    private static DBSqlIdiomAdapter[] sqlIdiom = {new DBSqlIdiomOfOracleImpl(),
    	new DBSqlIdiomOfSqlServerImpl(),new DBSqlIdiomOfMySqlImpl()};
	public static String[] getSupportType() {
		return supportType;
	}
	public static String[] getSupportTypeLabel() {
		return supportTypeLabel;
	}
	
	/**
	 * 根据传入的数据库类型获取该数据库类型方言转换类
	 * @param dbtype：数据库类型
	 * @return：该数据库类型方言转换类，不存在则返回null
	 */
	public static DBSqlIdiomAdapter getSqlIdiom(String dbtype) {
		if(dbtype == null)
			return null;
		for(int i = 0;i < supportType.length;i++)
		{
			if(supportType[i].toUpperCase().equals(dbtype.toUpperCase()))
				return sqlIdiom[i];
		}
		return null;
	}
}
