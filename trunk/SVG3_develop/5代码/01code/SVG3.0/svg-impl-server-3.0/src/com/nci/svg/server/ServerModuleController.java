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
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ�Servlet�������������
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
	 * ������־����
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
	 *      ��ʼ��,��ʼ����Ҫ������ݿ����Ӻ�����嵥���ػ�ȡ
	 */
	public int init() {
		// ��ʼ����־���
		logger = initLoggerAdapter();
		// ��ȡ��־����
		log = getLogger();
		// ��ʼ�����ݿ����
		dbma = initDBConnectionManagerAdapter();

		int ret = 0;
		// ���Ȼ�ȡϵͳ����ܰ����
		HashMap map = getSysJarList();
		String pathName = getCachePath();
		Iterator iterator = null;
		iterator = map.values().iterator();
		while (iterator.hasNext()) {
			ModuleInfoBean bean = (ModuleInfoBean) iterator.next();
			ret = downloadModuleJar(bean, pathName);
		}

		// ��ȡϵͳ����ƽ̨���
		ret = getModuleList();
		if (ret != 0) {
			log.log(this, LoggerAdapter.ERROR, "��ȡ����嵥ʧ��");
			return OPER_ERROR;
		}
		// ��������嵥��һ���������������
		if (mapModuleInfo.size() == 0) {
			log.log(this, LoggerAdapter.ERROR, "����嵥Ϊ�գ�����ʧ��");
			return OPER_ERROR;
		}

		// ��ȡ���ش洢·����Ϣ

		iterator = mapModuleInfo.values().iterator();
		while (iterator.hasNext()) {
			ModuleInfoBean bean = (ModuleInfoBean) iterator.next();
			ret = downloadModuleJar(bean, pathName);
		}

		return OPER_SUCCESS;
	}

	/**
	 * ��ȡ���ش洢·��
	 * 
	 * @return�����ش洢·��
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
	 *      �������������,��������ʵ��������ʼ�� ʵ��������ʼ�����̾���Ҫ�ڿ���̨��ӡ
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
		// ��������嵥��һ�������
		// mapModuleInfo
		// �������־���Դ��ݸ���־���
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
		// ����������
		log.log(this, LoggerAdapter.INFO, "�������");
		className = getClassName(CacheManagerAdapter.class.toString()
				.substring(6));
//		log.log(this, LoggerAdapter.INFO, "�������1:" + className);
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "�������������");
			return MODULE_START_FAILED;
		}
//		log.log(this, LoggerAdapter.INFO, "�������2");
		try {
			cacheManager = (CacheManagerAdapter) startModule(log, className,
					mapParams);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (cacheManager == null) {
			log.log(this, LoggerAdapter.ERROR, "���������������");
			return MODULE_START_FAILED;
		}
//		log.log(this, LoggerAdapter.INFO, "�������3");
		logger.log(this, LoggerAdapter.INFO, "�������:" + className);

		mapModule.put(CacheManagerAdapter.class.toString(), cacheManager);
		mapParams.put(CacheManagerAdapter.class.toString(), cacheManager);
		// ����������,����־�������ͻ������������󴫵ݽ���
		className = getClassName(ServiceModuleManagerAdapter.class.toString()
				.substring(6));
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "����������������");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "����������:" + className);
		serviceModuleManager = (ServiceModuleManagerAdapter) ServerUtilities
				.startModule(log, className, mapParams);
		if (serviceModuleManager == null) {
			log.log(this, LoggerAdapter.ERROR, "������������������");
			return MODULE_START_FAILED;
		}

		mapModule.put(ServiceModuleManagerAdapter.class.toString(),
				serviceModuleManager);
		// ����������,����־�������ͻ������������󴫵ݽ���
		className = getClassName(ServiceShuntAdapter.class.toString()
				.substring(6));
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "����·�����������");
			return MODULE_START_FAILED;
		}

		logger.log(this, LoggerAdapter.INFO, "����·�����:" + className);
		serviceShunt = (ServiceShuntAdapter) ServerUtilities.startModule(log,
				className, mapParams);
		if (serviceShunt == null) {
			log.log(this, LoggerAdapter.ERROR, "����·�������������");
			return MODULE_START_FAILED;
		}

		mapModule.put(ServiceShuntAdapter.class.toString(), serviceShunt);
		// ҵ��ӿڹ������,����־�������ͻ������������󴫵ݽ���
		className = getClassName(OperInterfaceManagerAdapter.class.toString()
				.substring(6));
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "ҵ��ӿڹ������������");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "ҵ��ӿڹ������:" + className);
		operInterfaceManager = (OperInterfaceManagerAdapter) ServerUtilities
				.startModule(log, className, mapParams);
		if (operInterfaceManager == null) {
			log.log(this, LoggerAdapter.ERROR, "ҵ��ӿڹ��������������");
			return MODULE_START_FAILED;
		}

		mapModule.put(OperInterfaceManagerAdapter.class.toString(),
				operInterfaceManager);
		// ͼ�δ洢���,����־�������ͻ������������󴫵ݽ���
		className = getClassName(GraphStorageManagerAdapter.class.toString()
				.substring(6));
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "ͼ�����������");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "ͼ�����:" + className);
		graphStorageManager = (GraphStorageManagerAdapter) ServerUtilities
				.startModule(log, className, mapParams);
		if (graphStorageManager == null) {
			log.log(this, LoggerAdapter.ERROR, "ͼ�������������");
			return MODULE_START_FAILED;
		}

		mapModule.put(GraphStorageManagerAdapter.class.toString(),
				graphStorageManager);

		// ����ƽ̨�����ʼ��
		logger.log(this, LoggerAdapter.INFO, "modules init");
		if (cacheManager.init() == cacheManager.MODULE_INITIALIZE_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "���������ʼ������");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "������� init ok");
		if (serviceModuleManager.init() == serviceModuleManager.MODULE_INITIALIZE_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "������������ʼ������");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "���������� init ok");
		if (serviceShunt.init() == serviceShunt.MODULE_INITIALIZE_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "����·�������ʼ������");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "����·����� init ok");
		if (operInterfaceManager.init() == operInterfaceManager.MODULE_INITIALIZE_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "ҵ��ӿڹ��������ʼ������");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "ҵ��ӿڹ������ init ok");
		if (graphStorageManager.init() == graphStorageManager.MODULE_INITIALIZE_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "ͼ�������ʼ������");
			return MODULE_START_FAILED;
		}
		logger.log(this, LoggerAdapter.INFO, "ͼ����� init ok");
		// ����ƽ̨�������
		logger.log(this, LoggerAdapter.INFO, "modules start");
		if (serviceModuleManager.start() == serviceModuleManager.MODULE_START_FAILED) {
			log.log(this, LoggerAdapter.ERROR, "������������������");
			return MODULE_START_FAILED;
		}
		return MODULE_START_COMPLETE;
	}

	/**
	 * ����������ͣ���ȡ�����������
	 * 
	 * @param type:�������
	 * @return�������������
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
			// ��ȡ���ݿ�����
			Connection conn = dbma.getConnection("SVG");
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,��ȡϵͳ�ܰ�����嵥ʧ�ܣ�");
				return map;
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			getLogger().log(this, LoggerAdapter.DEBUG,
					"��ȡ����嵥:" + sql.toString());
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
			log.log(this, LoggerAdapter.DEBUG, "�ɹ���ȡ������������嵥!");
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
	 * ���ô�������ݿ����ӣ������ݿ��ȡ������������嵥
	 * 
	 * @param conn�����ݿ�����
	 * @return���ɹ�����0��ʧ�ܷ���<0
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
			// ��ȡ���ݿ�����
			Connection conn = dbma.getConnection("SVG");
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,��ȡ����嵥ʧ�ܣ�");
				return OPER_ERROR;
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			getLogger().log(this, LoggerAdapter.DEBUG,
					"��ȡ����嵥:" + sql.toString());
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
			log.log(this, LoggerAdapter.DEBUG, "�ɹ���ȡ������������嵥!");
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
	 * ���ݴ���������Ϣ�����ݿ����ӣ������ݿ���������ܰ������أ���������jvm��
	 * 
	 * @param bean�������Ϣ
	 * @param conn�����ݿ�����
	 * @return�����������ɹ�����0.ʧ�ܷ���<0
	 */
	protected int downloadModuleJar(ModuleInfoBean bean, String pathName) {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT ml_content FROM t_svg_module_list WHERE ml_shortname = '")
				.append(bean.getModuleShortName()).append("'");

		String type = dbma.getDBType("SVG");
		if (type != null) {
			// ͨ������ת�����ȡblob�ֶ�
			DBSqlIdiomAdapter sqlIdiom = dbma.getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return -1;
			// ��ȡ���ݿ�����
			Connection conn = dbma.getConnection("SVG");
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,��ȡ����嵥ʧ��");
				return OPER_ERROR;
			}
			// ��ȡ�������
			byte[] content = sqlIdiom.getBlob(conn, sql.toString(),
					"ml_content");
			log.log(this, LoggerAdapter.DEBUG, "�ɹ���ȡ��Ϊ��"
					+ bean.getModuleShortName() + "'�������");
			// �ر����ݿ�����
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.log(this, LoggerAdapter.ERROR, "����������ر����ݿ�ʧ�ܣ�");
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
	 * �����������ʼ����־����
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
	 * ���ݴ�������ü����������ò�������ȡ���ò���ֵ
	 * 
	 * @param section�����ü�����
	 * @param profile�����ò�����
	 * @return�����ò���ֵ �����ļ��̶�������WEB-INF\\classes\\server.xmlĿ¼�£� ��ʽΪ <xml> <���ü�����>
	 *               <���ò�����>���ò���ֵ</���ò�����> </���ü�����> </xml>
	 */
	protected String readServerCfg(String section, String profile) {
		String fileName = "/WEB-INF/classes/server.xml";
		// ��������ڲ���servlet��ʱʹ��
		fileName = Global.appRoot + fileName;
		// ��������ڲ���servlet��ʱʹ��

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
			log.log(null, LoggerAdapter.INFO, "��ʼ Class.forName");
			Class cl = Thread.currentThread().getContextClassLoader()
					.loadClass(className);
			// Class cl =
			// NCIClassLoader.loadSystemClass(className);//Class.forName(className);
			log.log(null, LoggerAdapter.INFO, "��ʼ����Constructor");
			Constructor constructor = cl.getConstructor(classargs);
			log.log(null, LoggerAdapter.INFO, "��ʼ����object");
			obj = constructor.newInstance(args);
			log.log(null, LoggerAdapter.INFO, "startModule3:" + className);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("����" + className + "�࣬ʧ�ܣ�");
			log.log(null, LoggerAdapter.ERROR, "startModule:" + className
					+ ":error");
			return null;
		}
		log.log(null, LoggerAdapter.INFO, "startModule:ok:" + className);
		return obj;
	}

}
