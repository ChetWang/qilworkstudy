package com.nci.ums.dialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UMSDialect {

	public String getScanInvalid_scanReplySQL(ResultSet rs) throws SQLException;

	public String getInchannel_replyFlagWithMsgIDSQL();

	public String getInchannel_replyFlagWithoutMsgIDSQL();

	public String getOutMsgSelectSQL_V2();

	public String getOutMsgSelectSQL_V3();

//	public String getAppServerMsgProcessSQL_V2();

	public String getCacheData_NextMsgSQL_V2();

	/**
	 * 获取所有服务前5条收到的信息
	 * @param services
	 * @return
	 */
	public String getInMsgReceiveSQL_V3(List services);

	/**
	 * OUT渠道统计发送个数SQL
	 * @return
	 */
	public String getChartShowCountsInSQL_V3();

	/**
	 * IN渠道统计发送个数SQL
	 * @return
	 */
	public String getChartShowCountsOutSQL_V3();
	
	public String getAckMsgSQL_V3();
	
	
	/**
	 * @功能 将根据传入的连接、sql语句及字段名设置相应的blob字段数据
	 * @param conn Connection 数据库连接
	 * @param tableName String 表名
	 * @param keyField String 主键字段名
	 * @param id String 主键
	 * @param field String 对应blob字段的字段名
	 * @param content byte[] 待写入的内容
	 * @return 成功写入标志
	 *
	 * @Add by ZHM 2009-8-26
	 */
	public boolean setBlob(Connection conn, String tableName, String keyField, String id, String field, byte[] content);

	public String getOptimizeTableSQLFileName(String tableName);
	
	/**
	 * 重命名UMS_SEND_OK
	 * @param table
	 * @param suffix
	 * @return
	 */
	public String[] getRenameSEND_OKTableSQL(String table,String suffix);

	/**
	 * 重命名TRANSMIT表
	 * @param table
	 * @param suffix
	 * @return
	 */
	public String[] getRenameTrantmitTableSQL(String table,String suffix);
}
