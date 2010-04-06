/**
 * ʵ��DBCP�����ݿ����ӳ�
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
	 * ������
	 */
	private Class driverClass = null;
	/**
	 * ���ӳ�
	 */
	private ObjectPool connectionPool = null;
	/**
	 * ���ӳؼ�
	 */
	public Map map = null;
	/**
	 * ��־����
	 */
	private static LoggerAdapter log;
	/**
	 * ���ݿ�������Ϣ
	 */
	private HashMap poolInformation = new HashMap();
	/**
	 * ��������Ķ���
	 */
	private static DBConnectionManagerAdapter dbConnectionManager;

	/**
	 * ���캯��
	 * 
	 * @param mainModuleController
	 */
	private DBConnectionManagerImpl(HashMap parameters) {
		super(parameters);
	}

	/**
	 * ��ȡ���ݿ����ӳع������
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
	 * ��ʼ������Դ
	 */
	private void initDataSource(String driverClassName) {
		// ��������Դ
		try {
			driverClass = Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			System.out.println("δ�ҵ����ݿ�������");
			e.printStackTrace();
		}
	}

	/**
	 * �ͷ����ӳ�
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
	 * ���ӳ�����
	 * 
	 * @param poolname
	 *            String ���ӳ�����
	 * @param dbJdbc
	 *            String ���ݿ��ַ
	 * @param dbUser
	 *            String ��¼�û���
	 * @param dbPwd
	 *            String ��¼����
	 * @param max
	 *            integer ���������
	 * @param wait
	 *            long �ȴ�ʱ��
	 * @param driverClassName
	 *            String ��������
	 * @param type
	 *            String ��������
	 * @throws Exception
	 */
	private void StartPool(String poolname, String dbJdbc, String dbUser,
			String dbPwd, int max, long wait, String driverClassName,
			String type) {
		// ��ʼ������Դ
		initDataSource(driverClassName);

		// ������ӳز�Ϊ��
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

			// �������õ����ӳؼ��뵽���ӳؼ���
			map.put(poolname, connectionPool);
			log
					.log(this, LoggerAdapter.DEBUG, "�ɹ�������Ϊ��" + poolname
							+ "���ݿ����ӳأ�");
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
			log.log(this, LoggerAdapter.DEBUG, "���ݿ����ӳؼ�Ϊ�գ�");
			map = new HashMap();
		}
		if (map.get(poolName) == null) {
			// initPool(poolName);// ��ʼ����������
			poolInformation = read();
			ConnBean baseConnBean = (ConnBean) poolInformation.get(poolName);
			String dbJdbc = baseConnBean.getJdbcurl(); // ���ݿ�����
			String dbUser = baseConnBean.getUsername(); // ��¼�û���
			String dbPwd = baseConnBean.getPassword(); // ��¼����
			int max = baseConnBean.getMax(); // ���������
			long wait = baseConnBean.getWait(); // ��ȴ�ʱ��
			String driverClassName = baseConnBean.getDriver(); // ��������
			String type = baseConnBean.getDbType(); // ��������

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
	 * ��ȡ��Ϊsvg�����ӳ�����
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
	 * �������ļ��ж�ȡ���ݿ����ӳ�����
	 * 
	 * @param fileName
	 *            String �����ļ���
	 * @return ���ӳ�������Ϣ
	 */
	private HashMap read(String fileName) {
		HashMap pools = new HashMap();
		Document document;
		log.log(this, LoggerAdapter.DEBUG, "��ʼ��ȡ���ݿ������ļ�...");

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();

			// ��������ڲ���servlet��ʱʹ��
			fileName = Global.appRoot + "/WEB-INF/classes/" + fileName;
			// ��������ڲ���servlet��ʱʹ��

			document = builder.parse(new File(fileName));
			document.getDocumentElement().normalize();

			// ��ȡ���ڵ�
			Node root = document.getFirstChild();
			NodeList poolList = root.getChildNodes();
			for (int i = 0; i < poolList.getLength(); i++) {
				Node poolNode = poolList.item(i);
				if (poolNode.getNodeName().equals("pool")) { // ��ȡpoolԪ�ؽڵ�
					ConnBean cb = new ConnBean();
					NodeList paramList = poolNode.getChildNodes();
					boolean flag = false;
					for (int k = 0, pSize = paramList.getLength(); k < pSize; k++) {
						Node paramNode = paramList.item(k);

						// ��ȡ���ӳ�����
						if (paramNode.getNodeName().equals("name"))
							cb.setName(paramNode.getFirstChild().getNodeValue()
									.toUpperCase());
						// ��ȡ��¼�û���
						if (paramNode.getNodeName().equals("username"))
							cb.setUsername(paramNode.getFirstChild()
									.getNodeValue());
						// ��ȡ��¼����
						if (paramNode.getNodeName().equals("password"))
							cb.setPassword(paramNode.getFirstChild()
									.getNodeValue());
						// ��ȡ���ݿ��ַ
						if (paramNode.getNodeName().equals("jdbcurl"))
							cb.setJdbcurl(paramNode.getFirstChild()
									.getNodeValue());
						// ��ȡ���ݿ���������
						if (paramNode.getNodeName().equals("driver"))
							cb.setDriver(paramNode.getFirstChild()
									.getNodeValue());
						// ��ȡ���ӳ����������
						if (paramNode.getNodeName().equals("max")) {
							try {
								cb.setMax(Integer.parseInt(paramNode
										.getFirstChild().getNodeValue()));
							} catch (NumberFormatException e) {
								cb.setMax(3);
							}
						}
						// ��ȡ���ӳ���ȴ�ʱ��
						if (paramNode.getNodeName().equals("wait")) {
							try {
								cb.setWait(Long.parseLong(paramNode
										.getFirstChild().getNodeValue()));
							} catch (NumberFormatException e) {
								cb.setWait(1000L);
							}
						}

						// ��ȡ���ݿ�����
						if (paramNode.getNodeName().equals("type")) {
							// �ȶ����ݿ����ͣ�����֧�ֵĶ������򲻼��뻺���
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

		log.log(this, LoggerAdapter.DEBUG, "�ɹ���ȡ���ݿ������ļ���Ϣ��");
		return pools;
	}

	/**
	 * �ر�����
	 * 
	 * @param c
	 *            Connection ���ݿ�����
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

			stat.append(poolName + ":�������: ");
			stat.append(connectionPool.getNumActive());
			stat.append(", ");
			stat.append("����������: ");
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
	 *      ��ȡ���ݿ�����
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
	 *      �������ݿ�����,���������ݿ�У��
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
