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
	 * ��ȡ���з���ǰ5���յ�����Ϣ
	 * @param services
	 * @return
	 */
	public String getInMsgReceiveSQL_V3(List services);

	/**
	 * OUT����ͳ�Ʒ��͸���SQL
	 * @return
	 */
	public String getChartShowCountsInSQL_V3();

	/**
	 * IN����ͳ�Ʒ��͸���SQL
	 * @return
	 */
	public String getChartShowCountsOutSQL_V3();
	
	public String getAckMsgSQL_V3();
	
	
	/**
	 * @���� �����ݴ�������ӡ�sql��估�ֶ���������Ӧ��blob�ֶ�����
	 * @param conn Connection ���ݿ�����
	 * @param tableName String ����
	 * @param keyField String �����ֶ���
	 * @param id String ����
	 * @param field String ��Ӧblob�ֶε��ֶ���
	 * @param content byte[] ��д�������
	 * @return �ɹ�д���־
	 *
	 * @Add by ZHM 2009-8-26
	 */
	public boolean setBlob(Connection conn, String tableName, String keyField, String id, String field, byte[] content);

	public String getOptimizeTableSQLFileName(String tableName);
	
	/**
	 * ������UMS_SEND_OK
	 * @param table
	 * @param suffix
	 * @return
	 */
	public String[] getRenameSEND_OKTableSQL(String table,String suffix);

	/**
	 * ������TRANSMIT��
	 * @param table
	 * @param suffix
	 * @return
	 */
	public String[] getRenameTrantmitTableSQL(String table,String suffix);
}
