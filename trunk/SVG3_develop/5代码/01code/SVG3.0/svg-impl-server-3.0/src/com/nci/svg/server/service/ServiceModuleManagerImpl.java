package com.nci.svg.server.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import com.nci.svg.sdk.bean.ModuleInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.sdk.server.service.ServiceModuleManagerAdapter;
import com.nci.svg.sdk.server.service.ServiceShuntAdapter;
import com.nci.svg.sdk.server.util.ServerUtilities;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：业务服务组件管理组件实现类
 * 
 */
public class ServiceModuleManagerImpl extends ServiceModuleManagerAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8616941127521330960L;
	public static final String moduleID = "20fe40c7-f0ea-4d7f-9fd6-99da65bfa00d";
	private HashMap mapModuleInfo = null;
	/**
	 * 操作失败
	 */
	private final static int OPER_ERROR = -1;
	/**
	 * 操作成功
	 */
	private final static int OPER_SUCCESS = 0;
	/**
	 * 操作日志对象
	 */
	private static LoggerAdapter log;

	public ServiceModuleManagerImpl(HashMap parameters) {
		super(parameters);
	}

	public String getModuleID() {
		return moduleID;
	}

	public int init() {
		// 初始化操作，下载所有的业务服务组件清单至本地
		int ret = 0;
		try
		{
			String key = ServerModuleControllerAdapter.class
			.toString();
			System.out.println(key);
			Object obj = getParameter(key);
			System.out.println(obj);
		controller = (com.nci.svg.sdk.server.ServerModuleControllerAdapter)obj ;
		// 获取日志对象
		log = controller.getLogger();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return MODULE_INITIALIZE_FAILED;
		}
		// 获取组件清单
		ret = getModuleList();
		if (ret < 0) {
			log.log(this, LoggerAdapter.ERROR, "获取业务服务组件清单失败！");
		}

		// 根据组件清单逐一下载组件包至本地
		if (mapModuleInfo.size() == 0) {
			log.log(this, LoggerAdapter.INFO, "组件清单为空，启动失败！");
			return MODULE_INITIALIZE_COMPLETE;
		}

		// 获取本地存储路径信息
		String pathName = controller.getCachePath();
		Iterator iterator = mapModuleInfo.values().iterator();
		while (iterator.hasNext()) {
			ModuleInfoBean bean = (ModuleInfoBean) iterator.next();
			ret = downloadModuleJar(bean, pathName);
		}
		return super.init();
	}

	/**
	 * 获取业务服务组件清单
	 * 
	 * @return：成功返回0，失败返回小于0
	 */
	private int getModuleList() {
		mapModuleInfo = new HashMap();

		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT ml_shortname,ml_name,ml_kind,ml_type,ml_edition,")
				.append(
						"ml_log_level,ml_log_type,ml_class_name,ml_edition,ml_location FROM t_svg_module_list a ")
				.append(
						"WHERE ml_location ='2' AND ml_kind = '2' AND ml_type = '18'");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		try {
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法获取业务服务组件清单！");
				return OPER_ERROR;
			}

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
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
				String shortname = rs.getString("ml_shortname");
				mapModuleInfo.put(shortname, bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			controller.getDBManager().close(conn);
			log.log(this, LoggerAdapter.ERROR, e);
			return OPER_ERROR;
		} finally {
			// 关闭数据库连接
			controller.getDBManager().close(conn);
		}
		if (mapModuleInfo.size() == 0)
			return OPER_ERROR;
		return OPER_SUCCESS;
	}

	/**
	 * 根据传入的组件信息和数据库连接，从数据库下载组件架包至本地，并加载至jvm中
	 * 
	 * @param bean：组件信息
	 * @param pathName：存储路径
	 * @return：处理结果，成功返回0.失败返回<0
	 */
	protected int downloadModuleJar(ModuleInfoBean bean, String pathName) {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT ml_content FROM t_svg_module_list WHERE ml_shortname = '")
				.append(bean.getModuleShortName()).append("'");

		String type = controller.getDBManager().getDBType("SVG");
		if (type != null) {
			// 通过方言转换类获取blob字段
			DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
					.getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return -1;
			// 获取数据库连接
			Connection conn = controller.getDBManager().getConnection("svg");
			// 获取组件包内容
			byte[] content = sqlIdiom.getBlob(conn, sql.toString(),
					"ml_content");
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					log.log(this, LoggerAdapter.ERROR, "下载服务器组件包，关闭数据库连接出错！");
					log.log(this, LoggerAdapter.ERROR, e1);
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
						controller.addClassPath(localJarFile);
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

	public int start() {
		// 启动所有的业务服务组件
		Iterator iterator = mapModuleInfo.values().iterator();
		String className = null;
		while (iterator.hasNext()) {
			ModuleInfoBean bean = (ModuleInfoBean) iterator.next();
			className = bean.getClassName();
			if (className == null || className.length() == 0)
				continue;

			OperationServiceModuleAdapter module = (OperationServiceModuleAdapter) ServerUtilities
					.startModule(className, parameters);
			if (module != null) {
				module.init();
			}
		}
		return super.start();
	}

	public ResultBean configModule(String operType, String shortName) {
		ResultBean bean = null;
		if (operType.equals(OPER_ADD)) {
			bean = addModule(shortName);
		} else if (operType.equals(OPER_MODIFY)) {
			bean = modifyModule(shortName);
		} else if (operType.equals(OPER_DELETE)) {
			
			bean = deleteModule(shortName);
		}
		return bean;
	}
	
	/**
	 * 新增组件
	 * @param shortName：组件短名
	 * @return：新增结果集
	 */
	private ResultBean addModule(String shortName)
	{
		int ret = OPER_ERROR;
		ModuleInfoBean bean = getModuleBean(shortName);
		String pathName = controller.getCachePath();
		if(bean != null)
		{
			ret = downloadModuleJar(bean,pathName);
		}
		return new ResultBean(ret,"",null,null);
	}
	
	/**
	 * 维护组件
	 * @param shortName：组件短名
	 * @return：维护结果集
	 */
	private ResultBean modifyModule(String shortName)
	{
		int ret = OPER_ERROR;
		ModuleInfoBean newBean = getModuleBean(shortName);
		ModuleInfoBean oldBean = (ModuleInfoBean)mapModuleInfo.get(shortName);
		if(newBean != null || oldBean != null)
		{
			if(newBean.getEdition().equals(oldBean.getEdition()))
			{
				//组件版本没有变化，视为只改变了参数而已
				mapModuleInfo.put(shortName, newBean);
				ret = OPER_SUCCESS;
			}
			else
			{
				String pathName = controller.getCachePath();
				controller.getServiceShunt().handleService(
				    ServiceShuntAdapter.HANDLE_DELETE,
				    ServiceShuntAdapter.SERVICE_MODULE, shortName);
				String jarName = pathName + oldBean.getModuleShortName()
				+ oldBean.getEdition() + ".jar";
				//删除老版本的jar文件
				boolean flag = new File(jarName).delete();
				//下载新jar文件
				downloadModuleJar(newBean,pathName);
				
				mapModuleInfo.put(shortName, newBean);
				
				controller.getServiceShunt().handleService(
					    ServiceShuntAdapter.HANDLE_ADD,
					    ServiceShuntAdapter.SERVICE_MODULE, shortName);
				ret = OPER_SUCCESS;
			}
		}
		return new ResultBean(ret,"",null,null) ;
	}
	
	/**
	 * 删除组件
	 * @param shortName：组件短名
	 * @return：删除结果集
	 */
	private ResultBean deleteModule(String shortName)
	{
		return controller.getServiceShunt().handleService(
				ServiceShuntAdapter.HANDLE_DELETE,
				ServiceShuntAdapter.SERVICE_MODULE, shortName);
	}

	/**
	 * 根据组件短名获取组件信息bean
	 * 
	 * @param shortName:组件短名
	 * @return：成功返回组件信息bean，失败返回null
	 */
	private ModuleInfoBean getModuleBean(String shortName) {
		ModuleInfoBean bean = new ModuleInfoBean();
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT ml_shortname,ml_name,ml_kind,ml_type,ml_edition,")
				.append(
						"ml_log_level,ml_log_type,ml_class_name,ml_edition,ml_location FROM t_svg_module_list a ")
				.append("WHERE ml_shortname = '").append(shortName).append("'");

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		try {
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法获取业务服务组件清单！");
				return null;
			}

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				bean.setModuleShortName(rs.getString("ml_shortname"));
				bean.setName(rs.getString("ml_name"));
				bean.setLocation(rs.getString("ml_location"));
				bean.setKind(rs.getString("ml_kind"));
				bean.setType(rs.getString("ml_type"));
				bean.setLogLevel(rs.getString("ml_log_level"));
				bean.setLogType(rs.getString("ml_log_type"));
				bean.setClassName(rs.getString("ml_class_name"));
				bean.setEdition(rs.getString("ml_edition"));
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return null;
		} finally {
			// 关闭数据库连接
			controller.getDBManager().close(conn);
		}

		return bean;
	}

}
