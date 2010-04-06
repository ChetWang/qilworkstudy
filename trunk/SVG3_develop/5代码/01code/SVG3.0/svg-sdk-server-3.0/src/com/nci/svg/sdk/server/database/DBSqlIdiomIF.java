
package com.nci.svg.sdk.server.database;

import java.sql.Connection;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ����ݿⷽ�Խӿ�
 *
 */
public interface DBSqlIdiomIF {

	/**
	 * ���ݴ�������ӡ�sql��估�ֶ�����ȡ��Ӧ��blob�ֶ�����
	 * @param conn ���ݿ�����
	 * @param sql sql���
	 * @param field ��Ӧblob�ֶε��ֶ���
	 * @return�� blob�ֶ�����
	 */
	public byte[] getBlob(Connection conn,String sql,String field);
	
	/**
	 * ���ݴ�������ӡ�sql��估�ֶ���������Ӧ��blob�ֶ�����
	 * @param conn ���ݿ�����
	 * @param sql sql���
	 * @param field ��Ӧblob�ֶε��ֶ���
	 * @param content����д�������
	 * @return��д����
	 */
	public int setBlob(Connection conn,String sql,String field,byte[] content);
	
	/**
	 * ���ݴ������Сֵ�����ֵ��ת��sql
	 * @param sql:sql���
	 * @param min:ϣ����õ���С����
	 * @param max��ϣ����õ��������
	 * @return:ת��������
	 */
	public String getPartResultSet(String sql,int min,int max);
	
	/**
	 * ��ȡ֧�ֵ����ݿ�����
	 * @return��֧�ֵ����ݿ�����
	 */
	public String getSupportDBType();
	
	/**
	 * ��ȡ��������ǰʱ��
	 * @param conn�����ݿ�����
	 * @return����ǰʱ���ַ�����
	 */
	public String getSysDate(Connection conn);
	
	/**
	 * ��ȡ��ǰ���ݿ��blob�ֶδ����ַ���
	 * @return�������ַ���
	 */
	public String getEmptyBlobString();
	
	/**
	 * ���������sql��佫���ݿ��е�blob�ֶ�����Ϊ��
	 * @param conn:���ݿ����ӳ�
	 * @param sql:blob�ֶ��ÿ�sql���
	 * @return �Ƿ�����ɹ� >0��ʾ�ɹ�,<0��ʾʧ��
	 */
	public int emptyBlob(Connection conn, String sql);
}
