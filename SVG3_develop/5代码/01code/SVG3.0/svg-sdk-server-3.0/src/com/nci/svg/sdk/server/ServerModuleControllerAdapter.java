package com.nci.svg.sdk.server;

import java.io.File;
import java.util.HashMap;

import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.module.GeneralModuleIF;
import com.nci.svg.sdk.module.ModuleControllerAdapter;
import com.nci.svg.sdk.server.cache.CacheManagerAdapter;
import com.nci.svg.sdk.server.database.DBConnectionManagerAdapter;
import com.nci.svg.sdk.server.graphstorage.GraphStorageManagerAdapter;
import com.nci.svg.sdk.server.operationinterface.OperInterfaceManagerAdapter;
import com.nci.svg.sdk.server.service.ServiceModuleManagerAdapter;
import com.nci.svg.sdk.server.service.ServiceShuntAdapter;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ������������������������
 * 
 */
/**
 * <p>
 * ���⣺ServerModuleControllerAdapter.java
 * </p>
 * <p>
 * ������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-6-5
 * @version 1.0
 */
public abstract class ServerModuleControllerAdapter extends
		ModuleControllerAdapter {
	/**
	 * ����������־���
	 */
	protected LoggerAdapter logger = null;
	/**
	 * ���ݿ����ӳ����
	 */
	protected DBConnectionManagerAdapter dbma = null;

	public ServerModuleControllerAdapter() {
		// TODO initialize basic modules

	}

	public ServerModuleControllerAdapter(HashMap parameters) {
		super(parameters);
	}

	/**
	 * ��ʼ�����ݿ����ӳض���
	 */
	protected abstract DBConnectionManagerAdapter initDBConnectionManagerAdapter();

	/**
	 * �����������ȡ���
	 * 
	 * @param moduleName:�����
	 * @return:�������,�粻�����򷵻�null
	 */
	public abstract GeneralModuleIF getModule(String moduleName);

	/**
	 * ��������������������,��ȡ��������
	 * 
	 * @param type:������
	 * @param obj:��������
	 * @return:��ѯ���
	 */
	public abstract Object getCacheData(int type, Object obj);

	/**
	 * ��ʼ����־�������
	 * 
	 * @return
	 */
	protected abstract LoggerAdapter initLoggerAdapter();

	public String getModuleType() {
		// TODO Auto-generated method stub
		return ModuleDefines.SERVER_MODULE_CONTROLLER;
	}

	/**
	 * ��ȡ��־���
	 * 
	 * @return
	 */
	public LoggerAdapter getLogger() {
		return logger;
	}

	public DBConnectionManagerAdapter getDBManager() {
		return dbma;
	}

	public abstract String getCachePath();

	/**
	 * ��ȡ����������
	 * 
	 * @return������������ʵ�ֶ���
	 */
	public abstract CacheManagerAdapter getCacheManager();

	/**
	 * ��ȡҵ������������
	 * 
	 * @return��ҵ������������ʵ�ֶ���
	 */
	public abstract ServiceModuleManagerAdapter getServiceModuleManager();

	/**
	 * ҵ�����·�����
	 * 
	 * @return��ҵ�����·�����ʵ�ֶ���
	 */
	public abstract ServiceShuntAdapter getServiceShunt();

	/**
	 * ͼ��������
	 * 
	 * @return��ͼ��������ʵ�ֶ���
	 */
	public abstract GraphStorageManagerAdapter getGraphStorageManager();

	/**
	 * ҵ��ӿڹ������
	 * 
	 * @return��ҵ��ӿڹ������ʵ�ֶ���
	 */
	public abstract OperInterfaceManagerAdapter getOperInterfaceManager();

	public abstract void addClassPath(File file);

	/**
	 * 2009-6-5 Add by ZHM
	 * 
	 * @���� ��ȡ��������������
	 * @param section�����ü�����
	 * @param profile�����ò�����
	 * @return
	 */
	public abstract String getServiceSets(String section, String profile);
}
