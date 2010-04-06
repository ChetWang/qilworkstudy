
package com.nci.svg.sdk.server.database;

import java.sql.Connection;
import java.util.HashMap;

import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;
/**
 * ���ݿ��������࣬�ṩ���ݿ����ӳس�ʼ�������ݿ����ӻ�ȡ����
 * @author zhm
 * @since 3.0
 */
public abstract class DBConnectionManagerAdapter extends ServerModuleAdapter{
	
	/**
	 * ���캯��
	 * @param mainModuleController
	 */
	public DBConnectionManagerAdapter (HashMap parameters) {
		super(parameters);
	}
	
	/**
	 * ��ȡ���ݿ����ӳ��е�����
	 * @param poolName String ���ݿ����ӳ�����
	 * @return Connection ���ݿ�����
	 */
	public abstract Connection getConnection(String poolName);
	
	/**
	 * ��ȡ���ݿ����ӳ�״̬
	 * @param poolName String ���ݿ����ӳ�����
	 * @return String ���ӳ�״̬
	 */
	public abstract String getConnectionStatus(String poolName);

	public String getModuleType() {
		// TODO Auto-generated method stub
		return ModuleDefines.DATABASE_MANAGER;
	}
	
	/**
	 * ���ݴ�������ӳ����ƻ�ȡ�����ݿ�����
	 * @param poolName�����ӳ�����
	 * @return�����ݿ�����
	 */
	public abstract String getDBType(String poolName);
	
	/**
	 * ���ݴ�������ݿ����ͻ�ȡ�����ݿⷽ��ת����
	 * @param dbType:���ݿ�����
	 * @return��ת�������
	 */
	public abstract DBSqlIdiomAdapter getDBSqlIdiom(String dbType);

	/**
	 * �ر����ݿ�����
	 * @param conn Connection ָ�����ݿ�����
	 */
	public void close(Connection conn) {
	}

}
