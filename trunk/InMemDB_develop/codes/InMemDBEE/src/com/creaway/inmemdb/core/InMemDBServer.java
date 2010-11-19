/*
 * @(#)InMemDBServer.java	1.0  08/24/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.creaway.inmemdb.InMemSession;
import com.creaway.inmemdb.api.datapush.PushServiceIfc;
import com.creaway.inmemdb.api.jndi.InMemDBContextService;
import com.creaway.inmemdb.cluster.SlaveConnectionFactory;
import com.creaway.inmemdb.datapush.PushService;
import com.creaway.inmemdb.jndi.InMemDBContextFactory;
import com.creaway.inmemdb.util.ConnectionManager;
import com.creaway.inmemdb.util.DBLogger;
import com.creaway.inmemdb.util.Functions;

/**
 * 内存数据库主服务抽象类，抽象数据库引擎的适配器
 * 
 * @author Qil.Wong
 * 
 */
public abstract class InMemDBServer implements InMemDBServerMBean {

	protected List<DBModule> coreModules = new LinkedList<DBModule>();

	// 活动的数据库session
	protected Map<Integer, InMemSession> idleMasterSessions = new ConcurrentHashMap<Integer, InMemSession>();

	/**
	 * 系统属性（不是当前jre属性，是当前数据库内部属性）
	 */
	protected Properties systemProperties;

	protected Properties errors;

	/**
	 * JMX MBean服务器
	 */
	protected MBeanServer mbs;

	public static final String MBEAN_PREFIX = "com.creaway.inmemdb:type=";

	public static final String SERVER_MBEAN_NAME = "CORE SERVER";

	/**
	 * 服务器是否已经启动的标签
	 */
	protected boolean started = false;

	/**
	 * 数据推送服务
	 */
	protected PushServiceIfc pushService;

	/**
	 * JNDI服务端提供给外部应用的服务
	 */
	protected InMemDBContextService jndiContext;
	

	/**
	 * 服务器线程池
	 */
	private ThreadPoolExecutor threadPool;

	private static InMemDBServer db;

	protected InMemDBServer() {
		System.out.println(getClass().getClassLoader().getClass().getName());
		db = this;
		PropertyConfigurator.configureAndWatch(
				getClass().getResource("/log4j.properties").getFile(), 2000);
		try {
			mbs = ManagementFactory.getPlatformMBeanServer();
			registerMBean(this,
					new ObjectName(MBEAN_PREFIX + SERVER_MBEAN_NAME));
		} catch (Exception e) {
			DBLogger.log(DBLogger.ERROR, "", e);
		}
		Runtime.getRuntime().addShutdownHook(
				new Thread("InMemDB Server Shutdown") {
					public void run() {
						shutdownModule(getSystemProperties());
					}
				});
	}

	// 应用程序入口必须是InMemDBServer的实现类
	public static InMemDBServer getInstance() {
		return db;
	}
	
