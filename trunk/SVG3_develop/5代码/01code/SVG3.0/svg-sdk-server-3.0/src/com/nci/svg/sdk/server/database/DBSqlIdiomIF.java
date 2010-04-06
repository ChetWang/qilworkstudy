
package com.nci.svg.sdk.server.database;

import java.sql.Connection;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：数据库方言接口
 *
 */
public interface DBSqlIdiomIF {

	/**
	 * 根据传入的连接、sql语句及字段名获取相应的blob字段数据
	 * @param conn 数据库连接
	 * @param sql sql语句
	 * @param field 对应blob字段的字段名
	 * @return： blob字段内容
	 */
	public byte[] getBlob(Connection conn,String sql,String field);
	
	/**
	 * 根据传入的连接、sql语句及字段名设置相应的blob字段数据
	 * @param conn 数据库连接
	 * @param sql sql语句
	 * @param field 对应blob字段的字段名
	 * @param content：待写入的内容
	 * @return：写入结果
	 */
	public int setBlob(Connection conn,String sql,String field,byte[] content);
	
	/**
	 * 根据传入的最小值和最大值，转换sql
	 * @param sql:sql语句
	 * @param min:希望获得的最小行数
	 * @param max：希望获得的最大行数
	 * @return:转换后的语句
	 */
	public String getPartResultSet(String sql,int min,int max);
	
	/**
	 * 获取支持的数据库类型
	 * @return：支持的数据库类型
	 */
	public String getSupportDBType();
	
	/**
	 * 获取服务器当前时间
	 * @param conn：数据库连接
	 * @return：当前时间字符串，
	 */
	public String getSysDate(Connection conn);
	
	/**
	 * 获取当前数据库空blob字段创建字符串
	 * @return：创建字符串
	 */
	public String getEmptyBlobString();
	
	/**
	 * 根据输入的sql语句将数据库中的blob字段设置为空
	 * @param conn:数据库连接池
	 * @param sql:blob字段置空sql语句
	 * @return 是否操作成功 >0表示成功,<0表示失败
	 */
	public int emptyBlob(Connection conn, String sql);
}
