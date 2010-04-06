package com.nci.svg.server.operinterface;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.nci.svg.sdk.bean.ModuleInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;
import com.nci.svg.sdk.server.operationinterface.OperInterfaceAdapter;
import com.nci.svg.sdk.server.operationinterface.OperInterfaceManagerAdapter;
import com.nci.svg.sdk.server.util.ServerUtilities;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2008-11-25
 * @功能：业务接口管理类
 * 
 */
public class OperInterfaceManagerImpl extends OperInterfaceManagerAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4602101711028754855L;
	private HashMap mapModuleInfo = null;
	/**
	 * 日志对象
	 */
	private static LoggerAdapter log;
	
	/**
	 * 业务接口对应清单
	 */
	private HashMap mapInterface = null;
	
	private static final String SEP_NAME = "_$@nci@$_";

	public OperInterfaceManagerImpl(HashMap parameters) {
		super(parameters);
	}

	public int init() {
		// 获取业务接口组件清单
		controller = (ServerModuleControllerAdapter) getParameter(ServerModuleControllerAdapter.class
				.toString());
		// 获取日志操作对象
		log = controller.getLogger();
		// 获取业务接口组件清单
		int ret = getOperInterfaceList();
		if (ret == OPER_ERROR) {
			log.log(this, LoggerAdapter.ERROR, "获取业务接口组件清单失败");
		}

		// 根据组件清单逐一下载组件包至本地
		if (mapModuleInfo.size() == 0) {
			log.log(this, LoggerAdapter.INFO, "组件清单为空，启动失败");
			return MODULE_INITIALIZE_COMPLETE;
		}

		// 获取本地存储路径信息
		String pathName = controller.getCachePath();
		Iterator iterator = mapModuleInfo.values().iterator();
		while (iterator.hasNext()) {
			ModuleInfoBean bean = (ModuleInfoBean) iterator.next();
			ret = downloadModuleJar(bean, pathName);
		}
		
		ret = getOperInterfaceServices();
		if (ret == OPER_ERROR) {
			log.log(this, LoggerAdapter.ERROR, "获取业务接口清单失败");
		}
		return super.init();
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

		String type = controller.getDBManager().getDBType("SVG");
		if (type != null) {
			// 通过方言转换类获取blob字段
			DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
					.getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return OPER_ERROR;
			// 获取数据库连接
			Connection conn = controller.getDBManager().getConnection("svg");
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法下载组件架包！");
				return OPER_ERROR;
			}
			// 获取组件内容
			byte[] content = sqlIdiom.getBlob(conn, sql.toString(),
					"ml_content");
			// 关闭数据库连接
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.log(this, LoggerAdapter.ERROR, "下载组件架包，关闭数据库连接失败！");
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
						controller.addClassPath(localJarFile);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return OPER_SUCCESS;
			}
		}
		return OPER_ERROR;
	}

	public OperInterfaceAdapter getOperationModule(String bussinessID, String actionName) {
		String className = getModuleClass(bussinessID, actionName);
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "获取对应的业务接口实现类不存在！");
			return null;
		}
		OperInterfaceAdapter operInterface = (OperInterfaceAdapter) ServerUtilities
		              .startModule(className, parameters);
		return operInterface;
	}
	
	/**
	 * 获取业务接口实现类名
	 * 
	 * @param bussinessID:业务系统编号
	 * @param actionName:请求命令名
	 * @return
	 */
	protected String getModuleClass(String bussinessID, String actionName) {
		String name = bussinessID.toUpperCase() + SEP_NAME + actionName.toUpperCase();

		return (String)mapInterface.get(name);
	}

	/**
	 * 根据传入的数据库连接，获取业务接口组件清单
	 * 
	 * @return
	 */
	protected int getOperInterfaceList() {
		mapModuleInfo = new HashMap();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ml_shortname, ml_name,").append(
				" ml_kind, ml_type, ml_edition,").append(
				" ml_log_level, ml_log_type, ml_class_name,").append(
				"ml_edition,ml_location FROM t_svg_module_list a ").append(
				"WHERE ml_location ='2' AND ml_kind = '2'").append(
				" AND ml_type = '22'");
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		try {
			
			if (conn == null) {
				System.out.println("无法获取数据库连接，无法获取业务接口组件清单！");
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
				String name = rs.getString("ml_shortname");
				mapModuleInfo.put(name, bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return OPER_ERROR;
		}
		finally
		{
			controller.getDBManager().close(conn);
		}

		if (mapModuleInfo.size() == 0)
			return OPER_ERROR;
		return OPER_SUCCESS;
	}
	
	/**
	 * 根据传入的数据库连接，获取业务接口组件清单
	 * 
	 * @return
	 */
	protected int getOperInterfaceServices() {
		mapInterface = new HashMap();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ms_service_class,ms_service_name,ms_buss_type FROM t_svg_module_service,t_svg_module_list").append(
		" WHERE ms_validity='1' AND ml_shortname= ms_short_name AND ml_location='2' ")
		.append("AND ml_kind='2' AND ml_type= '22' AND ms_buss_type is not null");
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		try {
			
			if (conn == null) {
				System.out.println("无法获取数据库连接，无法获取业务接口组件清单！");
				return OPER_ERROR;
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				String name = rs.getString("ms_service_name");
				String className = rs.getString("ms_service_class");
				String bussType = rs.getString("ms_buss_type");
				name = bussType.toUpperCase() + SEP_NAME +name.toUpperCase();
				mapInterface.put(name, className);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return OPER_ERROR;
		}
		finally
		{
			controller.getDBManager().close(conn);
		}

		return OPER_SUCCESS;
	}

	public ResultBean OperInterfaceMultiplexing(String bussinessID,
			String action, Map requestParams) {
		OperInterfaceAdapter operInterface = getOperationModule(bussinessID, action);
		if(operInterface == null)
		{
			ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR,"无法获取业务接口组件",null,null);
			return bean;
		}
		return operInterface.handleOper(action, requestParams);
	}


}