	/**
	 * 初始化线程池
	 */
	private void initThreadPool() {
		int corePoolSize = Integer.parseInt(InMemDBServer.getInstance()
				.getSystemProperties().getProperty("core_threadpool_size"));
		int maxPoolSize = Integer.parseInt(InMemDBServer.getInstance()
				.getSystemProperties().getProperty("max_threadpool_size"));
		int workingQueueSize = Integer.parseInt(InMemDBServer.getInstance()
				.getSystemProperties().getProperty("working_queue_size"));
		threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 10,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(
						workingQueueSize), new InMemDBThreadFactory(
						"inmendb-pool-thread-"),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	/**
	 * 检测JMX连入的安全性
	 * 
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean checkJMXSecurity(String user, String password) {
		boolean result = getSystemProperties().getProperty("jmx_user").equals(
				user)
				&& getSystemProperties().getProperty("jmx_password").equals(
						password);
		if (!result) {
			DBLogger.log(DBLogger.ERROR, "JMX 验证失败，user=" + user + " psw="
					+ password);
		}
		return result;

	}

	@Override
	public void startModule(Map<?, ?> params) {
		DBLogger.log(DBLogger.INFO, "内存数据库服务准备启动...");		
		loadProperties();
		initThreadPool();
		pushService = new PushService();
		// 加载数据库和数据库连接
		SlaveConnectionFactory.init();
		ConnectionManager.load();
		initModules(params);
		bindJNDI();
		started = true;
	}

	protected void loadProperties() {
		InputStream is = getClass().getResourceAsStream(
				"/resources/dbserver.properties");
		try {
			systemProperties = new Properties();
			systemProperties.load(is);
			is.close();
		} catch (IOException e1) {
			DBLogger.log(DBLogger.ERROR, "加载系统属性文件dbserver.properties失败！", e1);
		}
		is = getClass().getResourceAsStream("/resources/error.conf");
		try {
			errors = new Properties();
			errors.load(is);
			is.close();
		} catch (IOException e1) {
			DBLogger.log(DBLogger.ERROR, "加载错误码error.conf失败！", e1);
		}
	}

	/**
	 * 初始化系统模块
	 * 
	 * @param params
	 */
	private void initModules(Map<?, ?> params) {
		Document modulesDoc = Functions.getXMLDocument(getClass().getResource(
				"/resources/modules.xml"));
		NodeList modules = modulesDoc.getElementsByTagName("module");
		for (int i = 0; i < modules.getLength(); i++) {
			Element moduleEle = (Element) modules.item(i);
			try {
				Class<?> c = Class.forName(moduleEle.getAttribute("class"));
				coreModules.add((DBModule) c.newInstance());
			} catch (ClassNotFoundException e) {
				DBLogger.log(DBLogger.ERROR,
						"无法找到对应的模块类" + moduleEle.getAttribute("class"), e);
			} catch (Exception e) {
				DBLogger.log(DBLogger.ERROR,
						"模块类" + moduleEle.getAttribute("class") + "反射生成对象错误", e);
			}
		}
		for (DBModule mo : coreModules) {
			mo.startModule(params);
		}
	}

	@Override
	public void shutdownModule(Map<?, ?> params) {
		DBLogger.log(DBLogger.INFO, "内存数据库服务准备关闭");
		unbindJNDI();
		for (int i = coreModules.size(); i > 0; i--) {
			coreModules.get(i - 1).shutdownModule(params);
		}
		coreModules.clear();
		idleMasterSessions.clear();
		systemProperties = null;
		if (pushService != null)
			pushService.close();
		pushService = null;
		unregisterMBeans();
		started = false;
		SlaveConnectionFactory.destroy();
		threadPool.shutdown();		
		System.gc();
		DBLogger.log(DBLogger.INFO, "内存数据库服务已关闭");
	}

	/**
	 * 根据指定的Class类型获取相对应的模块实例
	 * 
	 * @param moduleImpl
	 * @return
	 */
	public DBModule getModule(Class<?> moduleImpl) {
		for (DBModule mo : coreModules) {
			if (moduleImpl.isInstance(mo)) {
				return mo;
			}
		}
		return null;
	}

	/**
	 * 获取服务器当前定义的系统属性（不是jre系统属性）
	 * 
	 * @return
	 */
	public Properties getSystemProperties() {
		return systemProperties;
	}
	
	public Properties getErrors(){
		return errors;
	}

	/**
	 * 获取正在事务中的活动集群Master session
	 * 
	 * @return
	 */
	public Map<Integer, InMemSession> getIdleMasterDataBaseSessions() {
		return idleMasterSessions;
	}

	/**
	 * 创建内部数据库会话
	 * 
	 * @return 数据库会话
	 */
	public abstract InMemSession createDataBaseSession();

	/**
	 * 卸载已注册的内存数据库MBean，当前主服务MBean除外
	 */
	private void unregisterMBeans() {
		Set<ObjectName> names = mbs.queryNames(null, null);
		for (ObjectName o : names) {
			if (o.toString().indexOf("com.creaway.inmemdb") >= 0
					&& o.toString().indexOf(SERVER_MBEAN_NAME) < 0) {
				try {
					DBLogger.log(DBLogger.INFO, "卸载MBean:" + o);
					mbs.unregisterMBean(o);
				} catch (Exception e) {
					DBLogger.log(DBLogger.ERROR, "移除MBean错误", e);
				}
			}
		}
	}

	/**
	 * 注册MBean管理对象
	 * 
	 * @param mBean
	 * @param name
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws InstanceAlreadyExistsException
	 * @throws MBeanRegistrationException
	 * @throws NotCompliantMBeanException
	 * @throws ReflectionException
	 * @throws InstanceNotFoundException
	 */
	public void registerMBean(Object mBean, ObjectName name)
			throws MalformedObjectNameException, NullPointerException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, InstanceNotFoundException, ReflectionException {
		DBLogger.log(DBLogger.INFO, "注册MBean:" + name);		
		mbs.registerMBean(mBean, name);
	}

	@Override
	public void start(String user, String psw) {
		if (!isStarted()) {
			loadProperties();
			if (checkJMXSecurity(user, psw)) {
				startModule(getSystemProperties());
			}
		}
	}

	@Override
	public void stop(String user, String psw) {
		if (isStarted() && checkJMXSecurity(user, psw)) {
			shutdownModule(getSystemProperties());
		}
	}

	/**
	 * 判断服务器是否已正常启动
	 * 
	 * @return
	 */
	public boolean isStarted() {
		return started;
	}

	public PushServiceIfc getPushService() {
		return pushService;
	}

	private void bindJNDI() {
		jndiContext = new InMemDBContextService();
	}

	private void unbindJNDI() {
		DBLogger.log(DBLogger.INFO,
				"移除JNDI：" + InMemDBContextFactory.class.getName());
		jndiContext = null;
	}

	public Object getJndiContextObject() {
		return jndiContext;
	}
	

	public ThreadPoolExecutor getThreadPool() {
		return threadPool;
	}
}
