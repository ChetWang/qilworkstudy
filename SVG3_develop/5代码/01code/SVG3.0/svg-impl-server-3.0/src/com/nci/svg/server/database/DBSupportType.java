/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ�Ŀǰ֧�ֵ����ݿ���������
 *
 */
package com.nci.svg.server.database;

import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;

public class DBSupportType {
    /**
     * ֧�ֵ����ݿ����ͱ�ʶ
     */
	private static String[] supportType = {"Oracle","MSSqlServer","MySql"};
    /**
     * ֧�ֵ����ݿ��������������ʶһһ��Ӧ
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
	 * ���ݴ�������ݿ����ͻ�ȡ�����ݿ����ͷ���ת����
	 * @param dbtype�����ݿ�����
	 * @return�������ݿ����ͷ���ת���࣬�������򷵻�null
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
