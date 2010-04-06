package com.nci.ums.dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UMSDialect {

	public String getScanInvalid_scanReplySQL(ResultSet rs) throws SQLException;

	public String getInchannel_replyFlagWithMsgIDSQL();

	public String getInchannel_replyFlagWithoutMsgIDSQL();

	public String getOutMsgSelectSQL_V2();

	public String getOutMsgSelectSQL_V3();

	public String getAppServerMsgProcessSQL_V2();

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
	
	public abstract String getAckMsgSQL_V3();

}
