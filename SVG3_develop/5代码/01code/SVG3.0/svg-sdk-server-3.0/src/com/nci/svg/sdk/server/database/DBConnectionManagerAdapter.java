
package com.nci.svg.sdk.server.database;

import java.sql.Connection;
import java.util.HashMap;

import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;
/**
 * 数据库管理组件类，提供数据库连接池初始化、数据库连接获取功能
 * @author zhm
 * @since 3.0
 */
public abstract class DBConnectionManagerAdapter extends ServerModuleAdapter{
	
	/**
	 * 构造函数
	 * @param mainModuleController
	 */
	public DBConnectionManagerAdapter (HashMap parameters) {
		super(parameters);
	}
	
	/**
	 * 获取数据库连接池中的连接
	 * @param poolName String 数据库连接池名称
	 * @return Connection 数据库连接
	 */
	public abstract Connection getConnection(String poolName);
	
	/**
	 * 获取数据库连接池状态
	 * @param poolName String 数据库连接池名称
	 * @return String 连接池状态
	 */
	public abstract String getConnectionStatus(String poolName);

	public String getModuleType() {
		// TODO Auto-generated method stub
		return ModuleDefines.DATABASE_MANAGER;
	}
	
	/**
	 * 根据传入的连接池名称获取其数据库类型
	 * @param poolName；连接池名称
	 * @return：数据库类型
	 */
	public abstract String getDBType(String poolName);
	
	/**
	 * 根据传入的数据库类型获取该数据库方言转换类
	 * @param dbType:数据库类型
	 * @return：转换类对象
	 */
	public abstract DBSqlIdiomAdapter getDBSqlIdiom(String dbType);

	/**
	 * 关闭数据库连接
	 * @param conn Connection 指定数据库连接
	 */
	public void close(Connection conn) {
	}

}
