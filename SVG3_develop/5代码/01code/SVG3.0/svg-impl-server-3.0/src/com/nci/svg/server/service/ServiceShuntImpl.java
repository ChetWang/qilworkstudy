package com.nci.svg.server.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.sdk.server.service.ServiceInfoBean;
import com.nci.svg.sdk.server.service.ServiceShuntAdapter;
import com.nci.svg.sdk.server.util.ServerUtilities;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：Servlet版业务服务路由实现类
 * 
 */
public class ServiceShuntImpl extends ServiceShuntAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 701401696505158466L;

	/**
	 * 基础请求参数名
	 */
	public final static String REQUEST_TYPE_NAME = "action";

	private HashMap mapService = null;
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

	public byte[] lock = new byte[0];

	/**
	 * 构成函数
	 * 
	 * @param parameters
	 */
	public ServiceShuntImpl(HashMap parameters) {
		super(parameters);
	}

	/**
	 * 将传入的参数转换成Servlet格式
	 */
	public ResultBean shuntService(Object request) {
		return shuntService((HttpServletRequest) request);

	}

	/**
	 * 业务路由处理，根据传入的请求包，提取相应的命令号，分发给各业务组件完成
	 * 
	 * @param request：请求包
	 * @param response：返回包
	 */
	public ResultBean shuntService(HttpServletRequest request) {
		String action = request.getParameter(REQUEST_TYPE_NAME).toUpperCase();
		ServiceInfoBean bean = (ServiceInfoBean) mapService.get(action.toUpperCase());
		ResultBean resultBean = null;
		try {

			// 不存在该服务号，则直接返回
			if (bean == null || bean.isStartFlag() == false) {
				resultBean = new ResultBean();
				resultBean.setReturnFlag(OPER_ERROR);
				resultBean.setErrorText("该服务暂时未提供");
				return resultBean;
			}

			// 构建服务提供类实例，并交由处理
			OperationServiceModuleAdapter service = (OperationServiceModuleAdapter) ServerUtilities
					.startModule(bean.getClassName(), parameters);
			Map paramMap = request.getParameterMap();
			if (service != null) {
				synchronized (lock) {
					bean.increaseCount();
				}
				resultBean = service.handleOper(action, paramMap);
				synchronized (lock) {
					bean.decreaseCount();
				}
				if (resultBean == null) {
					resultBean = new ResultBean();
					resultBean.setReturnFlag(OPER_ERROR);
					resultBean.setErrorText("该服务处理有误");
				}
			} else {
				resultBean = new ResultBean();
				resultBean.setReturnFlag(OPER_ERROR);
				resultBean.setErrorText("该服务对象实例有误");
			}

			return resultBean;

		} catch (Exception e) {
			log.log(this, LoggerAdapter.ERROR, e);
			e.printStackTrace();
			resultBean = null;
		}
		return resultBean;

	}

	public String getModuleID() {
		// TODO Auto-generated method stub
		return null;
	}

	public int init() {
		// 获取主管理组件对象
		controller = (ServerModuleControllerAdapter) getParameter(ServerModuleControllerAdapter.class
				.toString());
		// 获取日志操作对象
		log = controller.getLogger();
		// 获取业务服务清单
		int ret = getServiceList();
		if (ret < 0) {
			log.log(this, LoggerAdapter.ERROR, "获取业务服务失败");
			return MODULE_INITIALIZE_COMPLETE;
		}
		return super.init();
	}

	public int start() {
		// 校验有效的启动组件，将所有的服务置位为服务状态
		Iterator iterator = mapService.values().iterator();
		while (iterator.hasNext()) {
			ServiceInfoBean bean = (ServiceInfoBean) iterator.next();
			bean.setStartFlag(true);
		}
		return super.start();
	}

	/**
	 * 获取业务服务清单
	 * 
	 * @return：成功返回0，失败返回<0
	 */
	protected int getServiceList() {
		mapService = new HashMap();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.ml_shortname, b.ms_service_name,").append(
				" b.ms_service_class, b.ms_validity").append(
				" FROM t_svg_module_list a,t_svg_module_service b ").append(
				"WHERE a.ml_shortname = b.ms_short_name AND").append(
				" b.ms_validity = '1' AND ml_location ='2'").append(
				" AND b.ms_buss_type is null");
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法获取服务列表！");
			return OPER_ERROR;
		}
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				ServiceInfoBean bean = new ServiceInfoBean();
				bean.setShortName(rs.getString("ml_shortname"));
				bean.setServiceName(rs.getString("ms_service_name"));
				bean.setClassName(rs.getString("ms_service_class"));

				String validity = (String) rs.getString("ms_validity");
				if (validity.equals("1")) {
					bean.setStartFlag(true);
				} else {
					bean.setStartFlag(false);
				}
				mapService.put(rs.getString("ms_service_name").toUpperCase(),
						bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			// e.printStackTrace();
			return OPER_ERROR;
		} finally {
			controller.getDBManager().close(conn);
		}
		if (mapService.size() == 0)
			return OPER_ERROR;
		return OPER_SUCCESS;
	}

	public ResultBean serviceMultiplexing(String action, Map requestParams) {
		ServiceInfoBean bean = (ServiceInfoBean) mapService.get(action.toUpperCase());
		if (bean == null)
			return null;
		try {
			// 构建服务提供类实例，并交由处理
			OperationServiceModuleAdapter service = (OperationServiceModuleAdapter) ServerUtilities
					.startModule(bean.getClassName(), parameters);
			return service.handleOper(action, requestParams);

		} catch (Exception e) {
			log.log(this, LoggerAdapter.ERROR, e);
		}
		return null;
	}

	public ResultBean getServiceStatus(String serviceType, String name) {
		// TODO Auto-generated method stub
		HashMap map = new HashMap();
		ResultBean bean = null;
		// 获取单个action服务状态
		if (serviceType.equals(SERVICE_ACTION)) {
			ServiceInfoBean sbean = (ServiceInfoBean) mapService.get(name);
			if (sbean == null) {
				bean = new ResultBean(ResultBean.RETURN_ERROR, "无此服务", null, null);
			} else {
				map.put(name, sbean.getStartFlag());
				bean = new ResultBean(ResultBean.RETURN_SUCCESS, null, "HashMap", map);
			}
		} else if (serviceType.equals(SERVICE_MODULE)) {
			// 获取一个集成服务包的状态
			Iterator iterator = mapService.values().iterator();
			while (iterator.hasNext()) {
				ServiceInfoBean sbean = (ServiceInfoBean) iterator.next();
				if (sbean.getShortName().equals(name)) {
					map.put(sbean.getServiceName(), sbean.getStartFlag());
				}
			}
			bean = new ResultBean(OPER_SUCCESS, null, "HashMap", map);
		} else {
			// 获取所有服务的状态
			Iterator iterator = mapService.values().iterator();
			while (iterator.hasNext()) {
				ServiceInfoBean sbean = (ServiceInfoBean) iterator.next();
				map.put(sbean.getServiceName(), sbean.getStartFlag());
			}
			bean = new ResultBean(OPER_SUCCESS, null, "HashMap", map);
		}
		return bean;
	}

	public ResultBean handleService(String handleType, String serviceType,
			String name) {
		int ret = OPER_SUCCESS;
		ResultBean bean = null;
		if (handleType.equals(HANDLE_START)) {
			ret = handleServiceStatus(serviceType, name, true);
		} else if (handleType.equals(HANDLE_STOP)) {
			ret = handleServiceStatus(serviceType, name, false);
		} else if (handleType.equals(HANDLE_ADD)) {
			ret = addService(serviceType, name);
		} else if (handleType.equals(HANDLE_MODIFY)) {
			ret = modifyService(serviceType, name);
		} else if (handleType.equals(HANDLE_DELETE)) {
			ret = deleteService(serviceType, name);
		}
		bean = new ResultBean(ret, null, null, null);
		return bean;
	}

	/**
	 * 根据传入的参数，设置服务状态
	 * 
	 * @param serviceType：服务类型，SERVICE_MODULE，SERVICE_ACTION，SERVICE_ALL
	 * @param name：对应的名称
	 * @param flag：true表示启动，false表示停止
	 * @return：设置结果
	 */
	public int handleServiceStatus(String serviceType, String name, boolean flag) {
		ServiceInfoBean sbean = null;
		if (serviceType.equals(SERVICE_ACTION)) {
			sbean = (ServiceInfoBean) mapService.get(name);
			if (sbean == null)
				return OPER_ERROR;
			else
				sbean.setStartFlag(flag);
		} else if (serviceType.equals(SERVICE_MODULE)) {
			Iterator iterator = mapService.values().iterator();
			while (iterator.hasNext()) {
				sbean = (ServiceInfoBean) iterator.next();
				if (sbean.getShortName().equals(name)) {
					sbean.setStartFlag(flag);
				}
			}
		} else if (serviceType.equals(SERVICE_ALL)) {
			Iterator iterator = mapService.values().iterator();
			while (iterator.hasNext()) {
				sbean = (ServiceInfoBean) iterator.next();
				sbean.setStartFlag(flag);
			}
		}
		return OPER_SUCCESS;
	}

	/**
	 * 增加服务
	 * 
	 * @param serviceType：服务类型，SERVICE_MODULE，SERVICE_ACTION，SERVICE_ALL
	 * @param name：对应的名称
	 * @return：增加结果
	 */
	private int addService(String serviceType, String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.ml_shortname, b.ms_service_name,").append(
				" b.ms_service_class").append(
				" FROM t_svg_module_list a,t_svg_module_service b ").append(
				"WHERE a.ml_shortname = b.ms_short_name AND").append(
				" b.ms_validity = '1' AND ml_location ='2'").append(
				" AND b.ms_buss_type is null ");
		if (serviceType.equals(SERVICE_MODULE)) {
			sql.append("AND b.ms_short_name = '").append(name).append("'");
		} else if (serviceType.equals(SERVICE_ACTION)) {
			sql.append("AND b.ms_service_name = '").append(name).append("'");
		} else {
			return OPER_ERROR;
		}

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		try {

			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法获取服务列表！");
				return OPER_ERROR;
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				ServiceInfoBean bean = new ServiceInfoBean();
				bean.setShortName(rs.getString("ml_shortname"));
				bean.setServiceName(rs.getString("ms_service_name"));
				bean.setClassName(rs.getString("ms_service_class"));
				bean.setStartFlag(false);
				mapService.put(rs.getString("ms_service_name").toUpperCase(),
						bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			// e.printStackTrace();
			return OPER_ERROR;
		} finally {
			controller.getDBManager().close(conn);
		}
		handleServiceStatus(serviceType, name, true);
		return OPER_SUCCESS;
	}

	/**
	 * 维护服务
	 * 
	 * @param serviceType：服务类型，SERVICE_MODULE，SERVICE_ACTION，SERVICE_ALL
	 * @param name：对应的名称
	 * @return：维护结果
	 */
	private int modifyService(String serviceType, String name) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.ml_shortname, b.ms_service_name,").append(
				" b.ms_service_class").append(
				" FROM t_svg_module_list a,t_svg_module_service b ").append(
				"WHERE a.ml_shortname = b.ms_short_name AND").append(
				" b.ms_validity = '1' AND ml_location ='2'").append(
				" AND b.ms_buss_type is null ");
		if (serviceType.equals(SERVICE_MODULE)) {
			sql.append("AND b.ms_short_name = '").append(name).append("'");
		} else if (serviceType.equals(SERVICE_ACTION)) {
			sql.append("AND b.ms_service_name = '").append(name).append("'");
		} else {
			return OPER_ERROR;
		}
		handleServiceStatus(serviceType, name, false);
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		try {

			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "无法获取数据库连接，无法获取服务列表！");
				return OPER_ERROR;
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				ServiceInfoBean bean = new ServiceInfoBean();
				bean.setShortName(rs.getString("ml_shortname"));
				bean.setServiceName(rs.getString("ms_service_name"));
				bean.setClassName(rs.getString("ms_service_class"));
				bean.setStartFlag(false);
				mapService.put(rs.getString("ms_service_name").toUpperCase(),
						bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			// e.printStackTrace();
			return OPER_ERROR;
		} finally {
			controller.getDBManager().close(conn);
		}
		handleServiceStatus(serviceType, name, true);
		return OPER_SUCCESS;
	}

	/**
	 * 删除服务
	 * 
	 * @param serviceType：服务类型，SERVICE_MODULE，SERVICE_ACTION，SERVICE_ALL
	 * @param name：对应的名称
	 * @return：删除结果
	 */
	private int deleteService(String serviceType, String name) {
		handleServiceStatus(serviceType, name, false);
		ServiceInfoBean sbean = null;
		if (serviceType.equals(SERVICE_ACTION)) {
			sbean = (ServiceInfoBean) mapService.get(name);
			if (sbean == null)
				return OPER_ERROR;
			else
				mapService.remove(name);
		} else if (serviceType.equals(SERVICE_MODULE)) {
			Iterator iterator = mapService.values().iterator();
			while (iterator.hasNext()) {
				sbean = (ServiceInfoBean) iterator.next();
				if (sbean.getShortName().equals(name)) {
					// FIXME:目前缺少无调用判定
					mapService.remove(sbean.getServiceName());
				}
			}
		} else {
			return OPER_ERROR;
		}
		return OPER_SUCCESS;
	}

}
