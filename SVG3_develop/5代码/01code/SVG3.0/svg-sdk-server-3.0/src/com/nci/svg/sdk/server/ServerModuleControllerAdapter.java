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
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：服务器端主管理组件抽象类
 * 
 */
/**
 * <p>
 * 标题：ServerModuleControllerAdapter.java
 * </p>
 * <p>
 * 描述：
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-6-5
 * @version 1.0
 */
public abstract class ServerModuleControllerAdapter extends
		ModuleControllerAdapter {
	/**
	 * 服务器端日志组件
	 */
	protected LoggerAdapter logger = null;
	/**
	 * 数据库连接池组件
	 */
	protected DBConnectionManagerAdapter dbma = null;

	public ServerModuleControllerAdapter() {
		// TODO initialize basic modules

	}

	public ServerModuleControllerAdapter(HashMap parameters) {
		super(parameters);
	}

	/**
	 * 初始化数据库连接池对象
	 */
	protected abstract DBConnectionManagerAdapter initDBConnectionManagerAdapter();

	/**
	 * 根据组件名获取组件
	 * 
	 * @param moduleName:组件名
	 * @return:组件对象,如不存在则返回null
	 */
	public abstract GeneralModuleIF getModule(String moduleName);

	/**
	 * 根据类型名和请求数据,获取缓存数据
	 * 
	 * @param type:类型名
	 * @param obj:请求数据
	 * @return:查询结果
	 */
	public abstract Object getCacheData(int type, Object obj);

	/**
	 * 初始化日志组件对象
	 * 
	 * @return
	 */
	protected abstract LoggerAdapter initLoggerAdapter();

	public String getModuleType() {
		// TODO Auto-generated method stub
		return ModuleDefines.SERVER_MODULE_CONTROLLER;
	}

	/**
	 * 获取日志组件
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
	 * 获取缓存管理组件
	 * 
	 * @return：缓存管理组件实现对象
	 */
	public abstract CacheManagerAdapter getCacheManager();

	/**
	 * 获取业务组件管理组件
	 * 
	 * @return：业务组件管理组件实现对象
	 */
	public abstract ServiceModuleManagerAdapter getServiceModuleManager();

	/**
	 * 业务服务路由组件
	 * 
	 * @return：业务服务路由组件实现对象
	 */
	public abstract ServiceShuntAdapter getServiceShunt();

	/**
	 * 图库管理组件
	 * 
	 * @return：图库管理组件实现对象
	 */
	public abstract GraphStorageManagerAdapter getGraphStorageManager();

	/**
	 * 业务接口管理组件
	 * 
	 * @return：业务接口管理组件实现对象
	 */
	public abstract OperInterfaceManagerAdapter getOperInterfaceManager();

	public abstract void addClassPath(File file);

	/**
	 * 2009-6-5 Add by ZHM
	 * 
	 * @功能 获取服务器参数设置
	 * @param section：配置集合名
	 * @param profile：配置参数名
	 * @return
	 */
	public abstract String getServiceSets(String section, String profile);
}
