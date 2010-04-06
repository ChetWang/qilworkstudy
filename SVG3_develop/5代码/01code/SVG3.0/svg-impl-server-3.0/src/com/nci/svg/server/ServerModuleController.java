package com.nci.svg.server;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import com.nci.svg.sdk.NCIClassLoader;
import com.nci.svg.sdk.bean.ModuleInfoBean;
import com.nci.svg.sdk.logger.LogPolicyBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.module.GeneralModuleIF;
import com.nci.svg.sdk.module.ModuleStopedException;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.sdk.server.cache.CacheManagerAdapter;
import com.nci.svg.sdk.server.database.DBConnectionManagerAdapter;
import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;
import com.nci.svg.sdk.server.graphstorage.GraphStorageManagerAdapter;
import com.nci.svg.sdk.server.operationinterface.OperInterfaceManagerAdapter;
import com.nci.svg.sdk.server.service.ServiceModuleManagerAdapter;
import com.nci.svg.sdk.server.service.ServiceShuntAdapter;
import com.nci.svg.sdk.server.util.ServerUtilities;
import com.nci.svg.server.database.DBConnectionManagerImpl;
import com.nci.svg.server.log.ServerLoggerImpl;
import com.nci.svg.server.util.Global;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：Servlet版主管理组件类
 * 
 */
