/**
 * 实现DBCP的数据库连接池
 * @author yx.nci
 *
 */
package com.nci.svg.server.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.logger.SVGDBAppender;
import com.nci.svg.sdk.module.ModuleStopedException;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.sdk.server.database.DBConnectionManagerAdapter;
import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;
import com.nci.svg.server.bean.ConnBean;
import com.nci.svg.server.util.Global;

public class DBConnectionManagerImpl extends DBConnectionManagerAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3616119243604641870L;
	/**
	 * 驱动类
	 */
	private Class driverClass = null;
	/**
	 * 连接池
	 */
	private ObjectPool connectionPool = null;
	/**
	 * 连接池集
	 */
	public Map map = null;
	/**
	 * 日志对象
	 */
	private static LoggerAdapter log;
	/**
	 * 数据库配置信息
	 */
	private HashMap poolInformation = new HashMap();
	/**
	 * 管理组件的对象
	 */
	private static DBConnectionManagerAdapter dbConnectionManager;

	/**
	 * 构造函数
	 * 
	 * @param mainModuleController
	 */
	private DBConnectionManagerImpl(HashMap parameters) {
		super(parameters);
	}

	/**
	 * 获取数据库连接池管理对象
	 * 
	 * @param mainModuleController
	 * @return
	 */
	public static DBConnectionManagerAdapter getInstance(
			ServerModuleControllerAdapter mainModuleController) {
		if (dbConnectionManager == null) {
			HashMap map = new HashMap();
			map.put(ServerModuleControllerAdapter.class.toString(),
					mainModuleController);
			dbConnectionManager = new DBConnectionManagerImpl(map);
			log = mainModuleController.getLogger();
		}
		return dbConnectionManager;
	}

	public static DBConnectionManagerAdapter getInstance(
			SVGDBAppender mainLoggerAdapter) {
		return dbConnectionManager;
	}

	/**
	 * 初始化数据源
	 */
	private void initDataSource(String driverClassName) {
		// 驱动数据源
		try {
			driverClass = Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			System.out.println("未找到数据库驱动类");
			e.printStackTrace();
		}
	}

	/**
	 * 释放连接池
	 */
	private void ShutdownPool() {
		try {
			PoolingDriver driver = (PoolingDriver) DriverManager
					.getDriver("jdbc:apache:commons:dbcp:");
			driver.closePool("dbpool");
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			e.printStackTrace();
		}
	}

	/**
	 * 连接池启动
	 * 
	 * @param poolname
	 *            String 连接池名称
	 * @param dbJdbc
	 *            String 数据库地址
	 * @param dbUser
	 *            String 登录用户名
	 * @param dbPwd
	 *            String 登录密码
	 * @param max
	 *            integer 最大连接数
	 * @param wait
	 *            long 等待时间
	 * @param driverClassName
	 *            String 驱动类名
	 * @param type
	 *            String 驱动类型
	 * @throws Exception
	 */
	private void StartPool(String poolname, String dbJdbc, String dbUser,
			String dbPwd, int max, long wait, String driverClassName,
			String type) {
		// 初始化数据源
		initDataSource(driverClassName);

		// 如果连接池不为空
		if (connectionPool != null) {
			ShutdownPool();
		}

		try {
			connectionPool = new GenericObjectPool(null, max, (byte) 1, wait);
			ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
					dbJdbc, dbUser, dbPwd);
			PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
					connectionFactory, connectionPool, null, null, false, true);
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			PoolingDriver driver = (PoolingDriver) DriverManager
					.getDriver("jdbc:apache:commons:dbcp:");
			driver.registerPool(poolname, connectionPool);

			// 将建立好的连接池加入到连接池集中
			map.put(poolname, connectionPool);
			log
					.log(this, LoggerAdapter.DEBUG, "成功创建名为：" + poolname
							+ "数据库连接池！");
		} catch (Exception e) {
			log.log(this, LoggerAdapter.ERROR, e);
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.database.DBConnectionManagerAdapter#getConnection(java.lang.String)
	 */
	public synchronized Connection getConnection(String poolName) {
		Connection conn = null;
		poolName = poolName.toUpperCase();
		if (map == null) {
			log.log(this, LoggerAdapter.DEBUG, "数据库连接池集为空！");
			map = new HashMap();
		}
		if (map.get(poolName) == null) {
			// initPool(poolName);// 初始化基本数据
			poolInformation = read();
			ConnBean baseConnBean = (ConnBean) poolInformation.get(poolName);
			String dbJdbc = baseConnBean.getJdbcurl(); // 数据库连接
			String dbUser = baseConnBean.getUsername(); // 登录用户名
			String dbPwd = baseConnBean.getPassword(); // 登录密码
			int max = baseConnBean.getMax(); // 最大连接数
			long wait = baseConnBean.getWait(); // 最长等待时间
			String driverClassName = baseConnBean.getDriver(); // 驱动类名
			String type = baseConnBean.getDbType(); // 驱动类型

			StartPool(poolName, dbJdbc, dbUser, dbPwd, max, wait,
					driverClassName, type);
		}
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:"
					+ poolName);
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			e.printStackTrace();
		}
		log.log(this, LoggerAdapter.DEBUG, getConnectionStatus(poolName));
		return conn;
	}

	/**
	 * 获取名为svg的连接池连接
	 * 
	 * @return
	 */
	public synchronized Connection getConnection() {
		String poolName = "svg";
		return getConnection(poolName);
	}

	public HashMap read() {
		String fileName = "DbConfig.xml";
		return read(fileName);
	}

	/**
	 * 从配置文件中读取数据库连接池配置
	 * 
	 * @param fileName
	 *            String 配置文件名
	 * @return 连接池配置信息
	 */
	private HashMap read(String fileName) {
		HashMap pools = new HashMap();
		Document document;
		log.log(this, LoggerAdapter.DEBUG, "开始读取数据库配置文件...");

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();

			// 下面代码在部署到servlet上时使用
			fileName = Global.appRoot + "/WEB-INF/classes/" + fileName;
			// 上面代码在部署到servlet上时使用

			document = builder.parse(new File(fileName));
			document.getDocumentElement().normalize();

			// 获取根节点
			Node root = document.getFirstChild();
			NodeList poolList = root.getChildNodes();
			for (int i = 0; i < poolList.getLength(); i++) {
				Node poolNode = poolList.item(i);
				if (poolNode.getNodeName().equals("pool")) { // 获取pool元素节点
					ConnBean cb = new ConnBean();
					NodeList paramList = poolNode.getChildNodes();
					boolean flag = false;
					for (int k = 0, pSize = paramList.getLength(); k < pSize; k++) {
						Node paramNode = paramList.item(k);

						// 获取连接池名称
						if (paramNode.getNodeName().equals("name"))
							cb.setName(paramNode.getFirstChild().getNodeValue()
									.toUpperCase());
						// 获取登录用户名
						if (paramNode.getNodeName().equals("username"))
							cb.setUsername(paramNode.getFirstChild()
									.getNodeValue());
						// 获取登录密码
						if (paramNode.getNodeName().equals("password"))
							cb.setPassword(paramNode.getFirstChild()
									.getNodeValue());
						// 获取数据库地址
						if (paramNode.getNodeName().equals("jdbcurl"))
							cb.setJdbcurl(paramNode.getFirstChild()
									.getNodeValue());
						// 获取数据库驱动类名
						if (paramNode.getNodeName().equals("driver"))
							cb.setDriver(paramNode.getFirstChild()
									.getNodeValue());
						// 获取连接池最大连接数
						if (paramNode.getNodeName().equals("max")) {
							try {
								cb.setMax(Integer.parseInt(paramNode
										.getFirstChild().getNodeValue()));
							} catch (NumberFormatException e) {
								cb.setMax(3);
							}
						}
						// 获取连接池最长等待时间
						if (paramNode.getNodeName().equals("wait")) {
							try {
								cb.setWait(Long.parseLong(paramNode
										.getFirstChild().getNodeValue()));
							} catch (NumberFormatException e) {
								cb.setWait(1000L);
							}
						}

						// 获取数据库类型
						if (paramNode.getNodeName().equals("type")) {
							// 比对数据库类型，不在支持的队列中则不加入缓存池
							String type = paramNode.getFirstChild()
									.getNodeValue().toUpperCase();
							String[] supportType = DBSupportType
									.getSupportType();
							for (int j = 0; j < supportType.length; j++) {
								if (supportType[j].toUpperCase().equals(type)) {
									cb.setDbType(paramNode.getFirstChild()
											.getNodeValue().toUpperCase());
									flag = true;
									break;
								}
							}

						}
					}
					if (flag)
						pools.put(cb.getName(), cb);
				}
			}
		} catch (Exception exp) {
			log.log(this, LoggerAdapter.ERROR, exp);
			return null;
		}

		log.log(this, LoggerAdapter.DEBUG, "成功读取数据库配置文件信息！");
		return pools;
	}

	/**
	 * 关闭连接
	 * 
	 * @param c
	 *            Connection 数据库连接
	 */
	public synchronized void close(Connection c) {
		try {
			if (c != null)
				c.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#getModuleID()
	 */
	public String getModuleID() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#handleOper(int,
	 *      java.lang.Object)
	 */
	public Object handleOper(int index, Object obj)
			throws ModuleStopedException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.database.DBConnectionManagerAdapter#getConnectionStatus(java.lang.String)
	 */
	public synchronized String getConnectionStatus(String poolName) {
		StringBuffer stat = new StringBuffer();
		try {
			
			PoolingDriver driver = (PoolingDriver) DriverManager
					.getDriver("jdbc:apache:commons:dbcp:");
			
//			System.out.println("begin getConnectionStatus");
			ObjectPool connectionPool = driver.getConnectionPool(poolName
					.toUpperCase());
//			System.out.println("end getConnectionStatus");

			stat.append(poolName + ":活动连接数: ");
			stat.append(connectionPool.getNumActive());
			stat.append(", ");
			stat.append("空闲连接数: ");
			stat.append(connectionPool.getNumIdle());
		} catch (Exception e) {
			log.log(this, LoggerAdapter.ERROR, e);
			e.printStackTrace();
		}
		return stat.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.database.DBConnectionManagerAdapter#getDBType(java.lang.String)
	 */
	public String getDBType(String poolName) {
		if (poolInformation == null)
			poolInformation = read();
		ConnBean baseConnBean = (ConnBean) poolInformation.get(poolName);
		if (baseConnBean == null)
			return null;
		else
			return baseConnBean.getDbType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.database.DBConnectionManagerAdapter#getDBSqlIdiom(java.lang.String)
	 */
	public DBSqlIdiomAdapter getDBSqlIdiom(String dbType) {
		return DBSupportType.getSqlIdiom(dbType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#init(java.lang.Object)
	 *      读取数据库配置
	 */
	public int init(Object obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#reInit(java.lang.Object)
	 */
	public int reInit(Object obj) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#start(java.lang.Object)
	 *      启动数据库连接,并进行数据库校验
	 */
	public int start(Object obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#stop(java.lang.Object)
	 */
	public int stop(Object obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isStoped() {
		// TODO Auto-generated method stub
		return false;
	}

}