public class ServerModuleController extends ServerModuleControllerAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 311058355961814199L;
	public DBConnectionManagerImpl dbConn = null;
	public HashMap mapModuleInfo = null;
	public HashMap mapModule = null;

	private CacheManagerAdapter cacheManager = null;
	private ServiceModuleManagerAdapter serviceModuleManager = null;
	private ServiceShuntAdapter serviceShunt = null;
	private GraphStorageManagerAdapter graphStorageManager = null;
	private OperInterfaceManagerAdapter operInterfaceManager = null;

	/**
	 * 操作日志对象
	 */
	private static LoggerAdapter log;

	public ServerModuleController(HashMap parameters) {
		super(parameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.ServerModuleControllerAdapter#getCacheData(int,
	 *      java.lang.Object)
	 */
	public Object getCacheData(int type, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.ServerModuleControllerAdapter#getModule(java.lang.String)
	 */
	public GeneralModuleIF getModule(String moduleName) {
		// TODO Auto-generated method stub
		if (mapModule != null)
			return (GeneralModuleIF) mapModule.get(moduleName);

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#getModuleID()
	 */
	public String getModuleID() {
		// TODO Auto-generated method stub
		return this.getClass().getName();
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
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#init(java.lang.Object)
	 *      初始化,初始化主要完成数据库连接和组件清单下载获取
	 */
	public int init() {
		// 初始化日志组件
		logger = initLoggerAdapter();
		// 获取日志对象
		log = getLogger();
		// 初始化数据库组件
		dbma = initDBConnectionManagerAdapter();

		int ret = 0;
		// 首先获取系统所需架包组件
		HashMap map = getSysJarList();
		String pathName = getCachePath();
		Iterator iterator = null;
		iterator = map.values().iterator();
		while (iterator.hasNext()) {
			ModuleInfoBean bean = (ModuleInfoBean) iterator.next();
			ret = downloadModuleJar(bean, pathName);
		}

		// 获取系统所需平台组件
		ret = getModuleList();
		if (ret != 0) {
			log.log(this, LoggerAdapter.ERROR, "获取组件清单失败");
			return OPER_ERROR;
		}
		// 根据组件清单逐一下载组件包至本地
		if (mapModuleInfo.size() == 0) {
			log.log(this, LoggerAdapter.ERROR, "组件清单为空，启动失败");
			return OPER_ERROR;
		}

		// 获取本地存储路径信息

		iterator = mapModuleInfo.values().iterator();
		while (iterator.hasNext()) {
			ModuleInfoBean bean = (ModuleInfoBean) iterator.next();
			ret = downloadModuleJar(bean, pathName);
		}

		return OPER_SUCCESS;
	}

	/**
	 * 获取本地存储路径
	 * 
	 * @return：本地存储路径
	 */
	public String getCachePath() {
		return readServerCfg("param-local", "CachePath");
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.server.ServerModuleControllerAdapter#getServiceSets(java.lang.String, java.lang.String)
	 */
	public String getServiceSets(String section, String profile){
		return readServerCfg(section, profile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#start(java.lang.Object)
	 *      主管理组件启动,负责各组件实例化及初始化 实例化及初始化过程均需要在控制台打印
	 */
	public int start() {
		System.out.println("ListExtClassPath");
		NCIClassLoader.listExtClassPath();
		System.out.println("ListSystemClassPath");
		NCIClassLoader.listSystemClassPath();
		String className = null;
		HashMap mapParams = new HashMap();
		mapModule = new HashMap();
		mapParams.put(ServerModuleControllerAdapter.class.toString(), this);
		// 根据组件清单逐一构建组件
		// mapModuleInfo
		// 将组件日志策略传递给日志组件
		HashMap mapPolicy = new HashMap();
		Iterator iterator = mapModuleInfo.values().iterator();
		while (iterator.hasNext()) {
			ModuleInfoBean bean = (ModuleInfoBean) iterator.next();
			LogPolicyBean logBean = new LogPolicyBean();
			logBean.setModuleLocation(bean.getLocation());
			logBean.setModuleKind(bean.getKind());
			logBean.setModuleType(bean.getType());
			logBean.setLogType(bean.getLogType());
			logBean.setLogLevel(bean.getLogLevel());
			mapPolicy.put(bean.getType(), logBean);
		}
		logger.putParameter("LOGPOLICY", mapPolicy);
		logger.start();
		// 缓存管理组件
		log.log(this, LoggerAdapter.INFO, "组件启动");
		className = getClassName(CacheManagerAdapter.class.toString()
				.substring(6));
//		log.log(this, LoggerAdapter.INFO, "组件启动1:" + className);
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "缓存组件不存在");
			return MODULE_START_FAILED;
		}
//		log.log(this, LoggerAdapter.INFO, "组件启动2");
		try {
			cacheManager = (CacheManagerAdapter) startModule(log, className,
					mapParams);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (cacheManager == null) {
			log.log(this, LoggerAdapter.ERROR, "缓存组件构建有误");
			return MODULE_START_FAILED;
		}
//		log.log(this, LoggerAdapter.INFO, "组件启动3");
		logger.log(this, LoggerAdapter.INFO, "缓存组件:" + className);

		mapModule.put(CacheManagerAdapter.class.toString(), cacheManager);
		mapParams.put(CacheManagerAdapter.class.toString(), cacheManager);
		// 服务管理组件,将日志组件对象和缓存管理组件对象传递进入
		className = getClassName(ServiceModuleManagerAdapter.class.toString()
				.substring(6));
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "服务管理组件不存在");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "服务管理组件:" + className);
		serviceModuleManager = (ServiceModuleManagerAdapter) ServerUtilities
				.startModule(log, className, mapParams);
		if (serviceModuleManager == null) {
			log.log(this, LoggerAdapter.ERROR, "服务管理组件构建有误");
			return MODULE_START_FAILED;
		}

		mapModule.put(ServiceModuleManagerAdapter.class.toString(),
				serviceModuleManager);
		// 服务分流组件,将日志组件对象和缓存管理组件对象传递进入
		className = getClassName(ServiceShuntAdapter.class.toString()
				.substring(6));
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "服务路由组件不存在");
			return MODULE_START_FAILED;
		}

		logger.log(this, LoggerAdapter.INFO, "服务路由组件:" + className);
		serviceShunt = (ServiceShuntAdapter) ServerUtilities.startModule(log,
				className, mapParams);
		if (serviceShunt == null) {
			log.log(this, LoggerAdapter.ERROR, "服务路由组件构建有误");
			return MODULE_START_FAILED;
		}

		mapModule.put(ServiceShuntAdapter.class.toString(), serviceShunt);
		// 业务接口管理组件,将日志组件对象和缓存管理组件对象传递进入
		className = getClassName(OperInterfaceManagerAdapter.class.toString()
				.substring(6));
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "业务接口管理组件不存在");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "业务接口管理组件:" + className);
		operInterfaceManager = (OperInterfaceManagerAdapter) ServerUtilities
				.startModule(log, className, mapParams);
		if (operInterfaceManager == null) {
			log.log(this, LoggerAdapter.ERROR, "业务接口管理组件构建有误");
			return MODULE_START_FAILED;
		}

		mapModule.put(OperInterfaceManagerAdapter.class.toString(),
				operInterfaceManager);
		// 图形存储组件,将日志组件对象和缓存管理组件对象传递进入
		className = getClassName(GraphStorageManagerAdapter.class.toString()
				.substring(6));
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "图库组件不存在");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "图库组件:" + className);
		graphStorageManager = (GraphStorageManagerAdapter) ServerUtilities
				.startModule(log, className, mapParams);
		if (graphStorageManager == null) {
			log.log(this, LoggerAdapter.ERROR, "图库组件构建有误");
			return MODULE_START_FAILED;
		}

		mapModule.put(GraphStorageManagerAdapter.class.toString(),
				graphStorageManager);

		// 所有平台组件初始化
		logger.log(this, LoggerAdapter.INFO, "modules init");
		if (cacheManager.init() == cacheManager.MODULE_INITIALIZE_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "缓存组件初始化有误");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "缓存组件 init ok");
		if (serviceModuleManager.init() == serviceModuleManager.MODULE_INITIALIZE_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "服务管理组件初始化有误");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "服务管理组件 init ok");
		if (serviceShunt.init() == serviceShunt.MODULE_INITIALIZE_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "服务路由组件初始化有误");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "服务路由组件 init ok");
		if (operInterfaceManager.init() == operInterfaceManager.MODULE_INITIALIZE_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "业务接口管理组件初始化有误");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "业务接口管理组件 init ok");
		if (graphStorageManager.init() == graphStorageManager.MODULE_INITIALIZE_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "图库组件初始化有误");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "图库组件 init ok");
		// 所有平台组件启动
		logger.log(this, LoggerAdapter.INFO, "modules start");
		if (serviceModuleManager.start() == serviceModuleManager.MODULE_START_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "服务管理组件启动有误");
			return MODULE_START_FAILED;
		}
		return MODULE_START_COMPLETE;
	}

	/**
	 * 根据组件类型，获取组件调用类名
	 * 
	 * @param type:组件类型
	 * @return：组件调用类名
	 */
	protected String getClassName(String type) {
		ModuleInfoBean bean = (ModuleInfoBean) mapModuleInfo.get(type);
		if (bean != null)
			return bean.getClassName();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.GeneralModuleIF#stop(java.lang.Object)
	 */
	public int stop() {
		// TODO Auto-generated method stub
		return 0;
	}

	protected HashMap getSysJarList() {
		HashMap map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT ml_shortname,ml_name,ml_kind,ml_type,ml_edition,")
				.append(
						"ml_log_level,ml_log_type,ml_class_name,ml_edition,ml_location FROM t_svg_module_list a ")
				.append("WHERE ml_location ='2' AND ml_kind = '0' ");

		try {
			// 获取数据库连接
			Connection conn = dbma.getConnection("SVG");
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,获取系统架包组件清单失败！");
				return map;
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			getLogger().log(this, LoggerAdapter.DEBUG,
					"获取组件清单:" + sql.toString());
			while (rs.next()) {
				ModuleInfoBean bean = new ModuleInfoBean();
				bean.setModuleShortName(rs.getString("ml_shortname"));
				bean.setName(rs.getString("ml_name"));
				bean.setLocation(rs.getString("ml_location"));
				bean.setKind(rs.getString("ml_kind"));
				bean.setType(rs.getString("ml_type"));
				bean.setLogLevel(rs.getString("ml_log_level"));
				bean.setLogType(rs.getString("ml_log_type"));
				bean.setClassName(rs.getString("ml_class_name"));
				bean.setEdition(rs.getString("ml_edition"));
				String type = rs.getString("ml_shortname");
				map.put(type, bean);
			}
			log.log(this, LoggerAdapter.DEBUG, "成功获取服务器端组件清单!");
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			// e.printStackTrace();
			return map;
		}
		return map;
	}

	/**
	 * 利用传入的数据库连接，从数据库获取服务器端组件清单
	 * 
	 * @param conn：数据库连接
	 * @return：成功返回0，失败返回<0
	 */
	protected int getModuleList() {
		mapModuleInfo = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT ml_shortname,ml_name,ml_kind,ml_type,ml_edition,")
				.append(
						"ml_log_level,ml_log_type,ml_class_name,ml_edition,ml_location,t.cc_param1 param FROM t_svg_module_list a,t_svg_code_commsets t ")
				.append(
						"WHERE ml_location ='2' AND ml_kind = '1' AND a.ml_type = t.cc_code AND upper(t.cc_shortname) = 'SVG_MODULE_TYPE'");

		try {
			// 获取数据库连接
			Connection conn = dbma.getConnection("SVG");
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,获取组件清单失败！");
				return OPER_ERROR;
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			getLogger().log(this, LoggerAdapter.DEBUG,
					"获取组件清单:" + sql.toString());
			while (rs.next()) {
				ModuleInfoBean bean = new ModuleInfoBean();
				bean.setModuleShortName(rs.getString("ml_shortname"));
				bean.setName(rs.getString("ml_name"));
				bean.setLocation(rs.getString("ml_location"));
				bean.setKind(rs.getString("ml_kind"));
				bean.setType(rs.getString("ml_type"));
				bean.setLogLevel(rs.getString("ml_log_level"));
				bean.setLogType(rs.getString("ml_log_type"));
				bean.setClassName(rs.getString("ml_class_name"));
				bean.setEdition(rs.getString("ml_edition"));
				String type = rs.getString("param");
				mapModuleInfo.put(type, bean);
			}
			log.log(this, LoggerAdapter.DEBUG, "成功获取服务器端组件清单!");
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			// e.printStackTrace();
			return OPER_ERROR;
		}
		if (mapModuleInfo.size() == 0)
			return OPER_ERROR;
		return OPER_SUCCESS;
	}

	/**
	 * 根据传入的组件信息和数据库连接，从数据库下载组件架包至本地，并加载至jvm中
	 * 
	 * @param bean：组件信息
	 * @param conn：数据库连接
	 * @return：处理结果，成功返回0.失败返回<0
	 */
	protected int downloadModuleJar(ModuleInfoBean bean, String pathName) {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT ml_content FROM t_svg_module_list WHERE ml_shortname = '")
				.append(bean.getModuleShortName()).append("'");

		String type = dbma.getDBType("SVG");
		if (type != null) {
			// 通过方言转换类获取blob字段
			DBSqlIdiomAdapter sqlIdiom = dbma.getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return -1;
			// 获取数据库连接
			Connection conn = dbma.getConnection("SVG");
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,获取组件清单失败");
				return OPER_ERROR;
			}
			// 获取组件内容
			byte[] content = sqlIdiom.getBlob(conn, sql.toString(),
					"ml_content");
			log.log(this, LoggerAdapter.DEBUG, "成功获取名为‘"
					+ bean.getModuleShortName() + "'的组件！");
			// 关闭数据库连接
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.log(this, LoggerAdapter.ERROR, "下载组件，关闭数据库失败！");
					log.log(this, LoggerAdapter.ERROR, e);
					return OPER_ERROR;
				}
			}
			if (content != null) {
				String jarName = pathName + bean.getModuleShortName()
						+ bean.getEdition() + ".jar";
				try {
					FileOutputStream fos = new FileOutputStream(new File(
							jarName));
					fos.write(content, 0, content.length);
					fos.close();
					File localJarFile = new File(jarName);
					if (localJarFile.exists()) {
						addClassPath(localJarFile);
					}

				} catch (Exception e) {
					log.log(this, LoggerAdapter.ERROR, e);
					// e.printStackTrace();
				}
				return OPER_SUCCESS;
			}
		}

		return OPER_ERROR;
	}

	public boolean isStoped() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 主管理组件初始化日志对象
	 */
	protected LoggerAdapter initLoggerAdapter() {
		LoggerAdapter logger = ServerLoggerImpl.getInstance(this);
		logger.start();
		return logger;
	}

	protected DBConnectionManagerAdapter initDBConnectionManagerAdapter() {
		return DBConnectionManagerImpl.getInstance(this);
	}

	/**
	 * 根据传入的配置集合名和配置参数名获取配置参数值
	 * 
	 * @param section：配置集合名
	 * @param profile：配置参数名
	 * @return：配置参数值 配置文件固定放置在WEB-INF\\classes\\server.xml目录下， 格式为 <xml> <配置集合名>
	 *               <配置参数名>配置参数值</配置参数名> </配置集合名> </xml>
	 */
	protected String readServerCfg(String section, String profile) {
		String fileName = "/WEB-INF/classes/server.xml";
		// 下面代码在部署到servlet上时使用
		fileName = Global.appRoot + fileName;
		// 上面代码在部署到servlet上时使用

		return ServerUtilities.readCfg(fileName, section, profile);
	}

	public CacheManagerAdapter getCacheManager() {
		// TODO Auto-generated method stub
		return cacheManager;
	}

	public GraphStorageManagerAdapter getGraphStorageManager() {
		// TODO Auto-generated method stub
		return graphStorageManager;
	}

	public OperInterfaceManagerAdapter getOperInterfaceManager() {
		// TODO Auto-generated method stub
		return operInterfaceManager;
	}

	public ServiceModuleManagerAdapter getServiceModuleManager() {
		// TODO Auto-generated method stub
		return serviceModuleManager;
	}

	public ServiceShuntAdapter getServiceShunt() {
		// TODO Auto-generated method stub
		return serviceShunt;
	}

	public synchronized void addClassPath(File file) {
		NCIClassLoader.addClassPath(file);

	}

	public synchronized Object startModule(LoggerAdapter log, String className,
			HashMap mapParams) {
		Object obj = null;
		log.log(null, LoggerAdapter.INFO, "startModule:" + className);
		try {
			Class[] classargs = { HashMap.class };
			Object[] args = { mapParams };
			log.log(null, LoggerAdapter.INFO, "开始 Class.forName");
			Class cl = Thread.currentThread().getContextClassLoader()
					.loadClass(className);
			// Class cl =
			// NCIClassLoader.loadSystemClass(className);//Class.forName(className);
			log.log(null, LoggerAdapter.INFO, "开始构造Constructor");
			Constructor constructor = cl.getConstructor(classargs);
			log.log(null, LoggerAdapter.INFO, "开始构造object");
			obj = constructor.newInstance(args);
			log.log(null, LoggerAdapter.INFO, "startModule3:" + className);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("启动" + className + "类，失败！");
			log.log(null, LoggerAdapter.ERROR, "startModule:" + className
					+ ":error");
			return null;
		}
		log.log(null, LoggerAdapter.INFO, "startModule:ok:" + className);
		return obj;
	}

}
